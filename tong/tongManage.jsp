<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");%>
<%@include file="../bank/checkpw.jsp"%><%
TongAction action=new TongAction(request);
action.tongManage(request);
TongBean tong=(TongBean)request.getAttribute("tong");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="管理帮会">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
<%=StringUtil.toWml(tong.getTitle())%>管理中心<br/>
帮主，祝你能千秋万代、一统江湖啊：<br/>
<a href="memberManage.jsp?tongId=<%=tong.getId()%>">会员管理</a>|
<%-- liuyi 2007-01-16 帮会修改 start --%>
<a href="descManage.jsp?tongId=<%=tong.getId()%>">帮会公告</a><br/>
<%-- liuyi 2007-01-16 帮会修改 start --%>
<a href="fundManage.jsp?tongId=<%=tong.getId()%>">帮会基金</a>|
<a href="tongrename.jsp?tongId=<%=tong.getId()%>">帮会昵称</a><br/>
<a href="revenueManage.jsp?tongId=<%=tong.getId()%>">税收管理</a>|
<a href="nominateAssistant.jsp?tongId=<%=tong.getId()%>">任命助手</a><br/>
<a href="changeName.jsp?tongId=<%=tong.getId()%>">帮会易帜</a>|
<a href="tongTransfer.jsp?tongId=<%=tong.getId()%>">帮主转让</a><br/>
<a href="tongDissolve.jsp?tongId=<%=tong.getId()%>">解散帮会</a>|
<a href="noticeManage.jsp?tongId=<%=tong.getId()%>">帮会通知</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回城市</a><br/>
<%}else if(request.getAttribute("tongfu")!=null){
 tong=(TongBean)request.getAttribute("tongfu");%>
 <%=StringUtil.toWml(tong.getTitle())%>管理中心<br/>
帮主，祝你能千秋万代、一统江湖啊：<br/>
<a href="memberManage.jsp?tongId=<%=tong.getId()%>">会员管理</a>|
<a href="fundManage.jsp?tongId=<%=tong.getId()%>">帮会基金</a><br/>
<a href="noticeManage.jsp?tongId=<%=tong.getId()%>">帮会通知</a>|
<a href="revenueManage.jsp?tongId=<%=tong.getId()%>">税收管理</a><br/><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%}else{%>
您无权管理该帮会！<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>