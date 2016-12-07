<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.friendlink.LinkRecordBean"%><%@ page import="net.joycool.wap.bean.friendlink.LinkTypeBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
LinkRecordBean linkrecord = (LinkRecordBean)request.getAttribute("linkrecord");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="申请友情连接">
<p align="left"> 
<%--马长青_2006-6-22_增加顶部信息_start--%>
<%=BaseAction.getTop(request, response)%>
<%--马长青_2006-6-22_增加顶部信息_end--%>
申请友情连接<br/>
------------<br/>
申请成功！<br/>
请把我们网站加到贵站的友链中！<br/>
标题是：乐酷游戏社区<br/>
地址是：http://wap.joycool.net/entry/entry.jsp?id=<%=linkrecord.getLinkId()%><br/>
我们也将在24小时内处理您的请求，将您的网站加到我们的友情链接中！<br/>
如有问题，请联系我们的负责人员，请记下他的联系方式：QQ48279634。<br/>
<br/>
<a href="http://wap.joycool.net" title="返回">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>