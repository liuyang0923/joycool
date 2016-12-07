<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.List"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.tong.TongCityRecordBean"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.tong.TongHonorBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongHonorList(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="荣誉度" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/tongList.jsp">帮会列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
TongBean tong = (TongBean) request.getAttribute("tong");
List honorList=(List)request.getAttribute("honorList");
int orderBy =StringUtil.toInt((String)request.getAttribute("orderBy"));
%>
<card title="荣誉度">
<p align="left">
<%=BaseAction.getTop(request, response)%>
帮会当前旗帜：<%=tong.getHonor()%><br/>
<a href="/tong/presentHonor.jsp?tongId=<%=tong.getId()%>">我要赠送旗帜</a><br/>
<%
if(orderBy==0){%>
赠旗记录|<a href="/tong/tongHonorList.jsp?tongId=<%=tong.getId()%>&amp;orderBy=1">抢旗记录</a><br/>
<%}else{%>
<a href="/tong/tongHonorList.jsp?tongId=<%=tong.getId()%>&amp;orderBy=0">赠旗记录</a>|抢旗记录<br/>
注:只有当帮会城破时,所有旗帜才会掉落,掉落后任何人都能抢夺！<br/>
<%}
if(honorList.size()>0){
TongHonorBean tongHonor = null;
for(int i=0;i<honorList.size();i++){
    tongHonor=(TongHonorBean)honorList.get(i);
   	UserBean user=UserInfoUtil.getUser(tongHonor.getUserId());
   	if(user==null){continue;}%>
<%=i+1%>.
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName()) %></a>
<%=DateUtil.formatDate1(tongHonor.getCreateDatetime())%>
<%if(tongHonor.getFlag()==0){%>赠旗<%}else{%>抢旗<%}%><br/>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<%}else{%>没有查询到记录！<br/><%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>