package com.bjhit.video.containers.muxer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import com.bjhit.video.common.Assert;
import com.bjhit.video.common.LongArrayList;
import com.bjhit.video.common.SeekableByteChannel;
import com.bjhit.video.common.model.Rational;
import com.bjhit.video.common.model.Size;
import com.bjhit.video.common.model.Unit;
import com.bjhit.video.containers.TrackType;
import com.bjhit.video.containers.boxes.Box;
import com.bjhit.video.containers.boxes.ChunkOffsets64Box;
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
import com.bjhit.video.containers.boxes.TimeToSampleBox;
import com.bjhit.video.containers.boxes.TrackHeaderBox;
import com.bjhit.video.containers.boxes.TrakBox;
import com.bjhit.video.containers.boxes.SampleToChunkBox.SampleToChunkEntry;
import com.bjhit.video.containers.boxes.TimeToSampleBox.TimeToSampleEntry;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:53:07
 * @version 1.0
 */
public class PCMMP4MuxerTrack extends AbstractMP4MuxerTrack {

    private int frameDuration;
    private int frameSize;
    private int framesInCurChunk;

    private LongArrayList chunkOffsets = new LongArrayList();
    private int totalFrames;
    private SeekableByteChannel out;

    public PCMMP4MuxerTrack(SeekableByteChannel out, int trackId, TrackType type, int timescale, int frameDuration, int frameSize,
            SampleEntry se) {
        super(trackId, type, timescale);
        this.out = out;
        this.frameDuration = frameDuration;
        this.frameSize = frameSize;
        addSampleEntry(se);

        setTgtChunkDuration(new Rational(1, 2), Unit.SEC);
    }

    public void addSamples(ByteBuffer buffer) throws IOException {
        curChunk.add(buffer);

        int frames = buffer.remaining() / frameSize;
        totalFrames += frames;

        framesInCurChunk += frames;
        chunkDuration += frames * frameDuration;

        outChunkIfNeeded();
    }

    private void outChunkIfNeeded() throws IOException {
        Assert.assertTrue(tgtChunkDurationUnit == Unit.FRAME || tgtChunkDurationUnit == Unit.SEC);

        if (tgtChunkDurationUnit == Unit.FRAME
                && framesInCurChunk * tgtChunkDuration.getDen() == tgtChunkDuration.getNum()) {
            outChunk();
        } else if (tgtChunkDurationUnit == Unit.SEC && chunkDuration > 0
                && chunkDuration * tgtChunkDuration.getDen() >= tgtChunkDuration.getNum() * timescale) {
            outChunk();
        }
    }

    private void outChunk() throws IOException {
        if (framesInCurChunk == 0)
            return;

        chunkOffsets.add(out.position());

        for (ByteBuffer b : curChunk) {
            out.write(b);
        }
        curChunk.clear();

        if (samplesInLastChunk == -1 || framesInCurChunk != samplesInLastChunk) {
            samplesInChunks.add(new SampleToChunkEntry(chunkNo + 1, framesInCurChunk, 1));
        }
        samplesInLastChunk = framesInCurChunk;

        chunkNo++;

        framesInCurChunk = 0;
        chunkDuration = 0;
    }

    protected Box finish(MovieHeaderBox mvhd) throws IOException {
        if (finished)
            throw new IllegalStateException("The muxer track has finished muxing");

        outChunk();

        finished = true;

        TrakBox trak = new TrakBox();
        Size dd = getDisplayDimensions();
        TrackHeaderBox tkhd = new TrackHeaderBox(trackId,
                ((long) mvhd.getTimescale() * totalFrames * frameDuration) / timescale, dd.getWidth(),
                dd.getHeight(), new Date().getTime(), new Date().getTime(), 1.0f, (short) 0, 0, new int[] {
                        0x10000, 0, 0, 0, 0x10000, 0, 0, 0, 0x40000000 });
        tkhd.setFlags(0xf);
        trak.add(tkhd);

        tapt(trak);

        MediaBox media = new MediaBox();
        trak.add(media);
        media.add(new MediaHeaderBox(timescale, totalFrames * frameDuration, 0, new Date().getTime(), new Date()
                .getTime(), 0));

        HandlerBox hdlr = new HandlerBox("mhlr", type.getHandler(), "appl", 0, 0);
        media.add(hdlr);

        MediaInfoBox minf = new MediaInfoBox();
        media.add(minf);
        mediaHeader(minf, type);
        minf.add(new HandlerBox("dhlr", "url ", "appl", 0, 0));
        addDref(minf);
        NodeBox stbl = new NodeBox(new Header("stbl"));
        minf.add(stbl);

        putEdits(trak);
        putName(trak);

        stbl.add(new SampleDescriptionBox(sampleEntries.toArray(new SampleEntry[0])));
        stbl.add(new SampleToChunkBox(samplesInChunks.toArray(new SampleToChunkEntry[0])));
        stbl.add(new SampleSizesBox(frameSize, totalFrames));
        stbl.add(new TimeToSampleBox(new TimeToSampleEntry[] { new TimeToSampleEntry(totalFrames, frameDuration) }));
        stbl.add(new ChunkOffsets64Box(chunkOffsets.toArray()));

        return trak;
    }

    public long getTrackTotalDuration() {
        return totalFrames * frameDuration;
    }
    
    @Override
    public int getTotalFrames() {
    	return totalFrames;
    }
}