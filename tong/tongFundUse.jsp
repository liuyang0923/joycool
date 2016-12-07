<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.tong.TongFundBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.PageUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongFundUse(request);
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
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List tongFundUseList=(List)request.getAttribute("tongFundUseList");
TongBean tong =(TongBean)request.getAttribute("tong");
%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(tongFundUseList.size()>0){
Integer tongFundId=null;
TongFundBean tongFund=null;
UserBean user=null;
for(int i=0;i<tongFundUseList.size();i++){
tongFundId=(Integer)tongFundUseList.get(i);
tongFund=action.getTongFund(tongFundId.intValue());
if(tongFund==null){continue;}
if(tongFund.getUserId()==0){
%>
<%=action.transitionDatetime(tongFund.getCreateDatetime())%>
系统管理员
取走<%=tongFund.getMoney()%>乐币<br/>
<%
}else{
user=UserInfoUtil.getUser(tongFund.getUserId());
if(user==null){continue;}%>
<%=action.transitionDatetime(tongFund.getCreateDatetime())%>
<%if(user.getId()!=loginUser.getId()){
%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName()) %></a>
<%}else{%>您自己<%}%>
取走<%=tongFund.getMoney()%>乐币<br/><%}}}else{%><%=StringUtil.toWml(tong.getTitle())%>帮基金没有使用过!<br/><%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/tong/tongFund.jsp?tongId=<%=tong.getId()%>">返回基金</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>