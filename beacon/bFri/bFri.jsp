<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	int uid = StringUtil.toId(request.getParameter("uid"));
	
	if(uid == 0) {
		response.sendRedirect("/beacon/bFri/myInfo.jsp");
		return;
	}
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
	
	BeanMaster master = serviceMaster.getMasterByUid(uid);
	BeanSlave slave = serviceSlave.getSlaveBySlaveUid(uid);
	BeanSlave mySlave = serviceSlave.getSlaveBySlaveUid(loginUser.getId());
	boolean flag = false;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买奴隶"><p><%=BaseAction.getTop(request, response)%>
<%if(mySlave != null && mySlave.getMasterUid() == uid) {%>
你是<%=StringUtil.toWml(master.getNickName()) %>的奴隶，你不能购买你的主人。<br/>赶快<a href="i.jsp">邀请好友</a>赚钱替自己赎身把TA买为自己的奴隶吧<br/>
<%} else {
	if(slave == null) {%>你确定购买<%=StringUtil.toWml(master.getNickName()) %>？<br/>
<%=StringUtil.toWml(master.getNickName()) %>为自由身
<%} else {
flag = (loginUser.getId() == slave.getMasterUid());
if(!flag) {%>
你确定购买<%=StringUtil.toWml(master.getNickName()) %>？<br/>
<%=StringUtil.toWml(master.getNickName()) %>当前是<%=StringUtil.toWml(slave.getMasterNickName()) %>的<%=slave.getSlaveTypeString() %>
<%} else {%>
<%=StringUtil.toWml(master.getNickName()) %>已经是你的奴隶<br/>
<%}}
if(!flag) { %>
<br/><%=StringUtil.toWml(master.getNickName()) %>当前身价为：<%=master.getPrice() %><br/>
希望<%=StringUtil.toWml(master.getNickName()) %>是你的<br/>
<select name="type"><option value="1">奴隶</option><option value="2">丫鬟</option><option value="3">仆人</option></select><br/>给他起个外号吧<br/>
<input name="alias"/><br/>
<anchor title="post">购买
<go href="<%=("doBuy.jsp?uid=" + uid)%>" method="post">
<postfield name="type" value="$type"/>
<postfield name="alias" value="$alias"/>
</go></anchor>
<%}}
String n = request.getParameter("n");
if(n.equals("a")){%>
<a href="<%=("bFris.jsp")%>">返回</a>
<%} else if(n.equals("b")){%>
<a href="<%=("myFris.jsp")%>">返回</a>
<%} else {%>
<a href="<%=("myInfo.jsp")%>">返回</a>
<%} %><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>

