<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.photo.*"%><%@ page import="jc.family.photo.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
FmPhotoAction action = new FmPhotoAction(request,response);
%><%@include file="inc.jsp"%><%
if (loginUser == null) {response.sendRedirect("phocat.jsp");return;}
if (action.hasParam("aln")) {
	if ("yes".equals((String)session.getAttribute("add"))){
		session.removeAttribute("add");
		FamilyUserBean fmUserBean = action.getFmUserByID(uid);
		action.addCat(fmUserBean,fid,uid);
	} else {
		response.sendRedirect("phocat.jsp");return;
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族相册"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (request.getAttribute("tip") !=null) {%><%=request.getAttribute("tip")%><br/><%}
if ("sucsess".equals(request.getAttribute("result"))) {
	%>相册添加成功!<br/><%
} else {
	session.setAttribute("add","yes");
	%>添加相册分类<br/>新增相册名称:<input name="an" size="18"/><br/>设置相册权限:<select name="per"><% 
	for (int i=0;i<FmPhotoAction.permissions.length;i++) {
	%><option value="<%=i%>"><%=FmPhotoAction.permissions[i]%></option><%
	}
 	%></select><br/><anchor>添加<go href="addcat.jsp" method="post"><postfield name="aln" value="$an"/><postfield name="permi" value="$per"/></go></anchor><br/><%
}
 %><a href="phocat.jsp">返回相册分类</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>