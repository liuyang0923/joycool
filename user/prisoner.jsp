<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
request.setAttribute("gender","1");
JCRoomChatAction action = new JCRoomChatAction(request);
action.getOnlineList_1(request,response);
UserBean user=null;
Vector vecOnline=(Vector)request.getAttribute("vecOnline");
int onlineCount=Integer.parseInt((String)request.getAttribute("onlineCount"));
String pagination=(String)request.getAttribute("pagination");
int roomId=0;
if(request.getParameter("roomId")!=null)
	roomId=Integer.parseInt(request.getParameter("roomId"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷拘留所">
<p align="left">
<%=BaseAction.getTop(request, response)%>

拘留所内现有人员<%=onlineCount%>人<br/>
<%
//liuyi 2006-11-01 结义显示 start 
UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
IUserService userService = ServiceFactory.createUserService();
//liuyi 2006-11-01 结义显示 end
for(int i=0;i<vecOnline.size();i++){
	user=(UserBean)vecOnline.get(i);
    //liuyi 2006-11-01 结义显示 start 
	UserFriendBean friend = userService.getUserFriend(loginUser.getId(), user.getId());
%>
  <%=i+1%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="ViewUserInfo.do?userId=<%=user.getId()%>"><%=((user.getNickName() == null || user.getNickName().equals("v") || user.getNickName().replace(" ", "").equals("")) ? user.getUserName() : StringUtil.toWml(user.getNickName()))%><% if(friend!=null){ if(friend.getMark()==1){%>(义)<%} } %></a>(<%=(user.getGender() == 0 ? "女":"男")%>|<%=user.getAge()%>岁<%if(user.getCityname() != null){%>|<%=user.getCityname()%><%}%>)<br/>
<%}
    //liuyi 2006-11-01 结义显示 end
%>
<%if(!pagination.equals("")){%><%=pagination%><br/><%}%>
<a href="http://wap.joycool.net/user/onlineManager.jsp?forumId=355">返回公安局</a><br/>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>