<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.List"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.tong.TongCityRecordBean"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.presentHonorRs(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="荣誉度" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/tongList.jsp">帮会列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong = (TongBean) request.getAttribute("tong");
String url=("/tong/tongHonorList.jsp?tongId="+tong.getId());
%>
<card title="荣誉度" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后返回帮会旗帜)<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>