<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.framework.*"%><%@ page import="java.io.*,java.util.*,net.joycool.wap.bean.friendadver.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.util.*"%><%
//登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//登录限制_end

response.setHeader("Cache-Control","no-cache");
if(request.getParameter("deleteImage")==null){//删除记录
int id = Integer.parseInt(request.getParameter("id"));
IFriendAdverService service = ServiceFactory.createFriendAdverService();
FriendAdverBean friendAdver=service.getFriendAdver("id = " + id);

LogUtil.logOperation("交友中心删贴: " + friendAdver.getTitle());

service.deleteFriendAdver("id = " + id);
OsCacheUtil.flushGroup(OsCacheUtil.FRIEND_ADV_LIST_GROUP);
OsCacheUtil.flushGroup(OsCacheUtil.FRIEND_ADVER_CACHE_GROUP);
IFriendAdverMessageService fams=ServiceFactory.createFriendAdverMessageService();
Vector friendAdverMessageList=fams.getFriendAdverMessageList("friend_adver_id="+id);
for(int i=0;i<friendAdverMessageList.size();i++)
{
	FriendAdverMessageBean fab=(FriendAdverMessageBean)friendAdverMessageList.get(i);
	File file=new File(fab.getAttachmentURL());
	if(fab.getAttachment()!=null) file.delete();
}
fams.deleteFriendAdverMessage("friend_adver_id="+id);
//response.sendRedirect("friendAdverIndex.jsp");
BaseAction.sendRedirect("/jcadmin/friendadver/friendAdverIndex.jsp", response);
return;
}else{//删除图片
	//fanys 2006-06-23 start
	int id = Integer.parseInt(request.getParameter("id"));
	IFriendAdverService service = ServiceFactory.createFriendAdverService();
	FriendAdverBean friendAdver=service.getFriendAdver("id = " + id);
	
	LogUtil.logOperation("交友中心删除图片，原贴: " + friendAdver.getTitle()+"  图片为："+friendAdver.getAttachmentURL());

	service.updateFriendAdver(" attachment='' "," id = " + id);
	File file=new File(friendAdver.getAttachmentURL());
	if(friendAdver.getAttachment()!=null) 
		file.delete();
	//response.sendRedirect("friendAdverIndex.jsp");
	BaseAction.sendRedirect("/jcadmin/friendadver/friendAdverIndex.jsp", response);
	//fanys 2006-06-23 end
	}
%>