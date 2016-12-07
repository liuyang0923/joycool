<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil,net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%TongAction action=new TongAction(request);
int tongId = StringUtil.toInt(request.getParameter("tongId"));
TongBean tong = action.getTongTwo(tongId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="公告管理">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){
session.setAttribute("desc", "");
%>
<%=StringUtil.toWml(tong.getTitle())%>帮会公告<br/>
<%=StringUtil.toWml(tong.getDescription())%><br/>
改变为(最多200字)<br/>
<input name="desc" value="" maxlength="200"/><br/>
<anchor title="确定">提交
    <go href="/tong/descResult.jsp?tongId=<%=tong.getId()%>" method="post">
    <postfield name="desc" value="$desc"/>
    </go>
</anchor>  <br/>
<%}else{%>
您无权管理该帮会的公告！<br/>
<%}%>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会城市</a><br/>
 <a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>