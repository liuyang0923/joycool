<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="修改个人照片">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/friend/editPhoto.jsp">修改个人照片</a>(支持WAP2.0的手机才可以上传图片。)<br/>
<a href="/friend/cartoon.jsp">选择一个卡通头像代替照片</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>