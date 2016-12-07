<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.bank.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.bank.*"%><%
response.setHeader("Cache-Control", "no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
BankAction bank=new BankAction(request);
bank.accountQuery(request);
String userStore=(String)request.getAttribute("userStore");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector loadList=(Vector)request.getAttribute("loadList");
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="业务查询">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：您好!您现在乐币<%=us.getGamePoint()%>;<br/>存款<%=userStore%>;<br/>
贷款情况为：
<%
if(loadList.size()<1)
{
%>您现在尚无贷款!<br/>
<%}else{%>
(请及时归还贷款!)<br/>&nbsp;(款额)      (倒计时) <br/>
<%
for(int i=0;i<loadList.size();i++)
{
	LoadBean load=(LoadBean)loadList.get(i);
%>
<%=load.getMoney()%>   <%=bank.getLeaveTime(load.getCurrentTime(),load.getExpireTime())+" "%> <br/>
<%}%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"accountQuery.jsp",false," ",response)%><br/>
<%}%>
请选择以下服务：<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>
