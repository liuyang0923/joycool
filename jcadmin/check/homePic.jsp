<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.action.home.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.FileUtil"%><%@ page import="net.joycool.wap.service.infc.IHomeService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.home.*"%><%
response.setHeader("Cache-Control","no-cache");
//fanys 2006-06-28 end
IHomeService service = ServiceFactory.createHomeService();
HomeAction action= new HomeAction(request);
HomePhotoBean homePhoto=null;
//删除聊天记录
/*
if(request.getParameter("delete") != null){	
	String deleteId =(String)request.getParameter("delete");
	homePhoto=service.getContent("id="+deleteId);
	//lbj_log_oper_start
//	LogUtil.logOperation("聊天室删贴: " + action.getMessageDisplay(homePhoto, request, response));
	//lbj_log_oper_end
	if(!homePhoto.getAttach().equals("")){
	String fileName=homePhoto.getAttach();
	String _fileName=FileUtil.getThumbnailName(fileName);
	String filePath = "/usr/local/joycool-rep/home/myalbum";
	File f1 = new File(filePath+"/"+fileName);
		if(f1.exists()){
	        f1.delete();
	    }
	File f2 = new File(filePath+"/"+_fileName);
		if(f2.exists()){
	        f2.delete();
	    }
	}
	if(homePhoto.getRoomId()>=0){
	RoomCacheUtil.deleteRoomContent("id="+deleteId,Integer.parseInt(deleteId),homePhoto.getRoomId());
	}
	if(homePhoto.getSecRoomId()>=0){
	RoomCacheUtil.deleteRoomContent("id="+deleteId,Integer.parseInt(deleteId),homePhoto.getSecRoomId());
	}
	//response.sendRedirect(("homePic.jsp?roomId="+roomId));
}*/
//fanys 2006-06-23 start,删除照片
if(request.getParameter("deleteImage")!=null){

	String deleteId =(String)request.getParameter("deleteImage");
	homePhoto=service.getHomePhoto("id="+deleteId);
	
	String fileName=homePhoto.getAttach();
	String filePath = Constants.MYALBUM_FILE_PATH;
//	LogUtil.logOperation("聊天室删除图片,原贴为: " 
//			+ action.getMessageDisplay(homePhoto, request, response)
//			+",图片位置为:"+filePath+"/"+fileName);
//	System.out.println(filePath+"/"+fileName);
	File f1 = new File(filePath+"/"+fileName);
		if(f1.exists()&&filePath.length()>5){
	        f1.delete();
	    }
//	System.out.println(filePath+"/"+_fileName);
	HomeCacheUtil.deleteHomePhoneCache("id="+deleteId,homePhoto.getUserId());
	service.updateHomeUser("photo_count=photo_count-1","user_id="+homePhoto.getUserId()+" and photo_count>0");
	//response.sendRedirect(("homePic.jsp?roomId="+roomId));
}
//fanys 2006-06-23 end
//设置分页参数
PagingBean paging = new PagingBean(action,10000,20,"p");
String prefixUrl = "homePic.jsp";

// 取得要显示的消息列表
int start = paging.getStartIndex();
int end = paging.getEndIndex();
Vector ml = service.getHomePhotoList(" 1 order by id desc limit "+ start + ",20");
int test=ml.size();
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<table align="center" border="1" width="100%">
<td>序号</td>
<td>用户</td>
<td>标题</td>
<td>附件</td>
<td>发送时间</td>
<td>操作</td>
<%HomePhotoBean m = null;
for(int i = 0; i < ml.size(); i ++){
	m = (HomePhotoBean)ml.get(i);
%>
<tr>
<td><%=m.getId()%></td>
<td><%=m.getUserId()%></td>
<td><%=m.getTitle()%></td>
<td><%if(m.getAttach().equals("")){%>无附件上传<%}else{%><img src="/rep<%=m.getAttach()%>" alt=""/><%}%></td>
<td width="80"><%=m.getCreateDatetime()%></td>
<td>

<a href="homePic.jsp?deleteImage=<%=m.getId()%>&p=<%=paging.getCurrentPageIndex()%>">删图片</a>

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