<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.service.factory.*,java.util.Date,net.joycool.wap.util.DateUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.friend.FriendBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.HashMap" %><%@ page import="net.joycool.wap.bean.RankBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.friendInfo(request);
String result=(String) request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("failure".equals(result)){
%>
<card title="真实资料">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/friend/friendCenter.jsp">交友中心</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else{
FriendBean friend=(FriendBean)request.getAttribute("friend");
UserBean user = UserInfoUtil.getUser(friend.getUserId());
boolean notSelf = (loginUser == null || loginUser.getId() != user.getId());
UserStatusBean userStatus = UserInfoUtil.getUserStatus(friend.getUserId());
user.setUs(userStatus);
%>
<card title="真实资料">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(request.getAttribute("tip")!=null){%>
<%=request.getAttribute("tip")%><br/>
<%}%>
<%if(notSelf){%>
【<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>】<br/>
<%}else{%>
<a href="/friend/editFriend.jsp">修改个人交友信息</a><br/>
您自己发布的交友信息<br/>
<%}%>
<%if(friend.getAttach()!=null && !friend.getAttach().equals("")){%>
<%//=("/img/friend/attach/")%>
<img src="<%=Constants.IMG_ROOT_URL%>/friend/attach/<%=friend.getAttach()%>" alt="loading..."/>
<%if(friend.getUserId()!=loginUser.getId()  && session.getAttribute("photoVote")==null){%>
<anchor title="确定">投<%=user.getGender() == 1? "他" : "她"%>一票
<go href="friendVote.jsp" method="post">
<postfield name="userId" value="<%=friend.getUserId()%>"/>
</go>
</anchor><%}%><br/>
真实姓名:<%=StringUtil.toWml(friend.getName())%> <br/>
<%if(friend.getUserId()==loginUser.getId()){%>
<a href="/friend/editPhotoTemp.jsp">修改个人照片</a>(支持WAP2.0的手机才可以上传图片。)<br/>
<%}}else{
if(friend.getUserId()==loginUser.getId()){%>
<a href="/friend/addPhoto.jsp">上传个人照片</a>(支持WAP2.0的手机才可以上传图片。)<br/>
<a href="/friend/cartoon.jsp">选择一个卡通头像代替照片</a><br/>
<%}}%>
<%String gender = friend.getGender()==0?"女":"男";%>
性别:<%=gender%><br/>
地区:<%=StringUtil.toWml(friend.getCity())%> <br/>
年龄:<%=friend.getAge()%>  <br/>
星座:<%=(String)LoadResource.getConstellation().get(friend.getConstellation()+"")%><br/>
<%--//liuyi 2007-01-22 生日信息 start --%>
<%
int openMark = user.getBirthdayOpenMark();
if(openMark!=2){
	if(openMark==0){ //对好友公开
		UserFriendBean friendBean = ServiceFactory.createUserService().getUserFriend(user.getId(), loginUser.getId());
	    if(friendBean!=null){
	        Date date = DateUtil.parseDate(friend.getBirthday(), DateUtil.normalDateFormat);
%>
生日:<%= DateUtil.getDateString(friend.getBirthday()) %><br/>
<%
	    }
	}
	else if(openMark==1){ //对所有人公开
		//Date date = DateUtil.parseDate(user.getBirthday(), DateUtil.normalDateFormat);
%>
生日:<%= DateUtil.getDateString(friend.getBirthday()) %><br/>
<%		
	}
}
%>
<%--//liuyi 2007-01-22 生日信息 end --%>
身高:<%=friend.getHeight()%> 厘米<br/>
体重:<%=friend.getWeight()%> KG<br/>
手机:(保密)<%--=friend.getMobile()--%> <br/>
职业:<%=StringUtil.toWml(friend.getWork())%> <br/>
性格:<%=(String)LoadResource.getPersonality().get(friend.getPersonality()+"")%><br/>
婚姻状况:<%=(String)LoadResource.getMarriage().get(friend.getMarriage()+"")%><br/>
交友目的:<%=(String)LoadResource.getFriendAim().get(friend.getAim()+"")%><br/>
择友条件:<%=StringUtil.toWml(friend.getFriendCondition())%> <br/>
<%--
上次登录时间:<%=user.getUs().getLastLoginTimeStr()%><br/>
当前头衔:
<%
HashMap map=LoadResource.getRankMap();
String name=null;
RankBean rank = (RankBean) map.get(new Integer(user.getUs().getRank()));
if(user.getGender()==1)
{name=rank.getMaleName();}
else{name=rank.getFemaleName();}
%>
<%=name%><br/>
当前等级:<%=user.getUs().getRank()%>级<br/>
当前经验值:<%=user.getUs().getPoint()%>点<br/>
当前反倭值:<%=user.getUs().getSpirit()%>点<br/>
当前社交指数:<%=user.getUs().getSocial()%>点<br/>
当前财富:<%=user.getUs().getGamePoint()%>乐币<br/>
<%if(friend.getUserId()!=loginUser.getId()){
String relationName=action.getRelation(loginUser.getId(),friend.getUserId());
if(relationName!=null){%>
<%=user.getGender() == 1? "他" : "她"%>是您的<%=relationName%><br/>
<%}
String friendLevel=action.getFriendLevel(friend.getUserId(),loginUser.getId());
if(friendLevel!=null){
%>
<%=user.getGender() == 1? "他" : "她"%>对你的友好度:<%=friendLevel%>点 <br/>
<%}
friendLevel=action.getFriendLevel(loginUser.getId(),user.getId());
if(friendLevel!=null){
%>
你对<%=loginUser.getGender() == 1? "他" : "她"%>的友好度:<%=friendLevel%>点 <br/>
<%}%>

<a href="/chat/post.jsp?toUserId=<%= user.getId()%>"><%if(onlineUser!=null){%>与<%}else{%>给<%}%><%=user.getGender() == 1? "他" : "她"%><%if(onlineUser!=null){%>聊天<%}else{%>留言<%}%></a>&nbsp;
<a href="/user/sendMessage.jsp?toUserId=<%=user.getId()%>">给<%=user.getGender() == 1? "他" : "她"%>写信</a><br/>
<%if(relationName!=null &&(relationName.equals("老公")||relationName.equals("老婆"))){%>
<a href="divorce.jsp?userId=<%=user.getId()%>">与<%=user.getGender() == 1? "他" : "她"%>离婚</a><br/>
<%}
else if(relationName!=null && relationName.equals("金兰")){%>
<a href="preDeleteJy.jsp?toId=<%=user.getId()%>">与<%=user.getGender() == 1? "他" : "她"%>割袍断义</a><br/>
<%}
}%>
--%>
<%if(notSelf){%>
<a href="/chat/post.jsp?toUserId=<%= user.getId()%>">与<%=user.getGenderText()%>聊天</a>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>">用户信息</a><br/>
<%}%>
<a href="friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>