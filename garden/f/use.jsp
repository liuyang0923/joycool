<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static final int COUNT_PRE_PAGE=5;%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int espId = action.getParameterInt("id");
	if (espId<=0 || espId > FlowerUtil.getEspecialMap().size()){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	boolean result = action.useEspecialGoods(espId);
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<% if (result){
		%>使用成功.<br/><%
   } else {
   		%><%=request.getAttribute("tip")%><br/><%
   }%>
<a href="lab.jsp">返回实验室</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>