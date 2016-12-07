<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	DishBean dish = FlowerUtil.getUserDish(action.getLoginUserId());
	List compList = action.dishToList(dish);
	String flowers = "";
	if ( compList.size() !=0 ){
		for (int i=0;i<compList.size();i++){
			flowers += flowerTypeList.get(new Integer(compList.get(i).toString()).intValue()) + ",";
		}
		flowers = flowers.substring(0,flowers.length()-1);
	}%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<% if (compList.size() != 0){
		%>你确定要合成“<%=flowers%>”吗?一旦合成失败,这些花将马上消失!<br/><a href="lab.jsp">再考虑一下</a><br/><a href="comp.jsp">确定合成</a><br/><%
   } else {
   		out.clearBuffer();
   		action.sendRedirect("mess.jsp?e=10",response);
   		return;
   }%>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>