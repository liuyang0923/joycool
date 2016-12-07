<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
String tip = null;
int result = 0;
int id = action.getFmId();
FamilyUserBean fmUser = action.getFmUser();
int fmId = fmUser.getFmId();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);

YardItemProtoBean item = null;
int li = action.getParameterIntS("li");
YardLandBean land = yard.getLand(li);
if(land==null||land.getFmid()!=fmId) {
	response.sendRedirect("land2.jsp");
	return;
}
if(!fmUser.isflagYard()){
	tip  = "只有餐厅大厨才有该权限.";
} else {
	if(action.hasParam("c")) {	// 收获
		synchronized (yard) {
			land.setSeedId(0);
			land.setCount(0);
			YardAction.getService().executeUpdate(
					"update fm_yard_fm_land set `count`=0,user_id=0,seed_id=0 where id=" + land.getId());
		}
		response.sendRedirect("land2.jsp");
		return;
	}
}
List list = yard.getLandList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="培育中心"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (tip!=null){	%><%=tip%><br/><%}
	%>【第<%=li+1%>块地】<br/><%
	long now = System.currentTimeMillis();
	item = land.getItemOnLand(); // 取得这块地上正在种着的物品
	if (item != null){
		int timeLeft = land.getTimeLeft(now);
		%><%=item.getNameWml()%><br/>
		数量:<%=land.getCount()%><br/>
		<a href="landdel.jsp?li=<%=li%>&amp;c=1">确认铲除这块地的所有种子</a><br/><br/>
		<%
	} else {
		%>这是一块空地<br/><%
	}

%><a href="land2.jsp?id=<%=id%>">返回培育中心</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>