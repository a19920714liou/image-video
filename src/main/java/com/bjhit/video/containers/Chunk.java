package com.bjhit.video.containers;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-3-2 下午3:00:07
 * @version 1.0
 */
public class Chunk {
    private long offset;
    private long startTv;
    private int sampleCount;
    private int sampleSize;
    private int sampleSizes[];
    private int sampleDur;
    private int sampleDurs[];
    private int entry;

    public Chunk(long offset, long startTv, int sampleCount, int sampleSize, int[] sampleSizes, int sampleDur,
            int[] sampleDurs, int entry) {
        this.offset = offset;
        this.startTv = startTv;
        this.sampleCount = sampleCount;
        this.sampleSize = sampleSize;
        this.sampleSizes = sampleSizes;
        this.sampleDur = sampleDur;
        this.sampleDurs = sampleDurs;
        this.entry = entry;
    }

    public long getOffset() {
        return offset;
    }

    public long getStartTv() {
        return startTv;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public int[] getSampleSizes() {
        return sampleSizes;
    }

    public int getSampleDur() {
        return sampleDur;
    }

    public int[] getSampleDurs() {
        return sampleDurs;
    }

    public int getEntry() {
        return entry;
    }

    public int getDuration() {
        if (sampleDur > 0)
            return sampleDur * sampleCount;
        int sum = 0;
        for (int i : sampleDurs) {
            sum += i;
        }
        return sum;
    }

    public long getSize() {
        if (sampleSize > 0)
            return sampleSize * sampleCount;
        long sum = 0;
        for (int i : sampleSizes) {
            sum += i;
        }
        return sum;
    }

}