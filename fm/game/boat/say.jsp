<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action = new BoatAction(request,response);
String[] url = {"","game.jsp","chatmore.jsp"};
Integer imid = (Integer) session.getAttribute("mid");
if(imid == null){
	response.sendRedirect("game.jsp");
	return;
}
MatchBean matchBean = (MatchBean) BoatAction.matchCache.get(imid);
if(matchBean == null) {
	response.sendRedirect("game.jsp");
	return;
}
int from = action.getParameterInt("f");
if(from != 1 && from != 2)
	from = 1;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="发言"><p align="left"><%=BaseAction.getTop(request, response)%>
<input name="gchat"  maxlength="100"/><br/>发言至<br/><anchor>家族<go href="<%=url[from]%>?ct=f" method="post"><postfield name="content" value="$gchat"/></go></anchor>|<anchor>所有<go href="<%=url[from]%>?ct=a" method="post"><postfield name="content" value="$gchat"/></go></anchor><br/>
<a href="<%=url[from]%>">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>