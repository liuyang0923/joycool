<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.home.*,net.joycool.wap.service.impl.*,net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean,net.joycool.wap.cache.util.HomeCacheUtil,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendMarriageBean,net.joycool.wap.bean.PagingBean,net.joycool.wap.cache.OsCacheUtil" %>
<%! static HomeServiceImpl homeService = new HomeServiceImpl(); 
    static int COUNT_PRE_PAGE = 5; %>
<%response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
String tip = "";
String otherTitle = "";
boolean isMySelf = false;
boolean flag = false;
UserBean user = null;
List list = null;
List photoList = null;
HomePhotoCat photoCat = null;
HomePhotoBean photo = null;
HomeUserBean homeUser = null;
HomeUserBean coupleUser = null;
PagingBean paging = null;
int backId = 0;
int coupleUid = 0;
int uid = action.getParameterInt("uid");
if (uid <= 0){
	uid = action.getParameterInt("userId");
}
if (uid <= 0){
	uid = action.getLoginUser().getId();
	isMySelf = true;
} else {
	if (uid == action.getLoginUser().getId()){isMySelf = true;}else{isMySelf = false;}
}
int catId = action.getParameterInt("cid");
//int del = action.getParameterInt("d");
//int submit = action.getParameterInt("s");
//if (del == 1){
//	if (submit == 1){
//		homeService.deleteHomePhotoCat(catId,action.getLoginUser().getId());
//		homeUser = HomeCacheUtil.getHomeCache(action.getLoginUser().getId());
//		if (homeUser != null){
//			homeUser.setPhotoCatCount(homeUser.getPhotoCatCount()>0?homeUser.getPhotoCatCount()-1:0);
//			OsCacheUtil.put(HomeCacheUtil.getKey(action.getLoginUser().getId()),homeUser,OsCacheUtil.HOME_USER_CACHE_GROUP);
//		}
//		tip  = "删除成功.";
//		backId = 2;
//	} else {
//		backId = 1;
//		tip = "1";
//	}
//}
user = UserInfoUtil.getUser(uid);
if (user == null){
	tip = "用户不存在.";
} else {
	homeUser = HomeCacheUtil.getHomeCache(uid);			    //"我"的家园用户信息
	// 取得"另一伴"的UID
	coupleUid = action.getCoupleUid(uid);
	// 取得特殊的标题
	otherTitle = action.getOtherTitle(homeUser,coupleUid);	
	if (homeUser == null){
		tip = uid==action.getLoginUser().getId()?"您":user.getGenderText();
		tip += "还没有建立家园.";
	} else {
		// 检查有没有默认分类，如果没有就建立
//		if (homeService.getHomePhotoCat(" uid=" + uid + " and def=1") == null){
//			action.addDefualtCat(action.getLoginUser().getId(),1);
//		}
		ArrayList userFriends = UserInfoUtil.getUserFriends(uid);
		flag = userFriends.contains(action.getLoginUser().getId() + "");
//		if (isMySelf){
//			// 我自己，浏览全部相册
//			list = homeService.getHomePhotoCatList(uid,HomePhotoCat.PRIVACY_SELF);
//		} else if (flag){
//			// "我"是相册主人的好友
//			list = homeService.getHomePhotoCatList(uid,HomePhotoCat.PRIVACY_FRIEND);
//		} else {
//			// 只能浏览“全部可见”的相册
//			list = homeService.getHomePhotoCatList(uid,HomePhotoCat.PRIVACY_ALL);
//		}
		list = homeService.getHomePhotoCatList(uid,HomePhotoCat.PRIVACY_SELF);
		paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
//		list = homeService.getHomeDiaryCatList2(uid);
		photoList = homeService.getHomePhotoList("user_id= " + uid + " order by id desc limit 3");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家园相册"><p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (isMySelf){
%>我的家园>相册<br/>注:照片数量上限为30张,最多新建4个分类.<br/>
<%for (int i = 0 ; i < photoList.size() ; i++){
	photo = (HomePhotoBean)photoList.get(i);
	if (photo != null){
		%><%=i+1%>.<%=StringUtil.toWml(photo.getTitle())%><br/><a href="homePhoto.jsp?cid=<%=photo.getCatId()%>"><img src="<%=Constants.IMG_ROOT_URL%><%=photo.getAttach()%>" alt="o"/></a><br/><%
	}
}%>
++<a href="homePhoto.jsp?cid=0">默认分类</a>(共<%=homeUser.getPhotoDefCount()%>张)<br/>
<%for (int i = paging.getStartIndex() ; i < paging.getEndIndex();i++){
photoCat = (HomePhotoCat)list.get(i);
if (photoCat != null){
	%>++<a href="homePhoto.jsp?cid=<%=photoCat.getId()%>"><%=StringUtil.toWml(photoCat.getCatName())%></a>(共<%=photoCat.getCount()%>张)<br/><%
}
}%><%=paging.shuzifenye("homePhotoCat.jsp",false,"|",response)%>
<a href="addCat.jsp?t=1">新增相片分类</a><br/><a href="home.jsp">返回我的家园</a><br/><a href="/image/lib/lib.jsp">去我的图库转转</a><br/><%
} else {
%><%=user.getNickNameWml()%>的家园>相册<br/>
<%for (int i = 0 ; i < photoList.size() ; i++){
	photo = (HomePhotoBean)photoList.get(i);
	if (photo.getCatId() != 0){
		photoCat = action.getCatFromList(photo,list);	
	} else {
		photoCat = new HomePhotoCat();
		photoCat.setPrivacy(HomePhotoCat.PRIVACY_ALL);
	}
	if (photo != null && photoCat != null){
		%><%=i+1%>.<%=StringUtil.toWml(photo.getTitle())%><br/><%if(photoCat.canRead(flag, isMySelf)){ %><a href="homePhoto.jsp?cid=<%=photo.getCatId()%>&amp;uid=<%=uid%>"><img src="<%=Constants.IMG_ROOT_URL%><%=photo.getAttach()%>" alt="o"/></a><%}else{%>(权限不足)<%}%><br/><%
	}
}%>
++<a href="homePhoto.jsp?cid=0&amp;uid=<%=uid%>">默认分类</a>(共<%=homeUser.getPhotoDefCount()%>张)<br/>
<%
for (int i = paging.getStartIndex(); i < paging.getEndIndex();i++){
	photoCat = (HomePhotoCat)list.get(i);
	if (photoCat != null){
		%>++<%if(photoCat.canRead(flag, isMySelf)){ %><a href="homePhoto.jsp?cid=<%=photoCat.getId()%>&amp;uid=<%=uid%>"><%=StringUtil.toWml(photoCat.getCatName())%></a>(共<%=photoCat.getCount() %>张)<%}else{%><%=StringUtil.toWml(photoCat.getCatName())%>(权限不足)<%}%><br/><%
	}
}%><%=paging.shuzifenye("homePhotoCat.jsp?uid=" + uid,true,"|",response)%><%
%><a href="home2.jsp?uid=<%=uid %>">返回<%=user.getNickNameWml()%>的家园</a><br/><a href="/image/lib/lib.jsp?uid=<%=uid%>">去<%=user.getNickNameWml()%>的图库转转</a><br/><a href="home.jsp">返回我的家园</a><br/><%	
}
} else {
if (backId == 0){
// 普通的返回
%><%=tip %><br/><a href="home.jsp">返回我的家园</a><br/><%
}
}%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>