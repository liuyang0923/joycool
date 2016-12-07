<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.battlePoint();
FarmUserBean user = action.getUser();
FarmWorld world = action.world;
int id = action.getParameterInt("id");
int[] set = action.getBattlePointSet();
int sum = set[0] + set[1] + set[2] + set[3] + set[4];
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<a href="pro9.jsp">返回</a><br/>
<%}else if(user.getBattlePoint()>0){

%>
===战斗属性<a href="../help/pro9.jsp">??</a>===<br/>
已分配点数:<%=sum%>/<%=user.getBattlePoint()%><br/>
力量:<%=user.getAttr1()+set[0]%><a href="pro9.jsp?a=1&amp;o=0&amp;c=1">+1</a>|<a href="pro9.jsp?a=1&amp;o=0&amp;c=2">+2</a><br/>
敏捷:<%=user.getAttr2()+set[1]%><a href="pro9.jsp?a=1&amp;o=1&amp;c=1">+1</a>|<a href="pro9.jsp?a=1&amp;o=1&amp;c=2">+2</a><br/>
耐力:<%=user.getAttr3()+set[2]%><a href="pro9.jsp?a=1&amp;o=2&amp;c=1">+1</a>|<a href="pro9.jsp?a=1&amp;o=2&amp;c=2">+2</a><br/>
智力:<%=user.getAttr4()+set[3]%><a href="pro9.jsp?a=1&amp;o=3&amp;c=1">+1</a>|<a href="pro9.jsp?a=1&amp;o=3&amp;c=2">+2</a><br/>
精神:<%=user.getAttr5()+set[4]%><a href="pro9.jsp?a=1&amp;o=4&amp;c=1">+1</a>|<a href="pro9.jsp?a=1&amp;o=4&amp;c=2">+2</a><br/>

<a href="pro9.jsp?a=2">确认以上分配</a><%if(sum>0){%>|<a href="pro9.jsp?a=3">重新分配</a><%}%><br/>
<%}else{%>
没有剩余的可分配点数<br/>
力量:<%=user.getAttr1()+set[0]%><br/>
敏捷:<%=user.getAttr2()+set[1]%><br/>
耐力:<%=user.getAttr3()+set[2]%><br/>
智力:<%=user.getAttr4()+set[3]%><br/>
精神:<%=user.getAttr5()+set[4]%><br/>

<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>