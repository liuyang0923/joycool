<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.tongTransfer(request);
TongBean tong=(TongBean)request.getAttribute("tong");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮主转让">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
请问您要将帮主转让给：<br/>
<a href="/tong/tongUsers.jsp?tongId=<%=tong.getId()%>">本帮兄弟 </a><br/>
<a href="/tong/tongHosts.jsp?tongId=<%=tong.getId()%>">其他帮帮主 </a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%}else{%>
您无权转让！ <br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>