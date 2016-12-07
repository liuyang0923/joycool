<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.action.user.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*"%><%
	response.setHeader("Cache-Control","no-cache");
	net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();
	IHomeService homeService =ServiceFactory.createHomeService();
	int userId = 0;
	
	if(request.getParameter("uid") != null) {
		userId = StringUtil.toId(request.getParameter("uid"));
		if(userId > 0) {
			response.sendRedirect(("/homeland/home.jsp?uid="+userId));
			return;
		}
		
	}
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	int uid = loginUser.getId();
//	if(loginUser != null) {
//		if(uid == loginUser.getId()) {
//			response.sendRedirect(("/homeland/myPage.jsp"));
//			return;
//		}
//	}
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(uid);

	if(homeUser == null) {
		homeUser = new HomeUserBean();
		homeUser.setUserId(uid);
		homeUser.setName(loginUser.getNickName());
		homeUser.setMobile("");
		homeUser.setCity("未知");
		homeUser.setConstellation(0);
		homeUser.setHeight(0);
		homeUser.setWeight(0);
		homeUser.setWork("未知");
		homeUser.setPersonality(0);
		homeUser.setMarriage(0);
		homeUser.setAim(0);
		homeUser.setGender(loginUser.getGender());
		homeUser.setAge(loginUser.getAge());
		homeUser.setFriendCondition("未知");
		HomeCacheUtil.addHomeCache(homeUser);
		
		UserInfoUtil.updateUser("home=" + Constants.USER_INFO_HOME_MARK, "id="
				+ loginUser.getId(), loginUser.getId() + "");
		// 更新用户session中的个人家园标志位
		// 注册个人家园赠送50点经验值
		RankAction.addPoint(loginUser, Constants.REGISTER_HOME_POINT);
		loginUser.setHome(Constants.USER_INFO_HOME_MARK);
		
		HomeUserImageBean userImage = new HomeUserImageBean();
		userImage.setUserId(uid);
		userImage.setImageId(Constants.HOME_HOUSE_INIT_BACKGROUND);
		userImage.setTypeId(Constants.HOME_IMAGE_BACKGROUND_TYPE);
		userImage.setHomeId(1);
		homeService.addHomeUserImage(userImage);
	}
	
	int friendCount = service.getFriendCount("user_id = " + uid);
	UserBean user = UserInfoUtil.getUser(uid);
	int photoCount = homeService.getHomePhotoCount("user_id = " + uid);
	int diaryCount = homeService.getHomeDiaryCount("user_id = " + uid + " and del = 0");
	String diaryCondition = "user_id = " + uid + " and del=0 order by id desc limit 0, 3";
	Vector diaryList = homeService.getHomeDiaryList(diaryCondition);
	int forumCount = SqlUtil.getIntResult("select count(id) from jc_forum_content where user_id = " + StringUtil.trim(""+uid));

	Vector homeReviewList = homeService.getHomeReviewList("user_id=" + uid + " order by id desc limit 0, 5");
	
	ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
	boolean flag = userFriends.contains(uid + "");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="家园">
	<p>
	<%=BaseAction.getTop(request, response)%>
	<a href="<%=("/homeland/myPage.jsp") %>">家园</a>|管理|<a href="<%=("/lswjs/index.jsp") %>">导航</a><br/>	
	<a href="<%=("/home/homeDiaryList.jsp") %>">日记</a>.<a href="<%=("/home/homePhoto.jsp") %>">相册</a>.<a href="<%=("/beacon/bFri/myInfo.jsp") %>">奴隶</a>.<a href="/user/userInfo.jsp">设置</a><br/>
	<a href="<%=("/pet/info.jsp") %>">宠物</a>.<a href="<%=("/team/question/my.jsp") %>">缘分测试</a><br/>
	
	【日记】<%if(diaryCount > 0) { %><%=diaryCount%>篇.<%}%><%if(diaryCount > 3) { %><a href="<%=("/home/homeDiaryList.jsp?userId="+uid) %>">更多</a><%}%><a href="<%=("/home/homeDiaryList.jsp") %>">写日记</a><br/>
	
	<%
		if(diaryCount > 0) {
		int j = 1;
		for(int i=0;i<diaryList.size();i++){
			
			HomeDiaryBean homeDiary=(HomeDiaryBean)diaryList.get(i);
	%>
			<%=j%>.<a href="<%=("/home/homeDiary.jsp?userId="+user.getId()+"&amp;diaryId="+homeDiary.getId())%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>(阅<%=homeDiary.getHits()%>|回<%=homeDiary.getReviewCount()%>)
			<br/>
	<%
			j++;
		}
		} else {
	%>
		暂无日记<br/>
	<%} %>
	【相册】<%if(photoCount > 0) { %>.<%=photoCount %>张<%}%><%if(photoCount > 1) { %><a href="<%=("/home/homePhoto.jsp?userId="+uid) %>">更多</a><%}%><br/>
	<%
	HomePhotoBean homePhoto = homeService.getHomePhoto("user_id=" + uid	+ " order by create_datetime desc limit 0,1 ");
	if(homePhoto!=null){%>
	<img src="<%=Constants.MYALBUM_RESOURCE_ROOT_URL%><%=homePhoto.getAttach()%>" alt="loading..."/>
	<%}else{%>暂无照片.<a href="<%=("/home/homePhoto.jsp") %>">上传</a><%}%><br/>
	
	【我的家】<br/>
	<a href="<%=("/home/house.jsp?userId="+ uid +"&amp;in=1")%>"><img src="<%=("/img/home/house/"+homeUser.getTypeId()+".gif")%>" alt="家的图片"/></a><br/>
	<a href="<%="/home/house.jsp?userId="+ uid +"&amp;in=1"%>">管理家园</a>.<a href="<%=("/home/viewAllHome.jsp")%>">全部家园</a><br/>
	【留言】<%int hits=SqlUtil.getIntResult("SELECT count(id) FROM jc_home_review where user_id="+user.getId(),Constants.DBShortName);
				if(hits>0){%><%=hits%>条	<%} if(hits > 5) {%>.<a href="<%=("/home/homeReview.jsp") %>">更多</a><%} %><br/>
				
	<%
		HomeReviewBean homeReview=null;
		for(int i=0;i<homeReviewList.size();i++){
			homeReview=(HomeReviewBean)homeReviewList.get(i);
			//liuyi 2006-12-14 start
			if(homeReview==null)continue;
			UserBean reviewUser = UserInfoUtil.getUser(homeReview.getReviewUserId());
			if(reviewUser==null)continue;
	%>*<%
			if(reviewUser.getId()==user.getId()){   
			//	liuyi 2006-12-14 end
			%><%=BeanVisit.converDateToBefore2(homeReview.getCreateDdatetime()) %>
				我
			<%
				}else{
			%><a href="<%=("/user/ViewUserInfo.do?userId="+homeReview.getReviewUserId())%>" title="go"><%=StringUtil.toWml(reviewUser.getNickName())%></a>
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