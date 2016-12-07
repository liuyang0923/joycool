package net.joycool.wap.spec.castle;

import java.text.DecimalFormat;
import java.util.List;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;

public class UserResBean {
	public final static int FLAG_WOOD = (1 << 0);		// 木头产量增加25%
	public final static int FLAG_STONE = (1 << 1);		// 木头产量增加25%
	public final static int FLAG_FE = (1 << 2);			// 木头产量增加25%
	public final static int FLAG_GRAIN = (1 << 3);		// 木头产量增加25%
	public final static int FLAG_ATTACK = (1 << 4);		// 木头产量增加25%
	public final static int FLAG_DEFENSE = (1 << 5);	// 木头产量增加25%
	public final static int FLAG_ART = (1 << 6);	// 粮食消耗减少50%
	public final static int FLAG_ART2 = (1 << 7);	// 粮食消耗减少25%
	int id;	// cid
	int userId;
	long time;
	int wood;
	int woodSpeed;		//木头的增加速度
	int otherWoodSpeed;
	int fe;				//铁的增加速度
	int feSpeed;
	int otherFeSpeed;
	int grain;			//粮食增长速度
	int grainSpeed;
	int stone;
	int stoneSpeed;		//石头增长速度
	int otherGrainSpeed;
	int otherStoneSpeed;
	int civil = 2;
	int maxRes = 800;
	int maxGrain = 800;
	int maxPeople;
	int cave;		// 山洞隐藏的资源
	int people = 2;		// 居民，建筑物的人口消耗，初始位2，城市中心
	int people2;	// 兵的粮食消耗
	int wall;				// 城墙百分比
	int merchant;
	int flag;
	int trap;		// 造的陷阱数量
	
	long loyalTime;
	int loyalSpeed;	// 忠诚度随着时间增加，每小时增加这个数字，应该等于行宫、皇宫级别乘以一万
	int loyal;		// 忠诚度，最大为1000000
	public static int MAX_LOYAL = 1000000;
	
	byte[] buildings = null;		// 每个类型的建筑最高等级
// 根据城堡级别换算建筑时间
	public int calcBuildTime(int time2) {
		return (int) (ResNeed.getGradeTime(getBuildingGrade(4), time2));
	}
	public int getBuildingGrade(int type) {
		return getBuildings()[type];
	}
	public byte[] getBuildings() {
		if(buildings == null) {
			synchronized(this) {
				buildings = CacheService.getUserBuildings(id);
			}
		}
		return buildings;
	}
	// 建筑建造完成，更新最高等级数据
	public void updateBuilding(int type, int grade) {
		byte[] buildings = getBuildings();
		if(buildings[type] < grade)
			buildings[type] = (byte)grade;
	}
	public void dupdateBuilding(int type, int grade) {
		byte[] buildings = getBuildings();
		if(buildings[type] > grade)
			buildings[type] = (byte)grade;
	}

	public void setBuildings(byte[] buildings) {
		this.buildings = buildings;
	}

	public int getCave() {
		return cave;
	}

	public void setCave(int cave) {
		this.cave = cave;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public UserResBean(){}
	
	public UserResBean(int userId, int type){
		int[] initResSpeed = ResNeed.initResSpeed[type];
		this.userId = userId;
		this.time = System.currentTimeMillis();
		this.wood = 750;
		this.woodSpeed = initResSpeed[0];
		this.stone = 750;
		this.stoneSpeed = initResSpeed[1];
		this.fe = 750;
		this.feSpeed = initResSpeed[2];
		this.grain = 750;
		this.grainSpeed = initResSpeed[3];
	}
	
	public UserResBean(int userId,
			int otherWoodSpeed, int otherFeSpeed, int otherGrainSpeed,int otherStoneSpeed) {
		this.userId = userId;
		this.time = System.currentTimeMillis();
		this.wood = 750;
		this.woodSpeed = 8;
		this.otherWoodSpeed = otherWoodSpeed;
		this.fe = 750;
		this.feSpeed = 8;
		this.otherFeSpeed = otherFeSpeed;
		this.grain = 750;
		this.grainSpeed = 12;
		this.otherGrainSpeed = otherGrainSpeed;
		this.stone = 750;
		this.stoneSpeed = 8;
		this.otherStoneSpeed = otherStoneSpeed;
	}
	
	public int getFeSpeed() {
		return feSpeed;
	}
	

	public void setFeSpeed(int feSpeed) {
		this.feSpeed = feSpeed;
		if(this.feSpeed < 0)
			this.feSpeed = 0;
	}

	public int getGrainSpeed() {
		return grainSpeed;
	}
	

	public void setGrainSpeed(int grainSpeed) {
		this.grainSpeed = grainSpeed;
		if(this.grainSpeed < 0)
			this.grainSpeed = 0;
	}

	public int getWoodSpeed() {
		return woodSpeed;
	}
	

	public void setWoodSpeed(int woodSpeed) {
		this.woodSpeed = woodSpeed;
		if(this.woodSpeed < 0)
			this.woodSpeed = 0;
	}

	
	public void setFe(int fe) {
		this.fe = fe;
	}
	public int getGrainRealSpeed() {
		return grainSpeed + grainSpeed * otherGrainSpeed / 100 - people;
	}
	public int getGrainSpeed2() {
		return grainSpeed + grainSpeed * otherGrainSpeed / 100;
	}
	
	
	public int getStoneSpeed2() {
		if(!isFlagStone())
			return stoneSpeed + stoneSpeed * otherStoneSpeed / 100;
		else 
			return (stoneSpeed + stoneSpeed * otherStoneSpeed / 100) * 5 / 4;
	}
	
	public int getFeSpeed2() {
		if(!isFlagFe())
			return feSpeed + feSpeed * otherFeSpeed / 100;
		else 
			return (feSpeed + feSpeed * otherFeSpeed / 100) * 5 / 4;
	}
	
	public int getWoodSpeed2() {
		if(!isFlagWood())
			return woodSpeed + woodSpeed * otherWoodSpeed / 100;
		else
			return (woodSpeed + woodSpeed * otherWoodSpeed / 100) * 5 / 4;
	}
	public int getGrainRealSpeed2() {
		// 粮食产量要扣除建筑物和兵的消耗
		int people2x;
		if(isFlagArt())
			people2x = (people2 + 1) >> 1;
		else if(isFlagArt2())
			people2x = (people2 * 3 + 2) / 4;
		else
			people2x = people2;	
		if(!isFlagGrain()) 
			return grainSpeed + grainSpeed * otherGrainSpeed / 100 - people - people2x;
		else 
			return (grainSpeed + grainSpeed * otherGrainSpeed / 100) * 5 / 4 - people - people2x;
	}
	
	public void setGrain(int grain) {
		this.grain = grain;
	}
	
	public int getFe(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int fe = this.fe + (int)(getFeSpeed2() * hours);
		if(fe > maxRes)
			return maxRes;
		return fe;
	}
	
	public int getGrain(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int speed = getGrainRealSpeed2();
		int g = this.grain + (int)(speed * hours);
		if(g > maxGrain)
			return maxGrain;
		else if(g < 0) {
			if(people2 > 0) {	// 粮食仓库为负，士兵死亡
				return -CastleUtil.starv(this, -g, hours);
			}
			return 0;
		}
		return g;
	}
	public int getGrainOver(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int speed = getGrainRealSpeed2();
		int g = this.grain + (int)(speed * hours);
		return g - maxGrain;
	}
	
	public int getWood(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int w = this.wood + (int)(getWoodSpeed2() * hours) ;
		if(w > maxRes)
			return maxRes;
		return w;
	}
	
	public int getStone(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int s = this.stone + (int)(getStoneSpeed2() * hours) ;		
		if(s > maxRes)
			return maxRes;
		return s;
	}
	
	public void setWood(int wood) {
		this.wood = wood;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFe() {
		return fe;
	}

	public int getGrain() {
		return grain;
	}

	public int getWood() {
		return wood;
	}
	
	public int getOtherFeSpeed() {
		return otherFeSpeed;
	}

	public void setOtherFeSpeed(int otherFeSpeed) {
		this.otherFeSpeed = otherFeSpeed;
		if(this.otherFeSpeed < 0)
			this.otherFeSpeed = 0;
	}

	public int getOtherGrainSpeed() {
		return otherGrainSpeed;
	}

	public void setOtherGrainSpeed(int otherGrainSpeed) {
		this.otherGrainSpeed = otherGrainSpeed;
		if(this.otherGrainSpeed < 0)
			this.otherGrainSpeed = 0;
	}

	public int getOtherWoodSpeed() {
		return otherWoodSpeed;
	}

	public void setOtherWoodSpeed(int otherWoodSpeed) {
		this.otherWoodSpeed = otherWoodSpeed;
		if(this.otherWoodSpeed < 0)
			this.otherWoodSpeed = 0;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		this.stone = stone;
	}

	public int getStoneSpeed() {
		return stoneSpeed;
	}

	public void setStoneSpeed(int stoneSpeed) {
		this.stoneSpeed = stoneSpeed;
		if(this.stoneSpeed < 0)
			this.stoneSpeed = 0;
	}

	public int getOtherStoneSpeed() {
		return otherStoneSpeed;
	}

	public void setOtherStoneSpeed(int otherStoneSpeed) {
		this.otherStoneSpeed = otherStoneSpeed;
		if(this.otherStoneSpeed < 0)
			this.otherStoneSpeed = 0;
	}
	
	public int getCivil() {
		return civil;
	}

	public void setCivil(int civil) {
		this.civil = civil;
	}

	public int getMaxRes() {
		return maxRes;
	}

	public void setMaxRes(int maxRes) {
		this.maxRes = maxRes;
	}

	public int getMaxGrain() {
		return maxGrain;
	}

	public void setMaxGrain(int maxGrain) {
		this.maxGrain = maxGrain;
	}

	public int getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(int maxPeople) {
		this.maxPeople = maxPeople;
	}

	public void reCalc(long now) {
		setWood(getWood(now));
		setFe(getFe(now));
		setGrain(getGrain(now));
		setStone(getStone(now));
		setTime(now);
	}

	public boolean canBuild(BuildingTBean bt) {
		byte[] bs = getBuildings();
		List list = bt.getPreList();
		for(int i = 0;i < list.size();i++) {
			int[] is = (int[])list.get(i);
			if(is[1] == 0) {
				if(bs[is[0]] > 0)
					return false;
			} else {
				if(bs[is[0]] < is[1])
					return false;
			}
		}
		return true;
	}
	public boolean canBuild(List preList) {
		byte[] bs = getBuildings();
		for(int i = 0;i < preList.size();i++) {
			int[] is = (int[])preList.get(i);
			if(bs[is[0]] < is[1])
				return false;
		}
		return true;
	}
	public int getPeople2() {
		return people2;
	}
	public int getPeople2x() {
		if(isFlagArt())
			return (people2 + 1) >> 1;
		else if(isFlagArt2())
			return (people2 * 3 + 2) / 4;
		return people2;
	}
	public void setPeople2(int people2) {
		this.people2 = people2;
	}
	public void addPeople2(int add) {
		people2 += add;
		if(people2 < 0) {
			people2 = 0;
		}
	}
	public void addPeople(int add) {
		people += add;
		if(people < 0) {
			people = 0;
		}
	}
	public int getWall() {
		return wall;
	}
	public void setWall(int wall) {
		this.wall = wall;
	}
	public void addCivil(int add) {
		civil += add;
		if(civil < 0)
			civil = 0;
	}
	// 判断并扣除资源
	static CastleService castleService = CastleService.getInstance();
	public boolean decreaseRes(int wood, int stone, int fe, int grain) {
		synchronized(this) {
			reCalc(System.currentTimeMillis());
			if(getWood() < wood || getFe() < fe || getStone() < stone || getGrain() < grain)
				return false;
			setWood(getWood() - wood);
			setFe(getFe() - fe);
			setGrain(getGrain() - grain);
			setStone(getStone() - stone);
			castleService.updateUserRes(this);
		}
		return true;
	}
	public void increaseRes(int wood, int stone, int fe, int grain) {
		synchronized(this) {
			this.wood += wood;
			this.stone += stone;
			this.fe += fe;
			this.grain += grain;
			
			long now = System.currentTimeMillis();
			int tmp;
			tmp = getGrainOver(now);
			if(tmp > 0)
				this.grain -= tmp;
			
			castleService.updateUserRes(this);
		}
	}
	// 增加或者减少某种资源
	public boolean decreaseRes(int type, int res) {
		synchronized(this) {
			reCalc(System.currentTimeMillis());
			switch(type) {
			case 1:
				if(getWood() < res)
					return false;
				setWood(getWood() - res);
				break;
			case 2:
				if(getStone() < res)
					return false;
				setStone(getStone() - res);
				break;
			case 3:
				if(getFe() < res)
					return false;
				setFe(getFe() - res);
				break;
			case 4:
				if(getGrain() < res)
					return false;
				setGrain(getGrain() - res);
				break;
			default:
				return false;
			}
			
			castleService.updateUserRes(this);
		}
		return true;
	}
	public void addPeople2Calc(int add, long now) {
		reCalc(now);
		addPeople2(add);
		SqlUtil.executeUpdate("update castle_user_resource set people2=" + people2
				+ " where id=" + id, 5);
	}
	public void increaseRes(int type, int res) {
		synchronized(this) {
			increaseResNS(type, res);
			castleService.updateUserRes(this);
		}
	}
	public void increaseResNS(int type, int res) {
		long now = System.currentTimeMillis();
		int tmp;
		switch(type) {
		case 1:
			this.wood += res;
			break;
		case 2:
			this.stone += res;
			break;
		case 3:
			this.fe += res;
			break;
		case 4:
			this.grain += res;
			tmp = getGrainOver(now);
			if(tmp > 0)
				this.grain -= tmp;
			break;
		default:
			return;
		}
	}
	// 判断资源是否足
	public boolean hasEnoughRes(int type, int res) {
		long now = System.currentTimeMillis();
		synchronized(this) {
			switch(type) {
			case 1:
				if(getWood(now) < res)
					return false;
				break;
			case 2:
				if(getStone(now) < res)
					return false;
				break;
			case 3:
				if(getFe(now) < res)
					return false;
				break;
			case 4:
				if(getGrain(now) < res)
					return false;
				break;
			default:
				return false;
			}
		}
		return true;
	}
	public int getMerchant() {
		return merchant;
	}
	public void setMerchant(int merchant) {
		this.merchant = merchant;
	}
	public void addMerchant(int add) {
		merchant += add;
		if(merchant < 0)
			merchant = 0;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlagWood() {
		return (flag & FLAG_WOOD) != 0;
	}
	public boolean isFlagStone() {
		return (flag & FLAG_STONE) != 0;
	}
	public boolean isFlagFe() {
		return (flag & FLAG_FE) != 0;
	}
	public boolean isFlagGrain() {
		return (flag & FLAG_GRAIN) != 0;
	}
	public boolean isFlagAttack() {
		return (flag & FLAG_ATTACK) != 0;
	}
	public boolean isFlagDefense() {
		return (flag & FLAG_DEFENSE) != 0;
	}
	public boolean isFlagArt() {
		return (flag & FLAG_ART) != 0;
	}
	public boolean isFlagArt2() {
		return (flag & FLAG_ART2) != 0;
	}
	public boolean isFlag(int flagValue) {
		return (flag & flagValue) != 0;
	}
	public void addFlag(int flagValue) {
		this.flag |= flagValue;
	}
	public void deleteFlag(int flagValue) {
		this.flag &= ~flagValue;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	// 获取士兵移动速度加成（竞技场）
	public int getSpeedAdd() {
		return getBuildingGrade(ResNeed.ARENA_BUILD) * 10;
	}
	public int getTrap() {
		return trap;
	}
	public void setTrap(int trap) {
		this.trap = trap;
	}
	// 增加陷阱并保存到数据库
	public void addTrapDB(int add) {
		trap += add;
		if(trap < 0)
			trap = 0;
		int max = getBuildingGrade(ResNeed.TRAP_BUILD) * 10;
		if(trap > max)
			trap = max;
		else
			SqlUtil.executeUpdate("update castle_user_resource set trap=" + trap
					+ " where id=" + id, 5);
	}
	// 增加或者减少一块绿洲，改变产量，add应该是25或者-25
	public void addOasis(int type, int add) {
		reCalc(System.currentTimeMillis());
		switch(type) {
		case 1: {
			otherWoodSpeed += add;
		} break;
		case 2: {
			otherStoneSpeed += add;
		} break;
		case 3: {
			otherFeSpeed += add;
		} break;
		case 4: {
			otherWoodSpeed += add;
			otherGrainSpeed += add;
		} break;
		case 5: {
			otherStoneSpeed += add;
			otherGrainSpeed += add;
		} break;
		case 6: {
			otherFeSpeed += add;
			otherGrainSpeed += add;
		} break;
		case 7: {
			otherGrainSpeed += add;
		} break;
		case 8: {
			otherGrainSpeed += add;
			otherGrainSpeed += add;
		} break;
		}
		castleService.updateUserResAll(this);
	}
	
	// 忠诚度
	public int getLoyal(long now) {
		if(loyal == MAX_LOYAL)
			return loyal;
		
		float hours = (float)((now - loyalTime)) / DateUtil.MS_IN_HOUR;
		int cur = loyal + (int)(loyalSpeed * hours);
		if(cur > MAX_LOYAL)
			cur = loyal = MAX_LOYAL;	// 已经到达极限，避免再次计算
		return cur;
	}
	public void reCalcLoyal(long now) {
		if(loyal == MAX_LOYAL)
			return;
		float hours = (float)((now - loyalTime)) / DateUtil.MS_IN_HOUR;
		
		loyal += loyalSpeed * hours;
		if(loyal > MAX_LOYAL)
			loyal = MAX_LOYAL;
		
		loyalTime = now;
	}
	// 减少忠诚度，dec是从0-100的整数，减少后保存到数据库
	public void decLoyal(float dec) {
		loyal -= dec * 10000;
		if(loyal < 0)
			loyal = 0;
		castleService.updateLoyal(this);
	}
	public int getLoyal() {
		return loyal;
	}
	public void setLoyal(int loyal) {
		this.loyal = loyal;
	}
	public int getLoyalSpeed() {
		return loyalSpeed;
	}
	public void setLoyalSpeed(int loyalSpeed) {
		this.loyalSpeed = loyalSpeed;
	}
	public long getLoyalTime() {
		return loyalTime;
	}
	public void setLoyalTime(long loyalTime) {
		this.loyalTime = loyalTime;
	}
	private static DecimalFormat numFormat = new DecimalFormat("0.0");
	public String getLoyalString() {
		if(loyal == MAX_LOYAL)
			return "100";
		return numFormat.format((float)getLoyal(System.currentTimeMillis()) / MAX_LOYAL);
	}
}
