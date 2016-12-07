<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.io.*,net.joycool.wap.bean.friendadver.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.util.*"%><%
//登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//登录限制_end

response.setHeader("Cache-Control","no-cache");
int id = StringUtil.toInt(request.getParameter("id"));
int faId = StringUtil.toInt(request.getParameter("faId"));
IFriendAdverMessageService service=ServiceFactory.createFriendAdverMessageService();
FriendAdverMessageBean friendAdverM=service.getFriendAdverMessage("id = " + id);
LogUtil.logOperation("删除交友广告回贴: " + friendAdverM.getContent());
if(friendAdverM.getAttachment()!=null) 
{
	File file=new File(friendAdverM.getAttachmentURL());
	file.delete();
}
service.deleteFriendAdverMessage("id="+id);
//response.sendRedirect("friendAdverMessage.jsp?id="+faId);
BaseAction.sendRedirect("/jcadmin/friendadver/friendAdverMessage.jsp?id="+faId, response);
%>