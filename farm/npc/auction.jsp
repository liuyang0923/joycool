<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
FarmUserBean farmUser = action.getUser();
List userBagList = UserBagCacheUtil.getUserBagListCacheById(farmUser.getUserId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
拍卖行还没开张呢，过几天再来吧！<br/>
<a href="npc.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>