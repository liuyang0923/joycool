<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
String orderBy = request.getParameter("orderBy");


if (orderBy == null) {
	orderBy = "honor";
}
List tongList;
if (orderBy.equals("userCount")) {
	tongList = SqlUtil.getIntListCache("select id from jc_tong order by mark asc,user_count desc limit 50",30);
} else if (orderBy.equals("id")) {
	tongList = SqlUtil.getIntListCache("select id from jc_tong order by mark asc,id desc limit 50",30);
}else if (orderBy.equals("honor")) {
	tongList = SqlUtil.getIntListCache("select a.id from jc_tong a join jc_tong_hockshop b on a.id=b.tong_id order by a.mark asc,a.honor desc,b.develop desc limit 50",30);
}else if (orderBy.equals("userId")) {
	tongList = SqlUtil.getIntListCache("select a.user_id from jc_tong a join jc_tong_hockshop b on a.id=b.tong_id  where a.user_id>0 order by  b.develop desc limit 10",30);
}else if (orderBy.equals("depot")) {
	tongList = SqlUtil.getIntListCache("select id from jc_tong where mark=0 order by depot_week desc,id asc limit 50",30);
}else if (orderBy.equals("depot2")) {
	tongList = SqlUtil.getIntListCache("select id from jc_tong where mark=0 order by depot_last desc,id asc limit 50",30);
} else {
	tongList = SqlUtil.getIntListCache("select a.id from jc_tong a join jc_tong_hockshop b on a.id=b.tong_id order by a.mark asc, b.develop desc limit 50",30);
}
PagingBean paging = new PagingBean(action,tongList.size(),10,"p");
tongList = tongList.subList(paging.getStartIndex(), paging.getEndIndex());

List newTongList = TongCacheUtil.getTongListById("id");
PagingBean paging2 = new PagingBean(action,newTongList.size(),5,"p2");
newTongList = newTongList.subList(paging2.getStartIndex(), paging2.getEndIndex());


UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = null;
if (loginUser != null && loginUser.getId() > 0){
	us = UserInfoUtil.getUserStatus(loginUser.getId());	
} else {
	us = new UserStatusBean();
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮会排名">
<p align="left">
<img src="../img/tong/tongTop.gif" alt="帮会"/><br/>
<%=BaseAction.getTop(request, response)%>
瞧一瞧，看一看，谁才是乐酷的第一大帮会？<br/>
==江湖排行==<br/>
<%
if(orderBy.equals("develop")){
if(us.getTong()>0&&us.getTong()<20000){
int count = TongCacheUtil.getTongOrderById(us.getTong(),orderBy);
%>
你的帮会目前开发度排在第<%=count+1%>位。<br/>
<%}%>
<a href="/tong/tongList.jsp?orderBy=honor">综合</a>|开发度|<a href="/tong/tongList.jsp?orderBy=depot">仓库</a><br/>
<%}else if(orderBy.equals("depot")){%>
<a href="/tong/tongList.jsp?orderBy=honor">综合</a>|<a href="/tong/tongList.jsp?orderBy=develop">开发度</a>|仓库/<a href="/tong/tongList.jsp?orderBy=depot2">上周</a><br/>
<%}else if(orderBy.equals("depot2")){%>
<a href="/tong/tongList.jsp?orderBy=honor">综合</a>|<a href="/tong/tongList.jsp?orderBy=develop">开发度</a>|<a href="/tong/tongList.jsp?orderBy=depot">仓库</a>/上周<br/>
<%}else{
if(us.getTong()>0&&us.getTong()<20000){
int count = TongCacheUtil.getTongOrderById(us.getTong(),orderBy);%>
你的帮会综合排名为第<%=count+1%>位。<br/>
<%}%>
综合|<a href="/tong/tongList.jsp?orderBy=develop">开发度</a>|<a href="/tong/tongList.jsp?orderBy=depot">仓库</a><br/>
<%}
if(tongList.size()>0){
Integer tongId=null;
TongBean tong=null;
for(int i=0;i<tongList.size();i++){
tongId=(Integer)tongList.get(i);
tong=action.getTong(tongId.intValue());
if(tong==null)continue;
%>
<%=i+1 %>.<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle())%></a>(<%=action.getOnlineTongUserIdList(tong.getId()).size()%>/<%=tong.getUserCount() %>)<br/>
<%}%>
<%=paging.shuzifenye("tongList.jsp?orderBy=" + orderBy,true,"|",response)%>
<%}else{%>没有查询到结果记录<br/><%}
if(orderBy.equals("honor")){
%>
==最近成立==<br/>
<%
if(newTongList.size()>0){
Integer tongId=null;
TongBean tong=null;
for(int i=0;i<newTongList.size();i++){
tongId=(Integer)newTongList.get(i);
tong=action.getTong(tongId.intValue());
if(tong==null)continue;
%>
<%=i+1 %>.<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle())%></a>(<%=action.getOnlineTongUserIdList(tong.getId()).size()%>/<%=tong.getUserCount() %>)<br/>
<%}%>
<%=paging2.shuzifenye("tongList.jsp",false,"|",response)%>
<%}else{%>没有查询到结果记录<br/><%}}%>
搜索帮会:<br/>
帮会名称:
<input name="tongTitle"  maxlength="10" value=""/>
<anchor title="确定">GO
  <go href="tongSearch.jsp" method="post">
    <postfield name="tongTitle" value="$tongTitle"/>
  </go>
</anchor><br/>
<%
if(us.getTong()>0&&us.getTong()<20000){%>
<a href="/tong/tong.jsp?tongId=<%=us.getTong()%>">我的帮会</a><br/>
<%}%>
<a href="/tong/tongErect.jsp">申请建立帮会</a><br/>
<a href="/tong/tongCenter.jsp">所有帮会城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>