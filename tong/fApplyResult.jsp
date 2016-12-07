<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.fApplyResult(request);
String result =(String)request.getAttribute("result");
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
<%}else if(result.equals("error")){
TongBean tong =(TongBean)request.getAttribute("tong");
url=("/tong/tong.jsp?tongId="+tong.getId());
%>
<card title="帮会同盟" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml((String)request.getAttribute("tip")) %><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getDescription()) %></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
TongBean tong =(TongBean)request.getAttribute("tong");
BaseAction.sendRedirect("/tong/tong.jsp?tongId="+tong.getId(),response);
return;
}else{
TongBean tong =(TongBean)request.getAttribute("tong");
%>
<card title="帮会同盟">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml((String)request.getAttribute("tip")) %><br/>
<a href="/tong/fApplyList.jsp?tongId=<%=tong.getId()%>">返回申请列表</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle()) %></a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>