<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.fApply(request);
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
<%}else{
TongBean tong =(TongBean)request.getAttribute("tong");
String fApplyCount=(String)request.getAttribute("fApplyCount");
%>
<card title="结盟申请管理">
<p align="left">
<%=BaseAction.getTop(request, response)%>
维护和平，共谋发展<br/>
<a href="/tong/fApplyList.jsp?tongId=<%=tong.getId()%>">结盟申请<%=fApplyCount %>个未处理</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle()) %></a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>