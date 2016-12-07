<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	
	gardenAction.debug();
	int uid = gardenAction.getParameterInt("uid");
	int p1 = gardenAction.getParameterInt("p1");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>" ontimer="<%=(uid!=gardenAction.getLoginUser().getId()&&uid!=0)?response.encodeURL("/garden/garden.jsp?uid="+uid+"&amp;p="+p1):response.encodeURL("/garden/myGarden.jsp?p="+p1)%>">
<timer value="30"/>
<p>
<%=BaseAction.getTop(request, response)%>
<%if(gardenAction.isResult("null")) {%>
你不能实现此操作<br/>
<%} else if(gardenAction.isResult("no")) {%>
没有该土地<br/>
<%} else if(gardenAction.isResult("success")) {%>
杀虫成功<%=request.getAttribute("count")==null?"":",经验增加"+GardenAction.DO_EXP*((Integer)request.getAttribute("count")).intValue()+"点" %><%=request.getAttribute("level")!=null?",恭喜你升级到"+request.getAttribute("level")+"级":"" %><br/>
<%} else if(gardenAction.isResult("has")) {%>
做坏事的证据是不能被毁灭的<br/>
<%} else if(gardenAction.isResult("nobug")){%>
该地还没有虫子<br/>
<%} %>
<%if(uid!=gardenAction.getLoginUser().getId()&&uid!=0) {%>
<a href="garden.jsp?uid=<%=uid %>&amp;p=<%=p1 %>">返回<%=UserInfoUtil.getUser(uid).getNickNameWml() %>的农场</a><br/>
<%} %>
<a href="myGarden.jsp?p=<%=p1 %>">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>