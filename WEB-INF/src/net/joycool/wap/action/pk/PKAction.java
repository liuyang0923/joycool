package net.joycool.wap.action.pk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.pk.PKActBean;
import net.joycool.wap.bean.pk.PKEquipBean;
import net.joycool.wap.bean.pk.PKMObjBean;
import net.joycool.wap.bean.pk.PKMedicineBean;
import net.joycool.wap.bean.pk.PKMissionBean;
import net.joycool.wap.bean.pk.PKMonsterBean;
import net.joycool.wap.bean.pk.PKMonsterSkillBean;
import net.joycool.wap.bean.pk.PKNpcBean;
import net.joycool.wap.bean.pk.PKObjTypeBean;
import net.joycool.wap.bean.pk.PKProtoBean;
import net.joycool.wap.bean.pk.PKUserBSkillBean;
import net.joycool.wap.bean.pk.PKUserBagBean;
import net.joycool.wap.bean.pk.PKUserBean;
import net.joycool.wap.bean.pk.PKUserHSkillBean;
import net.joycool.wap.bean.pk.PKUserSkillBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.impl.PKServiceImpl;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author macq
 * @datetime 2007-1-31 上午11:03:24
 * @explain PK系统
 */
public class PKAction extends CustomAction{
	// 默认场景人数
	public static int PKACT_USER_COUNT = 20;

	// PK系统复活所需乐币
	public static int PK_REVIVAL = 100000;

	public static String PK_IMAGE_PATH = "/img/pk";

	public static long PK_USER_INACTIVE = 5 * 1000 * 60;		// 用户无动作时间
	public static long MIN_ATTACK_INTERVAL = 5 * 1000;		// 最小攻击间隔
	public static long MONSTER_REVIVE_INTERVAL = 1 * 1000;	// 怪兽复活间隔
	public static long USER_REVIVE_INTERVAL = 5 * 1000 * 60;	// 用户复活间隔

	
	public int defaultSceneId = 1;		// 默认战斗场景

	public int logSize = 50;

	public static String cardTitle = "侠客秘境（公测）";

	int NUMBER_PER_PAGE = 5;

	public static PKServiceImpl pkService = new PKServiceImpl();

	private UserBean loginUser = null;
	PKUserBean pkUser = null;

	public final static String NPCNAME = "NPC";	// NPC临时名字

	public final static int PK_EQUIP = 0;			// 装备类型ID
	public final static int PK_MEDICINE = 1;		// 物品类型ID
	public final static int PK_USER_SKILL = 3;	// 主动技能类型ID
	public final static int PK_USER_BSKILL = 4;	// 被动技能类型ID
	public final static int PK_MOBJ = 5;		// 任务系统物品类型ID

	public static int PK_BODY_COUNT = 5;		// 身体部位数量

	public final static int BODY_HEAD = 1;	// 身体部位-头
	public final static int BODY_NECK = 2;	// 身体部位-颈
	public final static int BODY_MAIN = 3;	// 身体部位-身体
	public final static int BODY_HAND = 4;	// 身体部位-手
	public final static int BODY_FEET = 5;	// 身体部位-脚

	
	public static int PK_SKILL = 1;		// 主动技能标志
	public static int PK_BSKILL = 0;		// 被动技能标志

	// 主动技能等级数组
	public static int[] PK_SKILL_RANK = new int[] { 100, 200, 400, 800, 1600,
			3200, 6400, 12800, 25600, 51200 };

	// 被动技能等级数组
	public static int[] PK_EXPERIENCE_RANK = new int[] { 100, 200, 300, 400,
			500, 600, 700, 800, 900, 1000 };

	// 战斗经验值增加体力
	public static int[] PK_BSKILL_RANK = new int[] { 100, 200, 400, 800, 1600,
			3200, 6400, 12800, 25600, 51200 };

	// 耗费气力等级数组
	public static int[] PK_ENERGY_RANK = new int[] { 5, 10, 15, 20, 25, 30, 35,
			40, 45, 50 };

	// 身体部位-对应名称
	public static String[] BODY_NAME = { "头", "身", "手", "脚", "饰品" };

	public PKAction(HttpServletRequest request) {
		super(request);
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		if(loginUser != null) {
			loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
			
			check();
			// 设置用户动作时间
			pkUser.setActionTime(System.currentTimeMillis());
		}
	}

	private void check() {
		pkUser = loginUser.getPkUser();
//		 session中不存在pk用户信息(先查数据库的操作是因为一个用户可能拥有多个角色)
		if (pkUser == null) {
			// 获取pk系统中用户的信息
			pkUser = PKWorld.getPKUser(loginUser.getId());
			// 判断是否为新用户
			if (pkUser == null) {
				pkUser = new PKUserBean();
				// 设置用户id
				pkUser.setUserId(loginUser.getId());
				// liuyi 2007-02-06 角色初始值 start
				pkUser.setBasePhysical(200);
				pkUser.setCurrentPhysical(200);
				pkUser.setBaseEnergy(50);
				pkUser.setCurrentEnergy(50);
				pkUser.setBaseAggressivity(20);
				pkUser.setBaseRecovery(5);
				pkUser.setBaseFlying(10);
				pkUser.setBaseLuck(10);
				pkUser.setMissionStart("");
				pkUser.setMissionEnd("");
				// liuyi 2007-02-06 角色初始值 end
				// 添加用户
				getPKService().addPKUser(pkUser);
				// 获取pk系统中用户信息
				pkUser = getPKService().getPKUser(
						"user_id=" + loginUser.getId());
				// 设置用户所进入场景id
				pkUser.setSceneId(defaultSceneId);
				// 添加用户初始拥有技能
				PKUserHSkillBean hskill = new PKUserHSkillBean();
				hskill.setUserId(loginUser.getId());
				// 默认技能普通攻击
				hskill.setSkillId(13);
				// 技能类型属于普通技能
				hskill.setSkillType(1);
				// 技能默认等级1
				hskill.setRank(1);
				getPKService().addPKUserHSkill(hskill);
				// 获取插入记录所生成id
				// int indexId = SqlUtil.getLastInsertId("pk_user_hskill",
				// Constants.DBShortName);
				// hskill.setId(indexId);
				// 添加用户拥有技能到pkUser中
				pkUser.getUserSkillList().add(hskill);
				// 向session中添加用户pk信息
				loginUser.setPkUser(pkUser);
				// 判断pkusermap中是否有该用户
				if (PKWorld.getPKUserMap().get(new Integer(pkUser.getId())) == null) {
					// 添加角色到用户中
					PKWorld.getPKUserMap().put(new Integer(pkUser.getId()),
							pkUser);
				}
				// 所要进入的用户场景
				PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
						new Integer(defaultSceneId));
				// 添加用户到场景中
				if (pkAct != null) {
					pkAct.getPkUserList().add(pkUser);
				}
				// 计算用户属性（被动技能 +装备）
				pkUser.countPkUser();
				// 添加场景log
				pkAct.addLog(loginUser.getNickName() + "进入");
			}// 已注册用户
			else {// 判断场景内是否已包含该用户信息
				PKUserBean pkUserCache = (PKUserBean) PKWorld.getPKUserMap()
						.get(new Integer(pkUser.getId()));
				// 用户不在场景内进入默认场景
				if (pkUserCache == null) {
					// 设置初始pk用户所在场景
					pkUser.setSceneId(defaultSceneId);
					// 获取用户所有技能
					Vector hskillList = getPKService().getPKUserHSkillList(
							"user_id=" + loginUser.getId());
					// 添加用户拥有技能到pkUser中
					PKUserHSkillBean hskill = null;
					for (int i = 0; i < hskillList.size(); i++) {
						hskill = (PKUserHSkillBean) hskillList.get(i);
						pkUser.getUserSkillList().add(hskill);
					}
					// 向session中添加用户pk信息
					loginUser.setPkUser(pkUser);
					// 判断pkusermap中是否有该用户
					if (PKWorld.getPKUserMap().get(new Integer(pkUser.getId())) == null) {
						// 添加角色到用户中
						PKWorld.getPKUserMap().put(new Integer(pkUser.getId()),
								pkUser);
					}
					PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
							new Integer(defaultSceneId));
					// 添加用户到场景中
					if (pkAct != null) {
						pkAct.getPkUserList().add(pkUser);
					}
					// 添加场景log
					pkAct.addLog(loginUser.getNickName() + "进入");
					// 计算用户属性（被动技能 +装备）
					pkUser.countPkUser();
				}// 用户在场景内直接进入上一个场景
				else {
					pkUser = pkUserCache;
					// 向session中添加用户pk信息
					loginUser.setPkUser(pkUser);
				}
			}

		} else {
			// 判断用户是否下线
			if (pkUser.isOffline()) {
				// 判断用户map中是否包含pkuser信息
				if (PKWorld.getPKUserMap().get(new Integer(pkUser.getId())) != null) {
					// 更新pkUser当前session中的引用
					pkUser = (PKUserBean) PKWorld.getPKUserMap().get(
							new Integer(pkUser.getId()));
					// 向session中添加用户pk信息
					loginUser.setPkUser(pkUser);
				} else {
					// 添加角色到用户map中
					PKWorld.getPKUserMap().put(new Integer(pkUser.getId()),
							pkUser);
				}
				pkUser.setOffline(false);
			}
		}
	}

	public static PKServiceImpl getPKService() {
		return pkService;
	}

	/**
	 * 
	 * @author macq
	 * @explain : pk系统首页
	 * @datetime:2007-1-31 下午04:29:55
	 * @param request
	 */
	public void index(HttpServletRequest request) {
		// 默认场景位置
		int scene = pkUser.getSceneId();
		// 获取场景id
		String sceneId = request.getParameter("sceneId");
		// 判断场景id是否改变
		if (sceneId != null) {
			scene = StringUtil.toInt(sceneId);
			if (scene < 0) {
				scene = pkUser.getSceneId();
			}
			// 所要进入的用户场景
			PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
					new Integer(sceneId));
			if (pkAct == null) {
				doTip(null, "该场景不存在！请确认");
				return;
			}
			// 判断房间是否已满
			boolean flag = pkActUserFull(scene);
			if (flag) {
				doTip("isFull", "该场景已人满为患，请去其它场景！");
				return;
			}
			// 离开上一个场景
			this.move(scene);
			// 进入下一个场景
			scene = this.enter(scene, true);
		} else {
			// 判断房间是否已满
			boolean flag = pkActUserFull(scene);
			if (flag) {
				doTip("isFull", "该场景已人满为患，请去其它场景！");
				return;
			}
			// 进入原来场景
			scene = this.enter(scene, false);
		}
		// 所要进入的用户场景
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(scene));
		request.setAttribute("pkAct", pkAct);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取npc售卖物品类型
	 * @datetime:2007-3-20 18:37:41
	 * @return
	 * @return int
	 */
	public int getNpcType(PKNpcBean bean) {
		if (bean == null)
			return PK_EQUIP;
		String[] equip = bean.getEquip().split(",");
		if (equip.length > 2) {
			return PK_EQUIP;
		}
		String[] medicine = bean.getMedicine().split(",");
		if (medicine.length > 2) {
			return PK_MEDICINE;
		}
		String[] skill = bean.getSkill().split(",");
		if (skill.length > 2) {
			return PK_USER_SKILL;
		}
		String[] bSkill = bean.getBSkill().split(",");
		if (bSkill.length > 2) {
			return PK_USER_BSKILL;
		}
		return PK_EQUIP;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 判断房间已满
	 * @datetime:2007-3-15 13:55:27
	 * @param request
	 * @return void
	 */
	public boolean pkActUserFull(int sceneId) {
		// 村庄不限制人数
		if (sceneId == 1) {
			return false;
		}
		// 所要进入的用户场景
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct != null) {
			Set userList = pkAct.getPkUserList();
			// 如果房间人数大于房间默认人数代表人满
			if (userList.size() >= PKACT_USER_COUNT) {
				// 判断场景用户列表中是否包含该用户不包含不让进入
				if (!userList.contains(pkUser)) {
					return true;
				}
			}
			// 场景用户未满
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 切换场景
	 * @datetime:2007-1-31 下午05:53:50
	 */
	public void move(int sceneId) {
		// 从session中获取用户的pk信息
		PKUserBean pkUser = loginUser.getPkUser();
		// 判断用户session中是否存在pkuser信息并且场景id准备切换
		if (pkUser != null && pkUser.getSceneId() != sceneId) {
			// 获取上一个场景
			PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
					new Integer(pkUser.getSceneId()));
			// 删除上一个场景中的pkUser用户信息
			if (pkAct != null) {
				pkAct.getPkUserList().remove(pkUser);
				// 添加场景log
				pkAct.addLog(loginUser.getNickName() + "离开");
				// 设置用户在pk系统里的默认位置
				pkUser.setSceneId(-1);

			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain : 进入场景
	 * @datetime:2007-1-31 下午05:53:50
	 */
	public int enter(int sceneId, boolean flag) {
		int scene = pkUser.getSceneId();
		if (flag) {
			scene = sceneId;
		}
		// 获取一个场景
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(scene));
		if (flag) {
			// 如果用户不在上一个页面改变用户场景
			if (pkUser.getSceneId() != scene) {
				// 设置初始pk用户所在场景
				pkUser.setSceneId(scene);
				// 添加场景log
				pkAct.addLog(loginUser.getNickName() + "进入");
				pkAct.getPkUserList().add(pkUser);
			}

		} else {
			// 获取上一个场景id
			scene = pkUser.getSceneId();
		}
		// }
		return scene;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加场景动态log
	 * @datetime:2007-2-1 下午01:22:16
	 * @param sceneId
	 * @param content
	 * @return
	 */
	// public void addLog(int sceneId, String content) {
	// // 获取场景信息
	// PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
	// new Integer(sceneId));
	// if (pkAct != null) {
	// // 获取场景log
	// List logList = pkAct.getLog();
	// // 判断场景log是否大于50条
	// if (logList.size() > logSize) {
	// logList.remove(0);
	// }
	// // 添加场景log
	// logList.add(content);
	// }
	//
	// }
	/**
	 * 
	 * @author macq
	 * @explain : 获取所有场景列表
	 * @datetime:2007-1-31 下午04:41:21
	 * @param request
	 */
	public void sceneList(HttpServletRequest request) {
		// 获取所有场景信息
		int totalCount = getPKService().getPKSceneCount("1=1");
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "sceneList.jsp";
		int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = NUMBER_PER_PAGE;
		// int startIndex = Math.min(start, totalCount);
		// int endIndex = Math.min(start + end, totalCount);
		// List pkAct1 = pkAct.subList(startIndex, endIndex);
		Vector pkActList = getPKService().getPKActList(
				"1=1 order by id asc limit " + start + "," + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("pkActList", pkActList);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取pk系统用户行囊中物品明细
	 * @datetime:2007-3-2 16:20:20
	 * @param request
	 * @return void
	 */
	public void pkUserBag(HttpServletRequest request) {
		if (pkUser == null) {
			doTip(null, null);
			return;
		}
		if (pkUser.isDeath()) {
			doTip("isDeath", "你已经被打死");
			return;
		}
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： NPC出售收购物品页面
	 * @datetime:2007-4-20 17:15:52
	 * @param request
	 * @return void
	 */
	public void pkSale(HttpServletRequest request) {
		if (pkUser == null) {
			doTip(null, null);
			return;
		}
		if (pkUser.isDeath()) {
			doTip("isDeath", "你已经被打死");
			return;
		}
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： NPC出售收购物品结果页面
	 * @datetime:2007-4-20 17:16:29
	 * @param request
	 * @return void
	 */
	public void pkSaleResult(HttpServletRequest request) {
		if (pkUser == null) {
			doTip(null, null);
			return;
		}
		if (pkUser.isDeath()) {
			doTip("isDeath", "你已经被打死");
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			doTip(null, null);
			return;
		}
		// 获取用户行囊列表
		Vector userBagList = pkUser.getUserBagList();
		PKUserBagBean userBag = null;
		// 通过ID从内存中获取一条用户行囊记录
		for (int i = 0; i < userBagList.size(); i++) {
			PKUserBagBean userBag1 = (PKUserBagBean) userBagList.get(i);
			if (userBag1.getId() == id) {
				userBag = userBag1;
				break;
			}
		}
		if (userBag == null || userBag.getUserId() != loginUser.getId()
				|| userBag.getSiteId() != 0) {
			doTip(null, "您没有该物品");
			return;
		}
		String tip = null;
		int price = 0;
		switch (userBag.getType()) {
		// 装备
		case 0:
			PKEquipBean equip = (PKEquipBean) userBag.getPorto();
			price = equip.getPrice() / 4;
			// 删除记录
			getPKService().delPKUserBag("id=" + id);
			// 更新内存中行囊数据
			deleteCacheUserBagById(userBagList, id);
			tip = equip.getName() + "成功出售,价格为" + price + "乐币。";
			break;
		// 物品
		case 1:
			PKMedicineBean medicine = (PKMedicineBean) userBag.getPorto();
			price = medicine.getPrice() / 4;
			// 删除记录
			getPKService().delPKUserBag("id=" + id);
			// 更新内存中行囊数据
			deleteCacheUserBagById(userBagList, id);
			tip = medicine.getName() + "成功出售,价格为" + price + "乐币。";
			break;
		}
		if (price < 0) {
			price = 0;
		}
		// 更新用户乐币
		UserInfoUtil.updateUserStatus("game_point=game_point+" + price,
				"user_id=" + loginUser.getId(), loginUser.getId(),
				UserCashAction.OTHERS, "PK系统出售物品");
		// 计算用户属性（被动技能 +装备）
		pkUser.countPkUser();
		doTip("success", tip);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 查看用户死亡时间(判断自动复活还是手动复活)
	 * @datetime:2007-3-15 11:26:55
	 * @param request
	 * @return void
	 */
	public void userIsDeath(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 判断用户是否死亡
		if (!pkloginUser.isDeath()) {
			doTip(null, "您没有死亡");
			return;
		}
		// 判断用户复生方式
		boolean flag = pkloginUser.userRevival();
		request.setAttribute("flag", flag + "");
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： pk系统用户死亡复活
	 * @datetime:2007-3-15 11:05:00
	 * @param request
	 * @return void
	 */
	public void pkRevival(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 判断用户是否死亡
		if (!pkloginUser.isDeath()) {
			doTip(null, "您没有死亡");
			return;
		}
		// 判断用户复生方式
		boolean flag = pkloginUser.userRevival();
		if (flag) {
			// 获取用户状态
			UserStatusBean us = loginUser.getUs();
			if (us.getGamePoint() < PK_REVIVAL) {
				doTip(null, "您的乐币不够");
				return;
			}
			// 更新用户乐币
			UserInfoUtil.updateUserStatus(
					"game_point=game_point-" + PK_REVIVAL, "user_id="
							+ loginUser.getId(), loginUser.getId(),
					UserCashAction.OTHERS, "PK系统人物复活");
		}
		// 复活时恢复当前体力中间值的一半
		pkloginUser.setCurrentPhysical(pkloginUser.getPhysical() / 2);
		// 复活时恢复当前气力中间值的一半
		pkloginUser.setCurrentEnergy(pkloginUser.getEnergy() / 2);
		pkloginUser.setDeath(false);
		request.setAttribute("pkloginUser", pkloginUser);
		doTip("success", "复活成功");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 用户选择休息回复气力方法
	 * @datetime:2007-3-17 12:40:27
	 * @param request
	 * @return void
	 */
	public void userRest(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 判断用户是否死亡
		if (pkloginUser.isDeath()) {
			doTip(null, "您已经死亡");
			return;
		}
		// 判断是否连续攻击
		if (System.currentTimeMillis() - pkloginUser.getLastAttackTime() < PKAction.MIN_ATTACK_INTERVAL) {
			doTip(null, "不能连续休息.");
			return;
		}
		// 更新最后攻击时间
		pkloginUser.setLastAttackTime(System.currentTimeMillis());
		int updateEnergy = pkloginUser.getCurrentEnergy()
				+ (int) (pkloginUser.getEnergy() * 0.1);
		if (pkloginUser.getEnergy() < updateEnergy) {
			pkloginUser.setCurrentEnergy(pkloginUser.getEnergy());
		} else {
			pkloginUser.setCurrentEnergy(updateEnergy);
		}
		doTip("success", "休息完毕，回复10%气力！");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： cpn售卖页面
	 * @datetime:2007-3-5 15:29:27
	 * @param request
	 * @return void
	 */
	public void npcInfo(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int npcId = StringUtil.toInt(request.getParameter("npcId"));
		if (npcId <= 0) {
			doTip(null, null);
			return;
		}
		PKNpcBean pkNpc = (PKNpcBean) PKWorld.loadNpc().get(npcId + "");
		if (pkNpc == null) {
			doTip(null, "该" + NPCNAME + "不存在!");
			return;
		}
		if (!pkAct.getNpcList().contains(pkNpc)) {
			if (pkNpc == null) {
				doTip(null, "场景内不存在该" + NPCNAME + "!");
				return;
			}
		}
		// 获取展示物品类型
		int type = StringUtil.toInt(request.getParameter("type"));
		if (type < PK_EQUIP || type > PK_USER_BSKILL) {
			// 默认展示装备
			type = PK_EQUIP;
		}
		request.setAttribute("npcId", npcId + "");
		request.setAttribute("type", type + "");
		request.setAttribute("pkNpc", pkNpc);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：npc受理任务页面
	 * @datetime:2007-4-17 16:01:05
	 * @param request
	 * @return void
	 */
	public void npcMission(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int npcId = StringUtil.toInt(request.getParameter("npcId"));
		if (npcId <= 0) {
			doTip(null, null);
			return;
		}
		PKNpcBean pkNpc = (PKNpcBean) PKWorld.loadNpc().get(npcId + "");
		if (pkNpc == null) {
			doTip(null, "该" + NPCNAME + "不存在!");
			return;
		}
		if (!pkAct.getNpcList().contains(pkNpc)) {
			if (pkNpc == null) {
				doTip(null, "场景内不存在该" + NPCNAME + "!");
				return;
			}
		}
		List mAcceptList = new ArrayList();
		List mStartList = new ArrayList();
		// List mEndList = new ArrayList();
		// 获取npc对应出售任务id（拆分成数组）
		String[] mission = pkNpc.getMission().split(",");
		for (int i = 0; i < mission.length; i++) {
			// 得到一个任务id
			int missionId = StringUtil.toInt(mission[i]);
			if (missionId != -1) {
				// 获取任务属性记录
				PKMissionBean pkMission = getPKMission(missionId);
				if (pkMission != null) {
					// 获取用户现在已接受的任务
					Set mStartSet = pkloginUser.getMStartSet();
					// 获取用户现在已完成的任务
					// Set mEndSet = pkloginUser.getMEndSet();
					if (mStartSet.contains(new Integer(missionId))) {
						mStartList.add(pkMission);
						continue;
					}// else if(mEndSet.contains(new Integer(missionId))){
					// mEndList.add(pkMission);
					// continue;
					// }
					else {
						mAcceptList.add(pkMission);
						continue;
					}

				}
			}
		}
		request.setAttribute("mStartList", mStartList);
		// request.setAttribute("mEndList", mEndList);
		request.setAttribute("mAcceptList", mAcceptList);
		request.setAttribute("pkNpc", pkNpc);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 提交任务
	 * @datetime:2007-4-18 11:40:57
	 * @param request
	 * @return void
	 */
	public void cMission(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int npcId = StringUtil.toInt(request.getParameter("npcId"));
		if (npcId <= 0) {
			doTip(null, null);
			return;
		}
		PKNpcBean pkNpc = (PKNpcBean) PKWorld.loadNpc().get(npcId + "");
		if (pkNpc == null) {
			doTip(null, "该" + NPCNAME + "不存在!");
			return;
		}
		if (!pkAct.getNpcList().contains(pkNpc)) {
			if (pkNpc == null) {
				doTip(null, "场景内不存在该" + NPCNAME + "!");
				return;
			}
		}
		// 获取任务Id
		int mId = StringUtil.toInt(request.getParameter("mId"));
		if (mId <= 0) {
			doTip(null, null);
			return;
		}

		// 获取用户现在已接受的任务
		Set mStartSet = pkloginUser.getMStartSet();
		if (!mStartSet.contains(new Integer(mId))) {
			doTip(null, "您没有接受过该任务！");
			return;
		}
		// 获取任务属性记录
		PKMissionBean pkMission = getPKMission(mId);
		if (pkMission == null) {
			doTip(null, "该任务不存在");
			return;
		}
		request.setAttribute("pkNpc", pkNpc);
		request.setAttribute("pkMission", pkMission);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：接受任务
	 * @datetime:2007-4-18 11:41:08
	 * @param request
	 * @return void
	 */
	public void mAccept(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int npcId = StringUtil.toInt(request.getParameter("npcId"));
		if (npcId <= 0) {
			doTip(null, null);
			return;
		}
		PKNpcBean pkNpc = (PKNpcBean) PKWorld.loadNpc().get(npcId + "");
		if (pkNpc == null) {
			doTip(null, "该" + NPCNAME + "不存在!");
			return;
		}
		if (!pkAct.getNpcList().contains(pkNpc)) {
			if (pkNpc == null) {
				doTip(null, "场景内不存在该" + NPCNAME + "!");
				return;
			}
		}
		// 获取任务Id
		int mId = StringUtil.toInt(request.getParameter("mId"));
		if (mId <= 0) {
			doTip(null, null);
			return;
		}
		// 获取任务属性记录
		PKMissionBean pkMission = getPKMission(mId);
		if (pkMission == null) {
			doTip(null, "该任务不存在");
			return;
		}
		request.setAttribute("pkNpc", pkNpc);
		request.setAttribute("pkMission", pkMission);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 任务系统接受处理
	 * @datetime:2007-4-18 13:56:45
	 * @param request
	 * @return void
	 */
	public void mAcptResult(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int npcId = StringUtil.toInt(request.getParameter("npcId"));
		if (npcId <= 0) {
			doTip(null, null);
			return;
		}
		PKNpcBean pkNpc = (PKNpcBean) PKWorld.loadNpc().get(npcId + "");
		if (pkNpc == null) {
			doTip(null, "该" + NPCNAME + "不存在!");
			return;
		}
		if (!pkAct.getNpcList().contains(pkNpc)) {
			if (pkNpc == null) {
				doTip(null, "场景内不存在该" + NPCNAME + "!");
				return;
			}
		}
		// 获取任务Id
		int mId = StringUtil.toInt(request.getParameter("mId"));
		if (mId <= 0) {
			doTip(null, null);
			return;
		}
		// 获取任务属性记录
		PKMissionBean pkMission = getPKMission(mId);
		if (pkMission == null) {
			doTip(null, "该任务不存在");
			return;
		}
		// 获取用户现在已接受的任务
		Set mStartSet = pkloginUser.getMStartSet();
		if (mStartSet.contains(new Integer(mId))) {
			doTip(null, "您已经接受过该任务！");
			return;
		}
		mStartSet.add(new Integer(mId));
		String condition = mStartSet.toString();
		condition = condition.replace("[", "");
		condition = condition.replace("]", "");
		condition = condition.replace(" ", "");
		// 更新用户任务属性
		getPKService().updatePKUser("mission_start='" + condition + "'",
				"id=" + pkloginUser.getId());
		// 更改内存中用户任务属性
		mStartSet.add(new Integer(mId));
		request.setAttribute("pkMission", pkMission);
		doTip("success", "成功领取任务！");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：任务系统提交处理
	 * @datetime:2007-4-18 13:57:55
	 * @param request
	 * @return void
	 */
	public void mMsResult(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int npcId = StringUtil.toInt(request.getParameter("npcId"));
		if (npcId <= 0) {
			doTip(null, null);
			return;
		}
		PKNpcBean pkNpc = (PKNpcBean) PKWorld.loadNpc().get(npcId + "");
		if (pkNpc == null) {
			doTip(null, "该" + NPCNAME + "不存在!");
			return;
		}
		if (!pkAct.getNpcList().contains(pkNpc)) {
			if (pkNpc == null) {
				doTip(null, "场景内不存在该" + NPCNAME + "!");
				return;
			}
		}
		// 获取任务Id
		int mId = StringUtil.toInt(request.getParameter("mId"));
		if (mId <= 0) {
			doTip(null, null);
			return;
		}
		// 获取任务属性记录
		PKMissionBean pkMission = getPKMission(mId);
		if (pkMission == null) {
			doTip(null, "该任务不存在");
			return;
		}
		// 获取用户现在已接受的任务
		Set mStartSet = pkloginUser.getMStartSet();
		if (!mStartSet.contains(new Integer(mId))) {
			doTip(null, "您没有接受过该任务！");
			return;
		}
		List objList = pkMission.getObjList();
		boolean flag = checkObjCount(objList, pkloginUser.getUserBagList());
		if (!flag) {
			doTip(null, "您没有收集齐全该任务所需物品！");
			return;
		}
		// 满足提交任务条件，删除提交物品
		removeObjCount(objList, pkloginUser.getUserBagList());
		// 完成任务奖励物品列表
		List prizeList = pkMission.getPrizeList();
		// 给用户添加奖励物品
		this.pirzeObj(prizeList, pkloginUser.getUserBagList());

		// 删除用户现在进行的任务
		mStartSet.remove(new Integer(mId));
		// 更新语句
		String condition = mStartSet.toString();
		condition = condition.replace("[", "");
		condition = condition.replace("]", "");
		condition = condition.replace(" ", "");
		Set mEndSet = pkloginUser.getMEndSet();
		// 增加用户提交完成的任务
		mEndSet.add(new Integer(mId));
		String condition1 = mEndSet.toString();
		condition1 = condition1.replace("[", "");
		condition1 = condition1.replace("]", "");
		condition1 = condition1.replace(" ", "");
		// 更新用户任务属性
		getPKService().updatePKUser(
				"mission_start='" + condition + "',mission_end='" + condition1
						+ "'", "id=" + pkloginUser.getId());
		pkloginUser.setMissionStart(condition);
		pkloginUser.setMissionEnd(condition1);
		request.setAttribute("pkMission", pkMission);
		doTip("success", "成功提交任务！");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 检查是否满足提交条件
	 * @datetime:2007-4-18 17:37:21
	 * @param objList
	 * @param userBagList
	 * @return
	 * @return boolean
	 */
	public boolean checkObjCount(List objList, List userBagList) {
		Iterator it = objList.iterator();
		PKObjTypeBean objType = null;
		// 判断是否满足提交任务
		while (it.hasNext()) {
			objType = (PKObjTypeBean) it.next();
			int type = objType.getType();
			int id = objType.getId();
			int count = objType.getCount();
			PKUserBagBean pkUserBag = null;
			int checkCount = 0;
			for (int i = 0; i < userBagList.size(); i++) {
				pkUserBag = (PKUserBagBean) userBagList.get(i);
				if (pkUserBag == null || pkUserBag.getSiteId() != 0)
					continue;
				if (pkUserBag.getType() == type && pkUserBag.getEquipId() == id) {
					checkCount++;
				}
			}
			if (checkCount < count) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 满足提交任务条件，删除提交物品
	 * @datetime:2007-4-18 17:37:21
	 * @param objList
	 * @param userBagList
	 * @return
	 * @return boolean
	 */
	public void removeObjCount(List objList, List userBagList) {
		Iterator it = objList.iterator();
		PKObjTypeBean objType = null;
		while (it.hasNext()) {
			objType = (PKObjTypeBean) it.next();
			int type = objType.getType();
			int id = objType.getId();
			int count = objType.getCount();
			PKUserBagBean pkUserBag = null;
			int checkCount = userBagList.size();
			for (int i = 0; i < userBagList.size(); i++) {
				pkUserBag = (PKUserBagBean) userBagList.get(i);
				if (pkUserBag == null || pkUserBag.getSiteId() != 0)
					continue;
				if (pkUserBag.getType() == type && pkUserBag.getEquipId() == id) {
					// 删除行囊中物品
					getPKService().delPKUserBag("id=" + pkUserBag.getId());
					// 删除内存中数据
					userBagList.remove(pkUserBag);
					checkCount--;
					count--;
					i--;
				}
			}
			if (count <= 0) {
				break;
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 完成任务奖励用户物品
	 * @datetime:2007-4-18 17:44:51
	 * @param objList
	 * @param userBagList
	 * @return void
	 */
	public void pirzeObj(List prizeList, List userBagList) {
		// 获取奖励物品列表大小
		int size = prizeList.size();
		// 随机获取一个列表中的位置
		size = RandomUtil.nextInt(size);
		// 获取一条奖励记录
		PKObjTypeBean objType = (PKObjTypeBean) prizeList.get(size);
		int type = objType.getType();
		int id = objType.getId();
		// 添加用户行囊
		PKUserBagBean pkUserBag = new PKUserBagBean();
		pkUserBag.setUserId(loginUser.getId());
		pkUserBag.setEquipId(id);
		pkUserBag.setSiteId(0);
		pkUserBag.setEnduranceDegree(1);
		pkUserBag.setType(type);
		getPKService().addPKUserBag(pkUserBag);
		// // 获取插入记录所生成id
		// int indexId = SqlUtil.getLastInsertId("pk_user_bag",
		// Constants.DBShortName);
		// pkUserBag.setId(indexId);
		// 装备
		if (type == PK_EQUIP) {
			PKEquipBean equip = (PKEquipBean) getProto(type, id);
			if (equip != null) {
				pkUserBag.setPorto(equip);
			}
		}// 物品
		else if (type == PK_MEDICINE) {
			PKMedicineBean medicine = (PKMedicineBean) getProto(type, id);
			if (medicine != null) {
				pkUserBag.setPorto(medicine);
			}
		}
		userBagList.add(pkUserBag);
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户获取场景内掉落物品
	 * @datetime:2007-4-19 13:14:39
	 * @param prizeList
	 * @param userBagList
	 * @return void
	 */
	byte[] lock = new byte[0];

	public void takeObj(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}

		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int type = StringUtil.toInt(request.getParameter("type"));
		if (type < 0) {
			doTip(null, null);
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id <= 0) {
			doTip(null, null);
			return;
		}

		// 获取物品属性信息
		PKProtoBean proto = this.getProto(type, id);
		if (proto == null) {
			doTip(null, "该物品不存在!");
			return;
		}
		// 获取用户行囊
		List userBagList = pkloginUser.getUserBagList();
		// 判断用户行囊是否已满
		int userBagCount = 0;
		for (int i = 0; i < userBagList.size(); i++) {
			PKUserBagBean userBag = (PKUserBagBean) userBagList.get(i);
			if (userBag.getSiteId() == 0) {
				userBagCount++;
			}
		}
		if (pkloginUser.getBag() < userBagCount + 1) {
			doTip("null", "您的行囊已满！");
			return;
		}
		String key = type + "_" + id;
		// 同步获取场景里掉落物品map
		synchronized (lock) {
			HashMap dropMap = pkAct.getDropMap();
			// 删除场景内掉落物品
			if (dropMap.remove(key) == null) {
				doTip(null, "该物品不存在或该物品已消失!");
				return;
			}
		}
		String tip = null;
		switch (type) {
		// 装备
		case 0:
			PKEquipBean equip = (PKEquipBean) proto;
			// 增加物品到用户行囊中
			PKUserBagBean userBag = new PKUserBagBean();
			userBag.setUserId(loginUser.getId());
			userBag.setEquipId(equip.getId());
			userBag.setSiteId(0);
			userBag.setEnduranceDegree(1);
			userBag.setType(type);
			getPKService().addPKUserBag(userBag);
			// 更新内存中用户行囊
			userBag.setPorto((PKProtoBean) equip);
			// // 获取插入记录所生成id
			// int indexId = SqlUtil.getLastInsertId("pk_user_bag",
			// Constants.DBShortName);
			// userBag.setId(indexId);
			// 添加到用户行囊
			userBagList.add(userBag);
			tip = "您获得了一个" + equip.getName() + ",请去行囊中查收!";
			break;
		// 物品
		case 1:
			PKMedicineBean medicine = (PKMedicineBean) proto;
			// 增加物品到用户行囊中
			userBag = new PKUserBagBean();
			userBag.setUserId(loginUser.getId());
			userBag.setEquipId(medicine.getId());
			userBag.setSiteId(0);
			userBag.setEnduranceDegree(1);
			userBag.setType(type);
			getPKService().addPKUserBag(userBag);
			// 更新内存中用户行囊
			userBag.setPorto((PKProtoBean) medicine);
			// // 获取插入记录所生成id
			// indexId = SqlUtil.getLastInsertId("pk_user_bag",
			// Constants.DBShortName);
			// userBag.setId(indexId);
			// 添加到用户行囊
			userBagList.add(userBag);
			tip = "您获得了一个" + medicine.getName() + ",请去行囊中查收!";
			break;
		// 任务物品
		case 5:
			PKMObjBean mObj = (PKMObjBean) proto;
			// 增加物品到用户行囊中
			userBag = new PKUserBagBean();
			userBag.setUserId(loginUser.getId());
			userBag.setEquipId(mObj.getId());
			userBag.setSiteId(0);
			userBag.setEnduranceDegree(1);
			userBag.setType(type);
			getPKService().addPKUserBag(userBag);
			// 更新内存中用户行囊
			userBag.setPorto((PKProtoBean) mObj);
			// // 获取插入记录所生成id
			// indexId = SqlUtil.getLastInsertId("pk_user_bag",
			// Constants.DBShortName);
			// userBag.setId(indexId);
			// 添加到用户行囊
			userBagList.add(userBag);
			tip = "您获得了一个" + mObj.getName() + ",请去行囊中查收!";
			break;
		}

		doTip("success", tip);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：判断npc是否可以买指定类型商品
	 * @datetime:2007-3-16 9:45:15
	 * @param bean
	 * @param type
	 *            商品类型(装备，药品，主动技能，被动技能)
	 * @param productId
	 *            商品id
	 * @return
	 * @return boolean
	 */
	public boolean getNpcProduct(PKNpcBean bean, int type, int productId) {
		int baseId = 0;
		switch (type) {
		// 装备
		case PK_EQUIP:
			String[] equip = bean.getEquip().split(",");
			for (int i = 0; i < equip.length; i++) {
				baseId = StringUtil.toInt(equip[i]);
				if (baseId == productId) {
					return true;
				}
			}
			break;
		// 物品
		case PK_MEDICINE:
			String[] medicine = bean.getMedicine().split(",");
			for (int i = 0; i < medicine.length; i++) {
				baseId = StringUtil.toInt(medicine[i]);
				if (baseId == productId) {
					return true;
				}
			}
			break;
		// 主动技能
		case PK_USER_SKILL:
			String[] skill = bean.getSkill().split(",");
			for (int i = 0; i < skill.length; i++) {
				baseId = StringUtil.toInt(skill[i]);
				if (baseId == productId) {
					return true;
				}
			}
			break;
		// 被动技能
		case PK_USER_BSKILL:
			String[] bskill = bean.getBSkill().split(",");
			for (int i = 0; i < bskill.length; i++) {
				baseId = StringUtil.toInt(bskill[i]);
				if (baseId == productId) {
					return true;
				}
			}
			break;
		}
		return false;
	}

	/**
	 * 
	 * @author macq
	 * @explain： pk系统道具购买确认页面
	 * @datetime:2007-3-7 16:21:11
	 * @param request
	 * @return void
	 */
	public void buyAffirm(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int npcId = StringUtil.toInt(request.getParameter("npcId"));
		if (npcId <= 0) {
			doTip(null, null);
			return;
		}
		PKNpcBean pkNpc = (PKNpcBean) PKWorld.loadNpc().get(npcId + "");
		if (pkNpc == null) {
			doTip(null, "该" + NPCNAME + "不存在!");
			return;
		}
		if (!pkAct.getNpcList().contains(pkNpc)) {
			if (pkNpc == null) {
				doTip(null, "场景内不存在该" + NPCNAME + "!");
				return;
			}
		}
		// 获取展示物品类型(3种类型 0 装备 1 物品 2暗器)
		int type = StringUtil.toInt(request.getParameter("type"));
		if (type < PK_EQUIP || type > PK_USER_BSKILL) {
			// 默认展示装备
			type = PK_EQUIP;
		}
		// 获取展示物品id
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			// 默认展示装备
			id = 1;
		}
		// 判断npc是否出售该id商品
		boolean flag = getNpcProduct(pkNpc, type, id);
		if (!flag) {
			doTip(null, pkNpc.getName() + "不出售该商品请重新选择!");
			return;
		}
		PKProtoBean proto = null;
		if (type == PK_EQUIP) {
			proto = (PKEquipBean) getProto(type, id);
			if (proto == null) {
				doTip(null, null);
				return;
			}
		} else if (type == PK_MEDICINE) {
			proto = (PKMedicineBean) getProto(type, id);
			if (proto == null) {
				doTip(null, null);
				return;
			}
		} else if (type == PK_USER_SKILL) {
			proto = (PKUserSkillBean) getProto(type, id);
			if (proto == null) {
				doTip(null, null);
				return;
			}
		} else if (type == PK_USER_BSKILL) {
			proto = (PKUserBSkillBean) getProto(type, id);
			if (proto == null) {
				doTip(null, null);
				return;
			}
		}
		if (proto == null) {
			doTip(null, null);
			return;
		}
		request.setAttribute("proto", proto);
		request.setAttribute("npcId", npcId + "");
		request.setAttribute("type", type + "");
		session.setAttribute("buyAffirm", "true");
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：pk系统道具购买结果页面
	 * @datetime:2007-3-7 16:21:47
	 * @param request
	 * @return void
	 */
	public void buyResult(HttpServletRequest request) {
		// 防止刷新
		if (session.getAttribute("buyAffirm") == null) {
			doTip("refurbish", null);
			return;
		}
		session.removeAttribute("buyAffirm");
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景属性
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		// 获取npcId
		int npcId = StringUtil.toInt(request.getParameter("npcId"));
		if (npcId <= 0) {
			doTip(null, null);
			return;
		}
		PKNpcBean pkNpc = (PKNpcBean) PKWorld.loadNpc().get(npcId + "");
		if (pkNpc == null) {
			doTip(null, "该" + NPCNAME + "不存在!");
			return;
		}
		if (!pkAct.getNpcList().contains(pkNpc)) {
			if (pkNpc == null) {
				doTip(null, "场景内不存在该" + NPCNAME + "!");
				return;
			}
		}

		// 获取展示物品类型(3种类型 0 装备 1 物品 2暗器 3 主动技能 4 被动技能)
		int type = StringUtil.toInt(request.getParameter("type"));
		if (type < PK_EQUIP || type > PK_USER_BSKILL) {
			// 默认展示装备
			type = PK_EQUIP;
		}
		// 获取展示物品id
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			// 默认展示装备
			id = 1;
		}
		// 判断npc是否出售该id商品
		boolean mark = getNpcProduct(pkNpc, type, id);
		if (!mark) {
			doTip(null, pkNpc.getName() + "不出售该商品请重新选择!");
			return;
		}
		// 获取用户行囊列表
		List userBagList = pkloginUser.getUserBagList();
		// 判断用户行囊是否已满
		int userBagCount = 0;
		for (int i = 0; i < userBagList.size(); i++) {
			PKUserBagBean userBag = (PKUserBagBean) userBagList.get(i);
			if (userBag.getSiteId() == 0) {
				userBagCount++;
			}
		}
		//macq_增加一次性购买物品数量(限制药品)_start
		int totalCount = StringUtil.toInt(request.getParameter("count"));
		if(totalCount<=0){
			totalCount=1;
		}
		//macq_增加一次性购买物品数量(限制药品)_start
		if (pkloginUser.getBag() < userBagCount + totalCount && type <= 2) {
			doTip("error", "您的行囊已满！");
			request.setAttribute("npcId", npcId + "");
			request.setAttribute("type", type + "");
			return;
		}
		String tip = null;
		UserStatusBean us = loginUser.getUs();
		// 装备
		if (type == PK_EQUIP) {
			PKEquipBean equip = (PKEquipBean) getProto(type, id);
			if (equip == null) {
				doTip(null, null);
				return;
			}
			// 判断用户是否有钱购买物品
			if (equip.getPrice() > us.getGamePoint()) {
				doTip("error", "您的乐币不够请确认！");
				request.setAttribute("npcId", npcId + "");
				request.setAttribute("type", type + "");
				return;
			}
			// 更新用户乐币
			updateGamePoint(loginUser.getId(), equip.getPrice(), equip
					.getName());
			// 增加物品到用户行囊中
			PKUserBagBean userBag = new PKUserBagBean();
			userBag.setUserId(loginUser.getId());
			userBag.setEquipId(equip.getId());
			userBag.setSiteId(0);
			userBag.setEnduranceDegree(1);
			userBag.setType(type);
			getPKService().addPKUserBag(userBag);
			// 更新内存中用户行囊
			userBag.setPorto((PKProtoBean) equip);
			// // 获取插入记录所生成id
			// int indexId = SqlUtil.getLastInsertId("pk_user_bag",
			// Constants.DBShortName);
			// userBag.setId(indexId);
			// 添加到用户行囊
			userBagList.add(userBag);
			// 获取装备名称
			tip = equip.getName() + "购买成功，请去您的行囊选择装备";
		}// 物品
		else if (type == PK_MEDICINE) {
			PKMedicineBean medicine = (PKMedicineBean) getProto(type, id);
			if (medicine == null) {
				doTip(null, null);
				return;
			}
			int totalPrice = totalCount*medicine.getPrice();
			// 判断用户是否有钱购买物品
			if (totalPrice > us.getGamePoint()) {
				doTip("error", "您的乐币不够请确认！");
				request.setAttribute("npcId", npcId + "");
				request.setAttribute("type", type + "");
				return;
			}
			// 更新用户乐币
			updateGamePoint(loginUser.getId(), totalPrice, medicine
					.getName());
			// 增加物品到用户行囊中
			for(int i =0;i<totalCount;i++){
				PKUserBagBean userBag = new PKUserBagBean();
				userBag.setUserId(loginUser.getId());
				userBag.setEquipId(medicine.getId());
				userBag.setSiteId(0);
				userBag.setEnduranceDegree(1);
				userBag.setType(type);
				getPKService().addPKUserBag(userBag);
				// 更新内存中用户行囊
				userBag.setPorto((PKProtoBean) medicine);
				// // 获取插入记录所生成id
				// int indexId = SqlUtil.getLastInsertId("pk_user_bag",
				// Constants.DBShortName);
				// userBag.setId(indexId);
				// 添加到用户行囊
				userBagList.add(userBag);
			}
			// 获取装备名称
			tip = medicine.getName() + "购买成功，请去您的行囊选择使用";
		}// 主动技能
		else if (type == PK_USER_SKILL) {
			PKUserSkillBean pkUserSkill = (PKUserSkillBean) getProto(type, id);
			if (pkUserSkill == null) {
				doTip(null, null);
				return;
			}
			// 获取用户所拥有所有技能
			List userHskill = pkloginUser.getUserSkillList();
			boolean flag = false;
			int count = 0;
			PKUserHSkillBean pkUserHSkill = null;
			for (int i = 0; i < userHskill.size(); i++) {
				pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
				// 判断是否为主动技能
				if (pkUserHSkill.getSkillType() == PK_SKILL) {
					if (pkUserHSkill.getSkillId() != 13) {
						count++;
					}
					// 判断用户是否已经学过该项技能
					if (pkUserHSkill.getSkillId() == pkUserSkill.getId()) {
						flag = true;
					}
				}
			}
			// 判断用户拥有主动技能的数量是否大于4
			if (count >= 4) {
				doTip("error", "最多只能学习4种主动技能！");
				request.setAttribute("npcId", npcId + "");
				request.setAttribute("type", type + "");
				return;
			}
			// 判断用户是否有钱购买物品
			if (pkUserSkill.getPrice() > us.getGamePoint()) {
				doTip("error", "您的乐币不够请确认！");
				request.setAttribute("npcId", npcId + "");
				request.setAttribute("type", type + "");
				return;
			}
			// 更新用户乐币
			updateGamePoint(loginUser.getId(), pkUserSkill.getPrice(),
					pkUserSkill.getName());
			if (!flag) {
				pkUserHSkill = new PKUserHSkillBean();
				pkUserHSkill.setUserId(loginUser.getId());
				pkUserHSkill.setSkillId(pkUserSkill.getId());
				pkUserHSkill.setSkillType(PK_SKILL);
				pkUserHSkill.setRank(1);
				getPKService().addPKUserHSkill(pkUserHSkill);
				// // 获取插入记录所生成id
				// int indexId = SqlUtil.getLastInsertId("pk_user_hskill",
				// Constants.DBShortName);
				// pkUserHSkill.setId(indexId);
				// 添加用户拥有技能到pkUser中
				pkloginUser.getUserSkillList().add(pkUserHSkill);
			}
			// 获取装备名称
			tip = pkUserSkill.getName() + "已学会，可以在攻击敌人时使用了";
		}// 被动技能
		else if (type == PK_USER_BSKILL) {
			PKUserBSkillBean pkUserBSkill = (PKUserBSkillBean) getProto(type,
					id);
			if (pkUserBSkill == null) {
				doTip(null, null);
				return;
			}
			// 用户拥有技能列表
			List userHskill = pkloginUser.getUserSkillList();
			boolean flag = false;
			int count = 0;
			PKUserHSkillBean pkUserHSkill = null;
			for (int i = 0; i < userHskill.size(); i++) {
				pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
				// 判断是否为被动技能
				if (pkUserHSkill.getSkillType() == PK_BSKILL) {
					count++;
					// 判断用户是否已经学过该项技能
					if (pkUserHSkill.getSkillId() == pkUserBSkill.getId()) {
						flag = true;
					}
				}
			}
			// 判断用户拥有被动技能的数量是否大于3
			if (count >= 3) {
				doTip("error", "最多只能学习3种被动技能！");
				request.setAttribute("npcId", npcId + "");
				request.setAttribute("type", type + "");
				return;
			}
			// 判断用户是否有钱购买物品
			if (pkUserBSkill.getPrice() > us.getGamePoint()) {
				doTip("error", "您的乐币不够请确认！");
				request.setAttribute("npcId", npcId + "");
				request.setAttribute("type", type + "");
				return;
			}
			// 更新用户乐币
			updateGamePoint(loginUser.getId(), pkUserBSkill.getPrice(),
					pkUserBSkill.getName());
			if (!flag) {
				pkUserHSkill = new PKUserHSkillBean();
				pkUserHSkill.setUserId(loginUser.getId());
				pkUserHSkill.setSkillId(pkUserBSkill.getId());
				pkUserHSkill.setSkillType(PK_BSKILL);
				pkUserHSkill.setRank(1);
				getPKService().addPKUserHSkill(pkUserHSkill);
				// // 获取插入记录所生成id
				// int indexId = SqlUtil.getLastInsertId("pk_user_hskill",
				// Constants.DBShortName);
				// pkUserHSkill.setId(indexId);
				// 添加用户拥有技能到pkUser中
				pkloginUser.getUserSkillList().add(pkUserHSkill);
			}
			// 获取装备名称
			tip = pkUserBSkill.getName() + "已学会，请去我的内功中点击详情开始修炼";
		}
		request.setAttribute("npcId", npcId + "");
		request.setAttribute("type", type + "");
		doTip("success", tip);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新用户乐币
	 * @datetime:2007-3-7 17:19:49
	 * @param request
	 * @return void
	 */
	public boolean updateGamePoint(int userId, int point, String name) {
		boolean flag = UserInfoUtil.updateUserStatus("game_point=game_point-"
				+ point, "user_id=" + userId, userId, UserCashAction.OTHERS,
				"PK系统购买" + name + "花费" + point + "乐币");
		return flag;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户身上物品明细介绍
	 * @datetime:2007-3-8 15:44:39
	 * @param request
	 * @return void
	 */
	public void bodyDetail(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			doTip(null, null);
			return;
		}
		// 获取用户行囊列表
		Vector userBagList = pkloginUser.getUserBagList();
		PKUserBagBean userBag = null;
		// 通过ID从内存中获取一条用户行囊记录
		for (int i = 0; i < userBagList.size(); i++) {
			userBag = (PKUserBagBean) userBagList.get(i);
			if (userBag.getId() == id) {
				break;
			}
		}
		if (userBag == null || userBag.getUserId() != loginUser.getId()
				|| userBag.getSiteId() == 0) {
			doTip(null, null);
			return;
		}
		request.setAttribute("userBag", userBag);
		session.setAttribute("bodyDetail", "true");
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户行囊中物品明细介绍
	 * @datetime:2007-3-8 15:44:43
	 * @param request
	 * @return void
	 */
	public void detailInfo(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			doTip(null, null);
			return;
		}
		// 获取用户行囊列表
		Vector userBagList = pkloginUser.getUserBagList();
		PKUserBagBean userBag = null;
		// 通过ID从内存中获取一条用户行囊记录
		for (int i = 0; i < userBagList.size(); i++) {
			userBag = (PKUserBagBean) userBagList.get(i);
			if (userBag.getId() == id) {
				break;
			}
		}
		if (userBag == null || userBag.getUserId() != loginUser.getId()
				|| userBag.getSiteId() != 0) {
			doTip(null, null);
			return;
		}
		request.setAttribute("userBag", userBag);
		session.setAttribute("detailInfo", "true");
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 用户行囊更新装备或物品
	 * @datetime:2007-3-8 17:09:07
	 * @param request
	 * @return void
	 */
	public void detailResult(HttpServletRequest request) {
		// 防止刷新
		if (session.getAttribute("detailInfo") == null) {
			doTip("refurbish", null);
			return;
		}
		session.removeAttribute("detailInfo");
		String tip = null;
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		int flag = StringUtil.toInt(request.getParameter("flag"));
		if (flag < 0 || flag > 1) {
			doTip(null, null);
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			doTip(null, null);
			return;
		}
		// 获取用户行囊列表
		Vector userBagList = pkloginUser.getUserBagList();
		PKUserBagBean userBag = null;
		// 通过ID从内存中获取一条用户行囊记录
		for (int i = 0; i < userBagList.size(); i++) {
			PKUserBagBean userBag1 = (PKUserBagBean) userBagList.get(i);
			if (userBag1.getId() == id) {
				userBag = userBag1;
				break;
			}
		}
		if (userBag == null || userBag.getUserId() != loginUser.getId()
				|| userBag.getSiteId() != 0) {
			doTip(null, null);
			return;
		}
		// 丢弃
		if (flag == 1) {
			// 删除记录
			getPKService().delPKUserBag("id=" + id);
			// 更新内存中行囊数据
			deleteCacheUserBagById(userBagList, id);
			tip = "操作成功！";
		}// 使用
		else {
			switch (userBag.getType()) {
			// 装备
			case 0:
				PKEquipBean equiup = (PKEquipBean) userBag.getPorto();
				int site = equiup.getSite();
				int mark = checkCacheUserBagById(userBagList, site);
				// mark等于0就代表指定位置上有装备
				if (mark != 0) {
					// 更新记录
					getPKService().updatePKUserBag("site_id=0", "id=" + mark);
					// 更新内存中行囊数据
					for (int i = 0; i < userBagList.size(); i++) {
						userBag = (PKUserBagBean) userBagList.get(i);
						if (userBag.getId() == mark) {
							userBag.setSiteId(0);
							break;
						}
					}
					// 更新记录
					getPKService().updatePKUserBag("site_id=" + site,
							"id=" + id);
					// 更新内存中行囊数据
					for (int i = 0; i < userBagList.size(); i++) {
						userBag = (PKUserBagBean) userBagList.get(i);
						if (userBag.getId() == id) {
							userBag.setSiteId(equiup.getSite());
							break;
						}
					}
					tip = equiup.getName() + "更换成功！";
				}// 指定位置上没有装备
				else {
					// 更新记录
					getPKService().updatePKUserBag("site_id=" + site,
							"id=" + id);
					// 更新内存中行囊数据
					for (int i = 0; i < userBagList.size(); i++) {
						userBag = (PKUserBagBean) userBagList.get(i);
						if (userBag.getId() == id) {
							userBag.setSiteId(equiup.getSite());
							break;
						}
					}
					tip = equiup.getName() + "更换成功！";
				}
				break;
			// 物品
			case 1:
				PKMedicineBean medicine = (PKMedicineBean) userBag.getPorto();
				updatePKUser(medicine, pkloginUser);
				// 删除记录
				getPKService().delPKUserBag("id=" + id);
				// 更新内存中行囊数据
				deleteCacheUserBagById(userBagList, id);
				tip = medicine.getName() + "使用成功！";
				break;
			// 暗器
			case 2:
				break;
			}
		}
		// 计算用户属性（被动技能 +装备）
		pkloginUser.countPkUser();
		doTip("success", tip);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 使用物品更新用户属性
	 * @datetime:2007-3-8 18:59:02
	 * @param bean
	 * @param pkloginUser
	 * @param flag
	 * @return void
	 */
	public void updatePKUser(PKMedicineBean bean, PKUserBean pkloginUser) {
		if (pkloginUser == null || bean == null) {
			return;
		}
		// 更新体力 如果大于最大体力就等于最大体力
		if (pkloginUser.getCurrentPhysical() + bean.getPhysicalGrowth() > pkloginUser
				.getPhysical()) {
			pkloginUser.setCurrentPhysical(pkloginUser.getPhysical());
		} else {
			pkloginUser.setCurrentPhysical(pkloginUser.getCurrentPhysical()
					+ bean.getPhysicalGrowth());
		}
		// 更新气力 如果大于最大气力就等于最大气力
		if (pkloginUser.getCurrentEnergy() + bean.getEnergyGrowth() > pkloginUser
				.getEnergy()) {
			pkloginUser.setCurrentEnergy(pkloginUser.getEnergy());
		} else {
			pkloginUser.setCurrentEnergy(pkloginUser.getCurrentEnergy()
					+ bean.getEnergyGrowth());
		}
		// 更新轻功
		pkloginUser.setFlying(pkloginUser.getFlying() + bean.getSkillGrowth());
		// 更新攻击力
		pkloginUser.setAggressivity(pkloginUser.getAggressivity()
				+ bean.getAggressGrowth());
		// 更新防御力
		pkloginUser.setRecovery(pkloginUser.getRecovery()
				+ bean.getRecoveryGrowth());
	}

	/**
	 * 
	 * @author macq
	 * @explain： 用户身上装备更新
	 * @datetime:2007-3-8 17:09:07
	 * @param request
	 * @return void
	 */
	public void bodyResult(HttpServletRequest request) {
		// 防止刷新
		if (session.getAttribute("bodyDetail") == null) {
			doTip("refurbish", null);
			return;
		}
		session.removeAttribute("bodyDetail");
		String tip = null;
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		int flag = StringUtil.toInt(request.getParameter("flag"));
		if (flag < 0 || flag > 1) {
			doTip(null, null);
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			doTip(null, null);
			return;
		}
		// 获取用户行囊列表
		Vector userBagList = pkloginUser.getUserBagList();
		PKUserBagBean userBag = null;
		// 通过ID从内存中获取一条用户行囊记录
		for (int i = 0; i < userBagList.size(); i++) {
			PKUserBagBean userBag1 = (PKUserBagBean) userBagList.get(i);
			if (userBag1.getId() == id) {
				userBag = userBag1;
				break;
			}
		}
		if (userBag == null || userBag.getUserId() != loginUser.getId()
				|| userBag.getSiteId() == 0) {
			doTip(null, null);
			return;
		}
		// 丢弃
		if (flag == 1) {
			// 删除记录
			getPKService().delPKUserBag("id=" + id);
			// 更新内存中行囊数据
			deleteCacheUserBagById(userBagList, id);
			tip = "操作成功！";
		}// 放入行囊
		else {
			// 更新记录
			getPKService().updatePKUserBag("site_id=0", "id=" + id);
			// 更新内存中行囊数据
			updateCacheUserBagById(userBagList, id);
			tip = "操作成功！";
		}
		// 计算用户属性（被动技能 +装备）
		pkloginUser.countPkUser();
		doTip("success", tip);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 删除内存中行囊数据
	 * @datetime:2007-3-8 17:29:32
	 * @param userBagList
	 * @param id
	 * @return void
	 */
	public void updateCacheUserBagById(List userBagList, int id) {
		if (userBagList == null) {
			return;
		}
		PKUserBagBean userBag = null;
		for (int i = 0; i < userBagList.size(); i++) {
			userBag = (PKUserBagBean) userBagList.get(i);
			if (userBag.getId() == id) {
				userBag.setSiteId(0);
				break;
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 删除内存中行囊数据
	 * @datetime:2007-3-8 17:29:32
	 * @param userBagList
	 * @param id
	 * @return void
	 */
	public void deleteCacheUserBagById(List userBagList, int id) {
		if (userBagList == null) {
			return;
		}
		PKUserBagBean userBag = null;
		for (int i = 0; i < userBagList.size(); i++) {
			userBag = (PKUserBagBean) userBagList.get(i);
			if (userBag.getId() == id) {
				userBagList.remove(userBag);
				break;
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取用户身上制定位置上是否有装备
	 * @datetime:2007-3-8 18:07:04
	 * @param userBagList
	 * @param id
	 * @return void
	 */
	public int checkCacheUserBagById(List userBagList, int siteId) {
		int userBagId = 0;
		if (userBagList == null) {
			return 0;
		}
		PKUserBagBean userBag = null;
		for (int i = 0; i < userBagList.size(); i++) {
			userBag = (PKUserBagBean) userBagList.get(i);
			if (userBag.getSiteId() == siteId) {
				return userBag.getId();
			}
		}
		return userBagId;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户所拥有的动技能列表
	 * @datetime:2007-3-19 9:26:19
	 * @param request
	 * @return void
	 */
	public void userSkillList(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		List userHskill1 = new ArrayList();
		// 获取用户所有技能
		List userHskill = pkloginUser.getUserSkillList();
		PKUserHSkillBean pkUserHSkill = null;
		for (int i = 0; i < userHskill.size(); i++) {
			pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
			// 取得所有被动技能
			if (pkUserHSkill.getSkillType() == PK_SKILL) {
				userHskill1.add(pkUserHSkill);
			}
		}

		request.setAttribute("userHskill1", userHskill1);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户所拥有的主动技能
	 * @datetime:2007-3-19 9:28:16
	 * @param request
	 * @return void
	 */
	public void userSkillInfo(HttpServletRequest request) {
		session.removeAttribute("userBSkill");
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			doTip(null, null);
			return;
		}
		boolean flag = false;
		// 获取用户所有技能
		List userHskill = pkloginUser.getUserSkillList();
		PKUserHSkillBean pkUserHSkill = null;
		for (int i = 0; i < userHskill.size(); i++) {
			pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
			// 判断用户是否拥有该被动技能
			if (pkUserHSkill.getId() == id) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			doTip(null, "您没有学习过该项被动技能！");
			return;
		}
		request.setAttribute("pkUserHSkill", pkUserHSkill);
		session.setAttribute("userBSkillInfo", "true");
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户所拥有的被动技能列表
	 * @datetime:2007-3-12 16:52:32
	 * @param request
	 * @return void
	 */
	public void userBSkillList(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		List userHskill1 = new ArrayList();
		// 获取用户所有技能
		List userHskill = pkloginUser.getUserSkillList();
		PKUserHSkillBean pkUserHSkill = null;
		for (int i = 0; i < userHskill.size(); i++) {
			pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
			// 取得所有被动技能
			if (pkUserHSkill.getSkillType() == PK_BSKILL) {
				userHskill1.add(pkUserHSkill);
			}
		}

		request.setAttribute("userHskill1", userHskill1);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户所拥有的被动技能
	 * @datetime:2007-3-12 18:09:29
	 * @param request
	 * @return void
	 */
	public void userBSkillInfo(HttpServletRequest request) {
		session.removeAttribute("userBSkill");
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			doTip(null, null);
			return;
		}
		boolean flag = false;
		// 获取用户所有技能
		List userHskill = pkloginUser.getUserSkillList();
		PKUserHSkillBean pkUserHSkill = null;
		for (int i = 0; i < userHskill.size(); i++) {
			pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
			// 判断用户是否拥有该被动技能
			if (pkUserHSkill.getId() == id) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			doTip(null, "您没有学习过该项被动技能！");
			return;
		}
		request.setAttribute("pkUserHSkill", pkUserHSkill);
		session.setAttribute("userBSkillInfo", "true");
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户选择修炼被动技能结果
	 * @datetime:2007-3-12 17:21:04
	 * @param request
	 * @return void
	 */
	public void userBSkillResult(HttpServletRequest request) {
		// 防止刷新
		if (session.getAttribute("userBSkillInfo") == null) {
			doTip("refurbish", null);
			return;
		}
		session.removeAttribute("userBSkillInfo");
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id < 0) {
			doTip(null, null);
			return;
		}
		boolean flag = false;
		// 获取用户所有技能
		List userHskill = pkloginUser.getUserSkillList();
		PKUserHSkillBean pkUserHSkill = null;
		for (int i = 0; i < userHskill.size(); i++) {
			pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
			// 取得所有被动技能判断用户是否拥有该被动技能
			if (pkUserHSkill.getExcersize() == 1) {
				if (pkUserHSkill.getId() == id) {
					doTip(null, "您当前正在修炼该被动技能！");
					return;
				} else {
					// 更新数据库
					if (getPKService().updatePKUserHSkill("excersize=0",
							"id=" + pkUserHSkill.getId())) {
						// 更新内存
						pkUserHSkill.setExcersize(0);
					}
				}
			}
			// 判断用户是否拥有该被动技能
			if (pkUserHSkill.getId() == id) {
				if (getPKService().updatePKUserHSkill("excersize=1",
						"id=" + pkUserHSkill.getId())) {
					// 更新内存
					pkUserHSkill.setExcersize(1);
					flag = true;
				}
			}
		}
		if (!flag) {
			doTip(null, "您没有学习过该项被动技能！");
			return;
		}
		// 计算用户属性（被动技能 +装备）
		pkloginUser.countPkUser();
		doTip("success", "更新成功");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 展示用户身上准备
	 * @datetime:2007-3-8 10:20:39
	 * @param request
	 * @return void
	 */
	public void bodyEquip(HttpServletRequest request) {
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 获取用户行囊中所有数据包括身上装备
		Vector bodyEquipList = pkloginUser.getUserBagList();
		if (bodyEquipList == null) {
			doTip(null, null);
			return;
		}
		PKUserBagBean[] userBagArray = new PKUserBagBean[PK_BODY_COUNT];
		PKUserBagBean userBag = null;
		for (int i = 0; i < bodyEquipList.size(); i++) {
			userBag = (PKUserBagBean) bodyEquipList.get(i);
			if (userBag.getSiteId() > 0) {
				userBagArray[userBag.getSiteId() - 1] = userBag;
			}
		}
		request.setAttribute("userBagArray", userBagArray);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取道具信息
	 * @datetime:2007-4-19 13:20:42
	 * @param type
	 * @param id
	 * @return
	 * @return PKProtoBean
	 */
	public PKProtoBean getProto(int type, int id) {
		PKProtoBean proto = null;
		switch (type) {
		// 装备
		case 0:
			proto = (PKEquipBean) PKWorld.getPKEquip().get(new Integer(id));
			break;
		// 物品
		case 1:
			proto = (PKMedicineBean) PKWorld.getPKMedicine().get(
					new Integer(id));
		// 暗器
		case 2:
			break;

		// 主动技能
		case 3:
			proto = (PKUserSkillBean) PKWorld.getUserSkill().get(
					new Integer(id));
			break;

		// 被动技能
		case 4:
			proto = (PKUserBSkillBean) PKWorld.getUserBSkill().get(
					new Integer(id));
			break;
		// 任务物品
		case 5:
			proto = (PKMObjBean) PKWorld.getPKMObjMap().get(new Integer(id));
			break;
		}
		return proto;
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取任务
	 * @datetime:2007-4-17 16:23:04
	 * @param npcIdId
	 * @return
	 * @return PKProtoBean
	 */
	public PKMissionBean getPKMission(int npcId) {
		PKMissionBean pkMission = (PKMissionBean) PKWorld.getPKMission().get(
				new Integer(npcId));
		return pkMission;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 准备攻击怪物的页面
	 * @datetime:2007-2-1 下午04:14:46
	 */
	public void monster(HttpServletRequest request) {
		// 获取用户id
		int index = StringUtil.toInt(request.getParameter("index"));
		if (index < 0) {
			index = 0;
		}
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		if (pkloginUser.isDeath()) {
			doTip("isDeath", "你已经被打死");
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 获取场景信息
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		if (((List) pkAct.getMonsterList()).size() <= index) {
			doTip(null, null);
			return;
		}
		// 获取场景中怪物的属性
		PKMonsterBean monster = (PKMonsterBean) pkAct.getMonsterList().get(
				index);
		// macq_2007-5-10_判断场景内怪物是否死亡_start
		if (monster.isDeath()) {
			doTip("monsterIsDeath", null);
			return;
		}
		// macq_2007-5-10_判断场景内怪物是否死亡_end
		request.setAttribute("pkAct", pkAct);
		request.setAttribute("monster", monster);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： pk系统用户信息
	 * @datetime:2007-3-14 9:26:58
	 * @param request
	 * @return void
	 */
	public void pkUserInfo(HttpServletRequest request) {
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 删除用户主动技能
	 * @datetime:2007-4-10 15:08:05
	 * @param request
	 * @return void
	 */
	public void delUserSkill(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id <= 0) {
			doTip(null, null);
			return;
		}
		boolean flag = false;
		// 获取用户所有技能
		List userHskill = pkUser.getUserSkillList();
		PKUserHSkillBean pkUserHSkill = null;
		for (int i = 0; i < userHskill.size(); i++) {
			pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
			// 判断用户是否拥有该被动技能
			if (pkUserHSkill.getId() == id) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			doTip(null, "您没有学习过该项主动技能！");
			return;
		} else if (pkUserHSkill.getId() == 13) {
			doTip(null, "您没有学习过该项主动技能！");
			return;
		} else {
			// 删除用户所拥有的被动技能
			boolean mark = getPKService().delPKUserHSkill(
					"id=" + pkUserHSkill.getId());
			if (mark) {
				// 删除内存中用户所拥有的被动技能
				userHskill.remove(pkUserHSkill);
			}
		}
		doTip("success", "操作成功！该主动技能已经被成功删除！");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 删除用户被动技能
	 * @datetime:2007-4-10 15:08:09
	 * @param request
	 * @return void
	 */
	public void delUserBSkill(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id <= 0) {
			doTip(null, null);
			return;
		}
		boolean flag = false;
		// 获取用户所有技能
		List userHskill = pkUser.getUserSkillList();
		PKUserHSkillBean pkUserHSkill = null;
		for (int i = 0; i < userHskill.size(); i++) {
			pkUserHSkill = (PKUserHSkillBean) userHskill.get(i);
			// 判断用户是否拥有该被动技能
			if (pkUserHSkill.getId() == id) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			doTip(null, "您没有学习过该项被动技能！");
			return;
		} else if (pkUserHSkill.getExcersize() == 1) {
			doTip(null, "该项被动技能正在修炼中，不能删除！");
			return;
		} else {
			// 删除用户所拥有的被动技能
			boolean mark = getPKService().delPKUserHSkill(
					"id=" + pkUserHSkill.getId());
			if (mark) {
				// 删除内存中用户所拥有的被动技能
				userHskill.remove(pkUserHSkill);
				// 计算用户属性（被动技能 +装备）
				pkUser.countPkUser();
			}
		}
		doTip("success", "操作成功！该被动技能已经被成功删除！");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 准备攻击用户的页面
	 * @datetime:2007-2-1 下午04:14:46
	 */
	public void player(HttpServletRequest request) {
		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		if (currentHour < 20 || currentHour > 21) {
			doTip(null, "打人只能在20:00-22:00之间!");
			return;
		}
		// 获取用户id
		int playerId = StringUtil.toInt(request.getParameter("playerId"));
		if (playerId <= 0) {
			doTip(null, "查询无此用户.");
			return;
		}
		// 获取pk用户信息
		PKUserBean pkUser = (PKUserBean) PKWorld.getPKUserMap().get(
				new Integer(playerId));
		if (pkUser == null) {
			doTip(null, "查询无此用户.");
			return;
		}
		if (pkUser.isDeath()) {
			doTip("pkUserIsDeath", null);
			return;
		}
		// 从session中获取当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		if (pkloginUser.isDeath()) {
			doTip("isDeath", "你已经被打死");
			return;
		}
		// 获取场景id
		int sceneId = pkloginUser.getSceneId();
		// 判断用户是否在该场景中
		if (pkUser.getSceneId() != sceneId) {
			doTip(null, "该用户不在此场景中.");
			return;
		}
		// 获取用户所在场景
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		request.setAttribute("pkAct", pkAct);
		request.setAttribute("player", pkUser);
		// request.setAttribute("result", result);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author liuy
	 * @explain：
	 * @datetime:2007-3-13 16:53:18
	 * @param request
	 * @return void
	 */
	public void fight(HttpServletRequest request) {
		// 当前用户pk信息
		PKUserBean pkloginUser = loginUser.getPkUser();
		if (pkloginUser == null) {
			doTip(null, null);
			return;
		}
		// 判断是否已经死亡
		if (pkloginUser.isDeath()) {
			doTip(null, "你已经被打死.");
			return;
		}
		// 判断是否连续攻击
		if (System.currentTimeMillis() - pkloginUser.getLastAttackTime() < PKAction.MIN_ATTACK_INTERVAL) {
			doTip(null, "不能连续攻击.");
			return;
		}
		// 场景id
		int sceneId = pkloginUser.getSceneId();
		// 场景信息
		PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
				new Integer(sceneId));
		if (pkAct == null) {
			doTip(null, null);
			return;
		}
		String isType = request.getParameter("isType");
		if ("monster".equals(isType)) { // 跟怪兽打
			// 怪兽下标
			int index = StringUtil.toInt(request.getParameter("index"));
			if (index < 0) {
				index = 0;
			}
			PKMonsterBean monster = (PKMonsterBean) pkAct.getMonsterList().get(
					index);
			// 怪物已经死亡
			if (monster.isDeath()) {
				doTip(null, "怪物已经死亡.");
				return;
			}
			// 人的技能
			int skillId = StringUtil.toInt(request.getParameter("skillId"));
			PKUserSkillBean userSkill = (PKUserSkillBean) PKWorld
					.getUserSkill().get(new Integer(skillId));
			if (userSkill == null) {
				doTip(null, null);
				return;
			}
			// 获取用户被动技能
			PKUserHSkillBean userHSkillBean = null;
			for (int i = 0; i < pkloginUser.getUserSkillList().size(); i++) {
				userHSkillBean = (PKUserHSkillBean) pkloginUser
						.getUserSkillList().get(i);
				if (userHSkillBean != null
						&& userHSkillBean.getExcersize() == 1
						&& userHSkillBean.getSkillType() == PK_BSKILL) {
					// 增加被动技能点
					userHSkillBean
							.setSkillKey(userHSkillBean.getSkillKey() + 1);
					if (userHSkillBean.getRank() <= PK_BSKILL_RANK.length) {
						// 获取该等级技能点基础数值
						int baseBSkillKey = PK_BSKILL_RANK[userHSkillBean
								.getRank() - 1];
						// 判断技能点增加后是否大于基础技能点，并且有下一等级
						if (userHSkillBean.getSkillKey() > baseBSkillKey
								&& userHSkillBean.getRank() + 1 <= PK_BSKILL_RANK.length) {
							// 被动技能等级升级
							userHSkillBean
									.setRank(userHSkillBean.getRank() + 1);
							// 计算用户属性（被动技能 +装备）
							pkloginUser.countPkUser();
						}
					}
					break;
				}
			}
			// 技能等级
			String[] radixes = userSkill.getAggressGrowthRadix().split(",");
			// PKUserHSkillBean userHSkillBean = null;
			for (int i = 0; i < pkloginUser.getUserSkillList().size(); i++) {
				userHSkillBean = (PKUserHSkillBean) pkloginUser
						.getUserSkillList().get(i);
				if (userHSkillBean != null
						&& userHSkillBean.getSkillId() == skillId
						&& userHSkillBean.getSkillType() == PK_SKILL) {
					// 更新技能点
					userHSkillBean
							.setSkillKey(userHSkillBean.getSkillKey() + 1);
					// 判断是否超过最大等级
					if (userHSkillBean.getRank() <= PK_SKILL_RANK.length) {
						// 获取该等级技能点基础数值
						int baseSkillKey = PK_SKILL_RANK[userHSkillBean
								.getRank() - 1];
						// 判断技能点增加后是否大于基础技能点，并且有下一等级
						if (userHSkillBean.getSkillKey() > baseSkillKey
								&& userHSkillBean.getRank() + 1 <= PK_SKILL_RANK.length) {
							// 主动技能等级升级
							userHSkillBean
									.setRank(userHSkillBean.getRank() + 1);
							// 计算用户属性（被动技能 +装备）
							pkloginUser.countPkUser();
						}
					}
					break;
				}
			}
			// 获取主动技能等级
			// int skillRank = Math.min(userHSkillBean.getSkillKey() / 100, 10);
			int skillRank = userHSkillBean.getRank();
			// 获取耗费气力数值
			int energy = pkloginUser.getCurrentEnergy()
					- PK_ENERGY_RANK[skillRank - 1];
			if (energy < 0) {
				doTip(null, "你的气力不够，无法使用该技能.");
				return;
			}
			// 打斗开始
			// 更新最后攻击时间
			pkloginUser.setLastAttackTime(System.currentTimeMillis());
			// 更新气力值
			pkloginUser.setCurrentEnergy(energy);

			// 主动技能攻击力
			float radix = Float.parseFloat(radixes[skillRank - 1]);
			// 现阶攻击力+攻击力随机数*攻击力系数
			int aggressivity = 0;
			// 被动攻击随机系数
			if (pkloginUser.getAggressivityRnd() > 0) {
				aggressivity = (int) ((pkloginUser.getAggressivity() + RandomUtil
						.nextInt(pkloginUser.getAggressivityRnd())) * radix);
			} else {
				aggressivity = (int) (pkloginUser.getAggressivity() * radix);
			}

			String[] descs = userSkill.getDescription2().split("&");
			String desc = descs[RandomUtil.nextInt(descs.length)];
			String log = desc.replace("你", loginUser.getNickName());
			// 攻击怪物数值
			int mosterValue = aggressivity - monster.getRecovery();
			if (mosterValue <= 0) {
				mosterValue = 1;
			}

			// 计算怪物参数
			int monsterPhysical = monster.getPhysical() - mosterValue;
			pkAct.addLog(log + "造成" + mosterValue + "点伤害");
			if (monsterPhysical <= 0) { // 怪物被打死
				// 更新怪物体力值
				monster.setPhysical(0);
				monster.setDeath(true);
				monster.setDeathTime(System.currentTimeMillis());
				// 获取怪物原始数据模型
				PKMonsterBean baseMonster = (PKMonsterBean) PKWorld
						.loadMoster().get(monster.getId() + "");
				int updateGamePoint = 0;
				if (baseMonster != null) {
					updateGamePoint = baseMonster.getPhysical() * 10;
					// 更新用户乐币
					UserInfoUtil.updateUserStatus("game_point=game_point+"
							+ updateGamePoint, "user_id=" + loginUser.getId(),
							loginUser.getId(), UserCashAction.OTHERS, "PK系统杀死"
									+ baseMonster.getName() + "获得"
									+ updateGamePoint + "乐币");
				}
				log = monster.getName() + "死了," + loginUser.getNickName()
						+ "获得" + updateGamePoint + "乐币.";

				// macq-2007-4-19_打死怪兽后场景内增加掉落物品_Start
				// 获取1-100之间的随机数
				int randomId = RandomUtil.nextInt(100);
				// 判断随机数是否大于怪兽掉落物品概率
				if (randomId > monster.getRate()) {
					// 获取怪物掉路物品列表大小
					int size = monster.getDropTypeIdList().size();
					// 随机获取一个列表中的位置
					size = RandomUtil.nextInt(size);
					PKObjTypeBean objType = (PKObjTypeBean) monster
							.getDropTypeIdList().get(size);
					if (objType != null) {
						// 向场景列表里添加掉落物品
						pkAct.getDropMap().put(
								objType.getType() + "_" + objType.getId(),
								objType);
					}
				}
				// macq-2007-4-19_打死怪兽后场景内增加掉落物品_end
				// macq-2007-4-23_打死怪兽后增加用户战斗经验值，并判断是否满足升级条件_start
				// PK_EXPERIENCE_RANK
				int experience = pkloginUser.getExperience();
				int experienceChange = experience + 1;
				// 增加用户战斗经验
				pkloginUser.setExperience(experienceChange);
				int rank = pkloginUser.getRank();
				// 判断是否还有下一等级
				if (rank < PK_EXPERIENCE_RANK.length) {
					int baseExperience = PK_EXPERIENCE_RANK[rank];
					// 战斗经验值升级增加人物属性
					if (experienceChange >= baseExperience) {
						int physicalChange = RandomUtil.nextInt(5) + 13;
						int energyChange = RandomUtil.nextInt(2) + 7;
						//int aggressivityChange = RandomUtil.nextInt(5) + 3;
						int aggressivityChange = 3;
						//int recoveryChange = RandomUtil.nextInt(5) + 1;
						int recoveryChange = 3;
						pkloginUser.setBasePhysical(pkloginUser
								.getBasePhysical()
								+ physicalChange);
						pkloginUser.setBaseEnergy(pkloginUser.getBaseEnergy()
								+ energyChange);
						pkloginUser.setBaseAggressivity(pkloginUser
								.getBaseAggressivity()
								+ aggressivityChange);
						pkloginUser.setBaseRecovery(pkloginUser
								.getBaseRecovery()
								+ recoveryChange);
						// 更新用户战斗等级
						pkloginUser.setRank(rank + 1);
						// 重新计算用户数据
						pkloginUser.countPkUser();
						String set = "experience="
								+ pkloginUser.getExperience()
								+ ",base_physical="
								+ pkloginUser.getBasePhysical()
								+ ",base_energy=" + pkloginUser.getBaseEnergy()
								+ ",base_aggressivity="
								+ pkloginUser.getBaseAggressivity()
								+ ",base_recovery="
								+ pkloginUser.getBaseRecovery();
						String condition = "id=" + pkloginUser.getId();
						// 更新数据库中用户属性数据
						getPKService().updatePKUser(set, condition);
						log = log + loginUser.getNickName() + "升级成功！";
					}
				}
				// macq-2007-4-23_打死怪兽后增加用户战斗经验值，并判断是否满足升级条件_end
				pkAct.addLog(log);
			} else {
				// 更新怪物体力值
				monster.setPhysical(monsterPhysical);

				if (System.currentTimeMillis() - monster.getLastAttackTime() > PKAction.MIN_ATTACK_INTERVAL) { // 怪物5秒内只能攻击一次
					monster.setLastAttackTime(System.currentTimeMillis());

					// 怪物技能
					String[] skillIds = monster.getSkillId().split(",");
					int randomIndex = RandomUtil.nextInt(skillIds.length);
					Integer monsterSkillId = new Integer(skillIds[randomIndex]);
					PKMonsterSkillBean monsterSkill = (PKMonsterSkillBean) PKWorld
							.getMosterSkill().get(monsterSkillId);
					// 怪物的攻击力
					int monsterAggressivity = monster.getAggressivity();

					log = monsterSkill.getDescription().replace("xxx",
							monster.getName()).replace("你",
							loginUser.getNickName());
					// 体力数值变化
					int physical = 0;
					// 造成伤害数值
					int value = 0;
					if (pkloginUser.getRecoveryRnd() > 0) {
						// 防御随机数
						int recoveryRnd = RandomUtil.nextInt(pkloginUser
								.getRecoveryRnd());
						value = monsterAggressivity
								- (pkloginUser.getRecovery() + recoveryRnd);
					} else {
						value = monsterAggressivity - pkloginUser.getRecovery();
					}
					if (value <= 0) {
						value = 1;
					}
					// 现阶段体力+用户防御力+防御力随机数-怪物攻击力
					physical = pkloginUser.getCurrentPhysical() - value;
					// pkAct.addLog(log);
					pkAct.addLog(log + "造成" + value + "点伤害");

					if (physical <= 0) { // 被打死
						// 更新体力值
						pkloginUser.setCurrentPhysical(0);
						pkloginUser.setDeath(true);
						pkloginUser.setDeathTime(System.currentTimeMillis());
						log = loginUser.getNickName() + "被" + monster.getName()
								+ "打死了";
						pkAct.addLog(log);
					} else {
						// 更新体力值
						pkloginUser.setCurrentPhysical(physical);
					}
				}
			}
			request.setAttribute("isType", isType);
			request.setAttribute("index", index + "");
		} else if ("person".equals(isType)) { // 跟人打
			Calendar cal = Calendar.getInstance();
			int currentHour = cal.get(Calendar.HOUR_OF_DAY);
			if (currentHour < 20 || currentHour > 22) {
				doTip(null, "打人只能在20:00-22:00之间!");
				return;
			}
			// 获取用户id
			int playerId = StringUtil.toInt(request.getParameter("playerId"));
			if (playerId <= 0) {
				doTip(null, "查询无此用户.");
				return;
			}
			// 获取pk用户信息
			PKUserBean pkUser = (PKUserBean) PKWorld.getPKUserMap().get(
					new Integer(playerId));
			if (pkUser == null) {
				doTip(null, "查询无此用户.");
				return;
			}
			UserBean user = UserInfoUtil.getUser(pkUser.getUserId());
			if (user == null) {
				doTip(null, "查询无此用户.");
				return;
			}
			if (pkUser.isDeath()) {
				doTip(null, user.getNickName() + "已经死亡.");
				return;
			}
			// 判断用户是否在该场景中
			if (pkUser.getSceneId() != pkloginUser.getSceneId()) {
				doTip(null, "该用户不在此场景中.");
				return;
			}
			// 人的技能
			int skillId = StringUtil.toInt(request.getParameter("skillId"));
			PKUserSkillBean userSkill = (PKUserSkillBean) PKWorld
					.getUserSkill().get(new Integer(skillId));
			if (userSkill == null) {
				doTip(null, null);
				return;
			}
			// 获取用户被动技能
			PKUserHSkillBean userHSkillBean = null;
			for (int i = 0; i < pkloginUser.getUserSkillList().size(); i++) {
				userHSkillBean = (PKUserHSkillBean) pkloginUser
						.getUserSkillList().get(i);
				if (userHSkillBean != null
						&& userHSkillBean.getExcersize() == 1
						&& userHSkillBean.getSkillType() == PK_BSKILL) {
					// 增加被动技能点
					userHSkillBean
							.setSkillKey(userHSkillBean.getSkillKey() + 1);
					if (userHSkillBean.getRank() <= PK_BSKILL_RANK.length) {
						// 获取该等级技能点基础数值
						int baseBSkillKey = PK_BSKILL_RANK[userHSkillBean
								.getRank() - 1];
						// 判断技能点增加后是否大于基础技能点，并且有下一等级
						if (userHSkillBean.getSkillKey() > baseBSkillKey
								&& userHSkillBean.getRank() + 1 <= PK_BSKILL_RANK.length) {
							// 被动技能等级升级
							userHSkillBean
									.setRank(userHSkillBean.getRank() + 1);
							// 计算用户属性（被动技能 +装备）
							pkloginUser.countPkUser();
						}
					}
					break;
				}
			}
			// 技能等级
			String[] radixes = userSkill.getAggressGrowthRadix().split(",");
			// PKUserHSkillBean userHSkillBean = null;
			for (int i = 0; i < pkloginUser.getUserSkillList().size(); i++) {
				userHSkillBean = (PKUserHSkillBean) pkloginUser
						.getUserSkillList().get(i);
				if (userHSkillBean != null
						&& userHSkillBean.getSkillId() == skillId
						&& userHSkillBean.getSkillType() == PK_SKILL) {
					// 更新技能点
					userHSkillBean
							.setSkillKey(userHSkillBean.getSkillKey() + 1);
					// 判断是否超过最大等级
					if (userHSkillBean.getRank() <= PK_SKILL_RANK.length) {
						// 获取该等级技能点基础数值
						int baseSkillKey = PK_SKILL_RANK[userHSkillBean
								.getRank() - 1];
						// 判断技能点增加后是否大于基础技能点，并且有下一等级
						if (userHSkillBean.getSkillKey() > baseSkillKey
								&& userHSkillBean.getRank() + 1 <= PK_SKILL_RANK.length) {
							// 主动技能等级升级
							userHSkillBean
									.setRank(userHSkillBean.getRank() + 1);
							// 计算用户属性（被动技能 +装备）
							pkloginUser.countPkUser();
						}
					}
					break;
				}
			}
			// 获取主动技能等级
			// int skillRank = Math.min(userHSkillBean.getSkillKey() / 100, 10);
			int skillRank = userHSkillBean.getRank();
			// 获取耗费气力数值
			int energy = pkloginUser.getCurrentEnergy()
					- PK_ENERGY_RANK[skillRank - 1];
			if (energy < 0) {
				doTip(null, "你的气力不够，无法使用该技能.");
				return;
			}
			// 打斗开始
			// 更新最后攻击时间
			pkloginUser.setLastAttackTime(System.currentTimeMillis());
			// 更新气力值
			pkloginUser.setCurrentEnergy(energy);

			// 主动技能攻击力
			float radix = Float.parseFloat(radixes[skillRank - 1]);
			// 现阶攻击力+攻击力随机数*攻击力系数
			int aggressivity = 0;
			// 被动攻击随机系数
			if (pkloginUser.getAggressivityRnd() > 0) {
				aggressivity = (int) ((pkloginUser.getAggressivity() + RandomUtil
						.nextInt(pkloginUser.getAggressivityRnd())) * radix);
			} else {
				aggressivity = (int) (pkloginUser.getAggressivity() * radix);
			}

			String[] descs = userSkill.getDescription2().split("&");
			String desc = descs[RandomUtil.nextInt(descs.length)];
			String log = desc.replace("你", loginUser.getNickName() + "对"
					+ user.getNickName());
			// 攻击人物数值
			int pkUserValue = aggressivity - pkUser.getRecovery();
			if (pkUserValue <= 0) {
				pkUserValue = 1;
			}
			// 计算人物参数
			int personPhysical = pkUser.getCurrentPhysical() - pkUserValue;
			pkAct.addLog(log + "造成" + pkUserValue + "点伤害");
			if (personPhysical <= 0) { // 怪物被打死
				// 更新体力值
				pkUser.setCurrentPhysical(0);
				pkUser.setDeath(true);
				pkUser.setDeathTime(System.currentTimeMillis());
				log = user.getNickName() + "被" + loginUser.getNickName()
						+ "打死了";
				pkAct.addLog(log);
				// macq_2007-5-10_pk系统人人对战记录打死人物次数_start
				getPKService().updatePKUser("kcount=kcount+1",
						"user_id=" + loginUser.getId());
				// macq_2007-5-10_pk系统人人对战记录打死人物次数_end
			} else {
				pkUser.setCurrentPhysical(personPhysical);
			}
			request.setAttribute("isType", isType);
			request.setAttribute("playerId", playerId + "");
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：杀人王排名
	 * @datetime:2007-5-10 10:23:13
	 * @param request
	 * @return void
	 */
	public void pkTop() {
		PKUserBean pkUser = getPKService().getPKUser(
				"user_id=" + loginUser.getId());

		int oldKCount = 0;
		if (pkUser != null) {
			oldKCount=pkUser.getOldKCount();
			if (oldKCount < 0) {
				oldKCount = 0;
			}
		}
		// 查询语句
		String query = "select count(id) count from pk_user where old_kcount>"
				+ oldKCount;

		String key = "" + (1 + oldKCount / 5);

		Integer rankCount = (Integer) OsCacheUtil.get(key,
				OsCacheUtil.PK_USER_KTOP_GROUP,
				OsCacheUtil.PK_USER_KTOP_FLUSH_PERIOD);
		if (rankCount == null) {
			DbOperation dbOp = new DbOperation();
			dbOp.init();

			// 执行查询
			int count = 0;
			ResultSet rs = dbOp.executeQuery(query);
			try {
				if (rs != null && rs.next()) {
					count = rs.getInt("count");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();

			rankCount = new Integer(count);
			OsCacheUtil.put(key, rankCount, OsCacheUtil.PK_USER_KTOP_GROUP);
		}
		Vector userList = LoadResource.getPKUserKTopList();
		request.setAttribute("count", String.valueOf(rankCount.intValue() + 1));
		request.setAttribute("userList", userList);
		request.setAttribute("pkUser", pkUser);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： PK系统帮助页面
	 * @datetime:2007-5-10 13:15:00
	 * @return void
	 */
	public void help() {
		// 查询语句
		String query = "select *  from pk_user order by old_kcount desc limit 1 ";
		PKUserBean pkUser = (PKUserBean) OsCacheUtil.get(query,
				OsCacheUtil.PK_USER_KTOP_GROUP,
				OsCacheUtil.PK_USER_KTOP_FLUSH_PERIOD);
		if (pkUser == null) {
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 执行查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				if (rs != null && rs.next()) {
					pkUser = new PKUserBean();
					pkUser.setId(rs.getInt("id"));
					pkUser.setUserId(rs.getInt("user_id"));
					// pkUser.setExperience(rs.getInt("experience"));
					// pkUser.setBasePhysical(rs.getInt("base_physical"));
					// pkUser.setBaseEnergy(rs.getInt("base_energy"));
					// pkUser.setBaseAggressivity(rs.getInt("base_aggressivity"));
					// pkUser.setBaseRecovery(rs.getInt("base_recovery"));
					// pkUser.setBaseFlying(rs.getInt("base_flying"));
					// pkUser.setBaseLuck(rs.getInt("base_luck"));
					// pkUser.setCurrentPhysical(rs.getInt("current_physical"));
					// pkUser.setCurrentEnergy(rs.getInt("current_energy"));
					// pkUser.setBag(rs.getInt("bag"));
					// pkUser.setMissionStart(rs.getString("mission_start"));
					// pkUser.setMissionEnd(rs.getString("mission_end"));
					pkUser.setKCount(rs.getInt("kcount"));
					pkUser.setOldKCount(rs.getInt("old_kcount"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
			OsCacheUtil.put(query, pkUser, OsCacheUtil.PK_USER_KTOP_GROUP);
		}
		UserBean user = UserInfoUtil.getUser(pkUser.getUserId());
		request.setAttribute("user", user);
		request.setAttribute("pkUser", pkUser);
	}

	/**
	 * liuyi 2007-02-01 pk系统定时任务
	 */
	public static void timeTask() {
		HashMap pkUserMap = PKWorld.getPKUserMap();
		if (pkUserMap != null) {
			PKServiceImpl service = getPKService();

			HashMap map = (HashMap) pkUserMap.clone();
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				Integer id = (Integer) iter.next();
				PKUserBean bean = (PKUserBean) map.get(id);
				if (bean == null)
					continue;

				// 保存角色信息
				// String set = "experience=" + bean.getExperience()
				// + ",base_physical=" + bean.getBasePhysical() +
				// ",base_energy="
				// + bean.getBaseEnergy() + ",base_aggressivity="
				// + bean.getBaseAggressivity() + ",base_recovery="
				// + bean.getBaseRecovery() + ",base_flying="
				// + bean.getBaseFlying() + ",base_luck=" + bean.getBaseLuck()
				// + ",current_physical=" + bean.getCurrentPhysical()
				// + ",current_energy=" + bean.getCurrentEnergy();
				String set = "experience=" + bean.getExperience()
						+ ",current_physical=" + bean.getCurrentPhysical()
						+ ",current_energy=" + bean.getCurrentEnergy();
				String condition = "id=" + bean.getId();
				service.updatePKUser(set, condition);

				// 保存用户技能点
				List skillList = bean.getUserSkillList();
				if (skillList != null) {
					for (int i = 0; i < skillList.size(); i++) {
						PKUserHSkillBean skill = (PKUserHSkillBean) skillList
								.get(i);
						if (skill != null) {
							set = "skill_key=" + skill.getSkillKey() + ",rank="
									+ skill.getRank();
							condition = "id=" + skill.getId();
							service.updatePKUserHSkill(set, condition);
						}
					}
				}
				// 判断用户是否下线
				if (bean.userOffline()) {
					bean.setOffline(true);
					// macq_2007-3-16_pk系统用户下线注销游戏中用户_start
					int sceneId = bean.getSceneId();
					// 获取用户所有场景信息
					PKActBean pkAct = (PKActBean) PKWorld.loadPKWorld().get(
							new Integer(sceneId));
					if (pkAct != null) {
						Set userList = pkAct.getPkUserList();
						// 删除场景列表中用户
						userList.remove(bean);
					}
					// 删除pk系统在线用户
					PKWorld.getPKUserMap().remove(new Integer(bean.getId()));
					// macq_2007-3-16_pk系统用户下线注销游戏中用户_end
				}

			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 更具经验值折合等级
	 * @datetime:2007-4-23 9:45:41
	 * @param rank
	 * @param point
	 * @return
	 * @return boolean
	 */
	public static int getExperienceRank(int experience) {
		int rank = 0;
		if (experience < 100) {
			return rank;
		}
		for (int i = 0; i < PK_EXPERIENCE_RANK.length; i++) {
			int basePoint = PK_EXPERIENCE_RANK[i];
			if (experience < basePoint) {
				return i;
			}
		}
		return PK_EXPERIENCE_RANK.length;
	}

	/**
	 * 
	 * @author macq
	 * @explain：主动技能等级计算，判断是否升级
	 * @datetime:2007-3-12 16:37:37
	 * @return void
	 */
	public boolean getSklillRank(int rank, int point) {
		// 获取主动技能等级个数
		int size = PK_SKILL_RANK.length;
		// 如果当前等级大于技能等级个数不升级
		if (rank >= size) {
			return false;
		}
		// 获取该等级默认升级技能点
		int basePoint = PK_SKILL_RANK[rank - 1];
		// 判断是否升级
		if (point > basePoint) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author macq
	 * @explain：被动技能等级计算，判断是否升级
	 * @datetime:2007-3-12 16:37:37
	 * @return void
	 */
	public boolean getBSklillRank(int rank, int point) {
		// 获取被动技能等级个数
		int size = PK_BSKILL_RANK.length;
		// 如果当前等级大于技能等级个数不升级
		if (rank >= size) {
			return false;
		}
		// 获取该等级默认升级技能点
		int basePoint = PK_BSKILL_RANK[rank - 1];
		// 判断是否升级
		if (point > basePoint) {
			return true;
		}
		return false;
	}

	/**
	 * liuyi 2007-02-06 定期复活怪物
	 */
	public static void monstersRevive() {
		HashMap actMap = PKWorld.loadPKWorld();

		Iterator iter = actMap.values().iterator();
		while (iter.hasNext()) {
			PKActBean scene = (PKActBean) iter.next();
			List monsterList = scene.getMonsterList();
			if (monsterList == null) {
				continue;
			}

			for (int k = 0; k < monsterList.size(); k++) {
				PKMonsterBean monster = (PKMonsterBean) monsterList.get(k);
				if (monster == null)
					continue;
				if (!monster.isDeath()) {
					continue;
				}

				if (System.currentTimeMillis() - monster.getDeathTime() > PKAction.MONSTER_REVIVE_INTERVAL) {
					// 获取初始化怪物属性
					PKWorld.buildMonster(monster, monster.getId());
					monster.setDeath(false);
					monster.setDeathTime(0);

					String log = monster.getName() + "复活了！";
					scene.addLog(log);
				}
			}
		}
	}

	/**
	 * @return Returns the pkUser.
	 */
	public PKUserBean getPkUser() {
		return pkUser;
	}
}
