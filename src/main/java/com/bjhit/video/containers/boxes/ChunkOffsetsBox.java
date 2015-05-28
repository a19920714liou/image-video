package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * A box to hold chunk offsets
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:21:31
 * @version 1.0
 */

public class ChunkOffsetsBox extends FullBox {
    private long[] chunkOffsets;
    
    public static String fourcc() {
        return "stco";
    }

    public ChunkOffsetsBox(long[] chunkOffsets) {
        super(new Header(fourcc()));
        this.chunkOffsets = chunkOffsets;
    }

    public ChunkOffsetsBox() {
        super(new Header(fourcc()));
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        int length = input.getInt();
        chunkOffsets = new long[length];
        for (int i = 0; i < length; i++) {
            chunkOffsets[i] = input.getInt() & 0xffffffffL;
        }
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(chunkOffsets.length);
        for (long offset : chunkOffsets) {
            out.putInt((int) offset);
        }
    }

    public long[] getChunkOffsets() {
        return chunkOffsets;
    }

    public void setChunkOffsets(long[] chunkOffsets) {
        this.chunkOffsets = chunkOffsets;
    }
}
