package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:27:04
 * @version 1.0
 */
public class MP4ABox extends Box {

    private int val;

    public MP4ABox(int val) {
        super(new Header("mp4a"));
    }

    protected void doWrite(ByteBuffer out) {
        out.putInt(val);
    }

    public void parse(ByteBuffer input) {
        val = input.getInt();
    }
}
