package net.joycool.wap.spec.chess;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 中国象棋
 * @datetime:1007-10-24
 */
public class ChessAction extends CustomAction{
	
	public static int END_WAIT = 40 * 1000;		// 等待30秒后才能重新开始棋局
	
	UserBean loginUser;
	public static HashMap boards = null;
	
	public static ICacheMap chessCache = CacheManage.chess;
	public static ICacheMap chessUserCache = CacheManage.chessUser;
	public static ChessService service = new ChessService();
	
	public ChessAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		loginUser = super.getLoginUser();
		check();
	}
	public ChessAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
		check();
	}
	public static void check() {
		if(boards == null) {
			synchronized(ChessAction.class) {
				if(boards == null) {
					boards = new LinkedHashMap();
					List list = service.getRecords();
					for(int i = 0;i < list.size();i++) {
						ChessBean chess = (ChessBean)list.get(i);
						boards.put(new Integer(chess.getId()), chess);
						chess.load();
					}
				}
			}
		}
	}
	
	public ChessUserBean getUser() {
		if(loginUser == null)
			return null;
		Integer key = Integer.valueOf(loginUser.getId());
		synchronized(chessUserCache) {
			ChessUserBean user = (ChessUserBean)chessUserCache.get(key);
			if(user == null) {
				user = service.getChessUser("user_id=" + key);
				if(user == null) {
					user = new ChessUserBean(loginUser.getId());
					if(!service.addChessUser(user))
						return null;
				}
				chessUserCache.put(key, user);
			}
			return user;
		}
	}
	public static ChessUserBean getChessUser(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(chessUserCache) {
			ChessUserBean user = (ChessUserBean)chessUserCache.get(key);
			if(user == null) {
				user = service.getChessUser("user_id=" + key);
				if(user == null)
					return null;

				chessUserCache.put(key, user);
			}
			return user;
		}
	}
	
	public void index() {
		
	}
	// 查看棋盘，或者已经选择一个棋子准备移动
	public void view() {
		ChessUserBean chessUser = getUser();
		int boardId = getParameterInt("b");		// 选择观看或者参与的游戏id
		if(boardId > 0) {
			ChessBean chess = (ChessBean)boards.get(new Integer(boardId));
			if(chess == null)
				return;
			session.setAttribute("chessId", Integer.valueOf(boardId));
			return;
		}
		ChessBean chess = getCurrentChess();
		if(chess == null)
			return;
		int pickSide = getParameterInt("ps");	// 选择一方
		if(pickSide != 0 && chess.getUserSide(chessUser.getUserId()) != pickSide) {
			if(chessUser.getCurrent() != 0) {
				tip("tip", "所在棋局未结束,不能加入");
				return;
			}
			synchronized(chess) {
				if(pickSide == 1) {		// 选择红方
					if(chess.getUserId1() != 0) {
						tip("tip", "该座位已经有人,请重新选择");
					} else {
						chess.setUserId1(loginUser.getId());
						chessUser.setCurrent(chess.getId());
					}
				} else if(pickSide == 2) {
					if(chess.getUserId2() != 0) {
						tip("tip", "该座位已经有人,请重新选择");
					} else {
						chess.setUserId2(loginUser.getId());
						chessUser.setCurrent(chess.getId());
					}
				}
			}
		}
		if(hasParam("up")) {	// un pick side，离开本局，未开始前才能选择
			synchronized(chess) {
				if(chess.isStatusReady()) {
					chessUser.setCurrent(0);
					chess.setUserId1(0);
					chess.setUserId2(0);
				}
			}
		}
		if(chess.getDrawSide() != 0) {	// 有人请求和棋子
			int res = getParameterIntS("draw");
			if(res != -1) {
				if(chess.isStatusPlay() && res == 1) {	// 如果不是1表示拒绝
					chess.end(3);
				}
				chess.setDrawSide(0);
			}
		}
		if(chess.getUserId1() != 0 && chess.getUserId2() != 0) //add by leihy
			if(chess.isStatusReady()) {
				chess.setStatus(ChessBean.STATUS_PLAY);
				
				chess.lastMoveTime = System.currentTimeMillis();
				chess.setStartTime(chess.lastMoveTime);
				
				SqlUtil.executeUpdate("update mcoolgame.chess set status=1,user_id1=" + chess.getUserId1() +
						",user_id2=" + chess.getUserId2() + 
						",start_time=now() where id=" + chess.getId(), 4);
				if(chess.getUserId1() != loginUser.getId())
					NoticeAction.sendNotice(chess.getUserId1(), loginUser.getNickNameWml() + "应战象棋", NoticeBean.GENERAL_NOTICE, "/wgamehall/chess/v.jsp?b=" + chess.getId());
				if(chess.getUserId2() != loginUser.getId())
					NoticeAction.sendNotice(chess.getUserId2(), loginUser.getNickNameWml() + "应战象棋", NoticeBean.GENERAL_NOTICE, "/wgamehall/chess/v.jsp?b=" + chess.getId());
				
			}
		//int pos = getParameterInt("p");
	}
	// 移动一个棋子
	public void go(){
		ChessBean chess = getCurrentChess();
		int side = chess.getUserSide(loginUser.getId());
		if(side == -1)
			return;
		int operate = getParameterInt("o");
		if(operate == 1) {		// 认输
			chess.end(-side);
			return;
		}
		if(operate == 4) {		// 求和
			chess.setDrawSide(side);
			return;
		}
		if(operate == 2 && System.currentTimeMillis() - chess.getEndTime() > END_WAIT) {		//重新开始，结束棋局后需要等待30秒才能重新开
			chess.reset();
			SqlUtil.executeUpdate("update mcoolgame.chess set win_side=0,status=0,user_id1=0,user_id2=0,history='' where id=" + chess.getId(), 4);
			return;
		}
		if(operate == 3) {		//胜利
			if(chess.getTimeLeft() == 0) {
				chess.end(side);
			}
			//this.redirect("v.jsp");
			return;
		}
		int pos = getParameterInt("p");
		int from = getParameterInt("f");
		
		chess.move(from, pos);
		//记录走的历史至数据库
		service.updateRecord(chess);
	}
	
	public ChessBean getCurrentChess() {	//	 返回用户正在看的棋局
		Integer iid = (Integer)session.getAttribute("chessId");
		if(iid != null) {
			return (ChessBean)boards.get(iid);
		} else {
			return null;
		}
	}
	public UserBean getLoginUser() {
		return loginUser;
	}
	public ChessBean getChessHistory(int id) {
		Integer key = Integer.valueOf(id);
		synchronized(chessCache) {
			ChessBean chess = (ChessBean)chessCache.get(key);
			if(chess == null) {
				chess = service.getChessHistory("id=" + id);
				if(chess != null)
					chessCache.put(key, chess);
			}
			return chess;
		}
	}
	// 返回该玩家当前棋局，并进行检查
	public static int getChessUserCurrent(ChessUserBean chessUser) {
		if(chessUser.getCurrent() == 0)
			return 0;
		ChessBean chess = (ChessBean)boards.get(Integer.valueOf(chessUser.getCurrent()));
		if(chess == null || chess.getUserId1() != chessUser.getUserId() && chess.getUserId2() != chessUser.getUserId()) {
			chessUser.setCurrent(0);		// 不在棋局内，修复数据
			return 0;
		}
		return chess.getId();
	}
	public static void endChess(ChessBean chess) {
		if(chess.getMoveCount() > 10) {	//下棋的步数大于10步，就把该棋盘保存
			service.addHistory(chess);
			// 计算胜负保存
			ChessUserBean user1 = getChessUser(chess.getUserId1());
			ChessUserBean user2 = getChessUser(chess.getUserId2());
			int ap = calcPoint(user1.point, user2.point, chess.winSide);
			DbOperation dbOp = new DbOperation(4);
			if(chess.winSide == 3) {	// 平局
				user1.draw++;
				user2.draw++;
				dbOp.executeUpdate("update mcoolgame.chess_user set draw=draw+1 where user_id=" + user1.getUserId());
				dbOp.executeUpdate("update mcoolgame.chess_user set draw=draw+1 where user_id=" + user2.getUserId());
			} else if(chess.winSide == 1) {
				user1.win++;
				user2.lose++;
				dbOp.executeUpdate("update mcoolgame.chess_user set win=win+1 where user_id=" + user1.getUserId());
				dbOp.executeUpdate("update mcoolgame.chess_user set lose=lose+1 where user_id=" + user2.getUserId());
			} else if(chess.winSide == 2) {
				user1.lose++;
				user2.win++;
				dbOp.executeUpdate("update mcoolgame.chess_user set lose=lose+1 where user_id=" + user1.getUserId());
				dbOp.executeUpdate("update mcoolgame.chess_user set win=win+1 where user_id=" + user2.getUserId());
			}
			user1.point += ap;
			user2.point -= ap;
			dbOp.executeUpdate("update mcoolgame.chess_user set point=" + user1.point + " where user_id=" + user1.getUserId());
			dbOp.executeUpdate("update mcoolgame.chess_user set point=" + user2.point + " where user_id=" + user2.getUserId());
			dbOp.release();
		}
		// 棋局结束，重置玩家current
		if(chess.getUserId1() > 0)
			getChessUser(chess.getUserId1()).setCurrent(0);
		if(chess.getUserId2() > 0)
			getChessUser(chess.getUserId2()).setCurrent(0);
	}
	
	public static int calcPoint(int pointRed, int pointBlack, int winSide) {
		int k = Math.max(pointRed, pointBlack);
		if(k < 1800)
			k = 30;
		else if(k < 2000)
			k = 25;
		else if(k < 2200)
			k = 20;
		else if(k < 2400)
			k = 15;
		else
			k = 10;
		int dr = pointRed - pointBlack + 100;
		float exp = 1f - (float)(1 / (Math.pow(10f, dr/400f) + 1));
		if(winSide == 2)
			return (int) -(exp * k);
		if(winSide == 1)
			return (int) ((1 - exp) * k);
		return (int) ((0.5f - exp) * k);
	}
	
	
	
	public static byte[] lock = new byte[0]; 
	public static long statPeopleTime = 0;
	public static long STAT_PEOPLE_INTERVAL = 30 * 60 * 1000;
	public static void statPeople() {
		long now = System.currentTimeMillis();
		if(now < statPeopleTime)
			return;
		synchronized(lock) {
			if(now < statPeopleTime)
				return;
			DbOperation db = new DbOperation(4);
			db.executeUpdate("truncate table mcoolgame.chess_stat");
//			db.executeUpdate("update castle_user a,castle_user_resource b set a.people=sum(b.people) where a.uid=b.uid group by b.uid");
			db.executeUpdate("insert into mcoolgame.chess_stat (uid,point) (select user_id,point from mcoolgame.chess_user order by point desc)");
			db.release();
			statPeopleTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	
}
