package com.bjhit.video.scale;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import com.bjhit.video.common.model.ColorSpace;
import com.bjhit.video.common.model.Picture;

/**
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-15 下午3:05:15
 * @version 1.0
 */
public class AWTUtil {
	public static BufferedImage toBufferedImage(Picture src) {
		BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), 5);

		toBufferedImage(src, dst);

		return dst;
	}

	public static void toBufferedImage(Picture src, BufferedImage dst) {
		byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
		int[] srcData = src.getPlaneData(0);
		for (int i = 0; i < data.length; ++i)
			data[i] = (byte) srcData[i];
	}

	public static Picture fromBufferedImage(BufferedImage src) {
		Picture dst = Picture.create(src.getWidth(), src.getHeight(), ColorSpace.RGB);
		fromBufferedImage(src, dst);
		return dst;
	}

	public static void fromBufferedImage(BufferedImage src, Picture dst) {
		int[] dstData = dst.getPlaneData(0);

		int off = 0;
		for (int i = 0; i < src.getHeight(); ++i)
			for (int j = 0; j < src.getWidth(); ++j) {
				int rgb1 = src.getRGB(j, i);
				dstData[(off++)] = (rgb1 >> 16 & 0xFF);
				dstData[(off++)] = (rgb1 >> 8 & 0xFF);
				dstData[(off++)] = (rgb1 & 0xFF);
			}
	}
}
