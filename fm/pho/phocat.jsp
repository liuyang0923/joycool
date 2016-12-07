<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.photo.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
FmPhotoAction action = new FmPhotoAction(request,response);
%><%@include file="inc.jsp"%><%
List albumList = FmPhotoAction.service.getAlbumList("fm_id="+fid+" order by id desc");
List defaltList = FmPhotoAction.service.getPhotoList("album_id=0 and fm_id="+fid);
FamilyUserBean fmUserBean = action.getFmUserByID(uid);
boolean my = fmUserBean != null && fmUserBean.getFm_id() == fid;// 我的家族
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族相册"><p align="left"><%=BaseAction.getTop(request, response)%>
++<a href="cat.jsp?id=0">默认分类</a>(共<%=defaltList.size()%>张)<br/><% 
if (albumList.size() > 0) {
	PagingBean paging = new PagingBean(action,albumList.size(),10,"p");
	for (int i = paging.getStartIndex();i<paging.getEndIndex();i++) {
		FmAlbumBean albumBean = (FmAlbumBean)albumList.get(i);
%>++<%if(albumBean.getPermission()==0||my){%><a href="cat.jsp?id=<%=albumBean.getId()%>"><%=StringUtil.toWml(albumBean.getAlbumName())%></a>(共<%=albumBean.getPhotoNum()%>张)<%}else{%><%=StringUtil.toWml(albumBean.getAlbumName())%>(私有)<%}%><br/><%
	}
%><%=paging.shuzifenye("phocat.jsp",false,"|",response)%><%
}
if (action.hasManagePower(fmUserBean,fid)){
%><a href="addcat.jsp">新增相册</a><% 
} else {
	%>新增相册<%
}
 %><br/>&lt;<a href="/fm/myfamily.jsp?id=<%=fid%>">返回家族</a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>