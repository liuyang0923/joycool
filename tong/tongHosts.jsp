<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.tongHosts(request);
TongBean tong=(TongBean)request.getAttribute("tong");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List tongList=(List)request.getAttribute("tongList");
UserBean loginUser= (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮主转让">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
将你的帮会并入其他帮：<br/>
<%if(tongList!=null){
for(int i=0;i<tongList.size();i++){
Integer tongId=(Integer)tongList.get(i);
TongBean tongBean=TongCacheUtil.getTong(tongId.intValue());
if(tongBean!=null){
if(tongBean.getUserId()==loginUser.getId())
continue;
UserBean user=(UserBean)UserInfoUtil.getUser(tongBean.getUserId());
if(user!=null){
%>
<a href="/tong/transferToNotice.jsp?tongId=<%=tong.getId()%>&amp;host=<%=tongBean.getUserId()%>"><%=StringUtil.toWml(user.getNickName())%> </a>
 <%=StringUtil.toWml(tongBean.getTitle())%> <%=tongBean.getUserCount()%>人<br/>
<%
  }
  }
 }
 String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<% } 
}else{%>
您无权转让！ <br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>