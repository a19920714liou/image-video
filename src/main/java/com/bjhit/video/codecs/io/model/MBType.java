package com.bjhit.video.codecs.io.model;

/**
 * 宏块类型
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:35:19
 * @version 1.0
 */
public enum MBType {

    I_NxN(true), I_16x16(true), I_PCM(true), P_16x16, P_16x8, P_8x16, P_8x8, P_8x8ref0, B_Direct_16x16, B_L0_16x16, B_L1_16x16, B_Bi_16x16, B_L0_L0_16x8, B_L0_L0_8x16, B_L1_L1_16x8, B_L1_L1_8x16, B_L0_L1_16x8, B_L0_L1_8x16, B_L1_L0_16x8, B_L1_L0_8x16, B_L0_Bi_16x8, B_L0_Bi_8x16, B_L1_Bi_16x8, B_L1_Bi_8x16, B_Bi_L0_16x8, B_Bi_L0_8x16, B_Bi_L1_16x8, B_Bi_L1_8x16, B_Bi_Bi_16x8, B_Bi_Bi_8x16, B_8x8;

    public boolean intra;

    private MBType(boolean intra) {
        this.intra = intra;
    }

    private MBType() {
        this(false);
    }

    public boolean isIntra() {
        return intra;
    }
}
