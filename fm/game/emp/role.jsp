<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null) {response.sendRedirect("war.jsp");return;}//观众可以直接进入
if (vsUser.getRole() != null){response.sendRedirect("seat.jsp");return;}//选过角色的用户直接进入座位页面
if (!action.hasParam("cid")) {response.sendRedirect("choro.jsp");return;} // 无参数就返回选角色
List chooseList = vsGame.getFmAChooseRoleList();
if (vsUser.getSide() == 1) {
	chooseList = vsGame.getFmBChooseRoleList();
}
int index = action.getParameterInt("cid");
if (index < 0 || index >= chooseList.size()){response.sendRedirect("choro.jsp");return;} // 参数错误,返回重选角色
EmperorChooseRoleBean chooseBean = (EmperorChooseRoleBean) chooseList.get(index);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="帝王之争"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (chooseBean != null) {
	EmperorRoleBean role = chooseBean.getRole();
%>
角色:<%=role.getName()%><br/>
人物介绍:<%=role.getPeopleIntroduction()%><br/>
技能:<%=role.getSkillName()%><br/>
技能介绍:<%=role.getSkillIntroduction()%><br/>
<%if (chooseBean.isBeChoose()) {%>已被选,<a href="choro.jsp">返回</a><%} else {%><a href="ovro.jsp?cid=<%=index%>">确认选择</a>|<a href="choro.jsp">取消</a><%}%><br/>
<%}%>
<%if(vsUser==null){%>
<%=vsGame.getChat().getChatString(0, 3)%><a href="chat2.jsp">更多聊天信息</a><br/>
<%}else{%>
聊天信息&gt;&gt;家族|<a href="chat2.jsp">公共</a><br/>
<%=vsGame.getChat(vsUser.getSide()).getChatString(0, 3)%>
<input name="fchat"  maxlength="100"/>
<anchor>发言<go href="chat.jsp" method="post"><postfield name="content" value="$fchat"/></go></anchor><br/>
<a href="chat.jsp">更多聊天信息</a><br/>
<%}%>
<a href="/fm/index.jsp">&lt;&lt;返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>