<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,jc.friend.stranger.*,net.joycool.wap.bean.*"%><%
	response.setHeader("Cache-Control", "no-cache");
	ChatAction action = new ChatAction(request,response);
	if(action.getParameterString("exit") != null){
		UserBean ub = action.getLoginUser();
		int uid = 0;
		if(ub != null)
			ub.getId();
		ChtBean ctb = action.service.getChater(" user_id="+ uid);
		if(ctb != null)
			action.updateChat(" user_id="+ ctb.getRoomId());
		action.deleteCht(uid);	
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="陌生人聊天"><p><%=BaseAction.getTop(request, response)%>
和神秘的TA一对一私聊<br/><a href="chat.jsp">进入陌生人聊天</a><br/>
<a href="strangerrule.jsp">陌生人聊天说明</a><br/><%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>