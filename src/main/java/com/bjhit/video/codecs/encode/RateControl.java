package com.bjhit.video.codecs.encode;

/**
 * MPEG 4 AVC ( H.264 ) Encoder pluggable rate control mechanism
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:23:04
 * @version 1.0
 */
public interface RateControl {

    int getInitQp();

    int getQpDelta();

    boolean accept(int bits);

    void reset();

}
