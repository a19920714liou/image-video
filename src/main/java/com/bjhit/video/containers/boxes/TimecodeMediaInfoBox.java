package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

import com.bjhit.video.common.NIOUtils;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:29:06
 * @version 1.0
 */
public class TimecodeMediaInfoBox extends FullBox {

    private short font;
    private short face;
    private short size;
    private short[] color = new short[3];
    private short[] bgcolor = new short[3];
    private String name;

    public static String fourcc() {
        return "tcmi";
    }

    public TimecodeMediaInfoBox(short font, short face, short size, short[] color, short[] bgcolor, String name) {
        this(new Header(fourcc()));
        this.font = font;
        this.face = face;
        this.size = size;
        this.color = color;
        this.bgcolor = bgcolor;
        this.name = name;
    }

    public TimecodeMediaInfoBox(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        font = input.getShort();
        face = input.getShort();
        size = input.getShort();
        input.getShort();
        color[0] = input.getShort();
        color[1] = input.getShort();
        color[2] = input.getShort();
        bgcolor[0] = input.getShort();
        bgcolor[1] = input.getShort();
        bgcolor[2] = input.getShort();
        name = NIOUtils.readPascalString(input);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort(font);
        out.putShort(face);
        out.putShort(size);
        out.putShort((short) 0);
        out.putShort(color[0]);
        out.putShort(color[1]);
        out.putShort(color[2]);
        out.putShort(bgcolor[0]);
        out.putShort(bgcolor[1]);
        out.putShort(bgcolor[2]);
        NIOUtils.writePascalString(out, name);
    }
}