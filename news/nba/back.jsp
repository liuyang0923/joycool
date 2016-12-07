<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>
<%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	String[] show ={"","输入不能为空哦!","长度不能超过50个字!","操作太快了,休息一下吧!","发言成功!"};
	int mid = action.getParameterInt("mid");
	int back = 0;
	BeanMatch bm = action.getMatchById(mid);
	if(action.getLoginUser() != null && bm!=null && bm.getId()>0)
		back = action.goBack(mid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%if(action.getLoginUser() == null){
%><card title="返回">
<p>
您暂未<a href="../../user/login.jsp">登录</a>,还不能发言哦!<br/><a href="alive.jsp?mid=<%=mid%>">返回</a><br/>
</p>
</card><%
}else if(bm!=null && bm.getId()>0 && bm.getStaticValue() > 0){
	if(back > 0 && back <= 3){
	%><card title="返回" ontimer="<%=response.encodeURL("reply.jsp?mid="+mid)%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%><%
	%><%=show[back]%>3秒后自动<a href="reply.jsp?mid=<%=mid%>">返回</a><br/><%=BaseAction.getBottom(request, response)%></p></card><%
	}else{
	%><card title="返回" ontimer="<%=response.encodeURL("alive.jsp?mid="+mid)%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%><%
		if(back == 4){
		%><%=show[back]%><%
		}
	%>3秒后自动<a href="alive.jsp?mid=<%=mid%>">返回</a><br/><%=BaseAction.getBottom(request, response)%>
	</p>
	</card><%
	}
}else{%><card title="返回"><p>不存在该赛事直播!<br/><a href="index.jsp">返回</a><br/></p></card><%
}
%></wml>