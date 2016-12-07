<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeUserImageBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageBean" %><%@ page import="net.joycool.wap.bean.home.HomeImageTypeBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.List" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
if(request.getParameter("sale")!=null){	// 售卖家具有漏洞
	response.sendRedirect("/bottom.jsp");
	return;
}
action.myStoreSort(request);
String result=(String) request.getAttribute("result");
String typeId=(String) request.getParameter("typeId");
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
}else if("typeError".equals(result)){%>
<card title="我的仓库">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/home/myStore.jsp">返回我的仓库</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%>
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if("searchError".equals(result)){
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
<%=request.getAttribute("tip")%><br/>
<a href="/home/myStore.jsp">我的仓库</a><br/> 
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
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List homeUserImageList=(List)request.getAttribute("homeUserImageList");
HomeImageTypeBean homeImageType=(HomeImageTypeBean)request.getAttribute("homeImageType");
%>
<card title="我的仓库">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(homeUserImageList.size()>0){
%><%=homeImageType.getName() %>类商品:<br/><%
HomeUserImageBean homeUserImage=null;
HomeImageBean homeImage=null;
int j=1;
for(int i=0;i<homeUserImageList.size();i++){
homeUserImage=(HomeUserImageBean)homeUserImageList.get(i);
homeImage=action.getHomeImage(homeUserImage.getImageId());
if(homeImage.getTypeId()==Constants.HOME_IMAGE_BACKGROUND_TYPE)
continue;
%>
<%=j%>.<%=homeImage.getName()%>(<%=homeImage.getPrice()/10000%>万乐币)
<a href="<%=("/home/myStoreSort.jsp?id="+homeUserImage.getId()+"&amp;typeId="+typeId+"&amp;imageId="+homeImage.getId()+"&amp;sale=1")%>">出售</a>|
<a href="/home/myStoreSort.jsp?id=<%=homeUserImage.getId()%>&amp;typeId=<%=typeId%>&amp;imageId=<%=homeImage.getId()%>">扔掉</a>|
<a href="/home/present.jsp?imageId=<%=homeImage.getId()%>">赠送</a><br/>
<img src="/img/home/model/<%=homeImage.getFile()%>" alt="家的图片"/><br/>
<%j++;}String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}}else{%>您还没有家具!<br/><%}%>
<a href="/home/myStore.jsp">返回我的仓库</a><br/>
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