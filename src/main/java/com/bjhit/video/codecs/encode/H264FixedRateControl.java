package com.bjhit.video.codecs.encode;

import com.bjhit.video.common.tools.MathUtil;

/**
 * H.264 rate control policy that would produce frames of exactly equal size
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:22:42
 * @version 1.0
 */
public class H264FixedRateControl implements RateControl {
    private static final int INIT_QP = 26;
    private int balance;
    private int perMb;
    private int curQp;

    public H264FixedRateControl(int bitsPer256) {
        perMb = bitsPer256;
        curQp = INIT_QP;
    }

    @Override
    public int getInitQp() {
        return INIT_QP;
    }

    @Override
    public int getQpDelta() {
        int qpDelta = balance < 0 ? (balance < -(perMb >> 1) ? 2 : 1) : (balance > perMb ? (balance > (perMb << 2) ? -2
                : -1) : 0);
        int prevQp = curQp;
        curQp = MathUtil.clip(curQp + qpDelta, 12, 30);

        return curQp - prevQp;
    }

    @Override
    public boolean accept(int bits) {

        balance += perMb - bits;

        // System.out.println(balance);

        return true;
    }

    @Override
    public void reset() {
        balance = 0;
        curQp = INIT_QP;
    }

    public int calcFrameSize(int nMB) {
        return ((256 + nMB * (perMb + 9)) >> 3) + (nMB >> 6);
    }

    public void setRate(int rate) {
        perMb = rate;
    }
}
