<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.action.friend.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.bean.friend.*"%><%
	response.setHeader("Cache-Control","no-cache");
	CustomAction customAction = new CustomAction(request);
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	int uid = user.getId();
	int coupleUid = 0;
	int viewCouple = customAction.getParameterInt("vc");
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	HomeUserBean homeUser = null;
	int totalCount = 0;
	PagingBean paging = null;
	List trendList = null;
	String tip  = "";
	FriendMarriageBean marriageBean = null;
	// 是否查看老公(老婆)的动态
	if (viewCouple == 1){
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
			if (homeUser == null){
				tip = "TA还没有建立家园.";
			} else {
				trendList =  homeUser.getTrend();
				paging = new PagingBean(action, trendList.size(), 10, "p");
			}
		} else {
			tip = "您还没结婚呢，没有TA的动态...";
		}
	} else {
		// 查看我好友的动态
		totalCount = serviceTrend.getCountFriendTrendByUid(uid);	
		paging = new PagingBean(customAction, totalCount, 10, "p");
		trendList = serviceTrend.getFriendTrendIdByUid(uid, paging.getStartIndex(), 10);
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="好友动态">
	<p>
	<%=BaseAction.getTop(request, response)%>
	<%if ("".equals(tip)){
		if (viewCouple == 1){
			// 查看老公/老婆的动态
			user = UserInfoUtil.getUser(coupleUid);
			%>===<%if (user.getGender()==1){%>老公<%}else{%>老婆<%}%>的动态===<br/><%
			for (int i = paging.getStartIndex(); i < paging.getEndIndex();i++){
				BeanTrend trend = ServiceTrend.getInstance().getTrendById((Integer)trendList.get(i));
				%>*<%=trend.getContent(customAction.getLoginUser().getId(),response,uid,marriageBean)%><br/><%
			}%><%=paging.shuzifenye("friendTrend.jsp?vc=1",true,"|",response)%><%
		} else {
			// 查看好友的动态
			%>===好友动态===<br/><%
			for(int i = 0;i < trendList.size(); i ++) {
				BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)trendList.get(i)));
				%>*<%=trend.getContent(customAction.getLoginUser().getId(),response,uid,marriageBean) %><br/><%
			}%><%=paging.shuzifenye("friendTrend.jsp", false, "|", response)%><%
		}
	} else {
		%><%=tip%><br/><%
	}%>
	<a href="home.jsp">返回我的家园</a><br/>
	<%=BaseAction.getBottomShort(request, response)%>
	</p>	
	</card>
</wml>