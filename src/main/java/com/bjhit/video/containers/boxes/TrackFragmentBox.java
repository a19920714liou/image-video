package com.bjhit.video.containers.boxes;

import java.util.List;

/**
 * Contains routines dedicated to simplify working with track fragments
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:30:49
 * @version 1.0
 */
public class TrackFragmentBox extends NodeBox {

    public TrackFragmentBox() {
        super(new Header(fourcc()));
    }

    public static String fourcc() {
        return "traf";
    }

    protected void getModelFields(List<String> model) {

    }

    public int getTrackId() {
        TrackFragmentHeaderBox tfhd = Box
                .findFirst(this, TrackFragmentHeaderBox.class, TrackFragmentHeaderBox.fourcc());
        if (tfhd == null)
            throw new RuntimeException("Corrupt track fragment, no header atom found");
        return tfhd.getTrackId();
    }
}
