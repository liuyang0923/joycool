<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.friend.*"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
int toId=StringUtil.toInt(request.getParameter("toId"));
IFriendService friendService = ServiceFactory.createFriendService();
Vector drinkList=friendService.getFriendDrinkList(null);
%>
<card title="结义">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(drinkList!=null){%>
来来来，同饮三杯，义结金兰！<br/>
<%
for(int j=0;j<drinkList.size();j++){
	FriendDrinkBean drink=(FriendDrinkBean)drinkList.get(j);%>
<a href="/friend/jyProposalResult.jsp?toId=<%=toId%>&amp;drinkId=<%=drink.getId()%>">
<%=drink.getName()%>(<%=drink.getPrice()%>)
</a><br/>
<%
}
}%>
<a href="/user/ViewUserInfo.do?userId=<%=toId%>">再考虑考虑</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>