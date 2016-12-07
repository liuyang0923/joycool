<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.bean.UserFriendBean" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="java.util.ArrayList" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
action.moreFunction(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/chat/pmSpace.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="更多互动功能" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转私聊空间)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
IUserService service = ServiceFactory.createUserService();
UserBean toUser=(UserBean)request.getAttribute("toUser");
ArrayList userFriends=UserInfoUtil.getUserFriends(loginUser.getId());
boolean isFriend=userFriends.contains(toUser.getId()+"");
int roomId=action.getParameterInt("roomId");
%>
<card title="更多互动功能">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(""+toUser.getId());
//liuyi 2006-10-30 交友系统 start
IUserService userService = ServiceFactory.createUserService();
UserFriendBean friendBean = userService.getUserFriend(loginUser.getId(), toUser.getId());
UserFriendBean friend2Bean = userService.getUserFriend(toUser.getId(),loginUser.getId());
if(toUser.getUs2()!=null){%><%=toUser.getUs2().getHatShow()%><%}%><%=((toUser.getNickName() == null || toUser.getNickName().equals("v") || toUser.getNickName().replace(" ", "").equals("")) ? toUser.getUserName() : StringUtil.toWml(toUser.getNickName()))%><% if(onlineUser!=null){%>(<%=LoadResource.getPostionById(onlineUser.getId() + "", onlineUser.getPositionId()).getPositionName()%>)<%}else{%>(离线)<%}%><br/>
<a href="/chat/pmSpace.jsp?toUserId=<%=toUser.getId()%>">与<%=toUser.getGender() == 1? "他" : "她"%>聊天</a>
<a href="../user/sendAction.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUser.getId()%>" title="进入">对<%=toUser.getGender() == 1? "他" : "她"%>动作</a><br/>
<a href="/user/sendMessage.jsp?toUserId=<%=toUser.getId()%>" title="进入">给<%=toUser.getGender() == 1? "他" : "她"%>写信</a>
<a href="postAttach.jsp?roomId=<%=roomId%>&amp;to=<%=toUser.getUserName()%>">贴图</a><br/>
<a href="../user/operUser.jsp?action=sendMoney&amp;toUserId=<%=toUser.getId()%>" title="进入">送<%=toUser.getGender() == 1? "他" : "她"%>乐币</a>
<a href="/user/userBagPost.jsp?roomId=<%=roomId%>&amp;userId=<%=toUser.getId()%>" title="进入">使用道具</a><br/>
＝＝＝＝＝＝＝＝＝<br/>
<%if(onlineUser!=null){%>
<a href="../user/operUser.jsp?action=inviteGame&amp;toUserId=<%=toUser.getId()%>" title="进入">邀请<%=toUser.getGender() == 1? "他" : "她"%>玩游戏</a><br/>
<%}%>
<a href="/user/ViewUserInfo.do?roomId=<%=roomId%>&amp;userId=<%=toUser.getId()%>">查<%=toUser.getGender() == 1? "他" : "她"%>社区信息</a><br/>
<%if(toUser.getFriend()==1){%>
<a href="/friend/friendInfo.jsp?userId=<%=toUser.getId()%>" title="进入">看<%=toUser.getGender() == 1? "他" : "她"%>交友资料</a><br/>

<%}%>
<%if(toUser.getHome()==1){%>
<a href="/home/home.jsp?userId=<%=toUser.getId()%>" title="进入">进入<%=toUser.getGender() == 1? "他" : "她"%>的家园</a><br/>
<%}%>
＝＝＝＝＝＝＝＝＝<br/>
<%if (!service.isUserBadGuy(loginUser.getId(), toUser.getId())) {%>
<a href="/user/OperBadGuy.do?add=1&amp;badGuyId=<%=toUser.getId()%>" title="进入">加黑名单</a>
<%}else{%>
<a href="/user/OperBadGuy.do?delete=1&amp;badGuyId=<%=toUser.getId()%>" title="进入">出黑名单</a>
<%}%>
<%if(isFriend && friendBean!=null && friendBean.getMark()==0){%>
<a href="/user/OperFriend.do?delete=1&amp;friendId=<%=toUser.getId()%>" title="进入">删除好友</a>
<%}else{%>
<a href="/user/OperFriend.do?add=1&amp;friendId=<%=toUser.getId()%>" title="进入">加为好友</a>
<%}%><br/>
<a href="/chat/compain.jsp?roomId=<%=roomId%>&amp;userId=<%=toUser.getId()%>">向管理员投诉<%=toUser.getGender() == 1? "他" : "她"%></a><br/>
<br/><a href="/chat/pmSpace.jsp?toUserId=<%=toUser.getId()%>">返回私聊空间</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>