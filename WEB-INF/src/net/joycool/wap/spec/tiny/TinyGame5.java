package net.joycool.wap.spec.tiny;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 小型嵌入游戏: 最多个数的图案
 * @datetime:1007-10-24
 */
public class TinyGame5 extends TinyGame {
	public static char[] mark = {'☆','★','○','●','◇','◆','□','■','△','▲'};
	
	int count;		// 需要进行几个数学运算，例如是3，那么要连续答对3题
	int type;		// 有几种图案
	int total;		// 有几个图案，一排8个
	
	int correct = 0;	// 已经答对的数量
	boolean lastWin = true;		// 上一题是否正确
	
	String show;	// 答案
	int[] answer; 
	
	/**
	 * @return Returns the options.
	 */
	public String getShow() {
		return show;
	}
	public TinyGame5(int count, int type, int total) {
		this.count = count;
		this.type = type;
		this.total = total;
	}
	public String getName() {
		return "统计" + count + "-" + type + "-" + total;
	}
	
	public boolean isCorrect(int option) {
		int ans = answer[option];
		for(int i = 0;i < type;i++) {
			if(answer[i] > ans) {
				return false;
			}
		}
		return true;
	}
	// 答题
	public void answer(int option) {
		correct++;
		lastWin = isCorrect(option);
		if(!lastWin)	
			correct = 0;

		if(correct < count)
			reset();
		else
			setStatus(1);
	}
	// 生成新的题目
	public void reset() {
		answer = new int[type];
		StringBuilder sb = new StringBuilder(total * 2);
		for(int i = 0;i < total;i++) {
			if(i != 0 && i % 8 == 0)
				sb.append("<br/>");
			int m = RandomUtil.nextInt(type);
			answer[m]++;
			sb.append(mark[m]);
		}
		show = sb.toString();
	}


	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getGameId() {
		return 1;
	}

	public String getGameURL() {
		return "/tiny/5q.jsp";
	}

	public boolean isLastWin() {
		return lastWin;
	}

	public void init() {
		reset();
	}

}
