<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.bank.*"%><%
response.setHeader("Cache-Control", "no-cache");
session.setAttribute("operation","begin");
BankAction bank=new BankAction(request);
long userStore=bank.getUserStore();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//用户无存款
if(userStore==0){
%>
<card title="敬告!无存款无法取款">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：对不起，您没有存款，想要什么服务？<br/><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
//用户有存款
}else{
%>
<card title="取款柜台" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：欢迎!您现在存款金额为<%=userStore%>，您要取多少？<br/>
<input type="text" name="mon" format="*N" emptyok="false" ></input>乐币<br/>
<anchor title="接收取款">确定
<go href="withdrawResult.jsp" method="post">
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
