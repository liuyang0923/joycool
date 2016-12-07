<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
%><%
Vector userList = (Vector) request.getAttribute("userList");
String backTo = (String) request.getAttribute("backTo");
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
String toPage = (String) request.getAttribute("toPage");
int i, count;
UserBean user;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="选择好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
选择好友<br/>
--------<br/>
0.<a href="<%=(toPage + "?backTo=" + backTo + "&amp;userId=-1")%>">所有好友</a><br/>
<%
count = userList.size();
for(i = 0; i < count; i ++){
	user = (UserBean) userList.get(i);
%>
<%=(i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="<%=(toPage + "?backTo=" + backTo + "&amp;userId=" + user.getId())%>"><%=user.getNickName()%></a>
<br/>
<%
}
%>
<br/>
<a href="<%=(oldBackTo.replace("&", "&amp;"))%>" title="进入">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>