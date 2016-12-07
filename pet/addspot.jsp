<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.spot();
PetUserBean petUser = (PetUserBean)request.getAttribute("petUser");
String tip = (String)request.getAttribute("tip");
PetTypeBean petBean = (PetTypeBean)request.getAttribute("petBean");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/pet/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐宠升级点管理">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/<%=action.getImage()%>" alt=""/><br/>

剩余升级点:<%=petUser.getSpot()%><br/>
灵活；<%=petUser.getAgile()%>
<%if(petUser.getSpot() >= petBean.getAl()){%>
<a href="/pet/spotresult.jsp?type=1">+1(升级点-<%=petBean.getAl()%>)</a>
<%}%>
<br/>
聪明：<%=petUser.getIntel()%>
<%if(petUser.getSpot() >= petBean.getIn()){%>
<a href="/pet/spotresult.jsp?type=2">+1(升级点-<%=petBean.getIn()%>)</a>
<%}%>
<br/>
强壮：<%=petUser.getStrength()%>
<%if(petUser.getSpot() >= petBean.getSt()){%>
<a href="/pet/spotresult.jsp?type=3">+1(升级点-<%=petBean.getSt()%>)</a>
<%}%>
<br/>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>