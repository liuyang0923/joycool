<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.fish.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
FishAction action = new FishAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(ForbidUtil.isForbid("game",loginUser.getId())){
	response.sendRedirect("/enter/not.jsp");
	return;
}

action.doFish();

AreaBean area = action.getFishUser().getArea();
if(area == null){
action.sendRedirect("index.jsp", response);
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FishAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("catch")){%>
<%PullBean pull = (PullBean)request.getAttribute("pull");%>
有鱼出现了!<br/>
<%=pull.getPattern()%><br/>
<%=loginUser.showImg("img/"+pull.getImage())%>
快决定怎样拉杆：<br/>
<%
int part = (pull.getId() - 1) / 4;	// 只显示4个一组的同组
Iterator iter = action.getWorld().pullList.iterator();
while(iter.hasNext()){
	pull = (PullBean)iter.next();
	if((pull.getId() - 1) / 4 == part) {
%>
<a href="getFish.jsp?pullId=<%=pull.getId()%>"><%=pull.getPullMode()%></a><br/>
<%}}%>
<%}else{%>
5分钟过去了，鱼钩没有任何动静，再试一次吧<br/>
<a href="waitFish.jsp">继续放鱼竿</a><br/>
<%}%>
<%@include file="info.jsp"%>
<a href="areaList.jsp">换个地方钓</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>