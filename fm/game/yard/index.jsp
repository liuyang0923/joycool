<%@ page language="java" import="jc.family.game.*,jc.family.game.yard.*,jc.family.*,java.util.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
int id=action.getParameterInt("id");
int fmId=action.getFmId();
if(id==0){
	if(fmId>0){
		id=fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
boolean my = (fmId == id);
YardBean yard=action.getYardByID(id);
if(yard==null){
	response.sendRedirect("/fm/index.jsp");
	return;
}
FamilyHomeBean fm=action.getFmByID(id);
String tip = "";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族餐厅"><p align="left"><%=BaseAction.getTop(request, response)%>
【<%=yard.getNameWml()%>】<a href="info.jsp?id=<%=id%>">(<%=yard.getRank()+1%>级)</a><br/>
&gt;&gt;<a href="hall.jsp?id=<%=id%>">餐厅大堂</a><br/>
<a href="prod.jsp?id=<%=id%>">生产基地</a>|<%if(my){%><a href="land2.jsp">培育中心</a><%}else{%>培育中心<%}%><br/>
<%if(my){%>
<a href="kitchen.jsp">厨房</a>|<a href="shop.jsp">杂货铺</a>|<a href="mate.jsp">库房</a><br/>
<%if(yard.canEnterKitchen()){%><a href="kitchen4.jsp?id=<%=id%>">中级厨房</a>|<a href="deploy.jsp?id=<%=id%>">配菜间</a><%}else{%>中级厨房|配菜间<%}%><%if(yard.canEnterFactory()){%>|<a href="factory4.jsp?id=<%=id%>">加工厂</a><%}else{%>|加工厂<%}%><br/>
<%}else{%>
厨房|杂货铺|库房<br/>
中级厨房|配菜间|加工厂<br/>
<%}%>
【餐厅动态】<%if(yard.getKitchenLog().size()>3){%><a href="trends.jsp?id=<%=id%>">更多</a><%}%><br/>
<%if(yard.getKitchenLog().size()==0){%>(暂无)<br/><%}else{%><%=yard.getKitchenLog().getLogString(3)%><%}%>
<a href="/Column.do?columnId=12593">游戏说明</a><br/>
&lt;<a href="/fm/myfamily.jsp?id=<%=id%>"><%=fm.getFm_nameWml()%></a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>