<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
Vector list = (Vector)request.getAttribute("list");
String backTo = (String)request.getAttribute("backTo");
String prefixUrl = (String)request.getAttribute("prefixUrl");
String rootBackTo = (String)request.getAttribute("rootBackTo");
String rootId = (String)request.getAttribute("rootId");
String jaLineId = (String)request.getAttribute("jaLineId");
String cId=(String)request.getParameter("id");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷酷图">
<p align="left">
<%=BaseAction.getTop(request, response)%>
    <%if((list == null)||(list.size()<1)){%>
    没有图片子类别！<br/>
    <%}else
    {   CatalogBean currentCata;
    for(int i = 0;i <list.size();i++)
        { currentCata = (CatalogBean)list.get(i);
          String name = currentCata.getName();
          int id = currentCata.getId();
          //String address = "/ImageCataList.do?id="+id;
        %>
    <a href="ImageCataList.do?id=<%=id %>&amp;jaLineId=<%=jaLineId %>&amp;backTo=<%=PageUtil.getBackTo(request)%>"><%=name%></a><br/>
    <%}
    }%>
    <br/>
    <!--<a href="ImageCataList.do?id=<%=rootId%>" title="返回">返回图片</a><br/>-->
    <%if(cId != null){%>
    <%String url=URLMap.getBacktoURL("ImageCataList.do?id=",StringUtil.toInt(cId));%>
    <a href="<%=(url)%>" title="进入">返回图片频道</a><br/>
    <%}%>
	<%=rootBackTo%>
</p>
</card>
</wml>