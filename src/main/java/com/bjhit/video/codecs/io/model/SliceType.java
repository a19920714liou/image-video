package com.bjhit.video.codecs.io.model;

/**
 * 片编码类型
 * I：帧内预测编码
 * P：帧间预测编码(单向，通过前一帧预测后一帧)
 * B：双向预测编码
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:40:08
 * @version 1.0
 */
public enum SliceType {
    P, B, I, SP, SI;
    /**
     * 是否为帧内预测编码类型
     * @return
     */
    public boolean isIntra() {
        return this == I || this == SI;
    }
    /**
     * 是否为帧间预测编码类型
     * @return
     */
    public boolean isInter() {
        return this != I && this != SI;
    }

    public static SliceType fromValue(int j) {
        for (SliceType sliceType : values()) {
            if (sliceType.ordinal() == j)
                return sliceType;
        }
        return null;
    }
}
