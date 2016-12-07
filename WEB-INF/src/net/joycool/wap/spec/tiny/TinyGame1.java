package net.joycool.wap.spec.tiny;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 小型嵌入游戏: 数学运算……
 * @datetime:1007-10-24
 */
public class TinyGame1 extends TinyGame {

	int count;		// 需要进行几个数学运算，例如是3，那么要连续答对3题
	int choice;		// 选项个数
	int number;		// 加法个数
	
	int correct = 0;	// 已经答对的数量
	boolean lastWin = true;		// 上一题是否正确
	
	String[] options;	// 答案
	int[] answer; 
	
	/**
	 * @return Returns the options.
	 */
	public String[] getOptions() {
		return options;
	}

	public TinyGame1(int count, int choice) {
		this.count = count;
		this.choice = choice;
		this.number = 2;
	}
	public TinyGame1(int count, int choice, int number) {
		this.count = count;
		this.choice = choice;
		this.number = number;
	}
	public String getName() {
		return "加法" + count + "-" + choice + "-" + number;
	}
	
	public boolean isCorrect(int option) {
		int ans = answer[option];
		for(int i = 0;i < choice;i++) {
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
		for(int i = 0;i < choice;i++) {
			int[] n = RandomUtil.nextInts(number, 50, 80);
			answer[i] = 0;
			options[i] = String.valueOf(n[0]);
			for(int j = 0;j < number;j++) {
				answer[i] += n[j];
				if(j > 0) {
					options[i] += "+";
					options[i] += n[j];
				}
			}
		}
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
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
		return "/tiny/1q.jsp";
	}

	public boolean isLastWin() {
		return lastWin;
	}

	public void init() {
		options = new String[choice];
		answer = new int[choice];
		reset();
	}

}
