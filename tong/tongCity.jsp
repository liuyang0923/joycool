<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongCity(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
long now = System.currentTimeMillis();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("tongList.jsp");%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong = (TongBean)request.getAttribute("tong");
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>" >
<p align="left">
<%=loginUser.showImg("/img/tong/city/"+action.tongCityImage(tong)+".gif")%>
<%=BaseAction.getTop(request, response)%>
当前耐久度<%=tong.getNowEndure()%><br/>
耐久度上限<%=tong.getHighEndure()%><br/>
<a href="tongCity2.jsp?mark=r&amp;tongId=<%=tong.getId()%>">加固城墙</a>
<%if(tong.getTongRecoveryTime().getTime()+1000*60*30-now>=0){%>(士气上升)<%}%><br/>
<%if(us.getTong()!=tong.getId()){%>
<a href="tongCity2.jsp?mark=d&amp;tongId=<%=tong.getId()%>">破坏城墙</a>
<%if(tong.getTongAssaultTime().getTime()+1000*60*30-now>=0){%>(士气上升)<%}%><br/><%}%>
===攻城道具===<br/><%
int count=0;
if(us.getTong()!=tong.getId()){
count = UserBagCacheUtil.getUserBagItemCount(15,loginUser.getId());
if(count>0){%>
使用<a href="tongCityResult.jsp?mark=d&amp;productId=15&amp;tongId=<%=tong.getId()%>">轰天炮(<%=count%>)</a><br/><%}
count = UserBagCacheUtil.getUserBagItemCount(25,loginUser.getId());
if(count>0){%>
使用<a href="tongCityResult.jsp?mark=d&amp;productIdA=25&amp;tongId=<%=tong.getId()%>">攻城战鼓(<%=count%>)</a><br/><%}}
count = UserBagCacheUtil.getUserBagItemCount(16,loginUser.getId());
if(count>0){%>
使用<a href="tongCityResult.jsp?mark=r&amp;productId=16&amp;tongId=<%=tong.getId()%>">防护膜(<%=count%>)</a><br/><%}
count = UserBagCacheUtil.getUserBagItemCount(26,loginUser.getId());
if(count>0){%>
使用<a href="tongCityResult.jsp?mark=r&amp;productIdA=26&amp;tongId=<%=tong.getId()%>">守城战鼓(<%=count%>)</a><br/><%}
if(us.getTong()!=tong.getId()){
count = UserBagCacheUtil.getUserBagItemCount(76,loginUser.getId());
if(count>0){%>
使用<a href="tongCityResult.jsp?mark=d&amp;productId=76&amp;tongId=<%=tong.getId()%>" method="post">大石头(<%=count%>)</a><br/><%}}%>
==========<br/>
<a href="tongCityRecoveryRecord.jsp?tongId=<%=tong.getId()%>">城墙加固记录</a><br/>
<a href="tongCityRecord.jsp?tongId=<%=tong.getId()%>">城墙破坏记录</a><br/>
<a href="tongDestroyHistory.jsp?tongId=<%=tong.getId()%>">城池沦陷记录</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
</p>
</card>
<%}%></wml>