package com.bjhit.video.containers.muxer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bjhit.video.common.Assert;
import com.bjhit.video.common.IntArrayList;
import com.bjhit.video.common.LongArrayList;
import com.bjhit.video.common.SeekableByteChannel;
import com.bjhit.video.common.model.Rational;
import com.bjhit.video.common.model.Size;
import com.bjhit.video.common.model.Unit;
import com.bjhit.video.containers.MP4Packet;
import com.bjhit.video.containers.TrackType;
import com.bjhit.video.containers.boxes.Box;
import com.bjhit.video.containers.boxes.ChunkOffsets64Box;
import com.bjhit.video.containers.boxes.CompositionOffsetsBox;
import com.bjhit.video.containers.boxes.Edit;
import com.bjhit.video.containers.boxes.HandlerBox;
import com.bjhit.video.containers.boxes.Header;
import com.bjhit.video.containers.boxes.MediaBox;
import com.bjhit.video.containers.boxes.MediaHeaderBox;
import com.bjhit.video.containers.boxes.MediaInfoBox;
import com.bjhit.video.containers.boxes.MovieHeaderBox;
import com.bjhit.video.containers.boxes.NodeBox;
import com.bjhit.video.containers.boxes.SampleDescriptionBox;
import com.bjhit.video.containers.boxes.SampleEntry;
import com.bjhit.video.containers.boxes.SampleSizesBox;
import com.bjhit.video.containers.boxes.SampleToChunkBox;
import com.bjhit.video.containers.boxes.SyncSamplesBox;
import com.bjhit.video.containers.boxes.TimeToSampleBox;
import com.bjhit.video.containers.boxes.TrackHeaderBox;
import com.bjhit.video.containers.boxes.TrakBox;
import com.bjhit.video.containers.boxes.CompositionOffsetsBox.Entry;
import com.bjhit.video.containers.boxes.SampleToChunkBox.SampleToChunkEntry;
import com.bjhit.video.containers.boxes.TimeToSampleBox.TimeToSampleEntry;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:52:37
 * @version 1.0
 */
public class FramesMP4MuxerTrack extends AbstractMP4MuxerTrack {

    private List<TimeToSampleEntry> sampleDurations = new ArrayList<TimeToSampleEntry>();
    private long sameDurCount = 0;
    private long curDuration = -1;

    private LongArrayList chunkOffsets = new LongArrayList();
    private IntArrayList sampleSizes = new IntArrayList();
    private IntArrayList iframes = new IntArrayList();

    private List<Entry> compositionOffsets = new ArrayList<Entry>();
    private int lastCompositionOffset = 0;
    private int lastCompositionSamples = 0;
    private long ptsEstimate = 0;

    private int lastEntry = -1;

    private long trackTotalDuration;
    private int curFrame;
    private boolean allIframes = true;
    private TimecodeMP4MuxerTrack timecodeTrack;
    private SeekableByteChannel out;

    public FramesMP4MuxerTrack(SeekableByteChannel out, int trackId, TrackType type, int timescale) {
        super(trackId, type, timescale);
        
        this.out = out;

        setTgtChunkDuration(new Rational(1, 1), Unit.FRAME);
    }

    public void addFrame(MP4Packet pkt) throws IOException {
        if (finished)
            throw new IllegalStateException("The muxer track has finished muxing");
        int entryNo = pkt.getEntryNo() + 1;

        int compositionOffset = (int) (pkt.getPts() - ptsEstimate);
        if (compositionOffset != lastCompositionOffset) {
            if (lastCompositionSamples > 0)
                compositionOffsets.add(new Entry(lastCompositionSamples, lastCompositionOffset));
            lastCompositionOffset = compositionOffset;
            lastCompositionSamples = 0;
        }
        lastCompositionSamples++;
        ptsEstimate += pkt.getDuration();

        if (lastEntry != -1 && lastEntry != entryNo) {
            outChunk(lastEntry);
            samplesInLastChunk = -1;
        }

        curChunk.add(pkt.getData());

        if (pkt.isKeyFrame())
            iframes.add(curFrame + 1);
        else
            allIframes = false;

        curFrame++;

        chunkDuration += pkt.getDuration();
        if (curDuration != -1 && pkt.getDuration() != curDuration) {
            sampleDurations.add(new TimeToSampleEntry((int) sameDurCount, (int) curDuration));
            sameDurCount = 0;
        }
        curDuration = pkt.getDuration();
        sameDurCount++;
        trackTotalDuration += pkt.getDuration();

        outChunkIfNeeded(entryNo);

        processTimecode(pkt);

        lastEntry = entryNo;
    }

    private void processTimecode(MP4Packet pkt) throws IOException {
        if (timecodeTrack != null)
            timecodeTrack.addTimecode(pkt);
    }

    private void outChunkIfNeeded(int entryNo) throws IOException {
        Assert.assertTrue(tgtChunkDurationUnit == Unit.FRAME || tgtChunkDurationUnit == Unit.SEC);

        if (tgtChunkDurationUnit == Unit.FRAME
                && curChunk.size() * tgtChunkDuration.getDen() == tgtChunkDuration.getNum()) {
            outChunk(entryNo);
        } else if (tgtChunkDurationUnit == Unit.SEC && chunkDuration > 0
                && chunkDuration * tgtChunkDuration.getDen() >= tgtChunkDuration.getNum() * timescale) {
            outChunk(entryNo);
        }
    }

    void outChunk(int entryNo) throws IOException {
        if (curChunk.size() == 0)
            return;

        chunkOffsets.add(out.position());

        for (ByteBuffer bs : curChunk) {
            sampleSizes.add(bs.remaining());
            out.write(bs);
        }

        if (samplesInLastChunk == -1 || samplesInLastChunk != curChunk.size()) {
            samplesInChunks.add(new SampleToChunkEntry(chunkNo + 1, curChunk.size(), entryNo));
        }
        samplesInLastChunk = curChunk.size();
        chunkNo++;

        chunkDuration = 0;
        curChunk.clear();
    }

    protected Box finish(MovieHeaderBox mvhd) throws IOException {
        if (finished)
            throw new IllegalStateException("The muxer track has finished muxing");

        outChunk(lastEntry);

        if (sameDurCount > 0) {
            sampleDurations.add(new TimeToSampleEntry((int) sameDurCount, (int) curDuration));
        }
        finished = true;

        TrakBox trak = new TrakBox();
        Size dd = getDisplayDimensions();
        TrackHeaderBox tkhd = new TrackHeaderBox(trackId, ((long) mvhd.getTimescale() * trackTotalDuration)
                / timescale, dd.getWidth(), dd.getHeight(), new Date().getTime(), new Date().getTime(), 1.0f,
                (short) 0, 0, new int[] { 0x10000, 0, 0, 0, 0x10000, 0, 0, 0, 0x40000000 });
        tkhd.setFlags(0xf);
        trak.add(tkhd);

        tapt(trak);

        MediaBox media = new MediaBox();
        trak.add(media);
        media.add(new MediaHeaderBox(timescale, trackTotalDuration, 0, new Date().getTime(), new Date().getTime(),
                0));

        HandlerBox hdlr = new HandlerBox("mhlr", type.getHandler(), "appl", 0, 0);
        media.add(hdlr);

        MediaInfoBox minf = new MediaInfoBox();
        media.add(minf);
        mediaHeader(minf, type);
        minf.add(new HandlerBox("dhlr", "url ", "appl", 0, 0));
        addDref(minf);
        NodeBox stbl = new NodeBox(new Header("stbl"));
        minf.add(stbl);

        putCompositionOffsets(stbl);
        putEdits(trak);
        putName(trak);

        stbl.add(new SampleDescriptionBox(sampleEntries.toArray(new SampleEntry[0])));
        stbl.add(new SampleToChunkBox(samplesInChunks.toArray(new SampleToChunkEntry[0])));
        stbl.add(new SampleSizesBox(sampleSizes.toArray()));
        stbl.add(new TimeToSampleBox(sampleDurations.toArray(new TimeToSampleEntry[] {})));
        stbl.add(new ChunkOffsets64Box(chunkOffsets.toArray()));
        if (!allIframes && iframes.size() > 0)
            stbl.add(new SyncSamplesBox(iframes.toArray()));

        return trak;
    }

    private void putCompositionOffsets(NodeBox stbl) {
        if (compositionOffsets.size() > 0) {
            compositionOffsets.add(new Entry(lastCompositionSamples, lastCompositionOffset));

            int min = minOffset(compositionOffsets);
            if (min > 0) {
                for (Entry entry : compositionOffsets) {
                    entry.offset -= min;
                }
            }

            Entry first = compositionOffsets.get(0);
            if (first.getOffset() > 0) {
                if (edits == null) {
                    edits = new ArrayList<Edit>();
                    edits.add(new Edit(trackTotalDuration, first.getOffset(), 1.0f));
                } else {
                    for (Edit edit : edits) {
                        edit.setMediaTime(edit.getMediaTime() + first.getOffset());
                    }
                }
            }

            stbl.add(new CompositionOffsetsBox(compositionOffsets.toArray(new Entry[0])));
        }
    }

    public static int minOffset(List<Entry> offs) {
        int min = Integer.MAX_VALUE;
        for (Entry entry : offs) {
            if (entry.getOffset() < min)
                min = entry.getOffset();
        }
        return min;
    }

    public long getTrackTotalDuration() {
        return trackTotalDuration;
    }

    public void addSampleEntries(SampleEntry[] sampleEntries) {
        for (SampleEntry se : sampleEntries) {
            addSampleEntry(se);
        }
    }

    public TimecodeMP4MuxerTrack getTimecodeTrack() {
        return timecodeTrack;
    }

    public void setTimecode(TimecodeMP4MuxerTrack timecodeTrack) {
        this.timecodeTrack = timecodeTrack;
    }
    
    @Override
    public int getTotalFrames() {
    	// TODO Auto-generated method stub
    	return curFrame;
    }
}