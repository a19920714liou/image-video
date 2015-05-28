package com.bjhit.video.containers.boxes;

public class MovieExtendsBox extends NodeBox {
    public static String fourcc() {
        return "mvex";
    }

    public MovieExtendsBox() {
        super(new Header(fourcc()));
    }
}
