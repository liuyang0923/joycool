<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.framework.BaseAction,jc.family.*,jc.family.game.yard.*"%><%
YardAction action=new YardAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fm=action.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(fm==null){
	%><wml><card title="系统提示"><p align="left"><%=BaseAction.getTop(request, response)%>
	家族已解散<br/>
	<a href="/fm/index.jsp">返回家族首页</a><br/><%
}else{
	int cmd=action.getParameterInt("cmd");
	String name = action.getParameterNoEnter("name");
	%><wml><card title="餐厅开张"><p align="left"><%=BaseAction.getTop(request, response)%><%
	if(cmd==0){
		%>餐厅开张需要花费一亿乐币!<br/>
餐厅名称:(2到8字)<br/>
<input name="eaname"  maxlength="8"/><br/>
<anchor title="确定">确认开张
<go href="eatery.jsp?cmd=1" method="post">
    <postfield name="name" value="$eaname"/>
</go></anchor><br/><%
	}else{
		if(!fmLoginUser.isflagLeader()){
			%>您不是族长,没有权限<br/><%
		}else if(name==null||name.length()<2||name.length()>8){
			%>餐厅名称为2到8个字!<br/><%
		}else if(fm.getMoney()<action.createMoney){
			%>您的家族基金不足,餐厅无法开张!<br/><%
		}else if(fm.isEatery()){
			%>您的家族已经有餐厅!<br/><%
		}else{
			int forum=action.createEatery(fm,name);
			if(forum!=0){
				%>家族餐厅开张了!<br/>
				<a href="/fm/game/yard/index.jsp?id=<%=fmLoginUser.getFm_id()%>">家族餐厅</a><br/><%
			}else{
				%>失败<br/><%
			}
		}
	}
	%><a href="management.jsp">返回家族管理</a><br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>