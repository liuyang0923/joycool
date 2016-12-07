<%@ page language="java" import="net.joycool.wap.bean.PagingBean,java.util.*,net.joycool.wap.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
YardRecipeProtoBean recipe = null;
int id = action.getFmId();
int fmId = action.getFmId();
String tip = "";
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);
YardCookBean cook = null;
YardPlantBean plant = null;
PagingBean paging = null;
int recipeId = action.getParameterInt("rid");
recipe = YardAction.getRecipeProto(recipeId);
int cooking = action.getParameterInt("c");
if (!yard.canEnterFactory()){
	tip = "餐厅等级不够,无法进入.";
} else {
	if (!action.getFmUser().isflagYard()){
		tip = "只有大厨才可以加工.";
	} else {
		if (cooking == 1){
			// 开始烹饪
	//		if (!yard.isWorksRecipeSet(recipeId)){
	//			tip = "您的家族还没有这本资料.";
	//		} else {
				// 检查所需要物品是否齐全
				// List cookingList = YardAction.yardService.getYardCookBeanList(" fmid=" + fmId);
				if (recipe != null && recipe.getType() == 5){		// 只能做加工厂
					// plant = YardAction.yardService.getYardPlantBean("fmid=" + fmId + " and recipe_id=" + recipe.getId() + " and plant_now = 0 limit 1");
					int plantId = action.getParameterInt("pid");
					plant = yard.getPlant2(plantId);
					if (plant == null){
						tip = "当前没有空车间用于生成[" + recipe.getName() + "]";
					} else {
						plant = yard.getPlant2(plant.getId());
						// recipe.makeMaterialList();
						if (!yard.checkMaterial(recipe.getMaterialList())){
							tip = "所需的材料不够.";
						} else if (plant.getPlantNow() > 0){
							tip = "此车间正在生产中.";
						} else {
							// 开始生产
							action.working(yard,recipe,plant);
							// tip = "[" + StringUtil.toWml(recipe.getName()) + "]加入生产列表!";
						}
					}
				} else {
					tip = "无法找到资料.";
				}
	//		}
		}
		int complete = action.getParameterInt("cp");
		if (complete > 0){
			plant = yard.getPlant2(complete);
			if (plant.getPlantNow() == 0){
				tip = "没有生产.";
			} else {
				if (plant != null && plant.isOk(recipe)){
					tip = action.finishWorking(plant,recipe,yard);
				} else {
					tip = "时间还没有到哦,嘿嘿~!";
				}
			}	
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="加工厂"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>车间列表:<a href="factory4.jsp?id=<%=fmId%>">刷新</a><br/><%
List list = yard.getPlantList();
paging = new PagingBean(action, list.size(), 6, "p");
for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
	plant = (YardPlantBean)list.get(i);
	if (plant != null){
		recipe = YardAction.getRecipeProto(plant.getRecipeId());
		if (recipe != null){
			%><%=i+1%>.[<%=recipe.getName()%>]车间.<%
			if (plant.getPlantNow() > 0){
				%>生产中.<%
				if (plant.isOk(recipe)){
					%><a href="factory4.jsp?id=<%=id%>&amp;cp=<%=plant.getId()%>&amp;rid=<%=recipe.getId()%>">完成</a><%
				}
			} else {
				%>闲置中.<a href="factory4.jsp?id=<%=fmId%>&amp;rid=<%=recipe.getId()%>&amp;pid=<%=plant.getId()%>&amp;c=1">生产</a><%
			}%><br/><%
		}
	}
}%><%=paging.shuzifenye("factory4.jsp?id=" + fmId + "",true,"|",response)%>
<a href="factoryrank.jsp">升级加工厂</a><br/>
<a href="factory5.jsp?id=<%=id%>">购买车间</a>|<a href="factory7.jsp?id=<%=fmId%>">卖出车间</a><br/>
<a href="factory4.jsp?id=<%=id%>">返回加工列表</a><br/>
<%
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>