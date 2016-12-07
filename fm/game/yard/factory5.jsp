<%@ page language="java" import="java.util.*,net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
int id = action.getFmId();
int fmId = action.getFmId();
String tip = "";
int tmp = 0;
List list = null;
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
// list = new ArrayList(yard.getWorksRecipeSet());
list = action.getWorksRecipeProtoList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买车间"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>请选择专供此车间生产的资料:<br/><%
if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		// tmp = ((Integer)list.get(i)).intValue();
		// recipe = YardAction.getRecipeProto(tmp);
		recipe = (YardRecipeProtoBean)list.get(i);
		if (recipe != null && recipe.getRank() <= yard.getRank()){
			isHave = true;
			%><%=i+1%>.[<a href="factory.jsp?id=<%=fmId%>&amp;rid=<%=recipe.getId()%>"><%=recipe.getName()%></a>]车间|<a href="factory6.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>">购买</a><br/><%
		}
	}
} else {
%>(暂无)<br/><%
}
} else {
%><%=tip%><br/><%
}%>
<a href="factory4.jsp?id=<%=id%>">返回加工厂</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>