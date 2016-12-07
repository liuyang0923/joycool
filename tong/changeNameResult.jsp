<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.changeNameResult(request);
String result=(String)request.getAttribute("result");
String tongId=(String)request.getAttribute("tongId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮会易帜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(result!=null&&result.equals("success")){%>
<%=StringUtil.toWml((String)request.getAttribute("tip"))%><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tongId%>">管理帮会 </a><br/><br/>
<a href="/tong/tong.jsp?tongId=<%=tongId%>">返回城市 </a><br/><br/>
<%}else if(result!=null&&result.equals("userError")){%>
<%=request.getAttribute("tip")%><br/>
<a href="/tong/changeName.jsp?tongId=<%=tongId%>">返回上一级</a><br/><br/>
<%}else if(result!=null&&result.equals("nameError")){%>
<%=request.getAttribute("tip")%><br/>
<a href="/tong/changeName.jsp?tongId=<%=tongId%>">返回上一级</a><br/><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>