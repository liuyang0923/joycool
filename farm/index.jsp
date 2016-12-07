<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%!
static HashMap logoMap = new HashMap();
static void add(String pos, int l){
	Integer logoi=Integer.valueOf(l);
	List posList = StringUtil.toInts2(pos);
	for(int i = 0;i < posList.size();i++){
	logoMap.put(posList.get(i), logoi);}
}
static{
add("701-769",2);
add("10345-10414,11450-11530,3101-3168,11609-11693",4);
add("901-945,1001-1109,10195-10211,10415-10589,5701-6000,6001-6300",3);
add("101-127,201-249,301-334,2001-2262",5);
add("1101-1300,1401-1618,1701-1909,2601-3000",7);
add("10163-10194",8);
add("3201-3500,4301-4364,4101-4300,4501-4700",9);
add("5001-5400",10);
}
%><%
response.setHeader("Cache-Control","no-cache");
FarmUserBean user = null;
if(new CustomAction(request).getLoginUser()!=null){
	FarmAction action = new FarmAction(request);	// 一旦new一个farmaction就会用到loginuser，所以要确保登陆才能调用
	action.index();
	if(action.getLoginUser()!=null)
		user = action.getUser();
}

String logo = "";
if(user!=null) {
Integer logoi = (Integer)logoMap.get(Integer.valueOf(user.getPos()));
if(logoi!=null)
	logo = logoi.toString();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/farm/img/logo<%=logo%>.gif" alt="logo"/><br/>
<%if(user==null){%>
还没有获得桃花源许可证,<a href="enter.jsp">点击获得</a><br/>
<%}else{%>
<%if(user.getRank()<3){%>欢迎来到世外桃源,勤劳的人在这里都能获得丰收:)<br/><%}%>
<% int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
if(hour>=20&&hour<=21 && user.getRank()>=6){%>
<a href="cb/tele.jsp">!查看现在可参加的擂台</a><br/>
<%}%>
<a href="map.jsp">进入桃花源</a><%--|
<a href="fields.jsp">农场</a>|
<a href="feeds.jsp">畜牧</a>|
<a href="lands.jsp">采集场</a>--%>?
<%if(user.getRank()<6){%>
<a href="help/index.jsp">新手入门</a>
<%}else{%>
<a href="help/index.jsp">进阶帮助</a>
<%}%><br/><br/>
<a href="user/bag.jsp">行囊</a>|
<a href="user/quests.jsp">任务(<%=user.getQuests().size()%>)</a>|
<a href="user/friend.jsp?jcofr=1">好友</a><br/>
<a href="user/info.jsp">个人资料</a>|
<a href="user/pros.jsp">专业</a>|
<a href="user/mypro.jsp">技能</a><br/>
<%if(user.getPro(FarmProBean.PRO_BATTLE)!=null){%>
<a href="user/equips.jsp">装备</a>|<a href="user/pro9.jsp">战斗属性点分配</a><br/>
<%}%>
<a href="bank.jsp">桃花钱庄</a>
|<a href="user/collects.jsp">收藏盒</a><br/>
<%}%><a href="/jcforum/forum.jsp?forumId=2878">+玩家交流区+</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>