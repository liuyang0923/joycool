<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
//设置评论返回参数
String returnURL=PageUtil.getCurrentPageURL(request);
session.setAttribute("returnURL",returnURL);

UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
//dao,action
IHomeService homeService =ServiceFactory.createHomeService();
IUserService userService =ServiceFactory.createUserService();
HomeAction home=new HomeAction(request);      
home.viewCommendPhoto(request);
//推荐相册
int perPage=StringUtil.toInt((String)request.getAttribute("NUM_PER_PAGE"));
int totalCount=StringUtil.toInt((String)request.getAttribute("totalCount"));
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector commendPhoto=(Vector)request.getAttribute("commendPhoto");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="推荐相册">
<p align="left">
<%=BaseAction.getTop(request, response)%>
通吃岛家园推荐相册(共<%=totalCount%>个)<br/>
--------------------<br/>
<%
int count = commendPhoto.size();
for(int i = 0; i < count; i ++){
	HomePhotoBean photo = (HomePhotoBean) commendPhoto.get(i);
%><%=perPage*pageIndex+i+1 %>.
<%=StringUtil.toWml(photo.getTitle())%> (点击<%=photo.getHits()%>|<anchor >评论<%=photo.getReviewCount()%><go href="/home/photoReview.jsp" method="post"><postfield name="photoId" value="<%=photo.getId()%>"/></go></anchor>)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;<img src="/rep<%=photo.getAttach()%>" alt="o"/><br/>
<%}%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "viewCommendPhoto.jsp", false, " ", response)%><%if(totalPage>1){%><br/><%}%>
<a href="/home/diaryTop.jsp">乐客日记</a><br/>
<a href="/home/photoTop.jsp">乐客相册</a><br/>   
<a href="/home/viewAllHome.jsp">*家园之星*</a><br/>       
<%if(loginUser!=null && loginUser.getHome()==1){%>
<a href="/home/home.jsp">返回我的家园</a><br/>
<%}else{%>
<a href="/home/inputRegisterInfo.jsp">我也要建立家园</a><br/> 
<%}%>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>