<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@include file="../bank/checkpw.jsp"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
int tongId = action.getParameterInt("tongId");
int userId = action.getParameterInt("userId");
UserBean user = UserInfoUtil.getUser(userId);
String nickname = "";
if(user!=null)
nickname=user.getNickNameWml();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮会会员">
<p align="left">
确定要开除[<%=nickname%>]？<br/>
<a href="tongExpel.jsp?userId=<%=userId %>&amp;tongId=<%=tongId%>">确认开除</a><br/>
<a href="tongUserList.jsp?tongId=<%=tongId%>">点错了返回</a><br/>
</p>
</card>
</wml>