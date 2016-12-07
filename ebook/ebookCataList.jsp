<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.bean.friendlink.RandomLinkBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
Vector list = (Vector)request.getAttribute("list");
String backTo = (String)request.getAttribute("backTo");
String prefixUrl = (String)request.getAttribute("prefixUrl");
String rootId = (String)request.getAttribute("rootId");
CatalogBean catalog = (CatalogBean) request.getAttribute("catalog");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="电子书城">
<p align="left">
<%=BaseAction.getTop(request, response)%>
    <%if((list == null)||(list.size()<1)){%>
    没有电子书子类别！<br/>
    <%}else
    {   CatalogBean currentCata;
    for(int i = 0;i <list.size();i++)
        { currentCata = (CatalogBean)list.get(i);
          String name = currentCata.getName();
          int id = currentCata.getId();
          //String address = "/ImageCataList.do?id="+id;
        %>
    <a href="EBookCataList.do?id=<%=id%>"><%=StringUtil.toWml(name)%></a><br/>
    <%}
    }%>
	<br/>
	<%if(catalog.getParentId() != 0){%>
    <a href="<%=(prefixUrl)%>" title="返回">返回书城首页</a><br/>
	<%=BaseAction.getBottom(request, response)%><br/>
	<%} else if(session.getAttribute("downjoy") != null){%>
<a href="http://wap.downjoy.com">返回当乐网</a><br/>
技术支持：<a href="http://wap.joycool.net">乐酷门户</a><br/>
	<%} else {%>
<%=BaseAction.getBottom(request, response)%><br/>
	<%}%>
</p>
</card>
</wml>