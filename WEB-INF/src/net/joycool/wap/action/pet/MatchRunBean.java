package net.joycool.wap.action.pet;

import java.util.HashMap;

import net.joycool.wap.util.SimpleGameLog;

public class MatchRunBean {
	// 游戏的id
	int id;

	// 比赛类型
	int type;

	// 玩家人数
	int peoplenumber = 0;

	// 玩家的bean
	PlayerBean player[];

	// 当前游戏状态8
	int Condition;

	// 比赛动态信息
	SimpleGameLog log = new SimpleGameLog();

	// 比赛标题信息
	String message = "";

	// 比赛玩家个数
	int PLAYNUMBER;

	// 创建时间
	long createtime;

	public MatchRunBean(int temp) {
		PLAYNUMBER = temp;
		player = new PlayerBean[PLAYNUMBER];
		// 设定创立时间
		this.setCreatetime(System.currentTimeMillis());
	}

	// 宠物赌注比赛专用,总赌注
	HashMap noticMap = new HashMap();

	// 宠物赌注比赛专用,奖金
	HashMap winMap = new HashMap();
//本局的赌注总额
	long totalstake = 0;
	//本局参赌人数
	int totalpeople = 0;
	//冠军赔率
	float winrate = 0.0f;
	//冠军奖金
	long win1= 0;
	//第二名奖金
	long win2= 0;
	//第三名奖金
	long win3= 0;
	// 获得奖金最多的前三人信息
	String winner= "";
	// 增加一个宠物玩家
	public boolean addPlayer(PetUserBean petUser) {
		if (peoplenumber < PLAYNUMBER) {
			// 循环找一个空位置
			for (int i = 0; i < PLAYNUMBER; i++) {
				// 空位置
				if (player[i] == null) {
					// 将宠物玩家放入玩家列表
					player[i] = new PlayerBean(petUser);
					// 宠物玩家数量加一
					peoplenumber++;
					break;
				}
			}
			return true;
		} else
			return false;
	}

	public boolean exitPlayer(PetUserBean petUser) {
		for (int i = 0; i < player.length; i++) {
			if ((player[i] != null)&&(player[i].getPetid() == petUser.getId())) {
				player[i] = null;
				peoplenumber--;
				return true;
			}
		}
		// 等待游戏的玩家数量减少一个
		return false;
	}

	public String toString() {
		return log.getLogString(5);
	}

	public void addLog(String content) {
		log.add(content);
	}

	public int getCondition() {
		return Condition;
	}

	public void setCondition(int condition) {
		Condition = condition;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeoplenumber() {
		return peoplenumber;
	}

	public void setPeoplenumber(int peoplenumber) {
		this.peoplenumber = peoplenumber;
	}

	public PlayerBean[] getPlayer() {
		return player;
	}

	public void setPlayer(PlayerBean[] player) {
		this.player = player;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HashMap getNoticMap() {
		return noticMap;
	}

	public void setNoticMap(HashMap noticMap) {
		this.noticMap = noticMap;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HashMap getWinMap() {
		return winMap;
	}

	public void setWinMap(HashMap winMap) {
		this.winMap = winMap;
	}

	public long getTotalstake() {
		return totalstake;
	}

	public void setTotalstake(long totalstake) {
		this.totalstake = totalstake;
	}

	public int getTotalpeople() {
		return totalpeople;
	}

	public void setTotalpeople(int totalpeople) {
		this.totalpeople = totalpeople;
	}

	public long getWin1() {
		return win1;
	}

	public void setWin1(long win1) {
		this.win1 = win1;
	}

	public long getWin2() {
		return win2;
	}

	public void setWin2(long win2) {
		this.win2 = win2;
	}

	public long getWin3() {
		return win3;
	}

	public void setWin3(long win3) {
		this.win3 = win3;
	}

	public float getWinrate() {
		return winrate;
	}

	public void setWinrate(float winrate) {
		this.winrate = winrate;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}
}
