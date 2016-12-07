<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.action.friend.*,net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*"%><%
	response.setHeader("Cache-Control","no-cache");
	CustomAction customAction = new CustomAction(request);
	int uid = customAction.getParameterInt("uid");
	UserBean user = UserInfoUtil.getUser(uid);
	int viewCouple = customAction.getParameterInt("vc");
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	HomeUserBean homeUser = null;
	int coupleUid = 0;
	PagingBean paging = null;
	String tip = "";
	String title = user==null?"":user.getNickNameWml();
	List list = null;
	FriendMarriageBean marriageBean = null;
	if (user == null){
		response.sendRedirect("home.jsp");
		return;
	}
	if (viewCouple == 1){
		// 是否查看老公(老婆)的动态
		// 是否有老公(老婆)
		FriendAction action = new FriendAction(request);
		marriageBean = action.infoMarriage(uid);
		if (marriageBean != null){
			// 得到另一半的用户ID
			if (marriageBean.getFromId() == uid){
				coupleUid = marriageBean.getToId();
			} else {
				coupleUid = marriageBean.getFromId();
			}
			homeUser = HomeCacheUtil.getHomeCache(coupleUid);
			UserBean coupleUser = UserInfoUtil.getUser(coupleUid);
			if (homeUser == null){
				tip = (coupleUid==action.getLoginUser().getId()?"你":coupleUser.getGenderText()) + "还没有建立家园.";
			} else {
				if (homeUser.getUserId() == loginUser.getId()){
					title = UserInfoUtil.getUser(uid).getGender()==0?"老公":"老婆";
				} else if (loginUser.getId() != uid && coupleUid != uid){
					title = homeUser.getGender()==0?"他老婆":"她老公";
				} else {
					title = homeUser.getGender()==0?"老公":"老婆";	
				}
				list =  homeUser.getTrend();
				paging = new PagingBean(action, list.size(), 10, "p");
			}
		} else {
			tip = "TA还没结婚呢，没有另一半的动态...";
		}
	} else {
		homeUser = HomeCacheUtil.getHomeCache(uid);
		if (homeUser == null){
			tip = "TA还没有建立家园.";
		} else {
			list = homeUser.getTrend();
			paging = new PagingBean(customAction, list.size(), 10, "p");
		}

	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="家园动态">
	<p>
	<%=BaseAction.getTop(request, response)%>
	<a href="<%=("home2.jsp?uid="+uid) %>"><%=user.getNickNameWml()%>的家园</a>|<a href="<%=("home.jsp") %>">回家</a><br/>
<% if ("".equals(tip)){
%>【<%=title%>的动态】<br/><%
		if (viewCouple == 1){
			// 查看老公/老婆的动态
			user = UserInfoUtil.getUser(coupleUid);
			for (int i = paging.getStartIndex(); i < paging.getEndIndex();i++){
				BeanTrend trend = ServiceTrend.getInstance().getTrendById((Integer)list.get(i));
				%>*<%=trend.getContent(loginUser.getId(), response,uid,marriageBean)%><br/><%
			}%><%=paging.shuzifenye("userTrend.jsp?vc=1&amp;uid=" + uid,true,"|",response)%><%
		} else {
			for(int i = paging.getStartIndex();i < paging.getEndIndex(); i ++) {
				BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)homeUser.getTrend().get(i)));
				%>*<%=trend.getContent(loginUser.getId(), response,uid,marriageBean)%><br/>
			<%} %><%=paging.shuzifenye("userTrend.jsp?uid="+uid, true, "|", response)%>
<%
		}
} else {
	%><%=tip%><br/><%
}%>
	<a href="<%=("home.jsp") %>">回家</a>
	<br/>
	<%=BaseAction.getBottomShort(request, response)%>
	</p>
	</card>
</wml>