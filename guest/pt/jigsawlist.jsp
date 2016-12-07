<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@page import="net.joycool.wap.util.RandomUtil"%><%@ page import="java.util.*,jc.guest.pt.*,jc.guest.*" %>
<%response.setHeader("Cache-Control","no-cache"); %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<wml>
<card title="闯关积分排行榜">
<p align="left">

<%response.setHeader("Cache-Control","no-cache");
JigsawAction action=new JigsawAction(request,response);
GuestHallAction gaction = new GuestHallAction(request,response);

JigsawUserBean meBean=action.getUserDetails();
int p=action.getParameterInt("p");
List list = action.getScoreList();
%>
我的闯关积分:<%=meBean==null?0:meBean.getMaxScore()%><br/>
我的排名:<%if(request.getAttribute("MyPTScore")!=null){%><%=request.getAttribute("MyPTScore")%><%}else{%>暂无排名<%}%><br/>
<%
if(list!=null){
	if(list.size()>0){
		for(int i=0;i<list.size();i++){
			JigsawUserBean bean =(JigsawUserBean) list.get(i);
			GuestUserInfo user = GuestHallAction.getGuestUser(bean.getUid()) ;
			%>
			<%=p*10+i+1%>.<a href="/guest/info.jsp?uid=<%=bean.getUid()%>"><%=user==null?"无名英雄":user.getUserNameWml() %></a>&#160;<%=bean.getMaxScore() %><br/>
<%		}
	}
}%>
<%if(request.getAttribute("ScoreList")!=null&&!"".equals(request.getAttribute("ScoreList"))){%>
<%=request.getAttribute("ScoreList") %>
<%}%> 
<a href="instructions.jsp">返回智慧拼图首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>