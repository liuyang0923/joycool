<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.home.*,net.joycool.wap.service.impl.*,net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.*,net.joycool.wap.cache.OsCacheUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean,net.joycool.wap.cache.util.HomeCacheUtil,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendMarriageBean" %>
<%! static HomeServiceImpl homeServiceImpl = new HomeServiceImpl(); %>
<%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
String tip = "";
int backFlag = 0;
String catName = "";
int type = action.getParameterInt("t");		// type=0,设置日记权限;type=1,设置照片权限
if (type < 0 || type > 1){type = 0;}
int catId = action.getParameterInt("cid");
HomeUserBean homeUser = null;
HomeDiaryCat diaryCat = homeServiceImpl.getHomeDiaryCat("id=" + catId);
HomePhotoCat photoCat = homeServiceImpl.getHomePhotoCat("id=" + catId);
if (type == 0){
	if (diaryCat == null || diaryCat.getUid() != action.getLoginUser().getId()){
		tip = "分类不存在或不是你建立的分类.";
	} else if (diaryCat.getDef() == 1){
		tip = "默认分类不可以修改.";
	} else {
		catName = StringUtil.toWml(diaryCat.getCatName());
	}
} else {
	if (photoCat == null || photoCat.getUid() != action.getLoginUser().getId()){
		tip = "分类不存在或不是你建立的分类.";
	} else if (photoCat.getDef() == 1){
		tip = "默认分类不可以修改.";
	} else {
		catName = StringUtil.toWml(photoCat.getCatName());
	}
}
if ("".equals(tip)){
	int del = action.getParameterInt("d");
	int submit2 = action.getParameterInt("s2");
	// 删除分类
	if (del == 1){
		if (submit2 == 1){
			boolean result = false;
			if (type == 0){
				if (diaryCat.getUid() == action.getLoginUser().getId()){
					result = homeServiceImpl.deleteHomeDiaryCat(catId,action.getLoginUser().getId());
					result = SqlUtil.executeUpdate("update jc_home_user set diary_count=diary_count-" + diaryCat.getCount() + " where diary_count>0 and user_id=" + diaryCat.getUid(),0);
				} else {
					tip = "无法删除他人的分类.";
				}
			} else {
				if (photoCat.getUid() == action.getLoginUser().getId()){
					result = homeServiceImpl.deleteHomePhotoCat(catId,action.getLoginUser().getId());
					result = SqlUtil.executeUpdate("update jc_home_user set photo_count=photo_count-" + photoCat.getCount() + " where photo_count>0 and user_id=" + photoCat.getUid(),0);
				} else {
					tip = "无法删除他人的分类.";
				}
			}
			if (result){
				homeUser = HomeCacheUtil.getHomeCache(action.getLoginUser().getId());
				if (homeUser != null){
					if (type == 0){
						if (homeUser.getDiaryCount() > 0){
							homeUser.setDiaryCount(homeUser.getDiaryCount() - diaryCat.getCount());
						}
						homeUser.setDiaryCatCount(homeUser.getDiaryCatCount()>0?homeUser.getDiaryCatCount()-1:0);
					} else {
						if (homeUser.getPhotoCount() > 0){
							homeUser.setPhotoCount(homeUser.getPhotoCount() - photoCat.getCount());
						}
						homeUser.setPhotoCatCount(homeUser.getPhotoCatCount()>0?homeUser.getPhotoCatCount()-1:0);
					}
//					OsCacheUtil.put(HomeCacheUtil.getKey(action.getLoginUser().getId()),homeUser,OsCacheUtil.HOME_USER_CACHE_GROUP);
				}
				tip  = "删除成功.";
			} else {
				tip = "删除失败.";
			}
			backFlag = 3;
		} else {
			backFlag = 2;
			tip = "删除分类后,该分类下的";
			if (type == 0){
				tip += "日记";
			} else {
				tip += "相册";
			}
			tip += "也将被删除.确认删除吗?";
		}
	}
}
int submit = action.getParameterInt("s");
if (submit == 1){
	if (type == 0){
		// 修改日记权限
		action.homeDiaryCat();
		String msg = request.getAttribute("msg")==null?"":request.getAttribute("msg").toString();
		if (request.getAttribute("catId") != null || (!"".equals(msg))){
			catId = request.getAttribute("catId")!=null?Integer.valueOf(request.getAttribute("catId").toString()).intValue():0;
			tip = msg;
		} else {
			catId = 0;
		}
	} else {
		// 修改照片权限
		action.homePhotoCat();
		String msg = request.getAttribute("msg")==null?"":request.getAttribute("msg").toString();
		if (request.getAttribute("catId") != null || (!"".equals(msg))){
			catId = request.getAttribute("catId")!=null?Integer.valueOf(request.getAttribute("catId").toString()).intValue():0;
			tip = msg;
		} else {
			catId = 0;
			tip = "分类不存在.";
		}
		backFlag = 1;
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="设置"><p align="left">
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%><%=catName%>>设置<br/>
当前状态是:<select name="privacy" value="<%=type==0?diaryCat.getPrivacy():photoCat.getPrivacy()%>">
<option value="<%=type==0?HomeDiaryCat.PRIVACY_ALL:HomePhotoCat.PRIVACY_ALL%>">所人有可见</option>
<option value="<%=type==0?HomeDiaryCat.PRIVACY_FRIEND:HomePhotoCat.PRIVACY_FRIEND%>">仅好友可见</option>
<option value="<%=type==0?HomeDiaryCat.PRIVACY_SELF:HomePhotoCat.PRIVACY_SELF%>">隐藏</option>
</select><br/>
修改<%=type==0?"日记":"相册"%>分类名称:<input name="title"  maxlength="10" value=""/><br/>
<anchor title="确定">确认修改
    <go href="set.jsp?s=1&amp;t=<%=type%>" method="post">
    <postfield name="action" value="e"/>
    <postfield name="id" value="<%=catId %>"/>
    <postfield name="privacy" value="$privacy"/>
    <postfield name="title" value="$title"/>
    </go>
</anchor><br/>
<a href="<%=type==0?"homeDiaryList.jsp":"homePhoto.jsp" %>?cid=<%=catId%>">返回<%=catName%></a><br/>
<a href="set.jsp?t=<%=type%>&amp;d=1&amp;cid=<%=catId%>">!删除分类:<%=catName%></a><br/><%
} else {
%><%=tip%><br/>
<%
if (backFlag == 0){
// 返回日记
%><a href="homeDiaryList.jsp?cid=<%=catId%>">返回</a><br/><%
} else if (backFlag == 1){
// 返回相册
%><a href="homePhoto.jsp?cid=<%=catId%>">返回</a><br/><%	
} else if (backFlag == 2){
// 删除分类确认
%><a href="set.jsp?t=<%=type%>&amp;d=1&amp;s2=1&amp;cid=<%=catId %>">删除</a><br/><a href="set.jsp?t=<%=type%>&amp;cid=<%=catId%>">返回</a><br/><%
} else if (backFlag == 3){
// 成功删除
%><a href="<%=type==0?"homeDiaryCat.jsp":"homePhotoCat.jsp"%>">返回</a><br/><%
}
}
%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>