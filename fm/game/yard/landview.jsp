<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
String tip = null;
int result = 0;
int id = action.getFmId();
FamilyUserBean fmUser = action.getFmUser();
int fmId = fmUser.getFmId();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);

YardItemProtoBean item = null;
int li = action.getParameterIntS("li");
YardLandBean land = yard.getLand(li);
if(land==null||land.getFmid()!=fmId) {
	response.sendRedirect("land2.jsp");
	return;
}
if(action.hasParam("c")) {	// 收获
	result = yard.reap(li,request);
	if (result == 0){
		tip = request.getAttribute("tip").toString();
	} else if (result == -1){
		tip = "参数错误.";
	} else if (result == -2){
		tip = "不是本家族的土地.";
	} else if (result == -3){
		tip = "仍未成熟.";
	} else if (result == -4){
		tip  = "这块地已经空了.";
	}
}

List list = yard.getLandList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="培育中心"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (tip!=null){	%><%=tip%><br/><%}
	%>【第<%=li+1%>块地】<br/><%
	long now = System.currentTimeMillis();
	item = land.getItemOnLand(); // 取得这块地上正在种着的物品
	if (item != null){
		UserBean seedUser = UserInfoUtil.getUser(land.getUserId());
		int timeLeft = land.getTimeLeft(now);
		%><%=item.getNameWml()%><br/>
		数量:<%=land.getCount()%><br/>
		<%if(timeLeft<=0){%><a href="landview.jsp?c=1&amp;li=<%=li%>">&gt;&gt;收获</a><%}else{%>剩余时间:<%=DateUtil.formatTimeInterval(timeLeft)%><br/><a href="landdel.jsp?li=<%=li%>">x铲除所有种子x</a><br/><%}%><br/>
播种人:<%if(seedUser!=null){%><a href="/chat/post.jsp?toUserId=<%=seedUser.getId()%>"><%=seedUser.getNickNameWml()%></a><%}else{%>(未知)<%}%><br/>		
		<%
	} else {
		%>这是一块空地<br/><%
	}

%><a href="land2.jsp?id=<%=id%>">返回培育中心</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>