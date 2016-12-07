<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.mall.*,net.wxsj.bean.mall.*"%><%
MallAdminAction action = new MallAdminAction();
action.editReply(request, response);

String result = (String) request.getAttribute("result");
ReplyBean reply = (ReplyBean) request.getAttribute("reply");

//失败
if("failure".equals(result)){
	String tip = (String) request.getAttribute("tip"); 
%>
<script>
alert("<%=tip%>");
history.back(-1);
</script>
<%
	return;
}
//成功
else if("success".equals(result)){
%>
<script>
alert("修改成功！");
document.location = "info.jsp?id=<%=reply.getParentId()%>";
</script>
<%
	return;
}
%>
<form action="editReply.jsp" method="post">
用户昵称：<input type="text" name="userNick" size="20" value="<%=reply.getUserNick()%>"/><br/>
内容：<textarea name="content" cols="40" rows="5"><%=reply.getContent()%></textarea> <br/>
<input type="hidden" name="id" value="<%=reply.getId()%>"/>
<input type="submit" name="B" size="20" value="修改"/>
</form>