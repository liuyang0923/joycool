<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.friend.*"%>
<% response.setHeader("Cache-Control","no-cache");
   AstroAction action = new AstroAction(request);
   int id = action.getParameterInt("id");		// 星座id
   int storyId = action.getParameterInt("sid");	// 故事id
   if (id < 1 || id > 12){id = 1;}
   if (storyId < 1 ){storyId = 1;}
   String tip = "";
   AstroStory story = (AstroStory)action.service.getStory(" id = " + storyId + " and astro_id = " + id );
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="星座"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%>星座解说<br/>
<% 	if (story != null){
%><%=StringUtil.toWml(story.getTitle())%><br/>
<%=StringUtil.toWml(action.pagingTopic(story,"intro2.jsp?id=" + id + "&amp;sid=" + storyId,true))%><br/>
<% if (request.getAttribute("pageFeet") != null){
%><%=request.getAttribute("pageFeet")%><%
}%>
<a href="intro.jsp?id=<%=id%>">返回</a><br/>
<%
	} else {
		%>没有找到相关信息.<br/><a href="index.jsp">返回</a><br/><%
	}
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}
%><%=BaseAction.getBottomShort(request, response)%></p></card></wml>