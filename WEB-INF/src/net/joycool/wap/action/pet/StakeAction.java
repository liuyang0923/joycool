package net.joycool.wap.action.pet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class StakeAction extends PetAction {

	public StakeAction(HttpServletRequest request) {
		super(request);
	}

	private static IMessageService messageService = null;

	// 记录当前比赛的在map中的key
	public static int stakeMatch = 0;

	// 长跑游戏的类型id
	public static int STAKEMATCH = 3;

	// 单位时间内，标准的距离
	public static int LONG = 200;

	// 分页数
	public static Integer lockswim = new Integer(5);

	// 游戏中设定的玩家人数
	public static int STAKE_PLAYNUMBER = 8;

	// 分页数
	public static int NUMBER_PER_PAGE = 10;

	public static int TOTAL_LONG = 5000;

	public void stakeing() {
		// 游戏场次id
		int id = StringUtil.toInt(request.getParameter("id"));
		int temp = id;
		// 默认进入当前最新的比赛
		if (id < 0)
			id = stakeMatch;

		if (petUser != null)
			request.setAttribute("petUser", petUser);

		stakeIndex();

		MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
				.get(new Integer(id));
		if ((matchrunbean != null)
				&& ((matchrunbean.getCondition() <= 1) || (temp >= 0))) {
			request.setAttribute("matchrunbean", matchrunbean);
			this.doTip("wait", "游戏中");
		} else {
			this.doTip("null", "游戏中");
			return;
		}

		Vector stakeList = server
				.getStakeList("user_id = " + loginUser.getId());

		if (stakeList != null)
			request.setAttribute("stakeList", stakeList);

		// 所有的赌注
		long totalstake = server.getStakeSum("1 = 1");
		request.setAttribute("totalstake", new Long(totalstake));

		// 共有多少人参加赌博
		int totalCount = server.getTotalCount();
		request.setAttribute("totalCount", new Integer(totalCount));
	}

	public void stakeIndex() {
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得总数
		int totalHallCount = matchMap[STAKEMATCH].size();
		int totalHallPageCount = totalHallCount / NUMBER_PER_PAGE;
		if (totalHallCount % NUMBER_PER_PAGE != 0) {
			totalHallPageCount++;
		}
		if (pageIndex > totalHallPageCount - 1) {
			pageIndex = totalHallPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Iterator iter = matchMap[STAKEMATCH].values().iterator();
		int number = 0;
		if (totalHallCount > NUMBER_PER_PAGE) {
			for (int i = 0; i < totalHallCount - (pageIndex + 1)
					* NUMBER_PER_PAGE; i++)
				iter.next();
			number = NUMBER_PER_PAGE;
		}
		number = totalHallCount;

		Vector vector = new Vector(number);
		int temp = 0;
		while (iter.hasNext()) {
			vector.add(iter.next());
			temp++;
			if (pageIndex + 1 != totalHallPageCount) {
				if (temp >= NUMBER_PER_PAGE)
					break;
			} else {
				if (temp >= totalHallCount - pageIndex * NUMBER_PER_PAGE)
					break;
			}

		}

		// String prefixUrl = "stakeindex.jsp?type=3";
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		// request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("vector", vector);

	}

	public static float getStake(int id) {
		// 单独某个宠物的
		long onestake = server.getStakeSum("pet_id = " + id);

		// 所有的赌注
		long totalstake = server.getStakeSum("1 = 1");
		if ((onestake == 0) || (totalstake == 0))
			return 0.0f;
		else
			return ((int) (totalstake * 0.9f / onestake * 10)) / 10.0f;
	}

	public void viewmessage() {
		// 游戏场次id
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id == -1) {
			this.doTip(null, "参数错误");
			return;
		}
		MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
				.get(new Integer(id));
		if (matchrunbean != null) {
			request.setAttribute("matchrunbean", matchrunbean);
			this.doTip("wait", "游戏中");
		} else {
			this.doTip(null, "此游戏不存在");
			return;
		}

	}

	public void stakeadd() {
		// 游戏场次id
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id == -1) {
			this.doTip(null, "参数错误");
			return;
		}
		// 取得游戏map
		MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
				.get(new Integer(id));
		if ((matchrunbean == null) || (matchrunbean.getCondition() != 0)) {
			this.doTip(null, "比赛已开始,无法下注");
			return;
		}
		// 要在哪个宠物身上下注
		int order = StringUtil.toInt(request.getParameter("order"));
		if ((order < 0) || (order >= STAKE_PLAYNUMBER)) {
			this.doTip(null, "参数错误");
			return;
		}

		PlayerBean[] playbean = matchrunbean.getPlayer();
		// request.setAttribute("playbean", playbean[order]);
		// 取得宠物id
		PetUserBean petBean = load(0, playbean[order].getPetid());
		if (petBean == null) {
			this.doTip(null, "参数错误");
			return;
		} else
			request.setAttribute("petBean", petBean);

		MatchStakeBean stakeBean = server.getStake(" user_id ="
				+ loginUser.getId() + " and pet_id = " + petBean.getId());
		if (stakeBean != null)
			request.setAttribute("stakeBean", stakeBean);

		request.setAttribute("order", new Integer(order));
		request.setAttribute("matchrunbean", matchrunbean);

		// 单独某个宠物的
		long onestake = server.getStakeSum("pet_id = " + petBean.getId());
		request.setAttribute("onestake", new Long(onestake));

		// 有多少人在这个宠物身上下注
		int oneCount = server.getStakeCount("pet_id = " + petBean.getId());
		request.setAttribute("oneCount", new Integer(oneCount));

		// 这个参赛宠物今天赢了多少乐币
		MatchUserBean matchUserBean = server.getMatchUser("pet_user = "
				+ petBean.getId());
		// long todaystake = matchUserBean.getTotalstake();
		// request.setAttribute("todaystake", new Long(todaystake));
		//
		// // 这个宠物今天赢了多少次
		// int wintime = matchUserBean.getWintime();
		// request.setAttribute("wintime", new Integer(wintime));
		//		
		request.setAttribute("matchUserBean", matchUserBean);
	}

	public void stakeResult() {
		// 游戏场次id
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id == -1) {
			this.doTip(null, "参数错误");
			return;
		}
		// 取得游戏map
		MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
				.get(new Integer(id));
		if ((matchrunbean == null) || (matchrunbean.getCondition() != 0)) {
			this.doTip(null, "比赛已开始,无法下注");
			return;
		}
		// 要在哪个宠物身上下注
		int order = StringUtil.toInt(request.getParameter("order"));
		if ((order < 0) || (order >= STAKE_PLAYNUMBER)) {
			this.doTip(null, "参数错误");
			return;
		}
		int number = StringUtil.toInt(request.getParameter("number"));
		if (number <= 0) {
			this.doTip(null, "您输入的金额不对");
			return;
		}
		PlayerBean[] playbean = matchrunbean.getPlayer();
		// 取得宠物id
		PetUserBean petBean = load(0, playbean[order].getPetid());
		if (petBean == null) {
			this.doTip(null, "参数错误");
			return;
		}
		UserStatusBean statusBeans = UserInfoUtil.getUserStatus(loginUser
				.getId());
		if (statusBeans.getGamePoint() < number) {
			this.doTip(null, "您身上带的钱不够！");
			return;
		}
		MatchStakeBean stakeBean = server.getStake(" user_id ="
				+ loginUser.getId() + " and pet_id = " + petBean.getId());
		if (stakeBean == null) {
			// 新加记录
			stakeBean = new MatchStakeBean();
			stakeBean.setPet_id(petBean.getId());
			stakeBean.setPet_name(petBean.getName());
			stakeBean.setStake(number);
			stakeBean.setUser_id(loginUser.getId());
			stakeBean.setUser_name(loginUser.getNickName());

			server.addStake(stakeBean);
		} else {
			// 更改记录
			server.updateStake("stake = stake + " + number, " user_id ="
					+ loginUser.getId() + " and pet_id = " + petBean.getId());
		}

		// 清空全部宠物赌注的缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH,
				"SELECT SUM(stake) FROM pet_match_stake where 1 = 1");
		// 单个宠物宠物赌注的缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH,
				"SELECT SUM(stake) FROM pet_match_stake where pet_id = "
						+ petBean.getId());
		// 单个宠物宠物下注人数的缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH,
				"SELECT count(*) FROM pet_match_stake where pet_id = "
						+ petBean.getId());
		// 所有宠物下注人数的缓存
		OsCacheUtil
				.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH,
						"select count(distinct user_id) from pet_match_stake");
		// 本人下注列表缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH,
				"SELECT * from pet_match_stake WHERE user_id = "
						+ loginUser.getId());

		// 扣钱
		UserInfoUtil.updateUserStatus("game_point=game_point-" + number,
				"user_id=" + loginUser.getId(), loginUser.getId(),
				UserCashAction.OTHERS, "玩家下宠物赌注" + number + "乐币");
		this.doTip("success", "下注成功");
	}

	// 每天早晨1点更新删除宠物比赛当天经验点
	public static void hourTask() {
		// 宠物比赛当天经验点
		String sql = "update pet_user set yesterday = today";
		SqlUtil.executeUpdate(sql, Constants.DBShortName);

		sql = "update pet_user set today = 0";
		SqlUtil.executeUpdate(sql, Constants.DBShortName);

		// 清空前一局比赛的缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH);
		// 清空赌博参赛人员表
		sql = "delete from pet_match_user";
		SqlUtil.executeUpdate(sql, 4);
		/**
		 * guip修改mark=0为可用
		 */
		Vector matchPetList = server.getUserList(
				" 1=1 and mark=0 order by yesterday DESC limit 8", 60);

		PetUserBean matchUser;
		MatchUserBean bean;
		Iterator iter = matchPetList.iterator();
		// /建立赌博参赛人员表mark=0为可用
		while (iter.hasNext()) {
			matchUser = (PetUserBean) iter.next();
			// 将宠物标记为游戏状态
			bean = new MatchUserBean();
			bean.setPet_user(matchUser.getId());
			bean.setYesterday(matchUser.getYesterday());
			bean.setName(matchUser.getName());
			// 生成参赛宠物列表
			server.addMatchUser(bean);
		}

	}

	// 非第一个玩家加入游戏
	public void stakeMatchAct() {
		String url = "/pet/stakeact.jsp";
		// 游戏场次id
		int task = StringUtil.toInt(request.getParameter("task"));
		// 开设赌局
		if (task == 1) {
			// 取得游戏map
			MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
					.get(new Integer(stakeMatch));
			if ((matchrunbean != null) && (matchrunbean.getCondition() == 0)) {
				this.doTip(null, "赌局已经开始，请不要刷新此页面");
				return;
			}
			// 清空前一局比赛的缓存
			//OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH);
			// String sql = "delete from pet_match_user";
			// server.executeUpdate(sql);
			//String sql = "delete from pet_match_stake";
			//server.executeUpdate(sql);
			// // 取得参加比赛人员的列表
			// Vector matchPetList = server.getUserList(
			// " 1=1 order by yesterday DESC limit 8", 60);
			// // 是否够8个人
			// if (matchPetList.size() != STAKE_PLAYNUMBER) {
			// doTip(null, "人数错误");
			// return;
			// }

			// 新建一个赌局的bean
			matchrunbean = new MatchRunBean(STAKE_PLAYNUMBER);
			// 标记为投注状态
			matchrunbean.setCondition(0);
			// 设定长跑比赛计数器,以及treemap的key
			MATCH[STAKEMATCH]++;
			matchrunbean.setId(MATCH[STAKEMATCH]);
			stakeMatch = MATCH[STAKEMATCH];
			// 设定游戏类型
			matchrunbean.setType(STAKEMATCH);

			PetUserBean matchUser;
			MatchUserBean bean;
			Vector matchPetList = server.getMatchUserList("1 = 1");
			Iterator iter = matchPetList.iterator();
			// 生成参赛宠物列表
			while (iter.hasNext()) {
				bean = (MatchUserBean) iter.next();
				matchUser = load(0, bean.getPet_user());
				// 将宠物放入游戏
				matchrunbean.addPlayer(matchUser);
				// bean = new MatchUserBean();
				//
				// bean.setPet_user(matchUser.getId());
				// bean.setYesterday(matchUser.getYesterday());
				// bean.setName(matchUser.getName());
				// // 生成参赛宠物列表
				// server.addMatchUser(bean);
			}
			// 将比赛加入赌局的map中
			matchMap[STAKEMATCH].put(new Integer(matchrunbean.getId()),
					matchrunbean);
			doTip("success", "赌局开设成功");
		} else if (task == 2) {
			// 清空前一局比赛的缓存
			OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH);
			// 清空赌博参赛人员表
			String sql = "delete from pet_match_user";
			SqlUtil.executeUpdate(sql, 4);
			/**
			 * guip修改mark=0为可用
			 */
			Vector matchPetList = server.getUserList(
					" 1=1 and mark=0 order by yesterday DESC limit 8", 60);

			PetUserBean matchUser;
			MatchUserBean bean;
			Iterator iter = matchPetList.iterator();
			// /建立赌博参赛人员表
			while (iter.hasNext()) {
				matchUser = (PetUserBean) iter.next();
				// 将宠物标记为游戏状态
				bean = new MatchUserBean();
				bean.setPet_user(matchUser.getId());
				bean.setYesterday(matchUser.getYesterday());
				bean.setName(matchUser.getName());
				// 生成参赛宠物列表
				server.addMatchUser(bean);
			}
		} else if (task == 5) {
			// 取得游戏map
			MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
					.get(new Integer(stakeMatch));
			if (matchrunbean == null) {
				this.doTip(null, "参数错误，可能赌局尚未开始");
				return;
			}
			if (matchrunbean.getCondition() > 0) {
				this.doTip(null, "比赛已经开始，请不要刷新此页面");
				return;
			}
			// 开始游戏
			stakeStartGame(matchrunbean);
			doTip("success", "开始游戏");

		} else if (task == 6) {
			// 赌局描述信息
			String name = request.getParameter("name");
			name = StringUtil.noEnter(name);
			if (name.length() > 0) {
				// 取得游戏map
				MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
						.get(new Integer(stakeMatch));
				if (matchrunbean != null) {
					matchrunbean.setMessage(name);
					doTip("success", "操作成功");
				} else {
					doTip(null, "参数错误");
				}
			} else {
				doTip(null, "参数错误");
				return;
			}
		}

		request.setAttribute("url", url);

	}
    //自动下注调用
	public static void stakeMatch() {
		MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
				.get(new Integer(stakeMatch));
		// 新建一个赌局的bean
		matchrunbean = new MatchRunBean(STAKE_PLAYNUMBER);
		// 标记为投注状态
		matchrunbean.setCondition(0);
		// 设定长跑比赛计数器,以及treemap的key
		MATCH[STAKEMATCH]++;
		matchrunbean.setId(MATCH[STAKEMATCH]);
		stakeMatch = MATCH[STAKEMATCH];
		// 设定游戏类型
		matchrunbean.setType(STAKEMATCH);

		PetUserBean matchUser;
		MatchUserBean bean;
		Vector matchPetList = server.getMatchUserList("1 = 1");
		Iterator iter = matchPetList.iterator();
		// 生成参赛宠物列表
		while (iter.hasNext()) {
			bean = (MatchUserBean) iter.next();
			matchUser = load(0, bean.getPet_user());
			// 将宠物放入游戏
			matchrunbean.addPlayer(matchUser);
		}
		// 将比赛加入赌局的map中
		matchMap[STAKEMATCH].put(new Integer(matchrunbean.getId()),
				matchrunbean);
	}
  //自动开始比赛调用
	public static void stakeMatchStart() {
		MatchRunBean matchrunbean = (MatchRunBean) matchMap[STAKEMATCH]
				.get(new Integer(stakeMatch));
		// 开始游戏
		if(matchrunbean!=null){
		stakeStartGame(matchrunbean);
		}
	}

	// 游戏开始前要做的事
	public static void stakeStartGame(MatchRunBean matchrunbean) {
		MatchEventBean matchEventBean;
		MatchFactorBean factorBean;
		PetUserBean petBean;
		float change = 0;
		int temp = 0;

		// 取得所有下注人的列表
		HashMap noticMap = new HashMap();
		// Vector userList = server.getStakeList("1 = 1 group by user_id ");
		Vector userList = server.getSumStakeList();
		MatchStakeBean petStake;
		for (int i = 0; i < userList.size(); i++) {
			petStake = (MatchStakeBean) userList.get(i);
			noticMap.put(new Integer(petStake.getUser_id()), "");
		}

		// 开始游戏
		PlayerBean[] playbean = matchrunbean.getPlayer();
		// 计算因子
		factorBean = server.getFactor("id =" + matchrunbean.getType());
		for (int j = 0; j < playbean.length; j++) {
			petBean = load(0, playbean[j].getPetid());
			noticMap.put(new Integer(petBean.getUser_id()), "");
			// 寵物因子
			if (petBean != null) {
				playbean[j].setFactor(factor(petBean, factorBean));
			}
		}
		// 给参赛的宠物和所有投注的玩家发信息
		Set set = noticMap.keySet(); // get set-view of keys
		Iterator itr = set.iterator();
		Integer noticInt;
		while (itr.hasNext()) {
			noticInt = (Integer) itr.next();

			// 給參加比賽的寵物主人發信息
			NoticeBean notice = new NoticeBean();
			notice.setTitle("本轮宠物赌博投注赛已开始!");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(noticInt.intValue());
			notice.setHideUrl("/pet/stakeindex.jsp");
			notice.setLink("/pet/stakeing.jsp?id="
					+ matchrunbean.getId() + "&type=" + matchrunbean.getType());
			// liq_2007-7-16_增加宠物消息类型_start
			notice.setMark(NoticeBean.PET);
			// liq_2007-7-16_增加宠物消息类型_end
			noticeService.addNotice(notice);
		}
		// 只将所有下注的人放入表中
		noticMap = new HashMap();
		// userList = server.getSumStakeList();
		for (int i = 0; i < userList.size(); i++) {
			petStake = (MatchStakeBean) userList.get(i);
			// 每个下注人赌注的总和
			noticMap.put(new Integer(petStake.getUser_id()), new Long(petStake
					.getStake()));
		}
		matchrunbean.setNoticMap(noticMap);
		// 所有的赌注
		long totalstake = server.getStakeSum("1 = 1");
		// 所有赌注总额
		matchrunbean.setTotalstake(totalstake);
		// 下赌注的总人数
		matchrunbean.setTotalpeople(userList.size());
		// 赌博赛开始
		matchrunbean.setCondition(1);
	}

	// 计算因子
	public static int factor(PetUserBean petBean, MatchFactorBean factorBean) {
		int x = (int) (petBean.getHungry()
				* petBean.getHealth()
				* (factorBean.getAl() * Math.sqrt(petBean.getAgile())
						+ factorBean.getIn() * Math.sqrt(petBean.getIntel()) + factorBean
						.getSt()
						* Math.sqrt(petBean.getStrength())) / 10000);

		return x;
	}

	/**
	 * 
	 * @author liq
	 * @explain，
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static void staketask() {
		MatchFactorBean factorBean;
		String log;
		Vector eventList;
		MatchEventBean matchEventBean;
		PetUserBean petBean = new PetUserBean();
		float change = 0;
		int factor = 0;
		Iterator iter = matchMap[STAKEMATCH].values().iterator();
		while (iter.hasNext()) {
			MatchRunBean matchrunbean = (MatchRunBean) iter.next();

			if (matchrunbean.getCondition() == 0) {
				// 建立后等待中
			} else if (matchrunbean.getCondition() == 1) {
				// 取得随机时间列表
				eventList = server.getMatchEventList("gameid ="
						+ matchrunbean.getType());
				// 游戏过程中
				PlayerBean[] playbean = matchrunbean.getPlayer();
				for (int i = 0; i < STAKE_PLAYNUMBER; i++) {
					if (playbean[i].getPosition() < TOTAL_LONG) {
						// ////////////////////////////////////////////////////////////////////////////////////////////////////
						// 宠物属性因子发生作用
						// 取得宠物id
						petBean = load(0, playbean[i].getPetid());

						if (playbean[i].getFactor() > 0)
							factor = RandomUtil
									.nextInt(playbean[i].getFactor());
						change = LONG;
						// /////////////////////////////////////////////////////////////////////////////////////////////////
						// 发生了随机事件
						if (RandomUtil
								.percentRandom(MatchEventBean.PROBABILITY)) {
							// 取得随机事件
							// 取得具体的事件
							matchEventBean = (MatchEventBean) eventList
									.get(RandomUtil.nextInt(eventList.size()));
							// 事件对长跑成绩发生影响
							change = change + change
									* matchEventBean.getFactor();

							// 将发生的时间记录对应比赛的log中，在页面上显示
							log = matchEventBean.getDescription().replace(
									"@", StringUtil.toWml(petBean.getName()));
							matchrunbean.addLog(log);
						} else {
							// 没有发生随机事件
						}
						// /////////////////////////////////////////////////////////////////////////////////////////////////
						// 总距离加上单位时间内的距离 以及属性因子的影响
						playbean[i].setPosition(playbean[i].getPosition()
								+ (int) change + factor);

						if (playbean[i].getPosition() >= TOTAL_LONG) {
							matchrunbean.addLog(StringUtil.toWml(petBean
									.getName())
									+ "已经游过了终点线");
						}
					} else {
						// 过终点后每圈加10000
						playbean[i]
								.setPosition(playbean[i].getPosition() + 10000);
					}
					factor = 0;
				}
				// /////////////////////////////////////////////////////////////////////////////////////////////////
				// 根据宠物的位置排序 0-8对应第一到第八

				Arrays.sort(playbean);

				// 比赛结束,判断排名倒地一的人是否跑完
				if (TOTAL_LONG <= playbean[playbean.length - 1].getPosition()) {
					// 清空所有缓存
					OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH);
					matchrunbean.setCondition(2);
					long tatalStake = server.getStakeSum("1 = 1");
					long winStake = server.getStakeSum("pet_id = "
							+ playbean[0].getPetid());
					float per = ((float) tatalStake) / 100;
					long temp;
					HashMap noticMap = new HashMap();
					String tempNotic;
					// 给参加比赛的宠物发信
					for (int j = 0; j < playbean.length; j++) {
						// 取得宠物id
						// petBean = (PetUserBean) userMap.get(new Integer(
						// playbean[j].getPetid()));
						petBean = load(0, playbean[j].getPetid());

						if (j == 0) {
							temp = (long) (per * 5);
							tempNotic = "您的宠物" + petBean.getName()
									+ "为你赢得了一场赌博投注赛冠军,奖金" + temp + "乐币.";
							// 加钱
							UserInfoUtil.updateUserStatus(
									"game_point=game_point+" + temp, "user_id="
											+ petBean.getUser_id(), petBean
											.getUser_id(),
									UserCashAction.OTHERS, "玩宠物赌博胜利加" + temp
											+ "乐币");
							// 今日参赛的宠物更新记录
							server.updateMatchUser(
									"wintime = wintime + 1 , totalstake = totalstake + "
											+ temp, "pet_user = "
											+ petBean.getId());
							// 清空参赛宠物当日比赛缓存
							OsCacheUtil.flushGroup(
									OsCacheUtil.PET_CACHE_GROUP_MATCH,
									"SELECT * from pet_match_user WHERE pet_user = "
											+ petBean.getId() + " limit 1");
							// 更新比赛bean中的第一名奖金
							matchrunbean.setWin1(temp);
							// 冠军的赔率
							matchrunbean.setWinrate(getStake(petBean.getId()));
							// /////////////////////////////////////////////////////////////
							// 获得乐币最多的三人
							UserBean tempUser;
							Vector userList = server.getStakeList("pet_id = "
									+ petBean.getId()
									+ " order by stake DESC limit 5");
							StringBuilder winnerString = new StringBuilder("");
							MatchStakeBean petStake;
							for (int i = 0; i < userList.size(); i++) {
								petStake = (MatchStakeBean) userList.get(i);
								tempUser = UserInfoUtil.getUser(petStake
										.getUser_id());
								winnerString.append((i + 1) + ",");

								// winnerString.append("<a
								// href=\"<%=(
								// + \"/user/ViewUserInfo.do?userId=");
								// winnerString.append(petStake.getUser_id());
								// winnerString.append("\")%>\">");
								winnerString.append(StringUtil.toWml(tempUser
										.getNickName())
										+ "<br/>");
								// winnerString.append("</a><br/>");
								winnerString
										.append("下注"
												+ petStake.getStake()
												+ "乐币,奖金"
												+ (int) (petStake.getStake() * matchrunbean
														.getWinrate())
												+ "乐币<br/>");
								// <a
								// href="<%=(
								// +
								// "/user/ViewUserInfo.do?userId="+777)%>">风华<br/></a><br/>
							}
							matchrunbean.setWinner(winnerString.toString());
							// //////////////////////////////////////////////////////////////////////////////////
						} else if (j == 1) {
							temp = (long) (per * 3);
							tempNotic = "您的宠物" + petBean.getName()
									+ "为你赢得了一场赌博投注赛亚军,奖金" + temp + "乐币.";
							// 加钱
							UserInfoUtil.updateUserStatus(
									"game_point=game_point+" + temp, "user_id="
											+ petBean.getUser_id(), petBean
											.getUser_id(),
									UserCashAction.OTHERS, "玩宠物赌博胜利加" + temp
											+ "乐币");
							// 今日参赛的宠物更新记录
							server.updateMatchUser("totalstake = totalstake + "
									+ temp, "pet_user = " + petBean.getId());
							// 清空参赛宠物当日比赛缓存
							OsCacheUtil.flushGroup(
									OsCacheUtil.PET_CACHE_GROUP_MATCH,
									"SELECT * from pet_match_user WHERE pet_user = "
											+ petBean.getId() + " limit 1");
							// 更新比赛bean中的第二名奖金
							matchrunbean.setWin2(temp);
						} else if (j == 2) {
							temp = (long) (per * 2);
							tempNotic = "您的宠物" + petBean.getName()
									+ "为你赢得了一场赌博投注赛季军,奖金" + temp + "乐币.";
							// 加钱
							UserInfoUtil.updateUserStatus(
									"game_point=game_point+" + temp, "user_id="
											+ petBean.getUser_id(), petBean
											.getUser_id(),
									UserCashAction.OTHERS, "玩宠物赌博胜利加" + temp
											+ "乐币");
							// 今日参赛的宠物更新记录
							server.updateMatchUser("totalstake = totalstake + "
									+ temp, "pet_user = " + petBean.getId());
							// 清空参赛宠物当日比赛缓存
							OsCacheUtil.flushGroup(
									OsCacheUtil.PET_CACHE_GROUP_MATCH,
									"SELECT * from pet_match_user WHERE pet_user = "
											+ petBean.getId() + " limit 1");
							// 更新比赛bean中的第三名奖金
							matchrunbean.setWin3(temp);
						} else {
							tempNotic = "您的宠物" + petBean.getName()
									+ "没有进入本次赌博投注赛前三，没有奖金。";
						}

						if (noticMap.get(new Integer(petBean.getUser_id())) != null) {
							tempNotic = noticMap.get(new Integer(petBean
									.getUser_id()))
									+ tempNotic;
						}
						noticMap.put(new Integer(petBean.getUser_id()),
								tempNotic);
					}
					HashMap winMap = new HashMap();
					// 给胜利者准备信息,以及加乐币
					per = (float) tatalStake * 90 / 100 / winStake;
					Vector stakeList = server.getStakeList("1 = 1");
					MatchStakeBean petStake;

					for (int i = 0; i < stakeList.size(); i++) {
						petStake = (MatchStakeBean) stakeList.get(i);
						if (petStake.getPet_id() == playbean[0].getPetid()) {
							temp = (long) (per * petStake.getStake());
							// 加钱
							UserInfoUtil.updateUserStatus(
									"game_point=game_point+" + temp, "user_id="
											+ petStake.getUser_id(), petStake
											.getUser_id(),
									UserCashAction.OTHERS, "玩宠物赌博胜利加" + temp
											+ "乐币");
							tempNotic = "您下注" + petStake.getStake() + "乐币的"
									+ StringUtil.toWml(petStake.getPet_name())
									+ "赢得了本次赌博投注赛冠军，奖金" + temp + "乐币.";
							// 此玩家押对了宠物,记录它赢取的乐币
							winMap.put(new Integer(petStake.getUser_id()),
									new Long(temp));
						} else {
							tempNotic = "您下注" + petStake.getStake() + "乐币的"
									+ StringUtil.toWml(petStake.getPet_name())
									+ "输掉了本次赌博投注赛。";
						}

						if (noticMap.get(new Integer(petStake.getUser_id())) != null) {
							tempNotic = noticMap.get(new Integer(petStake
									.getUser_id()))
									+ tempNotic;
						}
						noticMap.put(new Integer(petStake.getUser_id()),
								tempNotic);
					}
					// 记录用户赢取的乐币信息
					matchrunbean.setWinMap(winMap);
					// 发信息
					Set set = noticMap.keySet(); // get set-view of keys
					Iterator itr = set.iterator();
					Integer noticInt;
					while (itr.hasNext()) {
						noticInt = (Integer) itr.next();
						tempNotic = (String) noticMap.get(noticInt);

						// 系统给用户发信
						MessageBean message = new MessageBean();
						message.setFromUserId(100);
						message.setToUserId(noticInt.intValue());
						message.setContent(tempNotic);
						message.setMark(0);
						getMessageService().addMessage(message);
					}

					// 清空所有缓存
					OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_MATCH);
//					删除前一局比赛的记录
					String sql = "delete from pet_match_stake";
					SqlUtil.executeUpdate(sql, 4);
				}
			} else {
				// 结束后
			}
		}
	}

	public static IMessageService getMessageService() {
		if (messageService == null) {
			messageService = ServiceFactory.createMessageService();
		}
		return messageService;
	}

}
