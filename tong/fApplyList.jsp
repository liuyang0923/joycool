<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.bean.tong.TongFriendBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.fApplyList(request);
String result =(String)request.getAttribute("result");
TongFriendBean tongAlly =(TongFriendBean)request.getAttribute("tongAlly");
//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/tong/tongList.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="帮会同盟" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/tongList.jsp">帮会列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong =(TongBean)request.getAttribute("tong");
//int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
//int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
//String prefixUrl = (String) request.getAttribute("prefixUrl");
//List fApplyList=(List)request.getAttribute("fApplyList");
%>
<card title="结盟申请管理">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//if(fApplyList.size()>0){
if(tongAlly!=null){
//Integer tongId=null;
TongBean fTong=null;
//for(int i=0;i<fApplyList.size();i++){
//tongId=(Integer)fApplyList.get(i);
fTong=action.getTong(tongAlly.getTongId());
if(tong!=null){
%>
<a href="/tong/tong.jsp?tongId=<%=fTong.getId()%>"><%=StringUtil.toWml(fTong.getTitle())%></a>
<anchor title="确定">同意
  <go href="fApplyResult.jsp" method="post">
    <postfield name="tongId" value="<%=tong.getId()%>"/>
    <postfield name="fTongId" value="<%=fTong.getId()%>"/>
    <postfield name="flag" value="1"/>
  </go>
</anchor>|
<anchor title="确定">拒绝
  <go href="fApplyResult.jsp" method="post">
    <postfield name="tongId" value="<%=tong.getId()%>"/>
    <postfield name="fTongId" value="<%=fTong.getId()%>"/>
    <postfield name="flag" value="2"/>
  </go>
</anchor><br/>
<%}%>
<%}%>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle()) %></a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>