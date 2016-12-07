<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.action.jcforum.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.FileUtil"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.jcforum.*"%><%
response.setHeader("Cache-Control","no-cache");
IJcForumService service = ServiceFactory.createJcForumService();
ForumAction action= new ForumAction(request);
ForumReplyBean forumContent=null;
//删除聊天记录
/*
if(request.getParameter("delete") != null){	
	String deleteId =(String)request.getParameter("delete");
	jcRoomContent=service.getContent("id="+deleteId);
	//lbj_log_oper_start
//	LogUtil.logOperation("聊天室删贴: " + action.getMessageDisplay(jcRoomContent, request, response));
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
	//response.sendRedirect(("forumPic.jsp?roomId="+roomId));
}*/
//fanys 2006-06-23 start,只删除图片,不删除原贴
if(request.getParameter("deleteImage")!=null){

	int deleteId =Integer.parseInt((String)request.getParameter("deleteImage"));
	forumContent=service.getForumReply("id="+deleteId);
	
	String fileName=forumContent.getAttach();
	if(fileName!=null&&fileName.length()!=0){
		String _fileName=FileUtil.getThumbnailName(fileName);
		String filePath = ForumAction.ATTACH_ROOT;
	//	LogUtil.logOperation("聊天室删除图片,原贴为: " 
	//			+ action.getMessageDisplay(jcRoomContent, request, response)
	//			+",图片位置为:"+filePath+"/"+fileName);
	//	System.out.println(filePath+"/"+fileName);
		File f1 = new File(filePath+"/"+fileName);
			if(f1.exists()){
		        f1.delete();
		    }
	//	System.out.println(filePath+"/"+_fileName);
		File f2 = new File(filePath+"/"+_fileName);
			if(f2.exists()){
		        f2.delete();
		    }
		ForumCacheUtil.updateForumReply("attach=''","id="+deleteId,deleteId);
		//response.sendRedirect(("forumPic.jsp?roomId="+roomId));
	}
}
//fanys 2006-06-23 end
//设置分页参数
PagingBean paging = new PagingBean(action,10000,20,"p");
String prefixUrl = "forumPic2.jsp";

// 取得要显示的消息列表
int start = paging.getStartIndex();
Vector ml = service.getForumReplyList(" attach!='' order by id desc limit "+ start + ","+paging.getCountPerPage());
int test=ml.size();
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<table align="center" border="1" width="100%">
<td>id</td>
<td>发贴人</td>
<td>附件</td>
<td>时间</td>
<td>帖子id</td>
<td>操作</td>
<%ForumReplyBean m = null;
for(int i = 0; i < ml.size(); i ++){
	m = (ForumReplyBean)ml.get(i);
%>
<tr>
<td><%=m.getId()%></td>
<td><%=m.getUserId()%></td>
<td><%if(m.getAttach().equals("")){%>无附件上传<%}else{%><img src="/rep/jcforum/<%=m.getAttach()%>" alt=""/><%}%></td>
<td><%=m.getCreateDatetime()%></td>
<td><a href="/jcadmin/user/forumr.jsp?id=<%=m.getContentId()%>"><%=m.getContentId()%></a></td>
<td>
<a href="forumPic2.jsp?deleteImage=<%=m.getId()%>&p=<%=paging.getCurrentPageIndex()%>">删图片</a>
</td>
</tr>
<%
}
%>
</table>
<p align="center">
<%=paging.shuzifenye(prefixUrl,false,"|",response)%><br/>
</p>
<!-- fanys 2006-06-28 start -->
<p align="center"><a href="../chatmanage.jsp">返回</a></p>
<!-- fanys 2006-06-28 end -->
</body>
</html>