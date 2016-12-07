<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.user.*,net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
UserAction action = new UserAction(request);
UserBean loginUser = action.getLoginUser();
int toUserId = action.getParameterInt("tid");
String url = ("editNote.jsp?tid=" + toUserId);
UserNoteBean note = UserInfoUtil.getUserNote(loginUser.getId(), toUserId);
if(note == null) {
	response.sendRedirect(url);
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="备注">
<%=BaseAction.getTop(request, response)%>
<p align="left">
<%=StringUtil.toWml(note.getShortNote())%><br/>
<%=StringUtil.toWml(note.getNote())%><br/>
<a href="<%=url%>">编辑备注</a><br/>
<a href="editNote.jsp?d=1&amp;tid=<%=toUserId%>">删除备注</a><br/>
<a href="/chat/post.jsp?toUserId=<%=toUserId%>">返回聊天</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>