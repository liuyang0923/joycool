package jc.game.texas;

/**
 * @author bombzj
 * @datetime 2010-1-3 
 * @explain 德州扑克里每个玩家在游戏时候的数据
 */
public class TexasUser {
	int id;	// 数据库id
	long createTime;	// 加入时间
	int userId;
	int seat;		// 在texasgame中的索引
	int turn;		// 第几轮
	int[] userCards = new int[2];	// 用户的底牌，两张
	int status;		// 0 未加入 1 游戏中 2 已退出
	public static int STATUS_PLAY = 1;
	public static int STATUS_READY = 0;
	public static int STATUS_END = 2;

	public boolean isStatusPlay() {
		return status == STATUS_PLAY;
	}
	
	// 保存排的分析结果
//	String info;		// 牌的描述
	int type;			// 牌型
	int score;			// 牌分值，最高位是以下分值，之后每4位保存一个数值（最多是散牌等需要5个）
	int[] finalCards;	// 最佳组合的牌
	int wager;	// 本局共下了多少注
	int roundWager;		// 这轮下了多少注，一轮结束后加入到wager
	
	int money;		// 携带资金数量
	String action;		// 最后一个动作的描述，例如加注
	
	int winWager;		// 赢得的筹码
	
	int drop;		// 掉线次数，如果做过一个操作则重置
/*
 * type : 
	
10	皇家同花顺(royal flush)：由AKQJ10五张组成，并且这5张牌花色相同
9　　同花顺(straight flush)：由五张连张同花色的牌组成
8　　4条(four of a kind)：4张同点值的牌加上一张其他任何牌
7　　葫芦(full house)：3张同点值加上另外一对
6　　同花(flush)：5张牌花色相同，但是不成顺子
5　　顺子(straight)：五张牌连张，至少一张花色不同
4　　3条(three of a kind)：三张牌点值相同，其他两张各异
3　　两对(two pairs)：两对加上一个杂牌
2　　一对(one pair)：一对加上3张杂牌
1	散牌(high card)：不符合上面任何一种牌型的牌型	

*/
	
	public int getWinWager() {
		return winWager;
	}

	public void setWinWager(int winWager) {
		this.winWager = winWager;
	}

	// 计算自己的排值
	public void calc(int[] cards) {
		finalCards = new int[5];

		byte[] color = new byte[4];	// 四种花色的牌数量
		byte[] point = new byte[13];	// 各种点数的牌的数量
		int[][] points = new int[13][];	// 牌按点数分配
		int[] c = {cards[0], cards[1], cards[2], cards[3], cards[4], userCards[0], userCards[1]};	// 七张牌
		int[] c2 = {c[0] % 13, c[1] % 13, c[2] % 13, c[3] % 13, c[4] % 13, c[5] % 13, c[6] % 13}; 	// 去掉花色，0-12
		// 排序，对c c2同时以c2排序
		for(int i = 0;i < 6;i++)
			for(int j = i;j >= 0;j--)
				if(c2[j] < c2[j + 1]) {
					int tmp = c2[j];
					c2[j] = c2[j + 1];
					c2[j + 1] = tmp;
					tmp = c[j];
					c[j] = c[j + 1];
					c[j + 1] = tmp;
				}
		
		int[] c3 = {c[0] / 13, c[1] / 13, c[2] / 13, c[3] / 13, c[4] / 13, c[5] / 13, c[6] / 13};	// 花色
		
		
		int strt = 0;	// 顺子数量
		int last2 = 100;
		int[] sCards = new int[5];
		
		int strt2 = 0;	// 同花顺子数量
		int last22 = 100;
		int[] sCards2 = new int[5];
		
		
		for(int i = 0;i < 7;i++) {
			int pt = c2[i];
			color[c3[i]]++;	// 同花更新数量
			
			if(point[pt] == 0) {
				points[pt] = new int[4];
				points[pt][0] = c[i];
			} else {
				points[pt][point[pt]] = c[i];
			}
			point[pt]++;	// 点数牌更新数量

				
			
			if(strt < 5) {		// 超过5张不再计算顺子
				if(last2 == c2[i] + 1) {
					last2 = c2[i];
					
					strt++;
					sCards[5 - strt] = c[i];
				} else if(last2 > c2[i]) {
					last2 = c2[i];
					strt = 1;
					sCards[4] = c[i];
				}
			}
		}
		// 判断是否可以组成A2345顺子
		if(strt == 4 && last2 == 0 && c2[0] == 12) {
			strt++;
			sCards[0] = c[0];
		}
		
		int[] maxCount = {-1, -1, -1, -1, -1, -1, -1};	// 相同点数的数量大于1的，比如3带2就是3，2
		int[] maxCountPoint = {-1, -1, -1, -1, -1, -1, -1};
		int maxCountIndex = 0;
		// 计算点数数量，大于0张的挑出来
		for(int i = 0;i < 13;i++)
			if(point[i] > 0) {
				maxCount[maxCountIndex] = point[i];
				maxCountPoint[maxCountIndex] = i;
				maxCountIndex++;
			}
		for(int i = 0;i < maxCountIndex;i++)
			for(int j = i - 1;j >= 0;j--)
				if(maxCount[j] < maxCount[j + 1] || 
						maxCount[j] == maxCount[j + 1] && maxCountPoint[j] < maxCountPoint[j + 1]) {
					int tmp = maxCount[j];
					maxCount[j] = maxCount[j + 1];
					maxCount[j + 1] = tmp;
					tmp = maxCountPoint[j];
					maxCountPoint[j] = maxCountPoint[j + 1];
					maxCountPoint[j + 1] = tmp;
				}
			
		
		
//		 计算同花
		int flush = -1;	
		for(int i = 0;i < 4;i++)
			if(color[i] > 4) {
				flush = i;
				break;
			}
		if(flush != -1) {
			for(int i = 0;i < 7;i++) {
				if(strt2 < 5 && c3[i] == flush) {		// 超过5张或者不是同花的花色不再计算顺子
					if(last22 == c2[i] + 1) {
						last22 = c2[i];
						
						strt2++;
						sCards2[5 - strt2] = c[i];
					} else if(last22 > c2[i]) {
						last22 = c2[i];
						strt2 = 1;
						sCards2[4] = c[i];
					}
				}
			}
			// 判断是否可以组成A2345同花顺
			if(strt2 == 4 && last22 == 0) {
				int lastA = -1;
				for(int i = 0;i < 7;i++) {
					if(c2[i] != 12)
						break;
					else if(c3[i] == flush) {
						lastA = c[i];
						break;
					}
				}
				if(lastA != -1) {
					strt2++;
					sCards2[0] = lastA;
				}
			}
		}
		if(strt2 == 5) {
			if(sCards2[0] % 13 == 8)
				type = 10;
			else
				type = 9;
			
			finalCards = sCards2;
			score = finalCards[2] % 13;	// 用顺子的中间一张的大小来决定胜负（如果用第一张，会有A=12这个差别，A2345比23456要小，但是finalCards里保存的信息包含花色！
		} else if(maxCount[0] == 4) {
			type = 8;
			if(maxCount[1] == 2 && maxCountPoint[1] < maxCountPoint[2]) {	// 4 2 1这种情况，如果那个1的点数大于2，要取1
				maxCountPoint[1] = maxCountPoint[2];
			}
			finalCards[0] = points[maxCountPoint[0]][0];
			finalCards[1] = points[maxCountPoint[0]][1];
			finalCards[2] = points[maxCountPoint[0]][2];
			finalCards[3] = points[maxCountPoint[0]][3];
			finalCards[4] = points[maxCountPoint[1]][0];
			score = maxCountPoint[0] << 4 | maxCountPoint[1];
		} else if(maxCount[0] == 3 && maxCount[1] >= 2) {
			type = 7;
			finalCards[0] = points[maxCountPoint[0]][0];
			finalCards[1] = points[maxCountPoint[0]][1];
			finalCards[2] = points[maxCountPoint[0]][2];
			finalCards[3] = points[maxCountPoint[1]][0];
			finalCards[4] = points[maxCountPoint[1]][1];
			score = maxCountPoint[0] << 4 | maxCountPoint[1];
		} else if(flush != -1) {
			type = 6;
			int j = 0;
			for(int i = 0;i < 7;i++)
				if(flush == c3[i]) {
					finalCards[j ] = c[i];
					maxCountPoint[j] = c2[i];
					if(j == 4)	break; else j++;
				}
			score = maxCountPoint[0] << 16 | maxCountPoint[1] << 12 | maxCountPoint[2] << 8 | maxCountPoint[3] << 4 | maxCountPoint[4];
		} else if(strt == 5) {
			type = 5;
			finalCards = sCards;
			score = finalCards[2] % 13;
		} else if(maxCount[0] == 3) {
			type = 4;
			finalCards[0] = points[maxCountPoint[0]][0];
			finalCards[1] = points[maxCountPoint[0]][1];
			finalCards[2] = points[maxCountPoint[0]][2];
			finalCards[3] = points[maxCountPoint[1]][0];
			finalCards[4] = points[maxCountPoint[2]][0];
			score = maxCountPoint[0] << 8 | maxCountPoint[1] << 4 | maxCountPoint[2];
		} else if(maxCount[0] == 2) {
			if(maxCount[1] == 2) {
				type = 3;
				if(maxCount[2] == 2 && maxCountPoint[2] < maxCountPoint[3]) {	// 2 2 2 1这种情况，如果那个1的点数大于2，要取1
					maxCountPoint[2] = maxCountPoint[3];
				}
				finalCards[0] = points[maxCountPoint[0]][0];
				finalCards[1] = points[maxCountPoint[0]][1];
				finalCards[2] = points[maxCountPoint[1]][0];
				finalCards[3] = points[maxCountPoint[1]][1];
				finalCards[4] = points[maxCountPoint[2]][0];
				score = maxCountPoint[0] << 8 | maxCountPoint[1] << 4 | maxCountPoint[2];
			} else {
				type = 2;
				finalCards[0] = points[maxCountPoint[0]][0];
				finalCards[1] = points[maxCountPoint[0]][1];
				finalCards[2] = points[maxCountPoint[1]][0];
				finalCards[3] = points[maxCountPoint[2]][0];
				finalCards[4] = points[maxCountPoint[3]][0];
				score = maxCountPoint[0] << 12 | maxCountPoint[1] << 8 | maxCountPoint[2] << 4 | maxCountPoint[3];
			}
		} else {
			type = 1;
			finalCards[0] = points[maxCountPoint[0]][0];
			finalCards[1] = points[maxCountPoint[1]][0];
			finalCards[2] = points[maxCountPoint[2]][0];
			finalCards[3] = points[maxCountPoint[3]][0];
			finalCards[4] = points[maxCountPoint[4]][0];
			score = maxCountPoint[0] << 16 | maxCountPoint[1] << 12 | maxCountPoint[2] << 8 | maxCountPoint[3] << 4 | maxCountPoint[4];
		}
		
		score |= type << 20;
	}

	public static TexasUser testCalc(int[] cards) {
		TexasUser user = new TexasUser();
		user.userCards[0] = cards[5];
		user.userCards[1] = cards[6];
		user.calc(cards);
		return user;
	}

	public int[] getFinalCards() {
		return finalCards;
	}

	public void setFinalCards(int[] finalCards) {
		this.finalCards = finalCards;
	}
	public String getTypeName() {
		return TexasGame.typeNames[type];
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int[] getUserCards() {
		return userCards;
	}

	public void setUserCards(int[] userCards) {
		this.userCards = userCards;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getRoundWager() {
		return roundWager;
	}

	public void setRoundWager(int roundWager) {
		this.roundWager = roundWager;
	}

	public int getWager() {
		return wager;
	}

	public void setWager(int wager) {
		this.wager = wager;
	}

	public String getAction() {
		if(action == null)
			return "";
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	// 加注，扣除对应的注
	public void addWager(int add) {
		wager += add;
	}
	public void addRoundWager(int add) {
		roundWager += add;
		money -= add;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public void addWinWager(int add) {
		winWager += add;
	}

	public void addMoney(int add) {
		money += add;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
