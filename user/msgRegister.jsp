<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
String mobile = request.getParameter("mobile");
if(mobile != null)
	Util.msgRegister(mobile);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
done!
</wml>