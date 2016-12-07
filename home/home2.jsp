<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*,net.joycool.wap.action.user.*,net.joycool.wap.action.friend.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.spec.friend.*"%><%!
static ServiceVisit serviceVisit = ServiceVisit.getInstance();
static net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();
static IHomeService homeService =ServiceFactory.createHomeService();%><%
	response.setHeader("Cache-Control","no-cache");
	int uid = 0;
	if(request.getParameter("uid") != null) {
		uid = Integer.parseInt(request.getParameter("uid"));
	} else if((request.getParameter("userId") != null)) {
		uid = Integer.parseInt(request.getParameter("userId"));
	}
	int backId = 0;
	if (request.getParameter("b") != null){
		backId = Integer.parseInt(request.getParameter("b"));
	}
	//session.setAttribute("userId", new Integer(uid));
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	if(loginUser != null) {
		if(uid == 0) {
			response.sendRedirect("home.jsp");
			return;
		}
	} else {
		request.getRequestDispatcher("/home/guestHome.jsp?userId=" + uid).forward(request,response);
		return;
	}
	//int friendCount = service.getFriendCount("user_id = " + uid);
	UserBean user = UserInfoUtil.getUser(uid);
	if (user == null){
		response.sendRedirect("home.jsp");
		return;
	}
	// 结婚没？
	FriendAction action = new FriendAction(request);
	FriendMarriageBean marriageBean = action.infoMarriage(uid);
	HomePhotoBean photo = null;
	int coupleUid = 0;
	if (marriageBean == null){
		// 没结婚?那就该回谁家回谁家吧
		if (user.getGender() == 0){
			response.sendRedirect("fehome2.jsp?userId=" + uid);
			return;
		}
	} else {
		// 用户ID与自己的ID相等，返回自己的夫妻家园
		if (user.getId() == loginUser.getId()){
			response.sendRedirect("couple.jsp");
			return;
		} 
		if (loginUser.getId() != marriageBean.getFromId() && loginUser.getId() != marriageBean.getToId()){
			response.sendRedirect("couple2.jsp?userId=" + uid);
			return;
		}
		// 如果ID是女的，跳到女性的家园
		if (user.getGender()==0){
			response.sendRedirect("fehome2.jsp?userId=" + uid);
			return;
		}
		if (marriageBean.getFromId() == uid){
			coupleUid = marriageBean.getToId();
		} else {
			coupleUid = marriageBean.getFromId();
		}
		//response.sendRedirect("couple2.jsp?userId=" + uid);
		//return;
	}
	//int photoCount = homeService.getHomePhotoCount("user_id = " + uid);
	//int diaryCount = homeService.getHomeDiaryCount("user_id =" + uid + " and del=0");
	String diaryCondition = "user_id = " + uid + " and del=0 order by id desc limit 0, 3";
	Vector diaryList = homeService.getHomeDiaryList(diaryCondition);
	//int forumCount = SqlUtil.getIntResult("select count(id) from jc_forum_content where user_id = " + StringUtil.trim(""+uid));

	Vector homeReviewList = homeService.getHomeReviewList("user_id=" + uid + " order by id desc limit 0, 3");
	
	boolean flag = true;
	
	if(loginUser != null&&loginUser.getId()!=uid) {
		ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
		flag = userFriends.contains(uid + "");
	}
	
	
	//最近访问记录人数
	int visitCount = serviceVisit.getCountVisitByToUid(uid);

	//自动建立家园
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(uid);
	HomeUserBean coupleHomeUser = HomeCacheUtil.getHomeCache(coupleUid);
	if(homeUser == null) {
/*		homeUser = new HomeUserBean();
		homeUser.setUserId(uid);
		homeUser.setName(user.getNickName());
		homeUser.setMobile("");
		homeUser.setCity("未知");
		homeUser.setConstellation(0);
		homeUser.setHeight(0);
		homeUser.setWeight(0);
		homeUser.setWork("未知");
		homeUser.setPersonality(0);
		homeUser.setMarriage(0);
		homeUser.setAim(0);
		homeUser.setGender(user.getGender());
		homeUser.setAge(user.getAge());
		homeUser.setFriendCondition("未知");
		HomeCacheUtil.addHomeCache(homeUser);
		
		UserInfoUtil.updateUser("home=" + Constants.USER_INFO_HOME_MARK, "id="
				+ user.getId(), user.getId() + "");
		// 更新用户session中的个人家园标志位
		// 注册个人家园赠送50点经验值
		RankAction.addPoint(user, Constants.REGISTER_HOME_POINT);
		user.setHome(Constants.USER_INFO_HOME_MARK);
		
		HomeUserImageBean userImage = new HomeUserImageBean();
		userImage.setUserId(uid);
		userImage.setImageId(Constants.HOME_HOUSE_INIT_BACKGROUND);
		userImage.setTypeId(Constants.HOME_IMAGE_BACKGROUND_TYPE);
		userImage.setHomeId(1);
		homeService.addHomeUserImage(userImage);*/
		response.sendRedirect("viewAllHome.jsp");
		return;
	}
	boolean isABadGuys=service.isUserBadGuy(uid,loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(user.getNickName())%>的家园">
<p>
<%if(isABadGuys){%>
你已经被对方加入黑名单,无法进入对方的家园.<br/>
<%}else{
session.setAttribute("homeReview","homeReview");
%><%=BaseAction.getTop(request, response)%>
<%if (marriageBean != null && (marriageBean.getFromId() == uid || marriageBean.getToId() == uid)){
%>老公<%	
} else {
%><a href="<%=("/user/ViewUserInfo.do?userId="+uid)%>"><%=StringUtil.toWml(user.getNickName())%></a><%	
}%>的家园|<%if (backId == 1){%><a href="couple.jsp">回家</a><%}else{%><a href="home.jsp">回家</a><%}%><br/>
<a href="homeDiaryCat.jsp?uid=<%=uid%>">日记</a><%if(homeUser.getDiaryCount() > 0) { %><%=homeUser.getDiaryCount()%><%}%>.<a href="homePhotoCat.jsp?uid=<%=uid %>">相册</a><%if(homeUser.getPhotoCount() > 0) { %><%=homeUser.getPhotoCount() %><%}%>.<a href="<%=("house.jsp?userId="+ uid +"&amp;in=1")%>">家</a><br/>
<%if (homeUser != null & homeUser.getRecommended() > 0){
photo = homeService.getHomePhoto(" id=" + homeUser.getRecommended());
if (photo != null){
%>★★推荐照片★★<br/><a href="homePhotoCat.jsp?uid=<%=photo.getUserId()%>"><img src="/rep<%=photo.getAttach()%>" alt="o"/></a><br/><%	
}
}
// 得到老婆(老公)的推荐图片(如果TA有推荐的话)
if (coupleHomeUser != null){
	photo = homeService.getHomePhoto(" id=" + coupleHomeUser.getRecommended());
	if (photo != null){
		%>★★<%=user.getGender()==0?"老公":"老婆"%>照片★★<br/><a href="homePhotoCat.jsp?uid=<%=photo.getUserId()%>"><img src="/rep<%=photo.getAttach()%>" alt="o"/></a><br/><%
	}
}%>
<a href="<%=("/chat/post.jsp?roomId=0&amp;toUserId="+uid) %>">和<%=user.getGenderText() %>聊天</a>.<a href="<%="viewHomeUser.jsp?userId="+uid%>">查<%=user.getGenderText() %>档案</a><br/>
<a href="<%=("/team/question/his.jsp?uid="+uid) %>">缘分测试</a>.<a href="<%=("/jcforum/userTopic.jsp?u="+uid)%>"><%=user.getGenderText() %>的帖子</a><br/>
<a href="/team/teams.jsp?id=<%=uid%>"><%=user.getGenderText() %>的圈子</a>.<a href="homeNeighbor.jsp?userId=<%=uid%>"><%=user.getGenderText() %>的邻居</a><br/>	
<a href="../friend/astro/index.jsp?uid=<%=uid%>">星座配对</a>.<a href="mf.jsp?uid=<%=uid%>">与他交友</a><br/>	
<%if(!flag) {%>
<a href="<%=("/user/OperFriend.do?add=1&amp;friendId="+uid) %>">加为好友</a><br/><%} %>
【最新动态】<% List trendList = homeUser.getTrend();
if(trendList.size() > 5) {%><a href="<%=("userTrend.jsp?uid=" + uid) %>">全部</a><%} else if(trendList.size() == 0) {%>暂无<%} %><br/>
	<%
		for(int i = 0;i < trendList.size(); i ++) {
			if(i >= 5) {
				break;
			}
			BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)homeUser.getTrend().get(i)));
	%>
*<%=trend.getContent((loginUser == null?0:loginUser.getId()), response,uid,null)%><br/>
<%} %>
【日记】<%if(homeUser.getDiaryCount() > 0) { %><%=homeUser.getDiaryCount()%>篇.<a href="<%=("homeDiaryCat.jsp?userId="+uid) %>">更多</a><%} else {%>暂无<%} %><br/>
<%
		int j = 1;
		for(int i=0;i<diaryList.size();i++){
			
			HomeDiaryBean homeDiary=(HomeDiaryBean)diaryList.get(i);
	%>
<%=j%>.<a href="<%=("homeDiary.jsp?userId="+user.getId()+"&amp;diaryId="+homeDiary.getId())%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>(阅<%=homeDiary.getHits()%>|回<%=homeDiary.getReviewCount()%>)<br/>
<%
			j++;
		}
	%>
<a href="<%=("house.jsp?userId="+ uid +"&amp;in=1")%>"><img src="<%=("/img/home/house/"+homeUser.getTypeId()+".gif")%>" alt="家"/></a><br/>	
【最近访问】<%=visitCount %>人<br/>
<%
		Iterator iterator = homeUser.getRecentVisit().iterator();
		int count = 0;
		
		while(iterator.hasNext()) {
			if(count >= 5)
				break;
			BeanVisit visit = (BeanVisit)iterator.next();
	%>
<a href="<%=("home2.jsp?uid=" + visit.getFromUid()) %>"><%=StringUtil.toWml(visit.getFromNickName()) %></a><%=visit.getVisitTime() %><br/>
<%
			count++;
		}
	%>
	
【留言】<%if(homeUser.getReviewCount()>0){%>
				<%=homeUser.getReviewCount()%>条<%if(homeUser.getReviewCount()>3){%><a href="<%=("homeReview.jsp?userId="+uid)%>">更多</a><%}
				}else {%>暂无<%} %><br/>
<%
		HomeReviewBean homeReview=null;
		for(int i=0;i<homeReviewList.size();i++){
			homeReview=(HomeReviewBean)homeReviewList.get(i);
			//liuyi 2006-12-14 start
			if(homeReview==null)continue;
			UserBean reviewUser = UserInfoUtil.getUser(homeReview.getReviewUserId());
			if(reviewUser==null)continue;
%>*<%=BeanVisit.converDateToBefore2(new Date(homeReview.getCreateTime())) %><%
			if(loginUser == null) {
		%>
<a href="<%=("home2.jsp?userId="+homeReview.getReviewUserId())%>"><%=StringUtil.toWml(reviewUser.getNickName())%></a>
<%
			} else if(reviewUser.getId()==loginUser.getId()){
			//	liuyi 2006-12-14 end
			%>
我
<%
				}else{
			%>
<a href="<%=("home2.jsp?userId="+homeReview.getReviewUserId())%>"><%=StringUtil.toWml(reviewUser.getNickName())%></a>
<%
				}
			%>
说:<%=StringUtil.toWml(StringUtil.limitString(homeReview.getReview(),30))%><br/>
<%
		}
	%>
<input name="review" maxlength="100" value=""/>
<anchor>发表评论
<go href="homeReview.jsp?userId=<%=uid%>" method="post">
  <postfield name="review" value="$review"/>
</go></anchor><br/>	
<%
	if(loginUser != null) {	
	%><a href="home.jsp">返回我的家园</a><br/><a href="viewAllHome.jsp">*家园之星*</a>

<%} %><br/><%
	//最近访客
	if(loginUser != null) {
		if(uid != loginUser.getId()) {
			BeanVisit visit = new BeanVisit();
			visit.setFromUid(loginUser.getId());
			visit.setFromNickName(loginUser.getNickName());
			visit.setToUid(uid);
			visit.setToNickName(user.getNickName());
			visit.setVisitTime(new Date());
			//if(homeUser.getRecentVisit().contains(visit)){
			if(homeUser.getRecentVisit().contains(visit)) {
				homeUser.getRecentVisit().remove(visit);
			} else {
				if(homeUser.getRecentVisit().size() >= 10) {
					homeUser.getRecentVisit().remove(9);
				}
			}
			homeUser.getRecentVisit().add(0, visit);
			serviceVisit.addVisit(visit);
		}
	}
	%>
<%}%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>