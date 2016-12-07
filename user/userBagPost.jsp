<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBagBean" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%
response.setHeader("Cache-Control","no-cache");
UserBagAction action = new UserBagAction(request);
action.userBagPost(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/user/userBag.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="使用道具" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List userBagList=(List)request.getAttribute("userBagList");
String userId = (String) request.getAttribute("userId");
String roomId = (String) request.getAttribute("roomId");
%>
<card title="使用道具">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(userBagList.size()>0){
UserBagBean userBag = null;
DummyProductBean dummyProduct = null;
int j=1;
for(int i=0;i<userBagList.size();i++){
    userBag=(UserBagBean)userBagList.get(i);
    dummyProduct=action.getDummyProduct(userBag.getProductId());
    if(dummyProduct==null || dummyProduct.getId()==11)continue;
    %>
<%=j%>.<a href="/user/userBagPostResult.jsp?roomId=<%=roomId%>&amp;userBagId=<%=userBag.getId()%>&amp;userId=<%=userId%>"><%=dummyProduct.getName()%></a>
<%if(dummyProduct.getTime()>1){%>(<%=action.getUserBag(userBag.getId()).getTime()%>/<%=dummyProduct.getTime()%>)<%} %><br/>
<%j++;}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<%}else{%>没有可供选择的物品!<br/><%}%>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>