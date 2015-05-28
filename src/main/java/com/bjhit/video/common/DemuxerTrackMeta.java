package com.bjhit.video.common;

import com.bjhit.video.common.model.Size;

/**
 * Meta information about this media track.
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:57:43
 * @version 1.0
 */
public class DemuxerTrackMeta {

    public static enum Type {
        VIDEO, AUDIO, OTHER
    };

    private Type type;
    private int[] seekFrames;
    private int totalFrames;
    private double totalDuration;
    private Size dimensions;

    public DemuxerTrackMeta(Type type, int[] seekFrames, int totalFrames, double totalDuration, Size dimensions) {
        this.type = type;
        this.seekFrames = seekFrames;
        this.totalFrames = totalFrames;
        this.totalDuration = totalDuration;
        this.dimensions = dimensions;
    }

    public Type getType() {
        return type;
    }

    /**
     * @return Array of frame indexes that can be used to seek to, i.e. which
     *         don't require any previous frames to be decoded. Is null when
     *         every frame is a seek frame.
     */
    public int[] getSeekFrames() {
        return seekFrames;
    }

    /**
     * @return Total number of frames in this media track.
     */
    public int getTotalFrames() {
        return totalFrames;
    }

    /**
     * @return Total duration in seconds of the media track
     */
    public double getTotalDuration() {
        return totalDuration;
    }

    public Size getDimensions() {
        return dimensions;
    }
}