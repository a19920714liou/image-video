package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * Movie fragment header box
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:26:36
 * @version 1.0
 */
public class MovieFragmentHeaderBox extends FullBox {

    private int sequenceNumber;

    public MovieFragmentHeaderBox() {
        super(new Header(fourcc()));
    }

    public static String fourcc() {
        return "mfhd";
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        sequenceNumber = input.getInt();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(sequenceNumber);
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
