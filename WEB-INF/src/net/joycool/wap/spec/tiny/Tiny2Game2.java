package net.joycool.wap.spec.tiny;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 小型嵌入游戏: 数学运算……（图片型）
 * @datetime:1007-10-24
 */
public class Tiny2Game2 extends Tiny2Game {
	
	public boolean lastWin = true;		// 上一题是否正确
	public int failCount = 0;
	public long unlockTime;		// 解锁时间
	public QuestionSet question;
	
	
	
	public static Color[] colors = {new Color(255,0,0),new Color(0,160,0),new Color(50,50,255)};
	public static String[] colorNames = {"红色", "绿色", "蓝色"};
	public static Color backgroundColor = new Color(255, 255, 255);
	public static Color disturbColor = new Color(220, 220, 220);
	public static int[] mainColorCounts = {2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4};	// 几个数相加？
	
	public static List questionList = new ArrayList();
	
	public static int maxQuestion;	// 最大缓存题目数量
	public static long timeout;	// 超时重置所有图片
	
	public static long nextTime;	// 下一次重置题目的时间
	public static int lockTime = 15;		// failCount超过maxFailCount后，加锁的时间基础(秒)
	public static int maxFailCount = 5;	// 超过后，开始锁

	public Tiny2Game2(int set, int time) {
		maxQuestion = set;
		timeout = time * 1000l;
		nextTime = System.currentTimeMillis() + timeout;
	}
	public String getName() {
		return "加一加";
	}
	
	// 如果已经锁过了，解锁
	public int lockTime() {	
		return (int) ((unlockTime - System.currentTimeMillis()) / 1000);
	}

	// 答题
	public void answer(int option) {
		long now = System.currentTimeMillis();
		if(checkExpired(now))
			return;
		lastWin = (question.answer == option);

		if(lastWin) {
			finishTime = now;
			setStatus(1);
		} else {
			failCount++;
			if(failCount >= maxFailCount)
				unlockTime = now + lockTime * 1000l * (failCount - maxFailCount + 1);	// 错的越多，锁的时间越长
			reset();
		}
	}

	// 生成新的题目
	public void reset() {
		question = null;
		long now = System.currentTimeMillis();
		synchronized (questionList) {
			if (now > nextTime) {
				nextTime = now + timeout;
				questionList.clear();
			}
			if (questionList.size() < maxQuestion) { // 逐步生成题目，达到上限后就每次取缓存
				question = new QuestionSet();
				questionList.add(question);
			}
			if (question == null) {
				question = (QuestionSet) RandomUtil.randomObject(questionList);
			}
		}
	}

	public String getGameURL() {
		return "/tiny2/2q.jsp";
	}

	public boolean isLastWin() {
		return lastWin;
	}

	public void init() {
		super.init();
		failCount = 0;
		reset();
	}
	
	public static class QuestionSet {
		public int answer;	// 正确的答案
		public byte[] image;
		public int mainColor;
		public int[] answers;
		
		public String getColorName() {
			return colorNames[mainColor];
		}
		
		public QuestionSet() {
			BufferedImage image = getVerifyJpgDef();
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			try {
				javax.imageio.ImageIO.write(image, "JPEG", baos);
			} catch (IOException e) {
				return;
			}
			this.image = baos.toByteArray();
			
			answers = new int[5];
			int r = answer - RandomUtil.nextInt(5);
			for (int i = 0; i < 5; i++) {
				answers[i] = r + i;
			}
		}
		
		/**
		 * 生成一个默认参数的验证码<br/>
		 * 60*20,300条干扰线,4位数字,18号字体.
		 * @return
		 */
		public BufferedImage getVerifyJpgDef() {
			return getVerifyJpg(60, 20, 50, 5, 20);
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
		public BufferedImage getVerifyJpg(int width, int height, int disturb, int codeLength, int fontSize) {

			if (width < 10)
				width = 10;
			if (height < 10)
				height = 10;

			// 生成图片
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();

			// 随机背景色
			g.setColor(backgroundColor);

			// 填充
			g.fillRect(0, 0, width, height);

			// 设定字体(字体名,样式,字号)
			g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));

//			// 生成N条干扰线
//			g.setColor(getRandColor(200, 230));
//			for (int i = 0; i < disturb; i++) {
//				int x = RandomUtil.nextInt(width);
//				int y = RandomUtil.nextInt(height);
//				int xl = RandomUtil.nextInt(30);
//				int yl = RandomUtil.nextInt(30);
//				g.drawLine(x, y, x + xl, y + yl);
//			}
			
			int[] code = new int[codeLength];
			int[] color = new int[codeLength];	// color index
			mainColor = RandomUtil.nextInt(colors.length);
			
			int[] notMainColor = new int[colors.length - 1];
			for(int i = 0;i < colors.length;i++) {
				if(i < mainColor)
					notMainColor[i] = i;
				else if(i > mainColor)
					notMainColor[i - 1] = i;
			}
			for(int i = 0;i < codeLength;i++) {
				color[i] = -1;
				code[i] = RandomUtil.nextInt(9);
			}
			int mainColorCount = mainColorCounts[RandomUtil.nextInt(mainColorCounts.length)];
			if(mainColorCount > codeLength)
				mainColorCount = codeLength;
			int[] tempColor = new int[codeLength];
			for(int i = 0;i < codeLength;i++)
				tempColor[i] = i;
			int leftCount = codeLength;
			for(int i = 0;i < mainColorCount;i++) {
				int get;
				if(leftCount > 1)
					get = RandomUtil.nextInt(leftCount);
				else
					get = 0;
				color[tempColor[get]] = mainColor;
				tempColor[get] = tempColor[leftCount - 1];
				leftCount--;
			}
			
			
			int start = 0;
			// 写干扰数字
			g.setColor(disturbColor);
			for(int i = 0;i < codeLength;i++) {
				start += RandomUtil.nextInt(6);
				
				g.drawString(String.valueOf(RandomUtil.nextInt(9)), start-4, 12 + RandomUtil.nextInt(9));
				
				start += 8;
			}

			start = 0;
			// 写验证码(字符串,x,y)
			answer = 0;
			for(int i = 0;i < codeLength;i++) {
				start += RandomUtil.nextInt(6);
				if(color[i] != -1) {
					g.setColor(colors[color[i]]);
					answer += code[i];
				} else {
					g.setColor(colors[notMainColor[RandomUtil.nextInt(notMainColor.length)]]);
				}
				g.drawString(String.valueOf(code[i]), start, 14 + RandomUtil.nextInt(6));

				start += 8;
			}

			// 生成图片
			g.dispose();
			return image;
		}
	}
}
