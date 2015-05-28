package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:22:11
 * @version 1.0
 */
public class CompositionOffsetsBox extends FullBox {

    private Entry[] entries;

    public static class Entry {
        public int count;
        public int offset;

        public Entry(int count, int offset) {
            this.count = count;
            this.offset = offset;
        }

        public int getCount() {
            return count;
        }

        public int getOffset() {
            return offset;
        }
    }

    public CompositionOffsetsBox() {
        super(new Header(fourcc()));
    }

    public CompositionOffsetsBox(Entry[] entries) {
        super(new Header(fourcc()));
        this.entries = entries;
    }

    public static String fourcc() {
        return "ctts";
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        int num = input.getInt();

        entries = new Entry[num];
        for (int i = 0; i < num; i++) {
            entries[i] = new Entry(input.getInt(), input.getInt());
        }
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);

        out.putInt(entries.length);
        for (int i = 0; i < entries.length; i++) {
            out.putInt(entries[i].count);
            out.putInt(entries[i].offset);
        }
    }

    public Entry[] getEntries() {
        return entries;
    }
}