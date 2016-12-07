<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean,java.util.List"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
int id=action.getParameterInt("id");
if(id==0){
response.sendRedirect("index.jsp");return;
}
FamilyHomeBean fmhome=action.getFmByID(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="家族历史"><p align="left"><%=BaseAction.getTop(request, response)%>
家族历史|<a href="history2.jsp?id=<%=id%>">成员变动记录</a><br/><%
int c=action.service.selectIntResult("select count(id) from fm_history where fm_id=" + id);
PagingBean paging= new PagingBean(action, c, 10, "p");
List list=action.service.selectFmHistoryList(id, paging.getStartIndex(), paging.getCountPerPage());
if(list!=null&&list.size()>0){
for(int i=0;i<list.size();i++){
FamilyHistoryBean history=(FamilyHistoryBean)list.get(i);%>
(<%=net.joycool.wap.util.DateUtil.sformatTime(history.getEvent_time())%>)<%=net.joycool.wap.util.StringUtil.toWml(history.getEvent())%><br/>
<%}
}else{%>
暂无家族历史!<br/><%
}%>
<%=paging.shuzifenye("history.jsp?id=" + id, true, "|", response)%>
&lt;<a href="myfamily.jsp?id=<%=id%>"><%=fmhome==null?"":fmhome.getFm_nameWml()%></a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>