<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
//当前用户信息
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
//dao.action
IHomeService homeService =ServiceFactory.createHomeService();
HomeAction home=new HomeAction(request);      
home.viewHomeDiary(request);
//人气排行
int perPage=StringUtil.toInt((String)request.getAttribute("NUM_PER_PAGE"));
int totalCount=StringUtil.toInt((String)request.getAttribute("totalCount"));
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector hitsDiary=(Vector)request.getAttribute("hitsDiary");
//新日记排行
String totalCount1=(String)request.getAttribute("totalCount1");
int totalPage1=StringUtil.toInt((String)request.getAttribute("totalPage1"));
int pageIndex1=StringUtil.toInt((String)request.getAttribute("pageIndex1"));
Vector newDiary=(Vector)request.getAttribute("newDiary");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐客日记">
<p align="left"><%=BaseAction.getTop(request, response)%>
乐客日记<br/>
---------<br/>
==人气排行==<br/>
<%
int count = hitsDiary.size();
for(int i = 0; i < count; i ++){
	HomeDiaryBean homeDiary=(HomeDiaryBean)hitsDiary.get(i);
	int reviewCount=homeService.getHomeDiaryReviewCount("diary_id="+homeDiary.getId());
%>
<%=perPage*pageIndex + i+1%>.
<a href="homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>  
(点击<%=homeDiary.getHits()%> | 评论<%=reviewCount%>)<br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "diary.jsp", false, " ", response)%><%if(totalPage>1){%><br/><%}%>
==新增日记==<br/>
<%
count = newDiary.size();
for(int i = 0; i < count; i ++){
	HomeDiaryBean homeDiary=(HomeDiaryBean)newDiary.get(i);
	int reviewCount=homeService.getHomeDiaryReviewCount("diary_id="+homeDiary.getId());
%>
<%=perPage*pageIndex1 + i+1%>.
<a href="homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>  
(点击<%=homeDiary.getHits()%> | 评论<%=reviewCount%>)<br/>
<%
}
%>
<%=PageUtil.shuzifenye1(totalPage1, pageIndex1, "diary.jsp", false, " ", response)%><%if(totalPage1>1){%><br/><%}%>
搜索家园<br/>
用户ID：<input name="userId" format="*N"  maxlength="10" value=""/> <anchor title ="search">Go
         <go href="searchHome.jsp" method="post">
             <postfield name="userId" value="$userId"/>
         </go>
         </anchor><br/>
<a href="photo.jsp">乐客相册</a><br/>
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