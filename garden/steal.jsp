<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getParameterInt("uid");
	gardenAction.steal();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(gardenAction.isResult("null")) {%>
果实已收,你出手太慢了<br/>
<%} else if(gardenAction.isResult("self")) {%>
不能摘取自己的果实<br/>
<%} else if(gardenAction.isResult("out")) {%>
行行好吧，我的果实所剩无几了<br/>
<%} else if(gardenAction.isResult("has")) {%>
做人不能太绝，你已经摘取过了<br/>
<%} else if(gardenAction.isResult("success")) {%>
成功摘取<%=request.getAttribute("count")%>个<%=request.getAttribute("name") %><br/>
<%}  else if(gardenAction.isResult("nofriend")) {%>
该用户不是你好友<br/>
<%}%>
<a href="garden.jsp?uid=<%=uid %>&amp;p=<%=gardenAction.getParameterInt("p1") %>">返回<%=UserInfoUtil.getUser(uid).getNickNameWml() %>的农场</a><br/>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>