<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.framework.*"%><% 
response.setHeader("Cache-Control","no-cache");
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
IChatService chatService = ServiceFactory.createChatService();
IUserService userService = ServiceFactory.createUserService();
Vector roomList=chatService.getRoomApplyList(" 1=1 order by mark asc,vote_count desc");
String delete=request.getParameter("delete");
String pass=request.getParameter("pass");
if(delete != null){
    
    //删除时查询申请记录
    RoomApplyBean roomApply=chatService.getRoomApply("id="+delete);
    //获房间信息
	JCRoomBean room=chatService.getJCRoom("name='"+roomApply.getRoomName()+"' and creator_id="+roomApply.getUserId());
	if(room!=null){
	    RoomUtil.addChangedRoom(room.getId());
	    //删除对应房间
		chatService.deleteJCRoom("id="+room.getId());
		//删除对应的房间聊天数量记录
		chatService.deleteJCRoomContentCount("room_id="+room.getId());
		//删除对应的房间聊天记录
		//chatService.deleteContent("room_id="+room.getId());
		//删除对应房间的房间管理员
		chatService.deleteJCRoomManager("room_id="+room.getId());
		//删除房间内所有用户
		chatService.deleteJCRoomUser("room_id="+room.getId());
	}
	chatService.delRoomApply("id="+delete);
	chatService.delRoomVote("apply_id="+delete);
	//response.sendRedirect("zjChatManager.jsp");
	BaseAction.sendRedirect("/jcadmin/zjChatManager.jsp", response);
	return;
}
if(pass!= null){
    //审批通过后查询申请记录
    RoomApplyBean roomApply=chatService.getRoomApply("id="+pass);
    chatService.updateRoomApply("mark=1","id="+roomApply.getId());
    //初始化
    JCRoomBean room=new JCRoomBean();
    //设置room对应的值
    room.setName(roomApply.getRoomName());
    room.setCreatorId(roomApply.getUserId());
    room.setMaxOnlineCount(200);
    room.setPayWay(0);
    room.setThumbnail("");
    room.setGrantMode(0);
    room.setStatus(1);
    room.setCurrentOnlineCount(0);
    //添加房间
	chatService.addJCRoom(room);
	//获取添加的房间信息
	room=chatService.getJCRoom("name='"+roomApply.getRoomName()+"' and creator_id="+roomApply.getUserId());
	//初始化JCRoomContentCountBean
	JCRoomContentCountBean roomContentCount =new JCRoomContentCountBean();
	roomContentCount.setRoomId(room.getId());
	//添加房间记录到JCRoomContentCount
	chatService.addJCRoomContentCount(roomContentCount);
	//初始化管理员表
	RoomManagerBean manager=new RoomManagerBean();
	manager.setRoomId(room.getId());
	manager.setUserId(room.getCreatorId());
	manager.setMark(1);
	//添加房间管理员
	chatService.addJCRoomManager(manager);
	//初始化房间用户表
	RoomUserBean roomUser=new RoomUserBean();
	roomUser.setRoomId(room.getId());
	roomUser.setUserId(room.getCreatorId());
	roomUser.setManagerId(room.getCreatorId());
	roomUser.setStatus(1);
	//添加创建房间的人到房间表
	chatService.addJCRoomUser(roomUser);
	//给房间申请人发一个系统通知
	NoticeBean notice =new NoticeBean();
    notice.setUserId(roomApply.getUserId());
    notice.setTitle("恭喜您，"+room.getName()+"聊天室已审批通过！");
    notice.setType(NoticeBean.GENERAL_NOTICE);
    notice.setHideUrl("");
    notice.setLink("/chat/hall.jsp?roomId="+ room.getId());
    NoticeUtil.getNoticeService().addNotice(notice);	   
	//取得所有支持这个房间用户的列表
	Vector userList=chatService.getRoomVoteList("apply_id="+pass);
	RoomVoteBean roomVote=null;
	for(int i=0;i<userList.size();i++){
	    roomVote=(RoomVoteBean)userList.get(i);
        // 加入消息系统
        notice=new NoticeBean();
        notice.setUserId(roomVote.getUserId());
        notice.setTitle("恭喜您，"+room.getName()+"聊天室已审批通过！");
        notice.setType(NoticeBean.GENERAL_NOTICE);
        notice.setHideUrl("");
        notice.setLink("/chat/hall.jsp?roomId="+ room.getId());
        NoticeUtil.getNoticeService().addNotice(notice);	    
    }
	//response.sendRedirect("zjChatManager.jsp");
	BaseAction.sendRedirect("/jcadmin/zjChatManager.jsp", response);
	return;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>自建聊天室审批</title>
<script language="javascript">
  function operate1()
     {
     if (confirm('你确定要删除信息吗？')) {
       return true;
       } else {
        return false;
       }
     }
  function operate2()
     {
	 if (confirm('你确定要审批通过吗？')) {
	   return true;
	   } else {
	    return false;
	   }
     }
</script>
</head>
<body>
<table border="1">
<th>聊天室名称</th><th>申请人</th><th>支持数量</th><th>标志</th><th>操作</th><th>操作</th>
<% 
RoomApplyBean roomApply=null;
UserBean user=null;
for(int i=0;i<roomList.size();i++){
  roomApply=(RoomApplyBean)roomList.get(i);
  user=UserInfoUtil.getUser(roomApply.getUserId());
  if(user==null){
    continue;
  }
%>
  <tr>
      <td><%=roomApply.getRoomName()%></td>
      <td><%=user.getNickName()%></td>
      <td><%=roomApply.getVoteCount()%></td>
      <td><%if(roomApply.getMark()==0){%>未处理<%}else{%>已审批<%}%></td>
      <td><a href="zjChatManager.jsp?delete=<%=roomApply.getId()%>" onClick="return operate1()">删除</a></td>
      <td><%if(roomApply.getMark()==0){%><a href="zjChatManager.jsp?pass=<%=roomApply.getId()%>" onClick="return operate2()">审批通过</a><%}else{%>审批通过<%}%></td>
  </tr>
<%}%>
</table>
</body>
</html>