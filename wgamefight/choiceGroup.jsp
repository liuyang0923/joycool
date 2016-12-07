<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightUserBean" %><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
action.choiceGroup(request);
Vector fightList = (Vector)request.getAttribute("fightList");
WGameFightUserBean userBean =null;
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
//登录用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷街霸" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//参数错误
if("notGroup".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamefight/index.jsp">返回</a><br/>
<%}else{%>
选择动作组<br/>
<%for(int i=0;i<fightList.size();i++){
userBean = (WGameFightUserBean)fightList.get(i);%>
<a href="/wgamefight/bkStart.jsp?groupId=<%=userBean.getGroupId()%>">动作组<%=userBean.getGroupId()%></a><br/>
<%}%>
<%}%>

<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>