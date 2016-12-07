<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	
	gardenAction.doGrass();
	int uid = gardenAction.getParameterInt("uid");
	int p1 = gardenAction.getParameterInt("p1");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>" ontimer="<%=response.encodeURL("/garden/garden.jsp?uid="+uid+"&amp;p="+p1)%>">
<timer value="30"/>
<p>
<%=BaseAction.getTop(request, response)%>
<%if(gardenAction.isResult("no")) {%>
一天最多只能放草<%=GardenAction.GRASS_COUNT %><br/>
<%} else if(gardenAction.isResult("full")) {%>
该地不能再放草了<br/>
<%} else if(gardenAction.isResult("self")) {%>
不能给自己的地放草<br/>
<%} else if(gardenAction.isResult("success")) {%>
放草成功<!-- 经验增加<%=GardenAction.DO_EXP %>点<%=request.getAttribute("level")!=null?",恭喜你升级到"+request.getAttribute("level")+"级":"" %> --><br/>
<%} else if(gardenAction.isResult("nofriend")) {%>
该用户不是你好友<br/>
<%} else if(gardenAction.isResult("grown")){%>
该阶段不能放草了<br/>
<%} %>
<a href="garden.jsp?uid=<%=uid %>&amp;p=<%=gardenAction.getParameterInt("p1") %>">返回<%=UserInfoUtil.getUser(uid).getNickNameWml() %>的农场</a><br/>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>