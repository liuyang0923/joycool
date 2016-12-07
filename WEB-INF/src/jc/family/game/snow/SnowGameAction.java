package jc.family.game.snow;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;
import jc.family.FamilyUserBean;
import jc.family.FundDetail;
import jc.family.game.ApplyBean;
import jc.family.game.GameAction;
import jc.family.game.MatchBean;
import jc.family.game.MemberBean;
import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.spec.shop.ShopAction;
import net.joycool.wap.spec.shop.ShopUtil;
import net.joycool.wap.spec.shop.UserInfoBean;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.UserInfoUtil;

public class SnowGameAction extends GameAction {

	public static SnowGameService service = new SnowGameService();

	public static ICacheMap snowMoneyCache = CacheManage.addCache(
			new LinkedCacheMap(10000), "snowMoney");
	public static ICacheMap snowActivityCache = CacheManage.addCache(
			new LinkedCacheMap(1000), "snowActivity");
	public static long startTime;
	public static int treeMapkey=Integer.MAX_VALUE;
	
	public SnowGameAction(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * 得到某人的雪币
	 * 
	 * @return
	 */
	public int getOneSnowMoney() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 0;
		}
		return getSnowMoney(fmUser.getId());
	}
	
	public static SnowMoneyBean getSnowMoneyBean(int userId) {
		SnowMoneyBean bean = null;
		Integer key = new Integer(userId);
		synchronized (snowMoneyCache) {
			bean = (SnowMoneyBean) snowMoneyCache.get(key);
			if (bean == null) {
				String sql = "select money from mcoolbea.fm_game_snow_money where uid=" + userId;
				int c = service.selectIntResult(sql);
				if (c < 0) {
					c = 0;
				}
				bean = new SnowMoneyBean();
				bean.setUid(userId);
				bean.setMoneyHold(c);
				snowMoneyCache.put(key, bean);
			}
		}
		return bean;
	}
	
	public static int getSnowMoney(int userId) {
		return getSnowMoneyBean(userId).getMoneyHold();
	}

	/**
	 * 兑换雪币
	 * 
	 * @return
	 */
	public int getChange() {
		int mid = getParameterInt("mid");
		if (mid == 0) {
			return 9;
		}
		UserBean userbean = getLoginUser();
		if (userbean == null) {
			return 4;// 未登录
		}
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 3;// 不是家族成员
		}
		int money = getParameterInt("change");
		if (money == 0 || money < 0 || money > 10000) {// 格式不对
			return 2;
		}
		
		int auid=service.selectIntResult("select id from fm_game_apply where uid="+fmUser.getId()+" and m_id="+mid);
		if(auid<0){
			return 11;// 未参加该比赛
		}
		
		MatchBean smb = (MatchBean) GameAction.matchCache.get(new Integer(
				mid));
		if (smb == null) {
			return 6;// 只准兑换当前赛事，且只准在游戏开玩的时候兑换
		}
		
		if(smb.getType()!=2){
			return 12;// 只能在雪仗游戏中兑换雪币
		}
		
		if(smb.getState()!=1){
			return 6;
		}
		
		SnowBean gb = (SnowBean) smb.getGameMap().get(new Integer(fmUser.getFm_id()));
		
		if (gb == null) {
			return 7;
		}
		
		if(gb.getRank()!=0){
			return 6;// 比赛已经有结果
		}

		int own = getOneSnowMoney();
		if (own + money > Constants.MAX_SNOW_MONEY_OWN) {
			return 1;// 超过雪币拥有量
		}
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(userbean.getId());
		int total = Constants.SNOW_MONEY_CHANGE_SCALE * money;
		if (status.getGamePoint() < total) {
			return 0;// 钱不够
		}
		
		if(request.getParameter("cmd")!=null){
			UserInfoBean bean = ShopAction.shopService.getUserInfo(fmUser.getId());
			if(bean==null){
				return 10;
			}
			float ownKu=bean.getGold();
			float spend=1;
			
			if(ownKu <spend){
				return 10;// 酷币不够
			}
			ShopUtil.updateUserGold(fmUser.getId(),spend,163);// 163是指兑换雪币用掉的
		}
		boolean update = service.updateSnowMoney(money+own, userbean.getId());
		if (update) {
			UserInfoUtil.updateUserCash(userbean.getId(), -total,
					UserCashAction.GAME, "兑换雪币扣:" + total);// 扣钱
		}

		SnowMoneyBean mb = getSnowMoneyBean(fmUser.getId());
		
		if (mb != null) {
			synchronized (mb) {
				mb.setMoneyHold(mb.getMoneyHold() + money);
			}
			MemberBean memb = (MemberBean) gb.getMemberBean().get(
					new Integer(fmUser.getId()));
			if (memb == null) {
				return 8;// 缓存内没有该用户
			}
			memb.setIsChange(1);// 表示该用户已经兑换过雪币了
		}
		return 5;// 兑换成功
	}

	// 判断是否兑换过雪币
	public boolean isInGame() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return false;
		}
		int mid = getParameterInt("mid");
		if (mid == 0) {
			return false;
		}
		ApplyBean bean = service.getApplyBean(" m_id=" + mid + " and uid="+ fmUser.getId());
		if(bean==null){
			return false;
		}
		MatchBean smb = (MatchBean) GameAction.matchCache.get(new Integer(mid));
		if(smb==null){
			return false;
		}
		SnowBean gb = (SnowBean) smb.getGameMap().get(new Integer(fmUser.getFm_id()));
		if (gb == null) {
			return false;
		}
		MemberBean memb = (MemberBean) gb.getMemberBean().get(new Integer(fmUser.getId()));
		if(memb.getIsChange()==1){
			return true;// 兑换过了
		}
		if(bean.getIsPay()==1){
			return true;// 付钱了，进过游戏了
		}
		return false;// 没进过
	}
	
	// 验证用户能否进入游戏
	public int couldGame(){
		if (request.getAttribute("mid") == null) {
			return 0;
		}
		Integer mid = new Integer(request.getAttribute("mid").toString());

		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 1;// 不是家族用户
		}
		
		MatchBean smb = (MatchBean) matchCache.get(mid);
		if (smb == null) {
			return 3;// 缓存里没该赛事
		}
		SnowBean gameDetails = (SnowBean) smb.getGameMap().get(
				new Integer(fmUser.getFm_id()));
		if (gameDetails == null) {
			return 4;// 缓存里该赛事没有该家族
		}

		ApplyBean bean = service.getApplyBean(" m_id=" + mid + " and uid="
				+ fmUser.getId()+" and fid="+fmUser.getFm_id());
		if (bean == null) {
			return 2;// 没有报名
		}
		
		if (bean != null) {
			if (bean.getState() != 2) {
				return 5;// 报名未通过审核!
			}
		}
		return 6;
	}
	

	// 游戏界面,显示两个家族比赛时的一些赛况
	public int getGameDetails() {
		if (request.getAttribute("mid") == null) {
			return 0;
		}
		Integer mid = new Integer(request.getAttribute("mid").toString());

		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 1;// 不是家族用户
		}
		
		MatchBean smb = (MatchBean) matchCache.get(mid);
		if (smb == null) {
			return 3;// 缓存里没该赛事
		}

		SnowBean gameDetails = (SnowBean) smb.getGameMap().get(new Integer(fmUser.getFm_id()));
		if (gameDetails == null) {
			return 4;// 缓存里该赛事没有该家族
		}
		
		ApplyBean bean = service.getApplyBean(" m_id=" + mid + " and uid="
				+ fmUser.getId()+" and fid="+fmUser.getFm_id());
		if (bean == null) {
			return 2;// 没有报名
		}
		
		if (bean != null) {
			if (bean.getState() != 2) {
				return 9;// 报名未通过审核!
			}
		}

		Integer key = new Integer(gameDetails.getFid2());

		SnowBean gameDetails2 = (SnowBean) smb.getGameMap().get(key);
		if (gameDetails2 == null) {
			setAttribute("vsSanowCount", 0);
			setAttribute("vsFmNum", 0);
		} else {
			setAttribute("vsSanowCount", gameDetails2.getSnowAccumulation());// 放入对方家族的积雪，方便页面取出
			setAttribute("vsFmNum", gameDetails2.getNumTotal());// 对方家族人数
		}

		SnowBean gb = (SnowBean) smb.getGameMap().get(
				new Integer(fmUser.getFm_id()));

		if (gameDetails.getRank() == 0) {// 游戏中才会扣钱
			if (bean.getIsPay() == 0) {// 检查是否扣过钱
				FamilyHomeBean fhome = getFmByID(fmUser.getFm_id());
				long money = fhome.getMoney();
				long applyNum = money / 10000000;
				if (applyNum<1) {
					return 5;// 钱不够
				}
				
				boolean updatemoney = updateFund(fhome, -10000000l, FundDetail.GAME_TYPE);// 家族基金扣钱
				if (!updatemoney) {
					return 6;
				}
				
				updatemoney = service.updatePay(mid.intValue(), fmUser.getId());
				if (!updatemoney) {
					return 7;
				}
				synchronized(gb){
					gb.setNumTotal(gb.getNumTotal() + 1);// 进入游戏后，参加人数加一
				}
			}
		}
		setAttribute("GameDetails", gameDetails);
		
		if (session.getAttribute("activityButton") == null) {
			session.setAttribute("activityButton", "0");
		}
		return 8;
	}
	
	// 显示比赛结果给用户看
	public int getEnd(){
		if (request.getAttribute("mid") == null) {
			return 0;
		}
		Integer mid = new Integer(request.getAttribute("mid").toString());

		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 1;// 不是家族用户
		}

		ApplyBean bean = service.getApplyBean(" m_id=" + mid + " and uid="
				+ fmUser.getId());
		if (bean == null) {
			return 2;// 没有报名
		}
		MatchBean smb = (MatchBean) matchCache.get(mid);
		if (smb == null) {
			return 3;// 缓存里没该赛事
		}
		SnowBean gameDetails = (SnowBean) smb.getGameMap().get(new Integer(fmUser.getFm_id()));
		if (gameDetails == null) {
			return 4;
		}
		Integer key = new Integer(gameDetails.getFid2());

		SnowBean gameDetails2 = (SnowBean) smb.getGameMap().get(key);
		if(gameDetails2!=null){
			setAttribute("vsFmPrize", gameDetails2.getPrize());
		}
		setAttribute("GameEnds", gameDetails);
		return 5;
	}
	
	// 判断是否比赛结束,防止用户赛前或赛中或赛后进入不该进入的页面
	public int isGameOver(int mid){
		MatchBean smb = (MatchBean) matchCache.get(new Integer(mid));
		if(smb==null){
			return 0;// 没有该比赛
		}

		if(smb.getState()==1){
			FamilyUserBean fmUser = getFmUser();
			if(fmUser==null){
				return 4;
			}
			SnowBean gb = (SnowBean) smb.getGameMap().get(new Integer(fmUser.getFm_id()));
			if(gb==null){
				return 2;
			}
			if(gb.getRank()!=0){
				return 2;
			}
			ApplyBean bean = service.getApplyBean(" m_id=" + mid + " and uid="
					+ fmUser.getId()+" and fid="+fmUser.getFm_id());
			if (bean == null) {
				return 4;// 没有报名
			}
			if (bean != null) {
				if (bean.getState() != 2) {
					return 4;// 报名未通过审核!
				}
			}
			return 1;// 正在比赛
		}
		if(smb.getState()==2){
			return 2;// 完赛
		}
		return 3;// 报名中
	}

	// 游戏中家族拥有的道具list
	public List getToolsList() {
		Integer mid = new Integer(request.getAttribute("mid").toString());
		MatchBean smb = (MatchBean) matchCache.get(mid);
		if (smb == null) {
			return null;// 不存在的赛事
		}
		FamilyUserBean fmUser = getFmUser();

		SnowBean gb = (SnowBean) smb.getGameMap().get(
				new Integer(fmUser.getFm_id()));
		if (gb == null) {
			return null;// 该家族未参加这次比赛
		}
		HashMap toolsMap = gb.getToolsBean();
		if (toolsMap == null || toolsMap.size() < 1) {
			return null;// 没有可用的道具
		}
		List list = new ArrayList();
		Object[] o = toolsMap.keySet().toArray();
		Arrays.sort(o);
		int maxKey = ((Integer) o[o.length - 1]).intValue();
		for (int i = 1; i <= maxKey; i++) {
			if (toolsMap.containsKey(Integer.valueOf(i))) {
				list.add(toolsMap.get(Integer.valueOf(i)));
			}
		}

		int c = list.size();
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(
				this, c, 10, "p");
		int start = paging.getStartIndex(), end = paging.getStartIndex()
				+ paging.getCountPerPage();
		if (list.size() < 10) {
			list = list.subList(paging.getStartIndex(), list.size());
		} else if (list.size() % 10 < 10) {
			if (start == (list.size() / 10) * 10) {
				end = list.size();
			}
			list = list.subList(start, end);
		}

		String s = paging.shuzifenye("snowBalls.jsp?mid=" + mid, true, "|",
				response);
		setAttribute("toolOwns", s);
		return list;
	}

	// 游戏中的动态list
	public List getActivit() {
		Integer mid = new Integer(request.getAttribute("mid").toString());
		MatchBean smb = (MatchBean) matchCache.get(mid);
		if(smb==null){
			return null;
		}

		FamilyUserBean fmUser = getFmUser();

		SnowBean gameDetails = (SnowBean) smb.getGameMap().get(
				new Integer(fmUser.getFm_id()));

		TreeMap treeMap = (TreeMap) snowActivityCache.get(fmUser.getFm_id() + "," + mid
				+ "," + gameDetails.getFid2());
		if (treeMap == null) {
			treeMap = (TreeMap) snowActivityCache.get(gameDetails.getFid2() + ","
					+ mid + "," + fmUser.getFm_id());
			if (treeMap == null) {
				return null;
			}
		}
		List list=new ArrayList();
		Iterator iterator = treeMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			SnowActivityBean sab = (SnowActivityBean) entry.getValue();
			list.add(sab);
		}
		int c = list.size();
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(
				this, c, 6, "p");
		int start = paging.getStartIndex(), end = paging.getStartIndex()
				+ paging.getCountPerPage();
		if (list.size() < 6) {
			list = list.subList(paging.getStartIndex(), list.size());
		} else if (list.size() % 6 < 6) {
			if (start == (list.size() / 6) * 6) {
				end = list.size();
			}
			list = list.subList(start, end);
		}
		String s = paging.shuzifenye("snowActivity.jsp?mid=" + mid, true, "|",
				response);
		setAttribute("ActivityList", s);
		return list;
	}

	// 本轮统计页面
	public int sureJoinGame() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 2;// 不是家族成员
		}
		int mid;
		if(request.getParameter("mid")!=null){
			mid=getParameterInt("mid");
		}else{
			mid =service.selectIntResult("select max(id) from fm_game_match where game_type=2 and state=2");
		}
		if(mid==0){
			return 0;
		}
		MatchBean mhb=service.getMatchBean(" id="+mid);
		if(mhb==null){
			return 0;
		}
			SnowBean bean = service.selectOneMatch(fmUser.getFm_id(), mid);
			if (bean == null) {
				bean=new SnowBean();
				bean.setHoldTime(DateUtil.formatDate(new Date(mhb.getCreateTime())));
				bean.setMid(mid);
				request.setAttribute("StaticsBean", bean);
				request.setAttribute("mid", new Integer(mid));
				return 4;// 未参赛
		}
			int con=service.selectIntResult("select contribution from fm_game_member where m_id="+mid+" and uid="+fmUser.getId()+" and fid="+fmUser.getFm_id());
			if(con<0||con==0||bean.getNumTotal()<1){
				bean.setContribution(0);
			}else{
				if (bean.getRank() == 3||bean.getRank()==4) {// 算出当伦比赛成员的贡献分
					if(bean.getNumTotal()<1){
						bean.setContribution(0);
					}else{
						bean.setContribution( Constants.DEUCE_HOME_EXPLOIT / bean.getNumTotal() + (Constants.DEUCE_HOME_EXPLOIT % bean.getNumTotal() > 5 ? 1 : 0));
					}
				} else if (bean.getRank() == 1) {
					bean.setContribution(Constants.WIN_HOME_EXPLOIT / bean.getNumTotal() + (Constants.WIN_HOME_EXPLOIT % bean.getNumTotal() > 5 ? 1 : 0));
				} else if (bean.getRank() == 2) {
					if(bean.getNumTotal()<1){
						bean.setContribution(0);
					}else{
						bean.setContribution(Constants.LOSE_HOME_EXPLOIT);
					}
				}
			}
			request.setAttribute("StaticsBean", bean);
			request.setAttribute("mid", new Integer(mid));
		return 5;
	}

	/**
	 * 在游戏开始的点自动调用
	 */
	public static void makePair(int mid) {// 配对家族，家族和参赛用户信息放到缓存里
		SnowGameToolsBean.setCountId(0);// 重置道具编号，防止过大超出int范围
		startTime=System.currentTimeMillis();// 记录比赛开始时间
		MatchBean snowGameBean = (MatchBean) GameAction.matchCache
				.get(new Integer(mid));
		if (snowGameBean == null || snowGameBean.getType() != 2)
			return;
		synchronized(snowGameBean){
			snowGameBean.setState(1);
			service.upd(" update fm_game_match set state=1,create_time=now() where id="
					+ snowGameBean.getId());
	
			List list = service.selectFmByScore(mid);
			if (list == null || list.size() < 1)
				return;// 没有家族参赛
			for (int i = 0; i < list.size() - 1;) {
				Integer key = (Integer) list.get(i);
				HashMap snowGameMap = snowGameBean.getGameMap();
				SnowBean bean = new SnowBean();
				HashMap toolsMap = new HashMap();
	
				bean.setFid1(key.intValue());
				bean.setFid2(((Integer) list.get(i + 1)).intValue());
				bean.setMid(mid);
				bean.setNumTotal(0);
				bean.setType(2);
	
				List memberList = service.selectApplyMembers("m_id=" + mid
						+ " and fid=" + key.intValue());
				HashMap memberMap = new HashMap();
				for (int j = 0; j < memberList.size(); j++) {
					MemberBean memberBean = (MemberBean) memberList.get(j);
//					SnowMoneyBean sMoney = new SnowMoneyBean();
//					sMoney.setMoneyHold(memberBean.getPayMake());
//					sMoney.setUid(memberBean.getUid());
//					snowMoneyCache.put(new Integer(memberBean.getUid()), sMoney);
	
					memberBean.setMid(mid);
//					memberBean.setPayMake(0);
					memberMap.put(new Integer(memberBean.getUid()), memberBean);
					if (OnlineUtil.isOnline(String.valueOf(memberBean.getUid()))) {
						NoticeAction.sendNotice(memberBean.getUid(), "雪仗活动已经开始", "",
								NoticeBean.GENERAL_NOTICE, "",
								"/fm/game/fmgame.jsp");// 发送给参加的用户,提示他们雪仗游戏开始了
					}
				}
				bean.setMemberBean(memberMap);
				bean.setToolsBean(toolsMap);
				snowGameMap.put(key, bean);
				key = (Integer) list.get(i + 1);
				SnowBean bean2 = new SnowBean();
				bean2.setFid1(key.intValue());
				bean2.setFid2(((Integer) list.get(i)).intValue());
				bean2.setMid(mid);
				bean2.setNumTotal(0);
				bean2.setType(2);
				List memberList2 = service.selectApplyMembers("m_id=" + mid
						+ " and fid=" + key.intValue());
				HashMap memberMap2 = new HashMap();
				for (int j = 0; j < memberList2.size(); j++) {
					MemberBean memberBean = (MemberBean) memberList2.get(j);
//					SnowMoneyBean sMoney = new SnowMoneyBean();
//					sMoney.setMoneyHold(memberBean.getPayMake());
//					sMoney.setUid(memberBean.getUid());
//					snowMoneyCache.put(new Integer(memberBean.getUid()), sMoney);
	
					memberBean.setMid(mid);
//					memberBean.setPayMake(0);
					memberMap2.put(new Integer(memberBean.getUid()), memberBean);
					
					if (OnlineUtil.isOnline(String.valueOf(memberBean.getUid()))) {
						NoticeAction.sendNotice(memberBean.getUid(), "雪仗活动已经开始", "",
								NoticeBean.GENERAL_NOTICE, "",
								"/fm/game/fmgame.jsp");// 发送给参加的用户,提示他们雪仗游戏开始了
					}
				}
				HashMap toolsMap2 = new HashMap();
				bean2.setMemberBean(memberMap2);
				bean2.setToolsBean(toolsMap2);
				snowGameMap.put(key, bean2);
	
				TreeMap alist = new TreeMap();
				String at = list.get(i) + "," + mid + "," + list.get(i + 1);
				snowActivityCache.put(at, alist);
				i = i + 2;
			}
			if (list.size() % 2 != 0) {// 但报名的家族为奇数时，将最后一个家族
				HashMap snowGameMap = snowGameBean.getGameMap();
				HashMap toolsMap = new HashMap();

					Integer key = (Integer) list.get(list.size() - 1);
					SnowBean bean = new SnowBean();
					bean.setFid1(key.intValue());
					bean.setFid2(0);
					bean.setMid(mid);
					List memberList2 = service.selectApplyMembers("m_id=" + mid
							+ " and fid=" + key.intValue());
					HashMap memberMap2 = new HashMap();
					for (int j = 0; j < memberList2.size(); j++) {
						MemberBean memberBean = (MemberBean) memberList2.get(j);
//						SnowMoneyBean sMoney = new SnowMoneyBean();
//						sMoney.setMoneyHold(memberBean.getPayMake());
//						sMoney.setUid(memberBean.getUid());
//						snowMoneyCache
//								.put(new Integer(memberBean.getUid()), sMoney);
	
						memberBean.setMid(mid);
//						memberBean.setPayMake(0);
						memberMap2
								.put(new Integer(memberBean.getUid()), memberBean);
						if (OnlineUtil.isOnline(String.valueOf(memberBean.getUid()))) {
							NoticeAction.sendNotice(memberBean.getUid(), "雪仗活动已经开始", "",
									NoticeBean.GENERAL_NOTICE, "",
									"/fm/game/fmgame.jsp");// 发送给参加的用户,提示他们雪仗游戏开始了
						}
					}
					bean.setRank(3);
					bean.setMemberBean(memberMap2);
					bean.setToolsBean(toolsMap);
					bean.setNumTotal(0);
					bean.setType(2);
					bean.setPrize(0);
					bean.setScore(Constants.SNOW_DEUCE_SCORE);
					snowGameMap.put(key, bean);
			}
		matchCache.put(new Integer(mid), snowGameBean);
		}
	}

	// 投，将个人信息放到snowMember里，或是修改，修改snowFamily
	public int throwSnow() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 2;// 不是家族成员
		}
		int id = getParameterInt("id");//
		int tid = getParameterInt("tid");// 道具类型id

		Integer mid = new Integer(request.getAttribute("mid").toString());
		MatchBean smb = (MatchBean) matchCache.get(mid);
		if (smb == null) {
			return 8;// 无赛事
		}
		SnowBean sgb = (SnowBean) smb.getGameMap().get(
				new Integer(fmUser.getFm_id()));
		if (sgb == null) {
			return 9;// 家族没参赛
		}
		HashMap memberMap = sgb.getMemberBean();
		MemberBean mb = (MemberBean) memberMap.get(new Integer(fmUser.getId()));
		if (mb == null) {
			return 6;// 未审核通过
		}

		HashMap toolsMap = sgb.getToolsBean();
		if (toolsMap == null) {
			return 5;// 无道具
		}
		SnowGameToolTypeBean sgttb = service.selectToolType(tid);
		if(sgttb==null){
			return 5;
		}
		SnowGameToolsBean tool = (SnowGameToolsBean) toolsMap.get(new Integer(
				id));
		if (tool == null) {
			return 5;
		} else if (tool != null) {

			if (!this.isCooldown("fmSnowCD", 3 * 1000)) {// 每三秒扔一次
				return 1; // CD
			}
			int usedTime = tool.getUsedTime();
			int useTime = service
					.selectIntResult("select use_time from fm_game_snow_tools where id="
							+ tool.getTid());
			if (usedTime < useTime) {
				int check = checkAnswer();
				if (check == 0) {
					return 3;// 题目不存在
				}
				if (check == 1) {
					tool.setUsedTime(usedTime + 1);
					if (usedTime + 1 == useTime) {
						synchronized(toolsMap){
							toolsMap.remove(new Integer(tool.getId()));
						}
					}
					return 4;// 答错
				}
				if (check == 2) {
					synchronized(tool){
						tool.setUsedTime(usedTime + 1);
						mb.setTotalHit(mb.getTotalHit() + 1);
					}
					synchronized(sgb){
						sgb.setHitNum(sgb.getHitNum()+1);
						if(sgb.getHitNum()>19&&sgb.getGamePoint()==0){// 击中次数大于20，设置获得游戏经验值
							sgb.setGamePoint(1);
						}
					}
					
					if (usedTime + 1 == useTime) {
						synchronized(toolsMap){
							toolsMap.remove(new Integer(tool.getId()));
						}
					}
					
				}
			}
		}
		SnowBean sgb2 = (SnowBean) smb.getGameMap().get(Integer.valueOf(sgb.getFid2()));// 取得对手家族游戏信息
		if(sgb.getRank()!=0||sgb2.getRank()!=0){
			return 1;// 有名次了，不让投
		}
		synchronized(sgb2){// 修改对方家族积雪量
			sgb2.setSnowAccumulation(sgb2.getSnowAccumulation()
					+ sgttb.getSnowEffect() < Constants.MAX_SNOW ? sgb2
					.getSnowAccumulation()
					+ sgttb.getSnowEffect() : Constants.MAX_SNOW);
			synchronized(sgb){
				if (sgb2.getSnowAccumulation() >= Constants.MAX_SNOW) {// 对方家族积雪量到达或者超过最大积雪量，记录这组比赛的结果
					if(sgb.getRank()==0&&sgb2.getRank()==0){
						sgb.setRank(1);
						sgb.setScore(Constants.SNOW_WIN_SCORE);
						boolean vsAttend = service.isAttend(mid.intValue(), sgb.getFid2());
						long prize=0;
						if(vsAttend){// 如果对方家族参赛，就返回双方花费的95%
							prize =((10000000l * (sgb.getNumTotal() + sgb2.getNumTotal())+ sgb.getPrize()*100000l) * 95+50 )/ 100;// 计算比赛奖金，双方报名费和所有游戏花费的95%并四舍五入
						}else{// 如果对方家族没参赛，就返回单方的全部
							prize=10000000l*sgb.getNumTotal()+sgb.getPrize()*100000l;
						}
						
						sgb.setPrize(prize);
						long spend = System.currentTimeMillis() - startTime;
						sgb.setSpendTime(spend);
						sgb2.setSpendTime(spend);
						sgb2.setPrize(0);
						sgb2.setRank(2);
						sgb2.setScore(Constants.SNOW_FALL_SCORE);
					}
				}
			}
		}
		if (tid == 4) {
			synchronized (snowActivityCache) {
				SnowActivityBean sab = new SnowActivityBean();
				sab.setAType(1);// 使用了
				sab.setTType(4);// 投雪机
				sab.setFmName(sgb.getFmName1());
				sab.setUName(fmUser.getNickNameWml());
				TreeMap treeMap = (TreeMap) snowActivityCache.get(sgb.getFid1() + ","
						+ mid.toString() + "," + sgb.getFid2());
				if (treeMap == null) {
					treeMap = (TreeMap) snowActivityCache.get(sgb.getFid2() + ","
							+ mid.toString() + "," + sgb.getFid1());
				}
				treeMap.put(Integer.valueOf(treeMapkey),sab);
				treeMapkey--;
			}
		}
		return 7;// 投雪球成功
	}

	// 扫
	public int clearSnow() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 1;// 不是家族成员
		}
		int tid = getParameterInt("tid");// 道具类型id

		SnowGameToolTypeBean sgttb = service.selectToolType(tid);
		if(sgttb==null){
			return 6;// 道具不存在
		}

		if (!this.isCooldown("fmSnowCD", sgttb.getSpendTime() * 1000)) {
			return 5; // CD
		}

		Integer mid = new Integer(request.getAttribute("mid").toString());
		MatchBean smb = (MatchBean) matchCache.get(mid);
		if (smb == null) {
			return 2;// 未参加比赛
		}
		HashMap memberMap = ((SnowBean) smb.getGameMap().get(
				new Integer(fmUser.getFm_id()))).getMemberBean();
		MemberBean mb = (MemberBean) memberMap.get(new Integer(fmUser.getId()));
		if (mb == null) {
			return 2;// 未审核通过
		}
		synchronized (snowMoneyCache) {
			SnowMoneyBean smBean = getSnowMoneyBean(fmUser.getId());

			if (smBean.getMoneyHold() < sgttb.getSpendMoney()) {
				return 3;// 钱不够
			}
			mb.setPaySweep(mb.getPaySweep() + sgttb.getSpendMoney());
			smBean.setMoneyHold(smBean.getMoneyHold() - sgttb.getSpendMoney());
		}
		
		SnowBean gb = (SnowBean) smb.getGameMap().get(
				Integer.valueOf(fmUser.getFm_id()));
		synchronized(gb){
		gb.setPrize(gb.getPrize() + sgttb.getSpendMoney());// 修改自己家族的奖金数，和积雪量
		gb.setSnowAccumulation(gb.getSnowAccumulation()
						- sgttb.getSnowEffect() > 0 ? gb.getSnowAccumulation()
						- sgttb.getSnowEffect() : 0);
		}
		Integer vsfid = Integer.valueOf(gb.getFid2());// 修改对方家族的奖金数
		SnowBean vsgb = (SnowBean) smb.getGameMap().get(vsfid);
		synchronized(vsgb){// 修改雪仗中所花费的雪币金额
			vsgb.setPrize(vsgb.getPrize() + sgttb.getSpendMoney());
		}
		if (tid == 7) {
			synchronized (snowActivityCache) {
				SnowActivityBean sab = new SnowActivityBean();
				sab.setAType(1);// 使用了
				sab.setTType(7);// 推雪机
				sab.setFmName(gb.getFmName1());
				sab.setUName(fmUser.getNickNameWml());
				TreeMap treeMap = (TreeMap) snowActivityCache.get(gb.getFid1() + ","+ mid.toString() + "," + gb.getFid2());
				
				if (treeMap == null) {
					treeMap = (TreeMap) snowActivityCache.get(gb.getFid2() + ","+ mid.toString() + "," + gb.getFid1());
				}
				
				treeMap.put(Integer.valueOf(treeMapkey),sab);
				
				treeMapkey--;
			}
		}
		request.setAttribute("toolMoney", Integer
				.valueOf(sgttb.getSpendMoney()));
		return 4;
	}

	// 造
	public int makeSnow() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 1;// 不是家族成员
		}

		Integer mid = new Integer(request.getAttribute("mid").toString());
		MatchBean smb = (MatchBean) matchCache.get(mid);
		if (smb == null) {
			return 6;// 没有这个赛事
		}
		SnowBean gb = (SnowBean) smb.getGameMap().get(
				new Integer(fmUser.getFm_id()));
		if (gb == null) {
			return 7;// 该家族未参加
		}
		HashMap memberMap = gb.getMemberBean();
		if (memberMap == null) {
			return 2;
		}
		MemberBean mb = (MemberBean) memberMap.get(new Integer(fmUser.getId()));
		if (mb == null) {
			return 2;// 未审核通过
		}

		int tid = getParameterInt("tid");// 道具类型id
		SnowGameToolTypeBean sgttb = service.selectToolType(tid);
		if(sgttb==null){
			return 9;// 道具类型不存在
		}
		SnowMoneyBean smBean = getSnowMoneyBean(fmUser.getId());
		int money = smBean.getMoneyHold();
		if (money < sgttb.getSpendMoney()) {
			return 3;// 钱不够
		}
		HashMap toolsMap = ((SnowBean) smb.getGameMap().get(
				new Integer(fmUser.getFm_id()))).getToolsBean();
		if (toolsMap != null) {
			if (toolsMap.size() > 0) {
				if (toolsMap.size()  > 99) {
					return 4;// 道具数量不能超过100
				}
			}
		}
		if (!this.isCooldown("fmSnowCD", sgttb.getSpendTime() * 1000)) {
			return 8; // CD
		}

		synchronized (mb) {
			mb.setPayMake(mb.getPayMake() + sgttb.getSpendMoney());
		}

		smBean.setMoneyHold(smBean.getMoneyHold() - sgttb.getSpendMoney());

		synchronized (toolsMap) {// 将道具放入缓存中
			SnowGameToolsBean bean = new SnowGameToolsBean(0);
			bean.setFmId(fmUser.getFm_id());
			bean.setTid(tid);
			int key = SnowGameToolsBean.getCountId();
			bean.setId(SnowGameToolsBean.getCountId());
			toolsMap.put(Integer.valueOf(key), bean);

		}
		synchronized (gb) {// 修改缓存中自己家族的游戏花费
			gb.setPrize(gb.getPrize() + sgttb.getSpendMoney());
		}
		
		int vsfid = gb.getFid2();
		SnowBean vsgb = (SnowBean) smb.getGameMap().get(Integer.valueOf(vsfid));
		
		synchronized(vsgb){// 修改缓存中对方家族的游戏花费
			vsgb.setPrize(vsgb.getPrize() + sgttb.getSpendMoney());
		}
		if (tid == 4) {
			synchronized (snowActivityCache) {
				SnowActivityBean sab = new SnowActivityBean();
				sab.setAType(2);// 做了一个
				sab.setTType(4);// 投雪机
				sab.setFmName(gb.getFmName1());
				sab.setUName(fmUser.getNickNameWml());
				TreeMap treeMap = (TreeMap) snowActivityCache.get(gb.getFid1() + ","
						+ mid.toString() + "," + gb.getFid2());
				if (treeMap == null) {
					treeMap = (TreeMap) snowActivityCache.get(gb.getFid2() + ","
							+ mid.toString() + "," + gb.getFid1());
				}
				treeMap.put(Integer.valueOf(treeMapkey),sab);
				treeMapkey--;
			}
		}
		request.setAttribute("toolMoney", Integer
				.valueOf(sgttb.getSpendMoney()));
		return 5;
	}

	// 显示一个问题
	public SnowQuestionBean getOneQuestion() {
		int num = service
				.selectIntResult("select count(id) from fm_game_snow_question");
		if (num == 0) {
			return null;// 数据库无题
		}
		SnowQuestionBean sqb = service.selectQuestion();
		while (sqb == null) {
			sqb = service.selectQuestion();
		}
		return sqb;
	}

	// 检查答案
	public int checkAnswer() {
		int answer = getParameterInt("a");
		SnowQuestionBean bean = (SnowQuestionBean) session
				.getAttribute("QuestionBean");
		if (bean == null) {
			return 0;// 该题目不存在
		}
		setAttribute2("QuestionBean", null);
		if (bean.getAnswer() != answer) {
			return 1;// 答错
		}
		return 2;// 答对
	}

	// 得到家族成员在某期比赛中1制造花费，2清理花费，3击中次数的列表
	public List getMemberGameList() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return null;// 不是家族成员
		}
		int mid = getParameterInt("mid");
		if (mid == 0) {
			return null;// 赛事不存在
		}
		String type = getParameterString("cmd");
		String cmd = "";
		if (type.equals("m")) {
			cmd = "pay_make";
		} else if (type.equals("c")) {
			cmd = "pay_sweep";
		} else if (type.equals("f")) {
			cmd = "total_hit";
		} else {
			return null;// 修改url参数产生的错误
		}
		int c = service
				.selectIntResult("select count(id) from fm_game_member where fid="
						+ fmUser.getFm_id() + " and m_id=" + mid);
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(
				this, c, 10, "p");
		List list = service.selectGameData(cmd, mid, fmUser.getFm_id(), paging
				.getStartIndex(), paging.getCountPerPage());
		String s = paging.shuzifenye("fightLists.jsp?mid=" + mid + "&amp;cmd="
				+ type, true, "|", response);
		setAttribute("gameData", s);
		return list;
	}

	// 得到某期比赛的家族vs列表
	public List getFightList(String page, boolean flag,int mid) {
		setAttribute("pageFight", "");
		List list = service.selectGameList(mid);
		int c = list.size();
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(
				this, c, 10, "p");
		int start = paging.getStartIndex(), end = paging.getStartIndex()
				+ paging.getCountPerPage();
		if (list.size() < 10) {
			list = list.subList(paging.getStartIndex(), list.size());
		} else if (list.size() % 10 < 10) {
			if (start == (list.size() / 10) * 10) {
				end = list.size();
			}
			list = list.subList(start, end);
		}
		String s = paging.shuzifenye(page, flag, "|", response);
		setAttribute("pageFight", s);
		return list;
	}

	// 得到这个家族参加的所有雪仗比赛
	public List getFmGameList() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return null;// 不是家族成员
		}
		
		List attendList=new ArrayList();
		List mlist=service.getMatchList("game_type=2 and state=2 order by id desc ");
		if(mlist==null||mlist.size()<1){
			return null;// 没有历史记录
		}
		for(int i=0;i<mlist.size();i++){
			MatchBean mb=(MatchBean)mlist.get(i);
			int mid=mb.getId();
			long time=mb.getCreateTime();
			
			SnowBean bean = service.selectOneMatch(fmUser.getFm_id(), mid);
			if(bean==null){
				bean=new SnowBean();
				bean.setFid1(0);
				bean.setHoldTime(DateUtil.formatDate(new Date(time)));
				bean.setMid(mid);
			}
			attendList.add(bean);
		}
		
		int c = attendList.size();
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		int start = paging.getStartIndex(), end = paging.getStartIndex()+ paging.getCountPerPage();
		if (attendList.size() < 10) {
			attendList = attendList.subList(paging.getStartIndex(), attendList.size());
		} else if (attendList.size() % 10 < 10) {
			if (start == (attendList.size() / 10) * 10) {
				end = attendList.size();
			}
			attendList = attendList.subList(start, end);
		}
		String s = paging.shuzifenye("history.jsp", false, "|", response);
		setAttribute("gameFights", s);
		return attendList;
	}

	public int getOneMatch() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 2;// 不是家族成员
		}
		int mid = getParameterInt("mid");
		if (mid == 0) {
			return 3;// 赛事不存在
		}
		MatchBean mhb=service.getMatchBean(" id="+mid);
		if(mhb==null){
			return 3;
		}
		SnowBean bean = service.selectOneMatch(fmUser.getFm_id(), mid);
		if (bean == null) {
			bean=new SnowBean();
			bean.setFid1(0);
			bean.setHoldTime(DateUtil.formatDate(new Date(mhb.getCreateTime())));
			bean.setMid(mid);
		}
		request.setAttribute("matchDetail", bean);
		return 4;// 得到赛事
	}

	// 得到要造的雪球类型
	public int getOneTypeTool() {
		int tid = getParameterInt("tid");
		if (tid == 0) {
			return 0;
		}
		SnowGameToolTypeBean bean = service.selectToolType(tid);
		if (bean == null) {
			return 1;// 不存在的道具
		}
		request.setAttribute("oneTool", bean);
		return 2;
	}

	// 比赛到点后将所有数据存入数据库
	public static void getSave(int mid) {
		MatchBean smb = (MatchBean) GameAction.matchCache.get(new Integer(mid));
		if (smb == null || smb.getType() != 2)
			return;
		
		synchronized(smb){
			smb.setState(2);
			service.upd(" update fm_game_match set state=2,end_time=now() where id="+ smb.getId());
	
			List list = service.selectFmByScore(smb.getId());
			if (list == null || list.size() < 1)
				return;
			for (int i = 0; i < list.size(); i++) {// 循环参赛家族，存入数据库
				Integer key = (Integer) list.get(i);
				SnowBean gb = (SnowBean) smb.getGameMap().get(key);// 一个家族
	
				int rank = gb.getRank();
				int vsfid = gb.getFid2();
				if (vsfid != 0) {
					SnowBean vsgb = (SnowBean) smb.getGameMap().get(
							Integer.valueOf(vsfid));// 它的对手家族
					int snow1 = gb.getSnowAccumulation();// 一个家族的积雪量
					int snow2 = vsgb.getSnowAccumulation();// 另一家族的积雪量
	
					// 如果未有家族胜出，根据积雪量判断哪个家族获胜，对平手的家族补填比赛信息，方便存入数据库
						if (rank == 0 || rank == 3||rank==4) {
								long spend = System.currentTimeMillis()- startTime;// 求出双方家族的对战时间
								if(gb.getSpendTime()<1){// 存入用时
									gb.setSpendTime(spend);
								}
								if(vsgb.getSpendTime()<1){
									vsgb.setSpendTime(spend);
								}
							if (snow1 < snow2) {
								gb.setRank(1);
								gb.setScore(Constants.SNOW_WIN_SCORE);
								boolean vsAttend = service.isAttend(mid, gb.getFid2());
								long prize=0;
								if(vsAttend){// 如果对方家族参赛，就返回双方花费的95%
									prize = ((10000000l
											* (gb.getNumTotal() + vsgb.getNumTotal())
											+ gb.getPrize()*100000l) * 95  + 50)/ 100;
								}else{// 如果对方家族未参赛，就返回单方全部花费
									prize = 10000000l * gb.getNumTotal()+ gb.getPrize()*100000l;
								}
								gb.setPrize(prize);
							} else if (snow1 > snow2) {
								gb.setRank(2);
								gb.setScore(Constants.SNOW_FALL_SCORE);
								gb.setPrize(0);
							} else if (snow1 == snow2) {
								if(gb.getRank()==0){
									gb.setRank(3);
									vsgb.setRank(4);
								}
								gb.setPrize(gb.getNumTotal() * 10000000l);
								gb.setScore(Constants.SNOW_DEUCE_SCORE);
							}
						}
			
				}
				boolean isAttend = service.isAttend(mid, gb.getFid1());
				if (!isAttend) {// 报名参赛后，审核通过，游戏开始后是否有人进入游戏，没人进入游戏返回false
					gb.setPrize(0);
					gb.setScore(0);
					gb.setNumTotal(0);
				}
				List memberList = service.selectApplyMembers("m_id=" + smb.getId()
						+ " and fid=" + key.intValue());
				HashMap memberMap = ((SnowBean) smb.getGameMap().get(key))
						.getMemberBean();
	
				int con = 0, num = gb.getNumTotal();
				if (num == 0) {
					con = 0;
				} else if (num > 0) {
					if (gb.getRank() == 1) {// 计算贡献度
						con = Constants.WIN_HOME_EXPLOIT / num + ( Constants.WIN_HOME_EXPLOIT  % num > 5 ? 1 : 0);
					} else if (gb.getRank() == 2) {
						con = Constants.LOSE_HOME_EXPLOIT;
					} else if (gb.getRank() == 3||gb.getRank() == 4) {
						con = Constants.DEUCE_HOME_EXPLOIT  / num + (Constants.DEUCE_HOME_EXPLOIT % num > 5 ? 1 : 0);
					}
				}
				for (int j = 0; j < memberList.size(); j++) {// 循环插入用户比赛记录
					MemberBean memberBean = (MemberBean) memberList.get(j);
					ApplyBean bean = service.getApplyBean(" m_id=" + mid + " and uid="+ memberBean.getUid());
					if(bean.getIsPay()==0)// 未进入游戏比赛的用户信息不做记录
						continue;
					
					MemberBean mb = (MemberBean) memberMap.get(new Integer(memberBean.getUid()));
					mb.setContribution(con);
					service.insertMember(mb);
					// 更新用户雪币记录
					int money = getSnowMoney(mb.getUid());
					service.updateSnowMoney(money, mb.getUid());
					
					addFmUserGameInfo(mb.getUid(),mb.getFid(),con,"参加雪仗比赛|"+con);// 修改和记录用户家族功勋
				}
				
				// 修改或者插入家族游戏积分表
				if(gb.getRank()==1||gb.getRank()==3||gb.getRank()==4){
					addScore(gb.getFid1(), 2, gb.getScore());
				}
				
				if (gb.getRank() != 2) {// 胜利和平局的家族有奖金，输的没有1为胜利2为输，3和4为平局
					GameAction.updateFmFund(gb.getFid1(), gb.getPrize());
					
					FamilyHomeBean familyHomeBean = getFmByID(gb.getFid1());// 更新家族基金缓存
					FamilyAction.service.insertFmFundDetail(gb.getFid1(), gb.getPrize(), FundDetail.GAME_TYPE,
							familyHomeBean.getMoney());
					
				}
				if(gb.getGamePoint()>0){// 满足条件(击中对方次数超过20)的家族家族经验值加1
					addFmGameInfo(gb.getFid1(),1);// 修改家族总的游戏经验值
				}
				service.insertFmGame(gb);// 插入家族对抗记录
				if (gb.getNumTotal()*10000000l > 0) {
					addFundHistory(gb.getFid1(), "扣除雪仗报名费" + gb.getNumTotal()*10000000l);
				}
			}
		}
		startTime=0;// 比赛结束后将开始比赛时间清零
	}
}