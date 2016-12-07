<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.List,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fHome = FamilyAction.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(fmLoginUser==null||!fmLoginUser.isflagNewMember()){
	%><wml><card title="招募新人" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><timer value="30"/><p align="left">
	您无权管理(3秒后自动返回)<br/><%
}else{
	%><wml><card title="招募新人"><p align="left"><%
	List list=action.service.selectfamilyJoin(fmLoginUser.getFm_id(), 0, 5);
	int joinConut =action.service.selectfamilyJoinCount(fmLoginUser.getFm_id());
	Integer joinconut=(Integer)request.getAttribute("joinconut");
	%>当前<%=joinConut%>个待处理申请<%=joinConut>5?"&#160;<a href=\"joinmore.jsp\">更多</a>":""%><br/><%
	for(int i=0;i<list.size();i++){
		FamilyNewManBean fmNew=(FamilyNewManBean)list.get(i);
		%><a href="/user/ViewUserInfo.do?userId=<%=fmNew.getUserid()%>"><%=1+i%>.<%=StringUtil.toWml(fmNew.getUsername())%></a>|<a href="rerecruit.jsp?id=<%=fmNew.getId()%>&amp;cmd=y" >接受</a>|<a href="rerecruit.jsp?id=<%=fmNew.getId()%>&amp;cmd=n" >拒绝</a><br/><%
	}
	%>邀请用户加入家族:<br/>
	ID:<input name="userid" format="*N" maxlength="9"/><br/>
	<anchor title="OK">邀请<go href="invite.jsp" method="post">
	<postfield name="userid" value="$(userid)"/></go>
	</anchor><br/><%
	list=action.service.selectfamilyInvite(fmLoginUser.getFm_id(), 0, 5);
	int inviteConut=action.service.selectfamilyInviteCount(fmLoginUser.getFm_id());
	%>已发送邀请:<%=inviteConut%><%=inviteConut>5?"&#160;<a href=\"invitemore.jsp\">更多</a>":""%><br/><%
	for(int i=0;i<list.size();i++){
		FamilyNewManBean fmNew=(FamilyNewManBean)list.get(i);
		%><a href="/user/ViewUserInfo.do?userId=<%=fmNew.getUserid()%>"><%=1+i%>.<%=StringUtil.toWml(fmNew.getUsername())%></a>|<a href="rerecruit.jsp?id=<%=fmNew.getId()%>&amp;cmd=c">取消邀请</a><br/><%
	}
	%><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>