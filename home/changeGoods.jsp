<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeTypeBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.home.HomeBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageTypeBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.home.HomeUserImageBean" %><%@ page import="net.joycool.wap.service.infc.IHomeService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.home.HomeImageBean" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.changeGoods(request);
Vector homeVec=(Vector)request.getAttribute("homeVec");
int homeId=StringUtil.toInt((String)request.getAttribute("homeId"));
HomeTypeBean bean = (HomeTypeBean )request.getAttribute("bean");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
IHomeService homeService =ServiceFactory.createHomeService();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="更换家具">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(homeId<0){%>
请选择你要更换家具的房间：<br/>
<%
String[] roomIds = bean.getRoomIds().split("_");
for(int i=0;i<roomIds.length;i++){
HomeBean homeBean=(HomeBean)homeVec.get(i);
if(homeBean!=null){%>
<a href="/home/changeGoods.jsp?homeId=<%=homeBean.getId()%>"><%=homeBean.getName()%></a> 
<%}}%><br/>
<%}else{
if(action.getHome("id="+homeId)==null){
out.clearBuffer();
response.sendRedirect("changeGoods.jsp");
return;
}
String userImageUrl = action.getImageUrl(Constants.HOME_IMAGE_SMALL_TYPE,loginUser.getId(),homeId);
Vector userImage=(Vector)request.getAttribute("userImage");
Vector imageType=(Vector)request.getAttribute("imageType");%>
<a href="/home/seeBigImage.jsp?type=1&amp;homeId=<%=homeId%>"><img src="<%=(userImageUrl)%>" alt="家的图片"/></a><br/>
<%
String[] roomIds = bean.getRoomIds().split("_");
for(int i=0;i<roomIds.length;i++){
HomeBean homeBean=(HomeBean)homeVec.get(i);
if(homeBean!=null){
if(homeBean.getId()!=homeId){%>
<a href="/home/changeGoods.jsp?homeId=<%=homeBean.getId()%>"><%=homeBean.getName()%></a> 
<%}else{%>
<%=homeBean.getName()%>
<%}}}%><br/>
<%HomeBean homeBean=action.getHome("id="+homeId);
int avail=0;
for(int i=0;i<imageType.size();i++){
avail=0;
HomeImageTypeBean image=(HomeImageTypeBean)imageType.get(i);%>
<%=i+1%>.<%=image.getName()%>
<%
for(int j=0;j<userImage.size();j++)
{HomeUserImageBean homeUser=(HomeUserImageBean)userImage.get(j);
if(homeUser.getTypeId()==image.getId()){
HomeImageBean homeI=homeService.getHomeImage("id="+homeUser.getImageId());
if(homeI!=null){
avail=1;%>
<%=homeI.getName()%>
<%}}}%>
<a href="/home/goodsList.jsp?typeId=<%=image.getId()%>&amp;homeId=<%=homeBean.getId()%>">更换</a><%if(avail==1){%>|<a href="<%=("/home/goodsList.jsp?typeId="+image.getId()+"&amp;homeId="+homeBean.getId()+"&amp;goods=1")%>">放入仓库</a><%}%> 
<br/>
<%}}%>
<a href="/home/myStore.jsp">购买家具</a><br/>
<a href="/home/myStore.jsp">查看仓库</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>