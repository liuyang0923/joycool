<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.FileUtil"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.chat.JCRoomContentBean"%><%@ page import="net.joycool.wap.bean.chat.JCRoomBean"%><%
response.setHeader("Cache-Control","no-cache");
int roomId=0;//房间编号
if(request.getParameter("roomId")!=null){
	roomId=Integer.parseInt(request.getParameter("roomId"));
}
//fanys 2006-06-28 end
IChatService chatService = ServiceFactory.createChatService();
JCRoomChatAction action= new JCRoomChatAction(request);
JCRoomContentBean jcRoomContent=null;
//删除聊天记录
if(request.getParameter("delete") != null){	
	String deleteId =(String)request.getParameter("delete");
	jcRoomContent=chatService.getContent("id="+deleteId);
	//lbj_log_oper_start
//	LogUtil.logOperation("聊天室删贴: " + action.getMessageDisplay(jcRoomContent, request, response));
	//lbj_log_oper_end
	if(!jcRoomContent.getAttach().equals("")){
		String fileName=jcRoomContent.getAttach();
		String filePath = JCRoomContentBean.ATTACH_ROOT;
		File f1 = new File(filePath+"/"+fileName);
		if(f1.exists()){
	        f1.delete();
	    }
	}
	if(jcRoomContent.getRoomId()>=0){
	RoomCacheUtil.deleteRoomContent("id="+deleteId,Integer.parseInt(deleteId),jcRoomContent.getRoomId());
	}
	if(jcRoomContent.getSecRoomId()>=0){
	RoomCacheUtil.deleteRoomContent("id="+deleteId,Integer.parseInt(deleteId),jcRoomContent.getSecRoomId());
	}
	//response.sendRedirect(("chatPic.jsp?roomId="+roomId));
}
//fanys 2006-06-23 start,只删除图片,不删除原贴
if(request.getParameter("deleteImage")!=null){

	String deleteId =(String)request.getParameter("deleteImage");
	jcRoomContent=chatService.getContent("id="+deleteId);
	
	String fileName=jcRoomContent.getAttach();
	if(fileName!=null&&fileName.length()!=0){
		String _fileName=FileUtil.getThumbnailName(fileName);
		String filePath = JCRoomContentBean.ATTACH_ROOT;
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
		RoomCacheUtil.updateRoomContent("attach=''","id="+deleteId,Integer.parseInt(deleteId));
		//response.sendRedirect(("chatPic.jsp?roomId="+roomId));
	}
}
//fanys 2006-06-23 end
//设置分页参数
PagingBean paging = new PagingBean(action,10000,20,"p");
String prefixUrl = "chatPic.jsp?"+"roomId="+roomId;

// 取得要显示的消息列表
int start = paging.getStartIndex();
int end = paging.getEndIndex();
Vector ml = chatService.getMessageList(" attach!='' order by id desc limit "+ start + ","+paging.getCountPerPage());
int test=ml.size();
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<table align="center" border="1" width="100%">
<td>序号</td>
<td>发送人</td>
<td>接收人</td>
<td>内容</td>
<td>附件</td>
<td>发送时间</td>
<td>方式</td>
<td>操作</td>
<%JCRoomContentBean m = null;
JCRoomBean roomBean=null;
for(int i = 0; i < ml.size(); i ++){
	m = (JCRoomContentBean)ml.get(i);
%>
<tr>
<td><%=m.getId()%></td>
<td><%=m.getFromNickName()%><br/>(<%=m.getFromId()%>)</td>
<td><%if(m.getToNickName().equals("")){%>无接收对象<%}else{%><%=m.getToNickName()%><%}%><br/>(<%=m.getToId()%>)</td>
<td><%=m.getContent()%></td>
<td><%if(m.getAttach().equals("")){%>无附件上传<%}else{%><img src="<%=JCRoomContentBean.ATTACH_URL_ROOT + m.getAttach()%>" alt="图片"/><%}%></td>
<td width="80"><%=m.getSendDateTime()%></td>
<td>
<%if(m.getIsPrivate()==0){%><%="公聊"%><%}else{%><%="私聊"%><%}%>
</td>
<td>
<a href="chatPic.jsp?deleteImage=<%=m.getId()%>&p=<%=paging.getCurrentPageIndex()%>&roomId=<%=roomId%>">删图片</a>
</td>
</tr>
<%
}
%>
</table>
<p align="center">
<%=paging.shuzifenye(prefixUrl,true,"|",response)%><br/>
</p>
<!-- fanys 2006-06-28 start -->
<p align="center"><a href="../chatmanage.jsp">返回</a></p>
<!-- fanys 2006-06-28 end -->
</body>
</html>