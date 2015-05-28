package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:21:52
 * @version 1.0
 */
public class ClipRegionBox extends Box {

    private short rgnSize;
    private short y;
    private short x;
    private short height;
    private short width;

    public static String fourcc() {
        return "crgn";
    }

    public ClipRegionBox(Header atom) {
        super(atom);
    }

    public ClipRegionBox() {
        super(new Header(fourcc()));
    }

    public ClipRegionBox(short x, short y, short width, short height) {
        this();
        rgnSize = 10;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void parse(ByteBuffer input) {
        rgnSize = input.getShort();
        y = input.getShort();
        x = input.getShort();
        height = input.getShort();
        width = input.getShort();
    }

    protected void doWrite(ByteBuffer out) {
        out.putShort(rgnSize);
        out.putShort(y);
        out.putShort(x);
        out.putShort(height);
        out.putShort(width);
    }

    public short getRgnSize() {
        return rgnSize;
    }

    public short getY() {
        return y;
    }

    public short getX() {
        return x;
    }

    public short getHeight() {
        return height;
    }

    public short getWidth() {
        return width;
    }
}