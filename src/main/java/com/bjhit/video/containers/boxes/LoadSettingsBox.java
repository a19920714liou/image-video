package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;

/**
 * Load setting atom
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:24:42
 * @version 1.0
 */
public class LoadSettingsBox extends Box {
    private int preloadStartTime;
    private int preloadDuration;
    private int preloadFlags;
    private int defaultHints;

    public static String fourcc() {
        return "load";
    }

    public LoadSettingsBox(Header header) {
        super(header);
    }

    public LoadSettingsBox() {
        super(new Header(fourcc()));
    }

    public void parse(ByteBuffer input) {
        preloadStartTime = input.getInt();
        preloadDuration = input.getInt();
        preloadFlags = input.getInt();
        defaultHints = input.getInt();
    }

    protected void doWrite(ByteBuffer out) {
        out.putInt(preloadStartTime);
        out.putInt(preloadDuration);
        out.putInt(preloadFlags);
        out.putInt(defaultHints);
    }

    public int getPreloadStartTime() {
        return preloadStartTime;
    }

    public int getPreloadDuration() {
        return preloadDuration;
    }

    public int getPreloadFlags() {
        return preloadFlags;
    }

    public int getDefaultHints() {
        return defaultHints;
    }
}