package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * Box type
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:21:19
 * @version 1.0
 */
public class ChunkOffsets64Box extends FullBox {
    private long[] chunkOffsets;
    
    public static String fourcc() {
        return "co64";
    }

    public ChunkOffsets64Box(Header atom) {
        super(atom);
    }
    
    public ChunkOffsets64Box() {
        super(new Header(fourcc(), 0));
    }

    public ChunkOffsets64Box(long[] offsets) {
        this();
        this.chunkOffsets = offsets;
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        int length = input.getInt();
        chunkOffsets = new long[length];
        for (int i = 0; i < length; i++) {
            chunkOffsets[i] = input.getLong();
        }
    }

    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(chunkOffsets.length);
        for (long offset : chunkOffsets) {
            out.putLong(offset);
        }
    }
    
    public long[] getChunkOffsets() {
        return chunkOffsets;
    }

    public void setChunkOffsets(long[] chunkOffsets) {
        this.chunkOffsets = chunkOffsets;
    }
}
