<%@ page contentType="application/vnd.wap.xhtml+xml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(true){
	response.sendRedirect("postAdver.jsp");
	return;
}
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发表广告</title>
</head>
<p align="left"><%=BaseAction.getTop(request, response)%>
发布帖图交友广告<br/>
------------------<br/>
<logic:present name="result" scope="request">
<p align="left">
发表成功！<br/>
<a href="friendAdverIndex.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</logic:present>
<logic:notPresent name="result" scope="request">
<logic:present name="tip">
<font color="red"><bean:write name="tip"/></font><br/>
</logic:present>
<form name="attForm" ENCTYPE="multipart/form-data" method="post" action="<%=response.encodeURL("/friendadver/PostAttach.do")%>">
广告标题(限20字):<br/><input name="title" value="<logic:present name='title'><bean:write name='title'/></logic:present>"/><br/>
期望性别:<select name="sex"><% if(loginUser.getGender()==1){%><option value="0" >女</option><option value="1">男</option><%}else{%><option value="1">男</option><option value="0" >女</option><%}%></select><br/>
期望年龄:<select name="age"><option value="<%=Constants.AGESUB17%>">17以下</option><option value="<%=Constants.AGE18_20%>" selected="selected">18-20</option><option value="<%=Constants.AGE21_25%>">21-25</option><option value="<%=Constants.AGE26_30%>">26-30</option><option value="<%=Constants.AGE31_40%>">31-40</option><option value="<%=Constants.AGEUP41%>">41以上</option></select><br/>
期望地区:<select name="area"><option value="0" selected="selected">不限</option><option value="<%=loginUser.getCityno()%>">本地</option></select><br/>
择友要求(限50字):<br/><input name="content" value="<logic:present name="content"><bean:write name="content"/></logic:present>"/><br/>
我的玉照*:<br/><input type="file" name="attachment" value=""/><br/>
<input type="submit" name="submit" value="发表"/><br/>
注：支持WAP2.0的手机才可以上传图片。
</form>
<br/>
<a href="friendAdverIndex.jsp">返回交友广告</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</logic:notPresent>
</p>
</html>