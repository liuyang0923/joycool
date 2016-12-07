<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.StringUtil,net.joycool.wap.framework.BaseAction,jc.family.*,jc.family.game.vs.*"%><%
FamilyAction action=new VsAction(request,response);
int id=action.getParameterInt("id");
if(id==0){
response.sendRedirect("index.jsp");return;
}
FamilyHomeBean fmhome=FamilyAction.getFmByID(id);
if(fmhome==null){
response.sendRedirect("index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="欢迎回来" ontimer="<%=response.encodeURL("/fm/myfamily.jsp?id="+id)%>"><timer value="20"/><p align="left"><%=BaseAction.getTop(request, response)%>
【<%=fmhome.getFm_nameWml()%>】家族<br/>
<%if(action.getLoginUser()!=null){%>
欢迎回来,<%=action.getLoginUser().getNickNameWml()%><br/>
<%}else{%>欢迎光临【<%=fmhome.getFm_nameWml()%>】家族<%}%>
(2秒后自动跳转)<br/>
&lt;<a href="/fm/myfamily.jsp?id=<%=id%>"><%=fmhome.getFm_nameWml()%></a>&lt;<a href="/fm/index.jsp">家族首页</a>
</p></card></wml>