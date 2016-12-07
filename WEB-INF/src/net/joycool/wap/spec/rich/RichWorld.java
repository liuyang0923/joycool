package net.joycool.wap.spec.rich;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author zhouj
 * @explain： 大富翁
 * @datetime:1007-10-24
 */
public class RichWorld {
	public static RichService service = new RichService();
	public static IMessageService messageService = ServiceFactory.createMessageService();
	
	public static HashMap userMap = new HashMap();		// 用户map
	public SimpleGameLog log = new SimpleGameLog();
	public List userList = new ArrayList();		// 保存这个世界的用户
	
	HashMap mapMap = null;		// 所有的节点map
	List mapList = null;	// 所有结点列表
	
	int flag;			// (按位)0 正常 1 不给奖励  2 破产后可以重来
	int calendar;		// 感恩节等事件的记录，过了一个就+1
	
	public HashMap buildingMap = null;	// 建筑物街道等map

	public RichShop shop = new RichShop();
	public RichGame game = new RichGame();
	
	public boolean loaded = false;		// 是否已经初始化
	
	public List jailNodes = new ArrayList();		// 所有的监狱
	public List hospitalNodes = new ArrayList();	// 所有的医院
	public List startNodes = new ArrayList();		// 所有的出发点，其实就是空地
	public List businessBuilding = new ArrayList();		// 商业建筑
	
	public int maxPlayer = 6;
	public int userCount = 0;
	
	public int[] roleUser = new int[10];		// 每个角色对应的玩家 
	
	public long startTime = 0;				// 本局开始时间
	public int interval = 60 * 60;		// 一局的最长时间
	
	public static int rewardCard100 = 20000;	// 大于这个值一定获得大富翁券\
	public static int minHouseValue = 1000;	// 超过这个房产才有卡拿
	
	public static int viewPort = 7;
	
	public int resetCount = 0;
	
	// 初始设置设置
	public RichWorld(int interval, int maxPlayer, int flag) {
		setInterval(interval);
		setMaxPlayer(maxPlayer);
		this.flag = flag;
	}
	
	/**
	 * 确认已经载入world必要数据
	 */
	byte[] lock = new byte[0];
	public void prepare() {
		if(!loaded) {
			synchronized(lock) {
				if(!loaded) {
					startTime = System.currentTimeMillis();		// 设置这个世界的开始时间
					loadMap();
					loaded = true;
				}
			}
		}
	}
	
	//	载入地图
	public void loadMap() {
		mapMap = service.getMapMap("1=1");
		mapList = new ArrayList(mapMap.values());
		List list = SqlUtil.getObjectsList("select way,flag from rich_way", 4);		// 创建节点之间的连接
		for(int i = 0;i < list.size();i++) {
			Object[] objs = (Object[])list.get(i);
			String way = (String)objs[0];
			int flag = ((Integer)objs[1]).intValue();
			String[] ways = way.split(",");
			for(int j = 0;j < ways.length - 1;j++) {
				RichNodeBean  left = (RichNodeBean)mapMap.get(Integer.valueOf(StringUtil.toInt(ways[j])));
				RichNodeBean  right = (RichNodeBean)mapMap.get(Integer.valueOf(StringUtil.toInt(ways[j + 1])));
				if(left != null && right != null) {
					left.next.add(right);
					if(flag == 0)
						right.prev.add(left);
				}
			}
		}
		RichShop.itemMap = service.getItemMap("1=1");
		shop.refresh();
		
		buildingMap = service.getBuildingMap("1=1");
		
		Iterator iter = mapList.iterator();
		while(iter.hasNext()) {		// 创建结点对应的建筑
			RichNodeBean node = (RichNodeBean)iter.next();
			switch(node.getType()) {
			case RichNodeBean.TYPE_HOUSE:
			{
				RichBuilding building = (RichBuilding)buildingMap.get(Integer.valueOf(node.getValue()));
				building.nodeList.add(node);
				node.setBuilding(building);
				node.house = new HouseBean();
				startNodes.add(node);
			} break;
			case RichNodeBean.TYPE_BUSINESS:
			{
				RichBuilding building = (RichBuilding)buildingMap.get(Integer.valueOf(node.getValue()));
				building.nodeList.add(node);
				node.setBuilding(building);
			} break;
			case RichNodeBean.TYPE_JAIL:
			{
				jailNodes.add(node);
			} break;
			case RichNodeBean.TYPE_HOSPITAL:
			{
				hospitalNodes.add(node);
			} break;
			}
		}
		
		iter = buildingMap.values().iterator();
		while(iter.hasNext()) {	
			RichBuilding bean = (RichBuilding)iter.next();
			switch(bean.getType()) {
			case 0:		// 住宅
			{
			} break;
			case 1:		// 商业建筑
			{
				HouseBean house = new HouseBean();
				house.setType(1);
				for(int i = 0;i < bean.nodeList.size();i++) {
					RichNodeBean node = (RichNodeBean)bean.nodeList.get(i);
					node.house = house;		// 下属几个格子是同一个建筑
				}
				businessBuilding.add(bean);
			} break;
			}
		}
		
		reset();
	}
	
	public void reset() {
		reset(System.currentTimeMillis());
	}
	// reset the world, clear all players
	public void reset(long now) {
		resetCount++;
		Iterator iter = mapList.iterator();
		while(iter.hasNext()) {		// 创建结点对应的建筑
			RichNodeBean node = (RichNodeBean)iter.next();
			node.reset();
		}
		
		for(int i = 0;i < roleUser.length;i++) {
			roleUser[i] = 0;
		}
		for(int i = 0;i < userList.size();i++) {
			RichUserBean user = (RichUserBean)userList.get(i);
			user.reset();
		}
		userList.clear();
		userCount = 0;
		startTime = now;
		calendar = 0;
		
		iter = buildingMap.values().iterator();
		while(iter.hasNext()) {		// 创建随机住宅
			RichBuilding building = (RichBuilding)iter.next();
			if(building.nodeList.size() > 2 && building.getType() == 0) {
				RichNodeBean node = (RichNodeBean)RandomUtil.randomObject(building.nodeList);
				node.addHouse(null, RandomUtil.nextInt(3) + 1);
			}
		}
		
		addEvent(4);
		addEvent(5);
		addEvent(6);
		addEvent(7);
		addEvent(8);
		addEvent(9);
		addEvent(10);
		addEvent(11);
		addEvent(12);
		
		game.reset();
	}
	
	/**
	 * 随机找个地方添加神仙
	 */
	public void addEvent(int event) {
		RichNodeBean node = getRandomEmptyNode();

		node.setEvent(event);
	}
	
	// 找个地方添加住宅
	public void addHouse(int level) {
		RichNodeBean node = getRandomEmptyNode();
		if(node.getType() == RichNodeBean.TYPE_HOUSE)
			node.addHouse(null, level);
	}
	
	// 找个空地，没有event或者其他人的
	public RichNodeBean getRandomEmptyNode() {
		RichNodeBean node = (RichNodeBean)RandomUtil.randomObject(startNodes);
		while(node.getEvent() != 0 || node.getType() != RichNodeBean.TYPE_HOUSE) {
			node = node.randomNext();
		}
		return node;
	}
	
	public List getNodes(RichUserBean bean) {
		int pos = bean.getPosition();
		return getNodes(pos, bean.isReverse());
	}
	
	/**
	 * 返回节点前后的一系列节点，岔路截止
	 * @param id
	 * @return
	 */
	public List getNodes(int id) {
		return getNodes(id, false);
	}
	public List getNodes(int id, boolean reverse) {
		List list = new ArrayList();
		RichNodeBean start = getNode(id);
		RichNodeBean current = start;
		list.add(0, current);
		for(int i = 0;i < viewPort;i++) {
			current = current.getNext();
			if(current == null)
				break;
			list.add(0, current);
		}
		current = start;
		for(int i = 0;i < viewPort;i++) {
			current = current.getPrev();
			if(current == null)
				break;
			list.add(current);
		}
		
		return list;
	}
	
	/**
	 * 得到临近的玩家列表
	 * @return
	 */
	public List getNearbyUser(RichUserBean bean) {
		List list = new ArrayList();
		List nodes = getNodes(bean);
		for(int i = 0;i < nodes.size();i++) {
			RichNodeBean node = (RichNodeBean)nodes.get(i);
			if(node.userList.size() > 0)
				list.addAll(node.userList);
		}
		return list;
	}
	
	/**
	 * 返回用户所在的节点
	 */
	public RichNodeBean getNode(RichUserBean bean) {
		int pos = bean.getPosition();
		return getNode(pos);
	}
	
	public RichNodeBean getNode(int id) {
		return (RichNodeBean)mapMap.get(Integer.valueOf(id));
	}
	// 获取周围的节点
	public RichNodeBean getNearbyNode(RichUserBean richUser, int id) {
		List list = getNodes(richUser);
		for(int i = 0;i < list.size();i++) {
			RichNodeBean node = (RichNodeBean)list.get(i);
			if(node.getId() == id)
				return node;
		}
		return null;
	}

	static byte[] lock2 = new byte[0];
	public static RichUserBean getRichUser(int userId) {
		Integer key = new Integer(userId);
		RichUserBean user = (RichUserBean) userMap.get(key);
		if (user == null) {
			synchronized(lock2) {
				user = (RichUserBean) userMap.get(key);
				if (user == null) {	
//					user = service.getRichUser("user_id=" + userId);	// unused
					if(user == null) {
						user = new RichUserBean();
						user.setUserId(userId);
						user.setName(UserInfoUtil.getUser(userId).getNickName());
					}
					userMap.put(key, user);
				}
			}
		}
		return user;
	}
	
	public static RichUserBean getRichUser(int userId, boolean add) {
		if(add)
			return getRichUser(userId);
		
		Integer key = new Integer(userId);
		return (RichUserBean) userMap.get(key);
	}
	
	// 把玩家扔到某个地方
	public void moveUser(RichUserBean user, int pos) {
		RichNodeBean node = getNode(pos);
		moveUser(user, node);
	}
	public void moveUser(RichUserBean user, RichNodeBean toNode) {
		RichNodeBean node = getNode(user);
		if(node != null)
			node.removeUser(user);
		if(toNode != null) {
			toNode.addUser(user);
			user.setPosition(toNode.getId());
		}
	}
	
	/**
	 * 检查是否破产
	 */
	public void checkBroke(RichUserBean user) {
		if(user.getMoney() + user.getSaving() <= 0) {
			setBroke(user);
		}
	}
	
	public void setBroke(RichUserBean user) {
		user.broke();
		
		Iterator iter = mapMap.values().iterator();		// 所有他的房屋去除所有权
		while(iter.hasNext()) {		// 创建结点对应的建筑
			RichNodeBean node = (RichNodeBean)iter.next();
			switch(node.getType()) {
			case RichNodeBean.TYPE_HOUSE:
			case RichNodeBean.TYPE_BUSINESS:
			{
				if(node.isUserOwner(user))
					node.house.setOwner(null);
			} break;
			}
		}
		user.noControl();
		moveUser(user, null);
		user.addLog("您的资金周转不灵，不幸破产……");
		
		for(int i = 0;i < userList.size();i++) {
			RichUserBean target = (RichUserBean)userList.get(i);
			if(target.isStatusPlay())
				target.addLog(user.getWmlName() + "由于资金周转不灵，破产退出");
		}
	}

	public void addRandomItem(RichUserBean richUser) {
		RichItemBean item = shop.getRandomItem();
		richUser.addBag(item);
		richUser.addLog("您获得了卡片：" + item.getName());
	}
	
	public void removeRandomItem(RichUserBean richUser) {
		int total = richUser.bag.size();
		if(total > 0)
			richUser.bag.remove(RandomUtil.nextInt(total));
	}
	
	/**
	 * 重置一个格子的事件（神仙？妖怪？谢谢）
	 */
	public void setNodeEvent(int event) {
		
	}

	// 有人加入游戏
	public void addUser(int role, RichUserBean richUser) {
		userList.add(richUser);
		roleUser[role] = richUser.getUserId();
		userCount++;
	}

	/**
	 * @return Returns the userCount.
	 */
	public int getUserCount() {
		return userCount;
	}

	/**
	 * @param userCount The userCount to set.
	 */
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	
	// 本场人已满
	public boolean isFull() {
		return userCount >= maxPlayer;
	}

	/**
	 * @return Returns the maxPlayer.
	 */
	public int getMaxPlayer() {
		return maxPlayer;
	}

	/**
	 * @param maxPlayer The maxPlayer to set.
	 */
	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}
	
	// 返回下一局开始所需时间
	public int timeLeft(long now) {
		int left = (int)((startTime - now) / 1000) + interval;
		if(left < 0)
			return 0;
		else
			return left;
	}
	public int timeLeft(Date now) {
		return timeLeft(now.getTime());
	}

	/**
	 * @return Returns the interval.
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * @param interval The interval to set.
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	/**
	 * @return Returns the startTime.
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime The startTime to set.
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int go(RichUserBean richUser, long now) {
		richUser.setGStatus(0);		// 外星人医院等状态取消
		RichNodeBean node = getNode(richUser);
		node.removeUser(richUser);
		int dice = richUser.getDice();

		if(dice == -1) {	// 没有使用遥控骰子或者停留卡
			dice = RandomUtil.nextInt(6) + 1;
		}

		for(int i = 0;i < dice;i++) {
			if(richUser.isReverse()) {
				if(node.hasPrev())
					node = node.randomPrev();
				else {
					node = node.randomNext();
					richUser.reverse();
				}
			} else {
				if(node.hasNext())
					node = node.randomNext();
				else {
					node = node.randomPrev();
					richUser.reverse();
				}
			}
			
			if(richUser.getWithType() == 3) {	// 定时炸弹
				if(richUser.addWithCount() >= 36) {
					richUser.addLog("定时炸弹引爆，受伤住院，须停留30秒之后才能继续前行。");
					moveUser(richUser, (RichNodeBean)RandomUtil.randomObject(hospitalNodes));
					richUser.lockAction(now + 30000, 2);
					richUser.setWith(0);
					return dice;
				}
			}
			
			if(node.getType() == RichNodeBean.TYPE_BANK)
				richUser.setInBank(true);
			
			if(node.getEvent() == 1) {	// 路障
				node.setEvent(0);
				break;
			}
		}
		richUser.decTimeLeft(dice);
		
		if(richUser.getWithType() == 1) {	// 神仙
			if(richUser.addWithCount() >= 7) {
				setUserWith(richUser, 0);
			}
		}
		
		richUser.setPosition(node.getId());
		node.addUser(richUser);
		
		getEvent(richUser, node.getEvent());
		node.setEvent(0);
		

		switch(node.getType()) {
		case RichNodeBean.TYPE_HOUSE:
		case RichNodeBean.TYPE_BUSINESS:
		{
			int place = node.getType() == RichNodeBean.TYPE_HOUSE ? 1 : 10;
			if(node.house.noOwner()) {
				if(richUser.getWith() == 8) {
					richUser.addLog("土地公公显灵，占有此地！");
					node.addHouse(richUser);
				} else {
					richUser.setInPlace(place);
				}
			} else if(node.house.getOwner() == richUser) {
				if(node.house.getLevel() > 0 && node.house.getType() == 1) {	// 研究所，得到道具
					int[] items = {0,9,1,6,22,16};
					RichItemBean item = RichShop.getItem(items[node.house.getLevel()]);
					richUser.addBag(item);
					richUser.addLog("研究所送给你" + item.getName());
				}
				if(!node.house.isMaxLevel()) {
					if(richUser.getWith() == 5 && RandomUtil.percentRandom(50)) {
						node.house.addLevel();
						richUser.addLog("福神显灵，帮你加盖一层");
					}
					if(!node.house.isMaxLevel()) {
						richUser.setInPlace(place);
					}
				}
			} else {
				RichUserBean target = node.house.getOwner();
				
				if(node.isHouse()) {
					int rent = node.getBuilding().getRents(target);		// 支付一条街道所有属于那个玩家的租金
					if(target.getGStatus() != 0) {
						richUser.addLog("您路过" + node.building.getName() + "，房主状态不明，租金免付！");
						rent = 0;
					} else if(richUser.getWith() == 4) {
						richUser.addLog("您路过" + node.building.getName() + "，财神显灵，租金全免！");
						rent = 0;
					} else if(richUser.getWith() == 7) {
						rent += rent;
						richUser.addLog("您路过" + node.building.getName() + "，穷神显灵，支付给" + target.getWmlName() + "加倍租金" + rent);
						target.addLog(richUser.getWmlName() + "路过" + node.building.getName() + "，支付给你租金" + rent);
					} else {
						richUser.addLog("您路过" + node.building.getName() + "，支付给" + target.getWmlName() + "租金" + rent);
						target.addLog(richUser.getWmlName() + "路过" + node.building.getName() + "，支付给你租金" + rent);
					}
					richUser.addMoney(-rent);
					target.addMoney(rent);
				} else if(node.house.getLevel() > 0){	// 商业建筑
					switch(node.house.getType()) {
					case 1: {		// 研究所
						
					} break;
					case 2: {		// 连锁超市
						int rent = node.getRent() * 2;
						if(target.getGStatus() != 0) {
							richUser.addLog("您路过" +target.getWmlName() + "的连锁超市，房主状态不明，租金免付！");
							rent = 0;
						} else if(richUser.getWith() == 4) {
							richUser.addLog("您路过" +target.getWmlName() + "的连锁超市，财神显灵，租金全免！");
							rent = 0;
						} else if(richUser.getWith() == 7) {
							rent += rent;
							richUser.addLog("您路过连锁超市，穷神显灵，支付给" + target.getWmlName() + "加倍租金" + rent);
							target.addLog(richUser.getWmlName() + "路过您的连锁超市，消费金额" + rent);
						} else {
							richUser.addLog("您路过" +target.getWmlName() + "的连锁超市，消费金额" + rent);
							target.addLog(richUser.getWmlName() + "路过您的连锁超市，消费金额" + rent);
						}
						richUser.addMoney(-rent);
						target.addMoney(rent);
					} break;
					case 3: {		// 饭店
						int rent = node.getRent();
						if(target.getGStatus() != 0) {
							richUser.addLog("您路过" +target.getWmlName() + "的饭店，房主状态不明，租金免付！");
							rent = 0;
						} else if(richUser.getWith() == 4) {
							richUser.addLog("您路过" +target.getWmlName() + "的饭店，财神显灵，租金全免！");
							rent = 0;
						} else if(richUser.getWith() == 7) {
							rent += rent;
							richUser.addLog("您路过" +target.getWmlName() + "的饭店，入住休息，穷神显灵，交纳加倍费用" + rent + "，10秒之后才能继续游戏。");
							target.addLog(richUser.getWmlName() + "路过您的饭店，交纳费用" + rent);
						} else {
							richUser.addLog("您路过" +target.getWmlName() + "的饭店，入住休息，交纳费用" + rent + "，10秒之后才能继续游戏。");
							target.addLog(richUser.getWmlName() + "路过您的饭店，交纳费用" + rent);
						}
						richUser.addMoney(-rent);
						target.addMoney(rent);
						richUser.lockAction(now + 10000, 4);
					} break;
					}
				}
//				node.house.addPassby();
//				if(node.house.isMaxPassby()) {
//					target.addLog("您在" + node.building.getName() + "的房子合同到期，产权已交还");
//					node.setHouse(null);
//				}
				if(richUser.getWith() == 8) {
					richUser.addLog("土地公公显灵，占有此地！");
					node.house.setOwner(richUser);
				}
			}
			
			if(richUser.getWith() == 9 && !node.house.noOwner() && !node.house.isMaxLevel()) {
				node.house.addLevel();
				richUser.addLog("天使显灵，此地房屋升级");
			}
		} break;
		case RichNodeBean.TYPE_SHOP:
			richUser.setInShop(true);
			shop.refresh();
			break;
		case RichNodeBean.TYPE_POINT:
			richUser.addMoney2(node.getValue());
			richUser.addLog("获得了" + node.getValue() + "点券");
			break;
		case RichNodeBean.TYPE_MONEY:
			richUser.addMoney(node.getValue());
			richUser.addLog("中奖啦，获得" + node.getValue() + "元");
			break;
		case RichNodeBean.TYPE_CARD:
			addRandomItem(richUser);
			break;
		case RichNodeBean.TYPE_NEWS:
			news(richUser);
			break;
		case RichNodeBean.TYPE_MAGIC:
			richUser.setInMagic(true);
			break;
		case RichNodeBean.TYPE_SPY:
			richUser.setInSpy(true);
			break;
		case RichNodeBean.TYPE_COURT:
			richUser.setInCourt(true);
			break;
		case RichNodeBean.TYPE_GAME1:
			richUser.setInGame1(true);
			break;
		case RichNodeBean.TYPE_GAME2:
			richUser.setInGame2(true);
			break;
		case RichNodeBean.TYPE_GAME3:
			richUser.setInGame3(true);
			break;
		}
		checkBroke(richUser);
		return dice;
	}
	

	// 新闻事件
	public void news(RichUserBean richUser) {
		long now = System.currentTimeMillis();
		int n = RandomUtil.nextInt(14);
		switch(n) {
		case 0:	{	// 获得卡片
			richUser.addLog("新闻，您在地上拣到两张卡片");
			addRandomItem(richUser);
			addRandomItem(richUser);
		} break;
		case 1:	{	// 获得钱财 500
			richUser.addLog("新闻，您意外获得遗产500");
			richUser.addMoney(500);
		} break;
		case 2:	{	// 存款增加10%
			richUser.addLog("新闻，银行调息，您的存款增加了10%");
			richUser.addSavingRate(0.1f);
		} break;
		case 3:	{	// 进监狱
			richUser.addLog("新闻，您被陷害进了监狱，30秒后才能继续游戏");
			moveUser(richUser, (RichNodeBean)RandomUtil.randomObject(jailNodes));
			richUser.lockAction(now + 30000, 1);
		} break;
		case 4:	{	// 增加点券
			richUser.addLog("新闻，红运当头，您不明原因的获得50点券");
			richUser.addMoney2(50);
		} break;
		case 5:	{	// 出现小狗
			richUser.addLog("新闻，最近街道上有野狗咬人");
			addEvent(10);
		} break;
		case 6:	{	// 出现小狗
			richUser.addLog("新闻，行人闯越马路罚款200");
			richUser.addMoney(-200);
		} break;
		case 7:	{	// 进医院
			richUser.addLog("新闻，您掉进水沟，送往医院，20秒后才能继续游戏");
			moveUser(richUser, (RichNodeBean)RandomUtil.randomObject(hospitalNodes));
			richUser.lockAction(now + 20000, 2);
		} break;
		case 8:	{	// 获得钱财 500
			richUser.addLog("新闻，财团海外投资亏损400元");
			richUser.addMoney(-400);
		} break;
		case 9:	{	// 获得钱财 500
			richUser.addLog("新闻，银行加发储金红利300");
			richUser.addMoney(300);
		} break;
		case 10:	{	// 存款增加10%
			richUser.addLog("新闻，投资买基金存款损失5%");
			richUser.addSavingRate(-0.05f);
		} break;
		case 11:	{	// 外星人
			richUser.addLog("新闻，您被外星人绑架了，30秒后才能继续游戏");
			richUser.lockAction(now + 30000, 3);
		} break;
		case 12:	{	// 出现小偷
			richUser.addLog("新闻，最近街道上有小偷出没");
			addEvent(13);
		} break;
		case 13:	{	// 出现强盗
			richUser.addLog("新闻，最近街道上有强盗出没");
			addEvent(14);
		} break;
		}
	}
	

	// 神仙附身
	public void getEvent(RichUserBean richUser, int event) {
		long now = System.currentTimeMillis();
		switch(event) {
		case 1:		// 路障
			break;
		case 2:		// 地雷
		{
			moveUser(richUser, (RichNodeBean)RandomUtil.randomObject(hospitalNodes));
			richUser.lockAction(now + 30000, 2);
			richUser.addLog("您踩中了地雷，被送进医院治疗，须停留30秒之后才能继续前行。");
		} break;
		case 3:		// 定时炸弹
		{
			richUser.addLog("踩中一个定时炸弹，36步之后爆炸");
			setUserWith(richUser, event);
		} break;
		case 4:		// 财神
		{
			int money = RandomUtil.nextInt(1000);
			richUser.addLog("财神附身，得到钱财。" + money);
			richUser.addMoney(money);
			setUserWith(richUser, event);
		} break;
		case 5:		// 福神
		{
			richUser.addLog("福神附身，得到两张卡片。");
			setUserWith(richUser, event);
			addRandomItem(richUser);
			addRandomItem(richUser);
		} break;
		case 6:		// 衰神
		{
			richUser.addLog("衰神附身，损失两张卡片。");
			setUserWith(richUser, event);
			removeRandomItem(richUser);
			removeRandomItem(richUser);
		} break;
		case 7:		// 穷神
		{
			int money = RandomUtil.nextInt(1000);
			richUser.addLog("穷神附身，损失钱财" + money);
			richUser.addMoney(-money);
			setUserWith(richUser, event);
		} break;
		case 8:		// 土地公公
		{
			richUser.addLog("土地公公附身，帮你占用别人土地。");
			setUserWith(richUser, event);
		} break;
		case 9:		// 天使
		{
			richUser.addLog("天使附身，所到之处房屋升级。");
			setUserWith(richUser, event);
		} break;
		case 10:		// 狗
		{
			moveUser(richUser, (RichNodeBean)RandomUtil.randomObject(hospitalNodes));
			richUser.lockAction(now + 30000, 2);
			richUser.addLog("您被狗咬伤，进医院治疗，须停留30秒之后才能继续前行。");
		} break;
		case 11:		// 宝箱
		{
			richUser.addMoney(500);
			richUser.addLog("拣到一个宝箱，获得500元。");
		} break;
		case 12:		// 宝箱
		{
			richUser.addMoney2(200);
			richUser.addLog("拣到一个宝箱，获得200点券。");
		} break;
		case 13:		// 小偷
		{
			richUser.setMoney2(richUser.getMoney2() / 2);
			richUser.addLog("路上遇到小偷，损失一半点券。");
		} break;
		case 14:		// 强盗
		{
			int num = richUser.bag.size() / 2;
			for(int i = 0;i < num;i++)
				removeRandomItem(richUser);
			richUser.addLog("路上遇到强盗，损失一半卡片。");
		} break;
		}
	}
	
	// 设置用户跟随的神仙（或者定时炸弹），处理当时身上的神仙离开
	public void setUserWith(RichUserBean richUser, int event) {
		if(RichNodeBean.eventTypes[richUser.getWith()] == 1) {	// 神仙
			addEvent(richUser.getWith());		// 神仙离开后跑到另一个随机的地方
			richUser.addLog(RichNodeBean.eventNames[richUser.getWith()] + "离开");
		}
		richUser.setWith(event);
	}

	// 奖励
	public void reward() {
		if(userCount == 0)
			return;
		Iterator iter = mapMap.values().iterator();		// 所有他的房屋去除所有权
		while(iter.hasNext()) {		// 创建结点对应的建筑
			RichNodeBean node = (RichNodeBean)iter.next();
			if(node.house != null) {
				RichUserBean ru = node.house.getOwner();
				if(ru == null)
					continue;
				ru.addTotalHouse();
				ru.addTotalHouseValue(node.getPriceValue());
			}
		}
		
		List rewardUserList	= new ArrayList(userCount);	// 有资格获得卡片的用户列表
		StringBuilder sb = new StringBuilder(256);
		for(int i = 0;i < userList.size();i++) {
			RichUserBean user = (RichUserBean)userList.get(i);
			sb.append(user.getRoleName());
			sb.append("(<a href=\"/user/ViewUserInfo.do?userId=");
			sb.append(user.getUserId());
			sb.append("\">");
			sb.append(UserInfoUtil.getUser(user.getUserId()).getNickNameWml());
			sb.append("</a>)");
			int sum = user.getMoney() + user.getSaving() + user.getTotalHouseValue();
			if(user.getStatus() == RichUserBean.STATUS_PLAY) {
				sb.append("资产");
				sb.append(sum);
			} else
				sb.append("破产");
			sb.append("(房产");
			sb.append(user.getTotalHouseValue());
			sb.append(",点券");
			sb.append(user.getMoney2());
			sb.append(",卡片");
			sb.append(user.bag.size());
			sb.append("张)");
			sb.append("<br/>");
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(isFlag(0) && sum >= rewardCard100 && user.getTotalHouseValue() >= minHouseValue && (hour > 7 || hour < 2)) {		// 奖励
				rewardUserList.add(user);
			}
		}
		if(rewardUserList.size() > 0) {
			int maxRewardCount = maxPlayer / 2;	// 最多一半人获奖
			if (rewardUserList.size() > maxRewardCount) {
				Collections.sort(rewardUserList, new Comparator() {
					public int compare(Object o1, Object o2) {
						RichUserBean u1 = (RichUserBean) o1;
						RichUserBean u2 = (RichUserBean) o2;
						int sum1 = u1.getMoney() + u1.getSaving() + u1.getTotalHouseValue();
						int sum2 = u2.getMoney() + u2.getSaving() + u2.getTotalHouseValue();
						return (sum1 > sum2 ? -1 : (sum1 == sum2 ? 0 : 1));
					}
				});
				rewardUserList = rewardUserList.subList(0, maxRewardCount);
			}
			
			for(int i = 0;i < rewardUserList.size();i++) {
				RichUserBean user = (RichUserBean)rewardUserList.get(i);
				UserBagCacheUtil.addUserBagCacheNotice(user.getUserId(), 68, null);
			}
		}
		
		RichLogBean richLog = new RichLogBean();
		richLog.setDetail(sb.toString());
		richLog.setUserCount(userCount);
		richLog.setFlag(flag);
		richLog.setInterval(interval);
		service.addRichLog(richLog);
		
		for(int i = 0;i < userList.size();i++) {
			RichUserBean user = (RichUserBean)userList.get(i);
			
			NoticeAction.sendNotice(user.getUserId(), "本局大富翁游戏已结束",
					NoticeBean.GENERAL_NOTICE, "/rich/glog.jsp?id=" + richLog.getId());
		}
	}
	
	public void timer(long now) {
		int elapsed = (int) ((now - startTime) / 1000);
		if(elapsed >= interval) {
			reward();
			reset(now);
		} else {
			if(userList.size() > 0) {
				for(int u = 0;u < userList.size();u++) {
					RichUserBean user = (RichUserBean)userList.get(u);
					if(user.getStatus() == RichUserBean.STATUS_PLAY && user.getLastActionTime() + 60000 < now) {		// 发呆太久
						go(user, now);
						user.noControl();
						user.addLastActionTime(30000);	//	如果没有人工操作，30秒动一次 
					}
				}
				runCalendar(now, elapsed, true);
			} else {
				runCalendar(now, elapsed, false);
			}
		}
	}

	static int[] festival = {10, 15, 20, 25, 30, 35, 40, 50};	// 发生时间，按分钟
	// 感恩节等事件检查，如果avail则生效，否则表示没有玩家
	private void runCalendar(long now, int elapsed, boolean avail) {
		if(calendar >= festival.length)
			return;
		if(elapsed > festival[calendar] * 60)
			calendar++;
		else
			return;
		if(!avail)
			return;
		
		for(int i = 0;i < userList.size();i++) {
			RichUserBean user = (RichUserBean)userList.get(i);
			if(user.isStatusPlay()) {

				switch(calendar) {
				case 1: {
					user.addLog("圣诞节到了，每人随机获得卡片一张");
					addRandomItem(user);
				} break;
				case 2: {
					user.addLog("元旦到了，每人获得500元");
					user.addMoney(500);
				} break;
				case 3: {
					user.addLog("情人节到了，所有女性角色获得一张卡片");
					if(!user.isMale())
						addRandomItem(user);
				} break;
				case 4: {
					user.addLog("春节到了，每人获得1000元");
					user.addMoney(1000);
				} break;
				case 5: {
					user.addLog("端午节到了，每人获得55点券");
					user.addMoney2(55);
				} break;
				case 6: {
					user.addLog("七夕到了，所有男性角色获得一张卡片");
					if(user.isMale())
						addRandomItem(user);
				} break;
				case 7: {
					user.addLog("感恩节到了，所有人的负面状态解除");
					if(user.getGStatus() > 0) {
						user.setGStatus(0);
						user.setNextActionTime(now);
					}
				} break;
				case 8: {
					user.addLog("重阳节到了，每人获得99点券");
					user.addMoney2(99);
				} break;
				}
			}
		}
	}
	
	// 所有玩家添加log
	public void addAllLog(String log) {
		for(int i = 0;i < userList.size();i++) {
			RichUserBean user = (RichUserBean)userList.get(i);
			if(user.isStatusPlay())
				user.addLog(log);
		}
	}


	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	// 判断第几位flag是不是1
	public boolean isFlag(int bit) {
		return (flag & (1 << bit)) != 0;
	}

	public boolean hasIp(String ip) {
		for(int i = 0;i < userList.size();i++) {
			RichUserBean user = (RichUserBean)userList.get(i);
			if(ip.equals(user.ip))
				return true;
		}
		return false;
	}
}
