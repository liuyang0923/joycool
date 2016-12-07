<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IHomeService" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
IHomeService homeService =ServiceFactory.createHomeService();
HomeAction action = new HomeAction(request);
action.diaryTop(request);
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector diaryMark=(Vector)request.getAttribute("diaryMark");
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
Vector diaryTotalTop=(Vector)request.getAttribute("diaryTotalTop");
int totalPageCount2 = ((Integer) request.getAttribute("totalPageCount2")).intValue();
int pageIndex2 = ((Integer) request.getAttribute("pageIndex2")).intValue();
String prefixUrl2 = (String) request.getAttribute("prefixUrl2");
Vector diaryTop=(Vector)request.getAttribute("diaryTop");
int totalPageCount3 = ((Integer) request.getAttribute("totalPageCount3")).intValue();
int pageIndex3 = ((Integer) request.getAttribute("pageIndex3")).intValue();
String prefixUrl3 = (String) request.getAttribute("prefixUrl3");
Vector diaryNew=(Vector)request.getAttribute("diaryNew");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="推荐日记">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/diary.gif" alt="乐客日记"/><br/>
==推荐日记==<br/>
<%
HomeDiaryBean homeDiary=null;
for(int i=0;i<diaryMark.size();i++){
homeDiary=(HomeDiaryBean)diaryMark.get(i);
%>
<%=i+1%>.<a href="homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>(点击<%=homeDiary.getHits()%> | 评论<%=homeDiary.getReviewCount()%>)<br/>
<%}
String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
==总人气排行==<br/>
<%
for(int i=0;i<diaryTotalTop.size();i++){
homeDiary=(HomeDiaryBean)diaryTotalTop.get(i);
%>
<%=i+1%>.<a href="homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>(点击<%=homeDiary.getHits()%> | 评论<%=homeDiary.getReviewCount()%>)<br/>
<%}
String fenye1 = action.shuzifenye(totalPageCount1, pageIndex1, prefixUrl1, true, "|", "pageIndex1" ,  response);
if(!"".equals(fenye1)){%><%=fenye1%><br/><%}%>
==每日人气排行==<br/>
<%
for(int i=0;i<diaryTop.size();i++){
homeDiary=(HomeDiaryBean)diaryTop.get(i);
%>
<%=i+1%>.<a href="homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>(点击<%=homeDiary.getHits()%> | 评论<%=homeDiary.getReviewCount()%>)<br/>
<%}
String fenye2 = action.shuzifenye(totalPageCount2, pageIndex2, prefixUrl2, true, "|", "pageIndex2" ,  response);
if(!"".equals(fenye2)){%><%=fenye2%><br/><%}%>
==新增日记==<br/>
<%
for(int i=0;i<diaryNew.size();i++){
homeDiary=(HomeDiaryBean)diaryNew.get(i);
%>
<%=i+1%>.<a href="homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>(点击<%=homeDiary.getHits()%> | 评论<%=homeDiary.getReviewCount()%>)<br/>
<%}
String fenye3 = action.shuzifenye(totalPageCount3, pageIndex3, prefixUrl3, true, "|", "pageIndex3" , response);
if(!"".equals(fenye3)){%><%=fenye3%><br/><%}%>
搜索家园<br/>
用户ID：<input name="userId" format="*N"  maxlength="10" value=""/> <anchor title ="search Image">Go
         <go href="searchHome.jsp" method="post">
             <postfield name="userId" value="$userId"/>
         </go>
         </anchor><br/>
<a href="photoTop.jsp">乐客相册</a><br/>
<a href="viewAllHome.jsp">*家园之星*</a><br/>       
<%if(loginUser!=null && loginUser.getHome()==1){%>
<a href="home.jsp">返回我的家园</a><br/>
<%}else{%>
<a href="inputRegisterInfo.jsp">我也要建立家园</a><br/> 
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>