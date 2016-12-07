<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.pk.PKEquipBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserBagBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.bodyEquip(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/pk/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转其它场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
PKUserBagBean[] userBagArray =(PKUserBagBean[])request.getAttribute("userBagArray");
String tip="目前尚无装备";
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/pk/equip.gif" alt="装备"/><br/>
<%
PKUserBagBean userBag=null;
for(int i=0;i<userBagArray.length;i++){
	userBag=userBagArray[i];%>
	<%=i+1%>.<%=PKAction.BODY_NAME[i]%>：
	<%if(userBag==null){%>
		<%=tip%><br/>
	<%}else{%>
		<a href="/pk/bodyDetail.jsp?id=<%=userBag.getId()%>"><%=((PKEquipBean)userBag.getPorto()).getName()%></a><br/>
	<%}%>
<%}%>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>