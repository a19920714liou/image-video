package com.bjhit.video.containers.boxes;

import java.util.HashMap;
import java.util.Map;

/**
 * Wave extension to audio sample entry
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:32:12
 * @version 1.0
 */
public class WaveExtension extends NodeBox {
    private static final MyFactory FACTORY = new MyFactory();

    public static String fourcc() {
        return "wave";
    }

    public WaveExtension(Header atom) {
        super(atom);
        factory = FACTORY;

    }

    public static class MyFactory extends BoxFactory {
        private Map<String, Class<? extends Box>> mappings = new HashMap<String, Class<? extends Box>>();

        public MyFactory() {
            mappings.put(FormatBox.fourcc(), FormatBox.class);
            mappings.put(EndianBox.fourcc(), EndianBox.class);
//            mappings.put(EsdsBox.fourcc(), EsdsBox.class);
        }

        public Class<? extends Box> toClass(String fourcc) {
            return mappings.get(fourcc);
        }
    }
}