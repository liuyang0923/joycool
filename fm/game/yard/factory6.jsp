<%@ page language="java" import="java.util.*,net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
int id = action.getFmId();
int fmId = action.getFmId();
String tip = "";
int sure = 0;
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
int recipeId = action.getParameterInt("rid");
recipe = YardAction.getRecipeProto(recipeId);
// List list2 = new ArrayList(yard.getWorksRecipeSet()); <-这是啥？我当初为什么要这么写来着？？？
List list2 = yard.getPlantList();	// <-这个是我现在拥有的工厂List
int money = 0;
int countNow = 0;
if (recipe == null){
	tip = "这个菜谱不存在哦.";
} else if (list2.size() > yard.getFactoryRankCounts(yard.getFactoryRank())){
	tip = "您的车间数量已达到上限,请升级后购买.";
} else {
	countNow = yard.getPlantCountByType(recipe.getId());	// 目前有几个同样的车间了
	money = yard.getFactoryMoney(countNow);
	sure = action.getParameterInt("s");
	if (sure != 0){
		// 买喽买喽
		if (yard.getFactoryMoneyCount() > countNow + 1 && yard.buyPlant(recipe.getId(),money)){
			tip = "购买成功~!";
		} else {
			tip = "购买失败~!";
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买车间"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (sure == 0){
%>此车间将只能用来生产[<%=recipe.getName()%>],您已经有此车间<%=countNow%>个.<br/>
<%if (yard.getFactoryMoneyCount() > countNow + 1){
%>购买第<%=countNow+1%>个需要花费:<%=action.moneyFormat(money)%>元.<br/>
<a href="factory6.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>&amp;s=1">确认购买</a><br/>
<a href="factory5.jsp?id=<%=id%>">再考虑下</a><br/><%
} else {
%>同类车间已达上限.<br/><%
}
}
} else {
%><%=tip%><br/><a href="factory5.jsp?id=<%=id%>">返回购买车间</a><br/><%
}%>
<a href="factory4.jsp?id=<%=fmId%>">返回加工厂</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>