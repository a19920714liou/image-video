package com.bjhit.video.containers;

import java.nio.ByteBuffer;

import com.bjhit.video.common.model.Packet;
import com.bjhit.video.common.model.TapeTimecode;

/**
 * @description mp4视频数据包
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:51:26
 * @version 1.0
 */
public class MP4Packet extends Packet {
    private long mediaPts;
    private int entryNo;
    private long fileOff;
    private int size;
    private boolean psync;
    /**
     * 
     * @param data 数据
     * @param pts 帧的相对时间戳
     * @param timescale 时间度量值
     * @param duration  持续时间度量单位
     * @param frameNo 帧值
     * @param iframe 是否iFrame格式，iframe可省去中间的转码过程，提升视频采集、编辑和共享的效率
     * @param tapeTimecode 
     * @param mediaPts 媒体帧相对时间戳
     * @param entryNo 
     */
    public MP4Packet(ByteBuffer data, long pts, long timescale, long duration, long frameNo, boolean iframe,
            TapeTimecode tapeTimecode, long mediaPts, int entryNo) {
        super(data, pts, timescale, duration, frameNo, iframe, tapeTimecode);
        this.mediaPts = mediaPts;
        this.entryNo = entryNo;
    }
    
    public MP4Packet(ByteBuffer data, long pts, long timescale, long duration, long frameNo, boolean iframe,
            TapeTimecode tapeTimecode, long mediaPts, int entryNo, long fileOff, int size, boolean psync) {
        super(data, pts, timescale, duration, frameNo, iframe, tapeTimecode);
        this.mediaPts = mediaPts;
        this.entryNo = entryNo;
        this.fileOff = fileOff;
        this.size = size;
        this.psync = psync;
    }

    public MP4Packet(MP4Packet packet, ByteBuffer frm) {
        super(packet, frm);
        this.mediaPts = packet.mediaPts;
        this.entryNo = packet.entryNo;
    }

    public MP4Packet(MP4Packet packet, TapeTimecode timecode) {
        super(packet, timecode);
        this.mediaPts = packet.mediaPts;
        this.entryNo = packet.entryNo;
    }

    public MP4Packet(Packet packet, long mediaPts, int entryNo) {
        super(packet);
        this.mediaPts = mediaPts;
        this.entryNo = entryNo;
    }

    public MP4Packet(MP4Packet packet) {
        super(packet);
        this.mediaPts = packet.mediaPts;
        this.entryNo = packet.entryNo;
    }

    /**
     * Zero-offset sample entry index
     * 
     * @return
     */
    public int getEntryNo() {
        return entryNo;
    }

    public long getMediaPts() {
        return mediaPts;
    }

    public long getFileOff() {
        return fileOff;
    }

    public int getSize() {
        return size;
    }

    public boolean isPsync() {
        return psync;
    }
}