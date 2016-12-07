<%@ page language="java" import="net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
int id = action.getFmId();
int fmId = action.getFmId();
String tip = "";
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
int recipId = action.getParameterInt("rid");
if (id <= 0){
	tip = "要查看的食谱不存在.";
} else {
	recipe = YardAction.getRecipeProto(recipId);
	if (recipe == null){
		tip = "要查看的食谱不存在.";
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="厨房"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=StringUtil.toWml(recipe.getName())%><br/>
原料:<%=YardAction.getItemListString(recipe.getMaterialList())%><br/>
完美烹饪:约<%=action.getCookingTimeStr(Long.valueOf(recipe.getTime()).longValue())%><br/><br/>
做法:<%=StringUtil.toWml(recipe.getDescribe())%><br/>
<%
} else {
%><%=tip%><br/><%
}%>
<a href="kitchen3.jsp">返回食谱列表</a><br/>
<a href="kitchen.jsp">返回厨房</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>