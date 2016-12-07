<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.match();
PetUserBean petUser = action.getPetUser();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/pet/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="比赛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if((petUser != null)&&(petUser.getMatchid() != 0)&&(petUser.getMatchtype() != 0)){%>
<a href="/pet/mymatch.jsp">我的比赛</a><br/>
<%}%>
比赛项目:<br/>
<a href="/pet/swimindex.jsp">泳池争霸</a><br/>
需要的能力靠大家摸索，5人报名开赛，参加就有宠物经验值奖励!在搞笑运气事件和有趣比赛道具中磨炼宠物。<br/>
<a href="/pet/runindex.jsp">趣味赛跑(6-32级)</a><br/>
8人报名即自动开始的跑步比赛，宠物相关能力起决定因素，不过也有有趣的运气事件影响速度哦!<br/>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>

</wml>