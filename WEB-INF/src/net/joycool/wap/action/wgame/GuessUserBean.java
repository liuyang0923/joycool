package net.joycool.wap.action.wgame;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;


/**
 * @author bomb
 *
 */
public class GuessUserBean {
	public static int STATUS_PLAY = 0;
	public static int STATUS_WIN = 1;
	public static int STATUS_LOSE = 2;
	
	static int GUESS_DIGIT = 4;
	static int MAX_TURN = 10;
	
	byte[] answer = null;	// 当前局的答案
	SimpleGameLog log = new SimpleGameLog(10);	// 保存本局猜的每次答案
	int turn = 0;		// 次数
	int gameStatus = STATUS_LOSE;
	
	/**
	 * @return Returns the turn.
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * @param turn The turn to set.
	 */
	public void setTurn(int turn) {
		this.turn = turn;
	}

	public void addLog(String content) {
		log.add(content);
	}
	public String getLogString() {
		return log.toString();
	}
	
	/**
	 * @return Returns the answer.
	 */
	public byte[] getAnswer() {
		return answer;
	}

	/**
	 * @param answer The answer to set.
	 */
	public void setAnswer(byte[] answer) {
		this.answer = answer;
	}
	
	public boolean isGameOver() {
		return gameStatus != STATUS_PLAY;
	}
	public boolean isWin() {
		return gameStatus == STATUS_WIN;
	}
	
	public void reset() {
		this.answer = randomAnswer();
		turn = 0;
		gameStatus = STATUS_PLAY;
	}

	public GuessUserBean() {
		super();
		reset();
	}
	
	public void addTurn() {
		if(isGameOver())
			return;
		turn++;
		if(turn >= MAX_TURN) {
			gameStatus = STATUS_LOSE;
			log.add("---失望的分隔符---");
		}
	}
	
	/**
	 * 返回一个随机的4位数字的数组，每位数字不同
	 */
	public byte[] randomAnswer() {
		byte tmp[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		int left = 10;	// 保证每位数字不同，保存剩余个数
		byte answer[] = new byte[GUESS_DIGIT];
		
		for(int i = 0;i < GUESS_DIGIT;i++){
			int j = RandomUtil.nextInt(left);
			answer[i] = tmp[j];
			left--;
			tmp[j] = tmp[left];	// 把最后一位放到当前位置
		}
		
		return answer;
	}

	/**
	 * @return Returns the gameStatus.
	 */
	public int getGameStatus() {
		return gameStatus;
	}

	/**
	 * @param gameStatus The gameStatus to set.
	 */
	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	
	public String guessResult(byte[] guess) {
		if(guess.length < GUESS_DIGIT)
			return "0A0B";
			
		int A = 0, B = 0;
		for(int i = 0;i < GUESS_DIGIT;i++) {
			if(answer[i] == guess[i])
				A++;
			else {
				for(int j = 0;j < GUESS_DIGIT;j++)
					if(answer[j] == guess[i])
						B++;

			}
		}
		if(A == GUESS_DIGIT) {
			gameStatus = STATUS_WIN;
		}
		return A + "A" + B + "B";
	}
}
