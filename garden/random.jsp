<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");	
	List list = GardenAction.gardenService.getGardenUsers("select uid from garden_user order by rand() limit 10",5);	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
农场玩家<br/>
<a href="random.jsp">刷新</a><br/>
<%for(int i = 0;i<list.size();i++) {
	Integer ii = (Integer)list.get(i);
%>
<a href="/user/ViewUserInfo.do?userId=<%=ii.intValue()%>"><%=UserInfoUtil.getUser(ii.intValue()).getNickNameWml() %></a><br/>
<%} %>
<a href="random.jsp">刷新</a><br/>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>