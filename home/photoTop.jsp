<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IHomeService" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomePhotoBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
IHomeService homeService =ServiceFactory.createHomeService();
HomeAction action = new HomeAction(request);
action.photoTop(request);
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector photoMark=(Vector)request.getAttribute("photoMark");
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
Vector photoTotalTop=(Vector)request.getAttribute("photoTotalTop");
int totalPageCount2 = ((Integer) request.getAttribute("totalPageCount2")).intValue();
int pageIndex2 = ((Integer) request.getAttribute("pageIndex2")).intValue();
String prefixUrl2 = (String) request.getAttribute("prefixUrl2");
Vector photoTop=(Vector)request.getAttribute("photoTop");
int totalPageCount3 = ((Integer) request.getAttribute("totalPageCount3")).intValue();
int pageIndex3 = ((Integer) request.getAttribute("pageIndex3")).intValue();
String prefixUrl3 = (String) request.getAttribute("prefixUrl3");
Vector photoNew=(Vector)request.getAttribute("photoNew");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="推荐照片">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/photo.gif" alt="乐客照片"/><br/>
==推荐照片==<br/>
<%
HomePhotoBean homePhoto=null;
for(int i=0;i<photoMark.size();i++){
homePhoto=(HomePhotoBean)photoMark.get(i);
%>
<img src="<%=Constants.IMG_ROOT_URL%><%=homePhoto.getAttach()%>" alt="网友照片"/>&nbsp;
<%}%><br/><%
String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<%--=BaseAction.getAdver(14,response)--%>
==总人气排行==<br/>
<%
for(int i=0;i<photoTotalTop.size();i++){
homePhoto=(HomePhotoBean)photoTotalTop.get(i);
%>
<%=i+1%>.<a href="homePhoto.jsp?userId=<%=homePhoto.getUserId()%>&amp;hit=<%=homePhoto.getId()%>"><%=StringUtil.toWml(homePhoto.getTitle())%></a>(点击<%=homePhoto.getHits()%> | 评论<%=homePhoto.getReviewCount()%>)<br/>
<%}
String fenye1 = action.shuzifenye(totalPageCount1, pageIndex1, prefixUrl1, true, "|", "pageIndex1" ,  response);
if(!"".equals(fenye1)){%><%=fenye1%><br/><%}%>
==每日人气排行==<br/>
<%
for(int i=0;i<photoTop.size();i++){
homePhoto=(HomePhotoBean)photoTop.get(i);
%>
<%=i+1%>.<a href="homePhoto.jsp?userId=<%=homePhoto.getUserId()%>&amp;hit=<%=homePhoto.getId()%>"><%=StringUtil.toWml(homePhoto.getTitle())%></a>(点击<%=homePhoto.getHits()%> | 评论<%=homePhoto.getReviewCount()%>)<br/>
<%}
String fenye2 = action.shuzifenye(totalPageCount2, pageIndex2, prefixUrl2, true, "|", "pageIndex2" ,  response);
if(!"".equals(fenye2)){%><%=fenye2%><br/><%}%>
==新增照片==<br/>
<%
for(int i=0;i<photoNew.size();i++){
homePhoto=(HomePhotoBean)photoNew.get(i);
%>
<%=i+1%>.<a href="homePhoto.jsp?userId=<%=homePhoto.getUserId()%>&amp;hit=<%=homePhoto.getId()%>"><%=StringUtil.toWml(homePhoto.getTitle())%></a>(点击<%=homePhoto.getHits()%> | 评论<%=homePhoto.getReviewCount()%>)<br/>
<%}
String fenye3 = action.shuzifenye(totalPageCount3, pageIndex3, prefixUrl3, true, "|", "pageIndex3" , response);
if(!"".equals(fenye3)){%><%=fenye3%><br/><%}%>
搜索家园<br/>
用户ID：<input name="userId" format="*N"  maxlength="20" value=""/> <anchor title ="search Image">Go
         <go href="searchHome.jsp" method="post">
             <postfield name="userId" value="$(userId)"/>
         </go>
         </anchor><br/>
<a href="diaryTop.jsp">乐客日记</a><br/>
<a href="viewAllHome.jsp">*家园之星*</a><br/>         
<%if(loginUser!=null && loginUser.getHome()==1){%>
<a href="home.jsp">返回我的家园</a><br/>
<%}else{%>
<a href="inputRegisterInfo.jsp">我也要建立家园</a><br/> 
<%}%>
<%--=BaseAction.getAdver(15,response)--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>