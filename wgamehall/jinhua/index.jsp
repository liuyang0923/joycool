<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
String type = (String)request.getParameter("type");
action.dealIndex(request, response);
//登录用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//坐庄列表
int totalHallPageCount = ((Integer) request.getAttribute("totalHallPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
Vector hallList = (Vector) request.getAttribute("hallList");
String prefixUrl = (String) request.getAttribute("prefixUrl");

//玩家列表
int totalOnlinePageCount = ((Integer) request.getAttribute("totalOnlinePageCount")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
Vector userList = (Vector) request.getAttribute("userList");
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
if(type == null)
   type = "1";
//当前游戏
WGameHallBean currentJinHua = (WGameHallBean)request.getAttribute("currentJinHua");

//刷新
String refreshUrl = PageUtil.getCurrentPageURL(request);
refreshUrl = refreshUrl.replace("&", "&amp;");

int i, count;
WGameHallBean hall = null;
UserBean user = null;
String fenye = null;
int count2 = SqlUtil.getIntResult("select count(id) from wgame_hall where mark != "+ HallBean.GS_END + " and game_id = " + HallBean.JINHUA, Constants.DBShortName);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="砸金花" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="center">砸金花</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/wgamehall/jinhua/ksGame.jsp?type=<%=type%>">快速开始游戏</a>(<%=count2*2%>人)<br/>
系统自动快速找人与你开始游戏<br/>
-------------<br/>
+玩家列表+<br/>
<%
count = userList.size();
for(i = 0; i < count; i ++){
	user = (UserBean) userList.get(i);
%>
<%=(pageIndex1 * action.ONLINE_NUMBER_PER_PAGE + i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%>(<%=user.getUs().getGamePoint()%>乐币)<a href="/wgamehall/jinhua/invite.jsp?userId=<%=user.getId()%>&amp;type=<%=type%>">邀请</a><br/>
<%
}

//分页
fenye = PageUtil.shuzifenye1(totalOnlinePageCount, pageIndex1, prefixUrl1, true, " ", response);
if(fenye != null && !fenye.equals("")){
%>
<%=fenye%><br/>
<%
}
%>
+当前战局+<br/>
<%
if(currentJinHua != null){
	String enemyNickname = null;
	UserStatusBean us=null;
	if(loginUser.getId() == currentJinHua.getLeftUserId()){
		us=UserInfoUtil.getUserStatus(currentJinHua.getRightUserId());
		if(us!=null)
			enemyNickname =us.getHatShow()+StringUtil.toWml(currentJinHua.getRightNickname());
		else
			enemyNickname=StringUtil.toWml(currentJinHua.getRightNickname());
	} else {
		us=UserInfoUtil.getUserStatus(currentJinHua.getLeftUserId());
		if(us!=null)
			enemyNickname =us.getHatShow()+StringUtil.toWml(currentJinHua.getLeftNickname());
		else
			enemyNickname=StringUtil.toWml(currentJinHua.getLeftNickname());
	}
%>
<a href="/wgamehall/jinhua/playing.jsp?gameId=<%=currentJinHua.getId()%>">你正和<%=StringUtil.toWml(enemyNickname)%>激战</a><br/>
<%
}
count = hallList.size();
for(i = 0; i < count; i ++){
	hall = (WGameHallBean) hallList.get(i);
	UserStatusBean leftUs=null;
	UserStatusBean rightUs=null;
	leftUs=UserInfoUtil.getUserStatus(hall.getLeftUserId());
	rightUs=UserInfoUtil.getUserStatus(hall.getRightUserId());
%>
<%=(pageIndex * action.NUMBER_PER_PAGE + i + 1)%>.<%if(leftUs!=null){%><%=leftUs.getHatShow()%><%}%>
<%=StringUtil.toWml(hall.getLeftNickname())%>vs<%if(rightUs!=null){%><%=rightUs.getHatShow()%><%}%><%=StringUtil.toWml(hall.getRightNickname())%>,<%if(hall.getMark() == WGameHallBean.GS_WAITING){%>邀请中<%}else if(hall.getMark() == WGameHallBean.GS_PLAYING){%>进行中<%}else{%>已结束<%}%><br/>
<%
}

//分页
fenye = PageUtil.shuzifenye(totalHallPageCount, pageIndex, prefixUrl, true, " ", response);
if(fenye != null && !fenye.equals("")){
%>
<%=fenye%><br/>
<%
}
%>
+功能选项+<br/>
<a href="/wgamehall/jinhua/help.jsp">游戏帮助</a>|<a href="/wgamehall/jinhua/history.jsp">今日战绩</a><br/>
<a href="/wgamehall/jinhua/start.jsp">返回砸金花首页</a>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>