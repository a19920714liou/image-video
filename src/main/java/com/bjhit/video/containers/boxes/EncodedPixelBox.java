package com.bjhit.video.containers.boxes;


/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:22:57
 * @version 1.0
 */
public class EncodedPixelBox extends ClearApertureBox {

    public static String fourcc() {
        return "enof";
    }

    public EncodedPixelBox(Header atom) {
        super(atom);
    }

    public EncodedPixelBox() {
        super(new Header(fourcc()));
    }

    public EncodedPixelBox(int width, int height) {
        super(new Header(fourcc()), width, height);
    }
}