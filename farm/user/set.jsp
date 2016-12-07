<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
if(farmUser.getRank() < 20)
	action.set();
else {
	response.sendRedirect("info.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<a href="set.jsp">重新修改</a><br/>
<%}else{%>

姓名:<%=farmUser.getNameWml()%><br/>
经验:<%=farmUser.getExp()%>/<%=farmUser.getUpgradeExp()%><br/>
等级:<%=farmUser.getRank()%>/<%=farmUser.getMaxRank()%><br/>
财产:<%=FarmWorld.formatMoney(farmUser.getMoney())%><br/>
<br/>
<%if(farmUser.getName().length()==0){%>

姓名必须是2-4位的中文,因为只能修改一次,请谨慎选择.<br/>
<%if(farmUser.getName().length()>0){%>改名需要<%=FarmWorld.formatMoney(farmUser.getRenameMoney())%><br/><%}%>
姓名:<input name="name" maxlength="5"/><br/>
<anchor title="确定">确认修改
<go href="set.jsp" method="post">
	<postfield name="name" value="$name"/>
</go>
</anchor><br/>
<%}%>
<%}%>
<a href="../map.jsp">返回场景</a><br/>
<a href="info.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>