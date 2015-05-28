package com.bjhit.video.containers;

import static com.bjhit.video.common.NIOUtils.readableFileChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bjhit.video.common.Codec;
import com.bjhit.video.common.NIOUtils;
import com.bjhit.video.common.SeekableByteChannel;
import com.bjhit.video.containers.boxes.Box;
import com.bjhit.video.containers.boxes.BoxFactory;
import com.bjhit.video.containers.boxes.Header;
import com.bjhit.video.containers.boxes.MovieBox;
import com.bjhit.video.containers.boxes.MovieFragmentBox;
import com.bjhit.video.containers.boxes.NodeBox;
import com.bjhit.video.containers.boxes.TrakBox;

/**
 * 
 * @description mp4视频格式工具类
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:53:21
 * @version 1.0
 */
public class MP4Util {

    private static Map<Codec, String> codecMapping = new HashMap<Codec, String>();

    static {
        codecMapping.put(Codec.MPEG2, "m2v1");
        codecMapping.put(Codec.H264, "avc1");
        codecMapping.put(Codec.J2K, "mjp2");
    }
    /**
     * 从视频流中获取MovieBox
     * @param input
     * @param url
     * @return
     * @throws IOException
     */
    public static MovieBox createRefMovie(SeekableByteChannel input, String url) throws IOException {
        MovieBox movie = parseMovie(input);

        for (TrakBox trakBox : movie.getTracks()) {
            trakBox.setDataRef(url);
        }
        return movie;
    }
    /**
     * 从视频流中获取MovieBox
     * @param input
     * @return
     * @throws IOException
     */
    public static MovieBox parseMovie(SeekableByteChannel input) throws IOException {
        for (Atom atom : getRootAtoms(input)) {
            if ("moov".equals(atom.getHeader().getFourcc())) {
                return (MovieBox) atom.parseBox(input);
            }
        }
        return null;
    }
    /**
     * 从视频流中解释视频分片信息
     * @param input
     * @return
     * @throws IOException
     */
    public static List<MovieFragmentBox> parseMovieFragments(SeekableByteChannel input) throws IOException {
        MovieBox moov = null;
        LinkedList<MovieFragmentBox> fragments = new LinkedList<MovieFragmentBox>();
        for (Atom atom : getRootAtoms(input)) {
            if ("moov".equals(atom.getHeader().getFourcc())) {
                moov = (MovieBox) atom.parseBox(input);
            } else if ("moof".equalsIgnoreCase(atom.getHeader().getFourcc())) {
                fragments.add((MovieFragmentBox) atom.parseBox(input));
            }
        }
        for (MovieFragmentBox fragment : fragments) {
            fragment.setMovie(moov);
        }
        return fragments;
    }

    public static List<Atom> getRootAtoms(SeekableByteChannel input) throws IOException {
        input.position(0);
        List<Atom> result = new ArrayList<Atom>();
        long off = 0;
        Header atom;
        while (off < input.size()) {
            input.position(off);
            atom = Header.read(NIOUtils.fetchFrom(input, 16));
            if (atom == null)
                break;
            result.add(new Atom(atom, off));
            off += atom.getSize();
        }

        return result;
    }

    public static Atom atom(SeekableByteChannel input) throws IOException {
        long off = input.position();
        Header atom = Header.read(NIOUtils.fetchFrom(input, 16));

        return atom == null ? null : new Atom(atom, off);
    }

    public static class Atom {
        private long offset;
        private Header header;

        public Atom(Header header, long offset) {
            this.header = header;
            this.offset = offset;
        }

        public long getOffset() {
            return offset;
        }

        public Header getHeader() {
            return header;
        }

        public Box parseBox(SeekableByteChannel input) throws IOException {
            input.position(offset + header.headerSize());
            return NodeBox.parseBox(NIOUtils.fetchFrom(input, (int) header.getSize()), header, BoxFactory.getDefault());
        }

        public void copy(SeekableByteChannel input, WritableByteChannel out) throws IOException {
            input.position(offset);
            NIOUtils.copy(input, out, header.getSize());
        }
    }

    public static MovieBox parseMovie(File source) throws IOException {
        SeekableByteChannel input = null;
        try {
            input = readableFileChannel(source);
            return parseMovie(input);
        } finally {
            if (input != null)
                input.close();
        }
    }

    public static MovieBox createRefMovie(File source) throws IOException {
        SeekableByteChannel input = null;
        try {
            input = readableFileChannel(source);
            return createRefMovie(input, "file://" + source.getCanonicalPath());
        } finally {
            if (input != null)
                input.close();
        }
    }

    public static void writeMovie(File f, MovieBox movie) throws IOException {
        FileChannel out = null;
        try {
            out = new FileInputStream(f).getChannel();
            writeMovie(f, movie);
        } finally {
            out.close();
        }
    }

    public static void writeMovie(SeekableByteChannel out, MovieBox movie) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(16 * 1024 * 1024);
        movie.write(buf);
        buf.flip();
        out.write(buf);
    }

    public static Box cloneBox(Box box, int approxSize) {
        return cloneBox(box, approxSize, BoxFactory.getDefault());
    }

    public static Box cloneBox(Box box, int approxSize, BoxFactory bf) {
        ByteBuffer buf = ByteBuffer.allocate(approxSize);
        box.write(buf);
        buf.flip();
        return NodeBox.parseChildBox(buf, bf);
    }

    public static String getFourcc(Codec codec) {
        return codecMapping.get(codec);
    }

    public static ByteBuffer writeBox(Box box, int approxSize) {
        ByteBuffer buf = ByteBuffer.allocate(approxSize);
        box.write(buf);
        buf.flip();

        return buf;
    }
}