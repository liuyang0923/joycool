<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.user.RankAction"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction" %><%@ page import="net.joycool.wap.test.TestAction" %><%@ include file="../testAction.jsp" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="调查问卷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
为了更好的为乐酷用户提供wap购物服务,特设立此次调查,以便准备更丰富的商品供您选择。只要您认真参与调查,调查结束时您即可获赠20元代金券在乐酷购物使用。<br/>
<a href="http://wap.joycool.net/test/book/page1.jsp"><%= StringUtil.toWml("立即参与调查>>") %></a><br/>
<a href="http://wap.joycool.net"><%= StringUtil.toWml("返回乐酷首页<<") %></a><br/>
</p>
</card>
</wml>