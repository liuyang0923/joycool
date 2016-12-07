<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
int id=action.getParameterInt("id");
int uid=action.getParameterInt("uid");
if(id==0){
response.sendRedirect("/fm/index.jsp");return;
}
String backTo=action.getParameterString("backTo");
if(backTo==null||"".equals(backTo)){
backTo="/fm/index.jsp";
}
FamilyHomeBean home=FamilyAction.getFmByID(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族"><p align="left"><%
if(request.getParameter("cmd")==null){
%><%=net.joycool.wap.util.StringUtil.toWml(home.getFm_name())%>家族族长<%=home.getLeaderNickNameWml()%>想将该家族族长职位转让给您,是否接受该申请?<br/>
<br/>
<a href="sureabdicate.jsp?cmd=y&amp;id=<%=id%>&amp;uid=<%=uid%>">接受转让</a><br/>
<a href="sureabdicate.jsp?cmd=n&amp;id=<%=id%>&amp;uid=<%=uid%>&amp;backTo=<%=backTo.replace("&", "&amp;")%>">拒绝</a><br/><%
}else if("y".equals(request.getParameter("cmd"))){
if(action.receiveShaikhAbdicate()){%>
您接受了转让申请,成为<%=net.joycool.wap.util.StringUtil.toWml(home.getFm_name())%>家族族长.<br/>
<a href="/fm/myfamily.jsp?id=<%=home.getId()%>">返回我的家族</a><br/><%
}else{
if(action.getTip()!=null){%><%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/><%}
}
}else if("n".equals(request.getParameter("cmd"))){action.refuseShaikhAbdicate();
out.println(action.getTip());
}%><a href="<%=backTo.replace("&", "&amp;")%>">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>