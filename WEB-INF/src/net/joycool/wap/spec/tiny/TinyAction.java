package net.joycool.wap.spec.tiny;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 小型嵌入游戏
 * @datetime:1007-10-24
 */
public class TinyAction extends CustomAction{
	public static TinyWorld world = TinyWorld.one;
	
	int userId = 0;
	TinyGame game = null;
	
	public long now = 0;
	
	public static long USER_INACTIVE = 30 * 1000 * 60;		// 用户无动作时间

	public TinyAction(HttpServletRequest request) {
		super(request);
		check(); // check user
	}
	
	public TinyAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		check(); // check user
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserGame(TinyGame game) {
		if(userId > 0)		// 如果是非登陆用户，从session取
			world.setGame(userId, game);
		else
			session.setAttribute("tinygame", game);
	}
	public TinyGame getUserGame() {
		TinyGame game;
		if(userId > 0)
			game = world.getGame(userId);
		else
			game = (TinyGame)session.getAttribute("tinygame");
		return game;
	}

	public void check() {
		UserBean loginUser = super.getLoginUser();
		if(loginUser != null)
			userId = loginUser.getId();

		game = getUserGame();
	}
	
	public String getReturnURL() {
		return game.getReturnURL();
	}
	public String getReturnURLWml() {
		return game.getReturnURL().replace("&", "&amp;");
	}
	// 判断游戏是否结束
	public boolean isGameOver() {
		if(game == null)
			return false;
		return game.isStatusOver();
	}
	
	// 结束游戏并返回数据
	public int gameEndResult() {
		setUserGame(null);
		return game.getResult();
	}
	
	// 彻底结束游戏，奖励已经得到
	public void setGameEnd() {
		game.setStatus(2);
	}
	public int getGameResult() {
		return game.getResult();
	}
	// 外部调用，开始一个游戏并自动跳转，返回0，如果有其他游戏进行中，也会跳转过去
	// 如果已经有同类有些，则跳转过去，如果同类游戏结束，返回游戏结果
	// 设置选择一个游戏
	public void chooseGame(TinyGame game) throws ServletException, IOException {
		String url = StringUtil.getURI(request);
		chooseGame(game, url);
	}
	public void chooseGame(TinyGame game, String url) throws ServletException, IOException {
		game.init();
		setUserGame(game);
		game.setReturnURL(url);
		redirect(game.getGameURL());
	}

	public void index() throws ServletException, IOException {
		int id = getParameterInt("g");
		if(id == 999) {
			setUserGame(null);
			id = 0;
		} else if(game != null) {
			if(game.isStatusPlay()) {
				redirect(game.getGameURL());
				tip("redirect");
				return;
			} else if(game.isStatusOver()) {
				game.setStatus(2);
			}
			tip("tip", "游戏结束，结果为" + game.getResult());
			id = 0;
		}
		setAttribute("id", id);
	}


	// 根据调用方，得到游戏并重定向进入
	public void r() {
//		try {
//			String url = (String)request.getAttribute("ret");
//			int gameId = ((Integer)request.getAttribute("game")).intValue();
//			switch(gameId) {
//			case 1: {
//				game = new TinyGame1(request);
//			} break;
//			case 2: {
//				game = new TinyGame2(request);
//			} break;
//			}
//			game.setUrl(url);
//			game.setGameId(gameId);
//			world.setGame(loginUser.getId(), game);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	
	
	/************** game1 ******************/
	// 提问
	public void question() {
	}
	
	// 回答
	public void answer() {
		if(game.isStatusPlay()) {
			TinyGame1 tg = (TinyGame1)game;
			int option = getParameterInt("o");	// 调用方id
			
			tg.answer(option);
		}
	}
	
	/************** game2 ******************/
	// 显示和……转
	public void view() {
		if(game.isStatusPlay()) {
			int direct = getParameterInt("d");	// 方向1-4
			if(direct > 0 && direct < 5) {
				TinyGame2 tg = (TinyGame2)game;
				int number = getParameterInt("n");	// 行数或者列数
				switch(direct) {
				case 1:
					tg.moveLeft(number);
					break;
				case 2:
					tg.moveRight(number);
					break;
				case 3:
					tg.moveUp(number);
					break;
				case 4:
					tg.moveDown(number);
					break;
				}
				tg.checkGame();
			}
		}
	}
	/************** game3 ******************/
	public void view3() {
		if(!isMethodGet()) {
			String guess = getParameterNoEnter("guess");
			if(guess != null) {
				TinyGame3 tg = (TinyGame3)game;
				tg.guess(guess);
			}
		}
	}
	/************** game4 ******************/
	public void view4() {
		if(game.isStatusPlay()) {
			int pos = getParameterIntS("p");	// 方向1-4
			if(pos >= 0) {
				TinyGame4 tg = (TinyGame4)game;
				tg.turn(pos);
				tg.checkGame();
			}
		}
	}
	/************** game5 ******************/
	// 提问2
	public void question2() {
	}
	
	// 回答2
	public void answer2() {
		if(game.isStatusPlay()) {
			TinyGame5 tg = (TinyGame5)game;
			int option = getParameterInt("o");	// 调用方id
			
			tg.answer(option);
		}
	}
	/************** game6 ******************/
	public void view6() {
		if(game.isStatusPlay()) {
			TinyGame6 tg = (TinyGame6)game;
			int from = getParameterIntS("f");
			int to = getParameterIntS("t");
			if(from >= 0 && to >= 0) {
				tg.move(from, to);
				tg.checkGame();
			}
		}
	}
	
	/************** game7 ******************/
	public void view7() {
		if(game.isStatusPlay()) {
			TinyGame7 tg = (TinyGame7)game;
			if(!tg.isLost()) {
				int x = getParameterIntS("x");
				int y = getParameterIntS("y");
				if(x >= 0 && y >= 0) {
					tg.open(x, y);
				}
			}
		}
	}
	/************** game8 ******************/
	public void view8() {
		if(game.isStatusPlay()) {
			TinyGame8 tg = (TinyGame8)game;
			int x = getParameterIntS("x");
			int y = getParameterIntS("y");
			if(x >= 0 && y >= 0) {
				tg.open(x, y);
			}
		}
	}
	/**
	 * @return Returns the game.
	 */
	public TinyGame getGame() {
		return game;
	}
	
	public void giveup() throws IOException {
		if(game != null) {
			game.setResult(-1);
			game.setStatus(1);
			redirect(game.getReturnURL());
		}
	}
	
	// 检查是否完成游戏，如果未完成，执行后需要立刻跳转，返回null
	public TinyAction checkGame(TinyGame newGame) {
		TinyGame game = getGame(); // 当前游戏，如果结束，会自动删除，当前页面必须自己处理这个game，例如保存
		try {
			if (game != null) {
				if (game.isStatusPlay()) {
					redirect(game.getGameURL());
					return null;
				}
				if (game.getId() != newGame.getId()) {		// 非本游戏
					redirect(game.getReturnURL());
					return null;
				}
				setUserGame(null);
			} else {
				chooseGame(newGame.copy());
				return null;
			}
		} catch(Exception e) {
			return null;
		}
		return this;
	}
}
