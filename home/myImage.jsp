<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeUserImageBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageTypeBean" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.myImage(request);
String userId=(String)request.getAttribute("userId");
String result=(String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if("failure".equals(result)){
%>
<card title="注册个人家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/home/inputRegisterInfo.jsp">（隆重）开通我的个人家园，让朋友们来做客！</a><br/> 
<a href="/home/viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("refrush".equals(result)){
out.clearBuffer();
response.sendRedirect("myImage.jsp");
return;
}else if("searchError".equals(result)){
%>
<card title="家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/home/home.jsp">我的家园</a><br/>
<a href="/home/viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("deleteOk".equals(result)){
String fitmentName=(String)request.getAttribute("fitmentName");
String gamePoint=(String)request.getAttribute("gamePoint");
String url = ("/home/myImage.jsp");
%>
<card title="我的家园" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您的<%=fitmentName%>已按照原价50％出售！（3秒钟跳转）<br/>
共获得<%=gamePoint%>乐币！<br/>
<a href="/home/seeBigImage.jsp?type=1">查看房间大图</a><br/> 
<a href="/home/homeManage.jsp">管理我的家园</a><br/> 
<a href="/home/viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("parameterError".equals(result)){
out.clearBuffer();
response.sendRedirect("myImage.jsp");
return;
}else if("success".equals(result)){
session.setAttribute("myImage","myImage");
Vector homeUserImageList=(Vector)request.getAttribute("homeUserImageList");
Vector homeImageTypeList=(Vector)request.getAttribute("homeImageTypeList");
%>
<card title="购买家具">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(homeUserImageList.size()>1){
%>您现在的家具有:<br/><%
HomeUserImageBean homeUserImage=null;
HomeImageBean homeImage=null;
int j=1;
for(int i=0;i<homeUserImageList.size();i++){
homeUserImage=(HomeUserImageBean)homeUserImageList.get(i);
homeImage=action.getHomeImage(homeUserImage.getImageId());
if(homeImage.getTypeId()==Constants.HOME_IMAGE_BACKGROUND_TYPE)
continue;
%>
<%=j%>.<%=homeImage.getName()%>(<%=homeImage.getPrice()%>乐币) <a href="/home/myImage.jsp?imageId=<%=homeImage.getId()%>">删除</a><br/>
<img src="/img/home/model/<%=homeImage.getFile()%>" alt="家的图片"/><br/>
<%j++;}}else{%>您还没有家具!<br/><%}%>
您还需要什么:<br/>
<%
HomeImageTypeBean homeImageType=null;
for(int i=0;i<homeImageTypeList.size();i++){
homeImageType=(HomeImageTypeBean)homeImageTypeList.get(i);
if(homeImageType.getId()==Constants.HOME_IMAGE_BACKGROUND_TYPE)
continue;
%>
<%=i+1%>.<a href="/home/buyFitment.jsp?typeId=<%=homeImageType.getId()%>"><%=homeImageType.getName()%></a><br/>
<%}%>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%>
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>