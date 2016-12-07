<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.chat.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.friendadver.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
//设置本页为图片评论的返回页
String returnURL=PageUtil.getCurrentPageURL(request);
session.setAttribute("returnURL",returnURL);
//当前用户信息
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//action
IHomeService homeService =ServiceFactory.createHomeService();
HomeAction home=new HomeAction(request);      
home.getPopPhotos(request);
//获取热门图片
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector photos=(Vector)request.getAttribute("photos");         
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="热门网友图片">
<p align="left"><%=BaseAction.getTop(request, response)%>
热门网友图片：<br/>
<%
int count = photos.size();
for(int i = 0; i < count; i ++){
	HomePhotoBean photo = (HomePhotoBean) photos.get(i);
	int viewCount=homeService.getHomePhotoReviewCount("photo_id="+photo.getId());
%>
<%=(Integer.parseInt(perPage)*Integer.parseInt(pageIndex) + i+1)%>.
<%if(loginUser!=null&& loginUser.getId()==photo.getUserId()){%><%=StringUtil.toWml(photo.getUser().getNickName())%><%}else{%><a href="/user/ViewUserInfo.do?userId=<%=photo.getUserId()%>"><%=StringUtil.toWml(photo.getUser().getNickName())%></a><%}%>&nbsp;<%=photo.getUser().getGender()==0?"女":"男"%>,<%=photo.getUser().getAge()%>(点击<%=photo.getHits()%>|<anchor>评论<%=viewCount%><go href="/home/photoReview.jsp" method="post"><postfield name="photoId" value="<%=photo.getId()%>"/></go></anchor>)<br/>
<a href="/home/homePhoto.jsp?userId=<%=photo.getUserId()%>&amp;hit=<%=photo.getId()%>"><img src="/rep<%=photo.getAttach()%>" alt="o"/></a><br/>
<%
}
%>
<%if(Integer.parseInt(totalPage)>1){if(pageIndex.equals("0")){%>上一页|<a href="/home/popPhotos.jsp?pageIndex=1">下一页</a><%}else{%><a href="/home/popPhotos.jsp?pageIndex=0">上一页</a>|下一页<%}}%>
<br/>
<a href="/home/home.jsp">我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>