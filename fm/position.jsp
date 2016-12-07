<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
int id=action.getParameterInt("id");
if(id==0){
response.sendRedirect("index.jsp");return;
}
FamilyHomeBean fmhome=action.getFmByID(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族官员"><p align="left"><%=BaseAction.getTop(request, response)%><%
java.util.List list=action.service.selectOfficerList(id,0,100);
if(list!=null&&list.size()>0){
for(int i=0;i<list.size();i++){
FamilyUserBean bean=(FamilyUserBean)list.get(i);%>
<%=i+1%>.<a href="fmuserinfo.jsp?userid=<%=bean.getId()%>"><%=bean.getNickNameWml()%></a>(<%=StringUtil.toWml(bean.getFm_name())%>)<br/><%
}
}%>&lt;<a href="myfamily.jsp?id=<%=id%>"><%=fmhome==null?"":fmhome.getFm_nameWml()%></a>&lt;<a href="index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>