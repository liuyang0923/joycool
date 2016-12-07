<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeUserImageBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageTypeBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.List" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.myStore(request);
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
response.sendRedirect("myStore.jsp");
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
String url = ("/home/myStore.jsp");
%>
<card title="我的家园" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您的小木凳已按照原价50％出售！（3秒钟跳转）
共获得500乐币！
<br/>
<a href="/home/seeBigImage.jsp?type=1">查看房间大图</a><br/> 
<a href="/home/homeManage.jsp">管理我的家园</a><br/> 
<a href="/home/viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("parameterError".equals(result)){
out.clearBuffer();
response.sendRedirect("myStore.jsp");
return;
}else if("success".equals(result)){
session.setAttribute("myStore","myStore");
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
List homeUserImageTypeList=(List)request.getAttribute("homeUserImageTypeList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List homeImageTypeList=(List)request.getAttribute("homeImageTypeList");
%>
<card title="我的仓库">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(homeUserImageTypeList.size()>0){
%>我的仓库:<br/><%
Integer typeId=null;
HomeImageTypeBean homeImageType=null;
int j=0;
for(int i=0;i<homeUserImageTypeList.size();i++){
typeId=(Integer)homeUserImageTypeList.get(i);
homeImageType=action.getHomeImageType(typeId.intValue());
if(homeImageType==null)continue;
%>
<%=j+1%>.<a href="/home/myStoreSort.jsp?typeId=<%=homeImageType.getId()%>"><%=homeImageType.getName()%></a><br/>
<%j++;}String fenye1 = action.shuzifenye(totalPageCount1, pageIndex1, prefixUrl1, false, "|", "pageIndex1" , response);
if(!"".equals(fenye1)){%><%=fenye1%><br/><%}}else{%>您的仓库中没有物品!<br/><%}%>
您还需购买哪类商品:<br/>
<%
HomeImageTypeBean homeImageType=null;
for(int i=0;i<homeImageTypeList.size();i++){
homeImageType=(HomeImageTypeBean)homeImageTypeList.get(i);
if(homeImageType.getId()==Constants.HOME_IMAGE_BACKGROUND_TYPE)
continue;
%>
<%=i+1%>.<a href="/home/buyFitment.jsp?typeId=<%=homeImageType.getId()%>"><%=homeImageType.getName()%></a><br/>
<%}String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/home/changeGoods.jsp">更换家具</a><br/>
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