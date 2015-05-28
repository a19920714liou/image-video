package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:28:06
 * @version 1.0
 */
public class SampleEntry extends NodeBox {

    private short drefInd;

    public SampleEntry(Header header) {
        super(header);
    }

    public SampleEntry(Header header, short drefInd) {
        super(header);
        this.drefInd = drefInd;
    }

    public void parse(ByteBuffer input) {
        input.getInt();
        input.getShort();
        
        drefInd = input.getShort();
    }
    
    protected void parseExtensions(ByteBuffer input) {
        super.parse(input);
    }

    protected void doWrite(ByteBuffer out) {
        out.put(new byte[] { 0, 0, 0, 0, 0, 0 });
        out.putShort(drefInd); // data ref index
    }
    
    protected void writeExtensions(ByteBuffer out) {
        super.doWrite(out);
    }

    public short getDrefInd() {
        return drefInd;
    }

    public void setDrefInd(short ind) {
        this.drefInd = ind;
    }

    public void setMediaType(String mediaType) {
        header = new Header(mediaType);
    }
}
