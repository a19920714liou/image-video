package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

import com.bjhit.video.common.JCodecUtil;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:22:02
 * @version 1.0
 */
public class ColorExtension extends Box {

    private short primariesIndex;
    private short transferFunctionIndex;
    private short matrixIndex;
    private final String type = "nclc";

    public ColorExtension(short primariesIndex, short transferFunctionIndex, short matrixIndex) {
        this();
        this.primariesIndex = primariesIndex;
        this.transferFunctionIndex = transferFunctionIndex;
        this.matrixIndex = matrixIndex;
    }

    public ColorExtension() {
        super(new Header(fourcc()));
    }

    @Override
    public void parse(ByteBuffer input) {
        long type = input.getInt();
        primariesIndex = input.getShort();
        transferFunctionIndex = input.getShort();
        matrixIndex = input.getShort();
    }

    @Override
    public void doWrite(ByteBuffer out) {
        out.put(JCodecUtil.asciiString(type));
        out.putShort(primariesIndex);
        out.putShort(transferFunctionIndex);
        out.putShort(matrixIndex);
    }

    public static String fourcc() {
        return "colr";
    }

    public short getPrimariesIndex() {
        return primariesIndex;
    }

    public short getTransferFunctionIndex() {
        return transferFunctionIndex;
    }

    public short getMatrixIndex() {
        return matrixIndex;
    }
}
