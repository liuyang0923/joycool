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
int complete = action.getParameterInt("cp");
if (complete > 0){
	cook = YardAction.yardService.getYardCookBean(" id=" + complete);
	if (cook == null){
		tip = "您没有烹饪该食谱.";
	} else if (cook.getFmid() != fmId){
		tip = "只能烹饪本空话的食谱.";
	} else {
		recipe = YardAction.getRecipeProto(cook.getRecipeid());
		if (recipe == null){
			tip = "该食谱不存在.";
		} else {
			// 动态写到action的方法中了
			tip = action.finishCooking(cook,recipe,yard);
		}
	}
}
PagingBean paging = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="厨房"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
	List cookingList = YardAction.yardService.getYardCookBeanList(" fmid=" + fmId);
// 正在烹饪
%><a href="kitchen3.jsp?id=<%=id%>">&gt;&gt;食谱列表</a><br/>
正在烹饪<a href="kitchen.jsp?id=<%=id%>">刷新</a><br/>
<%paging = new PagingBean(action, cookingList.size(), COUNT_PRE_PAGE, "p");
if (cookingList != null && cookingList.size() > 0){
	long now = System.currentTimeMillis();
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		cook = (YardCookBean)cookingList.get(i);
		if (cook != null){
			recipe = YardAction.getRecipeProto(cook.getRecipeid());
			if (recipe != null && recipe.getType() == 0){
				%><%=i+1 %>.<%=recipe.getName()%>(<%=action.getCookingTimeStr(now - cook.getCreateTime())%>)<a href="kitchen.jsp?id=<%=fmId%>&amp;cp=<%=cook.getId()%>">完成</a><br/><%
			}
		}
	}%><%=paging.shuzifenye("kitchen.jsp?id=" + id,true,"|",response)%><%
} else {
	%>(暂无)<br/><%
}
	%><a href="kitchenrank.jsp">升级厨房</a><br/><%
} else {
%><%=tip%><br/><a href="kitchen.jsp">返回厨房</a><br/><%
}%>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>