<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeReviewBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryReviewBean,net.joycool.wap.cache.util.HomeCacheUtil,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendMarriageBean,net.joycool.wap.bean.home.HomeUserBean " %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.diary(request);
String otherTitle = "";
String tip = "";
int coupleUid = 0;
String result=(String) request.getAttribute("result");
int userId=action.getParameterInt("userId");
if (userId == 0){
	userId = action.getParameterInt("uid");
	if (userId == 0){
		userId = action.getLoginUser().getId();
	}
}
if (UserInfoUtil.getUser(userId) == null){
	tip = "用户不存在";
}
String diaryId=(String)request.getAttribute("diaryId");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String orderBy = (String)request.getParameter("orderBy");
Boolean flagB = (Boolean)request.getAttribute("flag");
boolean flag = true;
if(flagB != null) {
	flag = flagB.booleanValue();
}
HomeUserBean homeUser = HomeCacheUtil.getHomeCache(userId);
if (homeUser == null){
	if (userId == action.getLoginUser().getId()){
		tip = "你";
	} else {
		tip = "TA";
	}
	tip += "还没有建立家园。";
}
// 取得"另一伴"的UID
//coupleUid = action.getCoupleUid(userId);
// 取得特殊的标题
//otherTitle = action.getOtherTitle(homeUser,coupleUid);
//HomeUserBean coupleUser = HomeCacheUtil.getHomeCache(coupleUid);	//"老婆(老公)"的家园用户信息
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if (!"".equals(tip)){
%><card title="家园"><p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/><a href="home.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p></card>
<%} else if(!flag) {
	UserBean user = UserInfoUtil.getUser(userId);
%>
<card title="隐私设置">
<p align="left">
由于隐私设置，您不能浏览<br/>
<a href="home.jsp?userId=<%=userId%>" >返回<%=user.getNickNameWml()%>的家园</a><br/>
<a href="home.jsp" >返回我的家园</a><br/>
</p>
</card>
<%
} //出错处理
else if("failure".equals(result)){%>
<card title="乐酷家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="inputRegisterInfo.jsp">（隆重）开通我的个人家园，让朋友们来做客！</a><br/> 
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
//添加评论
else if("noExist".equals(result)){
String url = ("home.jsp?userId=" + userId);
%>
<card title="<%=StringUtil.toWml(action.getUser(userId).getNickName())%>的日记" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%--<a href="homeDiaryList.jsp?userId=<%=userId%>">返回日记列表</a><br/>--%>
<%
UserBean user = UserInfoUtil.getUser(userId);
%>
<a href="home.jsp?userId=<%=userId%>" >返回<%=userId==action.getLoginUser().getId()?"我":user.getNickNameWml() %>的家园</a><br/>
<a href="viewAllHome.jsp">明星家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("refrush".equals(result)){
out.clearBuffer();
response.sendRedirect("homeReview.jsp?userId="+userId);
return;
} else if("addSuccess".equals(result)){

String url = ("homeDiary.jsp?userId="+userId+"&amp;diaryId="+diaryId+"&amp;orderBy="+orderBy);
%>
<card title="添加评论" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%
UserBean user = UserInfoUtil.getUser(userId);
%>
<a href="homeDiary.jsp?userId=<%=userId%>&amp;diaryId=<%=diaryId%>&amp;orderBy=<%=orderBy%>">直接返回日记</a><br/>
<a href="home.jsp?userId=<%=userId%>" >返回<%=userId==action.getLoginUser().getId()?"我":user.getNickNameWml()%>的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("delOk".equals(result)){
String url = ("homeDiary.jsp?userId="+loginUser.getId()+"&amp;diaryId="+diaryId+"&amp;orderBy="+orderBy);
%>
<card title="家园评论" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
Vector homeDiaryReviewList=(Vector)request.getAttribute("homeDiaryReviewList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
HomeDiaryBean homeDiary=(HomeDiaryBean)request.getAttribute("homeDiary");
HomeDiaryBean prevDiary=(HomeDiaryBean)request.getAttribute("prevDiary");
HomeDiaryBean nextDiary=(HomeDiaryBean)request.getAttribute("nextDiary");
String reviewCount=(String)request.getAttribute("reviewCount");
%>
<card title="<%=StringUtil.toWml(action.getUser(userId).getNickName())%>的日记">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(homeDiary.getTitel())%>(阅<%=homeDiary.getHits()%>|评<%=homeDiary.getReviewCount()%>)<br/>
<%=homeDiary.getCreateDatetime()%><br/>
<%=StringUtil.toWml(homeDiary.getContent())%><br/>

<%HomeDiaryReviewBean homeDiaryReview=null;
for(int i=0;i<homeDiaryReviewList.size();i++){
homeDiaryReview=(HomeDiaryReviewBean)homeDiaryReviewList.get(i);
//liuyi 2006-12-14 start
if(homeDiaryReview==null)continue;
UserBean reviewUser = action.getUser(homeDiaryReview.getReviewUserId());
if(reviewUser==null)continue;
%>
<%=i+1%>.
<%
if(reviewUser.getId()==loginUser.getId()){   
//	liuyi 2006-12-14 end
%>
我<%}else{%><a href="home2.jsp?userId=<%=homeDiaryReview.getReviewUserId()%>"><%=StringUtil.toWml(action.getUser(homeDiaryReview.getReviewUserId()).getNickName())%></a>
<%}%>说:<%=StringUtil.toWml(homeDiaryReview.getReview())%>(<%=homeDiaryReview.getCreateDatetime()%>)
<%if(loginUser.getId()==homeDiary.getUserId()){
%>
<a href="homeDiary.jsp?delete=1&amp;diaryId=<%=homeDiary.getId()%>&amp;diaryReviewId=<%=homeDiaryReview.getId()%>" >删除</a>
<%}
%><br/><%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%>
<%
 }
%>
<br/>
<!--wucx4_判断是否是黑名单不能到该用户的家园留   日期:2006-11－24-->
<%
IUserService service = ServiceFactory.createUserService();
boolean isABadGuys=service.isUserBadGuy(userId,loginUser.getId());
if(!isABadGuys){%>
内容:<input name="review"  maxlength="100" value=""/><br/>
 <anchor title="确定">发表评论
    <go href="homeDiary.jsp" method="post">
    <postfield name="review" value="$review"/>
    <postfield name="userId" value="<%=userId%>"/>
    <postfield name="reviewUserId" value="<%=loginUser.getId()%>"/>
    <postfield name="diaryId" value="<%=diaryId%>"/>
    <postfield name="orderBy" value="<%=orderBy%>"/>
    </go>
    </anchor><br/>
<%}
if(loginUser.getId()==homeDiary.getUserId()){
%>
<a href="homeDiaryEdit.jsp?diaryId=<%=homeDiary.getId()%>" >编辑本篇日记</a><br/>
<%}
//如果下一条图片不为空，显示下一条
if(nextDiary != null){
%>
<a href="homeDiary.jsp?userId=<%=nextDiary.getUserId()%>&amp;diaryId=<%=nextDiary.getId()%>&amp;orderBy=<%=orderBy%>">下一篇:<%=StringUtil.toWml(nextDiary.getTitel())%></a><br/>
<%
}
//如果上一条图片不为空，显示上一条
if(prevDiary != null){
%>
<a href="homeDiary.jsp?userId=<%=prevDiary.getUserId()%>&amp;diaryId=<%=prevDiary.getId()%>&amp;orderBy=<%=orderBy%>">上一篇:<%=StringUtil.toWml(prevDiary.getTitel())%></a><br/>
<%
}
%> 
<%--<a href="homeDiaryList.jsp?userId=<%=userId%>">返回日记列表</a><br/>--%>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<%String catName =  (String)request.getAttribute("catName");
%>
<a href="homeDiaryList.jsp?uid=<%=userId%>&amp;cid=<%=homeDiary.getCatId()%>">返回分类:<%=catName==null?"":StringUtil.toWml(catName) %></a><br/>
<%
UserBean user = UserInfoUtil.getUser(userId);
%>
<a href="home.jsp?userId=<%=userId%>" >返回<%=userId==action.getLoginUser().getId()?"我":user.getNickNameWml() %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%--<a href="viewAllHome.jsp">*家园之星*</a><br/>--%>
<%--<a href="diaryTop.jsp">乐客日记</a><br/>--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>