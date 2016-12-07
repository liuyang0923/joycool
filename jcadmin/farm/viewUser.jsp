<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.dummy.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
FarmNpcWorld nWorld = FarmWorld.getNpcWorld();
String idStr = request.getParameter("id");
int userId = action.getParameterInt("id");
FarmUserBean user=null;
BattleStatus bs=null;
FarmUserEquipBean[] equips=null;
List tipList = new ArrayList();
if(userId==0&&action.hasParam("id")){
	userId=SqlUtil.getIntResult("select user_id from farm_user where name='"+StringUtil.toSql(request.getParameter("id"))+"'", 4);
	if(userId>0){
	response.sendRedirect("viewUser.jsp?id="+userId);
	return;
	}
}
if(userId>0){
user = FarmWorld.one.getFarmUserCache(userId);
if(user!=null){

if(action.hasParam("rpro")){
FarmWorld.service.getFarmUserPro(user);
}
if(action.hasParam("rtarget")){
user.getTargetList().clear();
}
if(action.hasParam("rename") && !SqlUtil.exist("select user_id from farm_user where name='" + StringUtil.toSql(request.getParameter("rename")) + "' limit 1", 4)){		// 强制改名，但是不能有重名
FarmWorld.updateUserInfo(user, request.getParameter("rename"));
}
if(action.hasParam("abort")){		// 放弃任务
int qid = action.getParameterInt("abort");
FarmUserQuestBean userQuest = nWorld.getUserQuest(qid);
if(userQuest!=null&&userQuest.getStatus() == 0){
FarmQuestBean quest = nWorld.getQuest(userQuest.getQuestId());
nWorld.removeUserQuest(user, userQuest);
user.removeQuestFinishStatus(quest);
}}
if(action.hasParam("resetAttr")){
List ac = new ArrayList();
int[] acs = {21};
ac.add(acs);
FarmWorld.doAction(ac, user);
}
if(action.hasParam("propoint")){
int addProPoint = action.getParameterInt("propoint");
if(addProPoint>0&&addProPoint<=1000||addProPoint<0&&addProPoint>=-1000)
	FarmWorld.addProPoint(user,addProPoint);
}

if(action.hasParam("battlepoint")){
	int battlepoint = action.getParameterInt("battlepoint");
	user.setBattlePoint(user.getBattlePoint() + battlepoint);
	SqlUtil.executeUpdate("update farm_user set battle_point=" + user.getBattlePoint() + " where user_id=" + user.getUserId(), 4);
}

if(action.hasParam("class1")){
	int class1 = action.getParameterInt("class1");
	user.setClass1(class1);
	SqlUtil.executeUpdate("update farm_user set class1=" + user.getClass1() + " where user_id=" + user.getUserId(), 4);
}

if(action.hasParam("max")){
int max = action.getParameterInt("max");
if(max >= -2 && max <= 2 && max != 0){
int addPro = action.getParameterInt("pro");
if(addPro == 0){
	user.setMaxRank(user.getMaxRank() + max);
	SqlUtil.executeUpdate("update farm_user set max_rank=" + user.getMaxRank() + " where user_id=" + user.getUserId(), 4);
}else{
	FarmUserProBean pro = user.getPro(addPro);
	if(pro!=null){
		pro.setMaxRank(pro.getMaxRank() + max);
		SqlUtil.executeUpdate("update farm_user_pro set max_rank=" + pro.getMaxRank() + " where id=" + pro.getId(), 4);
	}
}
}
}

if(action.hasParam("resetstat")){
	user.resetCurStat();
}

if(action.hasParam("exp")){
int addExp = action.getParameterInt("exp");
if(addExp>0&&addExp<=1000){
int addPro = action.getParameterInt("pro");
if(addPro == 0)
FarmWorld.addFUExp(user,addExp);
else{
	FarmUserProBean pro = user.getPro(addPro);
	if(pro!=null)
		FarmWorld.addFUPExp(pro,addExp);
}
}
}
if(action.hasParam("rank")){
int addExp = action.getParameterInt("rank");
if(addExp>-5&&addExp<=5){
int addPro = action.getParameterInt("pro");
if(addPro > 0){
	FarmUserProBean pro = user.getPro(addPro);
	if(pro!=null)
		FarmWorld.addFUPRank(pro,addExp);
}
}
}
if(action.hasParam("money")){
int addMoney = action.getParameterInt("money");
if(addMoney>-1000000&&addMoney<=100000)
FarmWorld.addMoney(user, addMoney);
}
bs = user.getCurStat();
equips = user.getEquip();}
}
%>
			<html>
	<head>
	</head>
<script src='js/tooltip.js' type='text/javascript'></script>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>

<%if(user != null){
TongBean tong = null;
if(user.getTongUser()!=null)
	tong = FarmTongWorld.getTong(user.getTongUser().getTongId());
%>

姓名:<%=user.getNameWml()%><br/>
等级:<%=user.getRank()%>/<%=user.getMaxRank()%><br/>
职业:<%=user.getClass1Name()%><form action="viewUser.jsp?id=<%=user.getUserId()%>" method="post"><select name="class1">
<% String[] class1Name = FarmUserBean.class1Name;
for(int i=0;i<class1Name.length;i++){%><option value="<%=i%>"<%if(i==user.getClass1()){%> selected<%}%>><%=class1Name[i]%></option><%}%>
</select><input type=submit value="修改" onclick="return confirm('确定修改职业?')"></form>|门派:<%if(tong==null){%>(无)<%}else{%><a href="viewTong.jsp?id=<%=tong.getId()%>"><%=tong.getNameWml()%></a><%}%><br/>
经验:<%=user.getExp()%>/<%=user.getUpgradeExp()%><br/>
财产:<%=FarmWorld.formatMoney(user.getMoney())%><br/>
<table>
<tr valign=top><td align=left width=160>
==人物基本属性(<a href="viewUser.jsp?resetstat=1&amp;id=<%=user.getUserId()%>">计算</a>)==<br/>
战斗等级:<%=user.getProRank(FarmProBean.PRO_BATTLE)%><br/>

<%if(user.getPro(FarmProBean.PRO_BATTLE)!=null){%>
血:<%=user.hp%>/<%=bs.hp%><br/>
气力:<%=user.mp%>/<%=bs.mp%><br/>
体力:<%=user.sp%>/<%=bs.sp%><br/>
攻击:<%=bs.attack1%>-<%=bs.attack1+bs.attack1Float%><br/>
攻击速度:<%=FarmWorld.attackIntervalString(bs.attackSpeed,bs.attackSpeedAdd)%>s<br/>
防御:<%=bs.defense1%><br/>
致命:<%=bs.ds%>|
爆发:<%=(int)(bs.cb*100)%>%<br/>
<%=FarmUserBean.attrName[0]%>:<%=bs.attr1%><br/>
<%=FarmUserBean.attrName[1]%>:<%=bs.attr2%><br/>
<%=FarmUserBean.attrName[2]%>:<%=bs.attr3%><br/>
<%=FarmUserBean.attrName[3]%>:<%=bs.attr4%><br/>
<%=FarmUserBean.attrName[4]%>:<%=bs.attr5%><br/>
<%}%>
</td><td align=left width=200>
==当前装备==<br/>
<%
for(int i=0;i<FarmUserEquipBean.partCount;i++){
FarmUserEquipBean equip = equips[i];%>
<%=FarmUserEquipBean.getPartName(i)%>:
<% if(equip != null && equip.getUserbagId()!=0){ 
UserBagBean userBag = UserBagCacheUtil.getUserBagCache(equip.getUserbagId());
DummyProductBean item = UserBagCacheUtil.getItem(userBag.getProductId());
tipList.add(item);
%>
<a onmouseover='showdscp(event,"item<%=item.getId()%>")' onmousemove='movedscp(event,"item<%=item.getId()%>")' onmouseout='hinddscp(event,"item<%=item.getId()%>")'
href="editEquip.jsp?id=<%=item.getId()%>"><%=item.getName()%></a>[<%=item.getGradeName()%>]<br/>
<%}else{%>
(无)<br/>
<%}%>
<%}%>
</td></tr>
</table>
<%
MapNodeBean node = FarmWorld.one.getMapNode(user.getPos());
if(node==null) node=new MapNodeBean();
MapNodeBean node2 = FarmWorld.one.getMapNode(user.getBindPos());
if(node2==null) node2=new MapNodeBean();
%>
当前地区:<a href="editMapNode.jsp?id=<%=node.getId()%>"><%=node.getName()%></a>&nbsp;

绑定地区:<a href="editMapNode.jsp?id=<%=node2.getId()%>"><%=node2.getName()%></a><br/>

<form action="moveUser.jsp?id=<%=user.getUserId()%>" method=post>
<input type=text name="pos" value="<%=node.getId()%>">&nbsp;<input type=text name="bindPos" value="<%=node2.getId()%>">&nbsp;
<input type=submit value="移动玩家">
</form>
<form action="viewUser.jsp?id=<%=user.getUserId()%>" method=post>
<input type=text name="money" value="1000">
<input type=submit value="添加铜板">
</form>
<form action="viewUser.jsp?id=<%=user.getUserId()%>" method=post>
<input type=text name="exp" value="10">
<input type=submit value="添加经验">
<input type=submit name="pro" value="14">
<input type=submit name="pro" value="10">
<input type=submit name="pro" value="9">
<input type=submit name="pro" value="7">
<input type=submit name="pro" value="6">
<input type=submit name="pro" value="5">
<input type=submit name="pro" value="4">
<input type=submit name="pro" value="3">
</form>
<form action="viewUser.jsp?id=<%=user.getUserId()%>" method=post>
<input type=text name="rank" value="1">
<input type=submit value="添加等级">
<input type=submit name="pro" value="14">
<input type=submit name="pro" value="10">
<input type=submit name="pro" value="9">
<input type=submit name="pro" value="7">
<input type=submit name="pro" value="6">
<input type=submit name="pro" value="5">
<input type=submit name="pro" value="4">
<input type=submit name="pro" value="3">
</form>
<form action="viewUser.jsp?id=<%=user.getUserId()%>" method=post>
<input type=text name="propoint" value="10">
<input type=submit value="添加专业点数">
</form>
<form action="viewUser.jsp?id=<%=user.getUserId()%>" method=post>
<input type=text name="max" value="1">
<input type=submit value="添加等级上限">
<input type=submit name="pro" value="14">
<input type=submit name="pro" value="10">
<input type=submit name="pro" value="9">
<input type=submit name="pro" value="7">
<input type=submit name="pro" value="6">
<input type=submit name="pro" value="5">
<input type=submit name="pro" value="4">
<input type=submit name="pro" value="3">
</form>
任务猎杀内容
<%
List questCreatureFinish = user.getQuestCreatureFinish();
if(questCreatureFinish.size()==0){
%>（无）<br/><%}else{%>
<form action="setUserCreature.jsp?id=<%=user.getUserId()%>" method=post>
<% for(int j = 0;j<questCreatureFinish.size();j++){
int[] f = (int[])questCreatureFinish.get(j);
CreatureTBean creature = FarmNpcWorld.one.getCreatureT(f[1]);
%>
<%=creature.getName()%>
<input type=text name="j<%=j%>" value="<%=f[2]%>"><br/>
<%}%>
<input type=submit value="确认设置">
</form>
<%}%>
<form action="viewUser.jsp?id=<%=user.getUserId()%>" method=post>
<input type=text name="rename" value="<%=user.getName()%>">
<input type=submit value="改名">
</form>
===当前任务===<br/>
<%
List quests = user.getQuests();
for(int i=0;i<quests.size();i++){
FarmUserQuestBean userQuest = (FarmUserQuestBean)quests.get(i);
FarmQuestBean quest = FarmNpcWorld.one.getQuest(userQuest.getQuestId());
%>
<a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>(<%=DateUtil.formatDate2(userQuest.getStartTime())%>)
-<a href="viewUser.jsp?abort=<%=userQuest.getId()%>&amp;id=<%=user.getUserId()%>" onclick="return confirm('确认放弃[任务]<%=quest.getName()%>?')">放弃</a><br/>
<%}%>
<% 
int exp = 0, money = 0;
List endList = new ArrayList(user.getEndQuests().keySet());
for(int i=0;i<endList.size();i++){
Integer iid = (Integer)endList.get(i);
FarmQuestBean quest = FarmNpcWorld.one.getQuest(iid.intValue());
if(quest==null)continue;
List prize = quest.getPrizeList();
if(prize.size()==0) continue;
for(int j = 0;j<prize.size();j++){
int[] f = (int[])prize.get(j);
if(f[0]==1) exp+=f[1]; else if(f[0]==0) money+=f[1];
}}
%>
<a onclick="document.all.d1.style.display='block';return true;" target="sp" href="viewUserQuest.jsp?id=<%=userId%>">完成任务<%=endList.size()%>个</a>，奖励经验值<%=exp%>，金钱<%=FarmWorld.formatMoney(money)%><br/>
<div id=d1 style="display:none">
	<iframe name=sp width=90% height=450 align=center frameborder=0>
	</iframe>
</div>
<%
FarmUserProBean[] pros = user.getPro();
%>
剩余点数<%=user.getProPoint()%><br/>
<% int sumPoint = user.getProPoint();
for(int i=0;i<pros.length;i++){
FarmUserProBean userPro = pros[i];
if(userPro==null) continue;
FarmProBean pro = world.getPro(i);
if(pro==null) continue;
sumPoint += pro.getPoint() * userPro.getRank();
%>
+
<a href="editPro.jsp?id=<%=i%>"><%=pro.getName()%></a>
<%=userPro.getRank()%>/<%=userPro.getMaxRank()%>级[
<%=userPro.getExp()%>/<%=userPro.getUpgradeExp()%>
]<br/>
<%}
int sumPoint2=user.getRank()*10-10;

int battlePoint1=user.getBattlePoint()+user.getAttr1()+user.getAttr2()+user.getAttr3()+user.getAttr4()+user.getAttr5();
int battlePoint2=FarmWorld.rankBattlePoint[user.getProRank(9)]+25;
%>
总共点数<%if(sumPoint2!=sumPoint){%><font color=red><b><%=sumPoint%></b></font>-<a href="viewUser.jsp?id=<%=user.getUserId()%>&propoint=<%=sumPoint2-sumPoint%>">立即修复</a><%}else{%><%=sumPoint%><%}%>正确点数:<%=sumPoint2%><br/>
<a href="viewUser.jsp?rpro=1&amp;id=<%=user.getUserId()%>">重新载入该玩家的专业数据</a><br/>
战斗属性点<%if(battlePoint1!=battlePoint2){%><font color=red><b><%=battlePoint1%></b></font>-<a href="viewUser.jsp?id=<%=user.getUserId()%>&battlepoint=<%=battlePoint2-battlePoint1%>">立即修复</a><%}else{%><%=battlePoint1%><%}%>(<%=user.getBattlePoint()%>)/
<%=battlePoint2%><br/>
<form action="viewUser.jsp?id=<%=user.getUserId()%>" method=post>
<input type=text name="battlepoint" value="1">
<input type=submit value="添加属性点">
</form>
<a href="viewUser.jsp?resetAttr=1&amp;id=<%=user.getUserId()%>" onclick="return confirm('确认重置所有战斗点数?')">重置战斗点数分配</a><br/>
<br/>
现有目标<%=user.getTargetList().size()%>个
<a href="viewUser.jsp?rtarget=1&amp;id=<%=user.getUserId()%>">重置目标</a><br/>
<br/>
<%}else{%>
该人物不存在<br/>
<%}%>
<%@include file="bottom.jsp"%>
<%@include file="tipList.jsp"%>
	</body>
</html>