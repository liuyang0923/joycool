<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.bank.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
BankAction bankAction=new BankAction(request);
bankAction.loan(request);
String result=(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%//判断用户有存款
if("saving".equals(result)){
//存款金额
String money=(String)request.getAttribute("money");
%>
<card title="用户有存款">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：对不起，您还有存款<%=money%>，有存款时不能贷款。您还想要什么服务？<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%//判断用户有贷款
}else if("existLoan".equals(result)){
//存款金额
String exitLoan=(String)request.getAttribute("exitLoan");
//可以贷款的金额
String isCanLoan=(String)request.getAttribute("isCanLoan");
%>
<card title="无存款有贷款">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：欢迎！您现在已贷款总额为<%=exitLoan%>。根据等级，您最多还能贷款<%=isCanLoan%>，当前利息<%=bankAction.getLoadAccrual(bankAction.getUserCurrentLoadAccrual())%>每小时。<br/>
您要加贷款：<br/>
<input type="text" name="money" format="*N" maxlength="10"/>乐币<br/>                 
贷款时间（不能超过72小时）<br/>
<input type="text" name="hour" format="*N" maxlength="2"/>小时<br/>
<anchor title="post">确定
  <go href="loanResult.jsp" method="post">
    <postfield name="money" value="$(money)"/>
    <postfield name="hour" value="$(hour)"/>
  </go>
</anchor><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%//判断用户无贷款
}else{
//可以贷款的金额
String CanLoan=(String)request.getAttribute("CanLoan");
%>
<card title="贷款">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：欢迎！您现在贷款额为0，根据等级，您最多还能贷款<%=CanLoan%>，当前利息0.03每小时。<br/>
您要贷款：<br/>
<input type="text" name="money" format="*N" maxlength="10"/>乐币<br/>                 
贷款时间（不能超过72小时）<br/>
<input type="text" name="hour" format="*N" maxlength="2"/>小时<br/>
<anchor title="post">确定
  <go href="loanResult.jsp" method="post">
    <postfield name="money" value="$(money)"/>
    <postfield name="hour" value="$(hour)"/>
  </go>
</anchor><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>