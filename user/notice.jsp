<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.action.user.RankAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="登录乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=RankAction.getUserInfo(loginUser)%><br/>
<anchor title="back"><prev/>返回</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>