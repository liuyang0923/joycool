<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.top();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
PetUserBean petUser = (PetUserBean)request.getAttribute("petUser");
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/pet/top.jsp");
PetUserBean petBean;
UserBean user;
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
Vector vector = (Vector) request.getAttribute("vector");
String prefixUrl = (String) request.getAttribute("prefixUrl");
int totalHallPageCount = ((Integer) request.getAttribute("totalHallPageCount")).intValue();
String count = (String) request.getAttribute("count");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="趣味赛跑">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(5,request,response)%>

<%if(petUser != null){%>
<%=StringUtil.toWml(petUser.getName())%>:第<%=count%>名<br/>
<%}%>
==宠物排行榜==<br/>
<%
Iterator iter = vector.iterator();
int number=0;
while(iter.hasNext()) {
number++;
petBean = (PetUserBean)iter.next();
user = UserInfoUtil.getUser(petBean.getUser_id());
%>
<%=pageIndex*10+number%>.
<a href="/pet/viewpet.jsp?id=<%=petBean.getId()%>">
<%=StringUtil.toWml(petBean.getName())%>
</a>
<%=petBean.getRank()%>级<br/>
<%}%>


<%
String fenye = PageUtil.shuzifenye(totalHallPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%}%>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>

</wml>