<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryReviewBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
int diaryReviewId=StringUtil.toInt(request.getParameter("diaryReviewId"));
String delete=request.getParameter("delete");
if(delete!=null&&diaryReviewId>0){
action.deleteHomeDiaryReview(request);
}
action.diaryReview(request);
String result=(String) request.getAttribute("result");
String userId=(String)request.getAttribute("userId");
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
String diaryId=(String)request.getAttribute("diaryId");
out.clearBuffer();
response.sendRedirect("homeDiaryReview.jsp?userId="+userId+"&diaryId="+diaryId);
return;
}
if("addSuccess".equals(result)){ 
String diaryId=(String)request.getAttribute("diaryId");
String url = ("homeDiaryReview.jsp?diaryId="+diaryId+"&amp;userId="+userId);
%>
<card title="添加评论" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="homeDiary.jsp?diaryId=<%=diaryId%>&amp;userId=<%=userId%>">返回日记</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<%
UserBean user = UserInfoUtil.getUser(StringUtil.toInt(userId));
%>
<a href="home.jsp?userId=<%=userId%>" >返回<%= StringUtil.toWml(user.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
session.setAttribute("diaryReview","diaryReview");
HomeDiaryBean homeDiary =(HomeDiaryBean)request.getAttribute("homeDiary");
if(homeDiary!=null){
%>
<card title="<%=StringUtil.toWml(homeDiary.getTitel())%>的相关评论">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(homeDiary.getTitel())%>的相关评论<br/>
---------------------------------<br/>
<%
Vector homeDiaryReviewList=(Vector)request.getAttribute("homeDiaryReviewList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
HomeDiaryReviewBean homeDiaryReview=null;
//liuyi 2006-12-27 start
int index = 0;
for(int i=0;i<homeDiaryReviewList.size();i++){
homeDiaryReview=(HomeDiaryReviewBean)homeDiaryReviewList.get(i);
if(action.getUser(homeDiaryReview.getReviewUserId())==null)continue;
index++;
%>
<%=index%>.
<%
if(homeDiaryReview.getReviewUserId()==loginUser.getId()){%>你<%}else{
%>
<a href="/user/ViewUserInfo.do?userId=<%=homeDiaryReview.getReviewUserId()%>"><%=StringUtil.toWml(action.getUser(homeDiaryReview.getReviewUserId()).getNickName())%></a>
<%}%>
在<%=homeDiaryReview.getCreateDatetime()%>说:<%=StringUtil.toWml(homeDiaryReview.getReview())%>
<%
//liuyi 2006-12-27 end
int diaryId=homeDiaryReview.getDiaryId();
HomeDiaryBean homediary=(HomeDiaryBean)action.getHomeService().getHomeDiary("id="+diaryId);
int diaryuserId=homediary.getUserId();
if(loginUser.getId()==diaryuserId){
%>
<a href="homeDiaryReview.jsp?delete=1&amp;diaryReviewId=<%=homeDiaryReview.getId()%>&amp;diaryId=<%=diaryId%>&amp;userId=<%=userId%>" >删除</a>
<%}%>
<br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
 }
%>
<br/>
<!--wucx4_判断是否是黑名单不能到该用户的家园留   日期:2006-11－24-->
<%
IUserService service = ServiceFactory.createUserService();
boolean isABadGuys=service.isUserBadGuy(StringUtil.toInt(userId),loginUser.getId());
if(!isABadGuys){%>
内容:<input name="review"  maxlength="100" value=""/><br/>
 <anchor title="确定">发表评论
    <go href="homeDiaryReview.jsp" method="post">
    <postfield name="review" value="$review"/>
    <postfield name="userId" value="<%=userId%>"/>
    <postfield name="diaryId" value="<%=homeDiary.getId()%>"/>
    <postfield name="reviewUserId" value="<%=loginUser.getId()%>"/>
    </go>
    </anchor><br/>
   <%}%>
<!-- wucx_4_判断是否是黑名单不能到该用户的家园留   日期:2006-11－24-->
<a href="homeDiary.jsp?diaryId=<%=homeDiary.getId()%>&amp;userId=<%=userId%>">返回日记</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<%
UserBean user = UserInfoUtil.getUser(StringUtil.toInt(userId));
%>
<a href="home.jsp?userId=<%=userId%>" >返回<%= StringUtil.toWml(user.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
查询的日记评论不存在.<br/>
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
}%>
</wml>