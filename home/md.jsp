<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
// 推荐日记
CustomAction customAction = new CustomAction(request);
HomeServiceImpl homeService =HomeAction.getHomeService();
PagingBean paging = new PagingBean(customAction, 1000, 10, "p");
		Vector commendDiary = null;
		if (commendDiary == null || commendDiary.size() == 0) {
			commendDiary = homeService.getHomeDiaryTopList2("order by b.id desc limit " + paging.getStartIndex() + ",10");

		}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="推荐日记">
<p align="left">
<%=BaseAction.getTop(request, response)%>
【推荐日记】<br/>
<%
for(int i = 0; i < commendDiary.size(); i ++){
	HomeDiaryBean homeDiary=(HomeDiaryBean)commendDiary.get(i);
%>
<%=i+1%>.
<a href="homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>  
(阅<%=homeDiary.getDailyHits()%> | 评<%=homeDiary.getReviewCount()%>)<br/>
<%
}
%>
<%=paging.shuzifenye("md.jsp", false, "|", response)%>
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>