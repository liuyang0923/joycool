<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeTypeBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.home.HomeUserBean" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.buyHome(request);
Vector homeTypeList=(Vector)request.getAttribute("homeTypeList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
HomeUserBean homeUser=(HomeUserBean)request.getAttribute("homeUser");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="买新房子">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/home/house/<%=homeUser.getTypeId()%>.gif" alt="家的图片"/><br/>
请选择你要的房屋：<br/>
<%for(int i=0;i<homeTypeList.size();i++){
HomeTypeBean type=(HomeTypeBean)homeTypeList.get(i);
if(type!=null){
%>
<%=i+1%>.<%=type.getName()%>(<%=type.getMoney()/10000%>万乐币)<br/>
<img src="<%=type.getBigImageUrl()%>" alt="<%=type.getName()%>"/>
<%if(type.getId()==homeUser.getTypeId()){%>
您现在居住的房子.<br/>
<%}else{%>
<a href="/home/buyHomeResult.jsp?buy=<%=type.getId()%>">更改</a><br/>
<%}%>
<%}}%>
<%String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){%>
<%=fenye%><br/>
<%}%>
<a href="/home/home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>