<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);
action.change();
UserBean loginUser = action.getLoginUser();
int count1 = UserBagCacheUtil.getUserBagItemCount(68,loginUser.getId());
int count2 = UserBagCacheUtil.getUserBagItemCount(73,loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(!action.isResult("tip")){ %>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(11,request,response)%>
行囊现有大富翁券<%=count1%>张，大富翁奖牌<%=count2%>个<br/>
请选择兑换：(所需材料)<br/>
<a href="change.jsp?o=1">10亿乐币</a>(大富翁券x4)<br/>
<a href="change.jsp?o=8">高级荣誉卡</a>(大富翁券x8)<br/>
<a href="change.jsp?o=2">30亿乐币</a>(大富翁券x10)<br/>
<a href="change.jsp?o=3">大富翁奖牌</a>(大富翁券x10)<br/>
<a href="change.jsp?o=5">66亿乐币</a>(大富翁奖牌x2)<br/>
<a href="change.jsp?o=6">180亿乐币</a>(大富翁奖牌x5)<br/>
<a href="change.jsp?o=7">400亿乐币</a>(大富翁奖牌x10)<br/>
<a href="change.jsp?o=4">大富翁卡</a>(大富翁奖牌x16)<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="change.jsp">确定</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>