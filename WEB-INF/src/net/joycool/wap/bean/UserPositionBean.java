package net.joycool.wap.bean;

import java.util.*;

// 记录用户访问的模块，包括当前、历史等等
public class UserPositionBean {
	
	public static int MAX_HISTORY = 10;
	
	int userId;
	LinkedList history = new LinkedList();
	
	public void addHistory(ModuleBean module) {
		synchronized(history) {
			if(history.remove(module))
				history.addFirst(module);
			else {
				history.addFirst(module);
				if(history.size() > MAX_HISTORY)
					history.removeLast();
			}
				
		}
	}

	public LinkedList getHistory() {
		return history;
	}
	
	public void setHistory(LinkedList history) {
		this.history = history;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
