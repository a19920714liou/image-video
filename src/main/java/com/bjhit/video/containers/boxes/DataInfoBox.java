package com.bjhit.video.containers.boxes;

/**
 * Creates MP4 file out of a set of samples
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:22:20
 * @version 1.0
 */
public class DataInfoBox extends NodeBox {
    
    public static String fourcc() {
        return "dinf";
    }

    public DataInfoBox() {
        super(new Header(fourcc()));
    }

    public DataInfoBox(Header atom) {
        super(atom);
    }

    public DataRefBox getDref() {
        return findFirst(this, DataRefBox.class, "dref");
    }
}
