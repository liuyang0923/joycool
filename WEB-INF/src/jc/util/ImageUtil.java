package jc.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import jc.imglib.ImgLibService;
import jc.imglib.ImgPoolBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {
	public static ImgLibService service = new ImgLibService();	// 用于上传图片的整体记录
	public static String ATTACH_ROOT = Constants.RESOURCE_ROOT_PATH + "box/";

	/**
	 * 处理（缩小,旋转）
	 * 
	 * @param src
	 *            待处理图像地址
	 * @param fitWidth
	 *            宽高限制
	 * @param fitHeight宽高限制
	 * @param dest
	 *            新图像名称。
	 * @throws IOException
	 */
	public static void fitImage(String src, int fitWidth, int fitHeight, String dest) throws IOException {
		dest = dest.toLowerCase();
		BufferedImage bi2 = ImageIO.read(new java.io.File(src));

		int width = bi2.getWidth();
		int height = bi2.getHeight();

		if (width <= fitWidth && height <= fitHeight) {
			if (dest.endsWith("gif")) { // 如果上传的是gif图片，生成gif格式，否则全部生成jpg
				ImageIO.write(bi2, "gif", new java.io.File(dest));
			} else {
				FileOutputStream out = new FileOutputStream(dest); // 输出到文件流
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(bi2); // 近JPEG编码
				out.close();
			}
			return;
		}
		boolean rotate = (height < width) ^ (fitWidth > fitHeight);
		if (rotate) {
			int tmp = height;
			height = width;
			width = tmp;
		}
		double scale = 1;
		if (width > fitWidth || height > fitHeight) {
			if (width * fitHeight > height * fitWidth) {
				scale = (double) fitWidth / width;
				width = fitWidth;
				height = (int) Math.round(height * scale);
			} else {
				scale = (double) fitHeight / height;
				height = fitHeight;
				width = (int) Math.round(width * scale);
			}
		}

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = bi.createGraphics();

		if (rotate) {
			g2d.rotate(Math.toRadians(90));
			g2d.translate(0, -width);
		}
		if (scale != 1)
			g2d.scale(scale, scale);
		g2d.drawImage(bi2, 0, 0, null);

		if (dest.endsWith("gif")) { // 如果上传的是gif图片，生成gif格式，否则全部生成jpg
			ImageIO.write(bi, "gif", new java.io.File(dest));
		} else {
			FileOutputStream out = new FileOutputStream(dest); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(bi); // 近JPEG编码
			out.close();
		}
	}

	/**
	 * 调整大小并保存
	 * 
	 * @param maxSize
	 *            最大长宽
	 * @param imageInputStream
	 *            输入流
	 * @param targetImageName
	 *            保存位置
	 */
	public static boolean scaleToSave(int maxSize, InputStream imageInputStream, String targetImageName) {
		try {
			BufferedImage src = ImageIO.read(imageInputStream);// 构建image对象
			if (src == null) {
				return false;
			}
			int wideth = src.getWidth();// 得到源图宽GGs
			int height = src.getHeight();// 得到源图长

			if (wideth <= maxSize && height <= maxSize) {// 不需要调整大小
				if (targetImageName.endsWith("gif")) { // 如果上传的是gif图片，生成gif格式，否则全部生成jpg
					ImageIO.write(src, "gif", new java.io.File(targetImageName));
				} else {
					FileOutputStream out = new FileOutputStream(targetImageName); // 输出到文件流
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					encoder.encode(src); // 近JPEG编码
					out.close();
				}
				return true;
			}

			float scale = 1.0f;
			if (height > maxSize) {
				scale = (float) height / maxSize;
			}
			int newWideth = Math.round(wideth / scale);
			int newHeight = Math.round(height / scale);

			BufferedImage tag = new BufferedImage(newWideth, newHeight, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, newWideth, newHeight, null);// 绘制缩小后的图
			if (targetImageName.endsWith("gif")) { // 如果上传的是gif图片，生成gif格式，否则全部生成jpg
				ImageIO.write(src, "gif", new java.io.File(targetImageName));
			} else {
				FileOutputStream out = new FileOutputStream(targetImageName); // 输出到文件流
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(tag); // 近JPEG编码
				out.close();
			}
			imageInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 调整大小并保存
	 * 
	 * @param maxSize
	 *            最大长宽
	 * @param File
	 *            com.jspsmart.upload.File
	 * @param targetImageName
	 *            保存位置
	 */
	public static boolean scaleToSave(int maxSize, com.jspsmart.upload.File file, String targetImageName) {
		return scaleToSave(maxSize, new java.io.ByteArrayInputStream(file.getBinaryData(),
				file.getM_startData(), file.getSize()), targetImageName);

	}

	
	public static ImgPoolBean uploadImage(byte[] input, String ext, int userId, int type) {
		return uploadImage(input, 0, input.length, ext, userId, type);
	}
	
	public static ImgPoolBean uploadImage(byte[] input, int off, int len, String ext, int userId, int type) {
		Base64u b64 = new Base64u(input, off, len);
		String md5 = b64.get64x();
		long md5int = b64.getIndex32();

		ImgPoolBean here = service.getImagePoolBean("md5_short=" + md5int + " and md5='" + md5 + "'");
		if (here != null) {
			return here;
		}
		// 上传
		long now = System.currentTimeMillis();
		String filePath = String.valueOf(now / 2592000000l);
		java.io.File path = new java.io.File(ATTACH_ROOT + filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
		String fileName = filePath + "/" + (now % 2592000000l) / 100000 + md5.substring(0, 2)
				+ "." + ext;
		
		here = new ImgPoolBean();
		here.setUserId(userId);
		here.setCatalog(type);
		here.setFileSize(input.length);
		here.setMd5(md5);
		here.setMd5Short(md5int);
		here.setFileName("/box/" + fileName);
		here.setFlag(0);
		service.addImagePool(here);

		if(!scaleToSave(600, new java.io.ByteArrayInputStream(input, off, len),
				ATTACH_ROOT + fileName))
			return null;
		return here;

	}

	public static void insertCheck(ImgPoolBean pool, int type, int id) {
		if(pool.getFlag() == 0)
			SqlUtil.executeUpdate("insert into img_check set id2=" + id + ",type=" + type + ",create_time=now(),file='" + pool.getFileName() + "',bak='',pool_id=" + pool.getId());
	}
}
