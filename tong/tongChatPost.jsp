<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.world.ChatWorldBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongChatPost(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
String url=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
url=("tongList.jsp");%>
<card title="城邦列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转城邦列表)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong =(TongBean)request.getAttribute("tong");
%>
<card title="帮会聊天室">
<%--<onevent type="onenterforward">
<refresh>
<setvar name="content" value=""/>
</refresh>
</onevent>--%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
发言内容:<br/>
<input name="content"  maxlength="100" value=""/><br/>
<anchor title="确定">发表
<go href="tongChatPostResult.jsp?tongId=<%=tong.getId()%>" method="post">
  <postfield name="content" value="$content"/>
</go>
</anchor><br/>
<a href="tong.jsp?tongId=<%=tong.getId() %>">返回帮会首页</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>