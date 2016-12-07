<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.NoticeUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
int toId=StringUtil.toInt(request.getParameter("toId"));
UserBean toUser=(UserBean)UserInfoUtil.getUser(toId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="和<%= toUser.getGender() == 1? "他" : "她"%>割袍断义" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
您的结义<%= toUser.getGender() == 1? "兄弟" : "姐妹"%>：
<%= StringUtil.toWml(toUser.getNickName()) %><br/>
<a href="/friend/deleteJy.jsp?toId=<%=toId%>">与<%= toUser.getGender() == 1? "他" : "她"%>割袍断义</a><br/>
<a href="/user/ViewUserInfo.do?userId=<%=toId%>">兄弟姐妹有今生没来世，再考虑下</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>