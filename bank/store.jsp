<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.bank.*"%><%
response.setHeader("Cache-Control", "no-cache");
session.setAttribute("operation","begin");
BankAction bank=new BankAction(request);
bank.storeCounter(request);
UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
boolean hasLoad=((Boolean)request.getAttribute("hasLoad")).booleanValue();
String userStore=(String)request.getAttribute("userStore");
String interestRate=(String)request.getAttribute("interestRate");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//用户有贷款
if(hasLoad){
%>
<card title="敬告!有贷款不允许存款">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：对不起，您在偿还贷款之前，不能存款。您还想要什么服务？<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
//用户无贷款
}else{
%>
<card title="存款柜台" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：欢迎!您现在乐币<%=us.getGamePoint()%>，现有存款<%=userStore%>，当前利息每小时<%=interestRate%>。您要存多少？<br/>
<input type="text" name="mon" format="*N" emptyok="false" value=""/>乐币<br/>
<anchor title="接收存款">确定
<go href="storeResult.jsp" method="post">
<postfield name="money" value="$mon"/>
</go>
</anchor><br/><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
} 
%>
</wml>
