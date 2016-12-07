package jc.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 处理验证码
 * 
 * @author maning
 */
public class VerifyUtil3 {

	public static String ATTACH_ROOT = Constants.RESOURCE_ROOT_PATH + "verify/";
	public static String ATTACH_URL_ROOT = "/rep/verify/";
	
	public static long VERIFY_COUNT = 0l;	// 验证码生成次数
	public static HashMap verifyMap = new HashMap();
	
	/**
	 * 加入缓存
	 * @param key
	 */
	public static void addToMap(Object key){
		Object value = verifyMap.get(key);
		if (value == null) {
			int[] newone = {1};
			verifyMap.put(key, newone);
		} else {
			int[] count = (int[])value;
			count[0]++;
		}
	}
	
	/**
	 * 根据key返回错误的次数
	 * @param key
	 * @return
	 */
	public static int getVerifyCountByKey(Object key){
		Object value = verifyMap.get(key);
		if (value == null){
			return 0;
		}
		return ((int[])value)[0];
	}
	
	/**
	 * 生成一个默认参数的验证码<br/>
	 * 60*20,300条干扰线,4位数字,18号字体.
	 * @return
	 */
	public static BufferedImage getVerifyJpgDef(HttpServletRequest request) {
		return getVerifyJpg(60, 20, 300, 4, 18, ATTACH_ROOT,request);
	}
	
	/**
	 * 生成一个验证码图片<br/>
	 * 生成的图片格式为jpg.以当前时间的毫秒命名图片名.
	 * 
	 * @param width
	 *            图片宽度(必须10以上)
	 * @param height
	 *            图片高度(必须10以上)
	 * @param disturb
	 *            干扰线的数量(必须10以上)
	 * @param codeLength
	 *            验证码长度(必须2个以上)
	 * @param fontSize
	 *            字号(不可小于20)
	 * @param request
	 * @return 返回image和验证码
	 */
	public static BufferedImage getVerifyJpg(int width, int height, int disturb,
			int codeLength, int fontSize, String path,
			HttpServletRequest request) {

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

		// 生成图片
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

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
			rand = String.valueOf((char)('a' + RandomUtil.nextInt(20)));
			verify += rand;
			g.setColor(new Color(20 + RandomUtil.nextInt(110), 20 + RandomUtil
					.nextInt(110), 20 + RandomUtil.nextInt(110)));
			// 写验证码(字符串,x,y)
			g.drawString(rand, 13 * i + 6, 16);
		}

		// 生成图片
		g.dispose();
		VERIFY_COUNT++;
		HttpSession session = request.getSession();
		session.setAttribute("verifyCode", verify);
		return image;
	}

	/**
	 * 根据指定的范围,随机生成颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	public static Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + RandomUtil.nextInt(bc-fc);
		int g = fc + RandomUtil.nextInt(bc-fc);
		int b = fc + RandomUtil.nextInt(bc-fc);
		return new Color(r, g, b);
	}

	public static boolean verify(int id, String ip, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		String picVerify = request.getParameter("v");
		if(picVerify != null && session != null) {
			String verify = null;
			if(session != null)
				verify = (String)session.getAttribute("verifyCode");
			if (verify == null){
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "验证码错误.");
				return false;
			} else {
				session.removeAttribute("verifyCode");
				String inputVerify = request.getParameter("ver");
				if (inputVerify == null || (!inputVerify.equals(verify))){
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "验证码错误.");
					return false;
				} 
			}
		} else {
			int failCount = getVerifyCountByKey(new Integer(id));
			if(failCount >= 10) {
				return false;
			} else if (failCount >= 4){
				if(session != null && SecurityUtil.isMobile(request)) {	// 如果是手机，超过10次才要求验证码
					return true;
				} else 
					return false;
			}
		}

		return true;
	}

	public static void logFail(int id, String password, String ip) {
		addToMap(new Integer(id));
		SqlUtil.executeUpdate("insert into login_fail set user_id=" + id + ",create_time=now(),password='"
				+ StringUtil.toSql(password) + "',ip='" + ip + "'", 5);
	}

	public static void logSuccess(int uid) {
		verifyMap.remove(new Integer(uid));
	}
}