<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.petintro();
PetTypeBean petBean = (PetTypeBean)request.getAttribute("petBean");
String result = (String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="宠物介绍">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%=petBean.getName()%><br/>
<img src="img/<%=petBean.getId()%>_1.gif" alt=""/><br/>
<%=petBean.getLon()%><br/>
<%if(statusBeans.getGamePoint() >= petBean.getPrice()) {%>
<a href="/pet/buypetresult.jsp?pettype=<%=petBean.getId()%>">选择购买<%=petBean.getPrice()%>乐币</a>
<%}else{%>
价格:<%=petBean.getPrice()%>乐币,您身上没有这么多乐币.
<%}%><br/>
<a href="/pet/buypet.jsp">返回宠物商店</a><br/>
<a href="/pet/info.jsp">返回宠物大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

</wml>