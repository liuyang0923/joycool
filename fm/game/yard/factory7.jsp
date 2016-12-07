<%@ page language="java" import="java.util.*,net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
YardPlantBean plant = null;
int id = action.getFmId();
int fmId = action.getFmId();
String tip = "";
int tmp = 0;
boolean isHave = false;
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);
List list = yard.getPlantList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="卖出车间"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (list != null || list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
			plant = (YardPlantBean)list.get(i);
	if (plant != null){
		recipe = YardAction.getRecipeProto(plant.getRecipeId());
		if (recipe != null){
			%><%=i+1%>.[<%=recipe.getName()%>]车间.<a href="factory8.jsp?id=<%=fmId%>&amp;sfid=<%=plant.getId()%>">卖出</a><br/><%
		}
	}
	}
}
} else {
%><%=tip%><br/><%
}%>
<a href="factory4.jsp?id=<%=id%>">返回加工厂</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>