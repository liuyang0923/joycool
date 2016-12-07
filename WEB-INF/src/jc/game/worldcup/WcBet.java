package jc.game.worldcup;

import java.text.DecimalFormat;

import net.joycool.wap.util.StringUtil;

/**
 * 用户投注
 */
public class WcBet {
	
	public static DecimalFormat numFormat = new DecimalFormat("0.0");
	
	int id;
	int userId;
	int matchId;
	String team;
	int bet;	//金额
	int flag;	//0:胜1:负2:平
	long betTime;
	int result;	//0:输1:赢2:尚无结果
	int point;
	int odds;
	public int getOdds() {
		return odds;
	}
	public String getOddsf() {
		return numFormat.format(odds / 100f);
	}
	public void setOdds(int odds) {
		this.odds = odds;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public String getTeam() {
		return team;
	}
	public String getTeamWml(){
		return StringUtil.toWml(team);
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public int getBet() {
		return bet;
	}
	public void setBet(int bet) {
		this.bet = bet;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getBetTime() {
		return betTime;
	}
	public void setBetTime(long betTime) {
		this.betTime = betTime;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
}