package net.joycool.wap.action.wgamefight;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.bean.wgamefight.WGameFightBean;
import net.joycool.wap.bean.wgamefight.WGameFightUserBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.WGameFightServiceImpl;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.service.infc.IWGameFightService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/** 
 * @author guip
 * @explain：
 * @datetime:2007-10-22 13:36:33
 */
public class FightAction {
	public static int BK_NUMBER_PER_PAGE = 5;
	protected IUserService userService;
	
	protected WGameFightServiceImpl fightService = new WGameFightServiceImpl();
	
	protected INoticeService noticeService;
	
	protected UserBean loginUser;

	protected UserBean leftUser;
	
    // 最小下注金额一百
	public static long MIX_GAMEPOINT = 100;

	// 最大下注金额十亿
	public static long MAX_GAMEPOINT = 1000000000;
	
	public static byte[] lock = new byte[0];
	
	public static String[] title = {"轻拳","重拳","重腿","防御","闪避"};
	public static int[] actionW = {1,1,2,3,1,2};
	public FightAction(HttpServletRequest request)
	{
		userService = ServiceFactory.createUserService();
		noticeService = ServiceFactory.createNoticeService();
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
	}
	//游戏首页
	public void dealIndex(HttpServletRequest request) {
//		 取得参数
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String orderBy = "id";
		String condition = "mark = " + WGameFightBean.PK_MARK_BKING
		+ " and game_id = " + WGameFightBean.FIGHT;
		int totalCount = fightService.getWGameFightCount(condition);
		int totalPageCount = totalCount / BK_NUMBER_PER_PAGE;
		if (totalCount % BK_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "index.jsp";
		// 取得庄家列表
		condition += " order by " + orderBy + " desc limit " + pageIndex
				* BK_NUMBER_PER_PAGE + ", " + BK_NUMBER_PER_PAGE;
		Vector fightList = fightService.getWGameFightList(condition);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("fightList", fightList);
	}
	//游戏组设置
	public void group(HttpServletRequest request) {
		String tip = null;
		String result = null;
		WGameFightBean fight =null;
		WGameFightUserBean fightUser =null;
		int groupId = StringUtil.toInt(request.getParameter("groupId"));
		int boutId = StringUtil.toInt(request.getParameter("boutId"));
		int actId = StringUtil.toInt(request.getParameter("actId"));
		String content = "0,0,0,0,0,0,0,0,0,0" ; 
		if(groupId>0 && groupId<=3)
		{
			int userId = loginUser.getId();
			fightUser = new WGameFightUserBean();
			fightUser.setUserId(userId);
			fightUser.setGroupId(groupId);
			fightUser.setContent(content);
			//得到对应的数据,如果是新上来的就添加一条记录，有这组记录的就跳过
			WGameFightUserBean userBean = fightService.getWGameFightUser(userId, groupId);
			if(userBean==null){
			fightService.addWGameFightUser(fightUser);
			}
			//已经保存有记录的更改记录
			else if(userBean!=null && boutId>0 && actId>0)
			{
				//清除缓存
				
				fight = new WGameFightBean();
				String con = userBean.getContent();
				if(actionW[actId] + boutId > 11)
				{
					tip = "对不起,动作设置有误";
					result = "notGroup";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				}
				con = fight.StringAlter(con,boutId,actId);
				String fightCon[] = con.split(",");
				if(boutId>=2){
					if(StringUtil.toInt(fightCon[0])==3 && StringUtil.toInt(fightCon[1])!=0)
					{
						tip = "对不起,动作设置有误";
						result = "notGroup";
						request.setAttribute("tip", tip);
						request.setAttribute("result", result);
						return;
					}
					else if(StringUtil.toInt(fightCon[boutId-1-1])>=2 || boutId>=3 && StringUtil.toInt(fightCon[boutId-1-1-1])==3)
				{
					tip = "对不起,动作设置有误";
					result = "notGroup";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				}
				}
            //更新数据库
				fightService.updateWGameFightUser("content="+"'"+con+"'","user_id="+loginUser.getId()+ " and group_id="+groupId+" and mark=0 ");
				fightService.flushFightUserList(userId);
            //清除bean缓存
				fightService.flushFightUser(loginUser.getId(), groupId);
				userBean.setContent(con);
			}
			request.setAttribute("userBean", userBean);
			result = "success";
		}else
		{
			tip = "对不起,动作组参数有误";
			result = "notGroup";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		//取得参数更新数据库
		if(actId>0 && boutId>0 && groupId>0)
		{
			fight = new WGameFightBean();
        // 得到对应的出招数据
			WGameFightUserBean userBean = fightService.getWGameFightUser(loginUser.getId(), groupId);
			//得到出招的字符串
			if(userBean!=null)
			{
				content=userBean.getContent();
			}
			if(actionW[actId] + boutId > 11)
			{
				tip = "对不起,动作设置有误";
				result = "notGroup";
				request.setAttribute("tip", tip);
				request.setAttribute("result", result);
				return;
			}
			//更新数组
			content = fight.StringAlter(content,boutId,actId);
			//更新数据库
			fightService.updateWGameFightUser("content="+"'"+content+"'","user_id="+loginUser.getId()+ " and group_id="+groupId+" and mark=0 ");
			WGameFightUserBean fightBean = fightService.getWGameFightUser(loginUser.getId(), groupId);
	        content = fightBean.getContent();
			
			request.setAttribute("boutId", String.valueOf(boutId));
			request.setAttribute("actId",String.valueOf(actId));
		}
		
		request.setAttribute("content",content);
		request.setAttribute("groupId",String.valueOf(groupId));
		request.setAttribute("tip", tip);
		request.setAttribute("result", result);
	}
	//游戏回合设置
	public void bout(HttpServletRequest request) {
		String tip = null;
		String result = null;
		int boutId = StringUtil.toInt(request.getParameter("boutId"));
		int groupId = StringUtil.toInt(request.getParameter("groupId"));
		request.setAttribute("boutId",String.valueOf(boutId));
		request.setAttribute("groupId",String.valueOf(groupId));
	}
	//动作组设置
	public void choiceGroup(HttpServletRequest request) {
		String tip = null;
		String result = null;
		WGameFightBean fight =null;
		WGameFightUserBean fightUser =null;
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		int groupId = StringUtil.toInt(request.getParameter("groupId"));
		//得到用户设置组的list
		Vector fightList = fightService.getWGameFightUserList(loginUser.getId(),"user_id="+loginUser.getId()+" and mark=0");
		int a = fightList.size();
		String condition = "id = " + bkId;
		//得到pk的唯一数据
		WGameFightBean fightBean = fightService.getWGameFight(condition);
		if(fightBean!=null)
		{
			request.setAttribute("fightBean",fightBean);
		}
		if(fightList.size()==0)
		{
			tip = "对不起,您还没有设置动作组";
			result = "notGroup";
		}else{
		request.setAttribute("fightList",fightList);	
		result = "success";
		}
		request.setAttribute("tip",tip);
		request.setAttribute("result",result);
	}
	//坐庄
	public void bkStart(HttpServletRequest request) 
	{
		String tip = null;
		String result = null;
		int groupId = StringUtil.toInt(request.getParameter("groupId"));
		long money = getUserGamePoint();
       //得到用户设置组的list
		Vector fightList = fightService.getWGameFightUserList(loginUser.getId(),"user_id="+loginUser.getId()+" and mark=0");
		if(fightList.size()==0)
		{
			tip = "对不起,您还没有设置动作组";
			result = "notGroup";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}else{
		request.setAttribute("fightList",fightList);	
		result = "success";
		}
		if(money < MIX_GAMEPOINT)
		{
			tip = "对不起,您的乐币不够"+MIX_GAMEPOINT+",不能坐庄";
			result = "notEnoughMoney";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		else {
			// 已经坐庄
			String condition = "left_user_id = " + loginUser.getId()
					+ " and mark = " + WGameFightBean.PK_MARK_BKING
					+ " and game_id = " + WGameFightBean.FIGHT;
			if (fightService.getWGameFightCount(condition) >= WGameFightBean.MAX_BK_COUNT) {
				tip = "对不起,您已经坐庄了" + WGameBean.MAX_BK_COUNT + "个乐酷街霸,不能再坐庄";
				result = "hasBk";
			} else {
				result = "continue";
			}
		}
		request.setAttribute("tip", tip);
		request.setAttribute("result", result);
		request.setAttribute("money", String.valueOf(money));
		request.setAttribute("groupId", String.valueOf(groupId));
		
	}
	//坐庄确认
	public void bkDeal(HttpServletRequest request) 
	{
		int groupId = StringUtil.toInt(request.getParameter("groupId"));
		String content = StringUtil.noEnter(request.getParameter("content"));
		long wager = (long)StringUtil.toInt(request.getParameter("wager"));
		
		WGameFightUserBean fightUser =null;
		String tip = null;
		String result = null;
		//没取到组参数
		if (groupId ==-1) {
			tip = "对不起,您还没有动作组设置组";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (wager < MIX_GAMEPOINT) {
			tip = "至少要下注"+MIX_GAMEPOINT+"乐币";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (wager > MAX_GAMEPOINT) {
			tip = "最多能下注" + MAX_GAMEPOINT + "乐币";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		String condition = "left_user_id = " + loginUser.getId()
		+ " and mark = " + WGameFightBean.PK_MARK_BKING
		+ " and game_id = " + WGameFightBean.FIGHT;
	    if (fightService.getWGameFightCount(condition) >= WGameFightBean.MAX_BK_COUNT) {
			request.setAttribute("tip", "对不起,您已经坐庄了" + WGameFightBean.MAX_BK_COUNT + "个乐酷街霸,不能再坐庄");
			request.setAttribute("result", "failure");
			return;
		}
		
		synchronized(loginUser.getLock()) {
			
			long money = getUserGamePoint();
	        //乐币不够
			if (money < wager) {
				request.setAttribute("tip", "对不起,您乐币不够");
				request.setAttribute("result", "failure");
				return;
			}
			
			//输入内容判断
			else if (content==null || content.length() == 0) {
				content = "KO";
			}
			else if (content.length()>15) {
				tip = "输入内容最多15字!";
				result = "failure";
				request.setAttribute("tip", tip);
				request.setAttribute("result", result);
				return;
			}
			fightUser = fightService.getWGameFightUser(loginUser.getId(), groupId);

			//给用户减钱
			UserInfoUtil.updateUserCash(loginUser.getId(),-wager,UserCashAction.GAME,"wgame--乐酷街霸坐庄--减乐币"+ wager);
		}
		//添加游戏记录
		WGameFightBean fight = new WGameFightBean();
		fight.setGameId(WGameFightBean.FIGHT);
		fight.setLeftUserId(loginUser.getId());
		fight.setLeftNickname(loginUser.getNickName());
		fight.setLeftCardsStr(String.valueOf(groupId));
		fight.setContent(StringUtil.noEnter(content));
		fight.setMark(WGameFightBean.PK_MARK_BKING);
		fight.setLeftViewed(fightUser.getContent());
		fight.setWager(wager);
		if (!fightService.addWGameFight(fight)) {
			return;
		}
		 //更新用户游戏详细的mark标志位
		//fightService.updateWGameFightUser("mark=1","user_id=" + loginUser.getId() + " and mark=0");
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("fight", fight);

	}
	//用户挑战
	public void pkOut(HttpServletRequest request) {
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		int groupId = StringUtil.toInt(request.getParameter("groupId"));
		request.setAttribute("groupId", String.valueOf(groupId));
		String tip = null;
		String result = null;
    //得到用户设置组的list
		Vector fightList = fightService.getWGameFightUserList(loginUser.getId(),"user_id="+loginUser.getId()+" and mark=0");
		if(fightList.size()==0)
		{
			tip = "对不起,您还没有设置动作组";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}else{
		request.setAttribute("fightList",fightList);	
		result = "success";
		}
		if (bkId == -1) {
			tip = "该局已经被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		request.setAttribute("bkId", String.valueOf(bkId));
		String condition = "id = " + bkId;
		
		WGameFightBean fight = fightService.getWGameFight(condition);
		if (fight == null) {
			tip = "该局已经被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (fight.getLeftUserId() == loginUser.getId()) {
			tip = "您不能挑战自己的庄";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (fight.getMark() == WGameFightBean.PK_MARK_END) {
			tip = "该局已经结束";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		long money = this.getUserGamePoint();
		// 乐币不够
		if (money < fight.getWager()) {
			tip = "您的乐币不够";
			result = "failure";
		}

		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
		}
		// 发牌
		else {
			request.setAttribute("fight", fight);
			result = "success";
			request.setAttribute("result", result);
		}
	}
	//挑战开始游戏
	public void chlStart(HttpServletRequest request) {
		int winUserId = 0;
		WGameFightUserBean fightUser =null;
		WGameFightBean fight =null;
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		int groupId = StringUtil.toInt(request.getParameter("groupId"));
		String tip = null;
		String result = null;
		if (bkId == -1) {
			tip = "该局已被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (groupId == -1) {
			tip = "您还没有选择动作组";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		fightUser = fightService.getWGameFightUser(loginUser.getId(), groupId);
		
		String condition = "id = " + bkId;
		synchronized(lock) {	
		    fight = fightService.getWGameFight(condition);
			if (validate(fight, request) == false)
				return;
			
			fight.setRightUserId(loginUser.getId());
			fight.setRightNickname(loginUser.getNickName());
			fight.setRightCardsStr(String.valueOf(groupId));
			fight.setRightViewed(fightUser.getContent());
			
	        //给用户减钱
			WGameFightBean fightBean = new WGameFightBean();
			int rResult = fightBean.PKact(fight.getLeftViewed(),fight.getRightViewed(),WGameFightBean.LEFT_PK_ACT,WGameFightBean.RIGHT_PK_ACT);
			request.setAttribute("rResult", String.valueOf(rResult));
			if (WGameFightBean.RESULT_MARK_WIN == rResult) { // 庄家赢
				 //给庄家加钱
				UserInfoUtil.updateUserCash(fight.getLeftUserId(),fight.getWager()*2,UserCashAction.GAME,"wgame--乐酷街霸胜利--加乐币"+ (fight.getWager()*2));
				UserInfoUtil.updateUserCash(loginUser.getId(),-fight.getWager(),UserCashAction.GAME,"wgame--乐酷街霸坐庄--减乐币"+ fight.getWager());
				winUserId = fight.getLeftUserId();
			} else if(WGameFightBean.RESULT_MARK_LOSE == rResult){// 庄家输
	//			给挑战者加钱
				UserInfoUtil.updateUserCash(loginUser.getId(),fight.getWager(),UserCashAction.GAME,"wgame--乐酷街霸胜利--加乐币"+ (fight.getWager()*2));
				winUserId = loginUser.getId();
			}else if (WGameFightBean.RESULT_MARK_DRAW == rResult)//平局
			{
	//			返还庄家的乐币
				UserInfoUtil.updateUserCash(fight.getLeftUserId(),fight.getWager(),UserCashAction.GAME,"wgame--乐酷街霸平局--返还乐币"+ fight.getWager());
				winUserId =0;
			}
			//更新挑战者数据库的信息
			String set = "right_user_id = " + loginUser.getId()
			+ ", right_nickname = '" + StringUtil.toSql(loginUser.getNickName())
			+ "', right_cards = '" + StringUtil.toSql(fight.getRightCardsStr())
			+ "', end_datetime = now(), mark = " + WGameFightBean.PK_MARK_END
		    + ", right_viewed = '" +fight.getRightViewed() +"', wager= '"
			+ fight.getWager()+"',win_user_id=" + winUserId;
	        condition = "id = " + bkId;
	        fightService.updateWGameFight(set,condition);
		}
        //更新挑战者在fightuser的mark标注位
        //fightService.updateWGameFightUser("mark=1","user_id="+loginUser.getId()+" and mark=0");
        result = "success";
		request.setAttribute("result", result);
		request.setAttribute("fight", fight);
		
        //加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getChlStartNoticeTitle(fight.getRightNickname(), fight.getContent()));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(fight.getLeftUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamefight/viewPK.jsp?id=" + fight.getId());
		noticeService.addNotice(notice);
	}
	/**
	 * 看坐庄结果。
	 * 
	 * @param request
	 * @return
	 */
	public void viewWGamePK(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id == -1) {
			return;
		}
		String condition = "id = " + id;
		WGameFightBean fightBean = fightService.getWGameFight(condition);
		request.setAttribute("fightBean", fightBean);
	}
	public String getChlStartNoticeTitle(String enemyNickname, String gameName) {
		return enemyNickname + "挑战你的" + gameName + "赌局!";
	}
	//验证
	public boolean validate(WGameFightBean fightBean, HttpServletRequest request) {
		if (fightBean == null) {
			request.setAttribute("tip", "该局已被取消");
			request.setAttribute("result", "failure");
			return false;

		}
		if (fightBean.getLeftUserId() == loginUser.getId()) {
			request.setAttribute("tip", "您不能挑战自己的庄");
			request.setAttribute("result", "failure");
			return false;

		}
		if (fightBean.getMark() == WGameFightBean.PK_MARK_END) {
			request.setAttribute("tip", "该局已经结束");
			request.setAttribute("result", "failure");
			return false;

		}
		long money = getUserGamePoint();
		if (money < fightBean.getWager()) {
			request.setAttribute("tip", "您的乐币不够");
			request.setAttribute("result", "failure");
			return false;
		}
		request.setAttribute("result", "success");
		return true;
	}
	/**
	 * 
	 * @author guip
	 * @explain：获取用户随身携带乐币
	 * @datetime:2007-10-22 17:19:09
	 * @return
	 * @return long
	 */
	public long getUserGamePoint() {
		long money = 0;
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser.getId());
		if (userStatus != null) {
			money = userStatus.getGamePoint();
		}
		return money;
	}
	public static String getStringName(int id) {

			if (id==0) {
	            return "";
	        }
	        if (id==1) {
	            return "轻拳";
	        }
	        if (id==2) {
	            return "重拳";
	        }
	        if (id==3) {
	            return "重腿";
	        }
	        if (id==4) {
	            return "防御";
	        }
	        if (id==5) {
	            return "闪避";
	        }
	        return "";
	 }
}
