<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.tong.TongLocationBean" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.tongLocation(request);
String result=(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("success")){
String tongName=(String)request.getAttribute("tongName");
Vector locationList=(Vector)request.getAttribute("locationList");
%>
<card title="帮会地点">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您希望把帮会地点建立在：<br/>
<%if(locationList!=null){
for(int i=0;i<locationList.size();i++){
TongLocationBean location=(TongLocationBean)locationList.get(i);
if(location!=null){%>
<anchor title="确定"><%=location.getName()%>
    <go href="/tong/erectResult.jsp" method="post">
    <postfield name="tongName" value="<%=StringUtil.toWml(tongName)%>"/>
        <postfield name="locationId" value="<%=location.getId()%>"/>
    </go>
</anchor>
(<%=location.getDescription()%>)<br/>
<%}
 }
}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("nameError")){%>
<card title="提示" ontimer="<%=response.encodeURL("/tong/tongErect.jsp")%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
(5秒后自动跳转到帮会申请页面）<br/>
<a href="/tong/tongErect.jsp">直接返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("userError")){%>
<card title="提示" ontimer="<%=response.encodeURL("/tong/tongList.jsp")%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(request.getAttribute("tip")!=null){%>
<%=request.getAttribute("tip")%><br/>
(5秒后自动跳转到帮会列表页面）<br/>
<a href="/tong/tongList.jsp">直接返回</a><br/>
<%}else{%>
哈哈，这位仁兄，想当帮会老大是需要本钱的。<br/>
1、100万资金<br/>
2、等级大于40<br/>
3、社交指数大于1000。<br/>
大侠请好好锻炼锻炼，再来组织。（5秒钟跳转回帮会列表页面）<br/>
<a href="/tong/tongList.jsp">直接返回</a>
<a href="/lswjs/index.jsp">去赚钱</a><br/>
<a href="http://wap.joycool.net/Column.do?columnId=5671">升级提示</a>
<a href="/chat/hall.jsp">聊天室</a> <br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>
