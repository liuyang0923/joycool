<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%
response.setHeader("Cache-Control","no-cache");
//登录用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷街霸" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
动作组设置<br/>
<a href="/wgamefight/group.jsp?groupId=1">动作组1</a><br/>
<a href="/wgamefight/group.jsp?groupId=2">动作组2</a><br/>
<a href="/wgamefight/group.jsp?groupId=3">动作组3</a><br/><br/>
<a href="/wgamefight/bkStart.jsp">开始坐庄</a><br/>
<a href="/wgamefight/index.jsp">返回首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>