package com.bjhit.video.codecs.io.model;

/**
 * Reference picture marking used for IDR frames
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:36:44
 * @version 1.0
 */
public class RefPicMarkingIDR {
    boolean discardDecodedPics;
    boolean useForlongTerm;

    public RefPicMarkingIDR(boolean discardDecodedPics, boolean useForlongTerm) {
        this.discardDecodedPics = discardDecodedPics;
        this.useForlongTerm = useForlongTerm;
    }

    public boolean isDiscardDecodedPics() {
        return discardDecodedPics;
    }

    public boolean isUseForlongTerm() {
        return useForlongTerm;
    }
}
