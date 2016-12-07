package net.joycool.wap.spec.rich;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 大富翁各种游戏
 * @datetime:1007-10-24
 */
public class RichGame {
	public static int LOTTERY_MONEY = 100;
	public static int LK_MONEY = 300;
	public static int NUM_MONEY2 = 100;
	
	public static int NUM_MAX = 12;
	// 猜数字
	public int numStart;		// 数字最小
	public int numEnd;			// 数字最大
	public int number;			// 正确数字 1-12
	
	// 要乐还要酷
	public RichUserBean lkUser;			// 之前坐庄的人
	public int lkChoice;		// 之前坐庄的人的选择 0-1
	
	// 彩票
	public int lottery;		// 彩票累计奖金
	public int lotNum;			// 本期彩票数字 1-6
	
	public void reset() {
		resetLottery();
		resetLk();
		resetNum();
	}
	
	public void resetLottery() {
		lottery = 500;
		lotNum = RandomUtil.nextInt(6) + 1;
	}
	public void resetLk() {
		lkUser = null;	// 无人坐庄
	}
	public void resetNum() {
		number = RandomUtil.nextInt(NUM_MAX) + 1;
		numStart = 1;
		numEnd = NUM_MAX;
	}
	
	// 判断是否中彩票，返回中的数量
	public int winLottery(int num) {
		lottery += LOTTERY_MONEY;
		if(num == lotNum) {	
			int tmp = lottery;
			resetLottery();
			return tmp;
		}
		return 0;
	}
	
	public boolean winLk(int option) {
		int tmp = lkChoice;
		resetLk();
		return tmp == option;
	}
	
	public boolean winNum(int option) {
		if(option == number) {
			resetNum();
			return true;
		}
		if(option < number) {
			numStart = option + 1;
		} else {
			numEnd = option - 1;
		}
		return false;
	}
}
