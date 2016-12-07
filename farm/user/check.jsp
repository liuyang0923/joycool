<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
FarmNpcWorld nWorld = FarmWorld.getNpcWorld();
FarmUserBean user = FarmWorld.one.getFarmUserCache(action.getLoginUser().getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%if(user==null){%>
没有找到对应的人物，如果是桃花源玩家请到桃花源首页转一圈再回来。<br/>
<%}else{
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

FarmUserProBean[] pros = user.getPro();

%>
姓名:<%=user.getNameWml()%><br/>


===战斗属性点===<br/>
战斗等级:<%=user.getProRank(FarmProBean.PRO_BATTLE)%><br/>
已分配属性点:<%=user.getAttr1()%>+<%=user.getAttr2()%>+<%=user.getAttr3()%>+<%=user.getAttr4()%>+<%=user.getAttr5()%>=<%=user.getAttr1()+user.getAttr2()+user.getAttr3()+user.getAttr4()+user.getAttr5()%><br/>
未分配属性点:<%=user.getBattlePoint()%><br/>
实际的总点数为<%=user.getBattlePoint()+user.getAttr1()+user.getAttr2()+user.getAttr3()+user.getAttr4()+user.getAttr5()%><br/>
应有的总点数为<%=FarmWorld.rankBattlePoint[user.getProRank(9)]+25%><br/>
===升级点===<br/>
人物等级:<%=user.getRank()%><br/>
<% int sumPoint = 0;
for(int i=0;i<pros.length;i++){
FarmUserProBean userPro = pros[i];
if(userPro==null) continue;
FarmProBean pro = world.getPro(i);
if(pro==null) continue;
sumPoint += pro.getPoint() * userPro.getRank();
%>
--<%=pro.getName()%><%=userPro.getRank()%>级x<%=pro.getPoint()%>=<%=pro.getPoint() * userPro.getRank()%><br/>
<%}%>

总计已分配专业点:<%=sumPoint%><br/>
未分配专业点:<%=user.getProPoint()%><br/>
实际的总点数为<%=user.getProPoint()+sumPoint%><br/>
应有的总点数为<%=user.getRank() * 10 - 10%><br/>
===人物经验===<br/>
当前经验:<%=user.getExp()%><br/>
总计完成任务<%=endList.size()%>个，奖励经验值<%=exp%>，金钱<%=FarmWorld.formatMoney(money)%><br/>
注意:重复完成的奖励不计算在内<br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>