package jc.family;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jc.family.game.GameAction;
import jc.family.game.boat.BoatAction;
import jc.util.ImageUtil;
import jc.util.SimpleChatLog2;
import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.tong.TongAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.jcforum.ForumBean;
import net.joycool.wap.bean.tong.TongApplyBean;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.tong.TongUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.cache.util.TongCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.spec.buyfriends.ServiceTrend;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.IP;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

public class FamilyAction extends CustomAction {

	public static FamilyService service = new FamilyService();
	public static java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy-MM-dd");
	public static java.text.SimpleDateFormat sd2 = new java.text.SimpleDateFormat("yyyy-M-d/HH:mm");
	public static ICacheMap familyBaseCache = CacheManage.addCache(new LinkedCacheMap(500), "familyBase");
	public static ICacheMap familyUserCache = CacheManage.addCache(new LinkedCacheMap(1500), "familyUser");
	public static ICacheMap familyUserIdList = CacheManage.addCache(new LinkedCacheMap(500), "familyUserIdList");
	public static int maxWidth = 200;// 图片的宽
	public static int maxHeight = 60;// 图片的高
	public static int applyMoney = 10000000;// 邀请扣钱
	public static HashMap fmNotice = new HashMap(); // 家族通知记录,用来计算法通知的时间间隔。

	public static int createMoney = 100000000;// 创建家族餐厅需要花费的钱

	public FamilyAction() {

	}

	public FamilyAction(HttpServletRequest request) {
		super(request);
	}

	public FamilyAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * 根据家族ID得到家族信息
	 * 
	 * @param uid
	 * @return
	 */
	public static FamilyHomeBean getFmByID(int id) {
		FamilyHomeBean fHome = null;
		Integer key = Integer.valueOf(id);
		synchronized (familyBaseCache) {
			fHome = (FamilyHomeBean) familyBaseCache.get(key);
			if (fHome == null) {
				fHome = service.selectFamily(id);
				if (fHome != null) {
					familyBaseCache.put(key, fHome);
				}
			}
		}
		return fHome;
	}

	/**
	 * 根据用户id得到用户信息
	 * 
	 * @param userid
	 * @return
	 */
	public static FamilyUserBean getFmUserByID(int userid) {
		FamilyUserBean fmuser = null;
		Integer key = Integer.valueOf(userid);
		synchronized (familyUserCache) {
			fmuser = (FamilyUserBean) familyUserCache.get(key);
			if (fmuser == null) {
				fmuser = service.selectfmUser(key.intValue());
				if (fmuser != null) {
					familyUserCache.put(key, fmuser);
				}
			}
		}
		return fmuser;
	}
	
	public static FamilyService getService() {
		return service;
	}

	/**
	 * 根据家族ID得到家族所有成员ID
	 * 
	 * @param uid
	 * @return
	 */
	public static List getFmUserIdList(int id) {
		List list = null;
		Integer key = Integer.valueOf(id);
		FamilyHomeBean fmHome = getFmByID(id);
		if (fmHome == null) {
			return null;
		}
		synchronized (familyUserIdList) {
			list = (ArrayList) familyUserIdList.get(key);
			if (list == null) {
				list = service.selectUserIdList(id, "");
				if (list != null) {
					familyUserIdList.put(key, list);
				}
			}
		}
		return list;
	}

	/**
	 * 添加新家族用户
	 * 
	 * @param uid
	 * @return
	 */
	public static void addUserId(int id, int userId) {
		List list = getFmUserIdList(id);
		if (list != null) {
			list.add(Integer.valueOf(userId));
		}
	}

	/**
	 * 删除家族用户
	 * 
	 * @param uid
	 * @return
	 */
	public static void removeUserId(int id, int userId) {
		List list = getFmUserIdList(id);
		if (list != null) {
			list.remove(Integer.valueOf(userId));
		}
	}

	/**
	 * 得到家族用户(不存在就返回null)
	 * 
	 * @return
	 */
	public FamilyUserBean getFmUser() {
		UserBean userbean = getLoginUser();
		if (userbean == null) {// 未登录
			return null;
		}
		return getFmUserByID(userbean.getId());
	}

	/**
	 * 得到家族用户家族ID,如果是帮派的话就返回-1
	 * 
	 * @return
	 */
	public int getFmId() {
		UserBean userbean = getLoginUser();
		if (userbean == null) {// 未登录
			return 0;
		}
		return getFmId(userbean.getId());
	}

	/**
	 * 得到家族用户家族ID,如果是帮派的话就返回-1
	 * 
	 * @return
	 */
	public static int getFmId(int userid) {
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(userid);
		if (status == null) {
			return 0;
		}
		if (status.getTong() == 0) {
			return 0;
		}
		if (status.getTong() > 19999) {
			return status.getTong();
		}
		return -1;
	}

	/**
	 * 获取家族在线成员id列表
	 * 
	 * @param tongId
	 * @return
	 */
	public List getOnlineFmUserIdList(int tongId) {
		String key = tongId + "_isOnline";
		// 从缓存中取
		List onlineUser = (List) OsCacheUtil.get(key, OsCacheUtil.TONG_USER_ONLINE_CACHE_GROUP,
				OsCacheUtil.TONG_USER_ONLINE_FLUSH_PERIOD);
		if (onlineUser == null) {
			String sql = "SELECT user_id FROM jc_online_user where tong_id=" + tongId;
			onlineUser = SqlUtil.getIntList(sql, net.joycool.wap.util.Constants.DBShortName);
			if (onlineUser == null) {
				onlineUser = new ArrayList();
			}
			// 放到缓存中
			OsCacheUtil.put(key, onlineUser, OsCacheUtil.TONG_USER_ONLINE_CACHE_GROUP);
		}
		return onlineUser;
	}

	/**
	 * 封禁--得到家族用户
	 * 
	 * @return
	 */
	public FamilyUserBean getUserBan() {
		int userid = getParameterInt("uid");
		int fmid = getParameterInt("id");
		if (userid == 0) {
			this.tip = "ID输入错误，请重新输入.";
			return null;
		}
		FamilyUserBean fmMUser = getFmUser();
		if (fmMUser == null) {
			return null;
		}
		if (!FamilyUserBean.isflag_game(fmMUser.getFm_flags())) {
			this.tip = "没有权限";
			return null;
		}
		FamilyUserBean fmUser = getFmUserByID(userid);
		String game = getParameterString("game");
		if (fmUser == null || fmUser.getFm_id() != fmid) {
			this.tip = "该用户非本家族成员,无法封禁.";
			return null;
		}
		fmUser = editUserState(game, fmUser, fmid);
		return fmUser;
	}

	/**
	 * 修改禁赛
	 * 
	 * @param userid
	 * @param fmid
	 * @return
	 */
	private FamilyUserBean editUserState(String game, FamilyUserBean fmUser, int fmid) {
		if (game != null && game.equals("ask")) {
			if (FamilyUserBean.isBlocked_ask(fmUser.getFm_state())) {
				fmUser.setFm_state(fmUser.getFm_state() - FamilyUserBean.BLOCKED_ASK);
			} else {
				fmUser.setFm_state(fmUser.getFm_state() + FamilyUserBean.BLOCKED_ASK);
			}
		}
		if (game != null && game.equals("boat")) {
			if (FamilyUserBean.isBlocked_boat(fmUser.getFm_state())) {
				fmUser.setFm_state(fmUser.getFm_state() - FamilyUserBean.BLOCKED_BOAT);
			} else {
				fmUser.setFm_state(fmUser.getFm_state() + FamilyUserBean.BLOCKED_BOAT);
			}
		}
		if (game != null && game.equals("snow")) {
			if (FamilyUserBean.isBlocked_snow(fmUser.getFm_state())) {
				fmUser.setFm_state(fmUser.getFm_state() - FamilyUserBean.BLOCKED_SNOW);
			} else {
				fmUser.setFm_state(fmUser.getFm_state() + FamilyUserBean.BLOCKED_SNOW);
			}
		}
		if (game != null && (game.equals("ask") || game.equals("boat") || game.equals("snow"))) {
			service.updateFmUser("fm_state = " + fmUser.getFm_state(),
					"id=" + fmUser.getId() + " and fm_id=" + fmUser.getFm_id());
		}
		return fmUser;
	}

	/**
	 * 禁赛:用户list
	 * 
	 * @param page
	 * @return
	 */
	public List getFmGameBanUserList() {
		int fmid = getParameterInt("id");
		if (fmid == 0) {
			return null;
		}
		FamilyUserBean fmMUser = getFmUser();
		if (fmMUser == null) {
			return null;
		}
		String game = request.getParameter("game");

		int userid = getParameterInt("userid");// 修改权限
		if (userid != 0) {
			if (!FamilyUserBean.isflag_game(fmMUser.getFm_flags())) {
				this.tip = "没有权限";
				return null;
			}
			FamilyUserBean fmUser = getFmUserByID(userid);
			if (fmUser == null || fmUser.getFm_id() != fmid) {
				this.tip = "该用户非本家族成员,无法封禁.";
				return null;
			}
			fmUser = editUserState(game, fmUser, fmid);
		}
		String query = "";
		if (game == null) {
			game = "boat";
		}
		if (game.equals("boat")) {
			query += " and fm_state&" + FamilyUserBean.BLOCKED_BOAT;
		}
		if (game.equals("ask")) {
			query += " and fm_state&" + FamilyUserBean.BLOCKED_ASK;
		}
		if (game.equals("snow")) {
			query += " and fm_state&" + FamilyUserBean.BLOCKED_SNOW;
		}
		int c = service.selectIntResult("select count(id) from fm_user where fm_id=" + fmid + query);
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		List list = service.selectFmUserList(fmid, paging.getStartIndex(), paging.getCountPerPage(), query);
		String s = paging.shuzifenye("game.jsp?id=" + fmid + "&#38;game=" + game, true, "|", response);
		setAttribute("pages", s);
		return list;
	}

	/**
	 * 创建家族
	 * 
	 * @return
	 */
	public int createFmHome(int applyId) {
		UserBean userbean = getLoginUser();
		if (userbean == null) {// 未登录
			return 0;
		}
		if (getFmId(userbean.getId()) != 0) {
			this.tip = "您已经加入家族/帮会";
			return 0;
		}
		FmApply fmApply = service.selectFmApplybyId(applyId);
		if (fmApply == null || fmApply.getIsok() == 1) {
			this.tip = "失败";
			return 0;
		}
		String fname = getParameterNoEnter("fname");
		if (fname == null) {
			fname = fmApply.getFm_name();
		}
		if (!checkFamilyName(fname)) {
			return 0;
		}
		if (!service.selectcheckedNumber(applyId)) {
			this.tip = "人数不够";
			return 0;
		}
		FamilyHomeBean fmHome = service.insertfmHome(applyId, fmApply.getId(), fmApply.getApply_name(), fname);// 创建数据库
		if (fmHome == null) {
			this.tip = "失败";
			return 0;
		}
		familyBaseCache.put(Integer.valueOf(fmHome.getId()), fmHome);

		service.insertMovement(fmHome.getCreate_id(), fmHome.getLeader_name(), fmHome.getId(), fmHome.getFm_name(),
				null, null, 0);
		service.insertHistory(fmHome.getLeader_name() + "创建本家族:" + fmHome.getFm_name(), fmHome.getId(), 1);

		List list = getFmUserIdList(fmHome.getId());

		for (int i = 0; i < list.size(); i++) {
			Integer userid = (Integer) list.get(i);

			familyUserCache.srm(userid);

			UserInfoUtil.updateUserTong(userid.intValue(), fmHome.getId());
			TongAction.getUserService().updateOnlineUser("tong_id=" + fmHome.getId(), "user_id=" + userid);// 修改在线用户消息

			if (userid.intValue() == fmHome.getLeader_id()) {
				ActionTrend.addTrend(userid.intValue(), BeanTrend.TYPE_FAMILY, "%1创建了家族%3",
						UserInfoUtil.getUser(userid.intValue()).getNickName(), fname,
						"/fm/myfamily.jsp?id=" + fmHome.getId());// 动态
			} else {
				ActionTrend.addTrend(userid.intValue(), BeanTrend.TYPE_FAMILY, "%1加入了家族%3",
						UserInfoUtil.getUser(userid.intValue()).getNickName(), fname,
						"/fm/myfamily.jsp?id=" + fmHome.getId());// 动态
			}
		}

		setAttribute("fhid", fmHome.getId());
		setAttribute("fhname", fname);
		return 2;
	}

	/**
	 * 检查家族名是否存在
	 * 
	 * @param fname
	 * @return
	 */
	public boolean checkFamilyName(String fname) {
		if (fname == null || fname.trim().equals("")) {
			this.tip = "家族名称最少一个字!";
			return false;
		}
		if (fname.length() > 6) {
			this.tip = "您输入的名称过长,请重新输入!";
			return false;
		}
		int count = service.selectIntResult("select id from fm_home where fm_name='" + StringUtil.toSql(fname.trim())
				+ "' limit 1");
		if (count > 0) {
			this.tip = "您的家族名称已经存在!";
			return false;
		}
		return true;
	}

	/**
	 * 创建家族申请
	 * 
	 * @return
	 */
	public boolean createFmHomeapply() {
		UserBean userbean = getLoginUser();
		if (userbean == null) {// 未登录
			return false;
		}
		if (isBuildFm() == 3) {// 再次验证条件
			this.tip = "";
			String game_point = (String) request.getAttribute("game_point");
			String rank = (String) request.getAttribute("rank");
			String social = (String) request.getAttribute("social");
			if (game_point != null) {
				this.tip += game_point + "<br/>";
			}
			if (rank != null) {
				this.tip += rank + "<br/>";
			}
			if (social != null) {
				this.tip += social + "<br/>";
			}
			return false;
		}
		if (getFmId() != 0) {
			this.tip = "您已经创建了一个家族,不能创建第二个!";
			return false;
		}
		String fname = getParameterNoEnter("fname");// 家族名
		if (!checkFamilyName(fname)) {
			return false;
		}
		FmApplyUser fmappUser = service.selectFamilyApplyUser(userbean.getId());
		if (fmappUser != null && fmappUser.getIs_apply() == 1) {
			this.tip = "您已经创建了一个家族,不能创建第二个!";
			return false;
		}
		int applyId = service.insertfamilyapply(userbean.getId(), fname);// 创建数据库
		if (applyId == 0) {
			this.tip = "您输入的名称中含有非法字符,请重新输入";
			return false;
		}
		UserInfoUtil.updateUserCash(userbean.getId(), -Constants.MIN_MONEY_FOR_FM, UserCashAction.OTHERS, "创建家族扣:"
				+ Constants.MIN_MONEY_FOR_FM);// 扣钱
		setAttribute("applyId", applyId);
		setAttribute("fhname", fname);
		return true;
	}

	/**
	 * 处理家族邀请
	 * 
	 * @return
	 */
	public boolean disposeFmHomeapply(UserBean userbean, FmApply fmApply, int userid, int gamePoint) {
		if (fmApply.getSend_total() > 9) {
			if (gamePoint >= applyMoney) {
				UserInfoUtil.updateUserCash(userbean.getId(), -applyMoney, UserCashAction.OTHERS, "发信息扣钱:"
						+ Constants.MIN_MONEY_FOR_FM);// 扣钱
			} else {
				return false;
			}
		}
		if (service.insertFamilyApplyUser(fmApply.getId(), userid)) {
			NoticeAction.sendNotice(userid, userbean.getNickName() + "邀请您和他共同申请家族:" + fmApply.getFm_name(),
					NoticeBean.GENERAL_NOTICE, "/fm/buildfail.jsp?applyId=" + fmApply.getId());
			return true;
		}
		return false;
	}

	/**
	 * 创建家族
	 * 
	 * @return 2创建成功 3条件不够
	 */
	public int isBuildFm() {
		UserBean userbean = getLoginUser();
		if (userbean == null) {// 未登录
			return 3;
		}
		// 资格不够
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(userbean.getId());
		if (status == null) {
			return 3;
		}
		boolean result = true;
		if (status.getGamePoint() < Constants.MIN_MONEY_FOR_FM) {
			setAttribute("game_point", "您的资金不够");
			result = false;
		}
		if (status.getRank() <= Constants.MIN_RANK_FOR_FM) {
			setAttribute("rank", "您的等级不够");
			result = false;
		}
		if (status.getSocial() < Constants.MIN_SOCIAL_FOR_FM) {
			setAttribute("social", "您的社交值不够");
			result = false;
		}
		if (result) {
			return 2;
		}
		return 3;
	}
	
	// 更新家族基金
	public static boolean updateFund(FamilyHomeBean fm, long add, int type) {
		synchronized(fm) {
			long left = fm.getMoney() + add;
			if(left < 0)
				return false;
			fm.setMoney(left);
			service.executeUpdate("update fm_home set money=" + left + " where id=" + fm.getId());
			service.insertFmFundDetail(fm.getId(), add, type, fm.getMoney());
			
		}
		return true;
	}

	/**
	 * 易帜,改名
	 * 
	 * @param id
	 * @return
	 */
	public int setFmName(int id) {
		UserBean userbean = getLoginUser();
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagLeader() || fmUser.getFm_id() != id) {// 没有权限
			this.tip = "没有权限";
			return 0;
		}
		String name = getParameterNoEnter("fname");
		if (!checkFamilyName(name)) {
			this.tip += "(3秒后自动返回)";
			return 3;
		}

		FamilyHomeBean fHome = getFmByID(id);// 得到家族

		if (fHome.getMoney() < Constants.FM_RENAME) {
			this.tip = "修改失败,家族基金不足.(3秒后自动返回)";
			return 4;
		}
		boolean set = service.updateFmName(userbean.getId(), id, name, userbean.getNickName(), fHome.getFm_name());
		if (set) {

			ForumBean forum = null;// 论坛改名
			try {
				forum = ForumCacheUtil.getForumCache(fHome.getForumId());
			} catch (Exception e) {
			}
			if (forum != null) {
				ForumCacheUtil.updateForum("title='" + name + "家族的论坛'", "id=" + forum.getId(), forum.getId());
			}

			fHome.setFm_name(name);

			updateFund(fHome, -Constants.FM_RENAME, FundDetail.RENAME_TYPE);

			this.tip = "修改成功!(3秒后自动返回)";
		}
		return 5;
	}

	/**
	 * 设置公告
	 * 
	 * @param id
	 * @return
	 */
	public int setBulletin(int id) {
		String bulletin = getParameterString("bulletin");
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !FamilyUserBean.isbulletin(fmUser.getFm_flags()) || fmUser.getFm_id() != id) {
			this.tip = "您没有发公告的权限!";// 判断权限
			return 0;
		}
		if (bulletin == null || bulletin.equals("") || bulletin.trim().length() == 0) {
			this.tip = "公告内容不能为空!(3秒后自动返回)";
			return 1;
		} else if (bulletin.length() > 50) {
			this.tip = "修改失败,家族公告最多只能输入50个字.(3秒后自动返回)";
			return 2;
		}
		UserBean userbean = getLoginUser();
		boolean set = service.updateBulletin(id, bulletin, userbean.getNickName());
		if (set) {
			FamilyHomeBean fHome = getFmByID(id);// 得到家族
			fHome.setBulletin(bulletin);// 更新缓存
			this.tip = "修改成功,家族公告已变为:" + bulletin + "(3秒后自动返回)";
		} else {
			this.tip = "您输入的名称中含有非法字符,请重新输入.(3秒后自动返回)";
		}
		return 3;
	}

	/**
	 * 查看公告
	 * 
	 * @param fid
	 * @return
	 */
	public String selectBulletin(int fid) {
		FamilyHomeBean fHome = getFmByID(fid);// 得到家族
		if (fHome == null) {
			return "";
		}
		return fHome.getBulletin();
	}

	/**
	 * 得到今天发送的通知数
	 * 
	 * @param id
	 * @return
	 */
	public int getTodayNoticeNum() {
		int id = getParameterInt("id");
		if (id == 0) {
			return 0;
		}
		int c = service.selectIntResult("select count(id) from fm_notice where fm_id=" + id
				+ " and left(notice_time,10)=left(now(),10)");
		return c;
	}

	/**
	 * 增加通知
	 * 
	 * @param id
	 * @return
	 */
	public int insertNotice(int id) {

		UserBean userbean = getLoginUser();
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !FamilyUserBean.isflag_notice(fmUser.getFm_flags()) || fmUser.getFm_id() != id) {
			this.tip = "您没有发通知的权限!";// 判断权限
			return 0;
		}
		Long noticeTime = (Long) fmNotice.get(Integer.valueOf(id));
		if (noticeTime != null && noticeTime.longValue() > System.currentTimeMillis() - 10 * 60 * 1000) {
			this.tip = "您的家族在之前十分钟内发送过家族通知.请稍后再次发送(3秒后返回)";
			return 2;
		}
		String notice = getParameterString("notice");
		if (notice == null || notice.trim().equals("")) {
			this.tip = "通知不能为空!";
			return 3;
		}
		if (notice.length() > 50) {
			this.tip = "发送失败,最多只能输入50个字.(3秒后自动返回)";
			return 4;
		}

		String cmd = getParameterString("cmd").trim();
		FamilyHomeBean fHome = getFmByID(id);
		String items = request.getParameter("glist");
		if ((items == null || items.equals("")) && cmd.equals("g")) {
			this.tip = "请选择好发送人员.(3秒后自动返回)";
			return 7;
		}
		long noticemoney = 10000 * fHome.getFm_member_num();
		if (cmd.equals("on")) {
			noticemoney = 100000l;
		} else if (cmd.equals("g")) {
			noticemoney = 10000 * items.split(";").length;
		} else {
			noticemoney = 10000 * fHome.getFm_member_num();
		}
		if (fHome.getMoney() < noticemoney) {
			this.tip = "发送失败,家族基金不足.(3秒后自动返回)";
			return 5;
		}

		FamilyNoticeBean bean = new FamilyNoticeBean();
		bean.setFmid(id);
		bean.setUserid(userbean.getId());
		bean.setUsername(userbean.getNickName());

		bean.setContent(notice);
		bean.setNoticetime(new Date());
		boolean add = false;
		List numlist = getFmUserIdList(id);
		add = service.insertNotice(bean, noticemoney, id);
		int noticenu = 0;
		String notice1 = "家族:" + StringUtil.limitString(notice, 40);
		if (cmd.equals("on")) {
			for (int i = 0; i < numlist.size(); i++) {
				int userid = ((Integer) numlist.get(i)).intValue();
				if (OnlineUtil.isOnline(String.valueOf(userid))) {
					NoticeAction.sendNotice(userid, notice1, "", NoticeBean.GENERAL_NOTICE, "", "/fm/noticelist.jsp");// 发送给选择的用户,提示他们有通知
					noticenu++;
				}
			}
		} else if (cmd.equals("g")) {
			numlist = Arrays.asList(items.split(";"));
			for (int i = 0; i < numlist.size(); i++) {
				int userid = Integer.parseInt((String) numlist.get(i));
				FamilyUserBean fUser = getFmUserByID(userid);
				if (fUser != null && fUser.getFm_id() == id) {
					NoticeAction.sendNotice(userid, notice1, "", NoticeBean.GENERAL_NOTICE, "", "/fm/noticelist.jsp");// 发送给选择的用户,提示他们有通知
					noticenu++;
				}
			}
		} else {
			for (int i = 0; i < numlist.size(); i++) {
				int userid = ((Integer) numlist.get(i)).intValue();
				NoticeAction.sendNotice(userid, notice1, "", NoticeBean.GENERAL_NOTICE, "", "/fm/noticelist.jsp");// 发送给选择的用户,提示他们有通知
				noticenu++;
			}
		}
		if (add) {
			this.tip = "发送成功,共发送" + noticenu + "条.(3秒后自动返回)";
		}

		fmNotice.put(Integer.valueOf(id), Long.valueOf(System.currentTimeMillis()));

		fHome.setMoney(fHome.getMoney() - noticemoney);// 缓存

		service.insertFmFundDetail(id, -noticemoney, FundDetail.NOTICE_TYPE, fHome.getMoney());

		return 6;
	}

	/**
	 * 解散家族
	 * 
	 * @return
	 */
	public int dissolveFm(int id) {
		UserBean userbean = getLoginUser();
		// 判断是否有解散家族的权限,只有族长有权限
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagLeader() || fmUser.getFm_id() != id) {
			this.tip = "您没有解散家族的权限!";// 判断权限,是否解散自己的家族
			return 0;
		}
		// 判断银行密码输入正确与否;
		UserSettingBean userSetting = userbean.getUserSetting();
		String bankpass = getParameterString("bankpass");
		if (bankpass == null || !bankpass.equals(userSetting.getBankPw())) {
			this.tip = "解散家族失败,银行密码输入错误.(3秒后自动返回)";
			return 1;
		}
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(userbean.getId());

		if (status.getGamePoint() < Constants.MIN_MONEY_DISSOLVE) {
			this.tip = "解散家族失败,您的现金不足.(3秒后自动返回)";
			return 2;
		}

		List list = service.selectAllFmList(id);
		for (int i = 0; i < list.size(); i++) {
			FamilyUserBean fmUserBean = (FamilyUserBean) list.get(i);
			UserInfoUtil.updateUserTong(fmUserBean.getId(), 0);
			TongAction.getUserService().updateOnlineUser("tong_id=0", "user_id=" + fmUserBean.getId());// 修改在线用户消息
		}

		FamilyHomeBean fHome = getFmByID(id);
		boolean del = service.deleteFmByID(id, userbean.getId(), userbean.getNickName(), fHome.getFm_name());
		// 解散成功
		if (del) {
			familyUserCache.clear();
			familyBaseCache.srm(Integer.valueOf(id));
			UserInfoUtil.updateUserCash(userbean.getId(), -Constants.MIN_MONEY_DISSOLVE, UserCashAction.OTHERS,
					"解散家族扣:" + Constants.MIN_MONEY_DISSOLVE);// 扣钱

			this.tip = "您已将" + fHome.getFm_name() + "家族解散,扣除10亿乐币,30天内不能申请新的家族.";
		}
		return 3;
	}

	/**
	 * 提款
	 * 
	 * @return
	 */
	public int getFund(FamilyUserBean fmUser) {
		int fund = getParameterInt("getfund");
		UserBean userbean = getLoginUser();
		if (fmUser == null || !FamilyUserBean.isflag_money(fmUser.getFm_flags())) {
			this.tip = "您没有权限,无法提取(3秒后自动返回)";
			return 0;
		}
		if (fund < 1) {
			this.tip = "输入错误,请重新输入.(3秒后自动返回)";
			return 1;
		}
		if (fund < 10000) {
			this.tip = "提款至少提一万.(3秒后自动返回)";
			return 1;
		}
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(userbean.getId());
		this.tip = "您没有权限,无法提取(3秒后自动返回)";
		if (status == null) {
			return 0;
		}
		if (fund > 2100000000 - status.getGamePoint()) {
			this.tip = "您取款超过上限.(3秒后自动返回)";
			return 1;
		}
		FamilyFundBean bean = new FamilyFundBean();
		bean.setEvent("提款" + fund);
		bean.setFm_id(fmUser.getFm_id());
		bean.setUserid(userbean.getId());
		bean.setUsername(userbean.getNickName());
		bean.setFm_State(0);
		FamilyHomeBean familyHomeBean = getFmByID(fmUser.getFm_id());
		if (familyHomeBean == null) {
			this.tip = "输入错误,请重新输入.(3秒后自动返回)";
			return 2;
		}
		long money = familyHomeBean.getMoney();
		if (fund > money) {
			this.tip = "您输入的金额大于当前家族基金,无法提取(3秒后自动返回)";
			return 3;
		}
		boolean get = service.updateFundUse(fmUser.getFm_id(), userbean.getId(), fund, bean);
		if (!get) {
			return 4;
		}
		if (get) {
			this.tip = "您已成功提取家族基金" + fund + ",请用来为家族做贡献哟～";
			familyHomeBean.setMoney(money - fund);
			// 用户账户加入相应资金
			UserInfoUtil.updateUserCash(userbean.getId(), fund, UserCashAction.OTHERS, "从家族提款:" + fund);
			fmUser.setFm_money_used(fmUser.getFm_money_used() + fund);

			service.insertFmFundDetail(fmUser.getFm_id(), -fund, FundDetail.USER_TYPE, familyHomeBean.getMoney());
		}
		return 5;
	}

	/**
	 * 修改族员的职位
	 * 
	 * @return
	 */
	public boolean updateUserPosition(int fmId) {
		int userId = getParameterInt("uid");
		// 判断是否有修改族员职位的权限
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagAppoint() || fmUser.getFm_id() != fmId) {
			this.tip = "您没有这个权限!";
			return false;
		}
		FamilyUserBean user = getFmUserByID(userId);
		if (user == null || user.getFm_id() != fmUser.getFm_id()) {
			this.tip = "该成员不是本家族的成员";
			return false;
		}
		if(user.isflagLeader()) {
			this.tip = "族长的称号无法修改";
			return false;
		}
		if(user.isflagAppoint() && userId != fmUser.getId() && !fmUser.isflagLeader()) {
			this.tip = "该成员的称号无法修改";
			return false;
		}
		String fmname = getParameterNoEnter("fmname");
		if (fmname == null || "".equals(fmname.trim())) {
			this.tip = "输入错误,称号只能输入0-4个字.(3秒后自动返回)";
			return false;
		}
		fmname = fmname.trim();
		if (fmname.length() > 4) {
			this.tip = "输入错误,称号只能输入0-4个字.(3秒后自动返回)";
			return false;
		}
		if ("族长".equals(fmname)) {
			this.tip = "称号不能改为族长!";
			return false;
		}
		this.tip = "修改成功!";
		user.setFm_name(fmname);
		
		service.insertFmUserLog(fmId, fmUser.getNickName() + "给予" + user.getNickName() + "称号:"+ fmname, 5);	// 给予称号
		
		return service
				.updateFmUser("fm_name='" + StringUtil.toSql(fmname) + "'", "fm_id=" + fmId + " and id=" + userId);
	}

	public static String[] flagNames = {"招募新人", "踢除成员", "家族公告", "家族通知", "论坛管理", "聊天管理", "游戏管理", "基金管理", null, "餐厅大厨", 
			"相册管理", "权限助理", "宣传部长", null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null,};	// 权限的名称
	/**
	 * 修改某个族员的权限
	 * 
	 * @return
	 */
	public boolean updateFmFlagsById(int fmid) {
		int p = getParameterIntS("flag");
		int userid = getParameterInt("uid");
		// 判断是否有修改的权限
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagAppoint() || fmUser.getFm_id() != fmid 
				|| p < 0 || p > 30 || p == 8 || p == 11 && !fmUser.isflagLeader()) {	// 不能任命族长(权限8)，只有族长能任命权限助理
			tip("tip", "您没有该任命权!");
			return false;
		}
		FamilyUserBean user = getFmUserByID(userid);
		if (user == null || user.getFm_id() != fmUser.getFm_id()) {
			tip("tip", "该成员不是本家族的成员");
			return false;
		}
		if (user.isflagAppoint() && !fmUser.isflagLeader() && userid != fmUser.getId()) {
			tip("tip", "无法修改该成员的权限!");
			return false;
		}
		
		if (p == 4) {// 论坛权限
			try {
				ForumBean forum = null;
				FamilyHomeBean fm = getFmByID(fmid);
				if (fm.getForumId() != 0) {
					forum = ForumCacheUtil.getForumCache(fm.getForumId());
					if (forum != null) {
						Set setuser = forum.getUserIdSet();
						if (setuser.contains(Integer.valueOf(user.getId()))
								&& FamilyUserBean.isflag_bbs(user.getFm_flags())) {
							setuser.remove(Integer.valueOf(user.getId()));
						} else {
							if (setuser.size() < 3) {
								setuser.add(Integer.valueOf(user.getId()));
							} else {
								tip("tip", "无法增加更多的论坛管理(最多3人)!");
								return false;
							}
						}
						ForumCacheUtil.updateForum("user_id='" + forum.setToString(setuser) + "'",
								"id=" + fm.getForumId(), fm.getForumId());
					}
				}
			} catch (Exception e) {
			}
		}
		
		user.toggleFlag(p);
		String log = null;
		String flagName = flagNames[p];
		if(flagName != null) {
			if(user.isFlag(p)) {
				log = fmUser.getNickName() + "赋予" + user.getNickName() + "权限:"+ flagName;
			} else {
				log = fmUser.getNickName() + "撤销" + user.getNickName() + "权限:"+ flagName;
			}
		}
		if(log != null)
			service.insertFmUserLog(fmid, log, 6);	// 给予称号
		return service.updateFmUser("fm_flags=(" + user.getFm_flags() + ")", "id=" + userid);
	}

	/**
	 * 罢免官员
	 * 
	 * @return
	 */
	public boolean recall(int fmId, int userId) {
		// 判断是否有修改的权限
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagAppoint() || fmUser.getFm_id() != fmId) {
			tip("","您没有这个权限!");
			return false;
		}
		FamilyUserBean user = getFmUserByID(userId);
		if (user == null || fmUser.getFm_id() != user.getFm_id()) {
			tip("","该成员不是本家族的成员");
			return false;
		}
		if(user.isflagLeader()) {
			tip("","族长无法被罢免");
			return false;
		}
		if(user.isflagAppoint() && !fmUser.isflagLeader()) {
			tip("","无法罢免该官员");
			return false;
		}
		user.setFm_name("");
		user.setFm_flags(0);
		if (FamilyUserBean.isflag_bbs(fmUser.getFm_flags())) {// 删除论坛权限
			FamilyHomeBean fm = getFmByID(fmId);
			if (fm.getForumId() != 0) {
				ForumBean forum = ForumCacheUtil.getForumCache(fm.getForumId());
				if (forum != null) {
					Set setuser = forum.getUserIdSet();
					setuser.remove(Integer.valueOf(userId));
					ForumCacheUtil.updateForum("user_id='" + forum.setToString(setuser) + "'", "id=" + fm.getForumId(),
							fm.getForumId());
				}
			}
		}
		service.insertFmUserLog(fmId, fmUser.getNickName() + "罢免了" + user.getNickName() + "的官职", 7);	// 给予称号
		return service.updateFmUser("fm_name='',fm_flags=0", "id=" + userId + " and fm_id=" + fmId);
	}

	/**
	 * 根据族员的id得到他的权限
	 * 
	 * @return
	 */
	public int getOneFlagsById() {
		int userId = getParameterInt("uid");
		if (userId == 0) {
			return 0;
		}
		FamilyUserBean fmUser1 = getFmUserByID(userId);
		if (fmUser1 == null) {
			return 0;
		}
		return fmUser1.getFm_flags();
	}

	/**
	 * 得到家族的名字
	 * 
	 * @return
	 */
	public String getFmName() {
		int fmId = getParameterInt("id");
		if (fmId == 0) {
			return "";
		}
		return getFmName(fmId);
	}

	/**
	 * 得到家族的名字
	 * 
	 * @return
	 */
	public String getFmName(int fmId) {
		FamilyHomeBean fmHome = getFmByID(fmId);
		if (fmHome == null) {
			return "";
		}
		return fmHome.getFm_name();
	}

	/**
	 * 家族让位
	 * 
	 * @return
	 */
	public int shaikhAbdicate(int fmid) {
		int id = getParameterInt("uid");
		if (fmid == 0 || id == 0) {
			return 0;// 参数出错
		}
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagLeader() || fmUser.getFm_id() != fmid) {
			return 2;// 权限不够

		}
		if (fmUser.getId() == id) {
			return 4;// 让位给自己

		}
		FamilyUserBean fmUser1 = getFmUserByID(id);
		if (fmUser1 == null || fmUser1.getFm_id() != fmid) {
			return 3;// 不是本家族人员
		}
		// 资格不够
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(fmUser1.getId());
		if (status == null || status.getRank() < Constants.MIN_RANK_FOR_FM
				|| status.getSocial() < Constants.MIN_SOCIAL_FOR_FM) {
			return 6;
		}
		int cd = service.selectIntResult("select count(id) from fm_shaikh where fm_id=" + fmid
				+ "  and date_add(invite_time,interval 1 hour)>now() and left(invite_time,10)=left(now(),10)");
		if (cd != 0) {
			return 1;// 转让CD
		}

		String title = getLoginUser().getUserName() + "想将该家族族长职位转让给您";
		NoticeAction.sendNotice(id, title, "", NoticeBean.GENERAL_NOTICE, "", "/fm/manage/sureabdicate.jsp?uid=" + id
				+ "&id=" + fmid);// ?uid="+id+"&amp;id="+fmid

		UserBean olduser = getLoginUser();
		UserBean newuser = UserInfoUtil.getUser(id);
		service.insertShaikhAbdicate(id, fmid, newuser.getNickName(), olduser.getNickName());// 将让位消息插入到让位表里
		return 5;
	}

	/**
	 * 
	 * 接受族长转让
	 * 
	 * @return
	 */
	public boolean receiveShaikhAbdicate() {
		int userid = getParameterInt("uid");
		int fmid = getParameterInt("id");
		boolean done = false;
		if (fmid == 0 || userid == 0) {
			this.tip = "参数出错";
			return done;
		}
		FamilyHomeBean familyHomeBean = getFmByID(fmid);
		UserBean user = getLoginUser();
		if (familyHomeBean == null || user == null) {
			this.tip = "该家族已解散";
			return done;
		}
		int leader = familyHomeBean.getLeader_id();
		FamilyUserBean fmUser = getFmUserByID(leader);
		UserBean olduser = UserInfoUtil.getUser(leader);
		done = service.updateshaikhAbdicate(userid, fmid, leader, user.getNickName(), olduser.getNickName());
		if (done) {

			if (FamilyUserBean.isflag_bbs(fmUser.getFm_flags())) {// 删除论坛权限
				FamilyHomeBean fm = getFmByID(fmid);
				if (fm.getForumId() != 0) {
					ForumBean forum = ForumCacheUtil.getForumCache(fm.getForumId());
					if (forum != null) {
						Set setuser = forum.getUserIdSet();
						setuser.remove(Integer.valueOf(leader));
						setuser.add(Integer.valueOf(userid));
						ForumCacheUtil.updateForum("user_id='" + forum.setToString(setuser) + "'",
								"id=" + fm.getForumId(), fm.getForumId());
					}
				}
			}

			familyBaseCache.srm(Integer.valueOf(fmid));
			familyUserCache.srm(Integer.valueOf(leader));
			familyUserCache.srm(Integer.valueOf(userid));
			this.tip = "您接受了转让申请,成为" + familyHomeBean.getFm_name() + "家族族长.";
			familyHomeBean.setLeader_id(userid);
			familyHomeBean.setLeader_name(user.getNickName());
		} else {
			this.tip = "该信息已经失效";
		}
		return done;
	}

	/**
	 * 拒绝族长转让
	 * 
	 * @return
	 */
	public boolean refuseShaikhAbdicate() {
		int userid = getParameterInt("uid");
		int fmid = getParameterInt("id");
		if (fmid == 0 || userid == 0) {
			this.tip = "参数出错";
			return false;
		}
		FamilyHomeBean familyHomeBean = getFmByID(fmid);
		if (familyHomeBean == null) {
			this.tip = "参数出错";
			return false;
		}
		int leader = familyHomeBean.getLeader_id();
		String title = getLoginUser().getUserName() + "拒绝了您的让位申请";// 发送拒绝通知
		NoticeAction.sendNotice(leader, title, "", NoticeBean.GENERAL_NOTICE, "", "/fm/myfamily.jsp?id=" + fmid);

		this.tip = "您拒绝了家族的转让";
		return true;
	}

	/**
	 * 捐款
	 * 
	 * @return
	 */
	public boolean setFund() {

		UserBean userbean = getLoginUser();
		FamilyUserBean fmUser = getFmUser();
		int fund = getParameterInt("contribute");
		if (fund < 1) {
			this.tip = "输入错误,请重新输入.(3秒后自动返回)";
			return false;
		}
		if (fund < 10000) {
			this.tip = "还不足1万,你也太小气了吧?";
			return false;
		}
		FamilyHomeBean familyHomeBean = getFmByID(fmUser.getFm_id());
		if (familyHomeBean == null) {
			this.tip = "家族不存在.(3秒后自动返回)";
			return false;
		}
		if (fmUser.getFm_id() == 0 || fmUser.getFm_id() != familyHomeBean.getId()) {
			this.tip = "不是该家族的人,无法捐款.(3秒后自动返回)";
			return false;
		}
		// 检查用户要捐的数目是否超出他的余额.错误提示:现金不足,无法捐献.(3秒后自动返回)
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(userbean.getId());
		if (status == null || status.getGamePoint() < fund) {
			this.tip = "现金不足,无法捐献.(3秒后自动返回)";
			return false;
		}
		boolean give = service.updateFundGive(fmUser.getFm_id(), userbean.getId(), fund);
		if (give) {
			UserInfoUtil.updateUserCash(userbean.getId(), -fund, UserCashAction.OTHERS, "向家族捐款:" + fund);// 扣钱
			familyHomeBean.setMoney(familyHomeBean.getMoney() + fund);
			this.tip = "已成功捐献" + fund + "乐币.(3秒后自动返回)";
			fmUser.setGift_fm(fmUser.getGift_fm() + fund);

			service.insertFmFundDetail(fmUser.getFm_id(), fund, FundDetail.GIFT_TYPE, familyHomeBean.getMoney());

		}
		return give;
	}

	/**
	 * 得到家族基金总数跟用户金币数
	 * 
	 * @param id
	 * @return
	 */
	public long getFmFund(int id) {
		FamilyUserBean userbean = getFmUser();
		if (userbean == null) {
			return 0;
		}
		FamilyHomeBean familyHomeBean = getFmByID(id);
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(userbean.getId());
		if (familyHomeBean == null || status == null) {
			setAttribute("gamePoint", 0);
			return 0;
		}
		setAttribute("gamePoint", status.getGamePoint());
		return familyHomeBean.getMoney();
	}

	/**
	 * 得到个人从家族取出的钱
	 * 
	 * @return
	 */
	public long getOneFundUse() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 0l;
		}
		return fmUser.getFm_money_used();
	}

	/**
	 * 修改家族基金总数
	 * 
	 * @param id
	 * @param fund
	 * @return
	 */
	public boolean updateFmFund(int id, int fund) {
		return service.updateFmFund(id, fund);
	}

	/**
	 * 得到家族信息
	 * 
	 * @param id
	 * @return
	 */
	public FamilyHomeBean getFamily() {
		int id = getParameterInt("id");
		if (id == 0) {
			return null;
		}
		return getFmByID(id);
	}

	/**
	 * 得到邀请用户
	 * 
	 * @return
	 */
	public UserBean inviteUser() {
		int userid = getParameterInt("userid");
		if (userid == 0) {
			this.tip = "ID输入错误,请重新输入(3秒后自动返回)";
			return null;
		}
		int tongId = getFmId(userid);
		if (tongId != 0) {
			this.tip = "该用户已加入家族/帮会,无法邀请.(3秒后自动返回)";
			return null;
		}
		UserBean userBean = UserInfoUtil.getUser(userid);
//		if (userBean == null || userBean.getCityno() < Constants.MIN_RANK_FOR_JOIN) {
//			this.tip = "该用户不足" + Constants.MIN_RANK_FOR_JOIN + "级,无法邀请.(3秒后自动返回)<br/>";
//			return null;
//		}
		return userBean;
	}

	/**
	 * 处理邀请加入
	 * 
	 * @return
	 */
	public int inviteUserRe(int fmid) {
		UserBean userbean = getLoginUser();
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !FamilyUserBean.isflag_new_member(fmUser.getFm_flags())) {
			this.tip = "您没有权限,无法邀请(3秒后自动返回)";
			return 0;
		}
		int id = getParameterInt("userid");
		if (id == 0 || fmid == 0) {
			this.tip = "ID输入错误,请重新输入(3秒后自动返回)";
			return 1;
		}
		if (getFmId(id) != 0) {
			this.tip = "该用户已加入家族,无法邀请.(3秒后自动返回)";
			return 2;
		}
		FamilyHomeBean family = getFmByID(fmid);
		if (family.getFm_member_num() >= family.getMaxMember()) {// 加入人数是否满
			this.tip = "家族已满.(3秒后自动返回)";
			return 1;
		}
		if (service.selectisexist(fmid, id, false)) {
			this.tip = "你已经邀请过本用户,无法再次邀请.(3秒后自动返回)";
			return 3;
		}
		UserBean userBean = UserInfoUtil.getUser(id);
//		if (userBean == null || userBean.getCityno() < Constants.MIN_RANK_FOR_JOIN) {
//			this.tip = "该用户不足" + Constants.MIN_RANK_FOR_JOIN + "级,无法邀请.(3秒后自动返回)<br/>";
//			return 4;
//		}
		if (service.insertInvite(userBean.getId(), userBean.getNickName(), fmid, family.getFm_name(),
				userbean.getNickName())) {
			NoticeAction.sendNotice(userBean.getId(), userbean.getNickName() + "邀请你加入家族", "",
					NoticeBean.GENERAL_NOTICE, "", "/fm/manage/invitedispose.jsp?fmid=" + family.getId());
			this.tip = "邀请已发送!(3秒后自动返回)";
			return 5;
		}
		return 6;
	}

	/**
	 * 得到邀请加入人
	 * 
	 * @return
	 */
	public FamilyNewManBean getfamilyOneInvite() {
		UserBean userbean = getLoginUser();
		int fmid = getParameterInt("fmid");
		if (fmid == 0) {
			this.tip = "您未被邀请";
			return null;
		}
		FamilyNewManBean familyNewManBean = service.selectfamilyOneInvite(userbean.getId(), fmid);
		if (familyNewManBean == null) {
			this.tip = "您未被邀请";
			return null;
		}
		return familyNewManBean;
	}

	/**
	 * 处理邀请加入信息
	 * 
	 * @return 0 未被邀请 1 加入成功 2拒绝加入
	 */
	public int getfamilyInviteResult() {
		int fmid = getParameterInt("fmid");
		String cmd = getParameterString("cmd");
		if (fmid == 0 || cmd == null) {
			this.tip = "您未被邀请";
			return 0;
		}
		if (!cmd.equals("y") && !cmd.equals("n")) {
			this.tip = "您未被邀请";
			return 0;
		}
		UserBean userbean = getLoginUser();
		FamilyHomeBean fHome = getFmByID(fmid);
		if (fHome == null) {
			this.tip = "您未被邀请";
			return 0;
		}
		if (getFmId() != 0) {
			this.tip = "您已经加入家族了";
			return 0;
		}
		FamilyNewManBean familyNewManBean = service.selectfamilyOneInvite(userbean.getId(), fmid);
		if (familyNewManBean == null) {
			this.tip = "加入失败,该家族已撤销对您的邀请.(3秒后自动返回)";
			return 0;
		}
		FamilyUserBean fmUser = getFmUser();
		if (fmUser != null && fmUser.getFm_id() != 0) {
			service.deleteFamilyInvite(familyNewManBean.getId(), fmid);
			this.tip = "你已加入家族";
			return 0;
		}
		if (fHome.getFm_member_num() >= fHome.getMaxMember()) {// 加入人数是否满
			this.tip = "家族已满.(3秒后自动返回)";
			return 0;
		}
		if (cmd.equals("y")) {
			// 加入家族.删除邀请记录
			if (service.insertFamilyInvite(familyNewManBean.getId(), fmid)) {
				FamilyHomeBean familyHomeBean = getFmByID(fmid);
				familyHomeBean.setFm_member_num(familyHomeBean.getFm_member_num() + 1);
				familyUserCache.srm(Integer.valueOf(userbean.getId()));

				UserInfoUtil.updateUserTong(userbean.getId(), fmid);
				TongAction.getUserService().updateOnlineUser("tong_id=" + fmid, "user_id=" + userbean.getId());// 修改在线用户消息

				this.tip = "恭喜您加入家族:" + StringUtil.toWml(familyHomeBean.getFm_name()) + "(3秒后自动返回)";

				service.insertFmUserLog(fmid, familyNewManBean.getUsername() + "由" + familyNewManBean.getInvitename()
						+ "邀请加入了本家族", 0);// XXXX由XXX邀请加入本家族
				addUserId(fmid, familyNewManBean.getUserid());

				ActionTrend.addTrend(userbean.getId(), BeanTrend.TYPE_FAMILY, "%1加入了家族%3", userbean.getNickName(),
						familyHomeBean.getFm_name(), "/fm/myfamily.jsp?id=" + familyHomeBean.getId());// 动态

				return 0;
			} else {
				this.tip = "您未被邀请";
				return 0;
			}
		}
		if (cmd.equals("n") && service.deleteFamilyInvite(familyNewManBean.getId(), fmid)) {
			this.tip = "您拒绝了" + StringUtil.toWml(fHome.getFm_name()) + "家族邀请";
			return 0;
		}
		return 0;
	}

	/**
	 * 处理申请加入加入 0:无权限 1:加入成功 2:拒绝加入 3:取消邀请4:加入失败5:家族满7:已经加入
	 * 
	 * @return
	 */
	public int newMan(int fmid) {
		int id = getParameterInt("id");
		if (fmid == 0 || id == 0) {
			return 0;
		}
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !FamilyUserBean.isflag_new_member(fmUser.getFm_flags())) {
			return 0;// 没有权限
		}
		String cmd = getParameterString("cmd");
		if ("y".equals(cmd)) {// 加入
			FamilyNewManBean bean = service.selectName(id, true);
			setAttribute("username", bean == null ? "" : bean.getUsername());
			if (bean == null) {
				return 0;// 没有权限
			}
			FamilyHomeBean fHome = getFmByID(fmid);
			if (fHome == null) {
				return 0;// 没有权限
			}
			if (getFmId(bean.getUserid()) != 0) {
				service.deleteFamilyJoin(id, fmid);
				return 7;// 已经加入了家族
			}
			if (fHome.getFm_member_num() >= fHome.getMaxMember()) {// 加入人数是否满
				return 5;
			}
			if (service.insertFamilyUser(id, fmid)) {

				fHome.setFm_member_num(fHome.getFm_member_num() + 1);// 更新缓存
				familyUserCache.srm(Integer.valueOf(bean.getUserid()));

				UserInfoUtil.updateUserTong(bean.getUserid(), bean.getFm_id());
				TongAction.getUserService().updateOnlineUser("tong_id=" + bean.getFm_id(),
						"user_id=" + bean.getUserid());// 修改在线用户消息

				NoticeAction.sendNotice(bean.getUserid(), "恭喜您加入" + bean.getFm_name() + "家族", "",
						NoticeBean.GENERAL_NOTICE, "", "/fm/myfamily.jsp?id=" + bean.getFm_id());

				service.insertFmUserLog(id, bean.getUsername() + "由" + fmUser.getNickName() + "审批加入本家族", 1);// XXXX由XXX审批加入本家族
				addUserId(fmid, bean.getUserid());

				ActionTrend.addTrend(bean.getUserid(), BeanTrend.TYPE_FAMILY, "%1加入了家族%3",
						UserInfoUtil.getUser(bean.getUserid()).getNickName(), bean.getFm_name(), "/fm/myfamily.jsp?id="
								+ bean.getFm_id());// 动态
				return 1;
			}
			return 4;
		}
		if ("n".equals(cmd)) {// 拒绝加入
			FamilyNewManBean bean = service.selectName(id, true);
			setAttribute("username", bean == null ? "" : bean.getUsername());
			if (bean == null) {
				return 0;// 没有权限
			}
			if (service.deleteFamilyJoin(id, fmid)) {
				NoticeAction.sendNotice(bean.getUserid(), "很遗憾" + bean.getFm_name() + "家族拒绝了您的加入申请", "",
						NoticeBean.GENERAL_NOTICE, "", "/fm/myfamily.jsp?id=" + bean.getFm_id());
				return 2;
			}
			return 0;
		}
		if ("c".equals(cmd)) {
			FamilyNewManBean bean = service.selectName(id, false);
			setAttribute("username", bean == null ? "" : bean.getUsername());
			if (bean == null) {
				return 6;// 不存在
			}
			if (service.deleteFamilyInvite(id, fmid)) {
				return 3;
			}
			return 0;
		}
		return 0;
	}

	/**
	 * 退出家族
	 * 
	 * @return
	 */
	public int exitFamily(int fmid) {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || fmUser.isflagLeader() || fmUser.getFm_id() != fmid) {
			return 2;// 是族长
		}
		if (fmUser.isCreateDate() != Constants.MIN_CREATE_DATE) {
			return 3;// 不足N天
		}
		if (service.updateexitFmaily(fmUser.getId(), fmid)) {

			if (FamilyUserBean.isflag_bbs(fmUser.getFm_flags())) {// 删除论坛权限
				FamilyHomeBean fm = getFmByID(fmid);
				if (fm.getForumId() != 0) {
					ForumBean forum = ForumCacheUtil.getForumCache(fm.getForumId());
					if (forum != null) {
						Set setuser = forum.getUserIdSet();
						setuser.remove(Integer.valueOf(fmUser.getId()));
						ForumCacheUtil.updateForum("user_id='" + forum.setToString(setuser) + "'",
								"id=" + fm.getForumId(), fm.getForumId());
					}
				}
			}

			String fmName = getFmName(fmid);
			setAttribute("fmName", fmName);
			familyUserCache.srm(Integer.valueOf(fmUser.getId()));

			UserInfoUtil.updateUserTong(fmUser.getId(), 0);
			TongAction.getUserService().updateOnlineUser("tong_id=0", "user_id=" + fmUser.getId());// 修改在线用户消息

			FamilyHomeBean fHome = getFmByID(fmid);
			fHome.setFm_member_num(fHome.getFm_member_num() - 1);// 更新缓存

			service.insertFmUserLog(fmid, fmUser.getNickName() + "离开本家族", 2);// XXXX离开本家族
			removeUserId(fmid, fmUser.getId());

			ActionTrend.addTrend(fmUser.getId(), BeanTrend.TYPE_FAMILY, "%1离开了家族%3", fmUser.getNickName(),
					fmUser.getFamilyName(), "/fm/myfamily.jsp?id=" + fmUser.getFm_id());// 动态

			return 1;
		}
		return 2;
	}

	/**
	 * 申请加入
	 * 
	 * @return
	 */
	public int applyJoin() {
		int id = getParameterInt("id");
		if (id == 0) {
			this.tip = "家族不存在";
			return 0;
		}
		UserBean userbean = getLoginUser();
		FamilyHomeBean family = getFmByID(id);
		if (family == null) {
			this.tip = "家族不存在";
			return 0;
		}
		if (getFmId() == -1) {
			this.tip = "您必须退出帮派才能加入家族";
			return 0;
		}
		if (getFmId() != 0) {
			this.tip = "您已经加入了一个家族,不能 加入第二个";
			return 0;
		}
		if (family.getFm_member_num() >= family.getMaxMember()) {// 加入人数是否满
			this.tip = "家族已满";
			return 0;
		}
		setAttribute("familname", family.getFm_name());
		// 资格不够
		UserStatusBean status = (UserStatusBean) UserInfoUtil.getUserStatus(userbean.getId());
		if (status == null || status.getRank() < Constants.MIN_RANK_FOR_JOIN) {
			this.tip = "申请加入家族失败!社区等级大于３级才可以加入家族!(3秒后跳转到" + family.getFm_name() + "首页)";
			return 0;
		}
		if (service.selectisexist(id, userbean.getId(), true)) {
			this.tip = "你已申请家族,不能重复申请.";
			return 0;
		}
		service.insertapplyoin(userbean.getId(), userbean.getNickName(), id, family.getFm_name());
		this.tip = "加入" + family.getFm_name() + "家族申请已经发送!请耐心等待!(3秒后跳转到" + family.getFm_name() + "家族首页)";
		return 0;
	}

	/**
	 * 开除
	 * 
	 * @return
	 */
	public int toFireOut(FamilyUserBean loginuser) {
		int fmid = loginuser.getFmId();
		int userid = getParameterInt("uid");
		if (loginuser == null || !loginuser.isflagRemoveMemberint()) {
			this.tip = "您没有该权限,无法开除该成员!";
			return 0;// 没有权限
		}
		FamilyUserBean fmUser = getFmUserByID(userid);
		if (fmUser == null || fmUser.getFm_id() != loginuser.getFm_id()) {
			this.tip = "该成员不是本家族的成员,不能开除.";
			return 1;
		}
		if (loginuser.getId() == userid) {
			this.tip = "小子,不想混了?";
			return 2;
		}
		FamilyHomeBean fHome = getFmByID(fmid);
		if (fHome == null) {
			this.tip = "不存在该家族";
			return 3;
		}
		if (fmUser.isflagAppoint()) {
			this.tip = "该成员拥有特殊权限,无法被直接开除!";
			return 4;
		}
		boolean fire = service.updatetoFireOut(fmid, userid);
		if (fire) {

			if (FamilyUserBean.isflag_bbs(fmUser.getFm_flags())) {// 删除论坛权限
				FamilyHomeBean fm = getFmByID(fmid);
				if (fm.getForumId() != 0) {
					ForumBean forum = ForumCacheUtil.getForumCache(fm.getForumId());
					if (forum != null) {
						Set setuser = forum.getUserIdSet();
						setuser.remove(Integer.valueOf(userid));
						ForumCacheUtil.updateForum("user_id='" + forum.setToString(setuser) + "'",
								"id=" + fm.getForumId(), fm.getForumId());
					}
				}
			}

			familyUserCache.srm(Integer.valueOf(fmUser.getId()));

			UserInfoUtil.updateUserTong(fmUser.getId(), 0);
			TongAction.getUserService().updateOnlineUser("tong_id=0", "user_id=" + fmUser.getId());// 修改在线用户消息

			fHome.setFm_member_num(fHome.getFm_member_num() - 1);// 更新缓存
			this.tip = "已将" + fmUser.getNickNameWml() + "开除出本家族.";

			removeUserId(fmid, fmUser.getId());
			service.insertFmUserLog(fmid, fmUser.getNickName() + "被" + loginuser.getNickName() + "踢出本家族", 3);// XXXX被XXX踢出本家族

			NoticeAction.sendNotice(fmUser.getId(), "你已经被开除出家族", "", NoticeBean.GENERAL_NOTICE, "", "/fm/index.jsp");
		}
		return 5;
	}

	/**
	 * 家族升级
	 * 
	 * @return
	 */
	public String updateLevel(int fmid) {
		FamilyHomeBean family = getFmByID(fmid);
		if (family == null) {
			return "家族不存在!";
		}
		if (family.getFm_level() >= Constants.FM_LEVEL.length - 1) {
			return "家族等级暂满";
		}
		// 判断是否有升级家族的权限,只有族长有权限
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagLeader() || fmUser.getFm_id() != fmid) {
			return "您没有升级家族的权限!";// 判断权限,是否解散自己的家族
		}
		String result = "";
		if (family.getMoney() < Constants.FM_LEVEL_MONEY[family.getFm_level()]) {
			result = "升级失败!你的家族基金不足!";
		}
		if (family.getGame_num() < Constants.FM_LEVEL_GAME[family.getFm_level()]) {
			result += "升级失败!你的家族游戏经验值还差" + (Constants.FM_LEVEL_GAME[family.getFm_level()] - family.getGame_num());
		}
		if (!result.equals("")) {
			return result;
		}
		FamilyFundBean bean = new FamilyFundBean();
		bean.setEvent("家族升级扣" + Constants.FM_LEVEL_MONEY[family.getFm_level()]);
		bean.setFm_id(fmid);
		bean.setFm_State(2);
		boolean bool = service.updateLevel(fmid, family.getFm_level() + 1,
				Constants.FM_LEVEL_MONEY[family.getFm_level()], bean);
		int count=family.getMaxMember()+Constants.FM_LEVEL[family.getFm_level()+1]-Constants.FM_LEVEL[family.getFm_level()];
		bool&=service.updateFmHome("max_member="+count, fmid);
		if (bool) {
			family.setMoney(family.getMoney() - Constants.FM_LEVEL_MONEY[family.getFm_level()]);
			family.setFm_level(family.getFm_level() + 1);
			family.setMaxMember(count);

			service.insertFmFundDetail(fmid, -Constants.FM_LEVEL_MONEY[family.getFm_level()], FundDetail.LEVEL_TYPE,
					family.getMoney());
		}
		return "恭喜您升级成功!";
	}

	/**
	 * 家族修改简介
	 * 
	 * @return
	 */
	public boolean updateFmInfo(int fmid, String info) {
		FamilyHomeBean family = getFmByID(fmid);
		if (family == null) {
			this.tip = "家族不存在!";
			return false;
		}
		// 判断是否有修改简介的权限,宣传部长有权利
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagPublic() || fmUser.getFm_id() != fmid) {
			this.tip = "您没有家族的权限!";// 判断权限
			return false;
		}
		if (info == null || info.length() > 200) {
			this.tip = "修改失败,您输入的字数已超过上限,请重新输入.";// 判断权限
			return false;
		}
		service.updateFmHome("info='" + StringUtil.toSql(info) + "'", fmid);
		family.setInfo(info);
		this.tip = "修改成功";
		return true;
	}

	/**
	 * 增加个人贡献
	 * 
	 * @param userid
	 * @param exploit
	 *            增加贡献点
	 * @param event
	 *            为什么增加贡献点
	 * @return
	 */
	public boolean addFmUserExploit(int userid, int exploit, String event) {
		familyUserCache.srm(Integer.valueOf(userid));
		return service.updateFmUser(userid, exploit, event);
	}

	/**
	 * 
	 * 增加家族贡献
	 * 
	 * @param fmid
	 * @param exploit
	 *            增加贡献点
	 * @param event
	 *            为什么增加贡献点
	 * @param event_type
	 *            增加类型，1:问答 2:龙舟3:雪仗4:排行榜
	 * @param mid
	 *            比赛场次
	 * @return
	 */
	public boolean addFmHomeExploit(int fmid, int exploit, String event, int event_type, int mid) {
		if (service.updateFmHome(fmid, exploit, event, event_type, mid)) {
			FamilyHomeBean family = getFmByID(fmid);
			if (family == null) {
				return false;
			}
			family.setFm_exploit(family.getFm_exploit() + exploit);
			return true;
		}
		return false;
	}

	/**
	 * 家族功勋历史
	 * 
	 * @param id
	 * @return
	 */
	public List getFmExploitList(int id, String game) {// TODO 改count 为 用户数
		// 放到页面上
		FamilyHomeBean family = FamilyAction.getFmByID(id);
		if (family == null) {
			this.tip = "家族不存在";
			return null;
		}
		int event_type = 1;
		if ("ask".equals(game)) {
			event_type = 1;
		}
		if ("boat".equals(game)) {
			event_type = 2;
		}
		if ("boat".equals(game)) {
			event_type = 3;
		}
		if ("top".equals(game)) {
			event_type = 4;
		}
		setAttribute("family", family);
		int c = service.selectIntResult("select count(id) from fm_home_exploit_history where fm_id=" + id
				+ " and event_type=" + event_type);
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		List list = service.selectExpHisList(id, event_type, paging.getStartIndex(), paging.getCountPerPage());
		String s = paging.shuzifenye("exploit.jsp?id=" + id + "&#38;game=" + game, true, "|", response);
		setAttribute("pages", s);
		return list;
	}

	/**
	 * 添加游戏使用基金记录
	 * 
	 * @param fmid
	 *            家族id
	 * @param event
	 *            事件
	 * @return
	 */
	public static boolean addFundHistory(int fmid, String event) {
		if (service.insertFundHistory(fmid, 0, "", event, 2)) {
			return true;
		}
		return false;
	}

	/**
	 * 上传
	 * 
	 * @param smUpload
	 * @return
	 */
	public boolean upPic(SmartUpload smUpload, FamilyHomeBean fmhome) {
		String fileName = "";
		String fileName2 = "";
		File upFile = null;
		try {
			// 得到上传的图片
			upFile = smUpload.getFiles().getFile(0);
			if ("".equals(upFile.getFileName())) {
				this.setAttribute("tip", "请选择一张图片.");
				return false;
			}
			fileName = System.currentTimeMillis() + "." + upFile.getFileExt();
			fileName2 = fileName;
			fileName = net.joycool.wap.util.Constants.RESOURCE_ROOT_PATH + Constants.FAMILY_IMG_PATH + fileName;
			// 存入服务器
			upFile.saveAs(fileName, SmartUpload.SAVE_PHYSICAL);
			if (!"".equals(fmhome.getLogoUrl())) {
				java.io.File file = new java.io.File(net.joycool.wap.util.Constants.RESOURCE_ROOT_PATH
						+ Constants.FAMILY_IMG_PATH + fmhome.getLogoUrl());
				if (file.exists() && fmhome.getLogoUrl().length() > 6 && !file.delete()) {
					LogUtil.logDebug(Constants.FAMILY_IMG_PATH + fmhome.getLogoUrl() + "文件删除失败");
				}
			}
		} catch (IOException e) {
			this.setAttribute("tip", "请选择一张图片.");
			return false;
		} catch (SmartUploadException e) {
			this.setAttribute("tip", "请选择一张图片.");
			return false;
		}
		try {
			// 调整图片
			ImageUtil.fitImage(fileName, maxWidth, maxHeight, fileName);
		} catch (IOException e) {
			this.setAttribute("tip", "请选择一张图片.");
			return false;
		}
		if (service.updateFamilyLogo(fmhome.getId(), "o.gif")) {
			SqlUtil.executeUpdate("insert into img_check set id2=" + fmhome.getId()
					+ ",type=8,create_time=now(),file='" + fileName2 + "',bak=''");
			fmhome.setLogoUrl("o.gif");
		}
		return true;
	}

	/**
	 * 审核图片后更新数据库,缓存
	 * 
	 * @param fmid
	 * @param loGoUrl
	 */
	public static void checkLogoImg(int fmid, String loGoUrl) {
		FamilyHomeBean fmhome = getFmByID(fmid);
		if (service.updateFamilyLogo(fmhome.getId(), loGoUrl)) {
			fmhome.setLogoUrl(loGoUrl);
		}
	}

	/**
	 * 转化帮派为家族
	 * 
	 * @param tongId
	 * @param loginUser
	 * @return
	 */
	public boolean changeTong(TongBean tong, UserBean loginUser, String name) {
		if (name == null || name.trim().equals("")) {
			request.setAttribute("tip", "家族名称最少一个字!");
			return false;
		}
		if (name.equals("荒城")) {
			tip = "您的家族名称已经存在!";
			request.setAttribute("tip", tip);
			return false;
		}
		if (tong.getUserCount() > 500) {
			tip = "转换失败,您的帮会人数超过500人不能转化成家族!";
			request.setAttribute("tip", tip);
			return false;
		}
		if (tong.getUserCount() < 5) {
			tip = "操作失败,帮会人数至少为5人才可转为家族!";
			request.setAttribute("tip", tip);
			return false;
		}
		if (name.length() > 6) {
			tip = "您输入的名称过长,请重新输入";
			request.setAttribute("tip", tip);
			return false;
		}
		int count = jc.family.FamilyAction.service.selectIntResult("select id from fm_home where fm_name='"
				+ StringUtil.toSql(name) + "' limit 1");
		if (count > 0) {
			tip = "您的家族名称已经存在!";
			request.setAttribute("tip", tip);
			return false;
		}
		ForumBean forum = null;
		try {
			forum = ForumCacheUtil.getForumCacheBean(tong.getId());
		} catch (Exception e) {
		}
		int level = 0;
		if (tong.getUserCount() > 80) {
			level = 4;
		} else if (tong.getUserCount() < 81 && tong.getUserCount() > 50) {
			level = 3;
		} else if (tong.getUserCount() < 51 && tong.getUserCount() > 30) {
			level = 2;
		} else if (tong.getUserCount() < 31 && tong.getUserCount() > 4) {
			level = 1;
		}
		int fmid = jc.family.FamilyAction.service.insertchangTong(name,
				tong.getUserCount() > 100 ? 100 : tong.getUserCount(), level, tong.getUserId(),
				loginUser.getNickName(), tong.getFund(), forum == null ? 0 : forum.getId());// 创建家族

		if (forum != null) {
			ForumCacheUtil.updateForum("title='" + name + "家族的论坛'", "id=" + forum.getId(), forum.getId());
		}

		if (fmid == 0) {
			tip = "创建家族错误";
			request.setAttribute("tip", tip);
			return false;
		}

		jc.family.FamilyAction.service.insertFmFundDetail(fmid, tong.getFund(), FundDetail.TONG_TYPE, tong.getFund());

		Vector users = (Vector) TongAction.getTongService().getTongUserList("tong_id=" + tong.getId());
		List includeUser = null;
		int maxuser = 100;
		if (users.size() > maxuser) {
			includeUser = SqlUtil.getIntList("select a.user_id from jc_tong_user a,user_status b where a.tong_id="
					+ tong.getId() + " and a.user_id=b.user_id order by b.last_login_time desc limit " + maxuser, 0);
		}
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				TongUserBean user = (TongUserBean) users.get(i);
				if (user != null) {
					UserBean userBean = (UserBean) UserInfoUtil.getUser(user.getUserId());
					if (userBean != null) {
						if (includeUser == null || includeUser.contains(new Integer(userBean.getId()))) {
							UserInfoUtil.updateUserTong(user.getUserId(), fmid);
							TongAction.getUserService().updateOnlineUser("tong_id=" + fmid,
									"user_id=" + user.getUserId());// 修改在线用户消息
							if (user.getUserId() != loginUser.getId()) {
								jc.family.FamilyAction.service.insertuserchange(user.getUserId(), fmid);// 创建用户
							}
						} else { // 退出帮会不加入家族
							UserInfoUtil.updateUserTong(user.getUserId(), 0);
						}
					}
				}
			}
		}
		TongCacheUtil.updateTong("title='荒城',user_count=0,cadre_count=0,user_id=-1,user_id_a=-1,user_id_b=-1,rate=0",
				"id=" + tong.getId(), tong.getId());// 城变荒城
		TongCacheUtil.deleteTongUserAll("tong_id=" + tong.getId());

		ForumCacheUtil.updateForum("tong_id=" + fmid, "tong_id=" + tong.getId(), tong.getId());

		/**
		 * 删除加入请求
		 */
		Vector tongApplyList = TongAction.getTongService().getTongApplyList("tong_id=" + tong.getId() + " and mark=0 ");
		if (tongApplyList != null) {
			TongApplyBean tongapply = null;
			UserStatusBean status = null;
			for (int i = 0; i < tongApplyList.size(); i++) {
				tongapply = (TongApplyBean) tongApplyList.get(i);
				if (tongapply != null) {
					status = (UserStatusBean) UserInfoUtil.getUserStatus(tongapply.getUserId());
					if (status != null) {
						UserInfoUtil.updateUserTong(tongapply.getUserId(), 0);
					}
				}
			}
			TongAction.getTongService().delTongApply("tong_id=" + tong.getId() + " and mark=0 ");
		}
		int c = jc.family.FamilyAction.service.selectIntResult("select count(1) from fm_user where fm_id=" + fmid);
		jc.family.FamilyAction.service.updateFmHome("ft=1,fm_member_num=" + c, fmid);

		jc.family.FamilyAction.service.insertFamilyMovement(loginUser.getId(), loginUser.getNickName(), fmid, name,
				"帮会" + StringUtil.toWml(tong.getTitle()) + "转为家族", "", 4);

		request.setAttribute("fmid", Integer.valueOf(fmid));
		return true;
	}

	public static ServiceTrend trendService = ServiceTrend.getInstance();// 家族动态

	public static List getTrendList(int id) {// 得到家族动态
		return trendService.getFamilyTrendList(id);
	}

	/**
	 * 创建论坛
	 * 
	 * @param fmid
	 * @param name
	 * @param userid
	 * @return
	 */
	public int creatForum(int fmid, String name, int userid) {
		ForumBean forum = new ForumBean();
		forum.setUserId(Integer.toString(userid));
		forum.setMark(1);
		forum.setTitle(name + "家族的论坛");
		forum.setDescription("欢迎大家来做客!");
		forum.setTongId(fmid);
		if (!TongAction.getForumService().addForum(forum)) {
			return 0;
		}
		OsCacheUtil.flushGroup(OsCacheUtil.FORUM_CACHE_GROUP, "TongForum");
		FamilyHomeBean fmhome = getFmByID(fmid);
		fmhome.setForumId(forum.getId());
		service.updateFmHome("forumId=" + forum.getId(), fmid);
		familyBaseCache.rm(Integer.valueOf(fmid));
		return forum.getId();
	}

	/**
	 * 添加uv
	 * 
	 * @param fmid
	 * @param name
	 * @param userid
	 */
	public void uvStatistics(int fmid) {
		FamilyHomeBean fmHome = getFmByID(fmid);
		UserBean userBean = getLoginUser();
		if (userBean == null) {// 未登录
			long ip = getIP();
			boolean result = fmHome.ipVisitSet.add(Long.valueOf(ip));
			if (result) {
				fmHome.setUv(fmHome.getUv() + 1);
				service.updateFmHome("uv=uv+1", fmid);
			}
		} else {
			FamilyUserBean fmUser = getFmUserByID(userBean.getId());
			if (fmUser != null && fmUser.getFmId() == fmid) {// 登录本家族
				boolean result = fmHome.userVisitSet.add(Integer.valueOf(userBean.getId()));
				if (result) {
					fmHome.setUvSelf(fmHome.getUvSelf() + 1);
					service.updateFmHome("uv_self=uv_self+1", fmid);
					// 有效登陆，增加活跃度和修改最后登陆时间
					fmUser.setAlive(fmUser.getAlive() + 1);
					fmUser.setVisitTime(new Date());
					service.updateFmUser("alive=" + fmUser.getAlive() + ",visit_time=now()", "id=" + fmUser.getId());
				}
			} else {
				boolean result = fmHome.userVisitSet.add(Integer.valueOf(userBean.getId()));
				if (result) {
					fmHome.setUv(fmHome.getUv() + 1);
					service.updateFmHome("uv=uv+1", fmid);
				}
			}
		}
	}

	/**
	 * 得到正确的ip
	 * 
	 * @return
	 */
	public long getIP() {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return IP.ipToLong(ip);
	}

	/**
	 * 处理UV
	 */
	public static void dealUv() {
		synchronized (familyBaseCache) {
			service.upd("update fm_home set uv_self_yes=uv_self,uv_yes=uv,uv=0,uv_self=0");
			List list = familyBaseCache.valueList();
			for (int i = 0; i < list.size(); i++) {
				FamilyHomeBean fmHome = (FamilyHomeBean) list.get(i);

				fmHome.setUvSelfYes(fmHome.getUvSelf());
				fmHome.setUvYes(fmHome.getUv());

				fmHome.setUvSelf(0);
				fmHome.setUv(0);

				fmHome.userVisitSet.clear();
				fmHome.ipVisitSet.clear();
			}
		}
	}

	/**
	 * 处理友链
	 * 
	 * @param fmId
	 * @param allyFmId
	 */
	public void dealAlly(int fmId, int allyFmId, int cmd) {
		if (fmId == allyFmId) {
			this.tip = "添加失败,您不能添加自己为友链";
			return;
		}
		FamilyHomeBean fmHome = getFmByID(fmId);
		FamilyHomeBean allyFmid = getFmByID(allyFmId);
		if (fmHome == null || allyFmid == null) {
			this.tip = "添加失败,家族不存在";
			return;
		}
		int exist = service.selectIntResult("select id from fm_ally where fm_id=" + fmId + " and ally_id=" + allyFmId);
		if (exist != -1) {// 存在
			if (cmd == 2) {
				fmHome.setAllyCount(fmHome.getAllyCount() - 1);
				allyFmid.setAllyCount2(allyFmid.getAllyCount2() - 1);
				service.upd("delete from fm_ally where id=" + exist);
				service.updateFmHome("ally_count=ally_count-1", fmId);
				service.updateFmHome("ally_count2=ally_count2-1", allyFmId);
				this.tip = "取消成功,您已经取消此家族友链";
				fmHome.setAllyList(null);	// 清空缓存
				return;
			}
			this.tip = "添加失败,您已经添加过此家族";
			return;
		}
		if (fmHome.getAllyCount() >= fmHome.getAllyLevel()) {
			this.tip = "添加失败,当前友联家族数量已达上限";
			return;
		}
		fmHome.setAllyCount(fmHome.getAllyCount() + 1);
		allyFmid.setAllyCount2(allyFmid.getAllyCount2() + 1);
		service.upd("insert into fm_ally(fm_id,ally_id)values(" + fmId + "," + allyFmId + ")");
		service.updateFmHome("ally_count=ally_count+1", fmId);
		service.updateFmHome("ally_count2=ally_count2+1", allyFmId);
		fmHome.setAllyList(null);	// 清空缓存
		if (fmHome.getAllyLevel() == fmHome.getAllyCount()) {
			this.tip = "添加成功,当前友联家族数量已达上限";
			return;
		}
		this.tip = "添加成功，您还可以再添加" + (fmHome.getAllyLevel() - fmHome.getAllyCount()) + "个";
	}

	// 家族聊天室
	public static HashSet newChatUser = new HashSet(); // 有新家族聊天信息的玩家userid

	public static boolean isNewChatUser(int userId) {
		return newChatUser.contains(new Integer(userId));
	}

	public static void removeNewChatUser(int userId) {
		synchronized (newChatUser) {
			newChatUser.remove(new Integer(userId));
		}
	}

	public void ban() {
		UserBean loginUser = getLoginUser();
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || !fmUser.isflagChat()) {
			return;
		}
		FamilyHomeBean family = getFmByID(fmUser.getFmId());

		ForbidUtil.ForbidGroup fgroup = ForbidUtil.getGroup("c" + family.getId());
		request.setAttribute("fgroup", fgroup);
		String op = request.getParameter("op");
		if (op != null) {
			int userId = getParameterInt("userId");
			if (userId <= 0) {
				tip("op", "用户id输入有误！");
				return;
			}
			UserBean user = UserInfoUtil.getUser(userId);
			if (user == null) {
				tip("op", "封禁用户不存在！");
				return;
			}
			
			if (op.equals("add")) {// 添加封禁列表
				if (fgroup.isForbid(user.getId())) {
					tip("op", "该用户已在封禁列表中！");
					return;
				}
				String bak = getParameterNoCtrl("bak");
				if(bak == null)
					bak = "";
				int interval = getParameterInt("per");
				boolean kick = hasParam("k");// 踢出聊天室
				if(interval < 5) interval = 5;
				if(interval>43200) interval = 43200;
				fgroup.addForbid(new ForbidUtil.ForbidBean(userId, fmUser.getId(), bak, kick ? 1 : 0), interval);
				AdminAction.addForbidLog(family.getFm_name() + family.getId(), userId, fmUser.getId(), bak, interval, 0);
				// 加一条聊天记录
				SimpleChatLog2 sc = SimpleChatLog2.getChatLog(fmUser.getFm_id(),"fm");
				if(bak.length() == 0)
					bak = "发言不当";
				if(kick) {
					sc.add(loginUser.getId(), loginUser.getNickName(), user.getNickName() + "由于[" + bak + "]被踢出聊天室");
					tip("op", "踢出操作成功！");
				} else {
					sc.add(loginUser.getId(), loginUser.getNickName(), user.getNickName() + "由于[" + bak + "]被禁言");
					tip("op", "禁言操作成功！");
				}

				
				return;
			} else if (op.equals("del")) {// 删除封禁列表
				if (!fgroup.isForbid(user.getId())) {
					tip("op", "该用户不在封禁列表中！");
					return;
				}
				fgroup.deleteForbid(user.getId());
				AdminAction.addForbidLog(family.getFm_name() + family.getId(), userId, fmUser.getId(), "", 0, 1);
				
				// 加一条聊天记录
				SimpleChatLog2 sc = SimpleChatLog2.getChatLog(fmUser.getFm_id(),"fm");
				sc.add(loginUser.getId(), loginUser.getNickName(), user.getNickName() + "的封禁被手动解除");
				
				tip("op", "解封操作成功！");
				return;
			}
		}
		tip("success", "ok");
	}

	
	public static HashMap fromDomainMap = new HashMap();	// domain -> id
	public static HashMap domainMap = new HashMap();	// id -> domain
	
	public static void loadDomain() {
		HashMap map = new HashMap();
		HashMap map2 = new HashMap();
		List list = SqlUtil.getObjectsList("select id,`domain` from fm_domain", 5);
		for(int i = 0;i < list.size();i++) {
			Object[] objs = (Object[])list.get(i);
			map.put(objs[1], objs[0]);
			map2.put(objs[0], objs[1]);
		}
		domainMap = map2;
		fromDomainMap = map;
	}
	public static int getFromDomain(String f) {
		Long iid = (Long)fromDomainMap.get(f);
		if(iid == null)
			return 0;

		return iid.intValue();
	}
	public static String getDomain(int id) {
		return (String)domainMap.get(new Long(id));
	}

	// 应用启动后调用
	public static void init() {
		loadDomain();
		GameAction.initGame();
	}
	// 应用关闭前调用
	public static void destroy() {
		try {
			GameAction.fmTimer.cancel();
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			BoatAction.boatTimer.cancel();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
