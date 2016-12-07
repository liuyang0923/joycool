<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.util.SimpleChatLog2"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.game.*"%><%@ page import="jc.family.*"%><%!
static int NUMBER_OF_PAGE = 3;%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int mid=snowAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
if(request.getParameter("b")!=null&&(request.getParameter("b").equals("1")||request.getParameter("b").equals("0"))){session.setAttribute("activityButton",request.getParameter("b"));}
String button=session.getAttribute("activityButton")!=null?session.getAttribute("activityButton").toString():"0";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="打雪仗"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(request.getParameter("qid")!=null){ int sthrow=snowAction.throwSnow(); if(sthrow==3){ %>
题目不存在!<br/>
<%}else if(sthrow==4){ %>
回答错误,未命中!<br/>
<%}else if(sthrow==5){ %>
该道具不存在!<br/>
<%}else if(sthrow==6){ %>
您未被批准参加该游戏!<br/>
<%}else if(sthrow==7){ %>
回答正确,成功击中敌方!<br/>
<%}else if(sthrow==8){ %>
没有这场赛事!<br/>
<%}else if(sthrow==9){ %>
家族未参赛!<br/>
<%}else if(sthrow==1){ %>
您正在道具使用CD中!<br/>
<%}} %>
<%
int fight=snowAction.getGameDetails();
if(fight==0){%>
赛事不存在!<br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%}else if(fight==1){
	out.clearBuffer();
	response.sendRedirect("/fm/index.jsp");return;
}else if(fight==2){%>
您此次活动没有报名,不能参加!<br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%}else if(fight==3){%>
赛事不存在!<br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%}else if(fight==4){%>
您的家族没有参加此次游戏!<br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%}else if(fight==9){%>
您的报名申请没有通过审核!<br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%}else if(fight==5){%>
您家族的家族基金不够支付您的报名费用,不能参加比赛!<br/>
<a href="/fm/myfamily.jsp">返回我的家族</a><br/>
<%}else if(fight==6||fight==7){%>
系统错误!<br/>
<%}else if(fight==8){
	SnowBean gameBean=(SnowBean)request.getAttribute("GameDetails"); 
	if(gameBean==null){
	out.clearBuffer();
	response.sendRedirect("/fm/game/fmgame.jsp");return;
	}
	if(gameBean.getRank()!=0){
		out.clearBuffer();
		response.sendRedirect("end.jsp?mid="+mid);return;
	}
%>
<a href="fight.jsp?b=<%=button %>&amp;mid=<%=mid %>">刷新</a><br/>
打雪仗&gt;累计金额<%=gameBean.getPrize() %> 雪币<br/>
<%=gameBean.getFmName1()==null?"":gameBean.getFmName1() %>家族<%=gameBean.getNumTotal() %>人 积雪<%=gameBean.getSnowAccumulation() %>/<%=jc.family.game.snow.Constants.MAX_SNOW %><br/>
<%=gameBean.getFmName2()==null?"":gameBean.getFmName2() %>家族<%=snowAction.getAttributeInt("vsFmNum") %>人 积雪<%=snowAction.getAttributeInt("vsSanowCount") %>/<%=jc.family.game.snow.Constants.MAX_SNOW %><br/>
雪币剩余:<%=snowAction.getOneSnowMoney() %><br/>
投:<%List toolsList=snowAction.getToolsList();
if(toolsList!=null){
if(toolsList.size()>0){
for(int i=0;i<(toolsList.size()<5?toolsList.size():4);i++){
SnowGameToolsBean tool=(SnowGameToolsBean)toolsList.get(i);if(tool.getTid()==1){%>
<a href="throw.jsp?tid=1&amp;mid=<%=mid %>&amp;id=<%=tool.getId() %>">小</a>
<%}else if(tool.getTid()==2){%>
<a href="throw.jsp?tid=2&amp;mid=<%=mid %>&amp;id=<%=tool.getId() %>">中</a>
<%}else if(tool.getTid()==3){%>
<a href="throw.jsp?tid=3&amp;mid=<%=mid %>&amp;id=<%=tool.getId() %>">大</a>
<%}else if(tool.getTid()==4){%>
<a href="throw.jsp?tid=4&amp;mid=<%=mid %>&amp;id=<%=tool.getId() %>">投(<%=(10-tool.getUsedTime()) %>)</a>
<%}}%><a href="snowBalls.jsp?mid=<%=mid %>">更多</a><%   }}else{%>无投掷雪球,快去制造吧!<%} %><br/>
造:<a href="make.jsp?tid=1&amp;mid=<%=mid %>">小雪球</a>&#160;<a href="make.jsp?tid=2&amp;mid=<%=mid %>">中雪球</a>&#160;<a href="make.jsp?tid=3&amp;mid=<%=mid %>">大雪球</a>&#160;<a href="make.jsp?tid=4&amp;mid=<%=mid %>">投雪机</a> <br/>
清理:<a href="clear.jsp?tid=5&amp;mid=<%=mid %>">扫把</a>&#160;<a href="clear.jsp?tid=6&amp;mid=<%=mid %>">铁锹</a>&#160;<a href="clear.jsp?tid=7&amp;mid=<%=mid %>">推雪机</a><br/>
----游戏动态------<%List snowList=snowAction.getActivit();
boolean isList=true;if(snowList==null||snowList.size()==0){isList=false;}
if(button.equals("0")&& isList){ %>  <a href="fight.jsp?b=1&amp;mid=<%=mid %>">关</a>|<a href="snowActivity.jsp?mid=<%=mid %>">更多</a><br/>
<% if(isList){
for(int i=0;i<(snowList.size()<3?snowList.size():3);i++){
SnowActivityBean ab=(SnowActivityBean)snowList.get(i);
%>
<%=ab.getFmName()==null?"":ab.getFmName() %>家族的<%=ab.getUName() %><%if(ab.getAType()==2){ %> 做了一个<%}else if(ab.getAType()==1){%>使用了<%} %><%if(ab.getTType()==4){%>投雪机<%}else if(ab.getTType()==7){ %>推雪机<%} %> <br/>
<%}}}else if(button.equals("1") && isList){%>
<a href="fight.jsp?b=0&amp;mid=<%=mid %>">开</a><br/>
<%}else{%><br/>
<%} %>
<%
String ct="f";
if(request.getParameter("ct")!=null&&(request.getParameter("ct").equals("a")||request.getParameter("ct").equals("f"))){session.setAttribute("ct",request.getParameter("ct"));}
if(session.getAttribute("ct")!=null){ct=(String)session.getAttribute("ct");}
net.joycool.wap.bean.UserBean user = snowAction.getLoginUser();
FamilyUserBean fmUser = snowAction.getFmUser();
SimpleChatLog2 sc =null;
%>
----游戏交流------<%if(ct.equals("f")){%>家族<%}else{%><a href="fight.jsp?b=<%=button %>&amp;mid=<%=mid %>&amp;ct=f">家族</a><%} %>|<%if(ct.equals("a")){%>所有<%}else{%><a href="fight.jsp?b=<%=button %>&amp;mid=<%=mid %>&amp;ct=a">所有</a><%} %>|<a href="chatmore.jsp?mid=<%=mid %>&amp;ct=<%=ct %>">更多</a><br/>
<%
	String content = snowAction.getParameterNoEnter("content");
	if(content != null&&!"".equals(content)) {		// 发言
		String c="f";
		if(request.getParameter("c")!=null){c=request.getParameter("c");}
		SimpleChatLog2 cc =null;
		if("f".equals(c)){cc = SimpleChatLog2.getChatLog(new Integer(mid+""+fmUser.getFm_id()).intValue(),"match_fm");}
		if("a".equals(c)){cc = SimpleChatLog2.getChatLog(mid,"match");}
		cc.add(user.getId(),user.getNickNameWml(),StringUtil.toWml(content));
}
if(ct.equals("f")){sc = SimpleChatLog2.getChatLog(new Integer(mid+""+fmUser.getFm_id()).intValue(),"match_fm");}else if (ct.equals("a")){sc = SimpleChatLog2.getChatLog(mid,"match");}
PagingBean paging = new PagingBean(snowAction, sc.size(),NUMBER_OF_PAGE,"p");
%>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<input name="gchat"  maxlength="100"/><br/>
<anchor title="ok">发送给家族
<go href="fight.jsp?b=<%=button %>&amp;mid=<%=mid %>&amp;c=f" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor><br/>
<anchor title="ok">发送给所有
<go href="fight.jsp?b=<%=button %>&amp;mid=<%=mid %>&amp;c=a" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor><br/>
<a href="snowMoney.jsp?mid=<%=mid %>&amp;c=2">兑换雪币</a><br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%} %>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>