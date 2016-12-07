package jc.family.game.vs;

import java.util.Date;
import net.joycool.wap.util.DateUtil;
import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;

public class VsBean {

	int fmId; // 家族id
	int accept;// 收到的挑战
	boolean challenge;// 是否发出挑战
	int time;// 本日次数

	long challTime;// 时间
	int challFmid;// 家族
	int challGameId;// 类型
	int challengeId;// 挑战ID

	public static int maxGameTime = 10;
	public static int noScore = 4;
	public static int minNumber = 10;

	int gameid;// 游戏ID
	
	int[] gameTime=new int[10];	// 支持最多10个游戏？

	public VsBean() {

	}

	public VsBean(int fmId) {
		this.fmId = fmId;
	}

	public int getFmId() {
		return fmId;
	}

	public void setFmId(int fmId) {
		this.fmId = fmId;
	}

	public int getAccept() {
		return accept;
	}

	public void setAccept(int accept) {
		this.accept = accept;
	}

	public boolean isChallenge() {
		return challenge;
	}

	public void setChallenge(boolean challenge) {
		this.challenge = challenge;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public long getChallTime() {
		return challTime;
	}

	public String getChallTimeSfd() {
		return DateUtil.formatTime(new Date(challTime));
	}

	public void setChallTime(long challTime) {
		this.challTime = challTime;
	}

	public int getChallFmid() {
		return challFmid;
	}

	public String getChallFmidNameWml() {
		FamilyHomeBean fmHome = FamilyAction.getFmByID(challFmid);
		if (fmHome == null) {
			return "";
		}
		return fmHome.getFm_nameWml();
	}

	public void setChallFmid(int challFmid) {
		this.challFmid = challFmid;
	}

	public int getChallGameId() {
		return challGameId;
	}

	public String getChallGameIdName() {
		return VsGameBean.getGameIdName(challGameId);
	}

	public void setChallGameId(int fhallGameId) {
		this.challGameId = fhallGameId;
	}

	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}

	public int[] getGameTime() {
		return gameTime;
	}

	public void setGameTime(int[] gameTime) {
		this.gameTime = gameTime;
	}

	public int getGameid() {
		return gameid;
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

}
