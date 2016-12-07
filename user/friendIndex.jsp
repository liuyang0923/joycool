<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
我的好友<br/>
--------<br/>
1.<a href="ViewFriends.do">我的好友</a><br/>
2.<a href="ViewBadGuys.do">我的黑名单</a><br/>
3.<a href="searchUser.jsp" title="进入">查找添加好友</a><br/>
<br/>    

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>