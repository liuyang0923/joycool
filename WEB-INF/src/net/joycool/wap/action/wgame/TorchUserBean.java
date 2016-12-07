package net.joycool.wap.action.wgame;

import java.util.*;


public class TorchUserBean {
	int userId;
	int torchCount;		// 用户经过几个火炬
	long torches;		// 拿过的火炬，二进制位表示

	public int getTorchCount() {
		return torchCount;
	}
	public void setTorchCount(int torchCount) {
		this.torchCount = torchCount;
	}
	public long getTorches() {
		return torches;
	}
	public void setTorches(long torches) {
		this.torches = torches;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public boolean hadTorch(int torchId) {
		if(torchId < 0 || torchId > 50)
			return false;
		return (torches & (1 << torchId)) != 0;
	}
	
	public static boolean hadTorch(long torches, int torchId) {
		if(torchId < 0 || torchId > 50)
			return false;
		return (torches & (1 << torchId)) != 0;
	}
	
	public void addTorch(int torchId) {
		torches |= 1 << torchId;
		torchCount++;
	}
}
