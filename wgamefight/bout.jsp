<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
action.bout(request);
String boutId = (String)request.getAttribute("boutId");
String groupId = (String)request.getAttribute("groupId");
String result =null;
String tip =null;
//登录用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷街霸" >
<p align="left">
<%=BaseAction.getTop(request, response)%>

回合<%=boutId%>:<br/>
动作/点数/伤害值<br/>
<a href="/wgamefight/group.jsp?groupId=<%=groupId%>&amp;boutId=<%=boutId%>&amp;actId=<%=1%>">轻拳</a>/1/1<br/>
<a href="/wgamefight/group.jsp?groupId=<%=groupId%>&amp;boutId=<%=boutId%>&amp;actId=<%=2%>">重拳</a>/2/2<br/>
<a href="/wgamefight/group.jsp?groupId=<%=groupId%>&amp;boutId=<%=boutId%>&amp;actId=<%=3%>">重腿</a>/3/3<br/>
<a href="/wgamefight/group.jsp?groupId=<%=groupId%>&amp;boutId=<%=boutId%>&amp;actId=<%=4%>">防御</a>/1/-<br/>
<a href="/wgamefight/group.jsp?groupId=<%=groupId%>&amp;boutId=<%=boutId%>&amp;actId=<%=5%>">闪避</a>/2/-<br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>