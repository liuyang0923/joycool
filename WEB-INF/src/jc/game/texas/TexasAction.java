package jc.game.texas;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;

public class TexasAction extends CustomAction {
	public static TexasGame[] boards = {new TexasGame(0, 0), new TexasGame(1, 1), new TexasGame(2, 1, 20, 5, 400, 4000), new TexasGame(3, 1, 20, 9, 400, 4000), new TexasGame(4, 1, 200, 5, 4000, 40000),
			new TexasGame(5, 0), new TexasGame(6, 0), new TexasGame(7, 0), new TexasGame(8, 0), new TexasGame(9, 0)};	// 10张桌子


	public static TexasService service = new TexasService();
	UserBean loginUser;

	public UserBean getLoginUser() {
		return loginUser;
	}
	public TexasAction() {
	}

	public TexasAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}

	public TexasAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		loginUser = super.getLoginUser();
	}

	// play.jsp的调用，返回玩家正在的局
	public void play() {
		TexasGame game = (TexasGame)session.getAttribute("texas");
		if(game == null)
			return;
		if(hasParam("start")) {
			if(game.canNext() && game.getStatus() != 1)
				game.start();
			return;
		}
		int act = getParameterIntS("a");
		if(act > 0 && game.getStatus() == 1) {
			if(game.getCurrentUserId() == loginUser.getId())	// 用户动作
				game.act(act, getParameterIntS("w"), true);
		}
		if(game.getStatus() == 1)
			game.checkTime();
	}
	
	public void sit() {
		TexasGame game = (TexasGame)session.getAttribute("texas");
		if(game == null)
			return;
		TexasUserBean tub =	TexasGame.getUserBean(loginUser.getId());
		if(tub == null) {		// 用户不存在，添加一个
			tub = TexasGame.addTexasUserBean(loginUser.getId());
		}
		int sit = getParameterIntS("sit");
		int money = getParameterInt("money");
		if(sit >= 0 && sit < game.getMaxUser() && money > 0) {
			
			if(money < game.getMinMoney() || money > game.getMaxMoney()) {
				tip("tip", "金额超出范围,无法坐下");
				return;
			}

			if(game.getGameType() == 1 && money > tub.getMoney()) {
				tip("tip", "积分不足");
				return;
			}
			if(tub.getBoardId() != -1 || !game.join(loginUser.getId(), sit, money)) {
				tip("tip", "无法坐下");
				return;
			}
			tub.setBoardId(game.getBoardId());
			tip("ok", "");
		}
	}
	
	public TexasUser getUser() {
		TexasGame game = (TexasGame)session.getAttribute("texas");

		return game.getUser(loginUser.getId());
	}
}
