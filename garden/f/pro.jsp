<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	int f = action.getParameterInt("f");
	int back = action.getParameterInt("b");
	int propertyId = action.getParameterInt("p");
	int type = action.getParameterInt("t");		// t = 1 表示特殊物品
	if (back == 1 || back == 3){
		if ( propertyId <= 0 || propertyId > flowerTypeList.size() - 1){
			action.sendRedirect("mess.jsp?e=3",response);
			return;
		}
	} else if (back == 2){
		if ( propertyId <= 0 || propertyId > FlowerUtil.getEspecialMap().size()){
			action.sendRedirect("mess.jsp?e=3",response);
			return;
		}
	} else {
		if (type == 1){
			if ( propertyId <= 0 || propertyId > FlowerUtil.getEspecialMap().size()){
				action.sendRedirect("mess.jsp?e=3",response);
				return;
			}
		} else {
			if ( propertyId <= 0 || propertyId > flowerTypeList.size() - 1){
				action.sendRedirect("mess.jsp?e=3",response);
				return;
			}
		}
	}%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<% if (back == 1){
	%>【<%=flowerTypeList.get(propertyId) %>】<br/>
	   <img src ="img/<%=propertyId%>.gif" alt="<%=flowerTypeList.get(propertyId)%>" /><br/>
	   参考价格:<%=FlowerUtil.getFlower(propertyId).getPrice()%><br/>
	   <% if ( FlowerUtil.getFlower(propertyId).getType() != 5 ){
				%>需 花 种 :1枚<br/>
				成熟时间:<%=DateUtil.formatTimeInterval3(FlowerUtil.getFlower(propertyId).getTime()) %><br/>
				可 收 获:<%=FlowerUtil.getFlowerName(propertyId) %>一朵<br/><%
				if ( propertyId <= 7){
					if ( f==1 ){	//返回到“包裹”页面时，种子后带有“选择”字样
						%><a href="bag.jsp?f=1">返回</a><br/><%
					} else {
						%><a href="shop.jsp">返回</a>|<a href="bflower.jsp?t=<%=propertyId%>">购买</a><br/><%
					}
			    } else{
			   		%><a href="bag.jsp">返回</a><br/><%
			    }
		}%>
 <%} else if (back == 2){
   		%>【<%=FlowerUtil.getEspecial(propertyId).getName()%>】<br/>
   		   价格:<%=FlowerUtil.getEspecial(propertyId).getMoney()%>;需成就值:<%=FlowerUtil.getEspecial(propertyId).getExp()%><br/>
   		  <%=FlowerUtil.getEspecial(propertyId).getMessage()%><br/> 
   		  <a href="shop2.jsp">返回</a>|<a href="besp.jsp?esp=<%=propertyId%>">购买</a><br/><%
   } else if (back == 3){
		%>【<%=flowerTypeList.get(propertyId) %>】<br/>
		<img src ="img/<%=propertyId%>.gif" alt="<%=flowerTypeList.get(propertyId)%>" /><br/>
		需 花 种 :1枚<br/>
		成熟时间:<%=DateUtil.formatTimeInterval3(FlowerUtil.getFlower(propertyId).getTime()) %><br/>
		可 收 获:<%=FlowerUtil.getFlowerName(propertyId) %>一朵<br/>
   		<a href="lab.jsp">返回</a><br/><%
   } else {
   		if (type == 1){
		%>【<%=FlowerUtil.getEspecial(propertyId).getName()%>】<br/>
   		   价格:<%=FlowerUtil.getEspecial(propertyId).getMoney()%>;需成就值:<%=FlowerUtil.getEspecial(propertyId).getExp()%><br/>
   		  <%=FlowerUtil.getEspecial(propertyId).getMessage()%><br/> 
   		  <a href="bag.jsp">返回</a><br/><%	   		
   		} else {
   			%>【<%=flowerTypeList.get(propertyId) %>】<br/>
   			   <img src ="img/<%=propertyId%>.gif" alt="<%=flowerTypeList.get(propertyId)%>" /><br/>
   			   <% if ( FlowerUtil.getFlower(propertyId).getType() != 5 ){
				%>需 花 种 :1枚<br/>
				   成熟时间:<%=DateUtil.formatTimeInterval3(FlowerUtil.getFlower(propertyId).getTime()) %><br/>
				   可 收 获 :<%=FlowerUtil.getFlowerName(propertyId) %>一朵<br/>
				  <a href="bag.jsp">返回</a><br/><%
				  } else {
				   		%>物品描述:<%=FlowerUtil.describeMap.get(new Integer(propertyId)) %><br/><%
				   		%>物品功能:<%=FlowerUtil.effectMap.get(new Integer(propertyId))%><br/><%
				   		%><a href="store.jsp">返回仓库</a><br/><%
				  }
		}%>
 <%}%><a href="fgarden.jsp">返回我的花园</a><br/><a href="index.jsp">返回花之境首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>