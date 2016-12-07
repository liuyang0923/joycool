<%@ page language="java" import="java.util.*,net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
int id = action.getFmId();
int fmId = action.getFmId();
String tip = "";
int tmp = 0;
int count = 1;
boolean isHave = false;
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
List list = null;
YardBean yard = action.getYardByID(id);
if (!action.getFmUser().isflagYard()){
	tip = "只有大厨才可以加工.";
} else {
	list = YardAction.getWorksRecipeProtoList();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="加工厂"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		recipe = (YardRecipeProtoBean)list.get(i);
		if (recipe != null){
			if (yard.isWorksRecipeSet(recipe.getId())){
				%>[<%=recipe.getName()%>]已购买<br/><%
			} else {
				%><a href="factory3.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>">[<%=recipe.getName()%>]</a>.<a href="factory3.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>">购买</a><br/><%			
			}
		}
	}
}
} else {
%><%=tip%><br/><%
}%>
<a href="factory.jsp?id=<%=id%>">返回加工厂</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>