<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.action.bank.*"%><%@ page import="net.joycool.wap.bean.bank.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
BankAction bankAction=new BankAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
String ids = request.getParameter("id");
int id = StringUtil.toInt(ids);
if (id == -1){
	out.clearBuffer();
	response.sendRedirect("returnMoney.jsp");
	return;
}

LoadBean loadBean = bankAction.getLoad(id);
if(loadBean == null){
	out.clearBuffer();
	response.sendRedirect("returnMoney.jsp?msg=1");
	return;
}
%>
<card title="贷款还款">
<p align="left">
银行经理：您所选的还款项目是：<br/><br/>
<%=loadBean.getCreateTime2()%> 时刻的贷款 <%=loadBean.getMoney()%><br/>
<a href="/bank/returnResult.jsp?id=<%=id%>">确定</a><br/>
<a href="/bank/returnMoney.jsp">重新选择</a><br/><br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>