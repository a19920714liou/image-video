package com.bjhit.video.containers;

import java.util.EnumSet;

/**
 * Creates MP4 file out of a set of samples
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:51:49
 * @version 1.0
 */
public enum TrackType {
    VIDEO("vide"), SOUND("soun"), TIMECODE("tmcd"), HINT("hint"), TEXT("text"), HYPER_TEXT("wtxt"), CC("clcp"), SUB(
            "sbtl"), MUSIC("musi"), MPEG1("MPEG"), SPRITE("sprt"), TWEEN("twen"), CHAPTERS("chap"), THREE_D("qd3d"), STREAMING(
            "strm"), OBJECTS("obje");

    private String handler;

    private TrackType(String handler) {
        this.handler = handler;
    }

    public String getHandler() {
        return handler;
    }

    public static TrackType fromHandler(String handler) {
        for (TrackType val : EnumSet.allOf(TrackType.class)) {
            if (val.getHandler().equals(handler))
                return val;
        }
        return null;
    }
}
