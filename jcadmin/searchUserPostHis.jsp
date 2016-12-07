<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.infc.IChatService" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.chat.JCRoomContentBean" %><%@ page import="java.io.File" %><%@ page import="net.joycool.wap.util.RoomCacheUtil" %><%@ page import="net.joycool.wap.util.FileUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
IUserService userService =ServiceFactory.createUserService();
IChatService chatService =ServiceFactory.createChatService();
Vector contentList=null;
String prefixUrl=null;
int totalCount=0;
int totalPageCount=0;
int pageIndex=0;

//通过userID查询用户聊天记录
String userId=null;
int id = 0;
if(request.getParameter("userId")!=null){
userId=request.getParameter("userId");
id=StringUtil.toInt(userId);
UserBean user=UserInfoUtil.getUser(id);
if(user==null){}else{
//设置分页参数
int numberPage = 50;
pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if (pageIndex == -1) {
	pageIndex = 0;
}
prefixUrl = "searchUserPostHis.jsp?userId="+userId;
//totalCount = chatService.getMessageCount0("select count(id) c_id from mcoolhis.jc_room_content_his where (from_id = "+ id + " or to_id = "
//								+ id+ ") order by id desc");
//totalPageCount = totalCount / numberPage;
totalPageCount = pageIndex+1;
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
contentList=chatService.getMessageList0("select * from mcoolhis.jc_room_content_his where from_id = "+ id + " union select * from mcoolhis.jc_room_content_his where  to_id = "
								+ id+ " order by id desc limit "+start+","+end);
		if(contentList.size()==numberPage)
			totalPageCount++;
}}

%>
<html>
<head>
 <script language="javascript" >
function checkform(){
			if(document.searchUserPost.userId.value == ''){
				alert("用户Id不能为空！");
				return false;
			}else if(isNaN(document.searchUserPost.userId.value)){
				alert("用户Id必须为数字！");
				document.searchUserPost.userId.value="";
				document.searchUserPost.userId.focus();
				return false;
			}else{
				  return true;
				 }
		}
  function operate()
     {
     if (confirm('你确定要删除信息吗？')) {
       return true;
       } else {
        return false;
       }
      }
</script>
</head>
<body>
<form name="searchUserPost" action="searchUserPost.jsp" method="post" onsubmit="return checkform()">
用户ID:<input name="userId" type="text"  size="38" value="<%if(id>0)out.print(id);%>">
<input type="submit" value="聊天记录查询"><%if(id>0){%>-----<a href="user/queryUserInfo.jsp?id=<%=id%>">查询该用户</a><%}%>
</form>
<form name="searchUserPost" action="searchUserPostHis.jsp" method="post" onsubmit="return checkform()">
用户ID:<input name="userId" type="text"  size="38"  value="<%if(id>0)out.print(id);%>">
<input type="submit" value="历史聊天记录查询">
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
<td align="center" width="5%">接收者id</td>
<td align="center" width="10%">发送者昵称</td>
<td align="center" width="10%">接收者昵称</td>
<td align="center" width="20%">聊天内容</td>
<td align="center" width="10%">附件</td>
<td align="center" width="10%">发言时间</td>
<td align="center" width="5%">操作</td>
</tr>
<%
JCRoomContentBean roomContent=null;
if(contentList!=null){
for(int i=0;i<contentList.size();i++){
roomContent=(JCRoomContentBean)contentList.get(i);%>
<tr>
<td><%=i+1%></td>
<td><%=roomContent.getId()%></td>
<td><%=roomContent.getFromId()%></td>
<td><%=roomContent.getToId()%></td>
<td><%=roomContent.getFromNickName()%></td>
<%if(roomContent.getToNickName()==null || roomContent.getToNickName().equals("")){%>
<td>无接受对象</td>
<%}else{%><td><%=roomContent.getToNickName()%></td><%}%>
<td><%=roomContent.getContent()%></td>
<%if(roomContent.getAttach()==null || roomContent.getAttach().equals("")){%>
<td>无附件上传</td>
<%}else{%><td><img src="<%=JCRoomContentBean.ATTACH_URL_ROOT + roomContent.getAttach()%>" alt="图片"/></td><%}%>
<td><%=roomContent.getSendDateTime()%></td>
<td></td>
</tr>
<%}}else{%>没有该用户聊天记录<%}%>
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