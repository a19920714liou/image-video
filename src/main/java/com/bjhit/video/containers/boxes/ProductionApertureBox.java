package com.bjhit.video.containers.boxes;


/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:27:47
 * @version 1.0
 */
public class ProductionApertureBox extends ClearApertureBox {

    public static String fourcc() {
        return "prof";
    }

    public ProductionApertureBox(Header atom) {
        super(atom);
    }

    public ProductionApertureBox() {
        super(new Header(fourcc()));
    }

    public ProductionApertureBox(int width, int height) {
        super(new Header(fourcc()), width, height);
    }
}
