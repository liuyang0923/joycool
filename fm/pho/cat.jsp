<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.photo.*"%><%@ page import="jc.family.photo.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
FmPhotoAction action = new FmPhotoAction(request,response);
%><%@include file="inc.jsp"%><%
if (!action.hasParam("id")) {response.sendRedirect("phocat.jsp");return;}
int aid = action.getParameterInt("id");
FmAlbumBean albumBean = null;
if (aid != 0) {albumBean = FmPhotoAction.service.getAlbumBean("id="+aid+" and fm_id="+fid);}
if (aid != 0 && albumBean == null) {response.sendRedirect("phocat.jsp");return;}
List list = FmPhotoAction.service.getPhotoList("fm_id="+fid+" and album_id="+aid+" order by id desc");
FamilyUserBean fmUserBean = action.getFmUserByID(uid);
boolean my = fmUserBean != null && fmUserBean.getFm_id() == fid;// 我的家族
boolean manage=false;
if(my)
	manage=fmUserBean.isflagPhoto();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族相册"><p align="left"><%=BaseAction.getTop(request, response)%>
家族相册&gt;<%if (albumBean != null) {%><%=StringUtil.toWml(albumBean.getAlbumName())%><%} else {%>默认分类<%}%><br/><% 
if (my) {
%><a href="addpho.jsp?id=<%=aid%>">加照片</a><% 	
if (albumBean != null) {
if (action.hasManagePower(fmUserBean,fid)) {%>|<a href="manacat.jsp?id=<%=aid%>">管理</a><%} else {%>|管理<%}
}%><br/><%
}
if (action.canSee(albumBean,uid)) {
	if (list.size() > 0) {
		PagingBean paging = new PagingBean(action,list.size(),5,"p");
		for (int i = paging.getStartIndex();i<paging.getEndIndex();i++) {
			FmPhotoBean photoBean = (FmPhotoBean) list.get(i);
	%><%=i+1%>.<%=StringUtil.toWml(photoBean.getPhotoName())%>(<a href="comm.jsp?id=<%=photoBean.getId()%>">评<%=photoBean.getCommentTime()%></a>)<%if(manage||fmUserBean!=null&&photoBean.getUploadUid()==fmUserBean.getId()){%><a href="del.jsp?id=<%=photoBean.getId()%>">删</a><%}%><br/><%
	%><img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%><%=photoBean.getUrl()%>" alt="o"/><br/><%
		}
	  	%><%=paging.shuzifenye("cat.jsp?id="+aid,true,"|",response)%><%
	}
} else {
	%>此相册仅为本家族可见!<br/><%
}
%><a href="phocat.jsp">返回相册分类</a><br/>
&lt;<a href="/fm/myfamily.jsp?id=<%=fid%>">返回家族</a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>