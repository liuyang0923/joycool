<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.action.bank.*"%><%@ page import="net.joycool.wap.bean.bank.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
BankAction bankAction=new BankAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if(!bankAction.userHaveLoad()){
%>
<card title="用户没有贷款">
<p align="left">
银行经理：对不起，您没有贷款。想要什么服务？<br/><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
}else if(bankAction.userHaveLoad()){
%>
<card title="用户有贷款">
<p align="left">
<%
if (request.getParameter("msg")!=null && request.getParameter("msg").equals("1")){
%>
该批贷款已经过期，请选择其它贷款！<br/>
<%
}
%>
银行经理：欢迎！您现在贷款项目如下：<br/>
（归还倒计时）   (款额）<br/>
<%
Vector loadList = (Vector)bankAction.getUserLoadMoneyList();
if (loadList != null){
	LoadBean loadBean = null;
	for(int i=0;i<loadList.size();i++){
		loadBean = (LoadBean)loadList.get(i);
		%>
		<a href="/bank/returnConfirm.jsp?id=<%=loadBean.getId()%>">
		<%=bankAction.getLeaveTime(loadBean.getCurrentTime(),loadBean.getExpireTime())+" "%>   <%=loadBean.getMoney()%></a><br/>
		<%
	
	}
}
%>
请选择您要还的贷款.<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
}
%>
</wml>