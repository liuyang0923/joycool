<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.bean.PagingBean,net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean family = FamilyAction.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="权限管理"><p align="left">
您可以给予家族成员称号,并赋予其部分管理权限.<br/><%
String p="";
if(request.getParameter("p")!=null){
	p="&amp;p="+request.getParameter("p");
}
PagingBean paging = new PagingBean(action, family.getFm_member_num(), 10, "p");
List list=action.service.selectFmUserList(fmLoginUser.getFm_id(), paging.getStartIndex(), paging.getCountPerPage(), " order by gift_fm desc");
if(list!=null&&list.size()>0){
	for(int i=0;i<list.size();i++){
		FamilyUserBean bean=(FamilyUserBean)list.get(i);
		%><a href="../fmuserinfo.jsp?userid=<%=bean.getId()%>"><%=bean.getNickNameWml()%></a>|贡献<%=bean.getCon_fm()%>|<a href="powermgt.jsp?uid=<%=bean.getId()%><%=p%>">任命</a><br/><%
	}
}
%><%=paging.shuzifenye("power.jsp", false, "|", response)%>
按ID:<br/>
<input name="uid" format="*N" maxlength="9"/><br/><anchor title="任命">任命
<go href="powermgt.jsp" method="post">
<postfield name="uid" value="$(uid)"/></go>
</anchor><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>