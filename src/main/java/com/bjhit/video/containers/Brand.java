package com.bjhit.video.containers;

import java.util.Arrays;

import com.bjhit.video.containers.boxes.FileTypeBox;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:50:15
 * @version 1.0
 */
public enum Brand {
    MOV("qt  ", 0x00000200, new String[] { "qt  " }), MP4("isom", 0x00000200, new String[] { "isom", "iso2", "avc1",
            "mp41" });

    private FileTypeBox ftyp;

    private Brand(String majorBrand, int version, String[] compatible) {
        ftyp = new FileTypeBox(majorBrand, version, Arrays.asList(compatible));
    }

    public FileTypeBox getFileTypeBox() {
        return ftyp;
    }
}
