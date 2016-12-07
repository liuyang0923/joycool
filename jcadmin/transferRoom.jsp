<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.chat.*,net.joycool.wap.bean.*"%><%
	IChatService chatService = ServiceFactory.createChatService();
	IUserService userService = ServiceFactory.createUserService();
	String nickname=request.getParameter("toUser");
	String roomId=request.getParameter("roomId");
	String notice=null;
	String success=null;
	int NUM_PER_PAGE;
	int totalCount;
	int totalPage;
	int pageIndex;
	Vector roomList=new Vector();
	if(nickname!=null&&!nickname.equals(""))
	{

		UserBean ub=userService.getUser("nickname='"+nickname.trim()+"'");
		if(ub==null)
		{
			notice="你输入的用户不存在！";
		}else{
			JCRoomBean jcRoom=chatService.getJCRoom("id="+roomId);
			chatService.updateJCRoom("creator_id="+ub.getId(),"id="+roomId);
			chatService.updateJCRoomManager("user_id="+ub.getId(),"room_id="+roomId);
			if(jcRoom.getGrantMode()==1)
			{
				chatService.updateJCRoomUser("user_id="+ub.getId()+",manager_id="+ub.getId(),"room_id="+roomId);
			}
			success="聊天室："+jcRoom.getName()+"已经成功转让！";	 
		}
	}
		 
	NUM_PER_PAGE = 10;
	totalCount =chatService.getJCRoomCount("1=1");
	totalPage=(totalCount+NUM_PER_PAGE-1)/NUM_PER_PAGE;
	pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
	if (pageIndex > totalPage-1) {
	 	pageIndex = totalPage -1;
	}
	 if (pageIndex < 0 ) {
	 	pageIndex = 0;
	}
				
	roomList=chatService.getJCRoomList(" 1=1 order by id limit "+pageIndex*NUM_PER_PAGE+","+NUM_PER_PAGE);
	

%>
<html>
<head>

</head>
<body>
<%if(notice!=null){%><%=notice%><br/><%}%>
<%if(success!=null){%><%=success%><br/><%}%>
<table name="table" border="1">
<caption>转让聊天室</caption>
<tr>
<th>id</th><th>名字</th><th>所有人</th><th>受让人</th><th>转让</th>
</tr>
<%
for(int i=0;i<roomList.size();i++)
{
	JCRoomBean jcRoom=(JCRoomBean)roomList.get(i);
	UserBean ub=UserInfoUtil.getUser(jcRoom.getCreatorId());
%>
<form name="form1" method="post" action="transferRoom.jsp">
<tr >
<td><%=jcRoom.getId()%></td><td><%=StringUtil.toWml(jcRoom.getName())%></td><td><%=StringUtil.toWml(ub.getNickName())%></td><td><input type="text" name="toUser" value="" /></td><td><input type="hidden" name="roomId" value="<%=jcRoom.getId()%>"/><input type="submit"  value="转让" ></td>
</tr>
</form>
<%}%>
</table>

<%=PageUtil.shuzifenye(totalPage, pageIndex, "transferRoom.jsp", false, "|", response)%><br/>
</body>
</html>
