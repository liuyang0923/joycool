<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*,jc.family.game.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
String cmd=snowAction.getParameterString("cmd");
int mid=snowAction.getParameterInt("mid");
String title="系统提示";
if(cmd.endsWith("m")){title="做雪球列表";}
if(cmd.endsWith("c")){title="扫雪列表";}
if(cmd.endsWith("f")){title="攻击列表";}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title %>"><p align="left"><%=BaseAction.getTop(request, response)%>
名字|<%if(cmd.equals("m")||cmd.equals("c")){ %>
花费金额<%}else if(cmd.equals("f")){ %>击中次数<%} %><br/>
<%List list=snowAction.getMemberGameList();
if(list!=null){if(list.size()>0){
for(int i=0;i<list.size();i++){MemberBean bean=(MemberBean)list.get(i);
if(cmd.endsWith("m")){%>
<%=bean.getNickNameWml() %>|<%=bean.getPayMake() %>雪币<br/>
<%}else if(cmd.endsWith("c")){ %>
<%=bean.getNickNameWml() %>|<%=bean.getPaySweep() %>雪币<br/>
<%}else if(cmd.endsWith("f")){ %>
<%=bean.getNickNameWml() %>|<%=bean.getTotalHit() %>次<br/>
<%}}}}%><%if(request.getAttribute("gameData")!=null&&!"".equals(request.getAttribute("gameData"))){%>
<%=request.getAttribute("gameData") %>
<%}%> 
<a href="activityDetails.jsp?mid=<%=mid %>">返回详细记录</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>