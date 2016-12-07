/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.action.wgame;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author bomb
 *  
 */
public class GuessAction extends CustomAction {
	static String GUESS_USER_KEY = "guess_user_key";
	static int GUESS_DIGIT = 4;
	static int MAX_TURN = 10;
	
	static int rewardExps[] = {100, 90, 80, 70, 60, 50, 40, 30, 20, 10};
	static int rewardMoneys[] = {100000, 90000, 80000, 70000, 60000, 50000, 40000, 30000, 20000, 10000};

	public static String title = "猜数字";
	
	UserBean loginUser = null;
	GuessUserBean guessUser = null;
	
	public GuessAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		check();
	}

	private void check() {
		guessUser = (GuessUserBean)session.getAttribute(GUESS_USER_KEY);
		if(guessUser == null)
		{
			guessUser = new GuessUserBean();
			session.setAttribute(GUESS_USER_KEY, guessUser);
		}
	}

	public GuessUserBean getGuessUser() {
		return guessUser;
	}
	
	
	public void index() {

	}
	
	public void guess() {
		
	}
	
	public void reset() {
		String r = request.getParameter("reset");
		if(r == null)
			return;
		
		if(!guessUser.isGameOver())
			guessUser.addLog("---无奈的分隔符---");
		tip("reset");
		guessUser.reset();
	}
	
	public void doGuess() {
		String guess = request.getParameter("guess");
		if(guess == null || guessUser.isGameOver())		// 错误的输入，或者游戏已经结束
			return;
		
		byte[] g = guess.trim().getBytes();
		if(g.length < GUESS_DIGIT)
			return;
		
		String result = guessUser.guessResult(g);
		guessUser.addLog(guess + "-->" + result);
		if(guessUser.isWin()) {	// 游戏结束，给予奖励
			guessUser.addLog("---快乐的分隔符---");
			int turn = guessUser.getTurn();
			int exp = rewardExps[turn];
			int money = rewardMoneys[turn];
			tip = "猜对啦！<br/>奖励乐币" + money + "，获得经验值" + exp;
			rewardExp(exp);
			rewardMoney(money);
		} else {
			tip = "猜错啦！你还有" + (MAX_TURN - guessUser.getTurn() - 1) + "次机会！";
		}
		
		tip("ok");
		
		request.setAttribute("guess", guess);
		request.setAttribute("result", result);
		
		guessUser.addTurn();
	}	
	
	// 奖励经验值
	private void rewardExp(int point) {
		if(point > 0)
			RankAction.addPoint(loginUser, point);
	}

	// 奖励乐币
	private void rewardMoney(int point) {
		if(point > 0)
			UserInfoUtil.updateUserCash(loginUser.getId(), point,
				UserCashAction.GAME, "猜数字给用户增加" + point + "乐币");

	}
}
