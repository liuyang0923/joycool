<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.UserBagBean" %><%
response.setHeader("Cache-Control","no-cache");
%><%@include file="../bank/checkpw.jsp"%><%
UserBagAction action = new UserBagAction(request);
action.userBagResult(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
DummyProductBean dummyProduct=(DummyProductBean)request.getAttribute("dummyProduct");
UserBagBean userBag=(UserBagBean)request.getAttribute("userBag");
String url=("/user/userBag.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}if(result.equals("refrush")){
out.clearBuffer();
response.sendRedirect("userBag.jsp");
return;
}else if(result.equals("present")){
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List userFriendList=(List)request.getAttribute("userFriendList");
%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您要把<%=dummyProduct.getName()%><br/>
赠送给您的好友:<br/>
<%
String userId=null;
UserBean user = null;
for(int i=0;i<userFriendList.size();i++){ 
     userId=(String)userFriendList.get(i);
     user=UserInfoUtil.getUser(StringUtil.toInt(userId));
     if(user==null){continue;}
     if(user.getId()==loginUser.getId())
     continue;%>
<%=i+1 %>.<a href="/user/presentResult.jsp?friendId=<%=user.getId()%>&amp;userBagId=<%=userBag.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("compose")){%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
合成的同时将从行囊中移除所需的材料物品，合成后的物品也将自动放入行囊。<br/>
<a href="/user/userBagResult.jsp?compose2=1&amp;userBagId=<%=userBag.getId()%>">确定并立即合成</a><br/>
<a href="<%=url%>">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("composeResult")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="<%=url%>">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("request")){

%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
合成的同时将从行囊中移除所需的材料物品，合成后的物品也将自动放入行囊。<br/>
<a href="/user/userBagResult.jsp?compose2=1&amp;userBagId=<%=userBag.getId()%>">确定并立即合成</a><br/>
<a href="<%=url%>">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("requestResult")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="<%=url%>">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("lose")){// 确认丢弃页面%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="userBagResult.jsp?lose=2&amp;userBagId=<%=userBag.getId()%>">好吧，给你</a><br/>
<a href="<%=url%>">不丢了，决定留着</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%=(String)(request.getAttribute("tip"))%><br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>