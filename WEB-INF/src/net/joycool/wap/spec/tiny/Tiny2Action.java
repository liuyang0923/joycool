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
 * @datetime:2015-02-25
 */
public class Tiny2Action extends CustomAction{
	public static Tiny2World world = Tiny2World.one;
	
	int userId = 0;
	Tiny2Game game = null;
	
	public long now = 0;

	public Tiny2Action(HttpServletRequest request) {
		super(request);
		check(); // check user
	}
	
	public Tiny2Action(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		check(); // check user
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserGame(Tiny2Game game) {
		if(userId > 0)		// 如果是非登陆用户，从session取
			world.setGame(userId, game);
		else
			session.setAttribute("tiny2game", game);
	}
	public Tiny2Game getUserGame() {
		Tiny2Game game;
		if(userId > 0)
			game = world.getGame(userId);
		else
			game = (Tiny2Game)session.getAttribute("tiny2game");
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
	public void chooseGame(Tiny2Game game) throws ServletException, IOException {
		String url = StringUtil.getURI(request);
		chooseGame(game, url);
	}
	public void chooseGame(Tiny2Game game, String url) throws ServletException, IOException {
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
	
	/**
	 * @return Returns the game.
	 */
	public Tiny2Game getGame() {
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
	public boolean checkGame(int gameId) {
		return checkGame();
	}
	
	// 检查是否完成游戏，如果未完成，执行后需要立刻跳转，返回null
	public boolean checkGame() {
		Tiny2Game game = getGame(); // 当前游戏，如果结束，会自动删除，当前页面必须自己处理这个game，例如保存
		try {
			if (game != null) {
				if (game.isStatusPlay()) {
					game.checkExpired(System.currentTimeMillis());
					redirect(game.getGameURL());
					return true;
				}
				if(System.currentTimeMillis() - game.finishTime > Tiny2Game.finishDelay) {
					game.init();
					redirect(game.getGameURL());
					return true;
				}
				String url = StringUtil.getURI(request);
				if (!url.equals(game.getReturnURL())) {		// 非本游戏
					redirect(game.getReturnURL());
					return true;
				}
				setUserGame(null);
			}
		} catch(Exception e) {
			return true;
		}
		return false;
	}
	
	public void startGame(Tiny2Game newGame, int gameId) {
		startGame(newGame);
	}
	
	public void startGame(Tiny2Game newGame) {
		try {
			Tiny2Game game = newGame.copy();
			chooseGame(game);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
