package com.bjhit.video.containers;

import static java.util.Arrays.copyOfRange;

import com.bjhit.video.containers.boxes.AudioSampleEntry;
import com.bjhit.video.containers.boxes.Box;
import com.bjhit.video.containers.boxes.ChunkOffsets64Box;
import com.bjhit.video.containers.boxes.ChunkOffsetsBox;
import com.bjhit.video.containers.boxes.NodeBox;
import com.bjhit.video.containers.boxes.SampleDescriptionBox;
import com.bjhit.video.containers.boxes.SampleSizesBox;
import com.bjhit.video.containers.boxes.SampleToChunkBox;
import com.bjhit.video.containers.boxes.TimeToSampleBox;
import com.bjhit.video.containers.boxes.TrakBox;
import com.bjhit.video.containers.boxes.SampleToChunkBox.SampleToChunkEntry;
import com.bjhit.video.containers.boxes.TimeToSampleBox.TimeToSampleEntry;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:53:44
 * @version 1.0
 */
public class ChunkReader {
    private int curChunk;
    private int sampleNo;
    private int s2cIndex;
    private int ttsInd = 0;
    private int ttsSubInd = 0;
    private long chunkTv = 0;
    private long[] chunkOffsets;
    private SampleToChunkEntry[] sampleToChunk;
    private SampleSizesBox stsz;
    private TimeToSampleEntry[] tts;
    private SampleDescriptionBox stsd;

    public ChunkReader(TrakBox trakBox) {
        TimeToSampleBox stts = NodeBox.findFirst(trakBox, TimeToSampleBox.class, "mdia", "minf", "stbl", "stts");
        tts = stts.getEntries();
        ChunkOffsetsBox stco = NodeBox.findFirst(trakBox, ChunkOffsetsBox.class, "mdia", "minf", "stbl", "stco");
        ChunkOffsets64Box co64 = NodeBox.findFirst(trakBox, ChunkOffsets64Box.class, "mdia", "minf", "stbl", "co64");
        stsz = NodeBox.findFirst(trakBox, SampleSizesBox.class, "mdia", "minf", "stbl", "stsz");
        SampleToChunkBox stsc = NodeBox.findFirst(trakBox, SampleToChunkBox.class, "mdia", "minf", "stbl", "stsc");

        if (stco != null)
            chunkOffsets = stco.getChunkOffsets();
        else
            chunkOffsets = co64.getChunkOffsets();
        sampleToChunk = stsc.getSampleToChunk();
        stsd = NodeBox.findFirst(trakBox, SampleDescriptionBox.class, "mdia", "minf", "stbl", "stsd");
    }

    public boolean hasNext() {
        return curChunk < chunkOffsets.length;
    }

    public Chunk next() {
        if (curChunk >= chunkOffsets.length)
            return null;

        if (s2cIndex + 1 < sampleToChunk.length && curChunk + 1 == sampleToChunk[s2cIndex + 1].getFirst())
            s2cIndex++;
        int sampleCount = sampleToChunk[s2cIndex].getCount();

        int[] samplesDur = null;
        int sampleDur = 0;
        if (ttsSubInd + sampleCount <= tts[ttsInd].getSampleCount()) {
            sampleDur = tts[ttsInd].getSampleDuration();
            ttsSubInd += sampleCount;
        } else {
            samplesDur = new int[sampleCount];
            for (int i = 0; i < sampleCount; i++) {
                if (ttsSubInd >= tts[ttsInd].getSampleCount() && ttsInd < tts.length - 1) {
                    ttsSubInd = 0;
                    ++ttsInd;
                }
                samplesDur[i] = tts[ttsInd].getSampleDuration();
                ++ttsSubInd;
            }
        }

        int size = 0;
        int[] sizes = null;
        if (stsz.getDefaultSize() > 0) {
            size = getFrameSize();
        } else {
            sizes = copyOfRange(stsz.getSizes(), sampleNo, sampleNo + sampleCount);
        }

        int dref = sampleToChunk[s2cIndex].getEntry();
        Chunk chunk = new Chunk(chunkOffsets[curChunk], chunkTv, sampleCount, size, sizes, sampleDur, samplesDur, dref);

        chunkTv += chunk.getDuration();
        sampleNo += sampleCount;
        ++curChunk;
        return chunk;
    }

    private int getFrameSize() {
        int size = stsz.getDefaultSize();
        Box box = stsd.getBoxes().get(sampleToChunk[s2cIndex].getEntry() - 1);
        if (box instanceof AudioSampleEntry) {
            return ((AudioSampleEntry) box).calcFrameSize();
        }
        return size;
    }

    public int size() {
        return chunkOffsets.length;
    }
}