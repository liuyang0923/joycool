<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.photo.*"%><%@ page import="jc.family.photo.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
FmPhotoAction action = new FmPhotoAction(request,response);
%><%@include file="inc.jsp"%><%
if (!action.hasParam("id")) {response.sendRedirect("phocat.jsp");return;} // 没有相册id参数,直接跳回
FamilyUserBean fmUserBean = action.getFmUserByID(uid);
if (!action.hasManagePower(fmUserBean,fid)){response.sendRedirect("phocat.jsp");return;} // 不是管理直接跳回
int aid = action.getParameterInt("id");
FmAlbumBean albumBean = FmPhotoAction.service.getAlbumBean("id="+aid);
if ("yes".equals(session.getAttribute("mana"))) {session.removeAttribute("mana");if (action.hasParam("dy")) {if (action.delAlbum(albumBean,fmUserBean,uid)) {albumBean = null;}}
} else {if (action.hasParam("dy")) {response.sendRedirect("phocat.jsp");return;}}
if (action.hasParam("c")) {action.alterAlbum(albumBean,fmUserBean);}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族相册"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}
if (albumBean != null) {
	if (action.hasParam("d")) {
		session.setAttribute("mana","yes");
%><a href="manacat.jsp?dy=1&amp;id=<%=aid%>">确定删除</a><br/><a href="manacat.jsp?id=<%=aid%>">不删了</a><br/><%
	} else {
%><%=albumBean.getAlbumName()%>&gt;设置<br/><% 
%>当前状态是:<select name="per"><% 
	for (int i=0;i<FmPhotoAction.permissions.length;i++) {
		if (i == albumBean.getPermission()) {
%><option value="<%=i%>" selected="selected"><%=FmPhotoAction.permissions[i]%></option><%
		} else {
%><option value="<%=i%>"><%=FmPhotoAction.permissions[i]%></option><%	
		}
	}
%></select><br/>相册分类名称:<input size="18" name="an" value="<%=StringUtil.toWml(albumBean.getAlbumName())%>"/><br/><%
%><anchor>确认修改<go href="manacat.jsp?c=1&amp;id=<%=aid%>" method="post"><postfield name="perm" value="$per"></postfield><postfield name="aln" value="$an"></postfield></go></anchor><br/><%
%><a href="cat.jsp?id=<%=albumBean.getId()%>">返回<%=StringUtil.toWml(albumBean.getAlbumName())%></a><br/><a href="manacat.jsp?d=1&amp;id=<%=aid%>">!删除分类:<%=StringUtil.toWml(albumBean.getAlbumName())%></a><br/><%
	}
} 
%><a href="phocat.jsp">返回相册分类</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>