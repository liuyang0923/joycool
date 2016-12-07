package net.joycool.wap.bean.jcforum;

import java.util.*;

import net.joycool.wap.util.StringUtil;

public class ForumVoteBean {
	HashMap set = new HashMap();	// 参与投票的玩家id set
	int[] voteCount = new int[20];		// 各个选票的票数
	
	public ForumVoteBean() {
		
	}

	public HashMap getSet() {
		return set;
	}

	public void setSet(HashMap set) {
		this.set = set;
	}

	public int[] getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int[] voteCount) {
		this.voteCount = voteCount;
	}
	
	public synchronized void add(Object obj, int opt) {
		set.put(obj, new Integer(opt));
		voteCount[opt]++;
	}
	public synchronized void update(Object obj, int opt) {
		Integer old = (Integer)set.get(obj);
		voteCount[old.intValue()]--;
		set.put(obj, new Integer(opt));
		voteCount[opt]++;
	}

	public void setData(List list) {
		for(int i = 0;i < list.size();i++) {
			int[] ii = (int[])list.get(i);
			set.put(new Integer(ii[0]), new Integer(ii[1]));
			voteCount[ii[1]]++;
		}
	}
	
	public boolean contains(Object obj) {
		return set.containsKey(obj);
	}
}
