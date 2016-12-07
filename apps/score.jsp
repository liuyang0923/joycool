<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%! static int COUNT_PRE_PAGE=8;%><%
response.setHeader("Cache-Control","no-cache");
AppAction action = new AppAction(request);
action.view();
boolean result = false;
AppBean app = (AppBean)request.getAttribute("appBean");
String content = action.getParameterString("c");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="评分">
<p align="left">
【<%=app.getName()%>】评分<br/>
<% 	if(action.isResult("tip")){
		%><%=action.getTip()%><br/><a href="list.jsp">返回组件列表</a><br/><%
	} else{
		if (content != null && !"".equals(content)){
			int score = action.getParameterInt("s");
			result = action.score(content,score,app);
			if (result){
				%>评分成功.<br/><a href="score.jsp?id=<%=app.getId() %>">返回评分</a><br/><%
			} else {
				%><%=request.getAttribute("tip") %><br/><a href="score.jsp?id=<%=app.getId()%>">返回评分</a><br/><%			
			}
		} else {
			int totalCount = app.getScoreCount();
			PagingBean paging = new PagingBean(action, totalCount,COUNT_PRE_PAGE, "p");
			int pageNow = paging.getCurrentPageIndex();
			ArrayList scoreList = (ArrayList)action.getScoreList(app,pageNow,COUNT_PRE_PAGE);
			if (scoreList.size() != 0){
			   UserBean user = null;
			   AppScoreBean scoreBean = null;
			   for(int i = 0;i < scoreList.size();i++){
					scoreBean = (AppScoreBean)scoreList.get(i);
					user = UserInfoUtil.getUser(scoreBean.getUserId());
					%><%=pageNow * COUNT_PRE_PAGE + i + 1%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>(<%=scoreBean.getScore()%>分)说:<%=StringUtil.toWml(scoreBean.getContent())%><br/><%
			   }
		    }%><%=paging.shuzifenye("score.jsp?id=" + app.getId(), true, "|",response)%>
=评分(最多100字)=<br/>
分数:<select name="score" value="1">
	<option value="1">1分</option>
	<option value="2">2分</option>
	<option value="3">3分</option>
	<option value="4">4分</option>
	<option value="5">5分</option>
</select><br/>
评论:<input name="ccontent" value="" maxlength="100" /><br/>
<anchor>发表评分
<go href="score.jsp?id=<%=app.getId()%>" method="post">
	<postfield name="c" value="$ccontent" />
	<postfield name="s" value="$score"/>
</go>
</anchor><br/>
<%		}
	}%>
<a href="app.jsp?id=<%=app.getId() %>">返回组件</a><br/>
<a href="my.jsp">返回我的组件</a><br/>
<a href="index.jsp">返回组件集中营</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>