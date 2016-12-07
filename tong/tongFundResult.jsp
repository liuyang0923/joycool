<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongFundResult(request);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
//	liuyi 2007-01-23 页面跳转修改 start
UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/tong/tongFund.jsp?tongId="+userStatus.getTong());
//	liuyi 2007-01-23 页面跳转修改 end
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>（3秒钟跳转到帮会基金首页）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("countError")){
TongBean tong = (TongBean)request.getAttribute("tong");
String url=("/tong/tongFund.jsp?tongId="+tong.getId());
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>（3秒钟跳转到帮会基金首页）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
TongBean tong = (TongBean)request.getAttribute("tong");
out.clearBuffer();
response.sendRedirect(("tong.jsp?tongId="+tong.getId()));
return;
}else{
TongBean tong = (TongBean)request.getAttribute("tong");
String url=("/tong/tongFund.jsp?tongId="+tong.getId());
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>帮" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/tong/tongFundHelp.jsp?tongId=<%=tong.getId()%>">管理规则</a>
<a href="/tong/tongFundUse.jsp?tongId=<%=tong.getId()%>">使用明细</a><br/>
<a href="/tong/tongFundTop.jsp?tongId=<%=tong.getId()%>">帮会贡献度排行榜</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%>帮</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>