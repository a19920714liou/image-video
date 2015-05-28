package com.bjhit.video.codecs.io.model;

import static com.bjhit.video.codecs.decode.CAVLCReader.readSE;
import static com.bjhit.video.codecs.io.write.CAVLCWriter.writeSE;

import com.bjhit.video.common.io.BitReader;
import com.bjhit.video.common.io.BitWriter;

/**
 * Scaling list entity
 * 
 * capable to serialize / deserialize with CAVLC bitstream
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:36:56
 * @version 1.0
 */
public class ScalingList {

    public int[] scalingList;
    public boolean useDefaultScalingMatrixFlag;

    public void write(BitWriter out)  {
        if (useDefaultScalingMatrixFlag) {
            writeSE(out, 0, "SPS: ");
            return;
        }

        int lastScale = 8;
        int nextScale = 8;
        for (int j = 0; j < scalingList.length; j++) {
            if (nextScale != 0) {
                int deltaScale = scalingList[j] - lastScale - 256;
                writeSE(out, deltaScale, "SPS: ");
            }
            lastScale = scalingList[j];
        }
    }

    public static ScalingList read(BitReader in, int sizeOfScalingList)  {

        ScalingList sl = new ScalingList();
        sl.scalingList = new int[sizeOfScalingList];
        int lastScale = 8;
        int nextScale = 8;
        for (int j = 0; j < sizeOfScalingList; j++) {
            if (nextScale != 0) {
                int deltaScale = readSE(in, "deltaScale");
                nextScale = (lastScale + deltaScale + 256) % 256;
                sl.useDefaultScalingMatrixFlag = (j == 0 && nextScale == 0);
            }
            sl.scalingList[j] = nextScale == 0 ? lastScale : nextScale;
            lastScale = sl.scalingList[j];
        }
        return sl;
    }
}