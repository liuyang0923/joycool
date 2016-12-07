<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request,response);
if(action.hasParam("ok")){

	if(net.joycool.wap.util.ForbidUtil.isForbid("game",action.getLoginUser().getId())){
		action.redirect("/enter/not.jsp");
		return;
	}

	action.enter();
	action.redirect("index.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>

<%}else{%>
欢迎来到桃花源!<br/>
桃花源守则:为了所有玩家的利益和游戏的公平,禁止使用各种外挂.同时,如有有利用外挂或者漏洞进行刷铜板和刷经验的行为,一经发现将立即予以扣除.<br/>
文明游戏,适度游戏,开心游戏.<br/>
如果选择同意表示愿意接受上述条款.<br/>
<a href="enter.jsp?ok=1">同意</a><br/>
<a href="index.jsp">不同意</a><br/>

<%}%>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>