package com.bjhit.video.scale;

import com.bjhit.video.common.model.Picture;


/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午4:03:32
 * @version 1.0
 */
public interface Transform {
    public void transform(Picture src, Picture dst);
}