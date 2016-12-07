<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/zippo/game/index.jsp"));
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
IZippoService zippoService = ServiceFactory.createZippoService(IBaseService.CONN_IN_SERVICE, null);

ArrayList pairList = zippoService.getZippoPairList("user_id = " + loginUser.getId(), 0, -1, "id");
ZippoMobileBean oldMobile = zippoService.getZippoMobile("user_id = " + loginUser.getId());
int i, count;
ZippoPairBean pair = null;
count = pairList.size();
ZippoStarBean star = null;
ZippoBean zippo = null;
%>
<card title="猜名人专用火机">
<p align="left">
您已经确认的猜测<br/>
--------------------<br/>
<%
if(count == 0){
%>
您还没有完成一次搭配！<br/>
<%
}
else {
	for(i = 0; i < count; i ++){
		pair = (ZippoPairBean) pairList.get(i);
		star = ZippoFrk.getStarById(pair.getStarId());
		zippo = ZippoFrk.getZippoById(pair.getZippoId());
%>
<%=(i + 1)%>.<a href="/wxsj/zippo/game/star.jsp?id=<%=star.getId()%>"><%=StringUtil.toWml(star.getName())%></a>:<a href="/wxsj/zippo/game/zippo.jsp?id=<%=zippo.getId()%>"><%=StringUtil.toWml(zippo.getName())%></a><br/>
<%
	}
    if(oldMobile != null){
%>
您已经提交您的所有猜测结果！您的领奖手机号是<%=StringUtil.toWml(oldMobile.getMobile())%>，领奖号码是<%=oldMobile.getId()%>。<br/>
<%
	}
    else {
%>
您还没有提交您的猜测结果！<br/>
<a href="/wxsj/zippo/game/confirm.jsp">现在提交我的全部搭配</a><br/>
<%
	}
}
%>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<%
zippoService.releaseAll();
%>
<a href="/wxsj/zippo/rule.jsp">具体奖励和答题规则</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>