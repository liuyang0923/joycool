<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/zippo/game/index.jsp"));
	return;
}

int id = StringUtil.toInt(request.getParameter("id"));
ZippoStarBean star = ZippoFrk.getStarById(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猜名人专用火机">
<p align="left">
<%=StringUtil.toWml(star.getName())%><br/>
<img src="<%=star.getImageUrl()%>" alt="图片"/>&nbsp;<img src="/wxsj/zippo/images/star/<%=star.getId() %>_1.gif" alt="图片"/><br/>
<%=StringUtil.toWml(star.getIntroduction())%><br/>
你认为<%=StringUtil.toWml(star.getName())%>适合哪款火机？赶紧为他挑一款吧~<br/>
<a href="/wxsj/zippo/game/zippoList.jsp?typeId=4">军旅风格</a>&nbsp;<a href="/wxsj/zippo/game/zippoList.jsp?typeId=1">都市风格</a>&nbsp;<a href="/wxsj/zippo/game/zippoList.jsp?typeId=6">车迷一族</a><br/>
<a href="/wxsj/zippo/game/zippoList.jsp?typeId=2">成功人士</a>&nbsp;<a href="/wxsj/zippo/game/zippoList.jsp?typeId=5">青春魅力</a>&nbsp;<a href="/wxsj/zippo/game/zippoList.jsp?typeId=3">个性人类</a><br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<a href="/wxsj/zippo/rule.jsp">具体奖励和答题规则</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>