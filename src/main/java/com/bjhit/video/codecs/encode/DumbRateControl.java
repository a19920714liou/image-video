package com.bjhit.video.codecs.encode;

/**
 * Dumb rate control policy, always maintains the same QP for the whole video
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-3-2 下午2:59:54
 * @version 1.0
 */
public class DumbRateControl implements RateControl {
    private static final int QP = 20;

    @Override
    public int getInitQp() {
        return QP;
    }

    @Override
    public int getQpDelta() {
        return 0;
    }

    @Override
    public boolean accept(int bits) {
        return true;
    }

    @Override
    public void reset() {
        // Do nothing, remember we are dumb
    }
}
