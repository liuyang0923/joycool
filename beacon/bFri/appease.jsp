<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	String action = request.getParameter("action");
	boolean flag = false;
	if(action != null && action.equals("appease")){
		flag = true;
	}
	//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	//ServiceMaster serviceMaster = ServiceMaster.getInstance();
	//BeanMaster master = serviceMaster.getMasterByUid(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="安抚奴隶"><p><%=BaseAction.getTop(request, response)%>
<%if(!flag){
	int uid = Integer.valueOf(request.getParameter("uid")).intValue();
	int rank = Integer.valueOf(request.getParameter("rank")).intValue();
	UserBean userBean = UserInfoUtil.getUser(uid);
%>
你可以安抚你的奴隶<%=userBean.getNickNameWml() %>!，让他更听你的话<br/>
请选择你要怎么安抚你的奴隶<br/>
<%if(rank == 0) {%> 
邀请上来的奴隶受限制<br/>
<%} %>
<a href="<%=("appease.jsp?action=appease&amp;type=1&amp;uid=" + uid ) %>">-为<%=userBean.getGenderText() %>上烟</a><br/>
<a href="<%=("appease.jsp?action=appease&amp;type=2&amp;uid=" + uid ) %>">-为<%=userBean.getGenderText() %>洗衣服</a><br/>
<a href="<%=("appease.jsp?action=appease&amp;type=3&amp;uid=" + uid ) %>">-摸摸为<%=userBean.getGenderText() %>的头</a><br/>
<%if(rank > 0) {%>
<a href="<%=("appease.jsp?action=appease&amp;type=4&amp;uid=" + uid ) %>">-给<%=userBean.getGenderText() %>零用钱</a><br/>
<a href="<%=("appease.jsp?action=appease&amp;type=5&amp;uid=" + uid ) %>">-为<%=userBean.getGenderText() %>穿袜子</a><br/>
<a href="<%=("appease.jsp?action=appease&amp;type=6&amp;uid=" + uid ) %>">-听<%=userBean.getGenderText() %>阐述心声</a><br/>
<%if(false){%>
自定义安抚：<br/>
我<input name="a" size="5"/><%=userBean.getGenderText() %><input name="c" size="20"/><br/>
例如：我为<%=userBean.getGenderText() %>捶捶背<br/>
<anchor title="post">安抚
<go href="<%=("appease.jsp?action=appease&amp;type=10&amp;uid=" + uid ) %>" method="post">
<postfield name="a" value="$a"/>
<postfield name="c" value="$c"/>
</go>
</anchor>
<%}}%>
<a href="<%=("mySlaves.jsp")%>">返回我的奴隶</a><br/>
<a href="<%=("myInfo.jsp")%>">返回朋友买卖首页</a><br/>
<% }else {
	ActionBuyFriend buyFriend = new ActionBuyFriend(request,response);
	buyFriend.appease();
%><%=request.getAttribute("msg") %><br/><a href="<%=("mySlaves.jsp") %>">返回我的奴隶</a><br/>
<%} %><%=BaseAction.getBottomShort(request, response)%></p></card></wml>