<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="net.joycool.wap.spec.garden.*,java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.user.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.spec.friend.*"%><%!
static IHomeService homeService =ServiceFactory.createHomeService();%><%
	response.setHeader("Cache-Control","no-cache");
	
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	FriendAction action = new FriendAction(request);
	int userId = action.getParameterInt("userId");
	if(user == null) {
		request.getRequestDispatcher("/home/guestHome.jsp?userId=" + userId).forward(request,response);
		return;
	}
	int uid = user.getId();//LoginUser.getId();
	
	if(userId!=0&&userId!=uid){
		request.getRequestDispatcher("home2.jsp?userId="+userId).forward(request,response);
		return;
	}
	// 结婚没？
	FriendMarriageBean marriageBean = action.infoMarriage(uid);
	if (marriageBean == null){
		// 没结婚?那就该回谁家回谁家吧
		if (user.getGender() == 0){
			response.sendRedirect("fehome.jsp");
			return;
		}
	} else {
		// 结婚了，请直接去夫妻家园
		response.sendRedirect("couple.jsp");
		return;
	}
	
	//int friendCount = service.getFriendCount("user_id = " + uid);
	//UserBean user = UserInfoUtil.getUser(uid);//LoginUser
	//FriendAction action = new FriendAction(request);

	
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
	
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(uid);
	HomePhotoBean photo = null;

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
	
	String diaryCondition = "user_id = " + uid + " and del=0 order by id desc limit 0, 3";
	Vector diaryList = homeService.getHomeDiaryList(diaryCondition);
	
	//int forumCount = SqlUtil.getIntResult("select count(id) from jc_forum_content where user_id = " + StringUtil.trim(""+uid));

	Vector homeReviewList = homeService.getHomeReviewList("user_id=" + uid + " order by id desc limit 0, 3");
	
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	List trendList = serviceTrend.getFriendTrendIdByUid(uid, 0, 5);
	
	ServiceVisit serviceVisit = ServiceVisit.getInstance();
	//List visitList = serviceVisit.getVisitByToUid(uid, 0, 3);
	//int visitCount = serviceVisit.getCountVisitByToUid(uid);
		
	if(trendList.size()>0)
		user.latestTrend = ((Integer)trendList.get(0)).intValue();
	
	//int hits=SqlUtil.getIntResult("SELECT count(id) FROM jc_home_review where user_id="+user.getId(),Constants.DBShortName);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="家园">
<p>
<%=BaseAction.getTop(request, response)%>
我的家园<br/>
<a href="homeDiaryCat.jsp">日记</a><%if(homeUser.getDiaryCount() > 0) { %><%=homeUser.getDiaryCount()%><%}%>.<a href="homePhotoCat.jsp">相册</a><%if(homeUser.getPhotoCount() > 0) { %><%=homeUser.getPhotoCount() %><%}%>.<a href="<%=("homeReview.jsp") %>">留言</a><%if(homeUser.getReviewCount()>0){%><%=homeUser.getReviewCount()%><%}%>.<a href="<%=("house.jsp?userId="+ uid +"&amp;in=1")%>">家</a><br/>
<a href="../friend/astro/index.jsp">星座</a>.<a href="../news/index2.jsp">八卦</a>.<a href="../team/question/index.jsp">缘分测试</a>.<a href="../news/list.jsp?id=22">时尚</a><br/>
<%if (homeUser != null && homeUser.getRecommended() > 0){
photo = homeService.getHomePhoto(" id=" + homeUser.getRecommended());
if (photo != null){
%>★★推荐照片★★<br/>
<a href="homePhotoCat.jsp?uid=<%=photo.getUserId()%>"><img src="/rep<%=photo.getAttach()%>" alt="o"/></a><br/>
<a href="homePhotoCat.jsp">修改推荐照片</a><br/><%
}else {
%><a href="homePhotoCat.jsp">推荐照片</a><br/><%
}
} else {
%><a href="homePhotoCat.jsp">推荐照片</a><br/><%
}%>
【好友动态】<%if(trendList.size() == 5) {%><a href="<%=("friendTrend.jsp") %>">全部</a><%} %><br/>
<%
		for(int i = 0;i < trendList.size(); i ++) {
			BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)trendList.get(i)));
	%>
*<%=trend.getContent(action.getLoginUser().getId(),response,uid,null) %><br/>
<%} %>
【最近访客】<%if(homeUser.getRecentVisit().size() >= 3) {%><a href="<%=("aV.jsp") %>">全部</a><%} %><br/>
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
【<a href="homeReview.jsp">留言</a>】<%if(homeUser.getReviewCount()>0){%><%}%><br/>			
<%
		HomeReviewBean homeReview=null;
		for(int i=0;i<homeReviewList.size();i++){
			homeReview=(HomeReviewBean)homeReviewList.get(i);
			//liuyi 2006-12-14 start
			if(homeReview==null)continue;
			UserBean reviewUser = UserInfoUtil.getUser(homeReview.getReviewUserId());
			if(reviewUser==null)continue;
	%>*<%=BeanVisit.converDateToBefore2(new Date(homeReview.getCreateTime())) %> <%
			if(reviewUser.getId()==user.getId()){   
			//	liuyi 2006-12-14 end
			%>我
<%
				}else{
			%><a href="<%=("home2.jsp?userId="+homeReview.getReviewUserId())%>"><%=StringUtil.toWml(reviewUser.getNickName())%></a>
<%
				}
			%>
:<%=StringUtil.toWml(StringUtil.limitString(homeReview.getReview(),30))%>
<%
				if(user.getId()==homeReview.getUserId()){
			%>
<a href="<%=("homeReview.jsp?delete="+homeReview.getId())%>" >删</a>
<%	
				}
			%><br/>
<%
		}
	%><a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>