<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction"%><%@ page import="net.joycool.wap.bean.pk.PKActBean"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.PageUtil"%><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.sceneList(request);
//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector pkActList=(Vector)request.getAttribute("pkActList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/pk/scene.gif" alt="场景列表"/><br/>
请选择开始战斗的地点<br/>
<%
if(pkActList.size()>0){
	PKActBean pkAct=null;
	for(int i=0;i<pkActList.size();i++){
		pkAct=(PKActBean)pkActList.get(i);
		if(pkAct==null)continue;
		%>
		<%=i+1 %>.<a href="/pk/index.jsp?sceneId=<%=pkAct.getId()%>"><%=StringUtil.toWml(pkAct.getName())%></a><br/>
	<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
	if(!"".equals(fenye)){%><%=fenye%><br/><%}
}else{%>
没有查询到结果记录<br/><%
}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>