<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
MapNodeBean node = action.getUserNode();
List list = action.world.signList;
int sign = 0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
当前位置:<%=action.world.getNodeDetail(node)%><br/>
<%
for(int i = 0;i < list.size();i++){
MapSignBean bean = (MapSignBean)list.get(i);
if(bean.isVisible(node)){
sign++;
%>
路标-<%=bean.getName()%><br/>
-<%=FarmWorld.getDirectionString(node,bean.getNode())%>(<%=action.world.getNodeDetail(bean.getNode())%>)<br/>
<%}}%>
<%if(sign==0){%>附近没有可见的路标<br/><%}%>
<a href="map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>