<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.ring.PringBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String ringN=(String)request.getAttribute("ringName");
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector rings = (Vector)request.getAttribute("rings");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="炫铃搜索">
<p align="left">
<%=BaseAction.getTop(request, response)%>
    <%if((rings == null)||(rings.size()<1)){%>
    搜索结果为空<br/>
    <%}else
    {  %>
<%
     PringBean currentRing;
    for(int i = 0;i <rings.size();i++)
        { currentRing = (PringBean)rings.get(i);
          String ringName = currentRing.getName();
        %>
    <%=(Integer.parseInt(perPage)*Integer.parseInt(pageIndex) + i+1)%>:
    <a href="RingInfo.do?ringId=<%=currentRing.getId()%>"><%=ringName%></a><br/>
	人气：<%=currentRing.getDownload_sum()%><br/>
    <%}%>
    <%}%>
<%if(rings!=null&&rings.size()>5){%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"SearchRing.do?ringName="+ringN,true," ",response)%><br/>
<%}%>
<a href="/Column.do?columnId=5188" title="返回">返回铃声首页</a><br/>

	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>