<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean user = action.getUser();
FarmWorld world = action.world;
int id = action.getParameterInt("id");
FarmUserProBean userPro = null;
FarmProBean pro = null;
if(id > 0) {
	userPro = user.getPro(id);
	pro = world.getPro(id);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<% if(pro==null) { %>

不存在这个专业<br/>

<%}else{%>

**<%=pro.getName()%>**<br/>
<%=pro.getInfo()%><br/>
需要点数:<%=pro.getPoint()%><br/>
<% if(userPro==null) { %>
最高等级:<%=pro.getMaxRank()%><br/>
<a href="prosr.jsp?a=2&amp;id=<%=id%>">学习</a>
<%}else{%>
等级:<%=userPro.getRank()%>/<%=userPro.getMaxRank()%><br/>
经验值:<%=userPro.getExp()%>/<%=userPro.getUpgradeExp()%><br/>
<%if(userPro.canUpgrade(pro)){%><a href="prosr.jsp?a=1&amp;id=<%=id%>">升级</a><%}%>

<a href="prosr.jsp?a=3&amp;id=<%=id%>">废弃</a><br/>
<%}%>

<%}%>
<a href="pros.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>