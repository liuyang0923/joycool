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
装备上五行元素能增加角色的各方面属性.<br/>
不论是哪种元素,都将增加角色的血,气力和体力上限.除此以外还额外增加:<br/>
金:增加防御力<br/>
木:增加血上限<br/>
水:增加气力上限<br/>
火:增加爆发力<br/>
土:增加体力上限<br/>
如果角色本身属于五行元素之一,那么这个效果将收到五行相生相克的影响而改变部分数值.<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>