<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.job.*,java.util.*,net.joycool.wap.action.job.*,net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");

HuntAction  hunt=new HuntAction(request);

hunt.quarryList(request);

String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector quarryList=(Vector)request.getAttribute("quarryList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猎物列表">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您现在有猎物(<%=totalCount%>)种：<br/>
<%
if(quarryList!=null){
for(int i = 0; i < quarryList.size(); i ++){
	HuntUserQuarryBean userQuarry = (HuntUserQuarryBean) quarryList.get(i);
	HashMap quarryMap=LoadResource.getQuarryMap();
	HuntQuarryBean quarry=(HuntQuarryBean)quarryMap.get(new Integer(userQuarry.getQuarryId()));
%>
<%=(Integer.parseInt(pageIndex)*Integer.parseInt(perPage)+i + 1)%>.<%=StringUtil.toWml(quarry.getName())%>  <%=userQuarry.getQuarryCount()%><br/>
<%
}}
%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"viewQuarryList.jsp",false," ",response)%>
<br/>
<a href="quarryMarket.jsp">猎物收购站</a><br/>
<a href="huntArea.jsp">返回狩猎区</a><br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="<%=("/lswjs/index.jsp")%>">返回导航中心</a><br/>
</p>
</card>
</wml>