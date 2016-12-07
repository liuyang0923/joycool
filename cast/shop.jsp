<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	CastleAction action = new CastleAction(request);
	CastleUserBean user = CastleUtil.getCastleUser(action.getLoginUser().getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p>
拥有<%=user.getGold()%>个金币<br/>
==选择需要的商品==<br/>
<a href="buy.jsp?id=1">城堡SP帐号</a>(7天)15金币<br/>
<a href="buy.jsp?id=2">木头产量+25%</a>(7天)5金币<br/>
<a href="buy.jsp?id=3">石头产量+25%</a>(7天)5金币<br/>
<a href="buy.jsp?id=4">铁块产量+25%</a>(7天)5金币<br/>
<a href="buy.jsp?id=5">粮食产量+25%</a>(7天)5金币<br/>
<a href="buy.jsp?id=6">士兵攻击力+10%</a>(7天)3金币<br/>
<a href="buy.jsp?id=7">士兵防御力+10%</a>(7天)3金币<br/>
<a href="buy.jsp?id=8">立即完成建造任务</a>2金币<br/>
<a href="arrange.jsp">随意调配四种资源</a>3金币<br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>