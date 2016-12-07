<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.INoticeService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");
IChatService chatService = ServiceFactory.createChatService();
IUserService userService = ServiceFactory.createUserService();
INoticeService noticeService = ServiceFactory.createNoticeService();
UserBean toUser = null;
UserBean user = null;
NoticeBean notice=null;
String buser=null;
//判断输入用户id添加黑名单用户
String userId=(String)request.getParameter("userId");
if(userId!=null){
	userId=userId.trim();
   if(!(userId.equals(""))){
	   toUser = UserInfoUtil.getUser(StringUtil.toInt(userId));
	   if(toUser!=null){
		   //lbj_log_oper_start
	       LogUtil.logOperation("聊天室踢进小黑屋: 用户名:" + toUser.getUserName() + "昵称:" + toUser.getNickName());
	       //lbj_log_oper_end
		   boolean flag=chatService.getForBID("user_id="+userId);
		   if(!flag){
			   chatService.addForBID(userId);
			   notice = new NoticeBean();
			   notice.setUserId(Integer.parseInt(userId));
			   notice.setTitle("系统信息提示");
			   notice.setContent("有用户投诉您有骚扰行为，所以对您施以薄惩……下次不可以了噢！");
			   notice.setType(NoticeBean.GENERAL_NOTICE);		
			   notice.setHideUrl("");
			   notice.setLink("");
			   noticeService.addNotice(notice);
		   }
	    }
	   //response.sendRedirect("forchatbid.jsp");
	   BaseAction.sendRedirect("/jcadmin/forchatbid.jsp", response);
	   return; 
	}
}
//判断输入用户name添加黑名单用户
String userName=(String)request.getParameter("userName");
if(userName!=null){
   userName=userName.trim();
   if(!(userName.equals(""))){ 
   toUser = userService.getUser("nickname = '" + userName + "'");
      if(toUser!=null){
		 //lbj_log_oper_start
	     LogUtil.logOperation("聊天室踢进小黑屋: 用户名:" + toUser.getUserName() + "昵称:" + toUser.getNickName());
	     //lbj_log_oper_end
         boolean flag=chatService.getForBID("user_id="+toUser.getId());
         if(!flag){
         chatService.addForBID(String.valueOf(toUser.getId()));
         notice = new NoticeBean();
		 notice.setUserId(toUser.getId());
		 notice.setTitle("有用户投诉您...");
		 notice.setContent("有用户投诉您有骚扰行为，所以对您施以薄惩，进聊天室的话将被踢进小黑屋一次（偷偷告诉你，可以马上出来的）。下次不可以了噢！");
		 notice.setType(NoticeBean.GENERAL_NOTICE);		
		 notice.setHideUrl("");
		 notice.setLink("");
		 noticeService.addNotice(notice);
         }
   }
   //response.sendRedirect("forchatbid.jsp");
   BaseAction.sendRedirect("/jcadmin/forchatbid.jsp", response);
   return;
	}
}
//判断删除黑名单用户
if(request.getParameter("delete") != null){
	String deleteId = (String)request.getParameter("delete");
	boolean flag=chatService.getForBID("user_id="+deleteId);
	if(flag){
	  chatService.deltetForBID("user_id="+deleteId);
	} 
	//response.sendRedirect("forchatbid.jsp");
	BaseAction.sendRedirect("/jcadmin/forchatbid.jsp", response);
	return;
}
%>
<html>
<head>
 <script language="javascript" >
function checkform(){
			if(document.bid.userId.value == ''){
				alert("用户ID不能为空！");
				return false;
			}else if(isNaN(document.bid.userId.value)){
			    document.bid.userId.value="";
				alert("请输入用户ID！");
				return false;
			}else{
				  return true;
				}
		}
function checkform1(){
			if(document.bidname.userName.value == ''){
				alert("用户名称不能为空！");
				return false;
			}else{
				  return true;
				}
		}
</script>
</head>
<body>
<p>添加用户到小黑屋列表</p>
<table width="100%" border="2">
<th>序号</th>
<th>用户ID</th>
<th>用户名称</th>
<th>用户昵称</th>
<th>操作</th>
<%
//显示封禁列表
Vector bidList = chatService.getForBID();
JCRoomForbidBean forbid = null;
if(bidList != null){
int a=bidList.size();
for(int i=0;i<bidList.size();i++){
      forbid=(JCRoomForbidBean)bidList.get(i);
      user=UserInfoUtil.getUser(forbid.getUserId());
      int b=forbid.getUserId();
%>
<tr>
<td width="5"><%=i+1%></td>
<td width="30%"><%=forbid.getUserId()%></td>
<td width="30%"><%=user.getUserName()%></td>
<td width="30%"><%=user.getNickName()%></td>
<td width="5%"><a href="forchatbid.jsp?delete=<%=forbid.getUserId()%>">删除</a></td>


</tr>
<%
	}
}
%>
</table>
<form name="bid" action="forchatbid.jsp" method="post" onsubmit="return checkform()" >
用户ID:<input name="userId" type="text"  size="38" >
<input type="submit" value="提交">
<input type="reset" value="重置">
 </form>
<form name="bidname" action="forchatbid.jsp" method="post" onsubmit="return checkform1()" >
用户昵称:<input name="userName" type="text"  size="38" >
<input type="submit" value="提交">
<input type="reset" value="重置">
 </form>
<body>
</html>