package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:23:18
 * @version 1.0
 */
public class EndianBox extends Box {

    public static enum Endian {
        LITTLE_ENDIAN, BIG_ENDIAN
    };

    private Endian endian;

    public EndianBox(Box other) {
        super(other);
    }

    public static String fourcc() {
        return "enda";
    }

    public EndianBox(Header header) {
        super(header);
    }

    public EndianBox(Endian endian) {
        super(new Header(fourcc()));
        this.endian = endian;
    }

    public void parse(ByteBuffer input) {
        long end = input.getShort();
        if (end == 1) {
            this.endian = Endian.LITTLE_ENDIAN;
        } else {
            this.endian = Endian.BIG_ENDIAN;
        }
    }

    protected void doWrite(ByteBuffer out) {
        out.putShort((short)(endian == Endian.LITTLE_ENDIAN ? 1 : 0));
    }

    public Endian getEndian() {
        return endian;
    }

    protected int calcSize() {
        return 2;
    }
}