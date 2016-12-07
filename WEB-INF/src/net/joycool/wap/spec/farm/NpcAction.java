package net.joycool.wap.spec.farm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.SqlUtil;

/**
 * @author zhouj
 * @explain： 买卖物品等
 * @datetime:1007-10-24
 */
public class NpcAction extends CustomAction{

	public static NpcService service = new NpcService();
	public static FarmNpcWorld world = FarmNpcWorld.one;
	public static FarmWorld farmWorld = FarmWorld.one;
	
	UserBean loginUser;
	
	FarmUserBean farmUser = null;
	public static long now = 0;
	
	public static long USER_INACTIVE = 30 * 1000 * 60;		// 用户无动作时间

	public NpcAction(HttpServletRequest request) {
		super(request);
		check(); // check user
	}
	
	public NpcAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		check(); // check user
	}

	public void check() {
		loginUser = super.getLoginUser();
		farmUser = farmWorld.getFarmUser(loginUser.getId());
		FarmAction.now = now = System.currentTimeMillis();
	}

	public UserBean getLoginUser() {
		return loginUser;
	}
	
	public FarmUserBean getUser() {
		return farmUser;
	}

	public void npc() {
		int npcId = getParameterInt("id");
		if(npcId > 0)
			farmUser.setNpcId(npcId);
		else
			npcId = farmUser.getNpcId();

		if(npcId > 0) {
			FarmNpcBean npc = world.getNpc(npcId);
			if(npc != null && npc.getPos() == farmUser.getPos() && !npc.isFlagHide()) {
				request.setAttribute("npc", npc);
				if(farmUser.getName().length() != 0)
					TriggerUtil.npcTrigger.trigger(npc.getId(), farmUser);
			}
		}
	}
	// 出售物品列表
	public void sells() {
		int npcId = farmUser.getNpcId();
		if(npcId > 0) {
			setAttribute("sells", world.getShopMap(npcId));
		}
	}
	// 购买物品列表
	public void buys() {
		int npcId = farmUser.getNpcId();
		if(npcId > 0)
			setAttribute("buys", world.getShopList(npcId));
	}
	
	public FarmShopBean getNpcSellByItem(int itemId) {
		Map map = world.getShopMap(farmUser.getNpcId());
		if(map == null)
			return null;
		return (FarmShopBean)map.get(Integer.valueOf(itemId));
	}
	
	// 卖出单个
	public void sell() {
		int id = getParameterInt("id");	// userBagId
		synchronized(loginUser.getLock()) {
			UserBagBean userBag = UserBagCacheUtil.getUserBagCache(id);
			if (userBag == null || userBag.getUserId() != loginUser.getId()) {
				tip("tip", "行囊中没有该商品");
				return;
			}
			setAttribute("userBag", userBag);

			FarmShopBean sell = getNpcSellByItem(userBag.getProductId());
			if (sell == null || sell.isFlagNoSell() || sell.getNpcId() != farmUser.getNpcId()) {
				tip("tip", "无法出售该物品!");
				return;
			}
			setAttribute("sell", sell);
			
			int act = getParameterInt("a");
			if(act == 1) {		// 直接出售
				synchronized(farmUser.getLock()) {
					if(!sell.canIncStack(userBag.getTime())) {
						tip("tip", "无法出售,对方库存太多.");
						return;
					}
					int money = sell.getSellPrice() * userBag.getTime();
					FarmWorld.addMoney(farmUser, money);
					sell.addStack(userBag.getTime());
					UserBagCacheUtil.updateUserBagCacheById("time=0",
							"id=" + id, loginUser.getId(), id);
					tip("tip", "出售成功!得到" + FarmWorld.formatMoney(money));
				}
			}
		}
	}
	
	// 卖出单个装备
	public void sellEquip() {
		int id = getParameterInt("id");	// userBagId
		int npcId = farmUser.getNpcId();
		FarmNpcBean npc = world.getNpc(npcId);
		if(npc == null || !npc.isFlagEquipSell()) {
			tip("tip", "无法出售该物品!");
			return;
		}
		synchronized(loginUser.getLock()) {
			UserBagBean userBag = UserBagCacheUtil.getUserBagCache(id);
			if (userBag == null || userBag.getUserId() != loginUser.getId()) {
				tip("tip", "行囊中没有该商品");
				return;
			}
			setAttribute("userBag", userBag);
			DummyProductBean item = FarmWorld.getItem(userBag.getProductId());
			if(item == null || item.getClass1() == 0 || item.getClass1() == 10) {
				tip("tip", "无法出售该物品!");
				return;
			}
			int act = getParameterInt("a");
			if(act == 1) {		// 直接出售
				synchronized(farmUser.getLock()) {
					int money = item.getPrice() * userBag.getTime() / item.getTime();
					FarmWorld.addMoney(farmUser, money);
					UserBagCacheUtil.updateUserBagCacheById("time=0",
							"id=" + id, loginUser.getId(), id);
					tip("tip", "出售成功!得到" + FarmWorld.formatMoney(money));
				}
			}
		}
	}
	
	public void buy() {
		int id = getParameterInt("id");
		if(id > 0) {
			FarmShopBean bean = world.getShop(id);
			if(bean == null || bean.isFlagNoBuy() || bean.getNpcId() != farmUser.getNpcId()) {
				tip("tip", "不存在的物品!");
				return;
			}
			setAttribute("buy", bean);
			
			int act = getParameterInt("a");
			if(act == 1) {		// 直接购买
				int count = getParameterInt("cnt");
				if(count < 1 || count > 50)
					count = 1;
				synchronized(farmUser.getLock()) {
					if(!bean.canDecStack(count)) {
						tip("tip", "购买失败,对方库存不足.");
						return;
					}
					int price = bean.getBuyPrice() * count;
					if(farmUser.getMoney() >= price) {
						FarmWorld.addMoney(farmUser, -price);
						UserBagCacheUtil.addUserBagCacheStack(farmUser.getUserId(), bean.getItemId(), count);
						bean.addStack(-count);
						tip("tip", "购买成功!花费" + FarmWorld.formatMoney(price));
					} else {
						tip("tip", "购买失败,没有足够的钱!");
					}
				}
			}
		}
	}
	
	// 学习技能
	public void skills() {
		int npcId = farmUser.getNpcId();
		FarmNpcBean npc = world.getNpc(npcId);
		if(npc != null && npc.getPos() == farmUser.getPos())
			setAttribute("skills", npc.getLearnSkillList());
	}
	
	public void skill() {
		int id = getParameterInt("id");
		if(id > 0) {
			FarmSkillBean bean = farmWorld.getSkill(id);
			setAttribute("skill", bean);
			
			FarmUserProBean pro = farmUser.getPro(bean.getProId());
			setAttribute("pro", pro);
			
			int act = getParameterInt("a");
			if(act == 1) {		// 直接购买
				if(pro == null) {
					tip("tip", "无法学习，没有该专业!");
					return;
				}
				if(bean.getClass1() > 0 && bean.getClass1() != farmUser.getClass1()) {
					tip("tip", "无法学习，不是相应的职业!");
					return;
				}
				synchronized(farmUser.getLock()) {
					int price = bean.getPrice();
					if(farmUser.getMoney() >= price) {
						FarmWorld.addMoney(farmUser, -price);
						FarmWorld.addSkill(pro, id);
						tip("tip", "学会了[技能]" +bean.getName());
					} else {
						tip("tip", "学习失败,没有足够的钱!");
					}
				}
			}
		}
	}
	
	// 显示所有任务
	public void quests() {
		int npcId = farmUser.getNpcId();
		FarmNpcBean npc = world.getNpc(npcId);
		if(npc != null && npc.getPos() == farmUser.getPos())
			setAttribute("npc", npc);
	}
	
	// 查看、提交、接受任务
	public void questBegin() {
		int id = getParameterInt("id");
		FarmQuestBean quest = world.getQuest(id);
		FarmNpcBean npc = world.getNpc(farmUser.getNpcId());
		if(quest == null || npc == null || !npc.hasQuestBegin(id) || quest.isFlagClosed()) {
			tip("tip", "不存在的任务!");
			return;
		}
		setAttribute("quest", quest);
		int act = getParameterInt("a");
		if(act == 1) {		// 接收任务
			synchronized(farmUser.getLock()) {
				if(farmUser.isQuestStart(id)){
					tip("tip", "已经有这个任务了!");
				} else if(!farmUser.canDoQuest(quest, now)){
					tip("tip", "这个任务已经完成!");
				} else if(farmUser.getQuests().size() >= 5) {
					tip("tip", "任务接收失败，超过同时最大任务数!");
				} else if(!FarmNpcWorld.canDoQuest(farmUser, quest)){
					tip("tip", "还没达到做该任务的条件!");
				} else {
					UserBagCacheUtil.addProductINE(farmUser.getUserId(), quest.getGiveList());
					world.addUserQuest(farmUser, quest);
					farmUser.addQuestFinishStatus(quest);
					String tip = "你接受了[任务]" + quest.getName(); 
					tip("tip", tip);
					farmUser.addLog(tip);
					farmUser.flushNpcMark();
				}
			}
		}
	}
	
	public void questEnd() {
		int id = getParameterInt("id");
		FarmUserQuestBean userQuest = world.getUserQuest(id);
		if(userQuest == null) {
			tip("tip", "任务已中止!");
			return;
		}
		FarmQuestBean quest = world.getQuest(userQuest.getQuestId());
		
		FarmNpcBean npc = world.getNpc(farmUser.getNpcId());
		if(quest == null || npc == null || !npc.hasQuestEnd(userQuest.getQuestId())) {
			tip("tip", "不存在的任务!");
			return;
		}
		
		setAttribute("userQuest", userQuest);
		setAttribute("quest", quest);
		int act = getParameterInt("a");
		synchronized(farmUser.getLock()) {
			if(act == 0 || act == 1) {		// 提交任务
				if(userQuest.getStatus() == 0
						&& UserBagCacheUtil.checkMaterial(quest.getMaterialList(), farmUser.getUserId()) 
						&& farmUser.getMoney() >= quest.getPrice()
						&& FarmNpcWorld.checkQuestFinish(farmUser, quest)
						&& FarmWorld.isCondition(quest.getConditionList(), farmUser)) {
					if(quest.getPrice() > 0)
						FarmWorld.addMoney(farmUser, -quest.getPrice());
					UserBagCacheUtil.removeMaterial(quest.getMaterialList(), farmUser.getUserId());
					UserBagCacheUtil.addProduct(farmUser.getUserId(), quest.getProductList());
					List prize = quest.getPrizeList();
					FarmWorld.doAction(prize, farmUser);
					
					world.endUserQuest(farmUser, userQuest);
					farmUser.removeQuestFinishStatus(quest);
					userQuest.setDoneTime(now);
					String tip = "你完成了[任务]" + quest.getName(); 
					//tip("tip", tip);
					farmUser.addLog(tip);
					farmUser.flushNpcMark();
					
					TriggerUtil.questTrigger.trigger(quest.getId(), farmUser);
				} else {
					//tip("tip", "任务还没有完成!");
				}
			} else if(act == 2) {	// 拒绝任务
				if(userQuest.getStatus() == 0 && !quest.isFlagNoAbort()) {
					world.removeUserQuest(farmUser, userQuest);
					farmUser.removeQuestFinishStatus(quest);
					String tip = "放弃了[任务]" + quest.getName(); 
					tip("tip", tip);
					farmUser.addLog(tip);
					farmUser.flushNpcMark();
				} else {
					tip("tip", "无法放弃该任务!");
				}
			}
		}
	}
	
	// 进入加工厂
	public void factory() {
		int factoryId = getParameterInt("id");
		if(factoryId > 0)
			farmUser.setFactoryId(factoryId);
		else
			factoryId = farmUser.getFactoryId();

		if(factoryId > 0) {
			FactoryBean factory = world.getFactory(factoryId);
			request.setAttribute("factory", factory);
		}
	}
	
	// 得到当前玩家所在的工厂
	public FactoryBean getUserFactory() {
		int factoryId = farmUser.getFactoryId();
		if(factoryId > 0)
			return world.getFactory(factoryId);
		return null;
	}
	
	// 列表显示这个工厂的加工公式
	public void factoryComposes() {

	}
	
	// 显示这个工厂的某个加工公式
	public void factoryCompose() {
		int factoryId = farmUser.getFactoryId();
		int composeId = getParameterInt("id");
		
		if(composeId > 0) {
			FactoryComposeBean compose = world.getFactoryCompose(composeId);
			if(compose == null || compose.getFactoryId() != factoryId) {
				tip("tip", "不存在的加工");
			} else {
				setAttribute("compose", compose);
				if(hasParam("c")) {		// 直接加工
					synchronized(loginUser.getLock()) {
						if(UserBagCacheUtil.checkRemoveMaterial(compose.getMaterialList(), farmUser.getUserId())) {
							if(farmUser.getMoney() < compose.getPrice()) {
								tip("tip", "加工失败,没有足够的铜板");
							} else if(SqlUtil.getIntResult("SELECT count(id) from farm_factory_proc WHERE user_id=" + farmUser.getUserId() + " and status!=2", 4) >= 5) {
								tip("tip", "还有一些订单未完成或未领取，请稍后再来");
							} else {
								FarmWorld.addMoney(farmUser, -compose.getPrice());
								world.addFactoryProc(compose, farmUser.getUserId());
								tip("tip", "加工开始了,会尽快完成,请耐心等一会再来!");
							}
							
						} else {
							tip("tip", "加工失败,没有足够的材料");
						}
					}
				} else {				// 显示加工详情
					
				}
			}
		}
	}
	
	// 领取产品的列表
	public void factoryProducts() {
		int factoryId = farmUser.getFactoryId();
		List procList = service.getFactoryProcList("user_id=" + farmUser.getUserId() + " and factory_id=" + factoryId + " and status=1 order by id limit 5");
		setAttribute("procList", procList);
	}
	
	public void factoryProduct() {
		int factoryId = farmUser.getFactoryId();
		int procId = getParameterInt("id");
		
		if(procId > 0) {
			FactoryProcBean proc = world.getFactoryProc(procId);
			setAttribute("proc", proc);
			if(proc == null || proc.getFactoryId() != factoryId || proc.getUserId() != farmUser.getUserId() ||
					proc.getStatus() != 1) {
				tip("tip", "不存在的加工");
			} else {
				setAttribute("proc", proc);
				if(hasParam("c")) {		// 直接领取
					FactoryComposeBean compose = world.getFactoryCompose(proc.getComposeId());
					synchronized(loginUser.getLock()) {
						if(proc.getStatus() == 2) {
							tip("tip", "领取成功");
						} else {
							UserBagCacheUtil.addProduct(farmUser.getUserId(), compose.getProductList());
							SqlUtil.executeUpdate("update farm_factory_proc set status=2 where id=" + proc.getId(), 4);
							proc.setStatus(2);
							tip("tip", "领取成功");
						}
					}
				} else {				// 显示加工详情
					
				}
			}
		}
	}
	
	// 与npc聊天
	public void talk() {

	}
	// 查看怪物（可能是其他）
	public void creature() {
		int id = getParameterInt("id");
		if(id > 0) {
			FarmObject obj = world.getObject(id);
			if(!(obj instanceof CreatureBean))
				return;
			CreatureBean creature = (CreatureBean)obj;
			if(creature != null && creature.getPos() == farmUser.getPos())
				request.setAttribute("creature", creature);
		}
	}
	
	// 玩家点一个石头
	public void stone() {
		int id = getParameterInt("id");
		FarmStoneBean stone = world.getStone(id);
		if(stone == null || stone.getPos() != farmUser.getPos()) {
			tip("tip", "不存在的物体");
			return;
		}
		if(farmUser.getTargetList().size() > 0) {
			tip("tip", "战斗中无法使用");
			return;
		}
		switch(stone.getType()) {
		case 1: {		// 灵魂绑定，死后在这里复活
			FarmNpcWorld.updateUserBindPos(farmUser);
			tip("tip", "石头突然变得透明,像镜子一样反射出你的身影");
		} break;
		case 2: {		// 飞行石，可以随时飞到这里，最后设定的三个有效
			tip("tip", "石头周围的一切,突然都印在脑海中了");
		} break;
		case 3: {		// 加满血
			farmUser.incHp(1.0f);
			tip("tip", "你感觉到又获得了新生");
		} break;
		case 4: {		// 加满气力
			farmUser.incMp(1.0f);
			tip("tip", "你感觉到了无穷的力量");
		} break;
		case 5: {		// 加满体力
			farmUser.incSp(1.0f);
			tip("tip", "在一瞬之间,你感觉到体力又充沛了");
		} break;
		default:
			tip("tip", "石头闪烁了一下,什么事情也没有发生");
		}
	}
	
	// 修理装备
	public void repair() {
		int npcId = farmUser.getNpcId();
		FarmNpcBean npc = world.getNpc(npcId);
		if(npc == null || !npc.isFlagRepair()) {
			tip("tip", "无法修理装备!");
			return;
		}
		setAttribute("npc", npc);
		int option = getParameterInt("o");
		if(option > 0) {		// 修理某个装备
			UserBagBean userBag = UserBagCacheUtil.getUserBagCache(option);
			DummyProductBean item = FarmWorld.getItem(userBag.getProductId());
			int price = FarmWorld.getRepairMoney(item, userBag.getTime());
			
			synchronized(farmUser.getLock()) {
				if(farmUser.getMoney() < price) {
					tip("tip", "没有足够的钱来修理该装备");
				} else if(price == 0 || item.getClass1() == 0 || item.getClass1() > 2) {
					tip("tip", "此装备不需要修理");
				} else {
					UserBagCacheUtil.updateUserBagTime(userBag, item.getTime());
					FarmWorld.addMoney(farmUser, -price);
					farmUser.resetCurStat();
					tip("tip", "修理完毕,修理共花费" + FarmWorld.formatMoney(price));
				}
			}
			
		} else if(option == -2) {		// 修理所有装备
			int price = 0;
			FarmUserEquipBean[] equips = farmUser.getEquip();
			List list = new ArrayList(8);
			for(int i = 0;i < equips.length;i++) {
				FarmUserEquipBean equip = equips[i];
				if(equip != null && equip.getUserbagId() != 0) {
					UserBagBean userBag = UserBagCacheUtil.getUserBagCache(equip.getUserbagId());
					DummyProductBean item = FarmWorld.getItem(userBag.getProductId());
					int price2 = FarmWorld.getRepairMoney(item, userBag.getTime());
					if(price2 > 0) {
						price += price2;
						Object[] os = {userBag, item};
						list.add(os);
					}
				}
			}
			synchronized(farmUser.getLock()) {
				if(farmUser.getMoney() < price) {
					tip("tip", "没有足够的钱来修理装备");
				} else if(price == 0) {
					tip("tip", "装备不需要修理");
				} else {
					FarmWorld.addMoney(farmUser, -price);
					for(int i = 0;i < list.size();i++) {
						Object[] os = (Object[]) list.get(i);
						UserBagBean userBag = (UserBagBean) os[0];
						DummyProductBean item = (DummyProductBean) os[1];
						UserBagCacheUtil.updateUserBagTime(userBag, item.getTime());
					}
					farmUser.resetCurStat();
					tip("tip", "修理完毕,修理共花费" + FarmWorld.formatMoney(price));
				}
			}
		}
	}
	// 购买收藏盒
	public void collect() {
		int npcId = farmUser.getNpcId();
		FarmNpcBean npc = world.getNpc(npcId);
		if(npc == null || !npc.isFlagCollect()) {
			tip("tip", "无法购买该物品!");
			return;
		}
		int id = getParameterInt("id");
		CollectBean collect = farmWorld.getCollect(id);
		if(collect == null || collect.getRank() > farmUser.getRank()) {
			tip("tip", "不存在的物品");
			return;
		}
		request.setAttribute("collect", collect);
		int act = getParameterInt("a");
		if(act == 1) {		// 直接出售
			synchronized(farmUser.getLock()) {
				int ucId = FarmWorld.getUserCollect(farmUser.getUserId(), id);
				if(ucId == 0) {
					if(farmUser.getMoney() < collect.getPrice()) {
						tip("tip", "你还没有足够的钱购买收藏盒");
						return;
					}
					FarmWorld.addMoney(farmUser, -collect.getPrice());
					FarmWorld.addUserCollect(farmUser.getUserId(), id);
				}
				tip("tip", "获得了[收藏盒]" + collect.getName());
			}
		}
	}
	// 驿站车辆选择
	public void car() {
		int id = getParameterInt("id");
		FarmCarBean car = world.getCar(id);
		if(car == null
				|| car.getQuestId() > 0 && !farmUser.isQuestEnd(car.getQuestId())
				|| car.getStartPos() != farmUser.getPos()) {
			tip("tip", "不存在的线路!");
			return;
		}
		setAttribute("car", car);
		int act = getParameterInt("a");
		if(act == 1) {		// 直接乘坐
			synchronized(farmUser.getLock()) {
				if(farmUser.getMoney() < car.getMoney()) {
					tip("tip", "你还没有足够的钱乘坐");
					return;
				}
				FarmWorld.addMoney(farmUser, -car.getMoney());
				farmUser.startCar(car);
				FarmWorld.nodeRemovePlayer(farmUser);
			}
		}
	}
	// 驿站车辆列表
	public void cars() {
		int npcId = farmUser.getNpcId();
		FarmNpcBean npc = world.getNpc(npcId);
		if(npc != null)
			setAttribute("cars", npc.getCarList());
		else
			tip("tip", "不存在的目标");
	}
}