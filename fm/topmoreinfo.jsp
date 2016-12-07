<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.*,net.joycool.wap.util.StringUtil,java.util.List,net.joycool.wap.bean.PagingBean"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
int a=action.getParameterInt("a");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族"><p align="left"><%=BaseAction.getTop(request, response)%>
待定~<br/>
<a href="topmore.jsp?a=<%=a%>">返回家族排行榜</a><br/>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>