<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%response.setHeader("Cache-Control","no-cache");%><%
PetAction action = new PetAction(request);
%><%@include file="check.jsp"%><%
action.shop();
PetUserBean petUser = action.getPetUser();
String tip = (String)request.getAttribute("tip");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/pet/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="食堂">
<p align="left">
<%=BaseAction.getTop(request, response)%>
饥饿度：<%=petUser.getHungry()%>/100<br/>
商品列表：<br/>
<a href="/pet/shopresult.jsp?shop=1">糖果(饥饿+5,1000乐币)</a><br/>
<a href="/pet/shopresult.jsp?shop=2">牛奶(饥饿+10,2000乐币)</a><br/>
<a href="/pet/shopresult.jsp?shop=3">面包(饥饿+15,3500乐币)</a><br/>
<a href="/pet/shopresult.jsp?shop=4">鸡腿(饥饿+20,5000乐币)</a><br/>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>