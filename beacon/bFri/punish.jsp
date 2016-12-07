<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	String action = request.getParameter("action");
	boolean flag = false;
	if(action != null && action.equals("punish")){
		flag = true;
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="惩罚奴隶"><p><%=BaseAction.getTop(request, response)%>
<%if(!flag){
	int uid = Integer.valueOf(request.getParameter("uid")).intValue();
	int rank = Integer.valueOf(request.getParameter("rank")).intValue();
	UserBean userBean = UserInfoUtil.getUser(uid);%>
你要用最狠的手段去惩罚你的奴隶<%=StringUtil.toWml(userBean.getNickName()) %>，要让他知道你这个主人有多么的严厉<br/>
*惩罚前一定要先安抚，否则奴隶会逃跑的哦!*<br/>
<%if(rank == 0) {%> 
邀请上来的奴隶受限制<br/>
<%} %><a href="<%=("punish.jsp?action=punish&amp;type=1&amp;uid=" + uid ) %>">-让<%=userBean.getGenderText() %>出去要饭</a><br/>
<a href="<%=("punish.jsp?action=punish&amp;type=2&amp;uid=" + uid ) %>">-租<%=userBean.getGenderText() %>给别人当猪仔</a><br/>
<a href="<%=("punish.jsp?action=punish&amp;type=3&amp;uid=" + uid ) %>">-让<%=userBean.getGenderText() %>打扫房间</a><br/>
<%if(rank > 0) {%><a href="<%=("punish.jsp?action=punish&amp;type=4&amp;uid=" + uid ) %>">-让<%=userBean.getGenderText() %>去煤窑挖煤</a><br/>
<a href="<%=("punish.jsp?action=punish&amp;type=5&amp;uid=" + uid ) %>">-一天不许吃饭</a><br/>
<a href="<%=("punish.jsp?action=punish&amp;type=6&amp;uid=" + uid ) %>">-修整操场</a><br/>
<a href="<%=("punish.jsp?action=punish&amp;type=7&amp;uid=" + uid ) %>">-为老百姓挑水送粮</a><br/>
<a href="<%=("punish.jsp?action=punish&amp;type=8&amp;uid=" + uid ) %>">-把<%=userBean.getGenderText() %>许配给老黑奴</a><br/>
<%if(false) {%>
自定义惩罚：<br/>
我<input name="a" size="5"/><%=userBean.getGenderText() %><input name="c" size="20"/><br/>
例如：我 让 <%=userBean.getGenderText() %> 为我倒洗脚水<br/>
<anchor title="post">惩罚
<go href="<%=("punish.jsp?action=punish&amp;type=10&amp;uid=" + uid ) %>" method="post">
<postfield name="a" value="$a"/>
<postfield name="c" value="$c"/>
</go></anchor>
<%}}%>
<a href="<%=("mySlaves.jsp")%>">返回我的奴隶</a><br/>
<a href="<%=("myInfo.jsp")%>">返回朋友买卖首页</a><br/>
<%} else {
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
		ActionBuyFriend buyFriend = new ActionBuyFriend(request,response);
		int uid = buyFriend.getParameterInt("uid");
		if(buyFriend.hasParam("c") || serviceSlave.isAppease(buyFriend.getLoginUser().getId(), uid)) {
		buyFriend.punish();%>
<%=request.getAttribute("msg") %><br/>
<%if(request.getAttribute("money") != null) {%>
惩罚一天之后将得到￥<%=request.getAttribute("money") %><br/>
<%} %><a href="<%=("mySlaves.jsp") %>">返回我的奴隶</a><br/>
<%}else {%>
你还没有安抚您的奴隶！奴隶被安抚后，会死心塌地的人你摆布，否则他会逃跑的哦！<br/>
<a href="punish.jsp?action=punish&amp;type=<%=buyFriend.getParameterInt("type") %>&amp;uid=<%=uid %>&amp;c=1">继续惩罚</a>|<a href="appease.jsp?uid=<%=uid %>&amp;rank=2">去安抚他</a><br/>
<%}}%><%=BaseAction.getBottomShort(request, response)%></p></card></wml>