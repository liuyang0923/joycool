<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.ForbidUtil"%><%@ page import="java.util.List"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.tong.TongCityRecordBean"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.util.PageUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongCityHockShopRecord(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
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
String orderBy = (String) request.getAttribute("orderBy");
TongBean tong = (TongBean) request.getAttribute("tong");
if(orderBy==null){
orderBy="count";
}
if(orderBy.equals("tongCityTongId")){
List tongCityHockShopByIdList=(List)request.getAttribute("tongCityHockShopByIdList");%>
<card title="帮会当铺开发记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/tong/tongCityHockShopRecord.jsp?tongId=<%=tong.getId()%>&amp;orderBy=count">按次数</a>/<a href="/tong/tongCityHockShopRecord.jsp?tongId=<%=tong.getId()%>&amp;orderBy=create_datetime">按时间</a>/按帮会<br/>
<%
TongCityRecordBean tongCiytRecord = null;
TongBean tong1=null;
for(int i=0;i<tongCityHockShopByIdList.size();i++){
    tongCiytRecord=(TongCityRecordBean)tongCityHockShopByIdList.get(i);
   	tong1=TongCacheUtil.getTong(tongCiytRecord.getTongId());
   	if(tong1==null){continue;}
   	%>
<a href="/tong/tong.jsp?tongId=<%=tong1.getId()%>"><%= StringUtil.toWml(tong1.getTitle())%></a>
<%=tongCiytRecord.getCount()%>次<br/>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><%}%><br/>
<a href="/tong/hockshop.jsp?tongId=<%=tong.getId()%>">返回当铺</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
Vector tongCityHockShopList=(Vector)request.getAttribute("tongCityHockShopList");
%>
<card title="帮会当铺开发记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(orderBy.equals("create_datetime")){%>
<a href="/tong/tongCityHockShopRecord.jsp?tongId=<%=tong.getId()%>&amp;orderBy=count">按次数</a>/按时间<%--/<a href="/tong/tongCityRecord.jsp?tongId=<%=tong.getId()%>&amp;orderBy=tongCityTongId">按帮会</a>--%><br/>
<%}else{%>
按次数/<a href="/tong/tongCityHockShopRecord.jsp?tongId=<%=tong.getId()%>&amp;orderBy=create_datetime">按时间</a><%--/<a href="/tong/tongCityRecord.jsp?tongId=<%=tong.getId()%>&amp;orderBy=tongCityTongId">按帮会</a>--%><br/>
<%}
TongCityRecordBean tongCiytRecord = null;
UserBean user=null;
for(int i=0;i<tongCityHockShopList.size();i++){
    tongCiytRecord=(TongCityRecordBean)tongCityHockShopList.get(i);
   	user=UserInfoUtil.getUser(tongCiytRecord.getUserId());
   	if(user==null){continue;}
   	%>
<%if(ForbidUtil.isForbid("game",user.getId())){%>无名氏<%}else{%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName()) %></a><%=tongCiytRecord.getCount()%>次<%}%><br/>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><%}%><br/>
<a href="/tong/hockshop.jsp?tongId=<%=tong.getId()%>">返回当铺</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}}%>
</wml>