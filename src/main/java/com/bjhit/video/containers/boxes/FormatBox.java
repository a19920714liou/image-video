package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

import com.bjhit.video.common.NIOUtils;
import com.bjhit.video.common.JCodecUtil;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:23:40
 * @version 1.0
 */
public class FormatBox extends Box {
    private String fmt;

    public FormatBox(Box other) {
        super(other);
    }

    public FormatBox(Header header) {
        super(header);
    }

    public FormatBox(String fmt) {
        super(new Header(fourcc()));
        this.fmt = fmt;
    }

    public static String fourcc() {
        return "frma";
    }

    public void parse(ByteBuffer input) {
        this.fmt = NIOUtils.readString(input, 4);
    }

    protected void doWrite(ByteBuffer out) {
        out.put(JCodecUtil.asciiString(fmt));
    }
}