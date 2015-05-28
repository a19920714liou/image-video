package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

import com.bjhit.video.common.NIOUtils;
import com.bjhit.video.common.JCodecUtil;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:28:38
 * @version 1.0
 */
public class SegmentTypeBox extends Box {
    private String majorBrand;
    private int minorVersion;
    private Collection<String> compBrands = new LinkedList<String>();

    public static String fourcc() {
        return "styp";
    }

    public SegmentTypeBox(String majorBrand, int minorVersion, Collection<String> compBrands) {
        super(new Header(fourcc()));
        this.majorBrand = majorBrand;
        this.minorVersion = minorVersion;
        this.compBrands = compBrands;
    }

    public SegmentTypeBox() {
        super(new Header(fourcc()));
    }

    public void parse(ByteBuffer input) {
        majorBrand = NIOUtils.readString(input, 4);
        minorVersion = input.getInt();

        String brand;
        while ((brand = NIOUtils.readString(input, 4)) != null) {
            compBrands.add(brand);
        }
    }

    public String getMajorBrand() {
        return majorBrand;
    }

    public Collection<String> getCompBrands() {
        return compBrands;
    }

    public void doWrite(ByteBuffer out) {
        out.put(JCodecUtil.asciiString(majorBrand));
        out.putInt(minorVersion);

        for (String string : compBrands) {
            out.put(JCodecUtil.asciiString(string));
        }
    }
}