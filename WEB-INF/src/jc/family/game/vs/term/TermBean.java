package jc.family.game.vs.term;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;

public class TermBean {

	int id;
	String name;
	int gameType;	// 游戏类型，1表示丛林
	String fmids;	// 参与的家族id列表string
	long createTime;	// 这届比赛的开始日期
	String info;
	int state;	// 0表示还在准备中或者进行中，1表示已经结束

	List fmList;	// 参与家族的id列表
	public List termMatchList = new ArrayList();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public String getFmids() {
		return fmids;
	}

	public void setFmids(String fmids) {
		this.fmids = fmids;
		fmList = StringUtil.toInts(fmids);
	}


	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List getFmList() {
		return fmList;
	}

	public void setFmList(List fmList) {
		this.fmList = fmList;
	}

}
