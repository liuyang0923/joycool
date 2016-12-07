<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
MapNodeBean node = action.getUserNode();
MapBean map = action.world.getMap(node.getMapId());
List list = action.world.signList;
int sign = 0;
for(int i = 0;i < list.size();i++){
MapSignBean bean = (MapSignBean)list.get(i);
if(bean.isVisible(node))
sign++;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
当前位置:<%=node.getName()%>-<a href="chat.jsp">公聊</a><br/><%=node.getInfo()%><br/>
所属区域:<%=action.world.getMapDetail(map)%><%if(map.isFlagNoFlee()){%>(警告!!该区域地形狭窄,战斗中无法逃跑.)<%}%><br/>
区域等级:<%=map.getRank()%><br/>
<a href="mapS.jsp">查看临近路标(<%=sign%>)</a><br/>
<%if(!action.hasParam("c")){%>
<a href="mapD.jsp?c=2">查看区域地图</a><br/>
<%}else{%>
<%=map.getInfo()%><br/>
<a href="/farm/img/map/<%=map.getId()%>.gif"><img src="/farm/img/map/<%=map.getId()%>.gif" alt="map"/><br/>下载</a><br/>
<%}%>

<a href="map.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>