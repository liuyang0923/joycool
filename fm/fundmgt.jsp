<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族基金"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmLoginUser==null){
	%>家族不存在<br/>
	<a href="index.jsp">返回家族首页</a><br/><%
}else{
	%>你只要捐献一点点乐币,就可以为家族发展贡献力量哦.<br/>
	基金现有<%=action.getFmFund(fmLoginUser.getFm_id())%>乐币<br/>
	您现有乐币<%=request.getAttribute("gamePoint")%><br/>
	要捐款:<br/>
	<input name="contribute" format="*N" maxlength="10"/><br/>
	<anchor title="提取">确定
		<go href="recontribute.jsp" method="post">
			<postfield name="contribute" value="$(contribute)" />
		</go>
	</anchor><br/>
<a href="funduse.jsp">基金使用记录</a><br/>
<a href="fundapply.jsp">基金贡献榜</a><br/><%}%>
&lt;<a href="myfamily.jsp">返回家族</a>&lt;<a href="index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>