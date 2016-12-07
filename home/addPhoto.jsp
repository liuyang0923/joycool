<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.home.*,java.util.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
HomeServiceImpl homeServiceImpl = new HomeServiceImpl();
List list = homeServiceImpl.getHomePhotoCatList(loginUser.getId(), HomePhotoCat.PRIVACY_SELF);
response.setHeader("Cache-Control","no-cache");

String backTo=request.getParameter("backTo");
int catId = StringUtil.toInt(request.getParameter("cid")!=null?request.getParameter("cid").toString():"0");
if(backTo==null||backTo.equals("")) backTo="/friendadver/friendAdverIndex.jsp";
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
</head>
<body><%=BaseAction.getTop(request, response)%>
增加照片<br/>
------------------------<br/>
<logic:present name="result" scope="request">
<p align="left">
增加成功！<br/>
<a href="homePhoto.jsp?uid=<%=loginUser.getId()%>&amp;cid=<%=request.getAttribute("cid") %>">返回相册</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%=BaseAction.getBottom(request, response)%>
</p>
</logic:present>
<logic:notPresent name="result" scope="request">
<logic:present name="tip">
<font color="red"><bean:write name="tip"/></font><br/>
</logic:present>
<form name="attForm" enctype="multipart/form-data" method="post" action="<%=response.encodeURL("/home/UploadPhoto.do")%>">
标题(18字内):<br/>&nbsp;&nbsp;&nbsp;&nbsp;<input name="content" size="18" value="<logic:present name="content"><bean:write name="content"/></logic:present>"/><br/>
相册:<br/>&nbsp;&nbsp;&nbsp;&nbsp;<select name="age">
<option value="0"<%if(catId==0){%>selected<%}%>>默认分类</option>
<%for(int i = 0; i < list.size();i++) {
	HomePhotoCat cat = (HomePhotoCat)list.get(i);%>
<option value="<%=cat.getId() %>"<%if(catId==cat.getId()){%>selected<%}%>><%=StringUtil.toWml(cat.getCatName()) %></option>
<%} %></select><br/>
图片*:128*128以内<br/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="file" name="attachment" value=""/><br/>
<br/><input type="submit" name="submit" value="发表"/><br/>
注：支持WAP2.0的手机才可以上传图片。
</form>
<%if (catId <= 0){
%><a href="homePhotoCat.jsp">返回相册首页</a><br/><%	
} else {
%><a href="homePhoto.jsp?cid=<%=catId %>">返回相册</a><br/><%	
}
%>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%=BaseAction.getBottom(request, response)%>
</logic:notPresent>
</body>
</html>