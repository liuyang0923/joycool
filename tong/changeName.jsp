<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.changeName(request);
TongBean tong=(TongBean)request.getAttribute("tong");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮会易帜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
要改变"<%=StringUtil.toWml(tong.getTitle())%>"的名字？<br/>
请输入帮会新名称（不超过6个字）<br/>
需要花费乐币10亿<br/>
<input name="tongName"  maxlength="6" value="" title="名称"/><br/>
<anchor title="确定">确定
    <go href="/tong/changeNameResult.jsp" method="post">
    <postfield name="tongName" value="$tongName"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
    </go>
</anchor> <a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回 </a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%}else{%>
您无权易帜！ <br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>