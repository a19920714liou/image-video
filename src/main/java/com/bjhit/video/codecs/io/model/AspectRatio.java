package com.bjhit.video.codecs.io.model;

/**
 * Aspect ratio
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-13 下午3:34:42
 * @version 1.0
 */
public class AspectRatio {

    public static final AspectRatio Extended_SAR = new AspectRatio(255);

    private int value;

    private AspectRatio(int value) {
        this.value = value;
    }

    public static AspectRatio fromValue(int value) {
        if (value == Extended_SAR.value) {
            return Extended_SAR;
        }
        return new AspectRatio(value);
    }

    public int getValue() {
        return value;
    }
}
