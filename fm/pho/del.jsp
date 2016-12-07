<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.photo.*"%><%@ page import="jc.family.photo.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
FmPhotoAction action = new FmPhotoAction(request,response);
%><%@include file="inc.jsp"%><%
if (loginUser == null) {response.sendRedirect("phocat.jsp");return;}
if (!action.hasParam("id")) {response.sendRedirect("phocat.jsp");return;}
int pid = action.getParameterInt("id");
FmPhotoBean photoBean = null;
if (pid != 0) {photoBean = FmPhotoAction.service.getPhotoBean("id="+pid);}
if (action.hasParam("y")){
	FamilyUserBean fmUserBean = action.getFmUserByID(uid);
	action.delPhoto(photoBean,fmUserBean,uid);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族相册"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}
if (action.hasParam("y")) {
	%><a href="cat.jsp?id=<%=photoBean.getAlbumId()%>">返回相册</a><br/><%
} else {
	%><a href="del.jsp?y=1&amp;id=<%=pid%>">确认删除<%=StringUtil.toWml(photoBean.getPhotoName())%>!</a><br/><a href="cat.jsp?id=<%=photoBean.getAlbumId()%>">不删了</a><br/><%
}
%><a href="phocat.jsp">返回相册分类</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>