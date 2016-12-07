<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="有偿求助-发帖页面"><p><%=BaseAction.getTop(request, response)%>
标题:<br/>
<input type="text" name="tl"></input><br/>
奖品设定:<br/>
<input type="text" name="pz"></input><br/>
内容:<br/>
<input type="text" name="cont"></input><br/>
<anchor>确定
<go href="hpback.jsp" method="post">
<postfield name="title" value="$tl"/>
<postfield name="prize" value="$pz"/>
<postfield name="pcont" value="$cont"/>
</go>
</anchor>
<a href="hpindex.jsp">取消</a><br/><%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>