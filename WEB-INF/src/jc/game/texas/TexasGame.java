package jc.game.texas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.SqlUtil;

//判断用户的排名，按照score倒序

/**
 * @author bombzj
 * @datetime 2010-1-3 
 * @explain 德州扑克
 */
public class TexasGame {
	public static TexasService service = new TexasService();
	// 按牌排序
	static class UserComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			TexasUser c1 = (TexasUser) o1;
			TexasUser c2 = (TexasUser) o2;
			if (c1.getScore() < c2.getScore()) {
				return 1;
			} else {
				if (c1.getScore() == c2.getScore()) {
					return 0;
				} else {
					return -1;
				}
			}
		}
	}
	// 按下注总量排序
	static class UserComparator2 implements Comparator {

		public int compare(Object o1, Object o2) {
			TexasUser c1 = (TexasUser) o1;
			TexasUser c2 = (TexasUser) o2;
			if (c1.getWager() < c2.getWager()) {
				return 1;
			} else {
				if (c1.getWager() == c2.getWager()) {
					return 0;
				} else {
					return -1;
				}
			}
		}
	}
	static UserComparator comp = new UserComparator();
	static UserComparator2 comp2 = new UserComparator2();
	
	public static ICacheMap texasUserCache = CacheManage.addCache(new LinkedCacheMap(1000, true), "texasUser");	// 保存用户所在的桌
	public static int getUserBoardId(int userId) {
		TexasUserBean tub = (TexasUserBean)texasUserCache.sgt(new Integer(userId));
		if(tub == null)
			return -1;
		return tub.getBoardId();
	}
	
	public static String[] roundNames = {"","","底牌圈","翻牌圈","转牌圈","河牌圈",""};
	public static String[] typeNames = {"获胜", "散牌", "一对", "两对", "三条", "顺子", "同花", "葫芦", "四条", "同花顺", "皇家同花顺"};
	
	
	public static String[] cardColors = {"黑", "红", "梅", "方"};
	public static String[] cardColors2 = {"S", "H", "C", "D"};
	public static String[] cardPoints = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	public static String[] cardImages = new String[52];		// 52张牌的图片
	public static String[] cardNames = new String[52];		// 52张牌的中文名称
	public static String[] cardNames2 = new String[52];	// 52张牌的简写名称
	
	public static int STATUS_READY = 0;		// 游戏等待中
	public static int STATUS_PLAY = 1;		// 游戏进行中
	public static int STATUS_END = 2;		// 游戏结束
	static {
		int start = 0;
		for(int i = 0;i < 4;i++)
			for(int j = 0;j < 13;j++) {
				cardNames[start] = cardColors[i] + cardPoints[j];
				cardNames2[start] = cardColors2[i] + cardPoints[j];
				cardImages[start] = (j + 2) + "_" + (4 - i);
				start++;
			}
	}

	
//	 将牌显示为中文名称
	public static String toString(int[] cards) {
		StringBuilder sb = new StringBuilder(cards.length * 3);
		for(int i = 0;i < cards.length;i++) {
			if(cards[i] == -1)	continue;
			sb.append(cardNames[cards[i]]);
		}
		return sb.toString();
	}
//	 将牌显示为简写名称
	public static String toString2(int[] cards) {
		StringBuilder sb = new StringBuilder(cards.length * 3);
		for(int i = 0;i < cards.length;i++) {
			if(cards[i] == -1)	continue;
			sb.append(cardNames2[cards[i]]);
		}
		return sb.toString();
	}
	// 将牌显示为图片
	public static String toImage(int[] cards) {
		StringBuilder sb = new StringBuilder(cards.length * 32);
		for(int i = 0;i < cards.length;i++) {
			if(cards[i] == -1)	continue;
			sb.append("<img src=\"/wgame/cardImg/");
			sb.append(cardImages[cards[i]]);
			sb.append(".gif\" alt=\"");
			sb.append(cardNames2[cards[i]]);
			sb.append("\"/>");
		}
		return sb.toString();
	}
	public static String getCardImage(int i) {
		return cardImages[i];
	}
	
	int id;	// 数据库id
	int boardId;
	long createTime;	// 开始时间
	
	public static String[] gameTypeNames = {"练习", "积分", "实战", "擂台"};
	int gameType;		// 游戏类型
	public int getGameType() {
		return gameType;
	}
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
	public String getGameTypeName() {
		return gameTypeNames[gameType];
	}
	
	public int MAX_USER;	// 最多10人玩
	int userCount;	// 参赛人数
	int roundUserCount;	// 本局比赛剩余人数
	int actUserCount;		// 剩余的可以操作的玩家数，allin和放弃的扣除
	int gameUserCount;	// 本局一开始的才赛人数
	TexasUser[] users;		// 保存2-10个TexasUser，参赛人
	List winUsers;
	List roundUsers = new ArrayList(8);	// 参与的全部玩家列表
	
	int turn;		// 第几轮
	int[] cards = new int[5];	// 台面的排，-1表示没有开，0-51为牌
	
	int[] allCards;			// 所有的牌，打乱后从最后一张开始取得
	int getCardIndex;			// 开始获取牌的index，0 1 2往上
	
	int round;		// 轮，0是未开始，盲注后进入1，之后选择加注或者盖牌等等，之后亮3张牌进入2，3 4 5分别是三张、四张、五张牌的时候下注，6表示结束
	int status;		// 当前局状态
	int wager;		// 总注
	int roundWager;		// 这轮的最高注
	int giveupWager;	// 有人放弃，他下的注放这里（属于主池）
	
	int dealer = -1;	// 庄家的索引
	int lastRaise;		// 最后一个加注的人的索引，到这个人之后就进入下一轮
	int current;		// 当前动作玩家
	
	int smallWager;		// 小盲注
	int baseWager;	// 最小注，一般是2
	
	public static long NEXT_TIME_INTERVAL = 25 * 1000l;
	public static long WAIT_TIME = 40 * 1000l;	// 用户每次操作等待的时间
	long nextTime;		// 下一局可以开始的时间，等于结束时间加上等待时间NEXT_TIME_INTERVAL
	long actTime;		// 下一次用户操作必须在这个时间之前，否则自动视为放弃
	
	int maxMoney;		// 最多可以携带多少资金进入
	int minMoney;		// 最少需要携带多少资金进入
	
	SimpleGameLog log = new SimpleGameLog(50);	// 用于保存游戏最近记录
	
	public long getNextTime() {
		return nextTime;
	}
	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public int getDealer() {
		return dealer;
	}
	public void setDealer(int dealer) {
		this.dealer = dealer;
	}
	public int getLastRaise() {
		return lastRaise;
	}
	public void setLastRaise(int lastRaise) {
		this.lastRaise = lastRaise;
	}
	public int getWager() {
		return wager;
	}
	public void setWager(int wager) {
		this.wager = wager;
	}
	public TexasGame() {
		
	}
	public TexasGame(int boardId, int type) {
		this.boardId = boardId;
		MAX_USER = 5;
		baseWager = 2;
		smallWager = baseWager / 2;
		init();
		minMoney = 40;
		maxMoney = 400;
		gameType = type;
	}
	public TexasGame(int boardId, int type, int wager, int maxUser) {
		this.boardId = boardId;
		MAX_USER = maxUser;
		baseWager = wager;
		smallWager = baseWager / 2;
		init();
		minMoney = 40;
		maxMoney = 400;
		gameType = type;
	}
	public TexasGame(int boardId, int type, int wager, int maxUser, int min, int max) {
		this.boardId = boardId;
		MAX_USER = maxUser;
		baseWager = wager;
		smallWager = baseWager / 2;
		init();
		minMoney = min;
		maxMoney = max;
		gameType = type;
	}
	
	// 计算结果，谁输谁赢，赢多少
	public void calc() {
		round = 6;
		status = STATUS_END;
		winUsers = new ArrayList(roundUserCount);
		
		if(roundUserCount < 2) {	// 如果只剩一个玩家，不开牌，但要把这个玩家的roundWager加回到wager
			for(int i = 0;i < users.length;i++) {
				TexasUser user = users[i];
				if(user == null || !user.isStatusPlay())	continue;
				winUsers.add(user);
				user.addWager(user.getRoundWager());
				wager += user.getRoundWager();
				user.setWinWager(user.getWager() + giveupWager);
			}
		} else {
			for(int i = 0;i < users.length;i++) {
				TexasUser user = users[i];
				if(user == null || !user.isStatusPlay())	continue;
				user.calc(cards);
				winUsers.add(user);
			}
			
			Collections.sort(winUsers, comp);
			
			List calcList = new ArrayList(winUsers);
			Collections.sort(calcList, comp2);
			int[] side = new int[MAX_USER + 1];
			int[] sideWager = new int[MAX_USER];
			side[0] = 1;
			
			int last = ((TexasUser)calcList.get(0)).getWager();
			sideWager[0] = last;
				
			int sideCount = 0;
			// 计算分多少边池
			for(int i = 1;i < calcList.size();i++){	
				TexasUser tu = (TexasUser)calcList.get(i);
				if(tu.getWager() != last) {
					sideCount++;
					last = tu.getWager();
					sideWager[sideCount] = last;
				}
	
				side[sideCount] = i + 1;
			}
	
			for(int i = 0;i <= sideCount;i++) {
				
				int w = sideWager[i];		// 计算边池的总筹码
				if(side[i + 1] != 0)
					w -= sideWager[i + 1];
				w *= side[i];
				
				List sideWinUser = new ArrayList(calcList.subList(0, side[i]));
				Collections.sort(sideWinUser, comp);	// 边池的玩家比牌
				int same = 1;
				int score = ((TexasUser)sideWinUser.get(0)).getScore();
				for(;same < side[i];same++) 	// 判断有相同的牌分的玩家数量
					if(((TexasUser)sideWinUser.get(same)).getScore() != score)
						break;
				if(i == sideCount)	// 主池，把放弃了的筹码全部加入
					w += giveupWager;
				w /= same;
				for(int j = 0;j < same;j++) {
					TexasUser tu = (TexasUser)sideWinUser.get(j);
					tu.addWinWager(w);
				}
			}
		}
//		 保存记录到数据库
		service.addTexasRecord(this);
		
		// 每个用户增加钱
		for(int i = 0;i < winUsers.size();i++){
			TexasUser tu = (TexasUser)winUsers.get(i);
			if(tu.getWinWager() != 0) {
				tu.addMoney(tu.getWinWager());
				log.add("[荷官]" + (tu.getSeat() + 1) + "座赢得了" + tu.getWinWager());
			}
		}
		if(gameType == 1) {
			for(int i = 0;i < users.length;i++) {
				TexasUser user = users[i];
				if(user == null)	continue;
				if(user.isStatusPlay()) {	// 正在玩的玩家都保存积分
					addUserMoneyDB(user.getUserId(), user.getWinWager() - user.getWager());
				}
			}
		}
		
		for(int i = 0;i < users.length;i++) {
			TexasUser user = users[i];
			if(user == null)	continue;

			if(user.isStatusPlay())	// 正在玩的玩家都变成status_end
				user.setStatus(2);
			
			if(user.getMoney() == 0)	// 没有钱的玩家退出
				leaveSeat(i);
		}
		nextTime = System.currentTimeMillis() + NEXT_TIME_INTERVAL;
	}
	
	// 计算一个用户
	public void calcUser(TexasUser user) {
		user.calc(cards);
	}
	
	// 洗牌，每次从之前的结果开始洗，保证牌的混乱
	public void shuffle() {
		if(allCards == null) {
			allCards = new int[52];
			for(int i = 0;i < allCards.length;i++)
				allCards[i] = i;
		}

		for(int i = 0;i < 100;i++) {
			int rnd = RandomUtil.nextInt(52);
			int rnd2 = RandomUtil.nextInt(52);
			if(rnd != rnd2) {
				int tmp = allCards[rnd];
				allCards[rnd] = allCards[rnd2];
				allCards[rnd2] = tmp;
				rnd = rnd2;
			}
		}
//		allCards[0] = 1;
//		allCards[1] = 1;
//		allCards[6] = 2;
//		allCards[7] = 2;
//		allCards[8] = 3;
//		allCards[9] = 4;
//		allCards[10] = 3;
	}
	// 桌子初始化
	public void init() {
		userCount = 0;
		users = new TexasUser[MAX_USER];
	}
	// 游戏重置
	public void reset() {
		
		shuffle();
		getCardIndex = 0;
		

		for(int i = 0;i < 5;i++)
			cards[i] = -1;
		round = 0;
		status = STATUS_READY;
	}
	// 获得一张新的牌
	public int getNewCard() {
		int card = allCards[getCardIndex];
		getCardIndex++;
		return card;
	}
	
	// 开始游戏
	public synchronized void start() {
		status = STATUS_READY;
		round = 0;
		if(userCount < 2)
			return;
		
		reset();
		roundUsers.clear();
		gameUserCount = roundUserCount = actUserCount = userCount;
		for(int i = 0;i < users.length;i++) {
			TexasUser user = users[i];
			if(user == null)	continue;
			user.userCards[0] = getNewCard();
			user.userCards[1] = getNewCard();
			user.setRoundWager(0);
			user.setWager(0);
			user.setAction(null);
			user.setStatus(1);
			user.setWinWager(0);
			user.setType(0);	// 该玩家放弃？
			roundUsers.add(user);
		}
		dealer = getNextUser(dealer);	// 每次庄往后挪
		current = getNextUser(dealer);	// 获得庄家的下一个玩家，就是小盲注
		users[current].addRoundWager(smallWager);
		current = getNextUser(current);	// 获得小盲注的下一个玩家，就是大盲注
		users[current].addRoundWager(baseWager);
		
		current = getNextUser(current);	// 下一位玩家开始下注
		lastRaise = current;
		
		roundWager = baseWager;
		giveupWager = 0;
		wager = 0;
		round = 2;
		
		status = STATUS_PLAY;
		createTime = System.currentTimeMillis();
		actTime = createTime + WAIT_TIME;
	}
	// 进入下一轮
	public void next() {
		switch(round) {
		case 2: {	// 发3张牌
			cards[0] = getNewCard();
			cards[1] = getNewCard();
			cards[2] = getNewCard();
			log.add("荷官发放3张底牌");
			current = getNextUser(dealer);
			lastRaise = current;
		} break;
		case 3: {	// 发第四张牌
			cards[3] = getNewCard();
			
			current = getNextUser(dealer);
			lastRaise = current;
			log.add("荷官发放新底牌");
		} break;
		case 4: {	// 发第五张牌
			cards[4] = getNewCard();
			
			current = getNextUser(dealer);
			lastRaise = current;
			log.add("荷官发放新底牌");
		} break;
		}
		if(round <= 5) {	// 清除所有状态和下注信息
			for(int i = 0;i < users.length;i++) {
				TexasUser user = users[i];
				if(user == null || !user.isStatusPlay())	continue;
				user.setAction(null);
				wager += user.getRoundWager();
				user.addWager(user.getRoundWager());
				user.setRoundWager(0);
			}
			roundWager = 0;
		}
		if(round == 5) {
			calc();
			return;
		}
		if(round <= 5)
			round++;
	}
	public int[] getCards() {
		return cards;
	}
	public void setCards(int[] cards) {
		this.cards = cards;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public int getUserCount() {
		return userCount;
	}
	public int getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(int maxMoney) {
		this.maxMoney = maxMoney;
	}
	public int getMinMoney() {
		return minMoney;
	}
	public void setMinMoney(int minMoney) {
		this.minMoney = minMoney;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public TexasUser[] getUsers() {
		return users;
	}
	public void setUsers(TexasUser[] users) {
		this.users = users;
	}
	// 坐在某个位置，携带money数量的进入
	public synchronized boolean join(int userId, int pos, int money) {
		if(users[pos] == null) {
			
			TexasUser user = new TexasUser();
			user.userId = userId;
			user.setStatus(0);
			users[pos] = user;
			user.setSeat(pos);
			user.setMoney(money);
			userCount++;

//			if(userCount > 1 && status != STATUS_PLAY && canNext())
//				start();
			return true;
		}
		return false;
	}
	// 离开某个位置，如果不是force，那么玩家在游戏中的时候返回false而不离开
	public synchronized boolean leave(int userId, boolean force) {
		for(int i = 0;i < users.length;i++) {
			TexasUser user = users[i];
			if(user == null)	continue;
			if(user.userId == userId) {
				if(status == 1 && user.isStatusPlay()) {	// 玩家正在游戏，则先主动站起
					if(!force)
						return false;
					if(users[current].getUserId() == userId)
						act(5);
					else {
						userGiveup(user);
//						 检查用户数量，是否可以结束了（如果只剩一个人就结束）
						if(roundUserCount < 2) {
							calc();
						}
					}
				}
				users[i] = null;	// 离开
				userCount--;
				return true;
			}
		}
		return false;
	}
	public synchronized void leaveSeat(int seat) {
		TexasUserBean tub =	TexasGame.getUserBean(users[seat].getUserId());
		tub.setBoardId(-1);
		users[seat] = null;	// 离开
		userCount--;
	}
	// 返回从位置i开始往后next人对应的索引
	public int getNextUser(int i, int next) {
		while(next > 0) {
			i++;
			if(i >= MAX_USER)
				i = 0;
			if(users[i] != null && users[i].isStatusPlay()) {
				next--;
			}
		}
		return i;
	}
//	 返回从位置i开始往后的下一个人
	public int getNextUser(int i) {
		// TODO 这个函数和上一个函数注意避免死循环
		while(true) {
			i++;
			if(i >= MAX_USER)
				i = 0;
			if(users[i] != null && users[i].isStatusPlay()) {
				return i;
			}
		}
	}
//	 返回从位置i开始往后的下一个人，如果到了end这个index就结束
	public int getNextUser2(int i, int end) {
		// TODO 这个函数和上一个函数注意避免死循环
		while(true) {
			i++;
			if(i >= MAX_USER)
				i = 0;
			if(i == end)
				return -1;
			if(users[i] != null && users[i].isStatusPlay()) {
				return i;
			}
		}
	}
	// 获得loginUser对应的texasUser index
	public TexasUser getUser(int userId) {
		for(int i = 0;i < users.length;i++) {
			TexasUser user = users[i];
			if(user == null)	continue;
			if(user.userId == userId) {
				return user;
			}
		}
		return null;
	}
	public int getCurrentUserId() {
		return users[current].getUserId();
	}
	
	public boolean act(int act) {
		return act(act, 0, false);
	}
	// 玩家进行动作，只有current才行，如果操作不正确直接返回false
	public synchronized boolean act(int act, int wager2, boolean manual) {
		if(status != STATUS_PLAY)		// 游戏进行中才能操作
			return false;
		
		TexasUser user = users[current];
		switch(act) {
		case 1: {	// 跟注 call
			int need = roundWager - user.getRoundWager();	// 还需要下多少注
			if(need < 0)
				return false;
			if(need != 0) {
				if(need >= user.getMoney())
					return false;
				user.addRoundWager(need);
			}
			user.setAction("跟注");
			log.add("[荷官]" + (current + 1) + "座[跟注]" + need);
		} break;
		case 2: {	// 加注 raise
			if(wager2 < 0)
				return false;
			int need = roundWager - user.getRoundWager() + wager2;	// 还需要下多少注
			if(need >= user.getMoney())
				return false;
			user.addRoundWager(need);
			user.setAction("加注");
			lastRaise = current;
			roundWager += wager2;
			log.add("[荷官]" + (current + 1) + "座[加注]" + need);
		} break;
		case 3: {	// all in
			int need = user.getMoney();
			user.addRoundWager(user.getMoney());
			if(user.getRoundWager() > roundWager) {
				lastRaise = current;
				roundWager = user.getRoundWager();
			}
			user.setAction("全下");
			log.add("[荷官]" + (current + 1) + "座[全下]" + need);
			actUserCount--;
		} break;
		case 4: {	// 看牌 check
			if(roundWager != 0)
				return false;
			user.setAction("看牌");
			log.add("[荷官]" + (current + 1) + "座[看牌]");
		} break;
		case 5: {	// 放弃 fold
			userGiveup(user);
			if(roundUserCount < 2) {
				calc();
				return true;
			}
		} break;
		default:
			return false;
		}
		if(manual)	// 用户是手动操作，不让掉线了
			user.drop = 0;
		
		do {
			boolean roundEnd = false;
			int next = getNextUser2(current, lastRaise);
			if(next != -1) {
				current = next;
			} else {
				roundEnd = true;
			}
			if(roundEnd) {
				next();
				if(round > 5)	// 结束
					return true;
			}
		} while (users[current].getMoney() == 0 || actUserCount < 2 && users[current].getRoundWager() >= roundWager);	// 这个人已经全下了，跳过
		actTime = System.currentTimeMillis() + WAIT_TIME;
		return true;
	}
	// 玩家放弃
	public void userGiveup(TexasUser user) {
		user.setStatus(3);
		roundUserCount--;
		if(user.getMoney() != 0)	// 如果当时已经all in了，那说明当时actUserCount已经减少了，不需要再减少
			actUserCount--;
		if(gameType == 1)
			addUserMoneyDB(user.getUserId(), -user.getWager() - user.getRoundWager());	// 放弃的玩家，积分直接保存
		giveupWager += (user.getWager() + user.getRoundWager());
		wager += user.getRoundWager();
		user.addWager(user.getRoundWager());	// 还是计算一下wager吧，用于保存记录
		user.setRoundWager(0);
		user.setAction("放弃");
		log.add("[荷官]" + (user.getSeat() + 1) + "座[放弃]");
	}

	// 检查下一个动作的玩家是否超时
	public synchronized void checkTime() {
		if(status != 1)
			return;
		if(actTime < System.currentTimeMillis()) {
			TexasUser user = users[current];
			act(5);
			user.drop++;
			if(user.drop > 1)	// 连续超过两次掉线
				leaveSeat(user.getSeat());	// 这个时候current可能指向其他玩家了
		}
	}
	
	// 获得userbean，注意：texasuser和texasuserbean不同，前者是在游戏中存在，后者是用户数据，需要保存在数据库
	public static TexasUserBean getUserBean(int userId) {
		Integer key = new Integer(userId);
		synchronized(texasUserCache) {
			TexasUserBean user = (TexasUserBean)texasUserCache.get(key);
			if(user == null) {
				user = service.getTexasUserBean("user_id=" + userId);
				if(user != null)
					texasUserCache.put(key, user);
			}
			
			return user;
		}
	}
	public static TexasUserBean addTexasUserBean(int userId) {
		Integer key = new Integer(userId);
		TexasUserBean user = new TexasUserBean();
		user.setUserId(userId);
		user.setMoney(2000);
		service.addTexasUserBean(user);
		texasUserCache.spt(key, user);
		return user;
	}
	// 如果是积分桌，把对应的积分计入用户积分和数据库
	public static void addUserMoneyDB(int userId, int money) {
		TexasUserBean tub = getUserBean(userId);
		if(tub != null) {
			tub.addMoney(money);
			SqlUtil.executeUpdate("update texas_user set money=" + tub.getMoney() + " where user_id=" + userId, 5);
		}
	}
	
	public List getWinUsers() {
		return winUsers;
	}
	public void setWinUsers(List winUsers) {
		this.winUsers = winUsers;
	}
	// 可以进入下一局
	public boolean canNext() {
		return System.currentTimeMillis() > nextTime && userCount > 1;
	}
	// 剩余几秒可以开始
	public int getTimeLeft() {
		long left = nextTime - System.currentTimeMillis();
		if(left < 0)
			return 0;
		return (int)(left / 1000);
	}
	// 剩余几秒判断为放弃
	public int getWaitTimeLeft() {
		return (int)((actTime - System.currentTimeMillis()) / 1000);
	}
	public int getGiveupWager() {
		return giveupWager;
	}
	public void setGiveupWager(int giveupWager) {
		this.giveupWager = giveupWager;
	}
	public int getRoundWager() {
		return roundWager;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setRoundWager(int roundWager) {
		this.roundWager = roundWager;
	}
	public int getRoundUserCount() {
		return roundUserCount;
	}
	public void setRoundUserCount(int roundUserCount) {
		this.roundUserCount = roundUserCount;
	}
	public int getTotalWager() {
		return wager + giveupWager;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getMaxUser() {
		return MAX_USER;
	}
	public void setMaxUser(int maxUser) {
		this.MAX_USER = maxUser;
	}
	public int getBaseWager() {
		return baseWager;
	}
	public void setBaseWager(int baseWager) {
		this.baseWager = baseWager;
	}
	
	public String getRoundName() {
		return roundNames[round];
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public SimpleGameLog getLog() {
		return log;
	}
	public void setLog(SimpleGameLog log) {
		this.log = log;
	}
	public long getActTime() {
		return actTime;
	}
	public void setActTime(long actTime) {
		this.actTime = actTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGameUserCount() {
		return gameUserCount;
	}
	public void setGameUserCount(int gameUserCount) {
		this.gameUserCount = gameUserCount;
	}
	public static DecimalFormat codeFormat = new DecimalFormat("TE00000");
	// 历史记录的编号
	public String getCode() {
		return codeFormat.format(id & 0xFFFF);
	}
//	 数据库载入的历史记录，需要重新计算一些数据
	public void load() {
		// TODO Auto-generated method stub
		
	}
	public List getRoundUsers() {
		return roundUsers;
	}
	public void setRoundUsers(List roundUsers) {
		this.roundUsers = roundUsers;
	}
	public int getActUserCount() {
		return actUserCount;
	}
	public void setActUserCount(int actUserCount) {
		this.actUserCount = actUserCount;
	}
	public int[] getAllCards() {
		return allCards;
	}
	public void setAllCards(int[] allCards) {
		this.allCards = allCards;
	}
	public int getGetCardIndex() {
		return getCardIndex;
	}
	public void setGetCardIndex(int getCardIndex) {
		this.getCardIndex = getCardIndex;
	}
	public int getSmallWager() {
		return smallWager;
	}
	public void setSmallWager(int smallWager) {
		this.smallWager = smallWager;
	}
}

