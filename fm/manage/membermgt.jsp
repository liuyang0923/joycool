<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%@ page import="java.util.List" %><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fHome = FamilyAction.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族成员"><p align="left"><%
int is=0;
if(fmLoginUser.isflagNewMember()){
	is=action.service.selectisFmNewMan(fmLoginUser.getFm_id());
}
String p="";
if(request.getParameter("p")!=null){
	p="&amp;p="+request.getParameter("p");
}
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action,fHome.getFm_member_num(), 10, "p");
List list =action.service.selectFmUserList(fmLoginUser.getFm_id(),paging.getStartIndex(),paging.getCountPerPage(),"");
if(is!=0){
	%><a href="recruit.jsp">招募新人</a><%=is%>个未处理<br/><%
}
%>家族成员<%=fHome.getFm_member_num()%>个<br/><%
if(list!=null&&list.size()>0){
	for(int i=0;i<list.size();i++){
		FamilyUserBean bean=(FamilyUserBean)list.get(i);
		%><%=bean.getNickNameWml()%><%
		if(fHome.getLeader_id()!=bean.getId()&&fmLoginUser.getId()!=bean.getId()){
			%>|<a href="tofire.jsp?uid=<%=bean.getId()%><%=p%>">开除</a><%
		}%><br/><%
	}%><%=paging.shuzifenye("membermgt.jsp", false, "|", response)%><%
}
if(fmLoginUser.isflagNewMember()){
	%><a href="recruit.jsp">招募新人</a><br/><%
}
%>按ID:<br/>
<input name="uid" format="*N" maxlength="9"/><br/><anchor title="开除">
开除<go href="tofire.jsp" method="post">
	<postfield name="uid" value="$(uid)" /></go>
</anchor><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>