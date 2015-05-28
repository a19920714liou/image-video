package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

import com.bjhit.video.common.NIOUtils;
import com.bjhit.video.common.JCodecUtil;

/**
 * A handler description box
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:24:13
 * @version 1.0
 */
public class HandlerBox extends FullBox {
    private String componentType;
    private String componentSubType;
    private String componentManufacturer;
    private int componentFlags;
    private int componentFlagsMask;
    private String componentName;

    public static String fourcc() {
        return "hdlr";
    }

    public HandlerBox(String componentType, String componentSubType, String componentManufacturer, int componentFlags,
            int componentFlagsMask) {
        super(new Header("hdlr"));
        this.componentType = componentType;
        this.componentSubType = componentSubType;
        this.componentManufacturer = componentManufacturer;
        this.componentFlags = componentFlags;
        this.componentFlagsMask = componentFlagsMask;
        this.componentName = "";
    }

    public HandlerBox() {
        super(new Header(fourcc()));
    }

    public void parse(ByteBuffer input) {
        super.parse(input);

        componentType = NIOUtils.readString(input, 4);
        componentSubType = NIOUtils.readString(input, 4);
        componentManufacturer = NIOUtils.readString(input, 4);

        componentFlags = input.getInt();
        componentFlagsMask = input.getInt();
        componentName = NIOUtils.readString(input, input.remaining());
    }

    public void doWrite(ByteBuffer out) {
        super.doWrite(out);

        out.put(JCodecUtil.asciiString(componentType));
        out.put(JCodecUtil.asciiString(componentSubType));
        out.put(JCodecUtil.asciiString(componentManufacturer));

        out.putInt(componentFlags);
        out.putInt(componentFlagsMask);
        if (componentName != null) {
            out.put(JCodecUtil.asciiString(componentName));
        }
    }

    public String getComponentType() {
        return componentType;
    }

    public String getComponentSubType() {
        return componentSubType;
    }

    public String getComponentManufacturer() {
        return componentManufacturer;
    }

    public int getComponentFlags() {
        return componentFlags;
    }

    public int getComponentFlagsMask() {
        return componentFlagsMask;
    }
}