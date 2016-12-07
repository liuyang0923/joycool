<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.reNameManage(request);
TongBean tong=(TongBean)request.getAttribute("tong");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮会昵称">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
昵称最多两个汉字，显示在帮会成员昵称前。<%=tong.getTitle()%>
<%if(!tong.getShortTitle().equals("")){ %>目前昵称:<br/>
<%=StringUtil.toWml(tong.getShortTitle())%><%}%><br/>
改变为<br/>
<input name="name"  maxlength="2" value="" title="名称"/><br/>
<anchor title="确定">确定
    <go href="/tong/renameResult.jsp" method="post">
    <postfield name="name" value="$name"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
    </go>
</anchor><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a> <a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<%}else{%>
您无权管理该帮会通知！<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>