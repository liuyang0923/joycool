package net.joycool.wap.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {

	public static void scale(int maxSize, String sourceImageFile,
			String targetImageFile) {
		try {
			File sourceImage = new File(sourceImageFile); // 读入文件
			Image src = javax.imageio.ImageIO.read(sourceImage); // 构造Image对象
			int wideth = src.getWidth(null); // 得到源图宽
			int height = src.getHeight(null); // 得到源图长

			float scale = 1.0f;
			if (wideth > height) {
				scale = ((float) wideth) / maxSize;
			} else {
				scale = ((float) height) / maxSize;
			}

			int newWideth = Math.round(((float) wideth) / scale);
			int newHeight = Math.round(((float) height) / scale);

			BufferedImage tag = new BufferedImage(newWideth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, newWideth, newHeight, null); // 绘制缩小后的图
			FileOutputStream out = new FileOutputStream(targetImageFile); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag); // 近JPEG编码
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * zhul 2006-10-22 重载scale方法，对输入图片流处理缩小后保存
	 * 
	 * @param maxSize
	 * @param imageInputStream
	 * @param targetImageName
	 * @throws Exception
	 */
	public static void scale(int maxSize, InputStream imageInputStream,
			String targetImageName) {
		try {
			BufferedImage src = javax.imageio.ImageIO.read(imageInputStream);// 构建image对象
			if(src == null)
				return;
			int wideth = src.getWidth(null);// 得到源图宽
			int height = src.getHeight(null);// 得到源图长

			float scale = 1.0f;
			//liuyi 2006-10-24 按高度比例去缩放 start
//			if (wideth > height)
//				scale = (float) wideth / maxSize;
//			else				
			if(height > maxSize)
				scale = (float) height / maxSize;
//			else if(width > maxSize)
			//liuyi 2006-10-24 按高度比例去缩放 end

			int newWideth = Math.round(wideth / scale);
			int newHeight = Math.round(height / scale);

			BufferedImage tag = new BufferedImage(newWideth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, newWideth, newHeight, null);// 绘制缩小后的图
			FileOutputStream out = new FileOutputStream(targetImageName);// 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);// 近JPEG编码
			out.close();
			imageInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 拷贝源图的一部分
	 * @param xMove x方向偏移量
	 * @param yMove y方向偏移量
	 * @param sourceImageFile 源图文件
	 * @param targetImageFile 目标图文件
	 */
	public static void clip(int xMove, int yMove, String sourceImageFile,
			String targetImageFile) {
		try {
			File sourceImage = new File(sourceImageFile); // 读入文件
			Image src = javax.imageio.ImageIO.read(sourceImage); // 构造Image对象
			int wideth = src.getWidth(null); // 得到源图宽
			int height = src.getHeight(null); // 得到源图长
			
			int newWideth = wideth-Math.abs(xMove);
			int newHeight = height-Math.abs(yMove);
			
			if(newWideth<0 || newHeight<0){
				return;
			}

			BufferedImage tag = new BufferedImage(newWideth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, xMove, 0, wideth, height, Color.WHITE, null); // 绘制缩小后的图
			FileOutputStream out = new FileOutputStream(targetImageFile); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag); // 近JPEG编码
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			// ImageUtil.scale(70, "D:/1.jpg", "D:/2.jpg");
			FileInputStream input = new FileInputStream(new File(
					"f:/image/aa.gif"));
			ImageUtil.scale(90, input, "E:\\eclipse\\workspace\\joycool-portal\\img/home/k.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finish!");
	}
}