<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.ring.PringFileBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
Vector pring_file = (Vector)request.getAttribute("pring_file");
String catalog_id=(String)request.getAttribute("catalog_id");
String backTo=(String)request.getParameter("backTo");
String catalogname=(String)request.getAttribute("catalogname");
String downAddress="";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="炫铃下载">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if((pring_file == null)||(pring_file.size()<1)){%>
该歌曲暂时无法下载，请稍后再试！
<%}else{
    PringFileBean currentPring_file;
    for(int i = 0;i <pring_file.size();i++)
        { currentPring_file = (PringFileBean)pring_file.get(i);
          downAddress = currentPring_file.getFile();
		  %>
    <%=i+1%>:
	<%=currentPring_file.getFile_type()%>格式下载：<br/>
	<a href="http://wap.joycool.net/joycool-rep/pring/<%=downAddress%>">点击下载</a><br/>
	<%}%>
	<%}%>
    <br/>
    <a href="RingCataList.do?id=<%=catalog_id%>" title="返回">返回<%=catalogname%></a><br/>
    <%if(backTo != null){%>
    <a href="<%=backTo.replace("&","&amp;")%>" title="进入">返回上一级</a><br/>
    <%}%>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>