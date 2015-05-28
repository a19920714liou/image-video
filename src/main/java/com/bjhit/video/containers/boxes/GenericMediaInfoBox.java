package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:24:01
 * @version 1.0
 */
public class GenericMediaInfoBox extends FullBox {
    private short graphicsMode;
    private short rOpColor;
    private short gOpColor;
    private short bOpColor;
    private short balance;

    public static String fourcc() {
        return "gmin";
    }

    public GenericMediaInfoBox(short graphicsMode, short rOpColor, short gOpColor, short bOpColor, short balance) {
        this();
        this.graphicsMode = graphicsMode;
        this.rOpColor = rOpColor;
        this.gOpColor = gOpColor;
        this.bOpColor = bOpColor;
        this.balance = balance;
    }

    public GenericMediaInfoBox(Header atom) {
        super(atom);
    }

    public GenericMediaInfoBox() {
        this(new Header(fourcc()));
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        graphicsMode = input.getShort();
        rOpColor = input.getShort();
        gOpColor = input.getShort();
        bOpColor = input.getShort();
        balance = input.getShort();
        input.getShort();
    }

    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort(graphicsMode);
        out.putShort(rOpColor);
        out.putShort(gOpColor);
        out.putShort(bOpColor);
        out.putShort(balance);
        out.putShort((short)0);
    }
}
