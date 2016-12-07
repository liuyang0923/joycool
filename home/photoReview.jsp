<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page errorPage=""%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
//当前用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//获取评论返回参数
String returnURL=(String)session.getAttribute("returnURL");
//添加新评论参数
String photoId=request.getParameter("photoId");
String content=request.getParameter("content");
int catId = action.getParameterInt("cid");
if (catId <= 0){
	catId = action.getParameterInt("catId");
}
int userId=0;
int uid = action.getParameterInt("uid");
if (uid <= 0){
	uid = action.getParameterInt("userId");
}
//DAO
IHomeService homeService =ServiceFactory.createHomeService();
IUserService userService=ServiceFactory.createUserService();
String tip=null;
HomeAction home=new HomeAction(request);
int reviewId=StringUtil.toInt(request.getParameter("reviewId"));
String delete=request.getParameter("delete");
if(delete!=null&&reviewId>0){
home.deleteHomePhotoReview(request);

//response.sendRedirect("photoReview.jsp");
}
//此页面兼顾添加评论功能，如果内容不为空，执行添加评论方法
if(content!=null){
//过滤一个用户连续发相同评论
String lastContent=(String)session.getAttribute(photoId);
if(content.equalsIgnoreCase(lastContent)){
	tip="此评论您已经发表!";
	}else{
	session.setAttribute(photoId,content);
	home.addReview(request);
	}
}
//获取相关评论
home.photoReview(request);
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector photoReviews=(Vector)request.getAttribute("photoReviews");         
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="图片评论">
<p align="left">
<%=BaseAction.getTop(request, response)%>
==相片评论==<br/>
<%if(tip!=null){%><%=tip%><br/><%}%>
<%
int count = photoReviews.size();
for(int i = 0; i < count; i ++){
	HomePhotoReviewBean review = (HomePhotoReviewBean) photoReviews.get(i);
	UserBean user=UserInfoUtil.getUser(review.getReviewUserId());
%>
<%=(Integer.parseInt(perPage)*pageIndex + i+1)%>.<%if(user==null){%>访客<%}else{%><%if(loginUser!=null && user.getId()==loginUser.getId()){%>我<%}else{%><a href="home2.jsp?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><%}}%>说:<%=StringUtil.toWml(review.getReview())%>(<%=review.getCreateTime()%>)
<%
int myphotoId=review.getPhotoId();
HomePhotoBean homephoto=(HomePhotoBean)homeService.getHomePhoto("id="+myphotoId);
userId=homephoto.getUserId();
int photouserId=homephoto.getUserId();
if(loginUser.getId()==photouserId){
%>
<a href="photoReview.jsp?delete=1&amp;reviewId=<%=review.getId()%>&amp;photoId=<%=photoId%>" >删除</a>
<%}%>
<br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "photoReview.jsp?photoId="+photoId, true, " ", response)%><%if(totalPage>1){%><br/><%}%>
<!--wucx4_判断是否是黑名单不能到该用户的家园留   日期:2006-11－24-->
<%
boolean isABadGuys=userService.isUserBadGuy(userId,loginUser.getId());
if(!isABadGuys){%>
<br/><input name="content"  maxlength="1000" value=""/><br/>
    <anchor title="确定">发表评论
    <go href="photoReview.jsp?cid=<%=catId%><%=uid > 0?"&amp;uid=" + uid:"" %>" method="post">
	<postfield name="photoId" value="<%=photoId%>"/>
	<postfield name="content" value="$content"/>
    </go>
    </anchor><br/><a href="homePhoto.jsp?cid=<%=catId %><%=uid > 0?"&amp;uid=" + uid:"" %>">返回</a><br/>
     <%}%>
<!-- wucx_4_判断是否是黑名单不能到该用户的家园留   日期:2006-11－24-->
<%if(returnURL!=null){%>
    <a href="<%=(returnURL.replace("&","&amp;"))%>">返回相册</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>