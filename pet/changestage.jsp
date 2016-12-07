<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
PetUserBean petUser = action.getPetUser();
String tip = (String)request.getAttribute("tip");
String url=("/pet/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="积分换道具">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(petUser != null){%>
总积分:<%=petUser.getIntegral()%><br/>
剩余积分:<%=petUser.getLeftintegral()%><br/>
<a href="/Column.do?columnId=9395">如何获得积分</a><br/>
==可兑换道具==<br/>
宠物孟婆汤200 点:拥有后在宠物医院可以让你的宠物重新分配技能点
<a href="/pet/changeresult.jsp?type=1">兑换</a><br/>
隐藏宠物卡300点:将获得一个非常特别的宠物好好珍惜哦
<a href="/pet/changeresult.jsp?type=2">兑换</a><br/>
狙击步枪证15点:拥有就能在狩猎区购买百发百中的狙击枪
<a href="/pet/changeresult.jsp?type=3">兑换</a><br/>
宠物千里眼10点：可以查看别人宠物的详细属性。（可使用10次）
<a href="/pet/changeresult.jsp?type=4">兑换</a><br/>
宠物变形卡1600点：在医院给宠物动手术，改变宠物类型，保留级别积分！
<a href="/pet/changeresult.jsp?type=5">兑换</a><br/>

<br/>
<%}else{%>
请先携带宠物后,再来兑换<br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>