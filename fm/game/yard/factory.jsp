<%@ page language="java" import="net.joycool.wap.bean.PagingBean,java.util.*,net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
int id = action.getFmId();
int fmId = action.getFmId();
String tip = "";
int tmp = 0;
List list = null;
PagingBean paging = null;
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
if (!action.getFmUser().isflagYard()){
	tip = "只有大厨才可以加工.";
} else {
	int recipeId = action.getParameterInt("rid");
	if (recipeId > 0){
		recipe = YardAction.getRecipeProto(recipeId);
		if (recipe == null || recipe.getType() != 5){
			tip = "要查看的资料不存在.";
		}
	} else {
		list = new ArrayList(yard.getWorksRecipeSet());
		paging = new PagingBean(action, list.size(), 5, "p");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="加工厂"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (recipe == null){
// 显示列表
%>资料列表:<br/><%
if (list != null && list.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		tmp = ((Integer)list.get(i)).intValue();
		recipe = YardAction.getRecipeProto(tmp);
		if (recipe != null){
			isHave = true;
			%><%=i+1%>.<a href="factory.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>"><%=recipe.getName()%></a>|<a href="factory4.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>&amp;c=1">加工</a><br/><%
		}
	}%><%=paging.shuzifenye("factory.jsp?id=" + fmId + "",true,"|",response)%><%
} else {
%>(暂无)<br/><%
}
} else {
// 详细说明
%><%=StringUtil.toWml(recipe.getName())%><br/>
原料:<%=YardAction.getItemListString(recipe.getMaterialList())%><br/>
所需时间:<%=action.getCookingTimeStr(Long.valueOf(recipe.getTime()).longValue())%><br/><br/>
产品介绍:<%=StringUtil.toWml(recipe.getDescribe())%><br/><%
}
} else {
%><%=tip%><br/><%
}%>
============<br/>
<a href="factory5.jsp?id=<%=id%>">返回车间购买</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>