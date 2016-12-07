/*
 * Created on 2005-7-27
 *
 */
package net.joycool.wap.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.upload.FormFile;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author lbj
 * 
 */
public class FileUtil {
	public static int IMAGE_SIZE = 200;
	public static boolean uploadFile(FormFile file, String filePath,
			String fileURL) {
		String fileName = file.getFileName();
		String size = (file.getFileSize() + " bytes");

		try {
			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			filePath += "/" + fileURL;
			// retrieve the file data
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream stream = file.getInputStream();
			// zhul 2006-10-22 如果是上传图片，对图片进行缩小处理 start
			if (checkIconFileExt(getFileExt(file.getFileName().toLowerCase()))) {
				ImageUtil.scale(IMAGE_SIZE, stream, filePath);
			} else {
				// zhul 2006-10-22 如果是上传图片，对图片进行缩小处理 end
				// write the file to the file specified
				OutputStream bos = new FileOutputStream(filePath);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

		file.destroy();
		return true;
	}

	/**
	 * 
	 * @author guip
	 * @explain：显示猎物的上传，不进行缩小处理
	 * @datetime:2007-8-18 11:11:49
	 * @param file
	 * @param filePath
	 * @param fileURL
	 * @return
	 * @return boolean
	 */
	public static boolean uploadFiles(FormFile file, String filePath,
			String fileURL) {
		String fileName = file.getFileName();

		try {
			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			filePath += "/" + fileURL;
			// retrieve the file data
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream stream = file.getInputStream();

			OutputStream bos = new FileOutputStream(filePath);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
			bos.close();
			stream.close();

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

		file.destroy();
		return true;
	}

	/**
	 * 上传图片文件，不进行压缩
	 * 
	 * @param file
	 * @param filePath
	 * @param fileURL
	 * @return
	 */
	public static boolean uploadImage(FormFile file, String filePath,
			String fileURL, boolean compress) {
		String fileName = file.getFileName();

		try {
			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			filePath += "/" + fileURL;
			// retrieve the file data
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream stream = file.getInputStream();
			// zhul 2006-10-22 如果是上传图片，对图片进行缩小处理 start
			String ext = getFileExt(file.getFileName());
			if (compress
					&& checkIconFileExt(getFileExt(file.getFileName()
							.toLowerCase()))
					&& (ext != null && !ext.equalsIgnoreCase("gif"))) {
				ImageUtil.scale(IMAGE_SIZE, stream, filePath);
			} else {
				// zhul 2006-10-22 如果是上传图片，对图片进行缩小处理 end
				// write the file to the file specified
				OutputStream bos = new FileOutputStream(filePath);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

		file.destroy();
		return true;
	}
	
	// 也是上传图片的功能，注意调用的时候bak必须已经经过StringUtil.toSql处理
	public static String uploadImage(FormFile file, String filePath, String fileExt) {
		long t = getUniqueFileTime();
		String path2 = String.valueOf(t / 2592000000l);
		filePath += "/" + (path2); 
		String fileURL = "/" + (t % 2592000000l) + "." + fileExt;

		try {
			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			filePath += fileURL;
			// retrieve the file data
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream stream = file.getInputStream();
			// zhul 2006-10-22 如果是上传图片，对图片进行缩小处理 start
			String ext = getFileExt(file.getFileName());
			if (checkIconFileExt(fileExt)
					&& (ext != null && !ext.equalsIgnoreCase("gif"))) {
				ImageUtil.scale(IMAGE_SIZE, stream, filePath);
			} else {
				// zhul 2006-10-22 如果是上传图片，对图片进行缩小处理 end
				// write the file to the file specified
				OutputStream bos = new FileOutputStream(filePath);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
		file.destroy();
		
		return path2 + fileURL;
	}
	
	
	public static String getFileExt(String fileName) {
		if (fileName == null) {
			return null;
		}
		int i = fileName.lastIndexOf(".");
		if (i == -1) {
			return null;
		}
		return fileName.substring(i + 1, fileName.length());
	}

	public static int tail = 0;
	public static String getUniqueFileName() {
		if(tail > 20)
			tail = 0;
		else
			tail++;
		return String.valueOf(System.currentTimeMillis() + tail);
	}
	public static long getUniqueFileTime() {
		if(tail > 20)
			tail = 0;
		else
			tail++;
		return System.currentTimeMillis() + tail;
	}

	public static boolean checkIconFileExt(String fileExt) {
		if (!fileExt.equalsIgnoreCase("gif")
				&& !fileExt.equalsIgnoreCase("jpg")
				&& !fileExt.equalsIgnoreCase("png")
				&& !fileExt.equalsIgnoreCase("wbmp")
				&& !fileExt.equalsIgnoreCase("dgif")) {
			return false;
		}
		return true;
	}

	public static boolean checkImageFileExt(String fileExt) {
		if (!fileExt.equalsIgnoreCase("gif")
				&& !fileExt.equalsIgnoreCase("jpg")
				&& !fileExt.equalsIgnoreCase("png")
				&& !fileExt.equalsIgnoreCase("wbmp")
				&& !fileExt.equalsIgnoreCase("dgif")) {
			return false;
		}
		return true;
	}

	public static boolean checkRingFileExt(String fileExt) {
		if (!fileExt.equalsIgnoreCase("mid")
				&& !fileExt.equalsIgnoreCase("amr")
				&& !fileExt.equalsIgnoreCase("mp3")
				&& !fileExt.equalsIgnoreCase("mmf")) {
			return false;
		}
		return true;
	}

	public static boolean checkSoftwareFileExt(String fileExt) {
		if (!fileExt.equalsIgnoreCase("jar")
				&& !fileExt.equalsIgnoreCase("jad")) {
			return false;
		}
		return true;
	}

	public static boolean checkVideoFileExt(String fileExt) {
		if (!fileExt.equalsIgnoreCase("jar")
				&& !fileExt.equalsIgnoreCase("jad")) {
			return false;
		}
		return true;
	}

	public static void dealImage(String inURL, int newWidth, String outURL) {
		try {
			File _file = new File(inURL);
			if(!_file.canRead())
				return;
			// 构造Image对象
			Image src = javax.imageio.ImageIO.read(_file);
			if(src == null)
				return;
			// 得到源图宽
			int width = src.getWidth(null);
			// 得到源图长
			int height = src.getHeight(null);

			BufferedImage tag = new BufferedImage(newWidth, height * newWidth
					/ width, BufferedImage.TYPE_INT_RGB);
			// 绘制缩小后的图
			tag.getGraphics().drawImage(src, 0, 0, newWidth,
					height * newWidth / width, null);

			// 输出到文件流
			FileOutputStream out = new FileOutputStream(outURL);

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getThumbnailName(String imageName) {
		if (imageName == null) {
			return null;
		} else {
			imageName = imageName.substring(0, imageName.lastIndexOf('.') - 1)
					+ "_tn.jpg";
			return imageName;
		}
	}

	public static boolean copyFile(File fromFile, File toFile) {
		try {
			if (!toFile.exists()) {
				toFile.createNewFile();
			}

			FileInputStream fis = new FileInputStream(fromFile);
			FileOutputStream fos = new FileOutputStream(toFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = fis.read(buf)) > -1) {
				fos.write(buf, 0, len);
			}
			fis.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean copyDir(File fromDir, File toDir) {
		if (!fromDir.exists()) {
			return false;
		}

		if (!toDir.exists()) {
			toDir.mkdirs();
		}

		File[] fromFiles = fromDir.listFiles();
		if (fromFiles == null) {
			return true;
		}
		File fromFile = null;
		File toFile = null;

		int len = fromFiles.length;
		for (int i = 0; i < len; i++) {
			fromFile = fromFiles[i];
			toFile = new File(toDir.getAbsolutePath() + "/"
					+ fromFile.getName());
			if (!copyFile(fromFile, toFile)) {
				return false;
			}
		}

		return true;
	}

	public static void writeImage(HttpServletResponse response, String filePath) {
		try {
			InputStream is = new FileInputStream(new File(filePath));
			OutputStream os = response.getOutputStream();
			byte[] buffer = new byte[256];
			int len = -1;
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeImageURL(HttpServletRequest request,
			HttpServletResponse response, String filePath) {
		String file = request.getSession().getServletContext().getRealPath(
				filePath);
		response.setContentType("image/gif");
		writeImage(response, file);
	}
}
