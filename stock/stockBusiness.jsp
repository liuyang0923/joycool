<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page errorPage=""%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.stock.StockBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.stock.StockHolderBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser= (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
StockAction action = new StockAction(request);
action.stockBusiness(request);
int id =StringUtil.toInt((String)request.getAttribute("id"));
String tip=(String)request.getAttribute("tip");
String result=(String)request.getAttribute("result");
StockBean bean=(StockBean)request.getAttribute("stock");
StockHolderBean holder=(StockHolderBean)request.getAttribute("holder");
session.setAttribute("stockbusiness", "true");
int businessCount=StringUtil.toInt((String)session.getAttribute("businessCount"));
if(businessCount<0)
businessCount=0;

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个股信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(bean!=null){%>
名称：<%=bean.getName()%><br/>
当前价格：<%=bean.getPrice()%><br/>
持有：<%if(holder!=null){%><%=holder.getTotalCount()%><%}else{%>0<%}%>股<br/>
资金：<%=status.getGamePoint()%>乐币<br/>
交易信息<br/>
数量<input name="number"  maxlength="8" value="" title="数量"/>股<br/>
<anchor title="确定">买入
    <go href="/stock/jump.jsp" method="post">
    <postfield name="number" value="$number"/>
    <postfield name="buy" value="1"/>
    <postfield name="id" value="<%=id%>"/>
    <postfield name="businessCount" value="<%=businessCount%>"/>
    </go>
</anchor>|<anchor title="确定">抛出
    <go href="/stock/jump.jsp" method="post">
    <postfield name="number" value="$number"/>
        <postfield name="sale" value="1"/>
    <postfield name="id" value="<%=id%>"/>
    <postfield name="businessCount" value="<%=businessCount%>"/>
    </go>
    </anchor>
<br/>
<br/>
<%}%>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

</wml>