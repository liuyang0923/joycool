<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%! static int COUNT_PRE_PAGE=8;%><%
response.setHeader("Cache-Control","no-cache");
AppAction action = new AppAction(request);
action.view();
String content = action.getParameterString("c");
AppBean app = (AppBean)request.getAttribute("appBean");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="交流区">
<p align="left">
【<%=app.getName()%>】交流区<br/>
<% 	if(action.isResult("tip")){
		%><%=action.getTip()%><br/><%
	} else{
		if (content != null && !"".equals(content) ){
			boolean result = action.reply(content,app);
			if (result){
				%>发表成功.<br/><a href="content.jsp?id=<%=app.getId() %>">返回交流区</a><br/><%
			} else {
				%><%=request.getAttribute("tip")%><br/><a href="content.jsp?id=<%=app.getId()%>">返回交流区</a><br/><%
			}
		} else {
			int totalCount = app.getReplyCount();
			PagingBean paging = new PagingBean(action, totalCount,COUNT_PRE_PAGE, "p");
			int pageNow = paging.getCurrentPageIndex();
			ArrayList replyList = (ArrayList)action.getReplyList(app,pageNow,COUNT_PRE_PAGE);
			if (replyList.size() != 0){
			   UserBean user = null;
			   AppReplyBean replyBean = null;
			   for(int i = 0;i < replyList.size();i++){
					replyBean = (AppReplyBean)replyList.get(i);
					user = UserInfoUtil.getUser(replyBean.getUserId());
					%><%=pageNow * COUNT_PRE_PAGE + i + 1%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>说:<%=StringUtil.toWml(replyBean.getContent())%><br/><%
			   }
		   }%><%=paging.shuzifenye("content.jsp?id=" + app.getId(), true, "|",response)%>
=评论(最多100字)=<br/>
<input name="acontent" value="" maxlength="100" /><br/>
<anchor>发表评论
<go href="content.jsp?id=<%=app.getId()%>" method="post">
	<postfield name="c" value="$acontent" />
</go>
</anchor><br/>
<% }
} %>
<a href="app.jsp?id=<%=app.getId() %>">返回组件</a><br/>
<a href="my.jsp">返回我的组件</a><br/>
<a href="index.jsp">返回组件集中营</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>