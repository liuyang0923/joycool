<%@ page language="java" import="java.util.*,net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
YardPlantBean plant = null;
int id = action.getFmId();
int fmId = action.getFmId();
String tip = "";
int tmp = 0;
int count = 0;
int money = 0;
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
int sfid = action.getParameterInt("sfid");	// 要卖出的车间的ID
if (sfid <= 0){
	tip = "车间编号错误.";
} else {
	plant = yard.getPlant2(sfid);
	if (plant == null || plant.getFmid() != yard.getFmid()){
		tip = "此车间不存在.";
	} else {
		recipe = YardAction.getRecipeProto(plant.getRecipeId());
		if (recipe == null){
			tip = "参数错误.";
		} else {
			count = yard.getPlantCountByType(plant.getRecipeId());
			money = yard.factoryMoney[count - 1] / 2;
			int sell = action.getParameterInt("s");
			if (sell == 1){		// 卖
				if (yard.sellPlant(money,plant)){
					tip = "卖出成功.餐厅现有资金" + YardAction.moneyFormat(yard.getMoney()) + "元.";
				} else {
					tip = "卖出失败.";
				}
			}
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="卖出车间"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>[<%=recipe.getName()%>]车间,价值<%=YardAction.moneyFormat(money)%>元.确定卖出吗?<br/>
<a href="factory8.jsp?id=<%=fmId%>&amp;sfid=<%=sfid%>&amp;s=1">确认卖出</a><br/>
<a href="factory7.jsp?id=<%=fmId%>">再考虑下</a><br/><%
} else {
%><%=tip%><br/><%
}%>
<a href="factory4.jsp?id=<%=id%>">返回加工厂</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>