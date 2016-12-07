<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.*,jc.guest.sd.*"%><% 

	response.setHeader("Cache-Control","no-cache");
	ShuDuBean bean = (ShuDuBean) session.getAttribute("shuDuBean");

 %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="数独"><p>
<%=BaseAction.getTop(request, response)%>
在9*9的格中给出一定的已知数字，利用逻辑和推理，在其他的空格上填入1-9的数字。使1-9每个数字在每一行、每一列和每一宫中都只出现一次。<br/>
<%
if (bean != null) {
	%><a href="shudu.jsp">继续游戏</a><br/><%
}
 %>
<a href="lvl.jsp">新的游戏</a><br/>
<a href="rank.jsp">排行榜</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>