<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.stock2.*" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.action.stock2.*" %><%@ page import="java.util.List" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.topall();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
StockAccountBean stockUser = (StockAccountBean)request.getAttribute("stockUser");
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
List userList = (List)request.getAttribute("userList");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
乐酷股市排行榜<br/>
--------------<br/>
<%if(stockUser!=null){%>
你的总资产：<%=stockUser.getAsset()%>乐币，排名第<%=request.getAttribute("count")%>。<br/>
<%}%>
===TOP100===<br/>
<%
UserBean user = null;
for(int i =0 ;i<userList.size();i++){
	stockUser = (StockAccountBean)userList.get(i);
	user=UserInfoUtil.getUser(stockUser.getUserId());
	if(user==null)continue;%>
	<%=i+1+pagingBean.getCurrentPageIndex()*10%>.
	<%if(loginUser.getId()==user.getId()){%>你自己<%}else{%>
	<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>
	<%}%><%=StringUtil.bigNumberFormat(stockUser.getAsset())%>乐币<br/>
	
<%}%>
<%=PageUtil.shuzifenye(pagingBean, "topall.jsp", false, "|", response)%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>