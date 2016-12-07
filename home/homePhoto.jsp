<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.home.*,net.joycool.wap.service.impl.*,net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean,net.joycool.wap.cache.util.HomeCacheUtil,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendMarriageBean,net.joycool.wap.bean.PagingBean,net.joycool.wap.cache.OsCacheUtil" %>
<%! static HomeServiceImpl homeService = new HomeServiceImpl();
    static int COUNT_PRE_PAGE = 5;%>
<%response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
String tip = "";
String otherTitle = "";
boolean isMySelf = false;
int coupleUid = 0;
UserBean user = null;
HomePhotoBean photo = null;
HomeUserBean homeUser = null;
HomeUserBean coupleUser = null;
HomePhotoCat photoCat = null;
List list = null;
PagingBean paging = null;
int backFlag = 0;
int catId = action.getParameterInt("cid");
int uid = action.getParameterInt("uid");
if (uid <= 0){
	uid = action.getParameterInt("userId");
}
// 修改分类
//if (action.getParameterInt("e") == 1){
//	action.homePhotoCat();
//	String msg = request.getAttribute("msg")==null?"":request.getAttribute("msg").toString();
//	if (request.getAttribute("catId") != null || (!"".equals(msg))){
//		catId = request.getAttribute("catId")!=null?Integer.valueOf(request.getAttribute("catId").toString()).intValue():0;
//		backFlag = 2;
//		tip = msg;
//	} else {
//		catId = 0;
//		tip = "分类不存在.";
//		backFlag = 0;
//	}
//}
// 删除相册
int delId = action.getParameterInt("d");
int sure = action.getParameterInt("s");
if (delId == 1){
	if (sure == 1){
		action.deletePhoto(request);
		tip = "删除成功";
		backFlag = 2;
	} else {
		tip = "del";
		backFlag = 1;
	}
}
//int del = action.getParameterInt("d2");
//int submit = action.getParameterInt("s2");
//if (del == 1){
//	if (submit == 1){
//		homeService.deleteHomePhotoCat(catId,action.getLoginUser().getId());
//		homeUser = HomeCacheUtil.getHomeCache(action.getLoginUser().getId());
//		if (homeUser != null){
//			homeUser.setPhotoCatCount(homeUser.getPhotoCatCount()>0?homeUser.getPhotoCatCount()-1:0);
//			OsCacheUtil.put(HomeCacheUtil.getKey(action.getLoginUser().getId()),homeUser,OsCacheUtil.HOME_USER_CACHE_GROUP);
//		}
//		tip  = "删除成功.";
//		backFlag = 5;
//	} else {
//		backFlag = 4;
//		tip = "1";
//	}
//}
if (action.getParameterInt("a") == 1){
	session.setAttribute("diaryList",new ArrayList());
	action.diaryList(request);
	if ("addSuccess".equals(request.getAttribute("result"))){
		tip = (String)request.getAttribute("tip");
		backFlag = 0;
	}
}
if (catId<0){
	tip = "分类不存在.";
	backFlag = 0;
}
if (uid <= 0){
	uid = action.getLoginUser().getId();
	isMySelf = true;
} else {
	if (uid == action.getLoginUser().getId()){isMySelf = true;}else{isMySelf = false;}
}
user = UserInfoUtil.getUser(uid);
if (user == null){
	tip = "用户不存在";
} else {
	homeUser = HomeCacheUtil.getHomeCache(uid);			//"我"的家园用户信息
	// 取得"另一伴"的UID
	coupleUid = action.getCoupleUid(uid);
	// 取得特殊的标题
	if (coupleUid <= 0 || homeUser == null){
		otherTitle = user.getNickNameWml();		
	} else {
		otherTitle = action.getOtherTitle(homeUser,coupleUid);	
	}
	if (homeUser == null){
		if (uid==action.getLoginUser().getId()){tip = "您";} else {tip = otherTitle;}
		tip += "还没有建立家园.";
	} else {
		// 取得"另一伴"的UID
		coupleUid = action.getCoupleUid(uid);
		// 取得特殊的标题
		otherTitle = action.getOtherTitle(homeUser,coupleUid);
		// 传来的catId是否属于user
		if (catId > 0){
			photoCat = homeService.getHomePhotoCat(catId);
			if (photoCat == null){
				tip = "该分类不存在.";
			} else if (photoCat.getUid() != uid){
				tip = "不是此用户的分类.";
			} else if (uid != action.getLoginUser().getId()){
				if (photoCat.getPrivacy() == HomeDiaryCat.PRIVACY_SELF){
					tip = "由于隐私设置,您不能浏览.";
					backFlag = 3;
				} else if(photoCat.getPrivacy() == HomeDiaryCat.PRIVACY_FRIEND){
					ArrayList userFriends = UserInfoUtil.getUserFriends(uid);
					if (!userFriends.contains(action.getLoginUser().getId() + "")){
						tip = "由于隐私设置,您不能浏览.";
						backFlag = 3;
					}
				}
			}
		} else if (catId == 0){
			photoCat = new HomePhotoCat();
			photoCat.setCatName("默认分类");
			photoCat.setUid(homeUser.getUserId());
		}
		if ("".equals(tip)){
			// 推荐
			int recommended = action.getParameterInt("r");
			if (recommended > 0){
				photo = homeService.getHomePhoto(" id=" + recommended);
				if (photo != null){
					if (photo.getUserId() != action.getLoginUser().getId()){
						tip = "不是你的照片.";
					} else {
						// 更新换代数据库
						SqlUtil.executeUpdate("update jc_home_user set recommended=" + recommended + " where user_id=" + action.getLoginUser().getId(),0);
						// 更新缓存
						HomeUserBean homeUser2 = HomeCacheUtil.getHomeCache(action.getLoginUser().getId());
						homeUser.setRecommended(recommended);
//						OsCacheUtil.put(HomeCacheUtil.getKey(action.getLoginUser().getId()),homeUser,OsCacheUtil.HOME_USER_CACHE_GROUP);
						tip = "推荐成功.";
					}
				} else {
					tip = "照片不存在.";
				}
			}
			list = homeService.getHomePhotoList(" cat_id=" + catId + " and user_id=" + uid + " order by id desc");
			paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="相册"><p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=isMySelf?"我":user.getNickNameWml()%>的相册><%=photoCat==null?"":StringUtil.toWml(photoCat.getCatName())%><br/>
<%if(isMySelf){%><a href="addPhoto.jsp?cid=<%=catId%>">加图片</a><%if(photoCat != null && catId != 0){%>|<a href="set.jsp?cid=<%=catId%>&amp;t=1">设置|删除</a><%}%><br/><%} %>
<%for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	photo = (HomePhotoBean)list.get(i);
	if (photo != null){
		%><%=i+1%>.<%=StringUtil.toWml(photo.getTitle())%>(阅<%=photo.getHits()%>|<a href="/home/photoReview.jsp?photoId=<%=photo.getId()%>&amp;cid=<%=catId %><%=uid != action.getLoginUser().getId()?"&amp;uid=" + uid:"" %>">评<%=photo.getReviewCount()%></a>)<%if(isMySelf){%><a href="homePhoto.jsp?r=<%=photo.getId()%>&amp;cid=<%=catId %>">荐</a>|<a href="homePhoto.jsp?d=1&amp;photoId=<%=photo.getId()%>&amp;cid=<%=catId%>">删</a><%}%><br/>
		<img src="<%=Constants.IMG_ROOT_URL%><%=photo.getAttach()%>" alt="loading..."/><br/><%
	}
}%><%=paging.shuzifenye("homePhoto.jsp?cid=" + catId + (isMySelf?"":"&amp;uid=" + uid),true,"|",response)%><%
if (isMySelf){
%>
<a href="homePhotoCat.jsp" >返回我的相册首页</a><br/>
<%
} else {
%><a href="homePhotoCat.jsp?uid=<%=uid %>">返回<%=user.getNickNameWml()%>的相册首页</a><br/><%	
}
%><a href="home.jsp" >返回我的家园</a><br/><%
} else {
if (backFlag == 0){
// 一般性返回
%><%=tip%><br/><%=uid!=action.getLoginUser().getId()?"<a href=\"homePhotoCat.jsp?uid=" + uid + "\">返回" + otherTitle +"的相册首页</a><br/>":"" %><a href="homePhotoCat.jsp">返回我的相册首页</a><br/><%	
} else if (backFlag == 1){
// 是否删除?
%><a href="homePhoto.jsp?d=1&amp;photoId=<%=action.getParameterInt("photoId") %>&amp;cid=<%=catId %>&amp;s=1">确认删除这张相片</a><br/><a href="homePhoto.jsp?cid=<%=catId %>">不删除了,返回</a><br/><%		
} else if (backFlag == 2){
// 删除后的返回
%><%=tip%><br/><%
if (isMySelf){%><a href="homePhoto.jsp?cid=<%=catId%>">返回</a><br/><%}else{%><a href="homePhoto.jsp?uid=<%=uid%>&amp;cid=<%=catId%>">返回</a><br/><%}
} else if (backFlag == 3){
// 因为隐私而不可浏览
%><%=tip %><br/><a href="home2.jsp?uid=<%=uid%>">返回<%=user.getNickNameWml()%>的家园</a><br/><a href="home.jsp">返回我的家园</a><br/><%
} else if (backFlag == 4){
// 删除确认	
%>删除分类后,该分类下的相册也将被删除.确认删除吗?<br/><a href="homePhoto.jsp?d2=1&amp;s2=1&amp;cid=<%=catId %>">删除</a><br/><a href="homePhoto.jsp?cid=<%=catId %>">返回</a><br/><%
} else if (backFlag == 5){
// 删除成功
%>删除成功.<br/><a href="homePhotoCat.jsp">返回</a><br/><%
}
}
%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>