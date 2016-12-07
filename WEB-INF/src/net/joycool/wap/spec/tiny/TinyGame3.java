package net.joycool.wap.spec.tiny;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;

/**
 * @author zhouj
 * @explain： 传统的猜数字
 * @datetime:1007-10-24
 */
public class TinyGame3 extends TinyGame {
	int digit;		// 数字位数，默认是4
	int numberLimit;	// 可能出现的数字，例如如果是10，表示0-9，如果是7表示0-6
	char[] answer;	// 随机的数字
	int moveCount;		// 猜了几次
	
	SimpleGameLog log;	// 保存本局猜的每次答案
	
	public TinyGame3(int digit) {
		this.digit = digit;
		this.numberLimit = 10;
	}
	public TinyGame3(int digit, int numberLimit) {
		this.digit = digit;
		this.numberLimit = numberLimit;
	}
	public String getName() {
		return "猜数字" + digit + "-" + numberLimit;
	}

	public void init() {
		char tmp[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		int left = numberLimit;	// 保证每位数字不同，保存剩余个数
		answer = new char[digit];
		
		for(int i = 0;i < digit;i++){
			int j = RandomUtil.nextInt(left);
			answer[i] = tmp[j];
			left--;
			tmp[j] = tmp[left];	// 把最后一位放到当前位置
		}
		moveCount = 1;
		log = new SimpleGameLog(20);
	}
	
	public int guess(String input) {
		char[] guess = input.toCharArray();
		int length = guess.length;
		if(length > digit)		// 如果输入不足位数也可以，超过则限制
			length = digit;
			
		int A = 0, B = 0;
		for(int i = 0;i < length;i++) {
			if(answer[i] == guess[i])
				A++;
			else {
				for(int j = 0;j < digit;j++)
					if(answer[j] == guess[i])
						B++;

			}
		}
		if(A == digit) {
			setStatus(1);
		}

		log.add(input + "-->" + A + "A" + B + "B");
		moveCount++;
		return A;
	}
	
	public int getGameId() {
		return 3;
	}

	public String getGameURL() {
		return "/tiny/3v.jsp";
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	
	public String getLogString() {
		return log.toString();
	}

	public int getDigit() {
		return digit;
	}

	public void setDigit(int digit) {
		this.digit = digit;
	}

	public int getNumberLimit() {
		return numberLimit;
	}

	public void setNumberLimit(int numberLimit) {
		this.numberLimit = numberLimit;
	}
}
