<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
int roomId=StringUtil.toInt(request.getParameter("roomId"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="在线帅哥美女">
<p align="center">在线帅哥美女</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%      
if(roomId<0){
roomId=0;
}    
IUserService userService=ServiceFactory.createUserService();
IChatService chatService=ServiceFactory.createChatService();
int ONLINE_NUMBER_PER_PAGE = 10;
int totalCount = chatService.getOnlineCount("room_id="+roomId);
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if(pageIndex < 0){
	pageIndex =0;
}
 //取得总数        
int totalPageCount = totalCount / ONLINE_NUMBER_PER_PAGE;
if (totalCount % ONLINE_NUMBER_PER_PAGE != 0) {
    totalPageCount++;
}
if (pageIndex > totalPageCount - 1) {
    pageIndex = totalPageCount - 1;
}
if (pageIndex < 0) {
    pageIndex = 0;
}
UserBean user = null;
JCRoomOnlineBean online=null;
int start = pageIndex * ONLINE_NUMBER_PER_PAGE;
int end = ONLINE_NUMBER_PER_PAGE;	
Vector onlineList = chatService.getOnlineList("room_id="+roomId+" limit "+ start + ", " + end);
for(int i = 0; i < onlineList.size(); i ++){
	online = (JCRoomOnlineBean) onlineList.get(i);
//	user=userService.getUser("id="+online.getUserId());
	//zhul 2006-10-12_优化用户信息查询
	user = UserInfoUtil.getUser(online.getUserId());	
	if(user.getId()==loginUser.getId()){
		continue;
	}
%>
<%=i + 1%>.<a href="<%=("post.jsp?roomId="+roomId+"&amp;toUserId=" + user.getId()+ "&amp;toUserId="+user.getId())%>">
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%>
<%=((user.getNickName() == null || user.getNickName().equals("v") || user.getNickName().replace(" ", "").equals("")) ? user.getUserName() : StringUtil.toWml(user.getNickName()))%></a>(<%=(user.getGender() == 0 ? "女":"男")%>|<%=user.getAge()%>岁<%if(user.getCityname() != null){%>|<%=user.getCityname()%><%}%>)<br/>
<%
}
String prefixUrl = "onlineList.jsp?roomId="+roomId;
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
%>
<%if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<br/>
<a href="hall.jsp?roomId=<%=roomId%>">返回聊天大厅</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>