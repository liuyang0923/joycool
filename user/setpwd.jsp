<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.user.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.bean.friend.FriendRingBean" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String shuqian = request.getParameter("shuqian");
String tip2="";
if(!"".equals(shuqian)&&shuqian!=null){
	tip2 = "当前密码为空,必须设置密码后才能存书签！！";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:present name="result" scope="request">

    <logic:equal name="result" value="failure">
<card title="修改密码">
<p align="left">
<%=BaseAction.getTop(request, response)%>
修改密码<br/>
--------<br/>
    <bean:write name="tip" filter="false"/><br/>
	<anchor title="back"><prev/>返回修改密码页面</anchor><br/>
<%
String backTo = (String) request.getAttribute("backTo");
backTo = backTo.replace("&", "&amp;");
%> 
    <a href="<%=backTo%>">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>

    <logic:equal name="result" value="success">
<%
String backTo = (String) request.getAttribute("backTo");
backTo="/user/userInfo.jsp";
//zhul 2006-06-29 判断如果这个成功页面是从交友中心的修改页跳过来的，则该页自动跳回到交友中心　start
String tag=(String)request.getAttribute("tag");
if(tag!=null) backTo="/friendadver/friendAdverIndex.jsp";
//zhul 2006-06-29 判断如果这个成功页面是从交友中心的修改页跳过来的，则该页自动跳回到交友中心　end
backTo = backTo.replace("&", "&amp;");
%>
<card title="修改密码" ontimer="<%=response.encodeURL(backTo)%>">
    <timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
修改密码<br/>
------------------<br/>
修改成功！3秒后自动跳转。<br/>
<a href="/enter/index.jsp">免输入ID/密码直接登录的秘籍</a><br/>
<a href="<%=backTo%>">返回</a>
<%if(loginUser.getHome()==1){%>
<a href="/home/editHome.jsp">修改个人家园</a><br/> 
<%}else{%>
<a href="/home/inputRegisterInfo.jsp">开通个人家园</a><br/> 
<%}%>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
</logic:present>

<logic:notPresent name="result" scope="request">
<card title="修改密码">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if (!"".equals(tip2)){%><%=tip2%><br/><%}%>
<%if ("".equals(tip2)){%>输入旧密码:<br/><input name="old"  maxlength="20" value=""/><br/>(如果旧密码为空,可以不写)<br/><br/><%}%>
<%if (!"".equals(tip2)){%>输入密码:(4到20位)<br/><%}else{
%>输入新密码:(4到20位)<br/><%
}%><input name="password"  maxlength="20" value=""/><br/>
<anchor title="确定">
<%if ("".equals(tip2)){%>确认修改密码<br/><%}else{
%>确认密码<br/><%
}%><go href="UserInfo.do" method="post">
    <postfield name="old" value="$old"/>
    <postfield name="password" value="$password"/>
</go>
</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>
</wml>