<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.cache.util.HomeCacheUtil"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
//dao,action
IHomeService homeService =ServiceFactory.createHomeService();
IUserService userService =ServiceFactory.createUserService();
HomeAction home=new HomeAction(request);
home.viewAllHome(request);
home.hotHomeList(request);
//家园总数
String totalHome=(String)request.getAttribute("totalHome");
//推荐之星
Vector commendStar=(Vector)request.getAttribute("commendStar");
//推荐日记
int totalCount1=StringUtil.toInt((String)request.getAttribute("totalCount1"));
int totalPage1=StringUtil.toInt((String)request.getAttribute("totalPage1"));
int pageIndex1=StringUtil.toInt((String)request.getAttribute("pageIndex1"));
int start =StringUtil.toInt((String)request.getAttribute("start"));
int end =StringUtil.toInt((String)request.getAttribute("end"));
Vector commendDiary=(Vector)request.getAttribute("commendDiary");
//推荐相片
Vector commendPhoto=(Vector)request.getAttribute("commendPhoto");

Vector hitsHome=(Vector)request.getAttribute("hitsHome");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="家园之星">
<p align="left">
<img src="img/home.gif" alt="logo"/><br/>
<%=BaseAction.getTop(request, response)%>
【今日之星】<a href="hotHomeList.jsp">更多</a><br/>
<%
for(int i = 0; i < 3; i ++){
	HomeUserBean homeUser=(HomeUserBean)hitsHome.get(i);
	UserBean user=UserInfoUtil.getUser(homeUser.getUserId());
%>
<%=i+1%>.
<a href="home.jsp?userId=<%=homeUser.getUserId()%>" ><%=loginUser!=null&&loginUser.getId()==user.getId()?"我的家园":StringUtil.toWml(user.getNickName())%></a>(<%=homeUser.getHits()%>访)<br/>
<%}%>
【采访车】<br/>
<%
//int count = todayStar.size();
//for(int i = 0; i < count; i ++)
{
	HomeUserBean homeUser=HomeCacheUtil.getStarUser();//(HomeUserBean)todayStar.get(i);
	UserBean user=UserInfoUtil.getUser(homeUser.getUserId());
%>*<a href="home.jsp?userId=<%=homeUser.getUserId()%>" ><%=loginUser!=null&&loginUser.getId()==user.getId()?"我的家园":StringUtil.toWml(user.getNickName())%></a>(<%=homeUser.getTotalHits()%>访)<br/><%
}
%>
【推荐家园】<br/>
<%
int count = commendStar.size();

if(count > 5) {
	count = 5;
}

for(int i = 0; i < count; i ++){
	HomeUserBean homeUser=(HomeUserBean)commendStar.get(i);
	UserBean user=UserInfoUtil.getUser(homeUser.getUserId());
	if(user==null)continue;
%>
<%=i+1%>.
<a href="home.jsp?userId=<%=homeUser.getUserId()%>" ><%=loginUser!=null&&loginUser.getId()==user.getId()?"我的家园":StringUtil.toWml(user.getNickName())%></a>(<%=homeUser.getTotalHits()%>访)<br/>
<%
}
%>
【推荐日记】<a href="md.jsp">更多</a><br/>
<%
for(int i = start; i < end; i ++){
	if(i>totalCount1-1) break;
	HomeDiaryBean homeDiary=(HomeDiaryBean)commendDiary.get(i);
%>
<%=i+1%>.
<a href="homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>  
(阅<%=homeDiary.getDailyHits()%>|评<%=homeDiary.getReviewCount()%>)<br/>
<%}%>
<%--=PageUtil.shuzifenye1(totalPage1, pageIndex1, "viewAllHome.jsp", false, " ", response)%><%if(totalPage1>1){%><br/><%}--%>
<a href="newDiaryTop.jsp?orderBy=mark">查看网友精华日记</a><br/>
【推荐照片】<a href="photoTop.jsp">更多</a><br/>
<%--==<a href="viewCommendPhoto.jsp">推荐照片</a>==<br/>--%>
<%
for(int i=0;i<commendPhoto.size();i++){
	HomePhotoBean photo=(HomePhotoBean)commendPhoto.get(i);
%>
<a href="homePhoto.jsp?userId=<%=photo.getUserId()%>&amp;hit=<%=photo.getId()%>"><img src="/rep<%=photo.getAttach()%>" alt="loading..."/></a>
<%}%>
<br/>
用户ID:<input name="userId" format="*N"  maxlength="10" value=""/><anchor>搜索家园
 <go href="searchHome.jsp" method="post">
     <postfield name="userId" value="$userId"/>
 </go>
 </anchor><br/>
<a href="viewNewHome.jsp">新成立的家园</a><br/>       

<%if(loginUser!=null && loginUser.getHome()==1){%>
<a href="home.jsp">返回我的家园</a><br/>
<%}else{%>
<a href="inputRegisterInfo.jsp">我也要建立家园</a><br/> 
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>