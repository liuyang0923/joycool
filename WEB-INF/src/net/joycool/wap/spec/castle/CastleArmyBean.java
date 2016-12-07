package net.joycool.wap.spec.castle;

import java.util.Date;
import java.util.List;

import net.joycool.wap.util.DateUtil;

public class CastleArmyBean {
	int id;
	int cid;
	int at;		// 这个军队所在城堡的cid，如果是army2支援绿洲，则at是绿洲的pos
	CastleBean castle;
	int[] count = new int[ResNeed.soldierTypeCount + 1];	// 1保存的是type=1的士兵数量
//	int hero;		// 英雄数量
	// 英雄数量改为保存到count[0]
	CastleUserBean user;

	public CastleUserBean getUser() {
		return user;
	}
	public void setUser(CastleUserBean user) {
		this.user = user;
	}
	public int getHero() {
		return count[0];
	}
	public void setHero(int hero) {
		count[0] = hero;
	}
	public void mergeCount(CastleArmyBean army) {
		int[] add = army.getCount();
		for(int i = 0;i < count.length;i++)
			count[i] += add[i];
	}
	// 判断是否有足够的兵力
	public boolean isEnough(int[] c) {
		for(int i = 0;i < count.length && i < c.length;i++) {
			if(c[i] > count[i])
				return false;
		}
		return true;
	}
	// 保存11个数字，c[0]保存hero的值
	public boolean decrease(int[] c) {
		if(isEnough(c)) {
			for(int i = 0;i < count.length && i < c.length;i++) {
				count[i] -= c[i];
			}
			return true;
		}
		return false;
	}
	// 这支军队不存在士兵了
	public boolean isEmpty() {
		for(int i = 0;i < count.length;i++) {
			if(count[i] != 0)
				return false;
		}
		return true;
	}
	
	public void setCount(int type, int c) {
		count[type] = c;
	}
	public int getCount(int type) {
		return count[type];
	}
	public void addCount(int type, int c) {
		count[type] += c;
		if(count[type] < 0)
			count[type] = 0;
	}
	public void addCount(int type) {
		addCount(type, 1);
	}
	
	public int getAt() {
		return at;
	}

	public void setAt(int at) {
		this.at = at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int[] getCount() {
		return count;
	}

	public void setCount(int[] count) {
		this.count = count;
	}
	public float[] calcDefense(int race, SoldierSmithyBean[] smithy) {
		float[] sum = {0f,0f};
		SoldierResBean[] g = ResNeed.getSoldierTs(race);
		for(int i = 1;i < count.length;i++) {
			int count = this.count[i];
			if(count > 0) {
				SoldierResBean s = g[i];
				if(smithy[i] != null) {
					sum[0] += s.getDefense() * ResNeed.attackFactor[smithy[i].getDefence()] * count;
					sum[1] += s.getDefense2() * ResNeed.attackFactor[smithy[i].getDefence()] * count;
				} else {
					sum[0] += s.getDefense() * ResNeed.attackFactor[0] * count;
					sum[1] += s.getDefense2() * ResNeed.attackFactor[0] * count;
				}
			}
				
		}
		return sum;
	}
	// 总侦察防御
	public int calcScoutDefense(int race, SoldierSmithyBean[] smithy) {
		int sum = 0;
		SoldierResBean[] g = ResNeed.getSoldierTs(race);
		for(int i = 1;i < count.length;i++) {
			int count = this.count[i];
			if(count > 0) {
				SoldierResBean s = g[i];
				if(smithy[i] != null) {
					sum += s.getDefense3() * ResNeed.attackFactor[smithy[i].getDefence()] * count;
				} else {
					sum += s.getDefense3() * ResNeed.attackFactor[0] * count;
				}
			}
				
		}
		return sum;
	}
	public int getGrainCost(SoldierResBean[] so) {
		int sum = 0;
		for(int i = 1;i<so.length; i++) {
			if(count[i] > 0)
				sum += so[i].getPeople() * count[i];
		}
		if(count[0] != 0)
			sum += count[0] * ResNeed.heroGrainCost;
		return sum;
	}
	public String getSoldierString(int race) {
		if(isEmpty())
			return "无";
		else {
			SoldierResBean[] gs = ResNeed.getSoldierTs(race);
			StringBuilder sb = new StringBuilder(32);
			for(int i = 1;i<gs.length; i++) {
				if(count[i] != 0) {
					sb.append(gs[i].getSoldierName());
					sb.append(count[i]);
				}
			}
			if(count[0] != 0) {
				sb.append("指挥官");
				sb.append(count[0]);
			}
			return sb.toString();
		}
	}
	public StringBuilder getSoldierString(int race, StringBuilder sb) {
		if(isEmpty())
			sb.append("无");
		else {
			SoldierResBean[] gs = ResNeed.getSoldierTs(race);
			for(int i = 1;i<gs.length; i++) {
				if(count[i] != 0) {
					sb.append(gs[i].getSoldierName());
					sb.append(count[i]);
				}
			}
			if(count[0] != 0) {
				sb.append("指挥官");
				sb.append(count[0]);
			}
		}
		return sb;
	}
	public CastleBean getCastle() {
		return castle;
	}
	public void setCastle(CastleBean castle) {
		this.castle = castle;
	}
	// 切分最多count数量的士兵到to，返回实际切分的数量（例如数量不足）
	public int split(int total, CastleArmyBean to) {
		int[] count2 = to.getCount();
		int left = total;
		for(int i = 1;i < count.length;i++)
			if(count[i] > 0) {
				if(count[i] < left) {
					count2[i] += count[i];
					left -= count[i];
					count[i] = 0;
				} else {
					count2[i] += left;
					count[i] -= left;
					return total;
				}
			}
		return total - left;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 1;i < count.length;i++) {
			if(i != 1)
				sb.append(',');
			sb.append(count[i]);
		}
		return sb.toString();
	}
	public int splitFrom(int total, List soldiers) {
		int left = total;
		for(int i = 0;i < soldiers.size();i++) {
			int c = ((Integer)soldiers.get(i)).intValue();
			if(c > 0) {
				if(c < left) {
					soldiers.set(i, Integer.valueOf(0));
					left -= c;
					count[i + 1] = c;
				} else {
					soldiers.set(i, Integer.valueOf(c - left));
					count[i + 1] = left;
					return total;
				}
			}
		}
		return total - left;
	}
	public void addHero(int add) {
		count[0] += add;
	}
	// 军队总人数
	public int getTotalCount() {
		return count[0] + count[1] + count[2] + count[3] + count[4] + count[5] + 
			count[6] + count[7] + count[8] + count[9] + count[10];
	}
}
