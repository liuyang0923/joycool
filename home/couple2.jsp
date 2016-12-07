<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="net.joycool.wap.spec.garden.*,java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.user.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.action.home.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.*,net.joycool.wap.cache.util.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.friend.*"%><%!
static IHomeService homeService =ServiceFactory.createHomeService();%><%
	response.setHeader("Cache-Control","no-cache");
	
	HomeAction homeAction = new HomeAction(request);
	net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	if(user == null) {
		response.sendRedirect("/");
		return;
	}
	int uid = user.getId();//LoginUser.getId();
	
	//int friendCount = service.getFriendCount("user_id = " + uid);
	//UserBean user = UserInfoUtil.getUser(uid);//LoginUser
	FriendAction action = new FriendAction(request);
	int coupleUid = 0;
	int userId = action.getParameterInt("userId");
	
	if (userId == uid){
		// 返回自己的家园
		response.sendRedirect("couple.jsp");
		return;
	}
	
	user = UserInfoUtil.getUser(userId);
	
	if (user == null){
		response.sendRedirect("home.jsp");
		return;
	}
	uid = user.getId();
	FriendMarriageBean marriageBean = action.infoMarriage(userId);
	if (marriageBean == null){
		// 没结婚?那就该回谁家回谁家吧
		if (user.getGender() == 0){
			response.sendRedirect("fehome.jsp");
			return;
		} else {
			response.sendRedirect("home.jsp");
			return;		
		}
	} else {
		// 得到另一半的用户ID
		if (marriageBean.getFromId() == userId){
			coupleUid = marriageBean.getToId();
		} else {
			coupleUid = marriageBean.getFromId();
		}
	}
	
//	HomePlayer player = homeService.getPlayer(" user_id = " + userId);
	HomePhotoBean photo = null;
	
	//UserInfoUtil.getUserFriends(loginUser.getId());
	//FriendBean friend = action.getFriendService().getFriend(uid);
	
	//if(friend== null){
		//friend = new FriendBean();
		//friend.setAttach("no");
		//friend.setGender(0);
		//friend.setAge(20);
		//friend.setCity("未知");
	//}
	
	//int photoCount = homeService.getHomePhotoCount("user_id = " + uid);
	
	//int diaryCount = homeService.getHomeDiaryCount("user_id =" + uid);
	
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(userId);
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
	
	String diaryCondition = "user_id = " + homeUser.getUserId() + " and del=0 order by id desc limit 0, 3";
	Vector diaryList = homeService.getHomeDiaryList(diaryCondition);
	
	//int forumCount = SqlUtil.getIntResult("select count(id) from jc_forum_content where user_id = " + StringUtil.trim(""+uid));

	Vector homeReviewList = homeService.getHomeReviewList("user_id=" + userId + " order by id desc limit 0, 3");
	
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	List trendList = serviceTrend.getFriendTrendIdByUid(uid, 0, 5);
	
	ServiceVisit serviceVisit = ServiceVisit.getInstance();
	//List visitList = serviceVisit.getVisitByToUid(uid, 0, 3);
	//int visitCount = serviceVisit.getCountVisitByToUid(uid);
		
	if(trendList.size()>0)
		user.latestTrend = ((Integer)trendList.get(0)).intValue();
	
	boolean isABadGuys=service.isUserBadGuy(userId,action.getLoginUser().getId());
	//int hits=SqlUtil.getIntResult("SELECT count(id) FROM jc_home_review where user_id="+user.getId(),Constants.DBShortName);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="家园">
<p>
<%=BaseAction.getTop(request, response)%>
<%if(isABadGuys){
%>你已经被对方加入黑名单,无法进入对方的家园.<br/><%	
} else {
	UserBean coupleUser = UserInfoUtil.getUser(coupleUid);
	if (coupleUser != null){
		%><%=user.getNickNameWml()%>的家园<br/><%
	}
	if (user.getGender() == 1){
		%>老公房间|<a href="couple2.jsp?userId=<%=coupleUser.getId()%>">老婆房间</a>|<a href="home.jsp">回家</a><br/><%
	} else {
		%><a href="couple2.jsp?userId=<%=coupleUser.getId()%>">老公房间</a>|老婆房间|<a href="home.jsp">回家</a><br/><%
	}%>
	<% if (marriageBean.getMarriageDatetime() != null){
	%>于<%=marriageBean.getMarriageDatetime().substring(0,10)%>结为夫妻<br/>
	<a href="/friend/review.jsp?marriageId=<%=marriageBean.getId()%>">>>观看结婚录像</a><br/><%	
	}%>
	<a href="homeDiaryCat.jsp?uid=<%=userId%>">日记</a><%if(homeUser.getDiaryCount() > 0) { %><%=homeUser.getDiaryCount()%><%}%>.<a href="homePhotoCat.jsp?uid=<%=userId%>">相册</a><%if(homeUser.getPhotoCount() > 0) { %><%=homeUser.getPhotoCount() %><%}%>.<a href="<%=("house.jsp?userId="+ userId +"&amp;in=1")%>">家</a><br/>
	<%if (jc.match.MatchAction.getMatchUser(userId)==null){
		%><a href="/friend/match/index.jsp">乐后选秀</a><br/><%
	} else {
		%><a href="/friend/match/vote.jsp?uid=<%=userId%>">为她投票</a><br/><%	
	}%>
	<%  // 检查user与coupleUser是否都城推荐了照片
	if ((homeUser != null && homeUser.getRecommended() > 0) || (coupleHomeUser != null && coupleHomeUser.getRecommended() > 0)){
	%>★★夫妻照片★★<br/>
	<%photo = homeService.getHomePhoto(" id=" + homeUser.getRecommended());
	if (photo != null){%><a href="homePhotoCat.jsp?uid=<%=photo.getUserId()%>"><img src="/rep<%=photo.getAttach()%>" alt="o"/></a><%}
	if (coupleHomeUser != null){photo = homeService.getHomePhoto(" id=" + coupleHomeUser.getRecommended());
	if (photo != null){%><a href="homePhotoCat.jsp?uid=<%=photo.getUserId()%>"><img src="/rep<%=photo.getAttach()%>" alt="o"/></a><%}
	}%><br/>
	<%}%>
	<a href="<%=("/chat/post.jsp?roomId=0&amp;toUserId="+userId) %>">和<%=user.getGenderText() %>聊天</a>.<a href="<%="viewHomeUser.jsp?userId="+userId%>">查<%=user.getGenderText() %>档案</a><br/>
	<a href="<%=("/team/question/his.jsp?uid="+userId) %>">缘分测试</a>.<a href="<%=("/jcforum/userTopic.jsp?u="+userId)%>"><%=user.getGenderText() %>的帖子</a><br/>
	<a href="/team/teams.jsp?id=<%=userId%>"><%=user.getGenderText() %>的圈子</a>.<a href="homeNeighbor.jsp?userId=<%=userId%>"><%=user.getGenderText() %>的邻居</a><br/>
	<a href="../friend/astro/index.jsp?uid=<%=userId%>">星座配对</a>.<a href="mf.jsp?uid=<%=userId%>">与<%=user.getGenderText()%>交友</a><br/>
	【最新动态】<% List trendList2 = homeUser.getTrend();
	if(trendList2.size() > 5) {%><a href="<%=("userTrend.jsp?uid=" + userId) %>">全部</a><%} else if(trendList2.size() == 0) {%>暂无<%} %><br/>
		<%for(int i = 0;i < trendList2.size(); i++) {
			if(i >= 5) {
				break;
			}
			BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)homeUser.getTrend().get(i)));
		%>*<%=trend.getContent(action.getLoginUser().getId(),response,userId,null)%><br/>
	<%} %>
	【日记】<%if(homeUser.getDiaryCount() > 0) { %><%=homeUser.getDiaryCount()%>篇.<a href="<%=("homeDiaryCat.jsp?userId="+homeUser.getUserId()) %>">更多</a><%} else {%>暂无<%} %><br/>
	<%		int j = 1;
			for(int i=0;i<diaryList.size();i++){		
				HomeDiaryBean homeDiary=(HomeDiaryBean)diaryList.get(i);%>
	<%=j%>.<a href="<%=("homeDiary.jsp?userId="+user.getId()+"&amp;diaryId="+homeDiary.getId())%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>(阅<%=homeDiary.getHits()%>|回<%=homeDiary.getReviewCount()%>)<br/>
	<%
				j++;
			}
	%>
	<a href="<%=("house.jsp?userId="+ homeUser.getUserId() +"&amp;in=1")%>"><img src="<%=("/img/home/house/"+homeUser.getTypeId()+".gif")%>" alt="家"/></a>
	<% if (coupleHomeUser != null){
	%><a href="<%=("house.jsp?userId="+ coupleHomeUser.getUserId() +"&amp;in=1")%>"><img src="<%=("/img/home/house/"+coupleHomeUser.getTypeId()+".gif")%>" alt="家"/></a><%	
	}%><br/>
	【最近访客】<br/>
	<%
			Iterator iterator = homeUser.getRecentVisit().iterator();
			int count = 0;
			
			while(iterator.hasNext()) {
			//for(int i = 0; i < homeUser.getRecentVisit().size(); i++) {
				if(count >= 3)
					break;
				BeanVisit visit = (BeanVisit)iterator.next();
				
		%>
	<a href="<%=("home2.jsp?userId=" + visit.getFromUid()) %>"><%=StringUtil.toWml(visit.getFromNickName()) %></a> <%=visit.getVisitTime() %><br/>
	<%
				count++;
			}
		%>
	访问量:<%=homeUser.getHits()%>/<%=homeUser.getTotalHits()%><br/>
	【留言】<%if(homeUser.getReviewCount()>0){%>
					<%=homeUser.getReviewCount()%>条<%if(homeUser.getReviewCount()>3){%><a href="<%=("homeReview.jsp?userId="+userId)%>">更多</a><%}
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
				if(action.getLoginUser() == null) {
			%>
	<a href="<%=("home2.jsp?userId="+homeReview.getReviewUserId())%>"><%=StringUtil.toWml(reviewUser.getNickName())%></a>
	<%
				} else if(reviewUser.getId()==action.getLoginUser().getId()){
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
	<go href="homeReview.jsp?userId=<%=userId%>" method="post">
	  <postfield name="review" value="$review"/>
	</go></anchor><br/>	<a href="viewAllHome.jsp">*家园之星*</a><br/>
	<%	//最近访客
	if(userId != action.getLoginUser().getId()) {
		BeanVisit visit = new BeanVisit();
		visit.setFromUid(action.getLoginUser().getId());
		visit.setFromNickName(action.getLoginUser().getNickName());
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
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>