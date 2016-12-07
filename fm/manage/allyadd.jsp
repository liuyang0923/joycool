<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.bean.PagingBean, java.util.List,net.joycool.wap.util.LoadResource,net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.util.SqlUtil,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmUser=action.getFmUser();
if(fmUser==null||fmUser.getFm_id()==0||!fmUser.isflagPublic()){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fm=action.getFmByID(fmUser.getFm_id());
int cmd=action.getParameterInt("cmd");
boolean isfrom=false;
if(cmd!=0){
	action.dealAlly(fmUser.getFm_id(),action.getParameterInt("allyid"),cmd);
	isfrom=true;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="添加友联家族"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(isfrom){
	%><%=action.getTip()%><br/>
	<a href="allyadd.jsp">返回添加友联</a><br/><%
}else{
	%>家族ID:<br/>
	<input name="fmid" maxlength="10" />
	<anchor title="OK">添加
		<go href="allyadd.jsp?cmd=1" method="post">
		<postfield name="allyid" value="$(fmid)"/>
	</go></anchor><br/><%
	int c=action.service.selectIntResult("select count(id) from fm_home");
	PagingBean paging=new PagingBean(action, c, 10, "p");
	int p=action.getParameterInt("p");
	List list=action.service.selectFamilyIdList("1 limit "+paging.getStartIndex()+","+paging.getCountPerPage());
	if(list!=null&&list.size()>0){
		int count=0;
		for(int i=0;i<list.size();i++){
			Integer fmId=(Integer)list.get(i);
			FamilyHomeBean home=FamilyAction.getFmByID(fmId.intValue());
			if(home!=null){
				count++;
				%><%=count+p*10%>.<%=StringUtil.toWml(home.getFm_name())%>|<a href="allyadd.jsp?allyid=<%=fmId%>&amp;cmd=1">添加</a><br/><%
			}
		}
	}
	%><%=paging.shuzifenye("allyadd.jsp", false, "|", response)%>
	<a href="allyset.jsp">返回设置友联</a><br/><%
}
%><a href="management.jsp">返回家族管理</a><br/>
&lt;<a href="/fm/myfamily.jsp">我的家族</a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>