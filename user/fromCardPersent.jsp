<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.UserBagBean" %><%
response.setHeader("Cache-Control","no-cache");
session.removeAttribute("userBagUser");
UserBagAction action = new UserBagAction(request);
action.fromCardPersent(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
DummyProductBean dummyProduct=(DummyProductBean)request.getAttribute("dummyProduct");
String url=("/job/card/buyCard.jsp");
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
<%}else{
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
UserBean user = null;
for(int i=0;i<userFriendList.size();i++){ 
     user=(UserBean)userFriendList.get(i);
     if(user.getId()==loginUser.getId())
     continue;%>
<%=i+1 %>.<anchor title="确定"><%=StringUtil.toWml(user.getNickName())%>
  <go href="fromCardPersendResult.jsp" method="post">
    <postfield name="friendId" value="<%=user.getId()%>"/>
    <postfield name="productId" value="<%=dummyProduct.getId()%>"/>
  </go>
</anchor><br/>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>