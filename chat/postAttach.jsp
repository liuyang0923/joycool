<%@ page contentType="application/vnd.wap.xhtml+xml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
response.setHeader("Cache-Control","no-cache");

JCRoomChatAction action = new JCRoomChatAction(request);
int roomId=action.getParameterInt("roomId");
String result = (String)request.getAttribute("result");
if(result == null) {
    action.post(request);
}
UserBean loginUser = action.getLoginUser();
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
if(returnUrl==null){
	returnUrl = BaseAction.INDEX_URL;
}
String backTo = returnUrl; //(String)request.getAttribute("backTo");
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>贴图</title>
</head>
<p align="left">
<%
//表单
if(result == null){
%>

<%
	String to = request.getParameter("to");
	String target = (String)request.getAttribute("target");
	//有接收者
	if("notNull".equals(target)){
	    UserBean toUser = (UserBean) request.getAttribute("toUser");
		if(toUser == null){
%>
对方已下线。<br/>
<%
		}
        else {
%><form name="postAttachForm" ENCTYPE="multipart/form-data" method="post" action="<%=response.encodeURL("InsertAttach.do?privateSubmit=1&amp;roomId="+roomId+"&amp;to="+toUser.getId()+"&amp;backTo="+URLEncoder.encode(backTo))%>">
接收对象:<a href="/user/ViewUserInfo.do?userId=<%=toUser.getId()%>"><%=toUser.getNickNameWml()%></a><br/>
留言内容:<br/>
<input type="text" name="content" maxlength="100" value="你好啊"/><br/>
图片文件：<br/><input type="file" name="file"/><br/>
<%--<input type="submit" name="submit" value="发表"/>--%><input type="submit" name="submit" value="密语"/><br/>
<a href="post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUser.getId() %>&amp;backTo=<%=URLEncoder.encode(backTo)%>">文字留言</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%
		}
%>
<%
    }
    //没有接收者
	else {
%>
接收对象：<br/>
<select name="to" title="选择接收对象" value="joycoolnulluser">
  <option value="joycoolnulluser">所有人</option>
</select><br/>
留言内容：<br/>
<input type="text" name="content" maxlength="100" value="你好啊"/><br/>
图片文件：<br/><input type="file" name="file"/><br/>
<input type="submit" name="submit" value="发表"/><input type="submit" name="privateSubmit" value="密语"/><br/>
<a href="post.jsp?roomId=<%=roomId%>&amp;backTo=<%=URLEncoder.encode(backTo)%>">文字留言</a><br/>
</form><%
	}

}
//留言结果
else {	
	String tip = (String)request.getAttribute("tip");
	//失败
	if("failure".equals(result)){
%>
<%=tip%><br/>
请返回发言！<br/>
<%
	} else {
%>
留言成功！<br/>
<%
    }
%>
<%	
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="<%=(backTo.replace("&", "&amp;"))%>">返回</a><br/>
<%
String chatRoomId = (String)session.getAttribute("chatroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/> 
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</html>