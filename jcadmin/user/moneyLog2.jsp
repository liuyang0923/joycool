<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.bank.BankAction" %><%@ page import="net.joycool.wap.bean.UserMoneyLogBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.*" %><%!
static int numberPage = 50;
static IUserService userService = ServiceFactory.createUserService();
static IBankService bankService = ServiceFactory.createBankService();
%><%
CustomAction action = new CustomAction(request);
int userId = action.getParameterInt("userId");

List bankLog =null;
UserBean user=null;
String tip=null;
PagingBean paging = new PagingBean(action,1000,numberPage,"p");
if(userId!=0)
{
	
	user=UserInfoUtil.getUser(userId);

		bankLog=userService.getMoneyLogList("from_id = "+ userId + " order by id desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
		if(bankLog==null) {
	    tip="此用户没有赠送记录!";
	    }
}
%>
<html>
<div align="center">
<H2 align="center">用户赠送后台</H2>
<%if(user!=null){%>
<p><%=StringUtil.toWml(user.getNickName())%>(<%=user.getId()%>)</p>
<%}%>
<table border="1" align="center" >
<tr>
	<td>
		对方ID
	</td>
	<td align=center>
		赠送
	</td>
	<td>
		时间
	</td>
</tr>
<%
if(bankLog!=null){
for(int i = 0; i < bankLog.size(); i ++){
%><tr><%
	UserMoneyLogBean moneyLog=(UserMoneyLogBean)bankLog.get(i);
%>
<td><a href="queryUserInfo.jsp?id=<%=moneyLog.getToId()%>"><%=moneyLog.getToId()%></a></td>
<td align=right><%=moneyLog.getMoney()/10000%>万</td>
<td><%=moneyLog.getCreateDatetime()%></td>
</tr>
<%}%>
<%}%>
</table>
<%=paging.shuzifenye("moneyLog2.jsp?userId="+userId,true,"|",response)%><br/>
<font color="red"><%=tip!=null?tip:""%></font><br/>
<a href="moneyLog3.jsp?userId=<%=userId%>">根据赠送对象查询</a><br/>
根据ID查找赠送记录<br/>
<form name="form1" method="post" action="moneyLog2.jsp">
用户ID：<input type="text" name="userId"/><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
</html>