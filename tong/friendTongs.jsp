<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.DateUtil,net.joycool.wap.cache.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.friendTongs(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
TongBean tong = action.getTong(StringUtil.toInt(request.getParameter("tongId")));
if(tong==null){
	BaseAction.sendRedirect("/tong/tongList.jsp", response);
	return;
}
boolean isTongAdmin = (tong.getUserId() == loginUser.getId()|| tong.getUserIdA() == loginUser.getId() || tong.getUserIdB() == loginUser.getId());
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List tongIdList=(List)request.getAttribute("tongIdList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮会盟友">
<p align="left">
<img src="../img/tong/tong.gif" alt="帮会"/><br/>
<%=BaseAction.getTop(request, response)%>
<%
if(tongIdList==null || tongIdList.size()==0){ %>
<%= StringUtil.toWml(tong.getTitle()) %>没有盟友.<br/>
<%
}
else{
%><%= StringUtil.toWml(tong.getTitle()) %>盟友<br/><%	
Integer id=null;
TongBean bean=null;
for(int i=0;i<tongIdList.size();i++){
    id=(Integer)tongIdList.get(i);
    bean=TongCacheUtil.getTong(id.intValue());
    if(bean==null)continue;
%>
<%=i+1 %>.<a href="/tong/tong.jsp?tongId=<%=bean.getId()%>"><%=StringUtil.toWml(bean.getTitle())%></a>
<%if(isTongAdmin){%>
<a href="/tong/tongDisband.jsp?tongId=<%=tong.getId()%>&amp;fTongId=<%=bean.getId()%>">解除盟约</a>
<%}%>
<br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<%}%>
<%
UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
if(status.getTong()!=tong.getId()){%>
<a href="/tong/friendTongApply.jsp?tongId=<%=tong.getId()%>">向<%= StringUtil.toWml(tong.getTitle()) %>申请结盟</a><br/>
<%}%>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%= StringUtil.toWml(tong.getTitle()) %></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>