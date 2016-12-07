<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.bean.*,net.joycool.wap.framework.BaseAction,jc.answer.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	HelpAction action = new HelpAction(request);
	UserBean ub = action.getLoginUser();
	if(ub == null){
		response.sendRedirect("/user/login.jsp");
		return;
	}
	int gender = ub.getGender();
	int tp = action.getParameterInt("tp");
	int count = 0;
	List list = action.getPrombList();
	List dynamicList = HelpAction.service.get222ProblemList(" del=0 order by id desc limit 4");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="有偿求助"><p><%=BaseAction.getTop(request, response)%><%
if(tp == 1){
%><a href="hpindex.jsp">全部</a>|<a href="hpindex.jsp?tp=3">热帖</a>|我的:帖子|<a href="hpindex.jsp?tp=2">回复</a><br/><%
}else if(tp == 2){
%><a href="hpindex.jsp">全部</a>|<a href="hpindex.jsp?tp=3">热帖</a>|我的:<%if(gender == 0){%><a href="hpindex.jsp?tp=1">帖子</a><%}else{%>帖子<%}%>|回复<br/><%
}else if(tp == 3){
%><a href="hpindex.jsp">全部</a>|热帖|我的:<%if(gender == 0){%><a href="hpindex.jsp?tp=1">帖子</a><%}else{%>帖子<%}%>|<a href="hpindex.jsp?tp=2">回复</a><br/><%
}else{
	if(action.getParameterInt("p")<=0){
%><IMG src="img/hpLogo.gif" alt="logo"/><br/>注:只有美女才可发求助帖<br/>==美女新求助==<br/><%
if(dynamicList != null && dynamicList.size() > 0){
	for(int i=0;i<dynamicList.size();i++){
		BeanProblem bp = (BeanProblem)dynamicList.get(i);
	%><a href="hppost.jsp?pid=<%=bp.getId()%>"><%=StringUtil.toWml(StringUtil.limitString(bp.getPTitle(),24))%></a><br/><%
	}
}else{
%>您好,当前没有相关记录.<br/><%
}
%>==美女求助站==<br/><%}%>全部|<a href="hpindex.jsp?tp=3">热帖</a>|我的:<%if(gender == 0){%><a href="hpindex.jsp?tp=1">帖子</a><%}else{%>帖子<%}%>|<a href="hpindex.jsp?tp=2">回复</a><br/><%
}
if(gender == 0){
%><a href="hp.jsp">我要求助</a><%
}else{
%>我要求助<%
}
%>|<a href="hprule.jsp">规则与声明</a><br/><%
if(list != null && list.size() > 0){
	count = 1;
	PagingBean paging = new PagingBean(action, list.size(), 10, "p");
	for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
		BeanProblem bp = (BeanProblem)list.get(i);
		if(bp != null){
			%><%=count%>.<a href="hppost.jsp?pid=<%=bp.getId()%>"><%=StringUtil.toWml(bp.getPTitle())%></a>(<%=bp.getNumReply()%>/<%=bp.getNumView()%>)<br/><%
			count++;
		}
	}
	%><%=paging.shuzifenye("hpindex.jsp?jcfr=1&amp;tp="+action.getParameterInt("tp"), true, "|", response)%><%
}else{
%>您好,当前没有相关记录.<br/><%
}	
 %><%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>