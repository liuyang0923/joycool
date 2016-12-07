<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.job.CardAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.job.CardBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="--------俄罗斯轮盘---------">
<%=BaseAction.getTop(request, response)%>
<br/>
<a href="/job/wheel/StartWheel.jsp" >开始游戏</a><br/>
<a href="/job/wheel/RWheel.jsp" >游戏规则</a><br/>
<br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp" title="返回上一级">返回导航中心</a>
<br/>
<%=BaseAction.getBottom(request, response)%>

</card>

</wml>