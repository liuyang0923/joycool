<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightUserBean" %><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
action.group(request);
String groupId = (String)request.getAttribute("groupId");
String tip = (String) request.getAttribute("tip");
String result = (String) request.getAttribute("result");
int boutId = StringUtil.toInt((String)request.getAttribute("boutId"));
int actId = StringUtil.toInt((String)request.getAttribute("actId"));
WGameFightUserBean userBean = (WGameFightUserBean)request.getAttribute("userBean");
String strCon[] = null;
String str[] =null;
int s = 0;
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
<%}else{
String content = (String)request.getAttribute("content");
strCon =content.split(",");%>
动作组<%=groupId%><br/>
<%if(userBean!=null){
String con = userBean.getContent();
str = con.split(",");
for(int i=0;i<str.length;i++){
int actionId = StringUtil.toInt(str[i]);
if(s>0){s--;%>
回合<%=i+1%>:<br/>
<%}else{s=action.actionW[actionId]-1;%>
<a href="/wgamefight/bout.jsp?boutId=<%=(i+1)%>&amp;groupId=<%=groupId%>">回合<%=i+1%>:</a><%if(StringUtil.toInt(str[i])!=0){%><%=action.getStringName(StringUtil.toInt(str[i]))%><%}%><br/>
<%}%><%}%><%}else{%>
<%for(int i=0;i<strCon.length;i++){
int actionId = StringUtil.toInt(strCon[i]);
if(s>0){s--;%>
回合<%=i+1%>:<br/>
<%}else{s=action.actionW[actionId]-1;%>
<a href="/wgamefight/bout.jsp?boutId=<%=(i+1)%>&amp;groupId=<%=groupId%>">回合<%=i+1%>:</a><%if(StringUtil.toInt(strCon[i])!=0){%><%=action.getStringName(actionId)%><%}%><br/>

<%}%>
<%}%>
<%}%>
<%}%>
<br/>
<a href="/wgamefight/index.jsp">保存设置</a><br/>
<a href="/wgamefight/actGroup.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>