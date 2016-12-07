package jc.util.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import jc.util.VerifyUtil;
import net.joycool.wap.util.RandomUtil;

/**
 * 处理验证码
 * 
 * @author wuqk
 */
public class ImageCode extends VerifyUtil {

	public static class ChoiceBean {
		public BufferedImage image;
		public String[] choice;
		public int answer;
	}
	
	// 2012-4-5 wuqk ↓
	public static HashMap totalMap;
	static long makeTime = 0;
	static byte[] lock = new byte[0];

	public static ChoiceBean getAVerify() {
		ChoiceBean image = null;
		long now = System.currentTimeMillis();
		if (now - makeTime > 600000) {
			synchronized (lock) {
				if (now - makeTime > 600000) {
					makeTime = now;
					HashMap totalMap2 = new HashMap();
					for (int i = 0; i < 20; i++) {
						image = getVerifyJpgDef();
						totalMap2.put(Integer.valueOf(i), image);
					}
					totalMap = totalMap2;
				}
			}
		}
		return (ChoiceBean) totalMap.get(Integer.valueOf(RandomUtil.nextInt(20)));
	}
	
	public static ChoiceBean getVerifyJpgDef() {
		return getVerifyJpg(60, 20, 300, 4, 18, ATTACH_ROOT);
	}
	
	// 2012-4-5 wuqk ↑
	public static ChoiceBean getVerifyJpg(int width, int height, int disturb,
			int codeLength, int fontSize, String path) {

		if (width < 10)
			width = 10;
		if (height < 10)
			height = 10;
		if (disturb < 10)
			disturb = 10;
		if (codeLength < 2)
			codeLength = 2;
		if (fontSize < 20)
			fontSize = 20;

		if (path == null || "".equals(path))
			return null;

		ChoiceBean c = new ChoiceBean();
		// 生成图片
		c.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = c.image.getGraphics();

		// 随机背景色
		g.setColor(getRandColor(200, 250));

		// 填充
		g.fillRect(0, 0, width, height);

		// 设定字体(字体名,样式,字号)
		g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));

		// 生成N条干扰线
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < disturb; i++) {
			int x = RandomUtil.nextInt(width);
			int y = RandomUtil.nextInt(height);
			int xl = RandomUtil.nextInt(12);
			int yl = RandomUtil.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		String rand = "";
		String verify = "";
		for (int i = 0; i < codeLength; i++) {
			rand = String.valueOf(RandomUtil.nextInt(10));
			verify += rand;
			g.setColor(new Color(20 + RandomUtil.nextInt(110), 20 + RandomUtil
					.nextInt(110), 20 + RandomUtil.nextInt(110)));
			// 写验证码(字符串,x,y)
			g.drawString(rand, 13 * i + 6, 16);
		}
		c.choice = new String[]{verify};
		c.answer = 0;

		// 生成图片
		g.dispose();
		VERIFY_COUNT++;
		
		return c;
	}
}