<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@page import="net.joycool.wap.util.RandomUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*,jc.guest.pt.*,jc.guest.*" %>
<%response.setHeader("Cache-Control","no-cache"); %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<wml>
<card title="智慧拼图">
<p align="left">
<%
response.setHeader("Cache-Control","no-cache");
JigsawAction action=new JigsawAction(request,response);
GuestHallAction gaction = new GuestHallAction(request,response);
GuestUserInfo guestUser = gaction.getGuestUser();

JigsawUserBean jub=action.getUserDetails();
%>
<img src="<%=JigsawAction.ATTACH_URL_ROOT%>logo.gif" alt="o"/><br/>
将页面中的文字或图片按顺序排列即为完成.<%if(guestUser!=null){%>每新开一局将会花费1游币.<%}%><br/>
我的闯关积分:<%=jub==null?0:jub.getMaxScore()%><br/>
<a href="numjigsaw.jsp">文字模式</a>--每过一关会获得相应的闯关积分,看谁能拿到更多哦~<br/>
<a href="piclist.jsp">图片模式</a>--可随意选择难度和图片进行拼图.<br/>
<a href="jigsawlist.jsp">闯关积分排行榜</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=JigsawAction.winString==null?"还没有人玩过一关哦,快来做第一个吧!":JigsawAction.winString%><br/>
<%=BaseAction.getBottomShort(request,response)%></p>
</card>
</wml>