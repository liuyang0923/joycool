<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.user.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendBean"%><%
	response.setHeader("Cache-Control","no-cache");
	net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();
	IHomeService homeService =ServiceFactory.createHomeService();
	
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	if(user == null) {
		response.sendRedirect("/");
		return;
	}
	
	int uid = user.getId();//LoginUser.getId();
	//int friendCount = service.getFriendCount("user_id = " + uid);
	//UserBean user = UserInfoUtil.getUser(uid);//LoginUser
	FriendAction action = new FriendAction(request);
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
	String diaryCondition = "user_id = " + uid + " and del=0 order by id desc limit 0, 3";
	Vector diaryList = homeService.getHomeDiaryList(diaryCondition);
	
	//int forumCount = SqlUtil.getIntResult("select count(id) from jc_forum_content where user_id = " + StringUtil.trim(""+uid));

	Vector homeReviewList = homeService.getHomeReviewList("user_id=" + uid + " order by id desc limit 0, 3");
	
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	List trendList = serviceTrend.getFriendTrendIdByUid(uid, 0, 5);
	
	ServiceVisit serviceVisit = ServiceVisit.getInstance();
	//List visitList = serviceVisit.getVisitByToUid(uid, 0, 3);
	//int visitCount = serviceVisit.getCountVisitByToUid(uid);
		
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(uid);

	if(homeUser == null) {
		homeUser = new HomeUserBean();
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
		homeService.addHomeUserImage(userImage);
	}
	//int hits=SqlUtil.getIntResult("SELECT count(id) FROM jc_home_review where user_id="+user.getId(),Constants.DBShortName);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="家园">
	<p>
	<%=BaseAction.getTop(request, response)%>
	我的家园<br/>
	<a href="<%=("/home/homeDiaryList.jsp") %>">日记</a><%if(homeUser.getDiaryCount() > 0) { %>(<%=homeUser.getDiaryCount()%>)<%}%>.<a href="/home/homePhoto.jsp">相册</a><%if(homeUser.getPhotoCount() > 0) { %>(<%=homeUser.getPhotoCount() %>)<%}%>.<a href="<%=("/home/homeReview.jsp") %>">留言</a><%if(homeUser.getReviewCount()>0){%>(<%=homeUser.getReviewCount()%>)<%}%>.<a href="<%=("/home/house.jsp?userId="+ uid +"&amp;in=1")%>">装饰</a><br/>
	<!-- 
	<a href="/beacon/bFri/myInfo.jsp">奴隶</a>.<a href="/user/userInfo.jsp">设置</a><br/>
	<a href="/pet/info.jsp">宠物</a>.<a href="/team/question/my.jsp">缘分测试</a><br/> -->
	【好友动态】<%if(trendList.size() == 5) {%><a href="<%=("/homeland/friendTrend.jsp") %>">全部</a><%} %><br/>
	<%
		for(int i = 0;i < trendList.size(); i ++) {
			BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)trendList.get(i)));
	%>
			*<%=trend.getContent(uid, response) %><br/>
	<%} %>
	<!--  
	【我的家】<br/>
	<a href="<%=("/home/house.jsp?userId="+ uid +"&amp;in=1")%>"><img src="<%=("/img/home/house/"+homeUser.getTypeId()+".gif")%>" alt="家的图片"/></a><br/>
	<a href="<%="/home/house.jsp?userId="+ uid +"&amp;in=1"%>">管理家园</a>.<a href="<%=("/home/viewAllHome.jsp")%>">全部家园</a><br/>
	-->
	【最近访客】<%if(homeUser.getRecentVisit().size() == 3) {%><a href="<%=("/homeland/aV.jsp") %>">全部</a><%} %><br/>
	<%
		Iterator iterator = homeUser.getRecentVisit().iterator();
		int count = 0;
		
		while(iterator.hasNext()) {
		//for(int i = 0; i < homeUser.getRecentVisit().size(); i++) {
			if(count >= 3)
				break;
			BeanVisit visit = (BeanVisit)iterator.next();
			
	%>
		<a href="<%=("/homeland/home.jsp?uid=" + visit.getFromUid()) %>"><%=StringUtil.toWml(visit.getFromNickName()) %></a> <%=visit.getVisitTime() %><br/>
	<%
			count++;
		}
	%>
	【留言】<%if(homeUser.getReviewCount()>0){%><%=homeUser.getReviewCount()%>条	<%} if(homeUser.getReviewCount() > 3) {%>.<a href="<%=("/home/homeReview.jsp") %>">更多</a><%} %><br/>
				
	<%
		HomeReviewBean homeReview=null;
		for(int i=0;i<homeReviewList.size();i++){
			homeReview=(HomeReviewBean)homeReviewList.get(i);
			//liuyi 2006-12-14 start
			if(homeReview==null)continue;
			UserBean reviewUser = UserInfoUtil.getUser(homeReview.getReviewUserId());
			if(reviewUser==null)continue;
	%>*<%=BeanVisit.converDateToBefore2(homeReview.getCreateDdatetime()) %> <%
			if(reviewUser.getId()==user.getId()){   
			//	liuyi 2006-12-14 end
			%>我
			<%
				}else{
			%><a href="<%=("/homeland/home.jsp?userId="+homeReview.getReviewUserId())%>" title="go"><%=StringUtil.toWml(reviewUser.getNickName())%></a>
			<%
				}
			%>
				说:<%=StringUtil.toWml(homeReview.getReview())%>
			<%
				if(user.getId()==homeReview.getUserId()){
			%>
					<a href="<%=("/home/homeReview.jsp?delete="+homeReview.getId())%>" >删除</a>
			<%	
				}
			%><br/>
	<%
		}
	%><%=BaseAction.getBottomShort(request, response)%>
	</p>
	</card>
</wml>