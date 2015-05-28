package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

import com.bjhit.video.common.NIOUtils;
import com.bjhit.video.common.JCodecUtil;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:27:10
 * @version 1.0
 */
public class NameBox extends Box {
    private String name;

    public static String fourcc() {
        return "name";
    }

    public NameBox(String name) {
        this();
        this.name = name;
    }

    public NameBox() {
        super(new Header(fourcc()));
    }

    private NameBox(Header header) {
        super(header);
    }

    public void parse(ByteBuffer input) {
        name = NIOUtils.readNullTermString(input);
    }

    protected void doWrite(ByteBuffer out) {
        out.put(JCodecUtil.asciiString(name));
        out.putInt(0);
    }

    public String getName() {
        return name;
    }
}
