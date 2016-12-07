<%--mcq_2006-09-11_查询一个用户的发言并操作发言--%>
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.infc.IChatService" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.chat.JCRoomContentBean" %><%@ page import="java.io.File" %><%@ page import="net.joycool.wap.util.RoomCacheUtil" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.IMessageService" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.MessageBean" %><%
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
IMessageService messageService =ServiceFactory.createMessageService();
List messageList=null;
String prefixUrl=null;
int totalCount=0;
int totalPageCount=0;
int pageIndex=0;
//通过userID查询用户聊天记录
String userId=null;
if(request.getParameter("userId")!=null){
userId=request.getParameter("userId");
int id=StringUtil.toInt(userId);
UserBean user=UserInfoUtil.getUser(id);
	if(user==null){
	
	}else{
		//设置分页参数
		int numberPage = 50;
		pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		prefixUrl = "searchUserMessage.jsp?userId="+userId;
		totalCount = SqlUtil.getIntResult("select (select count(*) from user_message where from_user_id = "+ id + " )+(select count(*) from user_message where  to_user_id = "+ id+ " ) c_id");
		totalPageCount = totalCount / numberPage;
		if (totalCount % numberPage != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		// 取得要显示的消息列表
		int start = pageIndex * numberPage;
		int end = numberPage;
		messageList=messageService.getMessageList("from_user_id = "+ id + " union select * from user_message where  to_user_id = "+ id+ " order by id desc limit "+start+","+end);
	}
}

%>
<html>
<head>
 <script language="javascript" >
function checkform(){
	if(document.searchUserMessage.userId.value == ''){
		alert("用户Id不能为空！");
		return false;
	}else if(isNaN(document.searchUserMessage.userId.value)){
		alert("用户Id必须为数字！");
		document.searchUserMessage.userId.value="";
		document.searchUserMessage.userId.focus();
		return false;
	}else{
		  return true;
		 }
}
</script>
</head>
<body>
<form name="searchUserMessage" action="searchUserMessage.jsp" method="post" onsubmit="return checkform()">
用户ID:<input name="userId" type="text"  size="38" >
<input type="submit" value="提交">
<input type="reset" value="重置">
</form>
<p align="center">
<%
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, " ", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
}
%>
</p>
<table width="100%" border="2">
<tr>
<td align="center" width="5%">序号</td>
<td align="center" width="5%">记录ID</td>
<td align="center" width="5%">发送者id</td>
<td align="center" width="10%">发送者昵称</td>
<td align="center" width="5%">接收者id</td>
<td align="center" width="10%">接收者昵称</td>
<td align="center" width="40%">信件内容</td>
<td align="center" width="5%">信件状态</td>
<td align="center" width="5%">发言时间</td>
</tr>
<%
MessageBean message=null;
if(messageList!=null){
for(int i=0;i<messageList.size();i++){
message=(MessageBean)messageList.get(i);%>
<tr>
<td><%=i+1%></td>
<td><%=message.getId()%></td>
<td><%=message.getFromUserId()%></td>
<td>
<%message.setFromUser(UserInfoUtil.getUser(message.getFromUserId()));
if(message.getFromUser()==null){%>昵称有误<%}else{%><%=message.getFromUser().getNickName()%><%}%>
</td>
<td><%=message.getToUserId()%></td>
<td>
<%message.setToUser(UserInfoUtil.getUser(message.getToUserId()));
if(message.getToUser()==null){%>昵称有误<%}else{%><%=message.getToUser().getNickName()%><%}%>
</td>
<td><%=message.getContent()%></td>
<td><%=message.getFlag()%></td>
<td><%=message.getSendDatetime()%></td>
</tr>
<%}}else{%>没有该用户信件记录<%}%>
</table>
<p align="center">
<%
fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, " ", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
}
%>
</p>

</body>
</html>