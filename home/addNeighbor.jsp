<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if(loginUser.getHome()==1){
%>
<%
//所要查看其邻居的用户ID，如果没有些参数则查看本人邻居
String userId=request.getParameter("userId");
//DAO、
IUserService userService=ServiceFactory.createUserService();
//要查看的用户的信息
UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));      
//将其加为自己的邻居
HomeAction home=new HomeAction(request);
home.addNeighbor(Integer.parseInt(userId));
%>
<card title="家园邻居" ontimer="<%=response.encodeURL("home.jsp?userId="+userId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%><br/>
您已成功将<%=StringUtil.toWml(user.getNickName())%>的家园，变为自己家园的邻居!(3秒钟后跳回家园首页)<br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%=userId%>" >返回<%= StringUtil.toWml(user.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="开通家园？">
<p align="left">
您还没有个人家园呢，不能加对方为邻居。是否立刻开通？<br/>
<a href="/home/inputRegisterInfo.jsp" >我也要建立家园!</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>