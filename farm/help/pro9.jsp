<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
每增加一级战斗专业将获得一些战斗属性,可以自由分配.<br/>
力量:增加少许攻击力<br/>
敏捷:增加少许攻击力和防御力<br/>
耐力:增加血上限<br/>
智力:增加气力上限<br/>
精神:增加体力上限<br/>
<%if(farmUser.getPro(FarmProBean.PRO_BATTLE)!=null){%>
返回<a href="../user/pro9.jsp">战斗属性分配</a>功能<br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>