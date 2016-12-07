<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
int id=action.getParameterInt("id");
if(id==0){
	FamilyUserBean fmLoginUser=action.getFmUser();
	if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
		response.sendRedirect("/fm/index.jsp");return;
	}else{
		id=fmLoginUser.getFm_id();
	}
}
FamilyHomeBean fmhome=FamilyAction.getFmByID(id);
String cmd=request.getParameter("cmd");
boolean isfrom=false;
if(request.getParameter("from")!=null){
isfrom=true;
String info=action.getParameterString("fminfo");
action.updateFmInfo(id,info);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族简介"><p align="left"><%=BaseAction.getTop(request, response)%><%
if((cmd==null||!cmd.equals("see"))&&!isfrom){
	%><%=StringUtil.toWml(fmhome.getInfo())%><br/>
	修改为:(最多200字)<br/>
	<input name="fminfo" maxlength="200" />
	<anchor title="OK">确定
	  <go href="familydes.jsp" method="post">
	    <postfield name="from" value="from"/>
	    <postfield name="fminfo" value="$(fminfo)"/>
	  </go>
	</anchor><br/><%
}else if(isfrom){
	%><%=action.getTip()%><br/>
	<a href="familydes.jsp">直接返回</a><br/>
	<%
}else{
	%>【<%=fmhome.getFm_nameWml()%>】<br/>
成立于<%=DateUtil.formatDate1(fmhome.getFm_time())%>,现有成员<%=fmhome.getFm_member_num()%>人<br/>
【家族简介】<br/>
<%=StringUtil.toWml(fmhome.getInfo())%><br/><%
}
%>
&lt;<a href="/fm/myfamily.jsp?id=<%=fmhome.getId()%>">返回家族</a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>