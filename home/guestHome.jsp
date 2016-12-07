<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*,net.joycool.wap.framework.CustomAction"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*"%><%!
static ServiceVisit serviceVisit = ServiceVisit.getInstance();
static net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();
static IHomeService homeService =ServiceFactory.createHomeService();%><%
	response.setHeader("Cache-Control","no-cache");
	int uid = 0;
	CustomAction action=new CustomAction(request);
	if(request.getParameter("uid") != null) {
		uid = action.getParameterInt("uid");
	} else if((request.getParameter("userId") != null)) {
		uid = action.getParameterInt("userId");
	}
	String tip = "";
	UserBean loginUser = null;
	UserBean user = UserInfoUtil.getUser(uid);
	if(user==null){
		response.sendRedirect("/home/viewAllHome.jsp");
		return;
	}
	//int photoCount = homeService.getHomePhotoCount("user_id = " + uid);
	//int diaryCount = homeService.getHomeDiaryCount("user_id =" + uid + " and del=0");
	String diaryCondition = "user_id = " + uid + " and del=0 order by id desc limit 0, 3";
	Vector diaryList = homeService.getHomeDiaryList(diaryCondition);
	//int forumCount = SqlUtil.getIntResult("select count(id) from jc_forum_content where user_id = " + StringUtil.trim(""+uid));

	Vector homeReviewList = homeService.getHomeReviewList("user_id=" + uid + " order by id desc limit 0, 3");
	
	boolean flag = true;
	
	//最近访问记录人数
	int visitCount = serviceVisit.getCountVisitByToUid(uid);

	//自动建立家园
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(uid);

	if(homeUser == null) {
		tip = "此用户家园不存在.";
	}
//	boolean isABadGuys=service.isUserBadGuy(uid,loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(user.getNickName())%>的家园">
<p>
<%session.setAttribute("homeReview","homeReview");
%><%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%><a href="/user/guestViewInfo.jsp?userId=<%=uid%>"><%=StringUtil.toWml(user.getNickName())%></a>的家园<br/>

【相册】<%if(homeUser.getPhotoCount() > 0) { %><%=homeUser.getPhotoCount() %>张.<a href="/user/login.jsp">更多</a><%} else {%>暂无<%} %><br/>
<%
	HomePhotoBean homePhoto = homeService.getHomePhoto("user_id=" + uid	+ " order by create_datetime desc limit 0,1 ");
	if(homePhoto!=null){%>
<img src="/rep<%=homePhoto.getAttach()%>" alt="loading..."/><br/>
<%}%>
	
<a href="/user/login.jsp">和<%=user.getGenderText() %>聊天</a>.<a href="/user/login.jsp">查<%=user.getGenderText() %>档案</a><br/>
<a href="/user/login.jsp">缘分测试</a>.<a href="/user/login.jsp"><%=user.getGenderText() %>的帖子</a><br/>
<a href="/user/login.jsp"><%=user.getGenderText() %>的圈子</a>.<a href="/user/login.jsp"><%=user.getGenderText() %>的邻居</a><br/>
	
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
*<%=trend.getContent((loginUser == null?0:loginUser.getId()), response,0,null)%><br/>
<%} %>
【日记】<%if(homeUser.getDiaryCount() > 0) { %><%=homeUser.getDiaryCount()%>篇.<a href="/user/login.jsp">更多</a><%} else {%>暂无<%} %><br/>
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
<a href="/user/guestViewInfo.jsp?userId=<%=visit.getFromUid()%>"><%=StringUtil.toWml(visit.getFromNickName()) %></a><%=visit.getVisitTime() %><br/>
<%
			count++;
		}
	%>
	
【留言】<%if(homeUser.getReviewCount()>0){%>
				<%=homeUser.getReviewCount()%>条<%if(homeUser.getReviewCount()>3){%><a href="/user/login.jsp">更多</a><%}
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
<a href="/user/guestViewInfo.jsp?userId=<%=uid%>"><%=StringUtil.toWml(reviewUser.getNickName())%></a>
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
<a href="/user/login.jsp">发表评论</a><br/>	
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
<a href="/register.jsp">注册正式用户开通自己家园</a><br/>
<%	
} else {
%><%=tip%><br/><a href="/Column.do?columnId=10586">返回</a><br/><%	
}
%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>