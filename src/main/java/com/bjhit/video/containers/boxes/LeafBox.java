package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

import com.bjhit.video.common.NIOUtils;

/**
 * A leaf box
 * 
 * A box containing data, no children
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:24:27
 * @version 1.0
 */
public class LeafBox extends Box {
    private ByteBuffer data;

    public LeafBox(Header atom) {
        super(atom);
    }

    public LeafBox(Header atom, ByteBuffer data) {
        super(atom);
        this.data = data;
    }

    public void parse(ByteBuffer input) {
        data = NIOUtils.read(input, (int) header.getBodySize());
    }

    public ByteBuffer getData() {
        return data.duplicate();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        NIOUtils.write(out, data);
    }
}
