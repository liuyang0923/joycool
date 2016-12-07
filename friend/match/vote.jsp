<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,net.joycool.wap.bean.home.HomeDiaryBean"%>
<%! static net.joycool.wap.service.impl.HomeServiceImpl homeService = new net.joycool.wap.service.impl.HomeServiceImpl();  %>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
//if (loginUser == null){
//	response.sendRedirect("/user/login.jsp");
//	return;
//}
int cityRank = 0;
String tip = "";
String call = "她";
int fansCount = 0;
boolean isMySelf = false;
boolean homeExist = false;
String photoAddress = "";
MatchTodayVote todayVote = null;
HomeDiaryBean diary = null;
MatchUser matchUser = null;
UserBase userBase = null;
MatchVoted voted = null;
MatchRank rank = null;
MatchFans fans = null;
List diaryList = null;
MatchRes res = null;
List resList = MatchAction.getResList();
List forumContentIdList = null;
List haveList = new ArrayList();
if (loginUser != null){
	fans = MatchAction.getMatchFans(loginUser.getId());
}
int good[] = null;
if (fans != null){
	good = fans.getGood();
} else {
	good = new int[8];
}
int uid = action.getParameterInt("uid");
if (uid <= 0){
	tip = "该用户没有参赛.";
} else {
	matchUser = MatchAction.getMatchUser(uid);
	 if (matchUser == null){
		tip = "该用户没有参赛.";
	 } else {
		 if (loginUser != null && uid == loginUser.getId()){
			// 看我是否有家园
			if (net.joycool.wap.cache.util.HomeCacheUtil.getHomeCache(loginUser.getId())!=null){
				homeExist = true;
			}
		 	isMySelf = true;
		 	call = "我";
		 }
		 userBase = CreditAction.getUserBaseBean(matchUser.getUserId());
		 rank = MatchAction.service.getMatchRank(" user_id=" + matchUser.getUserId());
		 if (userBase == null){
			 tip = "该用户没有填写交友可信度信息.";
		 }
		 fansCount = SqlUtil.getIntResult("select count(id) from match_fans_ab where right_uid=" + uid,5);
		 // 取3条家园日记
		 diaryList = homeService.getHomeDiaryList("user_id= " + matchUser.getUserId() + " and del=0 and cat_id=0 order by id desc limit 3");
	 	 // 取得N条论坛主题
		 forumContentIdList = action.getForumTopicList(matchUser,3);
	 }
}
MatchInfo matchInfo = MatchAction.getCurrentMatch();
cityRank = action.cityRank2(matchUser);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="参赛资料"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><a href="/wapIndex.jsp">乐酷首页</a>><a href="index.jsp">乐后活动</a>><%=call%>的参赛资料<br/>
<a href="/home/home.jsp?userId=<%=matchUser.getUserId()%>"><%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%></a>(<%=userBase.getAge() + "岁," + action.getPlaceString(matchUser.getPlaceId())%>)<br/>
<img src="<%=action.getCurrentPhoto(matchUser,false)%>" alt="o" /><br/>
<%if (isMySelf){
%><a href="photo.jsp?uid=<%=matchUser.getUserId()%>">我的其他照片</a><%if(homeExist){%>|<a href="/home/addPhoto.jsp?cid=0">添加照片</a><%}%><br/>
<a href="/user/ViewUserInfo.do?userId=<%=matchUser.getUserId()%>">我的资料</a>|<a href="/home/home.jsp?userId=<%=matchUser.getUserId()%>">我的家园</a><br/>
修改:<a href="enounce.jsp?b=1">宣言</a>|<a href="uppic.jsp">参赛照片</a>|<a href="/friend/credit/credit.jsp">可信度</a><br/><%
} else {
%><a href="photo.jsp?uid=<%=matchUser.getUserId()%>">看她其他照片</a><br/>
<%if(loginUser != null){%><a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注她</a><%if (matchInfo != null && matchInfo.getFalg()==1){%>|<a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">为她投票</a><%}}%><br/>
<%if(loginUser == null){%><a href="/user/login.jsp?guest=1">与她聊天</a><%}else{%><a href="/chat/post.jsp?toUserId=<%=matchUser.getUserId()%>">与她聊天</a><%}%>|<a href="/user/ViewUserInfo.do?userId=<%=matchUser.getUserId()%>">她的资料</a><br/>
<%if(loginUser == null){%><a href="/user/login.jsp?guest=1">与她交友</a><%}else{%><a href="/user/OperFriend.do?add=1&amp;friendId=<%=matchUser.getUserId()%>">与她交友</a><%}%>|<a href="/home/home.jsp?userId=<%=matchUser.getUserId()%>">她的家园</a><br/><%	
}%>
参赛宣言:<%=StringUtil.toWml(matchUser.getEncounce())%><br/>
<%=call%>已有:<br/><%=action.getSheHas(matchUser)%><br/>
共计<%=matchUser.getVoteCount()%>靓点<br/>
所在城市:<%=action.getPlaceString(matchUser.getPlaceId())%>,<%if (cityRank <= 10){%>目前高居该地区第<%=cityRank%>名.<%}else{%>目前地区排名第<%=cityRank%>名.<%}%><br/>
<%if(todayVote!=null){%>(其中今日获得<%=todayVote.getVoteCount()%>靓点)<br/><%}%>
<%=call%>的日记:<%if(isMySelf){ if(homeExist){%><a href="/home/add.jsp?t=1">写日记</a><%}}else{%><a href="diary.jsp?uid=<%=matchUser.getUserId()%>">查看全部</a><%}%><br/>
<%if (diaryList != null && diaryList.size() > 0){
	for (int i = 0 ; i < diaryList.size() ; i++){
		diary = (HomeDiaryBean)diaryList.get(i);
		if (diary != null){
			%><%=i+1%>.<a href="diary.jsp?did=<%=diary.getId()%>&amp;uid=<%=matchUser.getUserId()%>"><%=StringUtil.toWml(diary.getTitel())%></a>(<%=diary.getReviewCount() %>/<%=diary.getHits() %>)<br/><%
		}
	}
} else {
%>暂无日记.<br/><%
}
%>
<%=call%>的帖子:<br/>
<%if(forumContentIdList != null && forumContentIdList.size() > 0){
   for(int i=0;i<forumContentIdList.size();i++){
  	Integer contentId =(Integer)forumContentIdList.get(i);
  	net.joycool.wap.bean.jcforum.ForumContentBean forumContent = net.joycool.wap.cache.util.ForumCacheUtil.getForumContent(contentId.intValue());
    if(forumContent==null){continue;}
    if(forumContent.isPeak()){%>[顶]<%}else if(forumContent.getMark1()==1){%>[精]<%}else{%><%=i+1%>.<%}%><a href="<%=("/jcforum/viewContent.jsp?contentId="+forumContent.getId()+"&amp;forumId="+forumContent.getForumId()+"&amp;myTopic=true")%>"><%=StringUtil.toWml(forumContent.getTitle())%></a>(<%=forumContent.getReply()%>|<%=forumContent.getCount()%>)<br/>
<%}
} else {
%><%=call%>还没有发表过主题.<br/><%
}%>
<a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">为<%=call%>投票</a><br/>
==<%=call%>的粉丝团==<br/>
<%=call%>共有<%=fansCount%>位粉丝<br/>
<%if (loginUser != null){
	%><%=action.getMyBestFans(uid,0)%><br/><%=action.getMyBestFans(uid,1)%><br/><%
}%>
<a href="allFans.jsp?uid=<%=uid%>">看<%=call%>全部粉丝</a><br/>
<%
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回乐后首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>