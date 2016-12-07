<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeImageBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.buyFitment(request);
String userId=(String)request.getAttribute("userId");
String result=(String) request.getAttribute("result");
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
}else if("refrush".equals(result)){
out.clearBuffer();
response.sendRedirect("/home/myStore.jsp");
return;
}else if("searchError".equals(result)){
%>
<card title="家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="home.jsp">我的家园</a><br/>
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("goodsError".equals(result)){
%>
<card title="家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="home.jsp">我的家园</a><br/>
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
<a href="home.jsp">我的家园</a><br/> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("buyOk".equals(result)){
String url = ("myStore.jsp");
%>
<card title="我的家园" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买成功!(3秒后跳转至买家具首页)<br/>
<a href="myStore.jsp">购买家具</a><br/> 
<a href="changeGoods.jsp">更换家具</a><br/>
<a href="homeManage.jsp">管理我的家园</a><br/> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("parameterError".equals(result)){
out.clearBuffer();
response.sendRedirect("myStore.jsp");
return;
}else if("success".equals(result)){
session.setAttribute("buyFitment","buyFitment");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector homeImageList=(Vector)request.getAttribute("homeImageList");
%>
<card title="购买家具">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择:<br/>
<%
HomeImageBean homeImange=null;
for(int i=0;i<homeImageList.size();i++){
homeImange=(HomeImageBean)homeImageList.get(i);
%>
<%=i+1%>.<%=homeImange.getName()%>(<%=homeImange.getPrice()/10000%>万乐币) 
<a href="buyFitment.jsp?imageId=<%=homeImange.getId()%>">购买</a>|<a href="/home/present.jsp?fromBuyPage=1&amp;imageId=<%=homeImange.getId()%>">赠送</a><br/>
<img src="/img/home/model/<%=homeImange.getFile()%>" alt="家的图片"/><br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
 }
%>

<a href="myStore.jsp">其他家具</a><br/> 
<a href="home.jsp">我的家园</a><br/> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>