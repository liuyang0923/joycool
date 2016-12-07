<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.bank.*"%><%
response.setHeader("Cache-Control", "no-cache");
String operation=(String)session.getAttribute("operation");
if(operation==null)
{
	//response.sendRedirect(("store.jsp"));
	BaseAction.sendRedirect("store.jsp", response);
	return;
}else
{
	session.removeAttribute("operation");
}
BankAction bank=new BankAction(request);
bank.store(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String tip = (String)request.getAttribute("tip");
String userStore=(String)request.getAttribute("userStore");
String interestRate=(String)request.getAttribute("interestRate");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//用户存款数额有误
if(tip!=null){
session.setAttribute("operation","refill");
%>
<card title="敬告!存款数额有误">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：<%=tip%><br/>
您拥有的乐币数为<%=us.getGamePoint()%>，重新填写存款？<br/>
<input type="text" name="mon" format="*N" emptyok="false" value=""/>乐币<br/>
<anchor title="接收存款">确定存款
<go href="storeResult.jsp" method="post">
<postfield name="money" value="$mon"/>
</go>
</anchor><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
//用户存款成功
}else{
%>
<card title="存款成功" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：存款成功!您现有乐币<%=us.getGamePoint()%>，存款<%=userStore%>，当前利息每小时<%=interestRate%>。您还要什么服务？<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
} 
%>
</wml>
