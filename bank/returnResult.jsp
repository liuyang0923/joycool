<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.action.bank.*"%><%@ page import="net.joycool.wap.bean.bank.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
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
	response.sendRedirect("returnMoney.jsp");
	return;
}

if(!bankAction.isCanReturnLoadMoney((int)loadBean.getMoney())){
// 没有足够的乐币还款
%>
<card title="还款失败">
<p align="left">
银行经理：还款失败！您的乐币不足！<br/>
<a href="/bank/returnMoney.jsp">重新选择还款</a><br/><br/>

<%@include file="bottom.jsp"%>
</p>
</card>
<%
}else {
// 还款操作
bankAction.returnMoney(id);
// 有足够的乐币还款
	UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
	UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
	if (!bankAction.userHaveLoad()){
		// 用户没有贷款了
	%>
	<card title="全部贷款已归还">
	<p align="left">
	银行经理：您的全部贷款已归还！<br/>
	您现在乐币<%=us.getGamePoint()%>.<br/>
	您还需要什么服务<br/><br/>

<%@include file="bottom.jsp"%>
	</p>
	</card>
	<%
	}else{
%>
<card title="还款成功">
<p align="left">
银行经理：您在 <%=loadBean.getCreateTime2()%> 时刻的贷款 <%=loadBean.getMoney()%> 已还款成功！<br/>
您目前拥有的乐币数为<%=us.getGamePoint()%>，未还贷款有：<br/>
（归还倒计时）   (款额）<br/>
<%
	Vector loadList = (Vector)bankAction.getUserLoadMoneyList();
	LoadBean loadBean1 = null;
	for(int i=0;i<loadList.size();i++){
		loadBean1 = (LoadBean)loadList.get(i);
		%>
		<a href="/bank/returnConfirm.jsp?id=<%=loadBean1.getId()%>">
		<%=bankAction.getLeaveTime(loadBean1.getCurrentTime(),loadBean1.getExpireTime())+" "%>   <%=loadBean1.getMoney()%></a><br/>
		<%
	}
%>
继续还款请直接选择<br/><br/>

<%@include file="bottom.jsp"%>
</p>
</card>
<%
	}
}
%>
</wml>