<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
int id = StringUtil.toInt(request.getParameter("id"));
ZippoStarBean star = ZippoFrk.getStarById(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猜名人专用zippo活动">
<p align="left">
<%=StringUtil.toWml(star.getName())%><br/>
<img src="/wxsj/zippo/images/end/<%=star.getId() %>.gif" alt="图片"/><br/>
<a href="/wxsj/zippo/end/index.jsp">返回颁奖页面</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>