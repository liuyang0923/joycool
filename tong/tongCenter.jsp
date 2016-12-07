<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.tong.TongCityRecordBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongCenter(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = null;
if(loginUser!=null)
	us=UserInfoUtil.getUserStatus(loginUser.getId());
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List tongList=(List)request.getAttribute("tongList");
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
List userList=(List)request.getAttribute("userList");
String orderBy = (String)request.getAttribute("orderBy");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮会列表">
<p align="left">
<img src="../img/tong/tong.gif" alt="帮会"/><br/>
<%=BaseAction.getTop(request, response)%>
人在江湖漂，哪能不挨刀？还是多找些兄弟互相照应吧！<br/>
<%
if(us!=null&&us.getTong()>0&&us.getTong()<20000){%>
	<a href="/tong/tong.jsp?tongId=<%=us.getTong()%>">我的帮会</a><br/>
<%}else{%>
	<a href="/tong/tongErect.jsp">申请建立帮会</a><br/>
<%}%>
<a href="/tong/tongList.jsp">帮会江湖排行</a><br/>
按名称搜索帮会:
<input name="tongTitle"  maxlength="100" value="200"/>
<anchor title="确定">GO
  <go href="tongSearch.jsp" method="post">
    <postfield name="tongTitle" value="$tongTitle"/>
  </go>
</anchor><br/>
=乐酷江湖•所有帮会=<br/>
<%--if(orderBy.equals("develop")){
if(us.getTong()>0){
int count = TongCacheUtil.getTongOrderById(us.getTong(),orderBy);
%>
你的帮会目前开发度排在第<%=count+1%>位置。<br/>
<%}%>
<a href="/tong/tongCenter.jsp?orderBy=userCount">人数</a>|开发度<br/>
<%}else{
if(us.getTong()>0){
int count = TongCacheUtil.getTongOrderById(us.getTong(),orderBy);%>
你的帮会目前人数排在第<%=count+1%>位置。<br/>
<%}%>
人数|<a href="/tong/tongCenter.jsp?orderBy=develop">开发度</a><br/>
<%}--%>
<%if(tongList.size()>0){
Integer tongId=null;
TongBean tong=null;
for(int i=0;i<tongList.size();i++){
tongId=(Integer)tongList.get(i);
tong=action.getTong(tongId.intValue());
if(tong==null)continue;
%>
<%=i+1 %>.<%if(tong.getStockId()>0){%><a href="/stock2/stockInfo.jsp?stockId=<%=tong.getStockId()%>"><img src="../img/tong/tongStock.gif" alt=""/></a><%}%><a href="/tong/tong.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle())%></a>(<%=action.getOnlineTongUserIdList(tong.getId()).size()%>/<%=tong.getUserCount() %>)<br/>
<%}String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}}else{%>没有查询到结果记录<br/><%}%>
=江湖名人•十大帮主=<br/>
<%
if(userList.size()>0){
Integer userId=null;
UserBean tUser=null;
for(int i=0;i<userList.size();i++){
userId=(Integer)userList.get(i);
tUser=UserInfoUtil.getUser(userId.intValue());
if(tUser==null)continue;
%>
<%=i+1 %>.<a href="/user/ViewUserInfo.do?userId=<%=tUser.getId()%>"><%=StringUtil.toWml(tUser.getNickName())%></a><br/>
<%}String fenye = action.shuzifenye(totalPageCount1, pageIndex1, prefixUrl1, false, "|", "pageIndex1" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}}else{%>没有查询到结果记录<br/><%}%>
=江湖传闻•风云四起=<br/>
<%
Vector cityRecordList = action.getTongCiytRecordListNew();
for(int i=0;i<cityRecordList.size();i++){
	TongCityRecordBean cityRecord=(TongCityRecordBean)cityRecordList.get(i);
	TongBean tong=action.getTong(cityRecord.getTongId());
	if(tong==null)continue;
	UserBean tUser=UserInfoUtil.getUser(cityRecord.getUserId());
	if(tUser==null)continue;
%>
<%=i+1 %>.<a href="/user/ViewUserInfo.do?userId=<%=tUser.getId()%>"><%=StringUtil.toWml(tUser.getNickName())%></a>攻打了<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle())%></a><br/>
<%}%>
<%--6、可手动插入一条文本或带链接的消息<br/>--%>
=江湖谈论•笑说恩怨=  <br/>
<%
Vector tongForumList=action.getForumListNew();
for(int i=0;i<tongForumList.size();i++){
	ForumContentBean forumContent=(ForumContentBean)tongForumList.get(i);
	TongBean tong=action.getTong(forumContent.getTongId());
	if(tong==null)continue;
%>
<%=i+1 %>.<a href="/jcforum/viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;forumId=<%=forumContent.getForumId()%>"><%=StringUtil.toWml(forumContent.getTitle())%></a>---<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle())%></a><br/>
<%}%>
<a href="/jcforum/index.jsp?city=1">更多帮会信息</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>