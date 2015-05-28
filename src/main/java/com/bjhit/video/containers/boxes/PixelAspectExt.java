package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

import com.bjhit.video.common.model.Rational;

/**
 * Pixel aspect ratio video sample entry extension
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:50:41
 * @version 1.0
 */
public class PixelAspectExt extends Box {
    private int hSpacing;
    private int vSpacing;

    public PixelAspectExt(Header header) {
        super(header);
    }

    public PixelAspectExt() {
        super(new Header(fourcc()));
    }

    public PixelAspectExt(Rational par) {
        this();
        this.hSpacing = par.getNum();
        this.vSpacing = par.getDen();
    }

    public void parse(ByteBuffer input) {
        hSpacing = input.getInt();
        vSpacing = input.getInt();
    }

    protected void doWrite(ByteBuffer out) {
        out.putInt(hSpacing);
        out.putInt(vSpacing);
    }

    public int gethSpacing() {
        return hSpacing;
    }

    public int getvSpacing() {
        return vSpacing;
    }
    
    public Rational getRational() {
        return new Rational(hSpacing, vSpacing);
    }

    public static String fourcc() {
        return "pasp";
    }
}