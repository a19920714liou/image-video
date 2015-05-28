package com.bjhit.video.common.model;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-26 下午4:40:34
 * @version 1.0
 */
public class TapeTimecode {
    private short hour;
    private byte minute;
    private byte second;
    private byte frame;
    private boolean dropFrame;

    public TapeTimecode(short hour, byte minute, byte second, byte frame, boolean dropFrame) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.frame = frame;
        this.dropFrame = dropFrame;
    }

    public short getHour() {
        return hour;
    }

    public byte getMinute() {
        return minute;
    }

    public byte getSecond() {
        return second;
    }

    public byte getFrame() {
        return frame;
    }

    public boolean isDropFrame() {
        return dropFrame;
    }

    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second) + (dropFrame ? ";" : ":")
                + String.format("%02d", frame);
    }
}
