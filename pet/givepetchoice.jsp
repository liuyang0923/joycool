<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.givePetChoice();
PetUserBean petUser = action.getPetUser();
String result= (String)request.getAttribute("result");
int userId = StringUtil.toInt((String)request.getAttribute("userId"));
UserBean user = (UserBean)UserInfoUtil.getUser(userId);
String url = ("/pet/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="宠物赠送" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/pet/index.jsp">我的宠物</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="宠物赠送">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%=StringUtil.toWml(petUser.getName())%>
眼巴巴地看着你：主人您真的要把我送给<%=StringUtil.toWml(user.getNickName())%> 吗？<br/>
<a href="<%=("/pet/givepetresult.jsp?userId="+userId+"&amp;"+"petId="+petUser.getId())%>">确定</a><br/>
<a href="/pet/index.jsp?">后悔了</a><br/>
<br/>
<a href="/pet/index.jsp?">返回宠物赠送</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>