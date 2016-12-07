<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
野外的生物有很多种类,野兽,妖,鬼和人类等等.<br/>
不同等级的生物会掉落不同等级的奖励物品.<br/>
有些生物十分稀少,会固定掉落装备.精英的血和攻击力都远高于其他生物,他们掉落物品和钱币的几率和数量也有所提高.<br/>
杀死小动物不会得到任何奖励.<br/>
对于Boss级别的生物,控制性技能(例如猛击)将不会产生任何效果.<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>