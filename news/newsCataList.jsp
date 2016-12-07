<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %><%
Vector list = (Vector)request.getAttribute("list");
String backTo = (String)request.getAttribute("backTo");
String rootBackTo = (String)request.getAttribute("rootBackTo");
String rootId = (String)request.getAttribute("rootId");
String jaLineId = (String)request.getAttribute("jaLineId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="新闻中心">
<p align="left">
<%=BaseAction.getTop(request, response)%>
    <%if((list == null)||(list.size()<1)){%>
    没有新闻子类别！<br/>
    <%}else
    {   CatalogBean currentCata;
    for(int i = 0;i <list.size();i++)
        { currentCata = (CatalogBean)list.get(i);
          String name = currentCata.getName();
          int id = currentCata.getId();
          String address = "NewsCataList.do?id="+id + "&amp;jaLineId=" + jaLineId;
        %>
    <a href="<%=(address)%>"><%=name%></a><br/>
    <%}
    }%>
    <br/>
    <!--<a href="NewsCataList.do?id=<%=rootId%>" title="返回">返回新闻</a><br/>-->
    <%if(backTo != null){%>
    <a href="<%=(backTo.replace("&","&amp;"))%>" title="进入">返回新闻首页</a><br/>
    <%}%>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>