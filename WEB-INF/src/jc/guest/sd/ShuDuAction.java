package jc.guest.sd;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jc.guest.GuestHallAction;
import jc.guest.GuestUserInfo;
import net.joycool.wap.util.RandomUtil;

public class ShuDuAction extends GuestHallAction {
	public ShuDuAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public static ShuDuService service = new ShuDuService();
	public static int[] rant = { 0, 1, 1, 2, 3 };
	public static int[] exp = { 0, 1, 1, 2, 4 };

	// 获得我的数独
	public ShuDuBean getShuDu(int uid, int lvl) {
		ShuDuBean bean = null;
		// 未登录
		if (uid == 0) {
			// 创建一个数独,不存数据库
			bean = getNewShuDuBean(lvl);
		} else {
			if ("yes".equals(this.getParameterString("con"))) {
				bean = service.getShuDuBean("is_over=0 and uid= " + uid + " order by id desc limit 1");
			} else {
				bean = onceAgain(uid, lvl);
			}
		}
		session.setAttribute("shuDuBean", bean);
		return bean;
	}

	// 重玩一次
	public ShuDuBean onceAgain(int uid, int lvl) {
		ShuDuBean shuDuBean = null;
		if (uid > 0) {
			GuestUserInfo guestUser = getGuestUser(uid);
			int rants = rant[lvl];
			if (guestUser.getMoney() > rants) {
				// 根据等级扣钱
				updateMoney(uid, -rants);
				shuDuBean = getNewShuDuBean(lvl);
				shuDuBean.setUid(uid);
				shuDuBean.setStartTime(System.currentTimeMillis());
				service.upd("update shudu set is_over=1 where lvl=" + lvl + " and uid=" + uid);
				service.insertShuDuBean(shuDuBean);
				shuDuBean = service.getShuDuBean("lvl=" + lvl + " and uid= " + uid + " order by id desc limit 1");
				//防止刷经验用
				session.setAttribute("start", "yes");
			}
		} else {
			shuDuBean = getNewShuDuBean(lvl);
		}
		return shuDuBean;
	}

	// 查看完成
	public boolean smtShuDu(ShuDuBean bean) {
		String strOver = bean.getOver();
		String[] temp2 = strOver.split("_");
		int[] num = new int[81];
		for (int i = 0; i < temp2.length; i++) {
			String[] strTemp = temp2[i].split(",");
			num[i] = Integer.parseInt(strTemp[0]);
		}
		service.alterShuDuBean(bean);
		for (int i = 0; i < num.length; i++) {
			if (num[i] == 0)
				return false;
			if (!isCorret(num, i)) {
				return false;
			}
		}
		if ("yes".equals(session.getAttribute("start")) && bean.getUid() > 0) {
			session.removeAttribute("start");
			int uid = bean.getUid();
			GuestUserInfo guestUser = getGuestUser(uid);
			// 完成游戏保存到数据库
			bean.setIsOver(1);
			bean.setEndTime(System.currentTimeMillis());
			bean.setSpendTime(bean.getEndTime() - bean.getStartTime());
			int lvl = bean.getLvl();
			service.alterShuDuBean(bean);
			if (lvl == 1) {
				service.upd("insert into shudu_user (uid,lvl1_num) values(" + uid + ",1) ON DUPLICATE KEY update lvl1_num=lvl1_num+1");
			} else if (lvl == 2) {
				service.upd("insert into shudu_user (uid,lvl2_num) values(" + uid + ",1) ON DUPLICATE KEY update lvl2_num=lvl2_num+1");
			} else if (lvl == 3) {
				service.upd("insert into shudu_user (uid,lvl3_num) values(" + uid + ",1) ON DUPLICATE KEY update lvl3_num=lvl3_num+1");
			} else if (lvl == 4) {
				service.upd("insert into shudu_user (uid,lvl4_num) values(" + uid + ",1) ON DUPLICATE KEY update lvl4_num=lvl4_num+1");
			} else {
				return false;
			}
			addPoint(guestUser, exp[lvl]);
		}
		session.removeAttribute("shuDuBean");
		return true;
	}

	// 保存数独
	public void saveShuDu(ShuDuBean bean) {
		if (bean.getUid() > 0) {
			service.alterShuDuBean(bean);
		}
	}

	public ShuDuBean getNewShuDuBean (int lvl) {
		int[] shuDu = makeNewShuDu(lvl);
		StringBuilder shu = new StringBuilder();
		for (int i = 0; i < shuDu.length; i++) {
			int tempI = shuDu[i];
			shu.append(tempI);
			shu.append(",");
			if (tempI == 0) {
				shu.append("0");
			} else {
				shu.append("1");
			}
			shu.append("_");
		}
		String temp = shu.toString();
		ShuDuBean shuDuBean = new ShuDuBean();
		shuDuBean.setStart(temp.substring(0, temp.length() - 1));
		shuDuBean.setLvl(lvl);
		return shuDuBean;
	}

	public static int LVL_1 = 18;
	public static int LVL_2 = 27;
	public static int LVL_3 = 45;
	public static int LVL_4 = 54;
	
	public int[] makeNewShuDu(int lvl) {
		// 生成随机数字的源数组，随机数字从该数组中产生
		int[] num = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		// 存储数字的数组
		int[] n = new int[81];
		// 保存每个小格实数的次数
		int[] numTime = new int[81];
		// 记录每个小格的试数数组
		int[][] oldNum = new int[91][9];
		int time = 0;
		boolean isBack = false;// 是否进行回退
		for (int i = 0; i < n.length; i++) {
			if (isBack) {
				num = oldNum[i];
				time = numTime[i] + 1;
				isBack = false;
			}
			n[i] = generateNum(num, time);
			// 如果返回值为0，则代表卡住，退回处理
			if (n[i] == 0) {
				i -= 2;
				isBack = true;
				continue;
			}
			// 填充成功
			if (isCorret(n, i)) {
				numTime[i] = time;
				for (int m = 0; m < oldNum[i].length; m++) {
					oldNum[i][m] = num[m];
				}
				// 初始化time，为下一次填充做准备
				time = 0;
			} else { // 继续填充
				// 次数增加1
				time++;
				// 继续填充当前格
				i--;
			}
		}
		if (lvl <= 0 || lvl > 4)
			lvl = 1;
		if (lvl == 1) {
			Dig(n, LVL_1);
			//Dig(n, 1);
		} else if (lvl == 2) {
			Dig(n, LVL_2);
		} else if (lvl == 3) {
			Dig(n, LVL_3);
		} else if (lvl == 4) {
			Dig(n, LVL_4);
		}
		return n;
	}

	/**
	 * 随机挖洞
	 * 
	 * @param n
	 * @param num
	 * @return
	 */
	public int[] Dig(int[] n, int num) {
		while (num > 0) {
			int i = RandomUtil.nextInt(n.length);
			if (n[i] != 0) {
				n[i] = 0;
				num--;
			}
		}
		return n;
	}

	/**
	 * 是否满足行、列和3X3区域不重复的要求
	 * 
	 * @return true代表符合要求
	 */
	public boolean isCorret(int[] n, int num) {
		return (checkRow(n, num) & checkLine(n, num) & checkNine(n, num));
	}

	/**
	 * 检查行是否符合要求
	 * 
	 * @return true代表符合要求
	 */
	public boolean checkRow(int[] n, int num) {
		// 排列成9X9的方格时,num所在行row,所在列col
		int row = num / 9;
		int start = row * 9;
		int end = start + 9;
		if (n[num] == 0)
			return true;
		for (int i = start; i <= end - 1; i++) {
			if (num == i)
				continue;
			if (n[num] == n[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查列是否符合要求
	 * 
	 * @return true代表符合要求
	 */
	public boolean checkLine(int[] n, int num) {
		int col = num % 9;
		int end = 81 - col - 9;
		if (n[num] == 0)
			return true;
		for (int i = col; i <= end; i += 9) {
			if (num == i)
				continue;
			if (n[num] == n[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查3X3区域是否符合要求
	 * 
	 * @return true代表符合要求
	 */
	public boolean checkNine(int[] n, int num) {
		// 排列成9X9的方格时,num所在行row,所在列col
		int row = num / 9;
		int col = num % 9;
		// 排列成9X9的方格时,num所在小方格左上角坐标为x,y
		int x = row / 3 * 3;
		int y = col / 3 * 3;
		// 算出num所在小方格开始点start和结束点end
		int start = x * 9 + y;
		int end = start + 20;
		if (n[num] == 0)
			return true;
		for (int i = start; i <= end; i++) {
			if (i > start + 2 && i < start + 9) {
				i = start + 9;
			}
			if (i > start + 11 && i < start + 18) {
				i = start + 18;
			}
			if (num == i)
				continue;
			if (n[num] == n[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 产生1-9之间的随机数字 规则：生成的随机数字放置在数组8-time下标的位置，随着time的增加，已经尝试过的数字将不会在取到
	 * 说明：即第一次次是从所有数字中随机，第二次时从前八个数字中随机，依次类推， 这样既保证随机，也不会再重复取已经不符合要求的数字，提高程序的效率
	 * 这个规则是本算法的核心
	 * 
	 * @param time
	 *            填充的次数，0代表第一次填充
	 * @return
	 */
	public int generateNum(int[] num, int time) {
		// System.out.println("time==="+time);
		// 第一次尝试时，初始化随机数字源数组
		if (time == 0) {
			for (int i = 0; i < 9; i++) {
				num[i] = i + 1;
			}
		}
		// 第10次填充，表明该位置已经卡住，则返回0，由主程序处理退回
		if (time == 9) {
			return 0;
		}
		// 不是第一次填充
		// 生成随机数字，该数字是数组的下标，取数组num中该下标对应的数字为随机数字
		int ranNum = (int) (Math.random() * (9 - time));
		// 把数字放置在数组倒数第time个位置，
		int temp = num[8 - time];
		num[8 - time] = num[ranNum];
		num[ranNum] = temp;
		// 返回数字
		return num[8 - time];
	}

	public List getUserRank(int lvl) {
		if (lvl <= 0 || lvl > 4)
			lvl = 1;
		List list = new ArrayList();
		if (lvl == 1) {
			list = service
					.getShuDuUserList("lvl1_num>0 order by lvl1_num desc limit 100");
		} else if (lvl == 2) {
			list = service
					.getShuDuUserList("lvl2_num >0 order by lvl2_num desc limit 100");
		} else if (lvl == 3) {
			list = service
					.getShuDuUserList("lvl3_num >0 order by lvl3_num desc limit 100");
		} else if (lvl == 4) {
			list = service
					.getShuDuUserList("lvl4_num >0 order by lvl4_num desc limit 100");
		}
		return list;
	}

}