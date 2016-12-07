<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	List drugList = action.getDrug();				//取得库存药品
	int flowerId = 0;
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
我的仓库<br/>
<% FlowerStoreBean fsb =  null;
   if (drugList.size() != 0){
		for (int i = 0; i < drugList.size();i++){
			fsb = (FlowerStoreBean)drugList.get(i);
			flowerId = fsb.getFlowerId();
			%><a href="pro.jsp?p=<%=flowerId %>"><%=flowerTypeList.get(flowerId)%></a>&nbsp; <%=fsb.getCount()%>个<br/><%
		}
   } else {
   		%>我的仓库中没有任何物品.<br/><%
   }%>
花市|<a href="ghouse.jsp">花房</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a>|仓库<br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>