<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UrlMapBean" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
Vector list = (Vector)request.getAttribute("list");
String backTo = (String)request.getAttribute("backTo");
String prefixUrl = (String)request.getAttribute("prefixUrl");
String rootId = (String)request.getAttribute("rootId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷炫铃">
<p align="left">
<%=BaseAction.getTop(request, response)%>
    <%if((list == null)||(list.size()<1)){%>
    没有炫铃子类别<br/>
    <%}else
    {   CatalogBean currentCata;
    for(int i = 0;i <list.size();i++)
        { currentCata = (CatalogBean)list.get(i);
          String name = currentCata.getName();
          int id = currentCata.getId();
        %>
    <a href="RingCataList.do?id=<%=id%>"><%=name%></a><br/>
    <%}
    }%>
    <br/>
   <a href="RingCataList.do?id=<%=rootId%>" title="返回">返回炫铃</a><br/>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>