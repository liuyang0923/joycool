<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.photo.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
FmPhotoAction action = new FmPhotoAction(request,response);
%><%@include file="inc.jsp"%><%
if (!action.hasParam("id")){response.sendRedirect("phocat.jsp");return;}
int pid = action.getParameterInt("id");
FmPhotoBean photoBean = FmPhotoAction.service.getPhotoBean("id="+pid);
List commList = FmPhotoAction.service.getCommentList("photo_id="+pid+" order by id desc");
if (action.hasParam("con") && "yes".equals(session.getAttribute("s"))) {session.removeAttribute("s");action.addComment(photoBean,uid);}
if (photoBean == null) request.setAttribute("tip","此照片已被删除!");
if (loginUser == null) request.setAttribute("tip","尚未登录,无法评论!");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族相册"><p align="left"><%=BaseAction.getTop(request, response)%><% 
if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}
if (photoBean != null) {
	if (!"sucsess".equals(request.getAttribute("result"))) {
		session.setAttribute("s","yes");
		%><input name="c"/><br/><anchor>评论<go href="comm.jsp?id=<%=pid%>" method="post"><postfield name="con" value="$c"></postfield></go></anchor><br/><%
		if (commList != null) {
			PagingBean paging = new PagingBean(action,commList.size(),10,"p");
			for (int i = paging.getStartIndex();i<paging.getEndIndex();i++) {
				FmCommentBean comm = (FmCommentBean) commList.get(i);
				%><%=i+1%>.<a href="/user/ViewUserInfo.do?userId=<%=comm.getUid()%>"><%=UserInfoUtil.getUser(comm.getUid()).getNickNameWml()%></a>:<%=StringUtil.toWml(comm.getContent())%>(<%=DateUtil.sformatTime(comm.getCreateTime())%>)<br/><%
			}
		  	%><%=paging.shuzifenye("comm.jsp?id="+pid,true,"|",response)%><%
		}
	} else {
		%><a href="comm.jsp?id=<%=pid%>">返回查看</a><br/><%
	}
	%><a href="cat.jsp?id=<%=photoBean.getAlbumId()%>">返回上一级</a><br/><%
}
%><a href="phocat.jsp">返回相册分类</a><br/>
&lt;<a href="/fm/myfamily.jsp?id=<%=fid%>">返回家族</a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>