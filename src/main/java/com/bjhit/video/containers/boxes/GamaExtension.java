package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:23:52
 * @version 1.0
 */
public class GamaExtension extends Box {

    private float gamma;

    public GamaExtension(float gamma) {
        super(new Header(fourcc(), 0));
        this.gamma = gamma;
    }

    public GamaExtension(Header header) {
        super(header);
    }

    public GamaExtension(Box other) {
        super(other);
    }

    public void parse(ByteBuffer input) {
        float g = input.getInt();
        gamma = g / 65536f;
    }

    protected void doWrite(ByteBuffer out) {
        out.putInt((int) (gamma * 65536));
    }
    
    public float getGamma(){
        return gamma;
    }

    public static String fourcc() {
        return "gama";
    }
}