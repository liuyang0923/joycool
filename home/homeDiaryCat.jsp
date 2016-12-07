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
List diaryList = null;
HomeDiaryCat diaryCat = null;
HomeDiaryBean diary = null;
HomeUserBean homeUser = null;
UserBean coupleUser = null;
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
//		homeService.deleteHomeDiaryCat(catId,action.getLoginUser().getId());
//		homeUser = HomeCacheUtil.getHomeCache(action.getLoginUser().getId());
//		if (homeUser != null){
//			homeUser.setDiaryCatCount(homeUser.getDiaryCatCount()>0?homeUser.getDiaryCatCount()-1:0);
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
	if (homeUser == null){
		if (uid==action.getLoginUser().getId()){tip = "您";} else {tip = "TA";}
		tip += "还没有建立家园.";
	} else {
		// 取得"另一伴"的UID
		coupleUid = action.getCoupleUid(uid);
		coupleUser = UserInfoUtil.getUser(coupleUid);
		// 取得特殊的标题
		otherTitle = action.getOtherTitle(homeUser,coupleUid);
		// 检查有没有默认分类，如果没有就建立
//		if (homeService.getHomeDiaryCat(" uid=" + uid + " and def=1") == null){
//			action.addDefualtCat(action.getLoginUser().getId(),0);
//		}
		ArrayList userFriends = UserInfoUtil.getUserFriends(uid);
		flag = userFriends.contains(action.getLoginUser().getId() + "");
//		if (isMySelf){
//			// 我自己，浏览全部日记
//			list = homeService.getHomeDiaryCatList(uid,HomeDiaryCat.PRIVACY_SELF);
//		} else if (flag){
//			// "我"是日记主人的好友
//			list = homeService.getHomeDiaryCatList(uid,HomeDiaryCat.PRIVACY_FRIEND);
//		} else {
//			// 只能浏览“全部可见”的日记
//			list = homeService.getHomeDiaryCatList(uid,HomeDiaryCat.PRIVACY_ALL);
//		}
		// 全部日记分类
		list = homeService.getHomeDiaryCatList(uid,HomeDiaryCat.PRIVACY_SELF);
		paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
//		list = homeService.getHomeDiaryCatList2(uid);
		// 如果是我自己，则54权限设置。
		diaryList = homeService.getHomeDiaryList("user_id= " + uid + " and del=0 order by id desc limit 5");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="日记薄"><p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (isMySelf){
%>我的家园>日记<br/>
<%for (int i = 0 ; i < diaryList.size() ; i++){
	diary = (HomeDiaryBean)diaryList.get(i);
	if (diary != null){
		%><%=i+1%>.<a href="homeDiary.jsp?diaryId=<%=diary.getId()%>"><%=StringUtil.toWml(diary.getTitel())%></a>(<%=diary.getReviewCount() %>/<%=diary.getHits() %>)<br/><%
	}
}%>
<a href="add.jsp?t=1">写日记>></a><br/>
++<a href="homeDiaryList.jsp?cid=0">默认分类</a>(共<%=homeUser.getDiaryDefCount()%>篇)<br/>
<%for (int i = paging.getStartIndex() ; i < paging.getEndIndex();i++){
diaryCat = (HomeDiaryCat)list.get(i);
if (diaryCat != null){
	%>++<a href="homeDiaryList.jsp?cid=<%=diaryCat.getId()%>"><%=StringUtil.toWml(diaryCat.getCatName())%></a>(共<%=diaryCat.getCount()%>篇)<br/><%
}
}%><%=paging.shuzifenye("homeDiaryCat.jsp",false,"|",response)%><a href="addCat.jsp">添加新分类</a><br/><a href="home.jsp">返回我的家园</a><br/><%
} else {
%><%=user.getNickNameWml()%>的家园>日记<br/>
<%for (int i = 0 ; i < diaryList.size() ; i++){
	diary = (HomeDiaryBean)diaryList.get(i);
	if (diary.getCatId() != 0){
		diaryCat = action.getCatFromList(diary,list);
	} else {
		diaryCat = new HomeDiaryCat();
		diaryCat.setPrivacy(HomeDiaryCat.PRIVACY_ALL);
	}
	if (diary != null && diaryCat != null){
		%><%=i+1%>.<%if(diaryCat.canRead(flag, isMySelf)){ %><a href="homeDiary.jsp?diaryId=<%=diary.getId()%>&amp;userId=<%=uid%>"><%=StringUtil.toWml(diary.getTitel())%></a>(<%=diary.getReviewCount() %>/<%=diary.getHits() %>)<%}else{%><%=StringUtil.toWml(diary.getTitel())%>(权限不足)<%}%><br/><%
	}
}%>
++<a href="homeDiaryList.jsp?uid=<%=uid%>&amp;cid=0">默认分类</a>(共<%=homeUser.getDiaryDefCount()%>篇)<br/>
<%
for (int i = paging.getStartIndex(); i < paging.getEndIndex();i++){
	diaryCat = (HomeDiaryCat)list.get(i);
	if (diaryCat != null){
		%>++<%if(diaryCat.canRead(flag, isMySelf)){ %><a href="homeDiaryList.jsp?cid=<%=diaryCat.getId()%>&amp;uid=<%=uid%>"><%=StringUtil.toWml(diaryCat.getCatName())%></a>(共<%=diaryCat.getCount() %>篇)<br/><%}else{%><%=StringUtil.toWml(diaryCat.getCatName())%>(权限不足)<br/><%}%><%
	}
}%><%=paging.shuzifenye("homeDiaryCat.jsp?uid=" + uid,true,"|",response)%><%
%><a href="home2.jsp?uid=<%=user.getId()%>">返回<%=user.getNickNameWml()%>的家园</a><br/><a href="home.jsp">返回我的家园</a><br/><%	
}
} else {
if (backId == 0){
// 普通的返回
%><%=tip %><br/><a href="home.jsp">返回我的家园</a><br/><%
}
}%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>