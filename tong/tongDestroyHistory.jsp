<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.DateUtil,net.joycool.wap.cache.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongDestoryHistory(request);
TongBean tong = action.getTong(StringUtil.toInt(request.getParameter("tongId")));
if(tong==null){
	//response.sendRedirect("/tong/tongList.jsp");
	BaseAction.sendRedirect("/tong/tongList.jsp", response);
	return;
}
int totalCount = ((Integer) request.getAttribute("totalCount")).intValue();
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List tongList=(List)request.getAttribute("tongDestroyList");
String createTime = tong.getCreateDatetime();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="城池沦陷记录">
<p align="left">
<img src="../img/tong/tong.gif" alt="帮会"/><br/>
<%=BaseAction.getTop(request, response)%>
<%
if(tongList==null || tongList.size()==0){ %>
本城自<%= DateUtil.getDateString(createTime) %>建成以来，众志成城、固若金汤，从未沦陷，号称不落之都!<br/>
<%
}
else{
%>
本城自<%= DateUtil.getDateString(createTime) %>建成以来，共沦陷<%= totalCount %>次，其时流血漂橹、生灵涂炭，帮中男女老幼俱遭凌辱：<br/>
<%	
Integer id=null;
TongDestroyHistoryBean bean=null;
for(int i=0;i<tongList.size();i++){
id=(Integer)tongList.get(i);
bean=TongCacheUtil.getTongDestroyHistory(id.intValue());
if(bean==null)continue;

String time = bean.getTime();
%>
<%= DateUtil.getDateString(time) %>,城破<br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<%}%>
<a href="/tong/tongCity.jsp?tongId=<%=tong.getId()%>">返回城墙</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>