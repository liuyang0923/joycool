<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*,net.joycool.wap.action.user.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*"%><%!
static ServiceVisit serviceVisit = ServiceVisit.getInstance();
static net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();
static IHomeService homeService =ServiceFactory.createHomeService();%><%
	response.setHeader("Cache-Control","no-cache");
	int uid = 0;
	if(request.getParameter("id") != null) {
		uid = Integer.parseInt(request.getParameter("id"));
	}
	
	//int friendCount = service.getFriendCount("user_id = " + uid);
	UserBean user = UserInfoUtil.getUser(uid);
	UserBean loginUser = null;
	//int photoCount = homeService.getHomePhotoCount("user_id = " + uid);
	//int diaryCount = homeService.getHomeDiaryCount("user_id =" + uid + " and del=0");
	String diaryCondition = "user_id = " + uid + " and del=0 order by id desc limit 0, 5";
	Vector diaryList = homeService.getHomeDiaryList(diaryCondition);
	//int forumCount = SqlUtil.getIntResult("select count(id) from jc_forum_content where user_id = " + StringUtil.trim(""+uid));

	Vector homeReviewList = homeService.getHomeReviewList("user_id=" + uid + " order by id desc limit 0, 5");
	
	boolean flag = true;
	
	if(loginUser != null&&loginUser.getId()!=uid) {
		ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
		flag = userFriends.contains(uid + "");
	}
	
	
	//最近访问记录人数
	int visitCount = serviceVisit.getCountVisitByToUid(uid);

	//自动建立家园
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(uid);


%><html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
<%if(homeUser==null){%>
(无)<br/>
<%}else{
%>
<a href="queryUserInfo.jsp?id=<%=uid%>"><%=StringUtil.toWml(user.getNickName())%></a>的家园<br/>

【相册】<%if(homeUser.getPhotoCount() > 0) { %><%=homeUser.getPhotoCount() %>张.更多<%} else {%>暂无<%} %><br/>
<%
	HomePhotoBean homePhoto = homeService.getHomePhoto("user_id=" + uid	+ " order by create_datetime desc limit 0,1 ");
	if(homePhoto!=null){%>
<img src="/rep<%=homePhoto.getAttach()%>" alt=""/><br/>
<%}%>
	
【最新动态】<% List trendList = homeUser.getTrend();
if(trendList.size() > 10) {%>全部<%} else if(trendList.size() == 0) {%>暂无<%} %><br/>
	<%
		for(int i = 0;i < trendList.size(); i ++) {
			if(i >= 10) {
				break;
			}
			BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)homeUser.getTrend().get(i)));
	%>
*<%=trend.getContentNoLink()%><br/>
<%} %>
【日记】<%if(homeUser.getDiaryCount() > 0) { %><%=homeUser.getDiaryCount()%>篇.更多<%} else {%>暂无<%} %><br/>
<%
		int j = 1;
		for(int i=0;i<diaryList.size();i++){
			
			HomeDiaryBean homeDiary=(HomeDiaryBean)diaryList.get(i);
	%>
<%=j%>.<%=StringUtil.toWml(homeDiary.getTitel())%>(阅<%=homeDiary.getHits()%>|回<%=homeDiary.getReviewCount()%>)<br/>
<%
			j++;
		}
	%>
<img src="<%=("/img/home/house/"+homeUser.getTypeId()+".gif")%>" alt="家"/><br/>	
【最近访问】<%=visitCount %>人<br/>
<%
		Iterator iterator = homeUser.getRecentVisit().iterator();
		int count = 0;
		
		while(iterator.hasNext()) {
			if(count >= 5)
				break;
			BeanVisit visit = (BeanVisit)iterator.next();
	%>
<a href="queryUserInfo.jsp?id=<%=visit.getFromUid()%>"><%=StringUtil.toWml(visit.getFromNickName()) %></a><%=visit.getVisitTime() %><br/>
<%
			count++;
		}
	%>
	
【留言】<%if(homeUser.getReviewCount()>0){%>
				<%=homeUser.getReviewCount()%>条<%if(homeUser.getReviewCount()>5){%>更多<%}
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
<a href="queryUserInfo.jsp?id=<%=homeReview.getReviewUserId()%>"><%=StringUtil.toWml(reviewUser.getNickName())%></a>
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

<%}%>
</body></html>