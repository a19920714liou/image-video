package com.bjhit.video.containers.boxes;

/**
 * A box storing a list of synch samples
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:27:37
 * @version 1.0
 */
public class PartialSyncSamplesBox extends SyncSamplesBox {
    public static String fourcc() {
        return "stps";
    }

    public PartialSyncSamplesBox() {
        super(new Header(fourcc()));
    }

    public PartialSyncSamplesBox(int[] array) {
        super(array);
    }
}
