<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 10;%>
<%YardAction action=new YardAction(request);
int tmp = 0;
String tip = "";
boolean isThisFamily = false;
int id = action.getFmId();
int fmId = action.getFmId();
if(fmId > 0){
	if (id == fmId)
		isThisFamily = true;
}else{
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard = action.getYardByID(id);
YardCookBean cook = null;
YardRecipeProtoBean recipe = null;
int cooking = action.getParameterInt("c");
int recipeId = action.getParameterInt("rid");
if (cooking == 1){
	// 开始烹饪
	if (!isThisFamily){
		tip = "只能为自己的家族烹饪哦.";
	} else {
		if (!yard.isRecipeSet(recipeId)){
			tip = "您的家族还没有这本食谱.";
		} else {
			// 检查所需要物品是否齐全
			recipe = YardAction.getRecipeProto(recipeId);
			List cookingList = YardAction.yardService.getYardCookBeanList(" fmid=" + fmId);
			if (recipe != null && recipe.getType() == 0){		// 这里只能做普通的菜谱
				recipe.makeMaterialList();
				if (!yard.checkMaterial(recipe.getMaterialList())){
					tip = "所需的材料不够.";
				} else if(cookingList.size()>=yard.getKitchenCount()) {
					tip = "已达到最大烹饪数量.";
				} else {
					// 开始烹饪
					action.cooking(yard,recipe);
					tip = "[" + StringUtil.toWml(recipe.getName()) + "]加入烹饪列表!";
				}
			} else {
				tip = "无法找到菜谱.";
			}
		}
	}
}

PagingBean paging = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="厨房"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){

// 食谱列表(***还要判断是否开放厨房,我还没有做***)
%>厨房食谱:<br/><%
List recipeList = new ArrayList(yard.getRecipeSet());
paging = new PagingBean(action, recipeList.size(), COUNT_PRE_PAGE, "p");
if (recipeList != null && recipeList.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		tmp = ((Integer)recipeList.get(i)).intValue();
		recipe = YardAction.getRecipeProto(tmp);
		if (recipe != null){
			if (yard.isRecipeSet(recipe.getId())){
				// 已经购买了该食谱
				%><%=i+1%>.<a href="kitchen2.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>"><%=StringUtil.toWml(recipe.getName())%></a>|<a href="kitchen3.jsp?c=1&amp;rid=<%=recipe.getId()%>&amp;id=<%=id%>">烹饪</a><br/><%
			} else {
				// 没买
				%><%=i+1%>.<%=StringUtil.toWml(recipe.getName())%><br/><%
			}
		}
	}%><%=paging.shuzifenye("kitchen3.jsp?id=" + id + "&amp;t=1",true,"|",response)%><%
}
%><a href="kitchen.jsp">返回厨房</a><br/><%

} else {
%><%=tip%><br/><a href="kitchen.jsp">返回厨房</a><br/><%
}%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>