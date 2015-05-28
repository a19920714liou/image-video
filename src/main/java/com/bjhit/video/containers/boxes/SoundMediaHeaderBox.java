package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * Sound media header
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:28:48
 * @version 1.0
 */
public class SoundMediaHeaderBox extends FullBox {
    private short balance;
    
    public static String fourcc() {
        return "smhd";
    }

    public SoundMediaHeaderBox(Header atom) {
        super(atom);
    }
    
    public SoundMediaHeaderBox() {
        super(new Header(fourcc()));
    }

    public void parse(ByteBuffer input) {
        super.parse(input);
        balance = input.getShort();
        input.getShort();
    }

    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort(balance);
        out.putShort((short)0);
    }

    public short getBalance() {
        return balance;
    }
}
