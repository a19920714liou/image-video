package com.bjhit.video.containers;

import java.io.IOException;

import com.bjhit.video.common.NIOUtils;
import com.bjhit.video.common.SeekableByteChannel;
import com.bjhit.video.containers.boxes.AliasBox;
import com.bjhit.video.containers.boxes.ChunkOffsets64Box;
import com.bjhit.video.containers.boxes.ChunkOffsetsBox;
import com.bjhit.video.containers.boxes.DataInfoBox;
import com.bjhit.video.containers.boxes.DataRefBox;
import com.bjhit.video.containers.boxes.MediaInfoBox;
import com.bjhit.video.containers.boxes.NodeBox;
import com.bjhit.video.containers.boxes.SampleEntry;
import com.bjhit.video.containers.boxes.TrakBox;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:53:51
 * @version 1.0
 */
public class ChunkWriter {
    private long[] offsets;
    private SampleEntry[] entries;
    private SeekableByteChannel[] inputs;
    private int curChunk;
    private SeekableByteChannel out;
    byte[] buf = new byte[8092];
    private TrakBox trak;

    public ChunkWriter(TrakBox trak, SeekableByteChannel[] inputs, SeekableByteChannel out) {
        entries = NodeBox.findAll(trak, SampleEntry.class, "mdia", "minf", "stbl", "stsd", null);
        ChunkOffsetsBox stco = NodeBox.findFirst(trak, ChunkOffsetsBox.class, "mdia", "minf", "stbl", "stco");
        ChunkOffsets64Box co64 = NodeBox.findFirst(trak, ChunkOffsets64Box.class, "mdia", "minf", "stbl", "co64");
        int size;
        if (stco != null)
            size = stco.getChunkOffsets().length;
        else
            size = co64.getChunkOffsets().length;
        this.inputs = inputs;

        offsets = new long[size];
        this.out = out;
        this.trak = trak;
    }

    public void apply() {
        NodeBox stbl = NodeBox.findFirst(trak, NodeBox.class, "mdia", "minf", "stbl");
        stbl.removeChildren("stco", "co64");

        stbl.add(new ChunkOffsets64Box(offsets));
        cleanDrefs(trak);
    }

    private void cleanDrefs(TrakBox trak) {
        MediaInfoBox minf = trak.getMdia().getMinf();
        DataInfoBox dinf = trak.getMdia().getMinf().getDinf();
        if (dinf == null) {
            dinf = new DataInfoBox();
            minf.add(dinf);
        }

        DataRefBox dref = dinf.getDref();
        if (dref == null) {
            dref = new DataRefBox();
            dinf.add(dref);
        }

        dref.getBoxes().clear();
        dref.add(AliasBox.createSelfRef());

        for (SampleEntry entry : NodeBox.findAll(trak, SampleEntry.class, "mdia", "minf", "stbl", "stsd", null)) {
            entry.setDrefInd((short) 1);
        }
    }

    private SeekableByteChannel getInput(Chunk chunk) {
        SampleEntry se = entries[chunk.getEntry() - 1];
        return inputs[se.getDrefInd() - 1];
    }

    public void write(Chunk chunk) throws IOException {
        SeekableByteChannel input = getInput(chunk);
        input.position(chunk.getOffset());
        long pos = out.position();

        out.write(NIOUtils.fetchFrom(input, (int) chunk.getSize()));
        offsets[curChunk++] = pos;
    }
}