<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%!
	static CastleService castleService = CastleService.getInstance();
%><%
	
	CustomAction action = new CustomAction(request);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="攻击"><p>
<%if(!action.isMethodGet()){
	SoldierResBean[] gs = ResNeed.getSoldierTs();
	//int[] sendAttacks = null;//ResNeed.calcAttack(soldier1, smithy);
	//int[] receiveDefences =null;// ResNeed.calcDefense(soldier2, smithy);
	int allSendCount = 0;
	int allReceiveCount = 0;
	float sendAllAttack = 0;
	int receiveAllDefence = 0;
	float[] receiveDefences = new float[2];
	//int sendAllAttack = sendAttacks[0] + sendAttacks[1];
	//int receiveAllDefence = ResNeed.getWhistleAttact(action.getParameterInt("tower"));
	//if(sendAllAttack!=0)
		//receiveAllDefence += (receiveDefences[0] * sendAttacks[0] + receiveDefences[0] * sendAttacks[0]) / sendAllAttack;
	int sendDead = 0;	// 攻击方死亡的人口数
	int receiveDead = 0;	// 被攻击方死亡的人口数
		
	float sendLost = 0f;		// 进攻放损失的百分比
	float receiveLost = 0f;		// 防守方所有部队损失的百分比
	
	
	
	int type = action.getParameterInt("type");
	SoldierSmithyBean[] fromSmithy = new SoldierSmithyBean[9+1];
	SoldierSmithyBean[] toSmithy = new SoldierSmithyBean[9+1];
	CastleArmyBean toArmy = new CastleArmyBean();
	String sendCount = "";
	
	for(int i = 1; i < fromSmithy.length; i++) {
		int sCount = action.getParameterInt("a" + i);
		allSendCount += sCount;
		if(i != 1)
			sendCount += "," + sCount;
		else 
			sendCount += sCount;
		int rCount = action.getParameterInt("d"+i);
		allReceiveCount += rCount;
		toArmy.setCount(i, rCount);
		
		int sa = action.getParameterInt("aa"+i);
		int rd = action.getParameterInt("dd"+i);
		SoldierSmithyBean sendSmithy = new SoldierSmithyBean();
		sendSmithy.setSoldierType(i);
		sendSmithy.setAttack(sa);
		fromSmithy[i] = sendSmithy;
		SoldierSmithyBean receiveSmithy = new SoldierSmithyBean();
		receiveSmithy.setSoldierType(i);
		receiveSmithy.setDefence(rd);
		toSmithy[i] = receiveSmithy;
	}
	List list = AttackThreadBean.toInts(sendCount);
	if(type == 2) {
		sendAllAttack = ResNeed.calcScout(list, fromSmithy);
		receiveAllDefence = toArmy.calcScoutDefense(toSmithy);
		
	} else {
		float[] sendAttacks = ResNeed.calcAttack(list, fromSmithy);
		sendAllAttack = sendAttacks[0] + sendAttacks[1];
		
		
		float[] defenses = toArmy.calcDefense(toSmithy);
		receiveDefences[0] += defenses[0];
		receiveDefences[1] += defenses[1];
		int tower = action.getParameterInt("tower");
		receiveAllDefence = ResNeed.getWhistleAttact(tower);
		
		if(sendAllAttack != 0)	// 攻击力为0？
			receiveAllDefence += (receiveDefences[0] * sendAttacks[0] + receiveDefences[1] * sendAttacks[1])
					/ sendAllAttack;
		else
			receiveAllDefence += 100;	// 攻击力如果是0，防御就是无穷大
	}
	
	//城墙效果
	int wall = action.getParameterInt("wall");
	if(wall > 0)
		receiveAllDefence += receiveAllDefence * ResNeed.getBuildingT(ResNeed.WALL_BUILD, wall).getValue() / 100;	// 城墙效果
	
	
	//如果攻击者的攻击大于被攻击者的防御，则攻下城堡
	// 注意，侦察的计算方式类似于抢夺，但是防御方不死伤
	if(sendAllAttack > receiveAllDefence) {		
		float factor = receiveAllDefence / sendAllAttack;
		factor = (float)Math.pow(factor, 1.5);

		if(type == 0) {
			sendLost = factor;
			receiveLost = 1.0f;
		} else {	// 抢夺
			sendLost = factor / (1 + factor);
			receiveLost = 1.0f / (1 + factor);	// 全部损失
		}

	} else if(receiveAllDefence > sendAllAttack) {	//如果被攻击者的防御大于攻击者的攻击，则攻不下城堡
		float factor = sendAllAttack / receiveAllDefence;
		factor = (float)Math.pow(factor, 1.5);
		
		if(type == 0) {
			sendLost = 1.0f;
			receiveLost = factor;		
		} else {	// 抢夺
			sendLost = 1.0f / (1 + factor);
			receiveLost = factor / (1 + factor);
		}
		
	} else 	if(receiveAllDefence == sendAllAttack) {	//如果被攻击人的防御和攻击者的攻击想同，则士兵全部死掉
		//减掉防守方失去的兵力，启动线程回城
		if(type == 0) {	
			sendLost = 1f;
			receiveLost = 1f;
		} else {	// 抢夺
			sendLost = 0.5f;
			receiveLost = 0.5f;
		}
	}
	
	int leftCount = 0;	// 剩余军队
	if(sendLost != 0) {
		StringBuilder sbc = new StringBuilder(32);
		
		for(int i = 0;i < list.size();i++) {
			int count = ((Integer)list.get(i)).intValue();
			if(count > 0) {
				if(sendLost == 1.0f) {
					sendDead += gs[i + 1].getPeople() * count;
					count = 0;	
				} else {
					int dead = (int) Math.round(count * sendLost);
					sendDead += gs[i + 1].getPeople() * dead;
					count -= dead;
					leftCount += count;
				}
			}
			if(i != 0)
				sbc.append(',');
			sbc.append(count);
		}
		sendCount = sbc.toString();
		list = AttackThreadBean.toInts(sendCount);
	} else {
		leftCount = 1;
	}
	
	int[] receiveCounts = new int[ResNeed.soldierTypeCount + 1];
	if(type == 2 || type == 3)	{	// 对于侦察，防守方兵力无损耗
		receiveLost = 0f;
		receiveCounts = toArmy.getCount();
	}
	else if(receiveLost > 0f) {
		receiveCounts = toArmy.getCount();
			
		for (int i = 1; i < receiveCounts.length; i++) {
			int count = receiveCounts[i];
			if(count > 0) {
				int dead = (int) Math.round(count * receiveLost);
				receiveDead += gs[i].getPeople() * dead;
				receiveCounts[i] -= dead;
			}
		}		
	}
	
%>
攻击模式:<%=type == 0? "攻击":( type == 1?"抢夺":"侦查") %><br/>
攻击方总攻击力:<%=sendAllAttack %>|防守方总防御力:<%=receiveAllDefence %><br/>
===攻击方===<br/>
总兵力:<%=allSendCount %><br/>
总损失百分比:<%=(int)(sendLost*100) %>%<br/>
剩余总人数:<%=leftCount %><br/>
[剩余兵力]<br/><%for(int i = 0; i < list.size(); i++) {
	int ssC = ((Integer)list.get(i)).intValue();
%><%=gs[i+1].getSoldierName() %><%=ssC %><%} %><br/>
===防守方===<br/>
总兵力:<%=allReceiveCount %><br/>
总损失百分比:<%=(int)(receiveLost*100) %>%<br/>
[剩余兵力]<br/><%for(int i = 1; i <= 9; i++) {
%><%=gs[i].getSoldierName() %><%=receiveCounts[i]%><%} %><br/>
------------------<br/>
<%}%>
模式:<select name="type">
<option value="0">攻击</option>
<option value="1">抢夺</option>
<option value="2">侦查</option>
</select><br/>
===攻击方===<br/>
<%
SoldierResBean[] ress = ResNeed.getSoldierTs();
for(int i = 1; i < ress.length; i++) {
%>
<%=ress[i].getSoldierName() %>:<input name="a<%=i%>" format="*N"/>等级<input name="aa<%=i %>" format="*N"/><br/>
<%} %>
===防御方===<br/>
<%
for(int i = 1; i < ress.length; i++) {
%>
<%=ress[i].getSoldierName() %>:<input name="d<%=i%>" format="*N"/>等级<input name="dd<%=i %>" format="*N"/><br/>
<%} %>
*哨塔等级:<input name="tower" format="*N" value="0"/><br/>
*城墙等级:<input name="wall" format="*N" value="0"/><br/>
<anchor title="攻击">开始计算
<go href="attack.jsp" method="post"/>
<postfield name="a1" value="$a1"/>
<postfield name="a2" value="$a2"/>
<postfield name="a3" value="$a3"/>
<postfield name="a4" value="$a4"/>
<postfield name="a5" value="$a5"/>
<postfield name="a6" value="$a6"/>
<postfield name="a7" value="$a7"/>
<postfield name="a8" value="$a8"/>
<postfield name="a9" value="$a9"/>
<postfield name="d1" value="$d1"/>
<postfield name="d2" value="$d2"/>
<postfield name="d3" value="$d3"/>
<postfield name="d4" value="$d4"/>
<postfield name="d5" value="$d5"/>
<postfield name="d6" value="$d6"/>
<postfield name="d7" value="$d7"/>
<postfield name="d8" value="$d8"/>
<postfield name="d9" value="$d9"/>
<postfield name="aa1" value="$aa1"/>
<postfield name="aa2" value="$aa2"/>
<postfield name="aa3" value="$aa3"/>
<postfield name="aa4" value="$aa4"/>
<postfield name="aa5" value="$aa5"/>
<postfield name="aa6" value="$aa6"/>
<postfield name="aa7" value="$aa7"/>
<postfield name="aa8" value="$aa8"/>
<postfield name="aa9" value="$aa9"/>
<postfield name="dd1" value="$dd1"/>
<postfield name="dd2" value="$dd2"/>
<postfield name="dd3" value="$dd3"/>
<postfield name="dd4" value="$dd4"/>
<postfield name="dd5" value="$dd5"/>
<postfield name="dd6" value="$dd6"/>
<postfield name="dd7" value="$dd7"/>
<postfield name="dd8" value="$dd8"/>
<postfield name="dd9" value="$dd9"/>
<postfield name="tower" value="$tower"/>
<postfield name="wall" value="$wall"/>
<postfield name="type" value="$type"/>
</anchor><br/>
<br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>