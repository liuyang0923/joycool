<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.home.*,net.joycool.wap.service.impl.*,net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean,net.joycool.wap.cache.util.HomeCacheUtil,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendMarriageBean,net.joycool.wap.bean.PagingBean,net.joycool.wap.cache.OsCacheUtil" %>
<%! static HomeServiceImpl homeService = new HomeServiceImpl();
    static int COUNT_PRE_PAGE = 10;%>
<%response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
String tip = "";
String otherTitle = "";
boolean isMySelf = false;
int coupleUid = 0;
UserBean user = null;
HomeDiaryBean diary = null;
HomeUserBean homeUser = null;
HomeUserBean coupleUser = null;
HomeDiaryCat diaryCat = null;
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
//	action.homeDiaryCat();
//	String msg = request.getAttribute("msg")==null?"":request.getAttribute("msg").toString();
//	if (request.getAttribute("catId") != null || (!"".equals(msg))){
//		catId = request.getAttribute("catId")!=null?Integer.valueOf(request.getAttribute("catId").toString()).intValue():0;
//		backFlag = 2;
//		tip = msg;
//	} else {
//		catId = 0;
//	}
//}
// 删除日记
int delId = action.getParameterInt("d");
int sure = action.getParameterInt("s");
if (delId > 0){
	if (sure == 1){
		if (action.deleteHomeDiary(delId,uid)){
			tip = "删除成功";
			backFlag = 2;
		}
	} else {
		tip = "del";
		backFlag = 1;
	}
}
//int del = action.getParameterInt("d2");
//int submit = action.getParameterInt("s2");
// 删除分类
//if (del == 1){
//	if (submit == 1){
//		homeService.deleteHomeDiaryCat(catId,action.getLoginUser().getId());
//		homeUser = HomeCacheUtil.getHomeCache(action.getLoginUser().getId());
//		if (homeUser != null){
//			homeUser.setDiaryCatCount(homeUser.getDiaryCatCount()>0?homeUser.getDiaryCatCount()-1:0);
//			OsCacheUtil.put(HomeCacheUtil.getKey(action.getLoginUser().getId()),homeUser,OsCacheUtil.HOME_USER_CACHE_GROUP);
//		}
//		tip  = "删除成功.";
//		backFlag = 5;
//	} else {
//		backFlag = 4;
//		tip = "1";
//	}
//}
//if (action.getParameterInt("a") == 1){
//	session.setAttribute("diaryList",new ArrayList());
//	action.diaryList(request);
//	if ("addSuccess".equals(request.getAttribute("result"))){
//		tip = (String)request.getAttribute("tip");
//		backFlag = 0;
//	}
//}
if (uid <= 0){
	uid = action.getLoginUser().getId();
	isMySelf = true;
} else {
	if (uid == action.getLoginUser().getId()){isMySelf = true;}else{isMySelf = false;}
}
if (catId < 0){
	tip = "分类不存在.";
	backFlag = 0;
} 
user = UserInfoUtil.getUser(uid);
if (user == null){
	tip = "用户不存在";
} else {
	homeUser = HomeCacheUtil.getHomeCache(uid);			//"我"的家园用户信息
	if (homeUser == null){
		if (uid==action.getLoginUser().getId()){tip = "您";} else {tip = "TA";}
		tip += "还没有建立家园.";
	} else {
		// 取得"另一伴"的UID
		coupleUid = action.getCoupleUid(uid);
		// 取得特殊的标题
		otherTitle = action.getOtherTitle(homeUser,coupleUid);
		// 传来的catId是否属于user
		if (catId > 0){
			diaryCat = homeService.getHomeDiaryCat(catId);
			if (diaryCat == null){
				tip = "该分类不存在.";
			} else if (diaryCat.getUid() != uid){
				tip = "不是此用户的分类.";
			}  else if (uid != action.getLoginUser().getId()){
				if (diaryCat.getPrivacy() == HomeDiaryCat.PRIVACY_SELF){
					tip = "由于隐私设置,您不能浏览.";
					backFlag = 3;
				} else if(diaryCat.getPrivacy() == HomeDiaryCat.PRIVACY_FRIEND){
					ArrayList userFriends = UserInfoUtil.getUserFriends(uid);
					if (!userFriends.contains(action.getLoginUser().getId() + "")){
						tip = "由于隐私设置,您不能浏览.";
						backFlag = 3;
					}
				}
			}
		} else if (catId == 0){
			diaryCat = new HomeDiaryCat();
			diaryCat.setCatName("默认分类");
			diaryCat.setUid(homeUser.getUserId());
		}
		if ("".equals(tip)){
			list = homeService.getHomeDiaryList(" cat_id=" + catId + " and user_id=" + uid + " and del=0 order by id desc");
			paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="日记"><p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=isMySelf?"我":user.getNickNameWml()%>的日记><%=diaryCat==null?"":StringUtil.toWml(diaryCat.getCatName())%><br/>
<%if(isMySelf){%><a href="add.jsp?cid=<%=catId%>">写日记</a><%if(diaryCat != null && catId != 0){%>|<a href="set.jsp?cid=<%=catId%>">设置|删除</a><%}%><br/><%}%>
<%for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	diary = (HomeDiaryBean)list.get(i);
	if (diary != null){
		%><%=i+1%>.<a href="homeDiary.jsp?diaryId=<%=diary.getId()%><%=isMySelf?"":"&amp;userId=" + uid%>"><%=StringUtil.toWml(diary.getTitel())%></a>(回<%=diary.getReviewCount()%>/阅<%=diary.getHits()%>)<%=DateUtil.sformatTime(diary.getCreateTime())%><%if(isMySelf){%><a href="homeDiaryList.jsp?d=<%=diary.getId()%>&amp;cid=<%=catId %>&amp;uid=<%=uid%>">删</a><%}%><br/><%
	}
}%><%=paging.shuzifenye("homeDiaryList.jsp?cid=" + catId + (isMySelf?"":"&amp;uid=" + uid),true,"|",response)%><%
if (isMySelf){
%>
<a href="homeDiaryCat.jsp" >返回我的日记首页</a><br/>
<%
} else {
%><a href="homeDiaryCat.jsp?uid=<%=uid %>">返回<%=user.getNickNameWml()%>的日记首页</a><br/><%	
}
%><a href="home.jsp" >返回我的家园</a><br/><%
} else {
if (backFlag == 0){
// 一般性返回
%><%=tip%><br/><a href="homeDiaryCat.jsp">返回我的日记首页</a><br/><%	
} else if (backFlag == 1){
// 是否删除?
%><a href="homeDiaryList.jsp?d=<%=delId%>&amp;cid=<%=catId %>&amp;uid=<%=uid%>&amp;s=1">确认删除这篇日记</a><br/><a href="homeDiaryList.jsp?cid=<%=catId %>">不删除了,返回</a><br/><%		
} else if (backFlag == 2){
// 删除后的返回
%><%=tip%><br/><%
if (isMySelf){%><a href="homeDiaryList.jsp?cid=<%=catId%>">返回</a><br/><%}else{%><a href="homeDiaryList.jsp?uid=<%=uid%>&amp;cid=<%=catId%>">返回</a><br/><%}
} else if (backFlag == 3){
// 因为隐私而不可浏览
%><%=tip %><br/><a href="home2.jsp?uid=<%=uid%>">返回<%=user.getNickNameWml()%>的家园</a><br/><a href="home.jsp">返回我的家园</a><br/><%
} else if (backFlag == 4){
// 确认删除分类
%>删除分类后,该分类下的日记也将被删除.确认删除吗?<br/><a href="homeDiaryList.jsp?d2=1&amp;s2=1&amp;cid=<%=catId %>">删除</a><br/><a href="homeDiaryList.jsp?cid=<%=catId%>">返回</a><br/><%
} else if (backFlag == 5){
// 成功删除分类
%>删除成功.<br/><a href="homeDiaryCat.jsp">返回</a><br/><%
}
}
%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>