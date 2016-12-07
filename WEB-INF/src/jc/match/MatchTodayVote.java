package jc.match;

/**
 * 某参赛用户的今日靓点
 */
public class MatchTodayVote {
	int id;
	int userId;
	int voteCount;
	long voteTime;
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
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public long getVoteTime() {
		return voteTime;
	}
	public void setVoteTime(long voteTime) {
		this.voteTime = voteTime;
	}
}
