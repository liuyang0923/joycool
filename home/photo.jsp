<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
//当前在线用户信息
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
//dao 
IHomeService homeService =ServiceFactory.createHomeService();
//action
HomeAction home=new HomeAction(request);      
home.viewHomePhoto(request);
//人气排行
int perPage=StringUtil.toInt((String)request.getAttribute("NUM_PER_PAGE"));
int totalCount=StringUtil.toInt((String)request.getAttribute("totalCount"));
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector hitsPhoto=(Vector)request.getAttribute("hitsPhoto");
//新日记排行
String totalCount1=(String)request.getAttribute("totalCount1");
int totalPage1=StringUtil.toInt((String)request.getAttribute("totalPage1"));
int pageIndex1=StringUtil.toInt((String)request.getAttribute("pageIndex1"));
Vector newPhoto=(Vector)request.getAttribute("newPhoto");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐客相册">
<p align="left"><%=BaseAction.getTop(request, response)%>
乐客相册<br/>
---------<br/>
==人气排行==<br/>
<%
int count = hitsPhoto.size();
for(int i = 0; i < count; i ++){
	HomePhotoBean homePhoto=(HomePhotoBean)hitsPhoto.get(i);
	int viewCount=homeService.getHomePhotoReviewCount("photo_id="+homePhoto.getId());
%>
<%=perPage*pageIndex + i+1%>.
<a href="homePhoto.jsp?userId=<%=homePhoto.getUserId()%>&amp;hit=<%=homePhoto.getId()%>"><%=StringUtil.toWml(homePhoto.getTitle())%></a>  
(点击<%=homePhoto.getHits()%> | 评论<%=viewCount%>)<br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "photo.jsp", false, " ", response)%><%if(totalPage>1){%><br/><%}%>
==新增照片==<br/>
<%
count = newPhoto.size();
for(int i = 0; i < count; i ++){
	HomePhotoBean homePhoto=(HomePhotoBean)newPhoto.get(i);
	int viewCount=homeService.getHomePhotoReviewCount("photo_id="+homePhoto.getId());
%>
<%=perPage*pageIndex1 + i+1%>.
<a href="homePhoto.jsp?userId=<%=homePhoto.getUserId()%>&amp;hit=<%=homePhoto.getId()%>"><%=StringUtil.toWml(homePhoto.getTitle())%></a>  
(点击<%=homePhoto.getHits()%> | 评论<%=viewCount%>)<br/>
<%
}
%>
<%=PageUtil.shuzifenye1(totalPage1, pageIndex1, "photo.jsp", false, " ", response)%><%if(totalPage1>1){%><br/><%}%>
搜索家园<br/>
用户ID：<input name="userId" format="*N"  maxlength="20" value=""/> <anchor title ="search Image">Go
         <go href="searchHome.jsp" method="post">
             <postfield name="userId" value="$(userId)"/>
         </go>
         </anchor><br/>
<a href="diary.jsp">乐客日记</a><br/>
<a href="viewAllHome.jsp">*家园之星*</a><br/>         
<%if(loginUser!=null && loginUser.getHome()==1){%>
<a href="home.jsp">返回我的家园</a><br/>
<%}else{%>
<a href="inputRegisterInfo.jsp">我也要建立家园</a><br/> 
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>