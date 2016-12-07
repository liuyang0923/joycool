<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.bean.tong.TongApplyBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tong(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
String url=null;
if(result.equals("failure")){
response.sendRedirect(("/tong/tongCenter.jsp"));
return;}
TongBean tong =(TongBean)request.getAttribute("tong");
if(tong.getUserId()!= loginUser.getId()){
response.sendRedirect(("/tong/tongCenter.jsp"));
return;}
int cmd=action.getParameterInt("cmd");
int tongId=action.getParameterInt("tongId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(cmd==0){%>
<wml><card title="转为家族"><p align="left">
是否把帮会转为家族?<br/>
<a href="tochange.jsp?tongId=<%=tongId%>">确定</a><br/>
去看看<a href="change.jsp?tongId=<%=tongId%>&#38;cmd=1">转家族须知</a>再说<br/>
<a href="tong.jsp?tongId=<%=tongId%>">返回我的帮会</a><br/><%
}if(cmd==1){
%><wml><card title="帮派转家族须知"><p align="left">
帮派转家族须知<br/>
1、帮会可以直接转为对应等级的家族,可省下无数乐币以及跳过游戏经验值的约束.<br/>
2、500人以上的帮会不能转为家族.<br/>
3、帮会转为家族以后,帮主位置变成族长,组员全转过去,但帮会的其他职位需要族长重新设定.帮会基金直接到家族基金里,帮会论坛的内容直接变成转移到家族论坛中.旗帜无,股票不受影响.<br/>
<a href="tochange.jsp?tongId=<%=tongId%>">直接转成家族</a><br/>
<a href="tong.jsp?tongId=<%=tongId%>">返回我的帮会</a><br/><%
}%>
<%=BaseAction.getBottom(request,response)%></p></card></wml>