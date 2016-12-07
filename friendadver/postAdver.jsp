<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String backTo=request.getParameter("backTo");
if(backTo==null||backTo.equals("")) backTo="/friendadver/friendAdverIndex.jsp";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发布交友广告">
<p align="left"><%=BaseAction.getTop(request, response)%>

发布交友广告<br/>
----------------<br/>
<logic:present name="tip">
<font color="red"><bean:write name="tip"/></font><br/>
</logic:present>
标题(限20字)<br/>
<input name="title" emptyok="false" size="20" maxlength="20" value=""/><br/>
期望性别:<select name="gender"><% if(loginUser.getGender()==1){%><option value="0" >女</option><option value="1">男</option><%}else{%><option value="1">男</option><option value="0" >女</option><%}%></select><br/>
期望年龄:<select name="age"><option value="<%=Constants.AGE18_20%>">18-20</option><option value="<%=Constants.AGESUB17%>">17以下</option><option value="<%=Constants.AGE21_25%>">21-25</option><option value="<%=Constants.AGE26_30%>">26-30</option><option value="<%=Constants.AGE31_40%>">31-40</option><option value="<%=Constants.AGEUP41%>">41以上</option></select><br/>
期望地区:<select name="area"><option value="0">不限</option><option value="<%=loginUser.getCityno()%>">本地</option></select><br/>
择友要求:(限50字)<br/>
<input name="remark" emptyok="false" size="20" maxlength="50" value=""/><br/><br/>
<anchor title="确定">提交
    <go href="/friendadver/PostAdver.do" method="post">
	<postfield name="title" value="$title"/>
	<postfield name="sex" value="$gender"/>
	<postfield name="age" value="$age"/>
	<postfield name="area" value="$area"/>
	<postfield name="remark" value="$remark"/>
    </go>
</anchor>
<br/>
<%--<a href="<%=(backTo.replace("&","&amp;"))%>">返回进入页</a><br/>--%>
<a href="friendAdverIndex.jsp">返回交友广告</a>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
</p>
</card>
</wml>