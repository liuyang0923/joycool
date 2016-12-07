package jc.family.game.pvz;

import java.util.*;
import net.joycool.wap.util.RandomUtil;
import jc.family.game.*;
import jc.family.game.vs.*;

public class PVZGameBean extends VsGameBean {
	
	public PVZGameBean() {
		startTime = System.currentTimeMillis() + 1 * 60 * 1000;
		endTime = startTime + 5 * 60 * 1000;
		plantProtoList = service.getPlantList("1");
		zombieProtoList = service.getZombieList("1");
		init();
	}

	public void init() {
		// 随机设置家族角色
		int rand = RandomUtil.nextInt(100);
		if (rand >= 50) {
			plantSide = 0;
			zombieSide = 1;
		} else {
			plantSide = 1;
			zombieSide = 0;
		}
		// 初始化大门
		for (int i = 0; i < gameMap[0].length; i++) {
			PVZBean pvzBean = new PVZBean();
			pvzBean.setState(1);
			gameMap[0][i] = pvzBean;
		}
		// 初始化其它地点
		for (int i = 1; i < gameMap.length; i++) {
			for (int j = 0; j < gameMap[i].length; j++) {
				gameMap[i][j] = new PVZBean();
			}
		}
	}

	public VsUserBean createUser() {
		return new PVZUserBean();
	}

	public void addUser(VsUserBean userNew) {
		if (isPanlt(userNew.getSide())) {
			userNew.setSide(0);
		} else {
			userNew.setSide(1);
		}
		if (userNew.getFmId() == getFmIdA()) {
			setFmCountA(getFmCountA() + 1);
		} else {
			setFmCountB(getFmCountB() + 1);
		}
		userMap.put(Integer.valueOf(userNew.getUserId()), userNew);
	}

	public String getGameUrl() {
		return "/fm/game/pvz/enter.jsp";
	}

	PVZGameTask task = new PVZGameTask();

	public boolean startGame() {
		if (!super.startGame())
			return false;
		task.setGame(this);
		GameAction.fmTimer.scheduleAtFixedRate(task, new Date(startTime), 20000);
		return true;
	}

	public boolean endGame() {
		if (!super.endGame()) {
			return false;
		}
		task.cancel();
		return true;
	}

	public int calcGameResult() {
		if (broken < 2)
			return plantSide;
		else
			return zombieSide;
	}

	private static final long serialVersionUID = 1L;
	public static int GType = 3;
	public static PVZService service = new PVZService();
	public List plantProtoList; // 原型植物列表
	public List zombieProtoList; // 原型僵尸列表
	
	public List getPlantProtoList() {
		return plantProtoList;
	}

	public void setPlantProtoList(List plantProtoList) {
		this.plantProtoList = plantProtoList;
	}

	public List getZombieProtoList() {
		return zombieProtoList;
	}

	public void setZombieProtoList(List zombieProtoList) {
		this.zombieProtoList = zombieProtoList;
	}

	public int turn;	// 回合数，10秒一回合，每回合运行calculate
	public void calculate() {
		turn++;
		plantSun += 25;
		if (turn < 120) // 开赛20分钟后不给僵尸方太阳
			zombieSun += ((int)Math.floor(Math.sqrt((turn + 3)*6)*10)/5)*5;
	}

	int plantSide;// 植物方
	int zombieSide;// 僵尸方
	int zombieNum; // 活着的僵尸数
	int plantNum; // 活着的植物数
	int zombieDieNum = 0; // 死亡僵尸数
	int plantDieNum = 0; // 死亡植物数
	int broken = 0; // 损坏的门数
	int zombieSun = 0; // 僵尸阳光数
	int plantSun = 100; // 植物阳光数
	Object[][] gameMap = new Object[10][5];// 游戏地图
	int[] zombieLineNum = new int[gameMap[0].length]; // 每列僵尸数量
	List plantUserList = new ArrayList();
	List zombieUserList = new ArrayList();
	LinkedList fightInformationList = new LinkedList();
	
	public int getPlantSide() {
		return plantSide;
	}

	public void setPlantSide(int plantSide) {
		this.plantSide = plantSide;
	}

	public int getZombieSide() {
		return zombieSide;
	}

	public void setZombieSide(int zombieSide) {
		this.zombieSide = zombieSide;
	}

	public int getZombieNum() {
		return zombieNum;
	}

	public void setZombieNum(int zombieNum) {
		this.zombieNum = zombieNum;
	}

	public int getPlantNum() {
		return plantNum;
	}

	public void setPlantNum(int plantNum) {
		this.plantNum = plantNum;
	}

	public int getZombieDieNum() {
		return zombieDieNum;
	}

	public void setZombieDieNum(int zombieDieNum) {
		this.zombieDieNum = zombieDieNum;
	}

	public int getPlantDieNum() {
		return plantDieNum;
	}

	public void setPlantDieNum(int plantDieNum) {
		this.plantDieNum = plantDieNum;
	}

	public int getBroken() {
		return broken;
	}

	public void setBroken(int broken) {
		this.broken = broken;
	}

	public int getZombieSun() {
		return zombieSun;
	}

	public void setZombieSun(int zombieSun) {
		this.zombieSun = zombieSun;
	}

	public int getPlantSun() {
		return plantSun;
	}

	public void setPlantSun(int plantSun) {
		this.plantSun = plantSun;
	}

	public Object[][] getGameMap() {
		return gameMap;
	}

	public void setGameMap(Object[][] gameMap) {
		this.gameMap = gameMap;
	}

	public int[] getZombieLineNum() {
		return zombieLineNum;
	}

	public void setZombieLineNum(int[] zombieLineNum) {
		this.zombieLineNum = zombieLineNum;
	}
	
	public List getPlantUserList() {
		return plantUserList;
	}

	public void setPlantUserList(List plantUserList) {
		this.plantUserList = plantUserList;
	}

	public List getZombieUserList() {
		return zombieUserList;
	}

	public void setZombieUserList(List zombieUserList) {
		this.zombieUserList = zombieUserList;
	}

	public LinkedList getFightInformationList() {
		return fightInformationList;
	}

	public void setFightInformationList(LinkedList fightInformationList) {
		this.fightInformationList = fightInformationList;
	}

	/**
	 * 获得列(僵尸最少)中僵尸的数量
	 * 
	 * @return
	 */
	public int getLestLineNum () {
		int temp = zombieLineNum[0];
		for (int i = 1; i < zombieLineNum.length; i++) {
			if (temp > zombieLineNum[i]) {
				temp = zombieLineNum[i];
			}
		}
		return temp;
	}
	
	/**
	 * 查看家族角色是否植物
	 * 
	 * @return
	 */
	public boolean isPanlt(int side) {
		return side == plantSide;
	}

}
