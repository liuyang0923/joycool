package net.joycool.wap.spec.buyfriends;

import java.util.Date;

import net.joycool.wap.util.UserInfoUtil;

public class BeanSlave {

	public static final int SLAVE = 1;	//奴隶
	public static final int YAYUAN = 2;	//丫鬟
	public static final int PUREN = 3;	//仆人
	
	private int id;
	private int masterUid;
	private int slaveUid;
	private String masterNickName;		//奴隶主
	private String slaveNickName;		//奴隶
	private String slaveAlias;			//奴隶别名
	//private String info;				//奴隶信息
	private int oldMasterUid;
	private Date buyTime;				
	private int slaveType;				//奴隶类型，是邀请上来的奴隶，还是花钱购买的奴隶
	private int buyPrice;				//购买价格
	int rank;
	long frozenTime;						//冻结时间
	
	private int money;
	private int price;
	
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMasterUid() {
		return masterUid;
	}
	public void setMasterUid(int masterUid) {
		this.masterUid = masterUid;
	}
	public int getSlaveUid() {
		return slaveUid;
	}
	public void setSlaveUid(int slaveUid) {
		this.slaveUid = slaveUid;
	}
	public String getMasterNickName() {
		return masterNickName;
	}
	public void setMasterNickName(String masterNickName) {
		this.masterNickName = masterNickName;
	}
	public String getSlaveNickName() {
		return slaveNickName;
	}
	public void setSlaveNickName(String slaveNickName) {
		this.slaveNickName = slaveNickName;
	}
	public String getSlaveAlias() {
		return slaveAlias;
	}
	public void setSlaveAlias(String slaveAlias) {
		this.slaveAlias = slaveAlias;
	}
	public String getInfo() {
		
		StringBuilder sb = new StringBuilder();
		//这个奴隶是你花600元把他从自由人买为奴隶 花xxx元把他从xxx手上买回来		
		if(this.oldMasterUid == 0) {
			sb.append("这个奴隶是你花");
			sb.append(buyPrice);
			sb.append("元把他从自由人买为奴隶");
		} else {
			sb.append("花");
			sb.append(this.buyPrice);
			sb.append("元把他从");
			sb.append(ActionBuyFriend.getUserLink(this.oldMasterUid,UserInfoUtil.getUser(this.oldMasterUid).getNickName()));
			sb.append("手上买回来");
		}
		
		return sb.toString();
	}
	public Date getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	
	public int getSlaveType(){
		return this.slaveType;
	}
	
	public String getSlaveTypeString() {
		
		switch(slaveType) {
		case 1:
			return "奴隶";
		case 2:
			return "丫鬟";
		case 3:
			return "仆人";
		}
		
		return "仆人";
	}
	public void setSlaveType(int slaveType) {
		this.slaveType = slaveType;
	}
	public int getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public long getFrozenTime() {
		return frozenTime;
	}
	public void setFrozenTime(long frozenTime) {
		this.frozenTime = frozenTime;
	}
	public int getOldMasterUid() {
		return oldMasterUid;
	}
	public void setOldMasterUid(int oldMasterUid) {
		this.oldMasterUid = oldMasterUid;
	}
}
