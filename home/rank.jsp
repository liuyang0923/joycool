<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="net.joycool.wap.spec.garden.*,java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.user.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.action.home.HomeAction,net.joycool.wap.bean.friend.FriendBean"%><%@ page import="net.joycool.wap.spec.friend.*"%><%!
static IHomeService homeService =ServiceFactory.createHomeService();static final int COUNT_PRE_PAGE=10;%><%
	response.setHeader("Cache-Control","no-cache");
	HomeAction homeAction = new HomeAction(request);
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	String tip = "";
	
	if(user == null) {
		response.sendRedirect("/");
		return;
	}
	int uid = user.getId();//LoginUser.getId();
	
	HomePlayer player = homeService.getPlayer(" user_id = " + uid);
	
	// 更新排行
	homeAction.updateRank();
	// 总数是不是要写死呢？
	int totalCount = SqlUtil.getIntResult("select count(id) from jc_home_player_rank", 0);
	PagingBean paging = new PagingBean(homeAction,totalCount,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
	ArrayList rankList = (ArrayList)homeService.getRankList("1 limit " + (pageNow * COUNT_PRE_PAGE) + "," + COUNT_PRE_PAGE);
	int myRank = SqlUtil.getIntResult("select id from jc_home_player_rank where user_id =" + uid,0);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="家园">
<p>
<%=BaseAction.getTop(request, response)%>
某选美比赛排名<br/>
<% if ("".equals(tip)){
if(myRank > 0){
	%>你当前排名:<%=myRank%><br/><%
}
%>==姓名/票数==<br/><%
		if (rankList.size() > 0){
			HomePlayerRank rank = null;
			for (int i = 0; i < rankList.size() ; i++){
				rank = (HomePlayerRank)rankList.get(i);
				user = UserInfoUtil.getUser(rank.getUserId());
				%><%=rank.getId()%>.<a href="/user/ViewUserInfo.do?userId=<%=rank.getUserId()%>"><%=user.getNickNameWml()%></a>/<%=rank.getVoteCount()%><br/><%
			}%><%=paging.shuzifenye("rank.jsp", false, "|", response)%>
<a href="home.jsp">>>返回我的家园</a><br/><%
		}
}else{
	%><%=tip%><br/><a href="home.jsp">返回</a><br/><%
}
%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>