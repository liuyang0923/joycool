<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
String tip = "";
String tip2 = "";
int buy = 0;
if (buy < 0 || buy > 1)
	buy = 0;
int id = action.getFmId();
int fmId = action.getFmId();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
int tmp = 0;
int buyId = action.getParameterInt("bid");
YardItemProtoBean item = null;
YardRecipeProtoBean recipe = null;
YardBean yard=action.getYardByID(id);
if (buy == 0){
// 买调味料
	item = YardAction.getItmeProto(buyId);
	if (item == null){
		tip = "您要购买的调味料不存在.";
	}
} else {

}
int count = action.getParameterInt("c");
if (count > 0 && "".equals(tip)){
// 购买
	if (buy == 0){
		int count2 = count * item.getBuyCount();
		int result = yard.buy(buyId,count * item.getPrice(),count2,0);
		if (result == 0){
			tip = "您购买了" + count + "份[" + item.getName() + "],花费" + YardAction.moneyFormat(count * item.getPrice()) + "元!";
		} else if (result == -1){
			tip2 = "参数错误.<br/>";
		} else if (result == -2){
			tip2 = "你没有足够资金!<br/>";
		}
	} else {
		count = 1;	// 食谱只买一份
		int result = yard.buy(buyId,recipe.getPrice(),count,1);
		if (result == 0){
			tip = "购买成功!食谱列表增加[" + recipe.getName() + "]!";
		} else if (result == -1){
			tip2 = "参数错误.<br/>";
		} else if (result == -2){
			tip2 = "你没有足够资金!<br/>";
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="杂货铺"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=tip2%>餐厅现有资金<%=YardAction.moneyFormat(yard.getMoney())%>元<br/><%
if (buy == 0){
// 买调味料
%>输入你要购买[<%=item.getName()%>(<%=item.getBuyCount()%>)]的数量:<br/>
<input name="c" format="*N"/><br/>
<anchor>
	确定
	<go href="shop2.jsp?id=<%=id%>&amp;b=<%=buy%>&amp;bid=<%=buyId%>" method="post">
		<postfield name="c" value="$c" />
	</go>
</anchor><br/>
<%
} else {
// 买菜谱
%><%=recipe.getName()%><a href="shop2.jsp?id=<%=id%>&amp;b=<%=buy%>&amp;bid=<%=buyId%>&amp;c=1">购买食谱</a><br/>
菜谱售价:<%=YardAction.moneyFormat(recipe.getPrice())%>元<br/>
原料:<%=recipe.getStuff()%><br/>
完美烹饪:<%=action.getCookingTimeStr(Long.valueOf(recipe.getTime()).longValue())%><br/>
做法:<%=StringUtil.toWml(recipe.getDescribe())%><br/>
<%
}
} else {
%><%=tip%><br/><%
}%>
<a href="shop.jsp">返回杂货铺</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>