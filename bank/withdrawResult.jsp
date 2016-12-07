<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.bank.*"%><%
response.setHeader("Cache-Control", "no-cache");
String operation=(String)session.getAttribute("operation");
if(operation==null)
{
	//response.sendRedirect(("withdraw.jsp"));
	BaseAction.sendRedirect("withdraw.jsp", response);
	return;
}else
{
	session.removeAttribute("operation");
}
BankAction bank=new BankAction(request);
bank.withdraw(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String tip = (String)request.getAttribute("tip");
String userStore=(String)request.getAttribute("userStore");
String interestRate=(String)request.getAttribute("interestRate");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//用户取款数额有误
if(tip!=null){
session.setAttribute("operation","refill");
%>
<card title="敬告!取款数额有误" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：<%=tip%><br/>
您现在存款金额为<%=userStore%>，重新填写取款？<br/>
<input type="text" name="mon" format="*N" emptyok="false" ></input>乐币<br/>
<anchor title="接收取款">确定取款
<go href="withdrawResult.jsp" method="post">
<postfield name="money" value="$mon"/>
</go>
</anchor><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
//用户取款成功
}else{
%>
<card title="取款成功" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：取款成功!您现在乐币<%=us.getGamePoint()%>，存款<%=userStore%>，当前利息每小时<%=interestRate%>。您还要什么服务？<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
} 
%>
</wml>
