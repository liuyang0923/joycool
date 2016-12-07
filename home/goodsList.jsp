<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeTypeBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.home.HomeBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageTypeBean" %><%@ page import="net.joycool.wap.bean.home.HomeUserImageBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageBean" %><%@ page import="net.joycool.wap.service.infc.IHomeService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.goodsList(request);
String typeId=(String)request.getAttribute("typeId");
String homeId=(String)request.getAttribute("homeId");
if(action.getHome("id="+homeId)==null)
{
//response.sendRedirect(("/home/changeGoods.jsp"));
BaseAction.sendRedirect("/home/changeGoods.jsp", response);
return;
}
if(request.getAttribute("over")!=null)
{
//response.sendRedirect(("/home/changeGoods.jsp?homeId="+homeId));
BaseAction.sendRedirect("/home/changeGoods.jsp?homeId="+homeId, response);
return;
}
Vector homeVec=(Vector)request.getAttribute("homeVec");
if(homeVec==null)
{
//response.sendRedirect(("/home/changeGoods.jsp"));
BaseAction.sendRedirect("/home/changeGoods.jsp", response);
return;
}
HomeImageTypeBean bean=(HomeImageTypeBean)request.getAttribute("bean");
IHomeService homeService =ServiceFactory.createHomeService();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String userImageUrl = action.getImageUrl(Constants.HOME_IMAGE_SMALL_TYPE,loginUser.getId(),StringUtil.toInt(homeId));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="更换家具">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/home/seeBigImage.jsp?type=1&amp;homeId=<%=homeId%>"><img src="<%=(userImageUrl)%>" alt="家的图片"/></a><br/>
<% if(homeVec.size()==0)
{%>
现在没有可以更换的<%if(bean!=null){%><%=bean.getName()%>！<br/>
<a href="/home/buyFitment.jsp?typeId=<%=bean.getId()%>">购买<%=bean.getName()%></a><br/><%}%>
<%}else{%>
请选择要更换的<%if(bean!=null){%><%=bean.getName()%><%}%>:<br/>
<%for(int i=0;i<homeVec.size();i++){
HomeUserImageBean homeBean=(HomeUserImageBean)homeVec.get(i);{
if(homeBean!=null){
HomeImageBean image=homeService.getHomeImage("id="+homeBean.getImageId());
if(image!=null){
%>
<%=i+1%>.
<%=image.getName()%>
<a href="/home/goodsList.jsp?imageId=<%=homeBean.getId()%>&amp;typeId=<%=typeId%>&amp;homeId=<%=homeId%>">更换</a> 
<br/>
<img src="/img/home/model/<%=image.getFile()%>" alt="家具的图片"/><br/>
<%}}}%>
<br/>
<%}}%>
<a href="/home/changeGoods.jsp?homeId=<%=homeId%>">返回上一级</a><br/>
<a href="/home/myStore.jsp">购买更多家具</a><br/>
<a href="/home/myStore.jsp">我的仓库</a><br/>
<a href="/home/home.jsp">我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>