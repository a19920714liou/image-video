package com.bjhit.video.containers.boxes;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:22:40
 * @version 1.0
 */
public class Edit {
    private long duration;
    private long mediaTime;
    private float rate;
    
    public Edit(long duration, long mediaTime, float rate) {
        this.duration = duration;
        this.mediaTime = mediaTime;
        this.rate = rate;
    }

    public Edit(Edit edit) {
        this.duration = edit.duration;
        this.mediaTime = edit.mediaTime;
        this.rate = edit.rate;
    }

    public long getDuration() {
        return duration;
    }

    public long getMediaTime() {
        return mediaTime;
    }

    public float getRate() {
        return rate;
    }

    public void shift(long shift) {
        mediaTime += shift;
    }

    public void setMediaTime(long l) {
        mediaTime = l;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
