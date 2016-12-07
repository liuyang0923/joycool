<%@ page language="java" import="java.util.*,net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
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
int buy = action.getParameterInt("b");
if (!action.getFmUser().isflagYard()){
	tip = "只有大厨才可以加工.";
} else {
	int recipeId = action.getParameterInt("rid");
	recipe = YardAction.getRecipeProto(recipeId);
	if (recipe == null || recipe.getType() != 5){
		tip = "您选择的研发资料不存在.";
	} else {
		if (buy == 1){
//			int count = action.getParameterInt("c");
//			if (count > 0 && count < 999){
				int result = yard.buy(recipe.getId(),recipe.getPrice(),1,1);
				if (result == 0){
					tip = "购买成功!研发列表增加[" + recipe.getName() + "]!";
				} else if (result == -1){
					tip = "参数错误.<br/>";
				} else if (result == -2){
					tip = "你没有足够资金!<br/>";
				}
//			}
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="加工厂"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>餐厅现有资金<%=YardAction.moneyFormat(yard.getMoney())%>元<br/><%
if (buy == 0){
// 显示介绍
%>资料名:<%=recipe.getName()%><%if(!yard.isRecipeSet(recipe.getId())){%><a href="factory3.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>&amp;b=1">购买</a><%}%><br/>
资料售价:<%=YardAction.moneyFormat(recipe.getPrice())%>元<br/>
原料:<%=recipe.getStuff()%><br/>
完美生产需要时间:<%=action.getCookingTimeStr(Long.valueOf(recipe.getTime()).longValue())%><br/>
生产方法:<%=StringUtil.toWml(recipe.getDescribe())%><br/>
<%
} else if (buy == 1){
// 购买数量
%>输入你要购买[<%=recipe.getName()%>]的数量:<br/>
<input name="c" format="*N"/><br/>
<anchor>
	确定
	<go href="factory3.jsp?id=<%=id%>&amp;b=2&amp;rid=<%=recipe.getId()%>" method="post">
		<postfield name="c" value="$c" />
	</go>
</anchor><br/>
<%
}
} else {
%><%=tip%><br/><%
}%>
<a href="factory.jsp?id=<%=id%>">返回加工厂</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>