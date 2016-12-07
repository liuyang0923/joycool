<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IJobService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.action.job.AngerAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser= (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
				StockAction action = new StockAction(request);
action.jump(request);
int id =StringUtil.toInt((String)request.getAttribute("id"));
String tip=(String)request.getAttribute("tip");
String result=(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(null!=result){
%>
<card title="个股信息" ontimer="<%=response.encodeURL("/stock/index.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%>3秒跳转.<br/>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
 else if(null!=tip){
%>
<card title="个股信息" ontimer="<%=response.encodeURL("/stock/stockBusiness.jsp?id="+id)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%>3秒跳转.<br/>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
	//response.sendRedirect(("/stock/stockBusiness.jsp?id="+id));
    BaseAction.sendRedirect("/stock/stockBusiness.jsp?id="+id, response);
    return;
}%>
</wml>