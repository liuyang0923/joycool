<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.friendlink.RandomLinkBean"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="电子书搜索">
<p align="left">
请输入电子书名称：<br/>
    <input name="ebookName"  maxlength="20" value="v"/>
    <br/>
         <anchor title ="search EBook">查询
         <go href="SearchEBook.do" method="post">
             <postfield name="ebookName" value="$(ebookName)"/>
         </go>
         </anchor>
<br/><%
//<!--add friend link start-->
RandomLinkBean randomLinkBean = new RandomLinkBean();%>
<img src="<%=( randomLinkBean.getRandomLink(request) ) %>" alt="loading..." />
//<!--add friend link end-->
%>
</p>
</card>
</wml>