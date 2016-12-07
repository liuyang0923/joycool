<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeReviewBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%!
static IUserService service = ServiceFactory.createUserService(); %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.homeReview(request);
String result=(String) request.getAttribute("result");
Integer iid=(Integer)request.getAttribute("userId");
int userId = (iid == null) ? 0 : iid.intValue();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("failure".equals(result)){
%>
<card title="乐酷家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="inputRegisterInfo.jsp">（隆重）开通我的个人家园，让朋友们来做客！</a><br/> 
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("refrush".equals(result)){
out.clearBuffer();
response.sendRedirect("homeReview.jsp?userId="+userId);
return;
} else if("addSuccess".equals(result)){
String url = ("homeReview.jsp?userId="+userId);
%>
<card title="添加评论" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<%
UserBean user = UserInfoUtil.getUser(userId);
%>
<a href="home.jsp?userId=<%=userId%>" >返回<%= StringUtil.toWml(user.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
session.setAttribute("homeReview","homeReview");
%>
<card title="访客留言">
<p align="left">
<%=BaseAction.getTop(request, response)%>
===访客留言===<br/>
<%
Vector homeReviewList=(Vector)request.getAttribute("homeReviewList");
PagingBean paging = (PagingBean) request.getAttribute("paging");
HomeReviewBean homeReview=null;
for(int i=0;i<homeReviewList.size();i++){
homeReview=(HomeReviewBean)homeReviewList.get(i);
//liuyi 2006-12-14 start
if(homeReview==null)continue;
UserBean reviewUser = action.getUser(homeReview.getReviewUserId());
if(reviewUser==null)continue;
%>
<%=i+1%>.<%
if(reviewUser.getId()==loginUser.getId()){   
//	liuyi 2006-12-14 end
%>我<%}else{%><a href="home2.jsp?userId=<%=homeReview.getReviewUserId()%>"><%=StringUtil.toWml(action.getUser(homeReview.getReviewUserId()).getNickName())%></a>
<%}%>:<%=StringUtil.toWml(homeReview.getReview())%>(<%=homeReview.getCreateDdatetime()%>)
<%if(loginUser.getId()==homeReview.getUserId()){
%><a href="homeReview.jsp?delete=<%=homeReview.getId()%>" >删</a>
<%}%><br/><%}%>
<%=paging.shuzifenye("homeReview.jsp?userId="+userId, true, "|", response)%>
<br/>
<%
boolean isABadGuys=service.isUserBadGuy(userId,loginUser.getId());
if(!isABadGuys){%>
<input name="review" maxlength="100" value=""/><br/>
<anchor title="确定">发表留言
<go href="homeReview.jsp?userId=<%=userId%>" method="post">
  <postfield name="review" value="$review"/>
</go></anchor><br/>
<%}
// liuyi 2006-12-26 返回家园首页修改 start 
UserBean user = UserInfoUtil.getUser(userId);
%><a href="home.jsp?userId=<%=userId%>" >返回<%if(user.getId()==action.getLoginUser().getId()){%>我<%}else{%><%=user.getNickNameWml()%><%}%>的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>