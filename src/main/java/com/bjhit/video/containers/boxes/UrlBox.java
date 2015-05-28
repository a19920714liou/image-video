package com.bjhit.video.containers.boxes;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.bjhit.video.common.NIOUtils;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:31:43
 * @version 1.0
 */
public class UrlBox extends FullBox {

    private String url;

    public static String fourcc() {
        return "url ";
    }

    public UrlBox(String url) {
        super(new Header(fourcc()));
        this.url = url;
    }

    public UrlBox(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        if ((flags & 0x1) != 0)
            return;
        Charset utf8 = Charset.forName("utf-8");
        
        url = NIOUtils.readNullTermString(input, utf8);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);

        Charset utf8 = Charset.forName("utf-8");

        if (url != null) {
            NIOUtils.write(out, ByteBuffer.wrap(url.getBytes(utf8)));
            out.put((byte) 0);
        }
    }

    public String getUrl() {
        return url;
    }
}
