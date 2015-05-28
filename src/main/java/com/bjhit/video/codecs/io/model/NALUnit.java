package com.bjhit.video.codecs.io.model;

import java.nio.ByteBuffer;

/**
 * Network abstraction layer (NAL) unit
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:35:29
 * @version 1.0
 */
public class NALUnit {

    public NALUnitType type;
    public int nal_ref_idc;

    public NALUnit(NALUnitType type, int nal_ref_idc) {
        this.type = type;
        this.nal_ref_idc = nal_ref_idc;
    }

    public static NALUnit read(ByteBuffer in) {
        int nalu = in.get() & 0xff;
        int nal_ref_idc = (nalu >> 5) & 0x3;
        int nb = nalu & 0x1f;

        NALUnitType type = NALUnitType.fromValue(nb);
        return new NALUnit(type, nal_ref_idc);
    }

    public void write(ByteBuffer out) {
        int nalu = type.getValue() | (nal_ref_idc << 5);
        out.put((byte) nalu);
    }
}
