<%@include file="/farm/antiAuto.jsp"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.fish.*" %><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
%><%
response.setHeader("Cache-Control","no-cache");

Tiny2Action action2 = new Tiny2Action(request, response);
if(action2.checkGame()) return;

if(action2.getGame() == null){
	int[] count = FishAction.countMap.getCount(action2.getLoginUser().getId());

	action2.startGame(games[0]);
	count[0]++;
	return;
}

FishAction action = new FishAction(request);
action.getFish();
AreaBean area = action.getFishUser().getArea();
if(area == null){
action.sendRedirect("index.jsp", response);
return;
}

UserBean loginUser = action.getLoginUser();
//if(!action.isCooldown("tong",3000)){
//action.sendRedirect("fish.jsp", response);
//return;
//}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FishAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="waitFish.jsp">继续放鱼竿</a><br/>
<%if(action.isResult("get")){%>
	<%FishBean fish = (FishBean)request.getAttribute("fish");%>
	<%=action.getTip()%><br/>
	<%if(fish != null){%>
<%if(loginUser.getUserSetting()==null||!loginUser.getUserSetting().isFlagHideLogo()){%><img src="img/<%=fish.getImage()%>" alt=""></img><br/><%}%>
	<a href="fishInfo.jsp?id=<%=fish.getId()%>">查看详细信息</a><br/>
	<%}else{	PullEventBean pullEvent = (PullEventBean)request.getAttribute("pullEvent");
	if(pullEvent != null){
	%>
<%if(loginUser.getUserSetting()==null||!loginUser.getUserSetting().isFlagHideLogo()){%><img src="img/<%=pullEvent.getImage()%>" alt=""/><br/><%}%>
	<%}}%>
<%}else{
out.clearBuffer();
action.sendRedirect("fish.jsp", response);
return;
}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>