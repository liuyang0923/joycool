<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeImageBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.home.HomeBean" %><%@ page import="net.joycool.wap.bean.home.HomeTypeBean" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.adornmentRoom(request);
String result=(String) request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
Vector homeVec=(Vector)request.getAttribute("homeVec");
HomeTypeBean bean = (HomeTypeBean )request.getAttribute("bean");
int homeId=StringUtil.toInt((String)request.getAttribute("homeId"));
int homeTypeId=StringUtil.toInt((String)request.getAttribute("homeTypeId"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//添加评论
if("failure".equals(result)){
%>
<card title="注册个人家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="inputRegisterInfo.jsp">（隆重）开通我的个人家园，让朋友们来做客！</a><br/> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("error".equals(result)){
%>
<card title="我的家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="homeManage.jsp">管理我的家园</a><br/> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if(homeTypeId<0){%>
<card title="家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择你要装修的房间：<br/>
<%
String[] roomIds=bean.getRoomIds().split("_");
for(int i=0;i<roomIds.length;i++){
HomeBean homeBean=(HomeBean)homeVec.get(i);
if(homeBean!=null){%>
<a href="adornmentRoom.jsp?homeTypeId=<%=homeBean.getTypeId()%>&amp;homeId=<%=homeBean.getId()%>"><%=homeBean.getName()%></a> <br/>
<%}}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if("refrush".equals(result)){
out.clearBuffer();
response.sendRedirect("adornmentRoom.jsp");
return;
}else if("searchError".equals(result)||"error".equals(result)){
%>
<card title="家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("moneyError".equals(result)){
%>
<card title="家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/lswjs/wagerHall.jsp">去赌场赚钱</a><br/> 
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("buyOk".equals(result)){
String url = ("/home/adornmentRoom.jsp");
HomeBean home = (HomeBean)request.getAttribute("home");
%>
<card title="我的家园" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>
更新成功!(3秒后跳转至装修房间首页)<br/>
<a href="seeBigImage.jsp?type=1&amp;homeId=<%=home.getId()%>">查看房间大图</a><br/> 
<a href="homeManage.jsp">管理我的家园</a><br/> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("success".equals(result)){
session.setAttribute("adornmentRoom","adornmentRoom");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector homeImangeList=(Vector)request.getAttribute("homeImangeList");
%>
<card title="">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String[] roomIds=bean.getRoomIds().split("_");
for(int i=0;i<roomIds.length;i++){
HomeBean homeBean=(HomeBean)homeVec.get(i);
if(homeBean!=null){
if(homeBean.getId()!=homeId){%>
<a href="adornmentRoom.jsp?homeTypeId=<%=homeBean.getTypeId()%>&amp;homeId=<%=homeBean.getId()%>"><%=homeBean.getName()%></a> 
<%}else{%>
<%=homeBean.getName()%>
<%}}}%><br/>
<%String userImageUrl = action.getImageUrl(Constants.HOME_IMAGE_SMALL_TYPE,loginUser.getId(),homeId);%>
<a href="seeBigImage.jsp?type=1&amp;homeId=<%=homeId%>&amp;unique=11652125596877">
<img src="<%=(userImageUrl)%>" alt="家的图片"/></a><br/>
请选择你要的图片：<br/>
<%
HomeImageBean homeImange=null;
for(int i=0;i<homeImangeList.size();i++){
homeImange=(HomeImageBean)homeImangeList.get(i);
%>
<%=i+1%>.<%=homeImange.getName()%>(<%=homeImange.getPrice()%>乐币) <a href="/home/adornmentRoom.jsp?homeTypeId=<%=homeTypeId%>&amp;homeId=<%=homeId%>&amp;imageId=<%=homeImange.getId()%>">购买</a><br/>
<img src="/img/home/small/<%=homeImange.getFile()%>" alt="家的图片"/><br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
 }
%>
<a href="homeManage.jsp">管理我的家园</a><br/> 
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>