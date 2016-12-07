<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.user.*,net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
UserAction action = new UserAction(request);
action.editNote();
UserBean loginUser = action.getLoginUser();
int toUserId = action.getParameterInt("tid");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(action.isResult("edit")){%>
<card title="编辑备注">
<%=BaseAction.getTop(request, response)%>
<p align="left">
简短备注(聊天时显示):<br/>
<input type="text" name="shortNote" maxlength="15" value=""/><br/>
详细备注:<br/>
<input type="text" name="note" maxlength="100" value=""/><br/>
<anchor title="post">确认
  <go href="editNote.jsp" method="post">
    <postfield name="tid" value="<%=toUserId%>"/>    
    <postfield name="shortNote" value="$shortNote"/>
    <postfield name="note" value="$note"/>
  </go>
</anchor><br/>
<a href="/chat/post.jsp?toUserId=<%=toUserId%>">返回聊天</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="编辑备注">
<%=BaseAction.getTop(request, response)%>
<p align="left">
<%=action.getTip()%><br/>
<a href="/chat/post.jsp?toUserId=<%=toUserId%>">返回聊天</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>