package com.bjhit.video.scale;

import com.bjhit.video.common.model.Picture;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-15 下午2:33:25
 * @version 1.0
 */
public class RgbToYuv422p implements Transform {

    private int upShift;
    private int downShift;
    private int downShiftChr;

    public RgbToYuv422p(int upShift, int downShift) {
        this.upShift = upShift;
        this.downShift = downShift;
        this.downShiftChr = downShift + 1;
    }

    public void transform(Picture img, Picture dst) {

        int[] y = img.getData()[0];
        int[][] dstData = dst.getData();

        int off = 0, offSrc = 0;
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth() >> 1; j++) {
                dstData[1][off] = 0;
                dstData[2][off] = 0;
                
                int offY = off << 1;

                RgbToYuv420p.rgb2yuv(y[offSrc++], y[offSrc++], y[offSrc++], dstData[0], offY, dstData[1], off,
                        dstData[2], off);
                dstData[0][offY] = (dstData[0][offY] << upShift) >> downShift;

                RgbToYuv420p.rgb2yuv(y[offSrc++], y[offSrc++], y[offSrc++], dstData[0], offY + 1, dstData[1], off,
                        dstData[2], off);
                dstData[0][offY + 1] = (dstData[0][offY + 1] << upShift) >> downShift;

                dstData[1][off] = (dstData[1][off] << upShift) >> downShiftChr;
                dstData[2][off] = (dstData[2][off] << upShift) >> downShiftChr;
                ++off;
            }
        }
    }
}