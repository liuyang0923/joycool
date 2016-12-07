<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.drops();
int pick = action.getParameterIntS("o");
if(pick>=0){
	Tiny2Action action2 = new Tiny2Action(request, response);
	if(action2.checkGame(10)) return;
	if(action2.getGame() == null){
		if(net.joycool.wap.util.RandomUtil.nextInt(20) == 0){
			action2.startGame(games[0], 10);
			return;
		}
	}
}
MapNodeBean node = action.getUserNode();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%if(node.getDropCount()>0){%>
<a href="drops.jsp">返回</a><br/>
<%}%>
<%}else{
List dropList = node.getDrops();
if(dropList!=null&&dropList.size()>0){
Iterator iter = dropList.iterator();
int i = 0;
while(iter.hasNext()){
	FarmDropBean drop = (FarmDropBean)iter.next();
%><%=FarmWorld.dropString(drop.getData())%>-
<%if(drop.isProtected(action.getUser().getUserId())){%>
(<%=FarmWorld.getUserName(drop.getUserId())%>)
<%}else{%>
<a href="drops.jsp?o=<%=i%>">拣</a>
<%}%>
<br/>
<%i++;
}} else {%>
没有发现任何东西!<br/>
<%}%>
<%}%>
<a href="cb/cb.jsp">休息/战斗</a>|<a href="map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>