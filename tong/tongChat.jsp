<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.world.ChatWorldBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongChat(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
url=("/tong/tongList.jsp");%>
<card title="城邦列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转城邦列表)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
List chatList=(List)request.getAttribute("chatList");
PagingBean paging = (PagingBean)request.getAttribute("paging");
TongBean tong =(TongBean)request.getAttribute("tong");
String tongOnlineUserCount=(String)request.getAttribute("tongOnlineUserCount");
url=("/tong/tongChat.jsp?tongId="+tong.getId());
%>
<card title="帮会聊天室" ontimer="<%=response.encodeURL(url)%>">
<timer value="300"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="tongChatPost.jsp?tongId=<%=tong.getId()%>">发言</a>|<a href="/tong/tongChat.jsp?tongId=<%=tong.getId()%>">刷新</a>|<a href="tongUserList.jsp?tongId=<%=tong.getId()%>&amp;orderBy=online">在线帮众</a>(<%=tongOnlineUserCount%>人)<br/>
<%if(chatList.size()>0){ChatWorldBean chatWorkd=null;
for(int i=0;i<chatList.size();i++){
chatWorkd=(ChatWorldBean)chatList.get(i);
UserBean user = UserInfoUtil.getUser(chatWorkd.getUserId());
if(user==null)continue;
%>
<%if(user.getId()!=loginUser.getId()){%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><%}else{%><%=StringUtil.toWml(user.getNickName())%><%}%>:<%=StringUtil.toWml(chatWorkd.getContent())%>
(<%=chatWorkd.getCreateDatetime()%>)
<br/>
<%}%><%=paging.shuzifenye("tongChat.jsp?tongId="+tong.getId(), true, "|", response)%><%}else{%>当前聊天室信息为空!<%}%>
<a href="tong.jsp?tongId=<%=tong.getId() %>">返回帮会首页</a><br/> 
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
<%}%>
</wml>