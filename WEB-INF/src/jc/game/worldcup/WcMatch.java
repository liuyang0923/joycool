package jc.game.worldcup;

import java.text.DecimalFormat;

import net.joycool.wap.util.StringUtil;

/**
 * 赛程表
 */
public class WcMatch {
	
	public static DecimalFormat numFormat = new DecimalFormat("0.0");
	
	int id;
	long matchTime;
	String matchTimeStr;
	String team1;	//队名1
	String team2;	//队名2
	int win;		//胜的赔率
	int lose;		//负
	int tie;		//平
	int score1;		//队1得分
	int score2;		//队2得分
	int show;		//0:不显示,1:显示
	int flag;		//1:关闭
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(long matchTime) {
		this.matchTime = matchTime;
	}
	public String getMatchTimeStr() {
		return matchTimeStr;
	}
	public void setMatchTimeStr(String matchTimeStr) {
		this.matchTimeStr = matchTimeStr;
	}
	public String getTeam1() {
		return team1;
	}
	public String getTeamWml1() {
		return StringUtil.toWml(team1);
	}
	public void setTeam1(String team1) {
		this.team1 = team1;
	}
	public String getTeam2() {
		return team2;
	}
	public String getTeamWml2() {
		return StringUtil.toWml(team2);
	}
	public void setTeam2(String team2) {
		this.team2 = team2;
	}
	public int getWin() {
		return win;
	}
	public String getWinf() {
		return numFormat.format(win / 100f);
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getLose() {
		return lose;
	}
	public String getLosef() {
		return numFormat.format(lose / 100f);
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
	public int getTie() {
		return tie;
	}
	public String getTief() {
		return numFormat.format(tie / 100f);
	}
	public void setTie(int tie) {
		this.tie = tie;
	}
	public int getScore1() {
		return score1;
	}
	public void setScore1(int score1) {
		this.score1 = score1;
	}
	public int getScore2() {
		return score2;
	}
	public void setScore2(int score2) {
		this.score2 = score2;
	}
	public int getShow() {
		return show;
	}
	public void setShow(int show) {
		this.show = show;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}

}
