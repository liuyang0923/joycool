<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeImageBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.present(request);
HomeImageBean image=(HomeImageBean)request.getAttribute("image");
String result=(String)request.getAttribute("result");
String url=("/home/myStore.jsp");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="赠送家具">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/home/home.jsp">家园首页</a><br/>
<a href="/home/myStore.jsp">我的仓库</a><br/>
<a href="/chat/hall.jsp">聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("persent")){%>
<card title="赠送家具" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/home/myStore.jsp">我的仓库</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
out.clearBuffer();
response.sendRedirect("myStore.jsp");
return;
}else{
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List userFriendList=(List)request.getAttribute("userFriendList");
String fromBuyPage = (String)request.getAttribute("fromBuyPage");
%>
<card title="赠送家具">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您要把<%=image.getName()%><br/>
赠送给您的好友:<br/>
<%
UserBean user = null;
for(int i=0;i<userFriendList.size();i++){ 
     user=(UserBean)userFriendList.get(i);
     if(user.getId()==loginUser.getId())
     continue;
     if(fromBuyPage.equals("no")){
     %>
<%=i+1 %>.<a href="/home/presentResult.jsp?friendId=<%=user.getId()%>&amp;imageId=<%=image.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%}else{%>
<%=i+1 %>.<a href="/home/presentResult.jsp?fromBuyPage=1&amp;friendId=<%=user.getId()%>&amp;imageId=<%=image.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%}}String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/home/myStore.jsp">返回仓库</a><br/>
<a href="/home/home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%} %>
</wml>