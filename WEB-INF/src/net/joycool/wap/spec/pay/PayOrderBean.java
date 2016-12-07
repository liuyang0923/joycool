package net.joycool.wap.spec.pay;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;

/**
 * @author zhouj
 * @explain： 支付订单
 * @datetime:1007-10-24
 */
public class PayOrderBean {
	//未处理
	public static int TYPE_UNDO = 0;
	//处理中
	public static int TYPE_DOING = 1;
	//已处理
	public static int TYPE_DONE = 2;
	
	public static String TRUE = "Y";
	public static String FALSE = "F";
	
	public static String[] types = {"未处理","处理中","已处理"};
	
	int id;
	int userId;
	int type;			// 订单状态
	int money;			// 订单金额
	String code;		// 订单编号
	String bak;			// 备注，例如商品信息
	long createTime;	// 订单生成时间
	long endTime;		// 订单完成时间
	String cardId;		//卡号
	String cardPwd;		//卡密码
	String result;
	String resultstr;
	String result2;
	int channelId;
	
	
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getResultstr() {
		return resultstr;
	}
	public void setResultstr(String resultstr) {
		this.resultstr = resultstr;
	}
	public String getResult2() {
		return result2;
	}
	public void setResult2(String result2) {
		this.result2 = result2;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardPwd() {
		return cardPwd;
	}
	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}
	public String getBak() {
		return bak;
	}
	public void setBak(String bak) {
		this.bak = bak;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getType() {
		return type;
	}
	
	public String getTypeStr(){
		return types[type];
	}
	
	public void setType(int type) {
		this.type = type;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getResult2Str(){
		
		if(type == TYPE_UNDO){
			return "充值失败";
		}
		
		if(this.result2 == null || this.result2.length() == 0) {
			return "充值等待";
		}
		
		if(this.result2.equals(TRUE)) {
			return "充值成功";
		} else if(this.result2.equals(FALSE)) {
			return "充值失败";
		} else {
			return "";
		}
	}
	
}
