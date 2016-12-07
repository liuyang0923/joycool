<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
StakeAction action = new StakeAction(request);
action.stakeMatchAct();
String result = (String)request.getAttribute("result");
String url = (String)request.getAttribute("url");
if(url==null)
	url = "/pet/stakeindex.jsp";
String tip = (String)request.getAttribute("tip");
url = (url);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赌局">
<p align="left">
<%=BaseAction.getTop(request, response)%>



<%if("failure".equals(result)) {%>
<%=tip%><br/>
<a href="/pet/stakeing.jsp">返回比赛列表</a><br/>

<%}else if("success".equals(result)) {%>
<%=tip%><br/>
<a href="/pet/stakeing.jsp">返回比赛列表</a><br/>
<%}else{%>



<a href="/pet/stakeact.jsp?task=1">开设赌局</a><br/>


<a href="/pet/stakeact.jsp?task=5">开始比赛</a><br/>
<br/><input name="name"  maxlength="10" value=""/>  
<br/>
    <anchor title="确定">确定
    <go href="/pet/stakeact.jsp?task=6" method="post">
    <postfield name="name" value="$name"/>
    </go>
    </anchor>
<br/>

<%}%>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>