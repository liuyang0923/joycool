<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%!
static int COUNT_PRE_PAGE = 10;
%><%
YardAction action=new YardAction(request);
String tip = "";
int fmId = action.getFmId();
if(fmId < 0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard = action.getYardByID(fmId);
PagingBean paging = null;
if (!action.getFmUser().isflagYard()){
	tip = "只有大厨才可以配菜.";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="配菜间"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>厨房食谱:<br/><%
List recipeList = new ArrayList(yard.getMiddleRecipeSet());
paging = new PagingBean(action, recipeList.size(), COUNT_PRE_PAGE, "p");
if (recipeList != null && recipeList.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		int tmp = ((Integer)recipeList.get(i)).intValue();
		YardRecipeProtoBean recipe = YardAction.getRecipeProto(tmp);
		if (recipe != null){
			if (yard.isMiddleRecipeSet(recipe.getId())){
				%><%=i+1%>.<%=StringUtil.toWml(recipe.getName())%>|<a href="deploymate.jsp?rid=<%=recipe.getId()%>">配菜</a><br/><%
			}
		}
	}%><%=paging.shuzifenye("deploy.jsp", false, "|", response)%><%
}
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>