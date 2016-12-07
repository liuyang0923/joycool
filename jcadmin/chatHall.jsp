<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.FileUtil"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.chat.JCRoomContentBean"%><%@ page import="net.joycool.wap.bean.chat.JCRoomBean"%><%
//fanys 2006-06-28 start
int roomId=0;//房间编号
if(request.getParameter("roomId")!=null){
	roomId=Integer.parseInt(request.getParameter("roomId"));
}
//fanys 2006-06-28 end
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
response.setHeader("Cache-Control","no-cache");
IChatService chatService = ServiceFactory.createChatService();
JCRoomChatAction action= new JCRoomChatAction();
JCRoomContentBean jcRoomContent=null;
//删除聊天记录
if(request.getParameter("delete") != null){	
	String deleteId =(String)request.getParameter("delete");
	jcRoomContent=chatService.getContent("id="+deleteId);
	//lbj_log_oper_start
	LogUtil.logOperation("聊天室删贴: " + action.getMessageDisplay(jcRoomContent, request, response));
	//lbj_log_oper_end
	if(!jcRoomContent.getAttach().equals("")){
	String fileName=jcRoomContent.getAttach();
	String _fileName=FileUtil.getThumbnailName(fileName);
	String filePath = JCRoomContentBean.ATTACH_ROOT;
	File f1 = new File(filePath+"/"+fileName);
		if(f1.exists()){
	        f1.delete();
	    }
	File f2 = new File(filePath+"/"+_fileName);
		if(f2.exists()){
	        f2.delete();
	    }
	}
	if(jcRoomContent.getRoomId()>=0){
	RoomCacheUtil.deleteRoomContent("id="+deleteId,Integer.parseInt(deleteId),jcRoomContent.getRoomId());
	}
	if(jcRoomContent.getSecRoomId()>=0){
	RoomCacheUtil.deleteRoomContent("id="+deleteId,Integer.parseInt(deleteId),jcRoomContent.getSecRoomId());
	}
	//response.sendRedirect(("chatHall.jsp?roomId="+roomId));
	BaseAction.sendRedirect("/jcadmin/chatHall.jsp?roomId="+roomId, response);
	return;
}
//fanys 2006-06-23 start,只删除图片,不删除原贴
if(request.getParameter("deleteImage")!=null){

	String deleteId =(String)request.getParameter("deleteImage");
	jcRoomContent=chatService.getContent("id="+deleteId);
	
	String fileName=jcRoomContent.getAttach();
	String _fileName=FileUtil.getThumbnailName(fileName);
	String filePath = JCRoomContentBean.ATTACH_ROOT;
	LogUtil.logOperation("聊天室删除图片,原贴为: " 
			+ action.getMessageDisplay(jcRoomContent, request, response)
			+",图片位置为:"+filePath+"/"+fileName);
	System.out.println(filePath+"/"+fileName);
	File f1 = new File(filePath+"/"+fileName);
		if(f1.exists()){
	        f1.delete();
	    }
	System.out.println(filePath+"/"+_fileName);
	File f2 = new File(filePath+"/"+_fileName);
		if(f2.exists()){
	        f2.delete();
	    }
	RoomCacheUtil.updateRoomContent("attach=''","id="+deleteId,Integer.parseInt(deleteId));
	//response.sendRedirect(("chatHall.jsp?roomId="+roomId));
	BaseAction.sendRedirect("/jcadmin/chatHall.jsp?roomId="+roomId, response);
	return;
}
//fanys 2006-06-23 end
//设置分页参数
int numberPage = 10;
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if (pageIndex == -1) {
	pageIndex = 0;
}
String backTo = request.getParameter("backTo");
if (backTo == null) {
	backTo = "http://wap.joycool.net";
}
String prefixUrl = "chatHall.jsp?"+"roomId="+roomId;
int totalCount = chatService.getMessageCount(" room_id="+roomId);

int totalPageCount = totalCount / numberPage;
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
Vector ml = chatService.getMessageList(" (room_id=" + roomId + " or sec_room_id=" + roomId + ") order by id desc limit "+ start + ", " + end);
int test=ml.size();
%>
<html>
<body>
<table align="center" border="1">
<th>序号</th>
<th>发送人ID</th>
<th>接收人ID</th>
<th>发送人昵称</th>
<th>接收人昵称</th>
<th>内容</th>
<th>附件</th>
<th>发送时间</th>
<th>聊天方式</th>
<th>操作</th>
<th>操作</th>
<%JCRoomContentBean m = null;
JCRoomBean roomBean=null;
for(int i = 0; i < ml.size(); i ++){
	m = (JCRoomContentBean)ml.get(i);
	roomBean=chatService.getRoomName("id="+m.getRoomId());
%>
<tr>
<td><%=m.getId()%></td>
<td><%=m.getFromId()%></td>
<td><%=m.getToId()%></td>
<td><%=m.getFromNickName()%></td>
<td><%if(m.getToNickName().equals("")){%>无接收对象<%}else{%><%=m.getToNickName()%><%}%></td>
<td><%=m.getContent()%></td>
<td><%if(m.getAttach().equals("")){%>无附件上传<%}else{%><img src="<%=JCRoomContentBean.ATTACH_URL_ROOT + m.getAttach()%>" alt="图片"/><%}%></td>
<td><%=m.getSendDateTime()%></td>
<td>
<%if(m.getIsPrivate()==0){%><%="公聊"%><%}else{%><%="私聊"%><%}%>
</td>
<%--
<td>
<%if(m.getRoomId()==0){%><%="聊天大厅"%><%}else{%><%="小黑屋"%><%}%>
<%if(roomBean!=null){%><%=roomBean.getName()%><%}%>
</td>
--%>
<td>
<a href="updateRoomContent.jsp?update=<%=m.getId()%>&roomId=<%=roomId%>">修改</a>
</td>
<td>
<a href="chatHall.jsp?delete=<%=m.getId()%>&roomId=<%=roomId%>">删除</a>
<%if(!m.getAttach().equals("")){ %>
<a href="chatHall.jsp?deleteImage=<%=m.getId()%>&roomId=<%=roomId%>">删除图片</a>
<%} %>
</td>
</tr>
<%
}
%>
</table>
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
<!-- fanys 2006-06-28 start -->
<p align="center"><a href="roomList.jsp">返回</a></p>
<!-- fanys 2006-06-28 end -->
</body>
</html>