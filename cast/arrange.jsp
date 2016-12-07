<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	CastleAction action = new CastleAction(request);
	
	//int id = action.getParameterIntS("id");
	//if(id <= 0 || id > CastleAction.affect.length){
	//	response.sendRedirect("shop.jsp");
	//	return;
	//}
	//action.shopBuy();
	action.arrange();
	CastleUserBean user = CastleUtil.getCastleUser(action.getLoginUser().getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p><%@include file="top.jsp"%>
<%if(request.getAttribute("msg") != null) {%><%=request.getAttribute("msg")%><br/><%} %>
拥有<%=user.getGold()%>个金币<br/>
价格:<%=CastleAction.shopPrice[9]%>金币<br/>
<%=CastleAction.affect[9]%><br/>
<%if(action.hasParam("a")) {
	int[] resG = (int[])action.getAttribute2(CastleAction.RES_SESSION_G);
	if(resG == null) resG = new int[5];
	int[] resP = (int[])action.getAttribute2(CastleAction.RES_SESSION_P);
	if(resP == null) resP = new int[5];
%>
总提取:木<%=resG[1]%>|石<%=resG[2]%>|铁<%=resG[3]%>|粮<%=resG[4]%><br/>
已分配:木<%=resP[1]%>|石<%=resP[2]%>|铁<%=resP[3]%>|粮<%=resP[4]%><br/>
<%if(resG[1]+resG[2]+resG[3]+resG[4]-resP[1]-resP[2]-resP[3]-resP[4]==0) {%>
<a href="arrange.jsp?a=s">分配完成</a>|<a href="arrange.jsp">取消</a><br/>
<%} else {%>
可分配:<%=resG[1]+resG[2]+resG[3]+resG[4]-resP[1]-resP[2]-resP[3]-resP[4]%>|<a href="arrange.jsp">取消</a><br/>
<%}	} else {%>
<%} %>
<select name="r1" value="1">
<option value="1">木</option>
<option value="2">石</option>
<option value="3">铁</option> 
<option value="4">粮</option>
</select>
<input name="g" format="*N"/>
<anchor>提取<go href="arrange.jsp?a=g" method="post">
<postfield name="r1" value="$r1"/>
<postfield name="g" value="$g"/>
</go></anchor><br/>
<select name="r2" value="1">
<option value="1">木</option>
<option value="2">石</option>
<option value="3">铁</option> 
<option value="4">粮</option>
</select>
<input name="p" format="*N"/>
<anchor>分配<go href="arrange.jsp?a=p" method="post">
<postfield name="r2" value="$r2"/>
<postfield name="p" value="$p"/>
</go></anchor><br/>
<a href="shop.jsp">返回商城</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>