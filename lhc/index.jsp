<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.lhc.LhcAction" %><%@ page import="net.joycool.wap.action.lhc.LhcResultBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
LhcAction action = new LhcAction(request);
action.index();
LhcResultBean lhcResult =(LhcResultBean)request.getAttribute("lhcResult");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(loginUser==null){%><img src="/img/lhc.gif" alt="logo"/><br/><%}else{%><%=loginUser.showImg("/img/lhc.gif")%><%}%>
<a href="/lhc/help.jsp">六合彩说明</a><br/>
六合彩开奖时间为:每日20点开奖,当日19点停止购买,兑奖时间为开奖后的10日内,过期作废<br/>
<%if(lhcResult==null){%>
第一期等待开奖中<br/>
<%}else{%>
=============<br/>
第<%=lhcResult.getId()%>期中奖号码：<br/>
<%=lhcResult.getNum1()%>
<%=lhcResult.getNum2()%>
<%=lhcResult.getNum3()%>
<%=lhcResult.getNum4()%>
<%=lhcResult.getNum5()%>
<%=lhcResult.getNum6()%>
(<%=lhcResult.getNum7()%>)<br/>
中奖名称：<br/>
<%=action.countName(lhcResult.getId())%><br/>
=============<br/>
<%}%>
<%--<a href="/lhc/buy.jsp?type=8">平码</a>|--%><a href="/lhc/buy.jsp?type=9">特码</a>|<a href="/lhc/buy.jsp?type=1">大小</a>|<a href="/lhc/buy.jsp?type=2">生肖</a>|<a href="/lhc/buy.jsp?type=4">前后</a><br/>
<a href="/lhc/buy.jsp?type=5">波色</a>|<a href="/lhc/buy.jsp?type=6">五行</a>|<a href="/lhc/buy.jsp?type=7">单双</a>|<a href="/lhc/buy.jsp?type=3">家禽野兽</a><br/><br/>
<%--<%if(lhcResult!=null){%>
<a href="/lhc/exchange.jsp?lhcId=<%=lhcResult.getId()%>">兑奖</a><br/>
<%}%>--%>
<a href="/lhc/buyList.jsp">兑奖记录</a><br/>
<a href="/lhc/lhcHistory.jsp">每期开奖结果</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>