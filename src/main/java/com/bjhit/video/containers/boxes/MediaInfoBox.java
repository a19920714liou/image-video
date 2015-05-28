package com.bjhit.video.containers.boxes;

/**
 * Creates MP4 file out of a set of samples
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:25:19
 * @version 1.0
 */
public class MediaInfoBox extends NodeBox {

    public static String fourcc() {
        return "minf";
    }

    public MediaInfoBox(Header atom) {
        super(atom);
    }

    public MediaInfoBox() {
        super(new Header(fourcc()));
    }

    public DataInfoBox getDinf() {
        return findFirst(this, DataInfoBox.class, "dinf");
    }

    public NodeBox getStbl() {
        return findFirst(this, NodeBox.class, "stbl");
    }
}
