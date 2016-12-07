<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int a = gardenAction.getParameterInt("a");
	GardenUserBean bean = GardenUtil.getUserBean(gardenAction.getLoginUser().getId());
	if(a != 1) {
		gardenAction.expand();
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(a == 1) {
	int index = bean.getFieldCount() - GardenUtil.defaultCount;
%>
扩充第<%=bean.getFieldCount()+1 %>块田需要现金<%=GardenUtil.condition[index][0] %>,等级<%=GardenUtil.condition[index][1] %>级,您确定要扩充吗？<br/>
<a href="expand.jsp">确定扩充</a><br/>
<%} else { %>
<%if(gardenAction.isResult("success")) {%>
成功扩充第<%=bean.getFieldCount()+1 %>块田,经验增加<%=GardenAction.EXPAND_EXP %>点<%=request.getAttribute("level")!=null?",恭喜你升级到"+request.getAttribute("level")+"级":"" %><br/>
<%} else if(gardenAction.isResult("null")) {%>
不能扩充<br/><%} else if(gardenAction.isResult("no")) {%>
扩充失败,扩充第<%=bean.getFieldCount()+1 %>块田需要现金<%=GardenUtil.condition[((Integer)(request.getAttribute("index"))).intValue() - 1][0] %>,等级<%=GardenUtil.condition[((Integer)(request.getAttribute("index"))).intValue() - 1][1] %>级<br/>
<%} else if(gardenAction.isResult("limit")){%>
没有足够多的土地供你扩充<br/>
<%}} %>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>