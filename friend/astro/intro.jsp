<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.friend.*"%>
<%! static int COUNT_PRE_PAGE = 5; %>
<% response.setHeader("Cache-Control","no-cache");
   AstroAction action = new AstroAction(request);
   int id = action.getParameterInt("id");	// 星座id
   if (id < 1 || id > 12){id = 1;}
   String tip = "";
   PagingBean paging = null;
   AstroStory storyExp = null;
   AstroStory story = null;
   // 这里还是要改的。
   // 注意:如果浏览者是男的，flag in (0,1,2),如果是女的，flag in (0,1,3)。标号为“2”的是男性文章，“3”的是女性文章。
   UserBean user = UserInfoUtil.getUser(action.getLoginUser().getId());
   ArrayList storyList = null;
   if (user != null){
   		// 星座解说
   		storyExp = AstroAction.service.getStory(" astro_id=" + id + " and flag=0");
   		if (user.getGender() == 1){
   			storyList = (ArrayList)AstroAction.service.getStoryList(" astro_id = " + id + " and flag in (1,2) order by flag");
   		} else {
   			storyList = (ArrayList)AstroAction.service.getStoryList(" astro_id = " + id + " and flag in (1,3) order by flag");
   		}
   		paging = new PagingBean(action, storyList.size(), COUNT_PRE_PAGE, "p");
   } else {
   		tip = "请登陆后再访问.";
   } 
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="星座"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%>星座解说<br/>
<%=action.astroList.get(id)%><br/>
<%if (storyExp != null){
%><%=StringUtil.toWml(StringUtil.limitString(storyExp.getContent(),60))%>[<a href="intro2.jsp?id=<%=id%>&amp;sid=<%=storyExp.getId()%>">详细</a>]<br/><%	
}
if (storyList.size() > 0){
    %>★★星座综述★★<br/><%
	for (int i = paging.getStartIndex();i < paging.getEndIndex();i++){
		story = (AstroStory)storyList.get(i);
		%><a href="intro2.jsp?id=<%=id%>&amp;sid=<%=story.getId()%>"><%=StringUtil.toWml(StringUtil.limitString(story.getTitle(),20))%></a><br/> <%
	}%><%=paging.shuzifenye("intro.jsp?id=" + id,true,"|",response)%>
查看其它星座:<select name="xz" value="<%=id%>">
<option value="1">白羊座</option>
<option value="2">金牛座</option>
<option value="3">双子座</option>
<option value="4">巨蟹座</option>
<option value="5">狮子座</option>
<option value="6">处女座</option>
<option value="7">天秤座</option>
<option value="8">天蝎座</option>
<option value="9">射手座</option>
<option value="10">摩羯座</option>
<option value="11">水瓶座</option>
<option value="12">双鱼座</option>
</select><anchor>查看
	<go href="intro.jsp" method="post">
		<postfield name="id" value="$xz"/>
	</go>
</anchor><br/>
<a href="index.jsp">返回我的星座</a><br/>
<%
} else {
	%>没有找到相关信息.<br/><a href="index.jsp">返回</a><br/><%
}
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}
%><%=BaseAction.getBottomShort(request, response)%></p></card></wml>