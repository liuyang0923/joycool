<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil"%><%@ page import="jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%!
	static String[] openNames = {"只允许家人进入","允许友联家族进入","允许全部家族玩家进入"};
%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0||!fmLoginUser.isflagAppoint()&&!fmLoginUser.isflagChat()){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fm =FamilyAction.getFmByID(fmLoginUser.getFm_id());
if(fm==null){
response.sendRedirect("/fm/index.jsp");return;
}
int operation = action.getParameterInt("op");
if(operation==1||operation == 2) {	// 设置天花或者2：删除天花
	if(fmLoginUser.isflagChat()){
		if(operation==2)
			fm.setChatTop("");
		else {
			String top = action.getParameterNoEnter("chattop");
			if(top!=null)
				fm.setChatTop(top);
			else
				fm.setChatTop("");
		}
		FamilyAction.service.executeUpdate("update fm_home set chat_top='" + StringUtil.toSql(fm.getChatTop()) + "' where id=" + fm.getId());
		action.tip("tip","聊天室置顶设置成功");
	}
} else if(operation == 3){	// 设置状态
	if(fmLoginUser.isflagAppoint()){
		int open = action.getParameterInt("open");
		if(open < 0 || open > 2)
			open = 0;
		if(open == 2 && fm.getFm_level()<2){
			action.tip("tip","家族等级不足无法设置");
		} else {
			if(open != fm.getChatOpen()){
				fm.setChatOpen(open);
				FamilyAction.service.executeUpdate("update fm_home set chat_open=" + open + " where id=" + fm.getId());
			}
			action.tip("tip","聊天室开放设置成功");
		}
	}
}
%>
<wml><card title="聊天室设置"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
==聊天室置顶==<br/>
<%=fm.getChatTopWml()%>
<%if(fmLoginUser.isflagChat()){%>
设置为:(最多50字)<br/>
<input name="chattop" maxlength="50"/>
<anchor title="确定">
确定设置<go href="chat.jsp?op=1" method="post">
<postfield name="chattop" value="$(chattop)" />
</go></anchor>|<a href="chat.jsp?op=2">删除设置</a><br/>
<%}%>
==聊天室开放==<br/>
当前状态:<%=openNames[fm.getChatOpen()]%><br/>
<%if(fmLoginUser.isflagAppoint()){%>
<select name="open" value="<%=fm.getChatOpen()%>"><%
for(int i=0;i<openNames.length;i++){
%><option value="<%=i%>"><%=openNames[i]%></option><%
}%>
</select>
<anchor title="确定">
确定设置<go href="chat.jsp?op=3" method="post">
<postfield name="open" value="$(open)" />
</go></anchor><br/>
<%}%>
<br/>
&gt;&gt;<a href="../chat/chat.jsp">进入聊天室</a><br/>
&gt;&gt;<a href="../chat/ban.jsp">查看聊天室封禁</a><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>