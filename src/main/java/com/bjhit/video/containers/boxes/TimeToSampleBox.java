package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * A box containing sample presentation time information
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:29:36
 * @version 1.0
 */
public class TimeToSampleBox extends FullBox {

    public static class TimeToSampleEntry {
        int sampleCount;
        int sampleDuration;

        public TimeToSampleEntry(int sampleCount, int sampleDuration) {
            this.sampleCount = sampleCount;
            this.sampleDuration = sampleDuration;
        }

        public int getSampleCount() {
            return sampleCount;
        }

        public int getSampleDuration() {
            return sampleDuration;
        }

        public void setSampleDuration(int sampleDuration) {
            this.sampleDuration = sampleDuration;
        }

        public void setSampleCount(int sampleCount) {
            this.sampleCount = sampleCount;
        }

        public long getSegmentDuration() {
            return sampleCount * sampleDuration;
        }
    }

    public static String fourcc() {
        return "stts";
    }

    private TimeToSampleEntry[] entries;

    public TimeToSampleBox(TimeToSampleEntry[] timeToSamples) {
        super(new Header(fourcc()));
        this.entries = timeToSamples;
    }

    public TimeToSampleBox() {
        super(new Header(fourcc()));
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        int foo = input.getInt();
        entries = new TimeToSampleEntry[foo];
        for (int i = 0; i < foo; i++) {
            entries[i] = new TimeToSampleEntry(input.getInt(), input.getInt());
        }
    }

    public TimeToSampleEntry[] getEntries() {
        return entries;
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(entries.length);
        for (TimeToSampleEntry timeToSampleEntry : entries) {
            out.putInt(timeToSampleEntry.getSampleCount());
            out.putInt(timeToSampleEntry.getSampleDuration());
        }
    }

    public void setEntries(TimeToSampleEntry[] entries) {
        this.entries = entries;
    }
}