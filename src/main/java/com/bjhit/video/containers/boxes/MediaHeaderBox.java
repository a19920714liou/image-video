package com.bjhit.video.containers.boxes;

import static com.bjhit.video.containers.TimeUtil.fromMovTime;
import static com.bjhit.video.containers.TimeUtil.toMovTime;

import java.nio.ByteBuffer;

/**
 * A media header atom
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:25:06
 * @version 1.0
 */
public class MediaHeaderBox extends FullBox {
    private long created;
    private long modified;
    private int timescale;
    private long duration;
    private int language;
    private int quality;

    public static String fourcc() {
        return "mdhd";
    }

    public MediaHeaderBox(int timescale, long duration, int language, long created, long modified, int quality) {
        super(new Header(fourcc()));
        this.timescale = timescale;
        this.duration = duration;
        this.language = language;
        this.created = created;
        this.modified = modified;
        this.quality = quality;
    }

    public MediaHeaderBox() {
        super(new Header(fourcc()));
    }

    public int getTimescale() {
        return timescale;
    }

    public long getDuration() {
        return duration;
    }

    public long getCreated() {
        return created;
    }

    public long getModified() {
        return modified;
    }

    public int getLanguage() {
        return language;
    }

    public int getQuality() {
        return quality;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTimescale(int timescale) {
        this.timescale = timescale;
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        if (version == 0) {
            created = fromMovTime(input.getInt());
            modified = fromMovTime(input.getInt());
            timescale = input.getInt();
            duration = input.getInt();
        } else if (version == 1) {
            created = fromMovTime((int) input.getLong());
            modified = fromMovTime((int) input.getLong());
            timescale = input.getInt();
            duration = input.getLong();
        } else {
            throw new RuntimeException("Unsupported version");
        }
    }

    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(toMovTime(created));
        out.putInt(toMovTime(modified));
        out.putInt(timescale);
        out.putInt((int) duration);
        out.putShort((short) language);
        out.putShort((short) quality);
    }
}