<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static int[] fieldExp = {0,3000,2000,4000,8000};		 //依次为雪山，山崖，平原，河畔的成就值
	static int[] fieldPrice = {0,40000,60000,80000,100000};  //依次为雪山，山崖，平原，河畔的价格
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	boolean result = false;
	FlowerUserBean fub = FlowerUtil.getUserBean(action.getLoginUserId());
	if (fub == null){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	if (fub.getFieldCount() >= FlowerUtil.MAX_FIELD_COUNT){
		action.sendRedirect("mess.jsp?e=14",response);
		return;	
	}
	int buy = action.getParameterInt("buy");
	if (buy == 1){
		//看用户的成就值是否足够
		if ((fub.getExp() - fub.getUsedExp()) < fieldExp[1] || (action.getGold() < fieldPrice[1])){
			action.sendRedirect("mess.jsp?e=8",response);
			return;
		}
		result = action.buyField(fieldPrice[1],fieldExp[1]);
		if (result){
			action.sendRedirect("fgarden.jsp",response);
			return;
		}
	}%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
你现在已有<%=fub.getFieldCount()%>块养殖地,扩充一块新的养殖地要扣除<%=fieldExp[1]%>点成就值和<%=fieldPrice[1]%>金币,你是否确定扩充?<br/>
<a href="addField.jsp?buy=1">确定扩充我的养殖地</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>