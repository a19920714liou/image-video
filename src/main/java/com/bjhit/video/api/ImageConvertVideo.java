package com.bjhit.video.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import com.bjhit.video.codecs.H264Encoder;
import com.bjhit.video.codecs.H264Utils;
import com.bjhit.video.codecs.encode.H264FixedRateControl;
import com.bjhit.video.common.NIOUtils;
import com.bjhit.video.common.SeekableByteChannel;
import com.bjhit.video.common.model.ColorSpace;
import com.bjhit.video.common.model.Picture;
import com.bjhit.video.containers.Brand;
import com.bjhit.video.containers.MP4Packet;
import com.bjhit.video.containers.TrackType;
import com.bjhit.video.containers.muxer.FramesMP4MuxerTrack;
import com.bjhit.video.containers.muxer.MP4Muxer;
import com.bjhit.video.scale.AWTUtil;
import com.bjhit.video.scale.RgbToYuv420p;


/**
 * @description 将图片转换成视频
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-15 下午2:52:01
 * @version 1.0
 */
public class ImageConvertVideo {

	private SeekableByteChannel ch;
	private Picture toEncode;
	private RgbToYuv420p transform;
	private H264Encoder encoder;
	private ArrayList<ByteBuffer> spsList;
	private ArrayList<ByteBuffer> ppsList;
	private FramesMP4MuxerTrack outTrack;
	private ByteBuffer _out;
	private long frameNo = 0;
	private MP4Muxer muxer;
	private int width;
	private int height;
	
	public ImageConvertVideo(File out,int timescale) throws IOException {
		this.ch = NIOUtils.rwFileChannel(out);

		//创建像素与YUV之间的转换对象
		transform = new RgbToYuv420p(0, 0);

		//创建Muxer对象来存储所有的编码帧
		muxer = new MP4Muxer(ch, Brand.MP4);

		// muxer对象添加video track
		outTrack = muxer.addTrack(TrackType.VIDEO, timescale);
		
		// 分配足够的缓冲区给输出的帧
		_out = ByteBuffer.allocate(1920 * 1080 * 6);
		//创建H264编码对象
		encoder = new H264Encoder(new H264FixedRateControl(30));
		// Encoder extra data ( SPS, PPS ) to be stored in a special place of MP4
		spsList = new ArrayList<ByteBuffer>();
		ppsList = new ArrayList<ByteBuffer>();

	}
	
	public void encodeImage(BufferedImage bi,long timescale,long duration) throws IOException {
		if (toEncode == null || width != bi.getWidth() || height != bi.getHeight()) {
			width = bi.getWidth();
			height = bi.getHeight();
			toEncode = Picture.create(width, height, ColorSpace.YUV420);
		}

		// Perform conversion
		for (int i = 0; i < 3; i++)
			Arrays.fill(toEncode.getData()[i], 0);
		transform.transform(AWTUtil.fromBufferedImage(bi), toEncode);//将image转换成Picture对象
		
		if (bi.getWidth() * bi.getHeight() * 3 > _out.capacity()) {
			_out = ByteBuffer.allocate(bi.getWidth() * bi.getHeight() * 3);
		}
		//清空输出缓冲区
		_out.clear();
		//将图片数据编码为H264的帧，并存放到_out缓冲区中
		ByteBuffer result = encoder.encodeFrame(toEncode, _out,true,0);
		spsList.clear();
		ppsList.clear();
		H264Utils.wipePS(result,spsList, ppsList);//写入PPS和SPS
		//将mp4数据包添加到track的帧中
		outTrack.addFrame(new MP4Packet(result, frameNo, timescale, duration, frameNo, true, null, frameNo, 0));
		outTrack.addSampleEntry(H264Utils.createMOVSampleEntry(spsList, ppsList,1));
		frameNo++;
	}
	
	public void finish() throws IOException {
		//最后写头信息
		muxer.writeHeader();
		//关闭输出流
		NIOUtils.closeQuietly(ch);
	}
	
//	public static void main(String[] args) throws IOException {
//		ImageConvertVideo encoder = new ImageConvertVideo(new File("d:/video.mp4"),3);
//		int count = 0;
//		File dictory = new File("d:/images");
//		File[] pictures = dictory.listFiles();
//		Random random = new Random(1000);
//		try {
//			while (count++ < 1) {
//				for (int i = 0; i < pictures.length; i++) {
//					File file = pictures[i];
//					if (file.exists() && file.getName().endsWith(".jpeg")) {
//						BufferedImage bi = ImageIO.read(file);
//						encoder.encodeImage(bi,3,1);
//					}
//
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			encoder.finish();
//		}
//		
//	}
}

