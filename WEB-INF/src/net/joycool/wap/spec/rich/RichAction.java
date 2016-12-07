package net.joycool.wap.spec.rich;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author zhouj
 * @explain： 大富翁
 * @datetime:1007-10-24
 */
public class RichAction extends CustomAction{
	
	public static RichWorld[] worlds = {
		new RichWorld(40 * 60, 4, 3),
		new RichWorld(50 * 60, 4, 3),
		new RichWorld(60 * 60, 6, 3),
		new RichWorld(80 * 60, 8, 3),
		new RichWorld(30 * 60, 6, 0),
		new RichWorld(30 * 60, 6, 0),
		new RichWorld(40 * 60, 6, 0),
	};	//  三个大富翁世界
	public static RichService service = new RichService();
	public static int ACTION_COOL_DOWN = 10000;		//	冷却时间，例如骰子(10秒间隔)和道具等等	
	
	public long now = 0;

	public RichWorld world = null;

	UserBean loginUser;

	RichUserBean richUser = null;

	public RichAction(HttpServletRequest request) {
		super(request);
		check(); // check user
	}
	
	public RichAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		check(); // check user
	}

	public void check() {
		loginUser = super.getLoginUser();
		if(loginUser == null)
			return;
		richUser = RichWorld.getRichUser(loginUser.getId());
		if(richUser.getWorldId()>= 0) {
			world = worlds[richUser.getWorldId()];
			world.prepare();
		}
		now = System.currentTimeMillis();
	}

	public void addLog(String content) {
		world.log.add(content);
	}
	public String getLogString() {
		return world.log.toString();
	}
	
	public void index() {
		for(int i = 0;i < worlds.length;i++)
			worlds[i].prepare();
	}
	
	public void spy() {
		if(richUser.isInSpy()) {
			int act = getParameterInt("a");
			if(act == 0) {
	
			} else {
				richUser.setInSpy(false);
			}
		}
	}
	
	public void magic() {
		if(richUser.isInMagic()) {
			int act = getParameterInt("a");
			if(act == 0) {
	
			} else if(act == 1){
				int option = getParameterInt("o");		// 选择一个玩家（可以是自己）
				if(option > 0) {
					RichUserBean target = getRichUser(option);
					if(target != null) {
						String result = "";
						switch(RandomUtil.nextInt(8)) {
						case 0:{
							result = "获得现金500";
							target.addMoney(500);
						} break;
						case 1:{
							result = "损失现金500";
							target.addMoney(-500);
						} break;
						case 2:{
							result = "银行存款增加10%";
							target.addSavingRate(0.1f);
						} break;
						case 3:{
							result = "银行存款减少10%";
							target.addSavingRate(-0.1f);
						} break;
						case 4:{
							result = "获得点券50";
							target.addMoney2(50);
						} break;
						case 5:{
							result = "损失点券50";
							target.addMoney2(-50);
						} break;
						case 6:{
							result = "获得一张卡片";
							world.addRandomItem(target);
						} break;
						case 7:{
							result = "损失一张卡片";
							world.removeRandomItem(target);
						} break;
						}
						if(richUser == target) {
							richUser.addLog("你使用了魔法屋，你" + result);
						} else {
							richUser.addLog("你使用了魔法屋，" + target.getWmlName() + result);
							target.addLog(richUser.getWmlName() + "使用了魔法屋，你" + result);
						}
					}
					richUser.setInMagic(false);
				}
				
			} else {
				richUser.setInMagic(false);
			}
		}
	}
	
	public void court() {
		if(richUser.isInCourt()) {
			int act = getParameterInt("a");
			if(act == 0) {
	
			} else if(act == 1){
				int option = getParameterInt("o");		// 选择一个玩家（不可以是自己）
				if(option > 0 && option != richUser.getUserId()) {
					RichUserBean target = getRichUser(option);
					if(target != null) {
						if(RandomUtil.percentRandom(25)) {		// 1/4概率自己入狱
							world.moveUser(richUser, (RichNodeBean)RandomUtil.randomObject(world.jailNodes));
							richUser.lockAction(now + 30000, 1);
							richUser.addLog("您告" + target.getWmlName() + "藐视电脑失败，关进监狱，须停留30秒之后才能继续前行。");
							
						} else {
							target.noControl();
							world.moveUser(target, (RichNodeBean)RandomUtil.randomObject(world.jailNodes));
							target.lockAction(now + 30000, 1);
							richUser.addLog("您告" + target.getWmlName() + "藐视电脑成功，他被关进监狱，须停留30秒之后才能继续前行。");
							target.addLog(richUser.getWmlName() + "告你藐视电脑成功，你被关进监狱，须停留30秒之后才能继续前行。");
						}
					}
					richUser.setInCourt(false);
				}
			} else {
				richUser.setInCourt(false);
			}
		}
	}
	
	public void game1() {		// 猜大小
		if(richUser.isInGame1()) {
			int act = getParameterInt("a");
			if(act == 0) {
	
			} else if(act == 1){
				int option = getParameterInt("o");
				if(world.game.winNum(option)) {
					tip("tip", "猜中啦！恭喜您赢得点券" + RichGame.NUM_MONEY2);
					richUser.addMoney2(RichGame.NUM_MONEY2);
				} else {
					tip("tip", "没有猜中，下次继续努力吧！");
				}
				richUser.setInGame1(false);
			} else {
				richUser.setInGame1(false);
			}
		}
	}
	
	public void game2() {		// 要乐要酷
		if(richUser.isInGame2()) {
			int act = getParameterInt("a");
			if(act == 0) {
				if(richUser.getMoney() < RichGame.LK_MONEY) {
					tip("tip", "现金不足，无法参与对决");
					richUser.setInGame2(false);
				}
			} else if(act == 1){
				if(richUser.getMoney() < RichGame.LK_MONEY) {
					tip("tip", "现金不足，无法参与对决");

				} else {
					
					int option = getParameterInt("o");
					richUser.addMoney(-RichGame.LK_MONEY);
					if(world.game.lkUser == null) {
						tip("tip", "已摆下擂台，等别人来挑战！");
						world.game.lkUser = richUser;
					} else {
						RichUserBean target = world.game.lkUser;
						if(world.game.winLk(option)) {
							richUser.addMoney(RichGame.LK_MONEY * 2);
							tip("tip", "对决获胜！赢得现金" + RichGame.LK_MONEY);
						} else {
							target.addMoney(RichGame.LK_MONEY * 2);
							tip("tip", "对决失败，下次再努力吧！");
						}
					}
				}
				richUser.setInGame2(false);
			} else {
				richUser.setInGame2(false);
			}
		}
	}
	
	public void game3() {		// 彩票
		if(richUser.isInGame3()) {
			int act = getParameterInt("a");
			if(act == 0) {
				if(richUser.getMoney() < RichGame.LOTTERY_MONEY) {
					tip("tip", "现金不足，无法参与彩票");
					richUser.setInGame3(false);
				}
			} else if(act == 1){
			
				if(richUser.getMoney() < RichGame.LOTTERY_MONEY) {
					tip("tip", "现金不足，无法参与彩票");
				} else {
					int option = getParameterInt("o");
					richUser.addMoney(-RichGame.LOTTERY_MONEY);
					int money = world.game.winLottery(option);
					if(money == 0)
						tip("tip", "没有中奖，欢迎下次再来！");
					else {
						tip("tip", "中奖了！赢得现金" + money);
						richUser.addMoney(money);
					}
				}
				richUser.setInGame3(false);
			} else {
				richUser.setInGame3(false);
			}
		}
	}
	
	//	开始游戏，选择人物
	public void start() {
		int w = getParameterInt("w");
		if(w >= 0 && w < worlds.length)
			world = worlds[w];
		else {
			tip("return");
			return;
		}
		if(world.isFull()) {
			tip("return", "人满了，请稍后再来");
			return;
		}
		
		int step = getParameterInt("s");	// s = 第几步
		switch(step) {
		case 0:		// 选择人物页面
		{
		} break;
		case 1:
		{
			int role = getParameterInt("r");	// r = role = 角色 0-9
			synchronized(world) {
				if(world.roleUser[role] > 0) {
					tip("return", "该角色已经有人选用，请换一个试试");
				} else if(world.isFull()) {
					tip("return", "人满了，请稍后再来");
				} else if(world.hasIp(request.getRemoteAddr()) && !net.joycool.wap.util.SecurityUtil.isInnerIp(request.getRemoteAddr())) {
					tip("return", "有ip重复，请下局再来 ");
				} else {
					if(richUser.getStatus() == RichUserBean.STATUS_PLAY)	// 还在游戏中，直接破产重来
						world.setBroke(richUser);
					
					world.addUser(role, richUser);
					richUser.init();
					richUser.setNextActionTime(now - ACTION_COOL_DOWN);
					richUser.setLastActionTime(now);
					richUser.setWorldId(w);
					richUser.setRole(role);
					richUser.ip = request.getRemoteAddr();
					world.moveUser(richUser, world.getRandomEmptyNode());
				}
			}
		} break;
			
		}
	}

	/**
	 * 扔骰子
	 */
	public void go() {
		int act = getParameterInt("a");
		switch(act) {
		case 0:		// 正常显示地图
		{
			List nodeList = world.getNodes(richUser);
			setAttribute("nodeList", nodeList);
		} break;
		case 1:
		{
			richUser.addNextActionTime(ACTION_COOL_DOWN);
			richUser.setLastActionTime(now);
			
			int dice = world.go(richUser, now);
			setAttribute("dice", dice);
			
		} break;
		}
	}
	
	// 购买道具啦！
	public void shop() {
		if(richUser.isInShop()) {
			int act = getParameterInt("a");
			if(act == 0) {		// r = result
	
			} else if(act == 1){
				if(richUser.isBagFull()) {
					tip(null, "背包满了，可以卖掉一些东西再买");
				} else {
					RichShop shop = world.shop;
					int itemId = getParameterInt("i");		// 之所以要这个值，怕用户购买的时候系统刷新了物品，导致买错
					int slot = getParameterInt("s");
					RichItemBean item = shop.items[slot];
					if(item != null && item.getId() == itemId) {
						if(richUser.getMoney2() < item.getPrice())
							tip(null, "点券不足");
						else {
							shop.removeItem(slot);
							richUser.addMoney2(-item.getPrice());
							richUser.addBag(item);
							tip(null, "购买成功");
						}
					} else {
						tip(null, "该物品不存在");
					}
				}
				
			} else {
				richUser.setInShop(false);
			}
		}
	}
	
	/**
	 * 卖道具啦！价格一半……
	 */
	public void shop2() {
		if(richUser.isInShop()) {
			int act = getParameterInt("a");
			if(act == 0) {		// r = result
	
			} else if(act == 1){
				int slot = getParameterInt("s");
				if(slot < 0 || slot >= richUser.bag.size()) {
					tip(null, "卖出成功");
					return;
				}
				RichItemBean item = (RichItemBean)richUser.bag.remove(slot);
				if(item != null) {
					richUser.addMoney2(item.getPrice() / 2);
					tip(null, "卖出成功");
				} else {
					tip(null, "该物品不存在");
				}
			} else {
				richUser.setInShop(false);
			}
		}
	}
	
	//	购买商业用地
	public void business() {
		if(richUser.isInBusiness()) {
			int act = getParameterInt("a");
			if(act == 0) {		// 提示
				RichNodeBean node = world.getNode(richUser);
				if(node.canBuyHouse(richUser)) {
					if(richUser.getMoney() < node.getPrice()) {
						tip("tip", "现金不足，无法投资");
						richUser.setInBusiness(false);
					}
				}
			} else if(act == 1){
				
				RichNodeBean node = world.getNode(richUser);
				if(richUser.getMoney() < node.getPrice())
					tip("tip", "现金不足，无法投资");
				else {
					richUser.addMoney(-node.getPrice());
					if(richUser.getWith() == 6 && RandomUtil.percentRandom(50)) {
						tip("tip", "衰神显灵，投资失败……");
					} else {
						if(node.house.noOwner()) {
							node.addHouse(richUser);
							if(richUser.getWith() == 5 && RandomUtil.percentRandom(50)) {
								node.house.addLevel();
								richUser.addLog("福神显灵，帮你加盖一层");
							} else if(richUser.getWith() == 9) {
								node.house.addLevel();
								richUser.addLog("天使显灵，此地房屋升级");
							}
							tip("tip", "成功买入");
						} else if(node.house.getLevel() == 0) {
							int option = getParameterInt("o");
							if(option > 0 && option < 4) {
								node.addHouseType(richUser, option);
								tip("tip", "成功升级为" + node.house.getLevelName());
							}
						} else {
							node.addHouse(richUser);
							tip("tip", "成功升级为" + node.house.getLevelName());
						}
					}
				}
				richUser.setInBusiness(false);
			} else {
				richUser.setInBusiness(false);
			}
		}
	}
	
	//	购买房子
	public void house() {
		if(richUser.isInHouse()) {
			int act = getParameterInt("a");
			if(act == 0) {		// 提示
				RichNodeBean node = world.getNode(richUser);
				if(node.canBuyHouse(richUser)) {
					if(richUser.getMoney() < node.getPrice()) {
						tip("tip", "现金不足，买不起");
						richUser.setInHouse(false);
					}
				}
			} else if(act == 1){
				
				RichNodeBean node = world.getNode(richUser);
				if(richUser.getMoney() < node.getPrice())
					tip("tip", "现金不足，买不起");
				else {
					richUser.addMoney(-node.getPrice());
					if(richUser.getWith() == 6 && RandomUtil.percentRandom(50)) {
						tip("tip", "衰神显灵，投资失败……");
					} else {
						if(node.house.noOwner()) {
							node.addHouse(richUser);
							if(richUser.getWith() == 5 && RandomUtil.percentRandom(50)) {
								node.house.addLevel();
								richUser.addLog("福神显灵，帮你加盖一层");
							} else if(richUser.getWith() == 9) {
								node.house.addLevel();
								richUser.addLog("天使显灵，此地房屋升级");
							}
							tip("tip", "成功买入房子");
						} else {
							node.addHouse(richUser);
							tip("tip", "成功升级为" + node.house.getLevelName());
						}
					}
				}
				richUser.setInHouse(false);
			} else {
				richUser.setInHouse(false);
			}
		}
	}
	
	/**
	 * 银行
	 */
	public void bank() {
		if(richUser.isInBank()) {
			int act = getParameterInt("a");
			if(act == 0) {	
	
			} else if(act == 1){
				int save = getParameterInt("save");		// 存款
				int load = getParameterInt("load");		// 取款
				if(save != 0) {
					if(richUser.getMoney() < save) {
						tip(null, "现金不足");
					} else {
						richUser.addMoney(-save);
						richUser.addSaving(save);
						tip("success", "存款成功");
					}
				} else {
					if(richUser.getSaving() < load) {
						tip(null, "现金不足");
					} else {
						richUser.addMoney(load);
						richUser.addSaving(-load);
						tip("success", "取款成功");
					}	
				}
			} else {
				richUser.setInBank(false);
			}
		}
	}

	/**
	 * 使用道具
	 */
	public void bag() {
		int act = getParameterInt("a");
		if(act == 0) {		// 提示
			
		} else {
			int used = 0;	// 0 not use, 1 used, 2 error
			
			int slot = getParameterIntS("s");
			if(slot < 0 || slot >= richUser.bag.size()) {
				tip("success", "物品不存在");
				return;
			}
			if(richUser.isBroke()) {
				tip("success", "已破产");
				return;
			}
			RichItemBean item = (RichItemBean)richUser.bag.get(slot);
			setAttribute("item", item);
	
			if(act == 1){
				switch(item.getId()) {
				case 1:		// 遥控骰子
				{
					int option = getParameterInt("o");		// 走几步 1-6
					if(option > 0 && option < 7) {
						richUser.setDice(option);
						used = 1;
					}
				} break;
				case 2:		// 路障卡
				{
					int option = getParameterInt("o");	// 扔路障的位置
					if(option > 0) {
						RichNodeBean node = world.getNearbyNode(richUser, option);
						if(node != null && node.putItem()) {
							node.setEvent(1);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 3:		// 转向卡
				{
					int option = getParameterInt("o");	// 选择的对象
					if(option > 0) {
						RichUserBean target = getNearbyUser(option);
						if(target != null) {
							target.reverse();
							if(target != richUser)
								target.addLog(richUser.getWmlName() + "对你使用了转向卡");
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 4:		// 停留卡
				{
					int option = getParameterInt("o");	// 选择的对象
					if(option > 0) {
						RichUserBean target = getNearbyUser(option);
						if(target != null) {
							richUser.setDice(0);
							if(target != richUser)
								target.addLog(richUser.getWmlName() + "对你使用了停留卡");
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 5:		// 机器娃娃
				{
					RichNodeBean node = world.getNode(richUser);
					for(int i = 0;i < 10;i++) {
						if(richUser.isReverse())
							node = node.randomPrev();
						else
							node = node.randomNext();
						if(node.getEventType() == 1)	// 神仙！
							world.addEvent(node.getEvent());	
						node.setEvent(0);
					}
					used = 1;
				} break;
				case 6:		// 乌龟卡
				{
					int option = getParameterInt("o");	// 选择的对象
					if(option > 0) {
						RichUserBean target = getNearbyUser(option);
						if(target != null) {
							if(target != richUser)
								target.addLog(richUser.getWmlName() + "对你使用了乌龟卡");
						
							target.setDice(1);
							target.addDice(1);
							target.addDice(1);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 7:		// 地雷
				{
					int option = getParameterInt("o");	// 扔的位置
					if(option > 0) {
						RichNodeBean node = world.getNearbyNode(richUser, option);
						if(node != null && node.putItem()) {
							node.setEvent(2);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 8:		// 定时炸弹
				{
					int option = getParameterInt("o");	// 扔的位置
					if(option > 0) {
						RichNodeBean node = world.getNearbyNode(richUser, option);
						if(node != null && node.putItem()) {
							node.setEvent(3);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 9:		// 机器工人
				{
					int option = getParameterInt("o");	// 扔的位置
					if(option > 0) {
						RichNodeBean node = world.getNearbyNode(richUser, option);
						if(node != null && node.isUserOwner(richUser) && !node.house.isMaxLevel()) {
							node.getHouse().addLevel();
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 10:		// 换屋卡
				{
					int option = getParameterInt("o");	// 位置
					if(option > 0) {
						RichNodeBean node2 = world.getNearbyNode(richUser, option);
						RichNodeBean node = world.getNode(richUser);
						if(node2 != null && node.isUserOwner(richUser) && node2.isOtherUserOwner(richUser) && node.isHouse() && node2.isHouse()) {
							int tmp = node.house.getLevel();
							node.house.setLevel(node2.house.getLevel());
							node2.house.setLevel(tmp);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 11:		// 换地卡
				{
					int option = getParameterInt("o");	// 位置
					if(option > 0) {
						RichNodeBean node2 = world.getNearbyNode(richUser, option);
						RichNodeBean node = world.getNode(richUser);
						if(node2 != null && node.isUserOwner(richUser) && node2.isOtherUserOwner(richUser) && node.isHouse() && node2.isHouse()) {
							HouseBean tmp = node.house;
							node.house = node2.house;
							node2.house = tmp;
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 12:		// 购地卡
				{
					RichNodeBean node = world.getNode(richUser);
					if(node.isOtherUserOwner(richUser)) {
						node.house.setOwner(richUser);
						used = 1;
					} else {
						used = 2;
					}
				} break;
				case 13:		// 拆除卡
				{
					int option = getParameterInt("o");	// 位置
					if(option > 0) {
						RichNodeBean node = world.getNearbyNode(richUser, option);
						if(node != null && node.isOtherUserOwner(richUser) && node.house.getLevel() > 0) {
							node.house.addLevel(-1);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 14:		// 怪兽卡
				{
					int option = getParameterInt("o");	// 位置
					if(option > 0) {
						RichNodeBean node = world.getNearbyNode(richUser, option);
						if(node != null && node.isOtherUserOwner(richUser)) {
							node.house.setLevel(0);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 15:		// 恶魔卡
				{
					int option = getParameterInt("o");	// 位置
					if(option > 0) {
						RichNodeBean node = world.getNearbyNode(richUser, option);
						if(node != null && node.getType() == RichNodeBean.TYPE_HOUSE) {
							node.getBuilding().addLevel(-5);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 16:		// 天使卡
				{
					int option = getParameterInt("o");	// 位置
					if(option > 0) {
						RichNodeBean node = world.getNearbyNode(richUser, option);
						if(node != null && node.getType() == RichNodeBean.TYPE_HOUSE) {
							node.getBuilding().addLevel(1);
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 17:		// 均贫卡
				{
					int option = getParameterInt("o");	// 选择的对象
					if(option > 0) {
						RichUserBean target = getNearbyUser(option);
						if(target != null && target != richUser) {
							int tmp = (target.getMoney() + richUser.getMoney()) / 2;
							target.setMoney(tmp);
							richUser.setMoney(tmp);
							target.addLog(richUser.getWmlName() + "对你使用了均贫卡，与你平分现金");
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 18:		// 请神符
				{
					List nodes = world.getNodes(richUser);
					for(int i = 0;i < nodes.size();i++) {
						RichNodeBean node = (RichNodeBean)nodes.get(i);
						if(node.getEvent() != 0 && node.getEventType() == 1) {
							world.getEvent(richUser, node.getEvent());
							node.setEvent(0);
							used = 1;
							break;
						}
					}
					if(used == 0)
						used = 2;
				} break;
				case 19:		// 送神符
				{
					if(richUser.getWith() != 0) {
						if(richUser.getWithType() == 1)		// 神仙离开后跑到另一个随机的地方
							world.addEvent(richUser.getWith());	
						richUser.setWith(0);
						used = 1;
					} else
						used = 2;
				} break;
				case 20:		// 抢夺卡
				{
					int option = getParameterInt("o");	// 选择的对象
					if(option > 0) {
						RichUserBean target = getNearbyUser(option);
						if(target != null && target != richUser) {
							setAttribute("target", target);
							int option2 = getParameterIntS("p");		// 抢夺第几个
							if(option2 >= 0 && option2 < target.bag.size()) {
								RichItemBean itemGet = (RichItemBean)target.bag.remove(option2);
								if(itemGet != null) {
									richUser.addBag(itemGet);
									target.addLog(richUser.getWmlName() + "使用抢夺卡抢走了你的" + itemGet.getName());
									used = 1;
								} else {
									used = 2;
								}
							}
						} else {
							used = 2;
						}
					}
				} break;
				case 21:		// 陷害卡
				{
					int option = getParameterInt("o");	// 选择的对象
					if(option > 0) {
						RichUserBean target = getNearbyUser(option);
						if(target != null && target != richUser) {
							target.noControl();
							world.moveUser(target, (RichNodeBean)RandomUtil.randomObject(world.jailNodes));
							target.lockAction(now + 30000, 1);
							target.addLog("您被" + richUser.getWmlName() + "陷害，关进监狱，须停留30秒之后才能继续前行。");
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				case 22:		// 飞弹
				{
					int option = getParameterInt("o");	// 选择的对象，可以是任意玩家
					if(option > 0) {
						RichUserBean target = getRichUser(option);
						if(target != null && target != richUser) {
							target.noControl();
							world.moveUser(target, (RichNodeBean)RandomUtil.randomObject(world.hospitalNodes));
							target.lockAction(now + 40000, 2);
							target.addLog(richUser.getWmlName() + "用飞弹把你打成重伤，进了医院，须停留40秒之后才能继续前行。");
							used = 1;
						} else {
							used = 2;
						}
					}
				} break;
				}
			} else if(act == 2) {		// 丢弃一个物品
				if(item != null) {
					richUser.bag.remove(slot);
					tip("success", "丢弃卡片:" + item.getName());
				}
			}
			if(used == 1) {
				richUser.bag.remove(slot);
				tip("success", "成功使用" + item.getName());
			} else if (used == 2){
				tip("success", "无法使用" + item.getName());
			}
		}
	}

	public UserBean getLoginUser() {
		return loginUser;
	}
	
	// 获取本局内的玩家
	public RichUserBean getRichUser(int userId) {
		RichUserBean target = RichWorld.getRichUser(userId, false);
		if(target.getWorldId() == richUser.getWorldId())
			return target;
		return null;
	}
	
	// 获取视野内的玩家
	public RichUserBean getNearbyUser(int userId) {
		if(richUser.getUserId() == userId)
			return richUser;
		List nearby = world.getNearbyUser(richUser);
		for(int i = 0;i < nearby.size();i++) {
			RichUserBean u = (RichUserBean)nearby.get(i);
			if(u.getUserId() == userId)
				return u;
		}
		return null;
	}

	public RichUserBean getRichUser() {
		return richUser;
	}
	
	public long getDelay() {
		return richUser.getNextActionTime() - now;
	}
	
	public void map() {
		int pos = getParameterInt("n");	//  展开的结点
		if(pos == 0)
			pos = richUser.getPosition();
		setAttribute("pos", pos);
		List nodeList = world.getNodes(pos);
		setAttribute("nodeList", nodeList);
	}
	
	static int[] int2(int a, int b) {
		int[] c = {a,b};
		return c;
	}
	
	static List[] changeMaterials = new List[10];
	static {
		for(int i = 0;i < changeMaterials.length;i++)
			changeMaterials[i] = new ArrayList();
		changeMaterials[1].add(int2(68, 4));
		changeMaterials[2].add(int2(68, 10));
		changeMaterials[3].add(int2(68, 10));
		changeMaterials[4].add(int2(73, 16));
		changeMaterials[5].add(int2(73, 2));
		changeMaterials[6].add(int2(73, 5));
		changeMaterials[7].add(int2(73, 10));
		changeMaterials[8].add(int2(68, 8));
	}
	// 大富翁券兑换场
	public void change() {
		int option = getParameterInt("o");
		boolean success = false;
		List mat = null;
		if(option > 0 && option < changeMaterials.length)
			mat = changeMaterials[option];
		int[] nullPro = null;
		synchronized(loginUser.getLock()) {
			switch(option) {
			case 1: {	// 大富翁券兑换乐币
				if(UserBagCacheUtil.composeDirectly(loginUser.getId(), mat, nullPro) > 0) {
					success = true;
					BankCacheUtil.updateBankStoreCacheById(1000000000l, loginUser.getId(),0,Constants.BANK_RICH_TYPE);
					UserInfoUtil.getMoneyStat()[Constants.BANK_RICH_TYPE] += 1000000000l;
					tip("tip", "兑换成功，资金已转帐到银行。");
				}
			} break;
			case 2: {	// 大富翁券兑换乐币
				if(UserBagCacheUtil.composeDirectly(loginUser.getId(), mat, nullPro) > 0) {
					success = true;
					BankCacheUtil.updateBankStoreCacheById(3000000000l, loginUser.getId(),0,Constants.BANK_RICH_TYPE);
					UserInfoUtil.getMoneyStat()[Constants.BANK_RICH_TYPE] += 3000000000l;
					tip("tip", "兑换成功，资金已转帐到银行。");
				}
			} break;
			case 3: {	// 大富翁券兑换奖牌
				int[] pro = {73};
				if(UserBagCacheUtil.composeDirectly(loginUser.getId(), mat, pro) > 0) {
					success = true;
					tip("tip", "兑换成功，物品已放入行囊。");
				}
			} break;
			case 4: {	// 奖牌兑换卡
				int[] pro = {69};
				if(UserBagCacheUtil.composeDirectly(loginUser.getId(), mat, pro) > 0) {
					success = true;
					tip("tip", "兑换成功，物品已放入行囊。");
				}
			} break;
			case 5: {	// 奖牌兑换乐币
				if(UserBagCacheUtil.composeDirectly(loginUser.getId(), mat, nullPro) > 0) {
					success = true;
					BankCacheUtil.updateBankStoreCacheById(6600000000l, loginUser.getId(),0,Constants.BANK_RICH_TYPE);
					UserInfoUtil.getMoneyStat()[Constants.BANK_RICH_TYPE] += 6600000000l;
					tip("tip", "兑换成功，资金已转帐到银行。");
				}
			} break;
			case 6: {	// 奖牌兑换乐币
				if(UserBagCacheUtil.composeDirectly(loginUser.getId(), mat, nullPro) > 0) {
					success = true;
					BankCacheUtil.updateBankStoreCacheById(18000000000l, loginUser.getId(),0,Constants.BANK_RICH_TYPE);
					UserInfoUtil.getMoneyStat()[Constants.BANK_RICH_TYPE] += 18000000000l;
					tip("tip", "兑换成功，资金已转帐到银行。");
				}
			} break;
			case 7: {	// 奖牌兑换乐币
				if(UserBagCacheUtil.composeDirectly(loginUser.getId(), mat, nullPro) > 0) {
					success = true;
					BankCacheUtil.updateBankStoreCacheById(40000000000l, loginUser.getId(),0,Constants.BANK_RICH_TYPE);
					UserInfoUtil.getMoneyStat()[Constants.BANK_RICH_TYPE] += 40000000000l;
					tip("tip", "兑换成功，资金已转帐到银行。");
				}
			} break;
			case 8: {	// 奖牌高级荣誉卡
				int[] pro = {72};
				if(UserBagCacheUtil.composeDirectly(loginUser.getId(), mat, pro) > 0) {
					success = true;
					tip("tip", "兑换成功，物品已放入行囊。");
				}
			} break;
			default:
				success = true;	// 普通显示
			}
		}
		if(!success) {
			tip("tip", "兑换失败");
		}
	}
		
	// 10秒跑一次
	public static void timer() {
		long now = System.currentTimeMillis();
		for(int i = 0;i < worlds.length;i++) {
			RichWorld world = worlds[i];
			if(world.loaded) {
				world.timer(now);
			}
		}
	}
	// 服务器重启的时候用于瞬间结束所有比赛
	public static void shutdown() {
		long now = System.currentTimeMillis();
		for(int i = 0;i < worlds.length;i++) {
			RichWorld world = worlds[i];
			if(world.loaded) {
				world.reward();
				world.reset(now);
			}
		}
	}
}
