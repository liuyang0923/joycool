package jc.game.worldcup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class WorldCupAction extends CustomAction{
	
	public static WorldCupService service = new WorldCupService();
	
	public static long statUpdateTime = 0;
	public static long STAT_UPDATE_INTERVAL = 60 * 1000;
	public static byte[] initLock = new byte[0];
//	public static long TWENTY_MINUTES = 20 * 60 * 1000;
	public static long ONE_MINUTES = 60 * 1000;
	public static long SIX_HOURS = 6 * 60 * 60 * 1000;
	public static long updateTime = 0;
	public static WcInfo wcInfo = null;
	
	public WorldCupAction(){}
	
	public WorldCupAction(HttpServletRequest request){
		super(request);
		if (SqlUtil.isTest){
			STAT_UPDATE_INTERVAL = 5 * 1000;
		}
		if (wcInfo == null){
			wcInfo = service.getInfo(" id=1");
		}
	}
	
	public static WcInfo getWcInfo() {
		return wcInfo;
	}

	public static void setWcInfo(WcInfo wcInfo) {
		WorldCupAction.wcInfo = wcInfo;
	}

	/**
	 * 更新排名
	 */
	public static void updateRank() {
		long now = System.currentTimeMillis();
		if (now < updateTime)
			return;
		synchronized (initLock) {
			if (now < updateTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table wc_rank");
			db.executeUpdate("insert into wc_rank (user_id,point) (select user_id,point from wc_user where point>10000 order by point desc,user_id)");
			db.release();
			updateTime = now + STAT_UPDATE_INTERVAL;
		}
	}
	
	/**
	 * 根据uid找user，不存在就创建一个
	 */
	public WcUser getUser(int uid){
		WcUser wcUser = service.getUser(" user_id=" + uid);
		if (wcUser == null){
			wcUser = new WcUser();
			UserBean user = this.getLoginUser();
			if (user == null){
				return null;
			}
			wcUser.setUserId(user.getId());
			wcUser.setPoint(10000);	// 送10000彩币
			service.addUser(wcUser);
		}
		return wcUser;
	}
	
	/**
	 * 检查看可不可以投注
	 */
	public WcMatch checkMatch(int matchId){
		WcMatch wcMatch = null;
		if (matchId <= 0){
			request.setAttribute("tip", "您要查询的比赛不存在.");
			return null;
		} else {
			wcMatch = WorldCupAction.service.getMatch(" id=" + matchId);
			if (wcMatch == null){
				request.setAttribute("tip", "您要查询的比赛不存在.");
				return null;
			} else {
				if (System.currentTimeMillis() > (wcMatch.getMatchTime() + (wcInfo.getLimitTime() * ONE_MINUTES))){
					request.setAttribute("tip", "比赛已开始" + wcInfo.getLimitTime() + "分钟以上,不可再投注.");
					return null;
				}
			}
		}
		return wcMatch;
	}
	
	/**
	 * 开始投注(比赛ID，betId:0胜1平2负)
	 */
	public boolean doBet(int matchId,int betId){
		UserBean loginUser = this.getLoginUser();
		if (loginUser == null){
			request.setAttribute("tip", "登陆用户不存在.");
			return false;
		}
		WcMatch wcMatch = checkMatch(matchId);
		if (wcMatch == null){
			return false;
		} else if (wcMatch.getFlag() == 1 || wcMatch.getShow() == 0){
			request.setAttribute("tip", "该比赛已关闭.");
			return false;
		} else if (wcMatch.getMatchTime() + (wcInfo.getLimitTime() * ONE_MINUTES) < System.currentTimeMillis()){
			request.setAttribute("tip", "不可为该场比赛投注.");
			return false;
		}
		WcUser wcUser = getUser(loginUser.getId());
		int bet = this.getParameterInt("bet");
		if (bet <= 0 || bet > 2000){
			request.setAttribute("tip", "投注错误.范围:1-2000彩币.<br/><a href=\"/wgame/wc/bet.jsp?t=1&amp;mid=" + wcMatch.getId() + "&amp;bid=" + betId + "\">返回投注页</a>");
			return false;
		}
		if (wcUser.getPoint() < bet){
			request.setAttribute("tip", "您的彩币不足.<br/><a href=\"/wgame/wc/bet.jsp?t=1&amp;mid=" + wcMatch.getId() + "&amp;bid=" + betId + "\">返回投注页</a>");
			return false;
		}
		if (betId < 0 || betId > 2){
			request.setAttribute("tip", "投注类型错误.<br/><a href=\"/wgame/wc/bet.jsp?t=1&amp;mid=" + wcMatch.getId() + "&amp;bid=" + betId + "\">返回投注页</a>");
			return false;
		}
		int count = SqlUtil.getIntResult("select sum(bet) from wc_bet where user_id=" + loginUser.getId() + " and match_id=" + matchId,5) + bet;
		if (count > 2000){
			request.setAttribute("tip", "您本场投注累计已经超过2000,不可再投.<br/><a href=\"/wgame/wc/bet.jsp?t=1&amp;mid=" + wcMatch.getId() + "&amp;bid=" + betId + "\">返回投注页</a>");
			return false;
		}
		// 操作用户彩币
		updatePoint(loginUser.getId(),0-bet);
		// 写入投注记录
		WcBet wcBet = new WcBet();
		wcBet.setUserId(loginUser.getId());
		wcBet.setMatchId(wcMatch.getId());
		wcBet.setTeam(wcMatch.getTeam1());
		wcBet.setBet(bet);	// 抽的金额
		wcBet.setFlag(betId);//0胜1平2负
		wcBet.setResult(2);	//0:输1:赢2:未处理
		service.addBet(wcBet);
		return true;
	}
	
	/**
	 * 操作用户的彩币(小于零为减少)
	 */
	public void updatePoint(int uid,int point){
		WcUser wcUser = getUser(uid);
		if (wcUser != null){
			if (point > 0){
				wcUser.setPoint(wcUser.getPoint() + point);
			} else {
				point = Math.abs(point);
				int tmp = wcUser.getPoint() - point;
				wcUser.setPoint(tmp > 0 ? tmp : 0);
			}
			SqlUtil.executeUpdate("update wc_user set `point`=" + wcUser.getPoint() + " where user_id=" + uid,5);
		}
	}
	
	/**
	 * 添加赛程
	 */
	public boolean addMatch(WcMatch wcMatch){
		if (wcMatch != null){
			service.addMatch(wcMatch);
		}
		return true;
	}
	
	/**
	 * 添加比分
	 */
	public boolean addScore(int matchId,int score1,int score2){
		if (score1 < 0 || score2 < 0){
			request.setAttribute("tip", "比分不能<0");
			return false;
		}
		if (matchId <= 0){
			request.setAttribute("tip", "比赛不存在.");
			return false;
		}
		WcMatch wcMatch = service.getMatch(" id=" + matchId);
		if (wcMatch == null){
			request.setAttribute("tip", "比赛不存在.");
			return false;
		}
		SqlUtil.executeUpdate("update wc_match set score1=" + score1 + ",score2=" + score2 + " where id=" + wcMatch.getId(),5);
		return true;
	}
	
	/**
	 * 统计比赛结果
	 */
	public void statResult(int matchId){
		if (matchId <= 0){
			return;
		}
		WcMatch wcMatch = service.getMatch(" id=" + matchId);
		if (wcMatch == null){
			return;
		}
		WcBet wcBet = null;
		int result = 0;
		int odds = 0;   // 赔率
		// 比赛结果，并找出赢者的赔率
		if (wcMatch.getScore1() > wcMatch.getScore2()){
			result = 0;	//胜
			odds = wcMatch.getWin();
		} else if (wcMatch.getScore1() < wcMatch.getScore2()){
			result = 1;	//负
			odds = wcMatch.getLose();
		} else {
			result = 2;	//平
			odds = wcMatch.getTie();
		}
		// 查找所有投了这场比赛但用户记录
		List betList = service.getBetList(" match_id=" + wcMatch.getId());
		// 给每个人算分
		int totalPoint = 0;
		for (int i = 0 ; i < betList.size() ; i++){
			wcBet = (WcBet)betList.get(i);
			if (wcBet != null && wcBet.getResult() == 2){
				// 赢了
				if (wcBet.getFlag() == result){
					// 给用户加彩币
					totalPoint = wcBet.getBet() * odds / 100;
					updatePoint(wcBet.getUserId(),totalPoint);
					// result=0:输1:赢2:尚无结果
					SqlUtil.executeUpdate("update wc_bet set result=1,`point`=" + totalPoint + ",odds=" + odds + " where id=" + wcBet.getId(),5);
				} else {
					SqlUtil.executeUpdate("update wc_bet set result=0 where id=" + wcBet.getId(),5);
				}
			}
		}
		
		// 所有小于1000彩币的用户，给补足1000彩币
		WcUser wcUser = null;
		List userList = service.getUserList(" 1");
		for (int i = 0 ; i < userList.size() ; i++){
			wcUser = (WcUser)userList.get(i);
			if (wcUser != null && wcUser.getPoint() < 1000){
				if (SqlUtil.getIntResult("select id from wc_bet where user_id=" + wcUser.getUserId() +" and result=2 limit 1",5) == -1){
					SqlUtil.executeUpdate("update wc_user set `point`=1000 where user_id=" + wcUser.getUserId(),5);
				}
			}
		}
		
		// 更新一下排行榜
		updateRank();
	}
}
