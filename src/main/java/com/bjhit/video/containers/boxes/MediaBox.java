package com.bjhit.video.containers.boxes;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:24:53
 * @version 1.0
 */
public class MediaBox extends NodeBox {

    public static String fourcc() {
        return "mdia";
    }

    public MediaBox(Header atom) {
        super(atom);
    }

    public MediaBox() {
        super(new Header(fourcc()));
    }

    public MediaInfoBox getMinf() {
        return Box.findFirst(this, MediaInfoBox.class, "minf");
    }
}
