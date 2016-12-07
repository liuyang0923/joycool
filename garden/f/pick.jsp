<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int fieldOrder = action.getParameterInt("o");
	int flowerId = action.pickMyFlower(fieldOrder);
%><wml><card title="花之境" ontimer="<%=response.encodeURL("back.jsp?b=1")%>"><timer value="30"/>
<p><%=BaseAction.getTop(request, response)%>
<%if (flowerId != 0){
	%>成功采摘了<%=FlowerUtil.getFlowerName(flowerId)%>,获得<%=FlowerUtil.getFlower(flowerId).getGrowExp()%>点成就值,采摘后的鲜花将放进你的花房中.(5秒后自动<a href="fgarden.jsp">返回</a>)<br/><%
} else {
	%>你还没种花.(5秒后自动<a href="fgarden.jsp">返回</a>)<br/><%
}%><%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>