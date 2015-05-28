package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:22:32
 * @version 1.0
 */
public class DataRefBox extends NodeBox {

    private static final MyFactory FACTORY = new MyFactory();

    public static String fourcc() {
        return "dref";
    }

    public DataRefBox() {
        this(new Header(fourcc()));
    }

    private DataRefBox(Header atom) {
        super(atom);
        factory = FACTORY;
    }

    @Override
    public void parse(ByteBuffer input) {
        input.getInt();
        input.getInt();
        super.parse(input);
    }

    @Override
    public void doWrite(ByteBuffer out) {
        out.putInt(0);
        out.putInt(boxes.size());
        super.doWrite(out);
    }

    public static class MyFactory extends BoxFactory {
        private Map<String, Class<? extends Box>> mappings = new HashMap<String, Class<? extends Box>>();

        public MyFactory() {
            mappings.put(UrlBox.fourcc(), UrlBox.class);
            mappings.put(AliasBox.fourcc(), AliasBox.class);
            mappings.put("cios", AliasBox.class);
        }

        public Class<? extends Box> toClass(String fourcc) {
            return mappings.get(fourcc);
        }
    }
}
