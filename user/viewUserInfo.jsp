<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="net.joycool.wap.service.infc.*" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.spec.friend.*,jc.credit.CreditAction,jc.credit.UserInfo,jc.match.MatchAction,jc.match.MatchUser,jc.match.MatchRank"%><%!
static IFriendService service = ServiceFactory.createFriendService();
//======增加心情======
static MoodService mservice=new MoodService();
//======增加完毕======
static float otrate = 0.56f;
static int markd = 4;
static String getOTString(int level) {
	StringBuffer sb = new StringBuffer();
	int n1 = level / markd;
	if(n1 > 0) {
		level -= n1 * markd;
		int n2 = n1 / markd;
		if(n2 > 0) {
			n1 -= n2 * markd;
			for(int i = 0;i < n2;i++)
				sb.append('★');
		}
		for(int i = 0;i < n1;i++)
			sb.append('◆');
	}
	for(int i = 0;i < level;i++)
		sb.append('▲');
	return sb.toString();
}
%><%
response.setHeader("Cache-Control","no-cache");
//得到房间号
int roomId=0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//String backTo = (String) request.getAttribute("backTo");
//String oldBackTo = backTo;
//backTo = URLEncoder.encode(backTo, "UTF-8");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserSettingBean set = loginUser.getUserSetting();
UserBean user = (UserBean) request.getAttribute("user");
if(user==null||user.getId()==100){%>无法显示用户信息<%}else{
UserStatusBean userStatus=(UserStatusBean)UserInfoUtil.getUserStatus(user.getId());
CreditAction creditAction = new CreditAction();
UserInfo ui = creditAction.service.getUserInfo(" user_id=" + user.getId()); 
String strId = String.valueOf(user.getId());
UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(strId);
int online=1; if(onlineUser == null) online=0;
boolean notSelf = (loginUser == null || loginUser.getId() != user.getId());

%>
<%=(set!=null&&set.isFlagHideHat())?user.getUserHatText():user.getUserHat()%><%=StringUtil.toWml(user.getNickName())%>[<%=user.getId()%>]<br/>
<%-- TorchUserBean torchUser = TorchAction.getTorchUser(user.getId());
if(torchUser!=null){%>称号:<a href="/wgame/torch/index.jsp">奥运火炬手(L<%=torchUser.getTorchCount()%>)</a><br/>
<%if(!notSelf){%>火炬指数:<%=StringUtil.numberFormat(TorchAction.getPoint(torchUser))%><br/><%}%>
<%}--%>
<%if(!notSelf){%>
<a href="userInfo.jsp">修改资料</a><br/>
<%}%>
<% // 显示用户信息图片，录入新人卡、旗帜卡等等
boolean hideStar = set!=null&&set.isFlagHideStar();
String extraInfo = hideStar ? user.getExtraInfoText(10) : user.getExtraInfoNoCache(10);
if(extraInfo!=null){%><a href="/user/extraInfo.jsp?userId=<%=user.getId()%>"><%=extraInfo%></a><br/><%}%>
<%if(user.getGender()==1){%>男<%}else{%>女<%}%>
/<%if(user.getCityname() != null){%><%=user.getCityname()%><%}else{%>未知<%}%>
/<%=user.getAge()%>岁/<a href="/friend/credit/credit.jsp?uid=<%=user.getId()%>">可信<%=ui==null?0:ui.getTotalPoint()%>%</a><br/>
<%
String cub = jc.show.CoolShowAction.getCoolShowThumb(user);
if(cub!=null){
String kshowLink = "index.jsp";
if(notSelf)
	kshowLink = "frishow.jsp?uid=" + user.getId();
%><a href="/kshow/<%=kshowLink%>"><img src="/rep/show/t/<%=cub%>" alt="秀"/></a><br/><%
}else{
%><a href="/kshow/index.jsp"><img src="/kshow/img/e<%=user.getGender()%>.gif" alt="秀"/></a><br/><%
}%>
<%if(user.isFlagFriend()){
%><a href="/friend/friendInfo.jsp?userId=<%=user.getId()%>">查看真实资料</a><%
// 必须是填写了交友资料并且可信度总分=0的人自己才能看到这个连接
if ((ui == null || ui.getTotalPoint() == 0) && user.getId() == loginUser.getId()){
%><br/><a href="imp.jsp">交友资料导至可信度</a><%
}
}else{
%>查看真实资料<%
}%><br/>
<%if(notSelf){%>
<a href="/chat/post.jsp?roomId=<%=roomId %>&amp;toUserId=<%=user.getId()%>"><%if(online==1){%>与<%}else{%>给<%}%><%=user.getGenderText()%><%if(online==1){%>聊天<%}else{%>留言<%}%></a>
<%if(online==1){%>
<a href="operUser.jsp?action=inviteGame&amp;toUserId=<%=user.getId()%>">邀请<%=user.getGenderText()%>玩游戏</a>
<%}%><br/><%}%>=======<br/>
心情:<%
//======增加心情======
MoodUserBean mub=mservice.getLastMoodBean(user.getId());
if (mub != null){
	%><%if(!hideStar){%><img src="../../beacon/mo/img/<%=mub.getType()%>.gif" alt="<%=mub.getTypeName()%>"/><%}else{%>[<%=mub.getTypeName()%>]<%}%><a href="../beacon/mo/mood.jsp?uid=<%=mub.getUserId()%>"><%=StringUtil.toWml(StringUtil.limitString(mub.getMood(),14))%></a><br/><%
}else{
	//登陆ID和信息中的ID相等，则可以添加心情
	if ( loginUser.getId() == user.getId() ){
		%><a href="../beacon/mo/mood.jsp">添加心情</a><br/><%
	}else{
		%>无<br/><%
	}
}
//======增加完毕======
%>个性签名:<%=StringUtil.toWml(user.getSelfIntroduction())%><br/>
<%-- liuyi 2006-12-20 在线用户状态显示修改 start --%>
当前状态:<%if(online==0){%><%="离线"%><%}else{
%><%=(PositionUtil.getPositionName(strId, onlineUser.getPositionId()))%><%}%>
<%-- liuyi 2006-12-20 在线用户状态显示修改 end --%><br/>
<%-- mcq_4_增加查看用户头衔\等级\经验值   日期:2006-6-7--%>
<%
HashMap map=LoadResource.getRankMap();
String name=null;
int mark=0;
RankBean rank = (RankBean) map.get(new Integer(userStatus.getRank()));
if(user.getGender()==1)
{name=rank.getMaleName();}
else{name=rank.getFemaleName();}
%>
等级:<%=userStatus.getRank()%>[<%=name%>]<br/>
<% ForumUserBean forumUser = ForumCacheUtil.getForumUser(user.getId());
if(forumUser!=null){
if(forumUser.getExp()>=10){	/*经验值10一下的无视*/%>
论坛经验:<%=forumUser.getExp()%>[<%=forumUser.getRankName()%>]<br/>
<%}%>
<%if(forumUser.getMForumId()>0){	/*版主*/
ForumBean forum = ForumCacheUtil.getForumCache(forumUser.getMForumId());
if(forum != null){
%>
论坛:<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>,版主<br/>
<%}}%>
<%if(forumUser.getInfo().length()>0){%>
<%=forumUser.getInfo()%><br/>
<%}%>
<%}%>
<%
if(userStatus.getTong()>0){
	if(userStatus.getTong()<20000){
TongUserBean tongUser=TongCacheUtil.getTongUser(userStatus.getTong(),user.getId());
TongBean tong=TongCacheUtil.getTong(userStatus.getTong());
TongAction taction=new TongAction(request);
if(tong!=null){
%>所属帮会:<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle())%></a>,<%
if(tongUser!=null){
%>称号<%if(tongUser.getMark()==2){%>帮主<%}else if(tongUser.getMark()==1){%>副帮主<%}else{%><%=StringUtil.toWml(taction.getTongTitle(tongUser))%><%}%><br/><%}else{%>无称号<br/>
<%}}
}else{
jc.family.FamilyUserBean fmUser=jc.family.FamilyAction.getFmUserByID(user.getId());
if(fmUser!=null&&fmUser.getFm_id()!=0){%>
所属家族:<a href="/fm/myfamily.jsp?id=<%=fmUser.getFm_id()%>"><%=fmUser.getFamilyName()%></a><%="".equals(fmUser.getFm_name())?"":",称号:"+StringUtil.toWml(fmUser.getFm_name())%><br/>
<%
}}}else{
jc.family.FamilyUserBean myfmu=jc.family.FamilyAction.getFmUserByID(loginUser.getId());
%>所属家族:(无)<%if(myfmu!=null&&myfmu.isflagNewMember()){%><a href="/fm/manage/invite.jsp?cmd=y&amp;userid=<%=user.getId()%>">邀请加入</a><%}%><br/><%
}
if(!notSelf){		// 自己才能看到的信息
UserHonorBean honor = user.getUserHonor();
%>
经验值:<%=userStatus.getPoint()%><br/>
反倭值:<%=userStatus.getSpirit()%><br/>
慈善值:<%=userStatus.getCharitarian()%><br/>
荣誉勋章:<%=honor.getRankName()%><br/>
荣誉值:<%=honor.getHonor()%><br/>
上周荣誉值:<%=honor.getLastWeek()%><br/>
在线时间:<%=userStatus.getOnlineTime()%>小时<br/>
<%}
int otlevel = 0;
if(userStatus.getOnlineTime() >= 5)
	otlevel = (int)Math.pow(userStatus.getOnlineTime() / 5, otrate);
%>
活跃:R<%=otlevel%>(<%=getOTString(otlevel)%>)<br/>
社交指数:<%=userStatus.getSocial()%>点<br/>
携带财富:<%=StringUtil.bigNumberFormat(userStatus.getGamePoint())%>乐币<br/>
<%-- mcq_4_end--%>
<%
if(userStatus.getMark()==2){
	FriendAction action =new FriendAction(request);
	FriendMarriageBean friendMarriage=action.infoMarriage(user.getId());
	if(friendMarriage!=null){
		int coupleId = (friendMarriage.getFromId()==user.getId())?friendMarriage.getToId():friendMarriage.getFromId();
		boolean flag = ((friendMarriage.getFromId()==loginUser.getId()) || (friendMarriage.getToId()==loginUser.getId()));
		UserBean coupleUser = UserInfoUtil.getUser(coupleId);
		int ringId=friendMarriage.getFingerRingId();
	    FriendRingBean ring = action.getFriendRing(ringId);
	    if(!flag || !notSelf){	
			if(coupleUser!=null){
			    boolean isMan = (user.getGender()==1);
			    %><%= (isMan?"他":"她")%>的<%= (isMan?"老婆":"老公") %>是<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=coupleUser.getId()%>" ><%= StringUtil.toWml(coupleUser.getNickName()) %></a>.<a href="/friend/review.jsp?marriageId=<%=friendMarriage.getId()%>"><img src="<%=ring.getImageUrl()%>" alt="loading"/></a><br/><%
			}
	    }
	    else{%>
			<%=user.getGenderText()%>是你的<%=user.getGender() == 1? "老公" : "老婆"%>。
				<a href="/friend/review.jsp?marriageId=<%=friendMarriage.getId()%>">	<img src="<%=ring.getImageUrl()%>" alt="loading"/></a><br/>
		<%
		}
	}
}
if(notSelf){	// 如果是自己，不显示部分链接
%>
<%
FriendAction action = new FriendAction(request);
int friendLevel=action.getFriendLevelInt(user.getId(),loginUser.getId());
int friendLevel2=action.getFriendLevelInt(loginUser.getId(),user.getId());
if(friendLevel!=0 || friendLevel2 != 0){%>
友好度：<%=friendLevel%>/<%=friendLevel2%><br/>
<%}%>
最近登录:<%=userStatus.getLastLoginTimeStr()%><br/>
<a href="sendMessage.jsp?toUserId=<%=user.getId()%>">给<%=user.getGenderText()%>写信</a><%if(online==0){%><br/><%}else{%> <%}%>
<%if(online==1){%>.<a href="/chat/viewMessages.jsp?toUserId=<%=user.getId()%>">聊天记录</a><br/><%}%>
<%
//liuyi 2006-10-30 交友系统 start
IUserService userService = UserBagAction.getUserService();
UserFriendBean friendBean = userService.getUserFriend(loginUser.getId(), user.getId());
UserFriendBean friend2Bean = userService.getUserFriend(user.getId(),loginUser.getId());
if(loginUser != null) {
boolean isBadGuys=userService.isUserBadGuy(loginUser.getId(), user.getId());
if(!isBadGuys){%>
<a href="sendAction.jsp?roomId=<%=roomId%>&amp;toUserId=<%=user.getId()%>">对<%=user.getGenderText()%>动作</a>  
<%}%>
<a href="operUser.jsp?action=sendMoney&amp;toUserId=<%=user.getId()%>">送<%=user.getGenderText()%>乐币</a><br/>
<%
if(friendBean==null || friendBean.getMark()==0){
if(friendBean!=null && friendBean.getMark()==0){%>
<a href="/user/dfriend.jsp?id=<%=user.getId()%>">删除好友</a> 
<% }else{ %>
<%if(!isBadGuys){%><a href="OperFriend.do?add=1&amp;friendId=<%=user.getId()%>">加为好友</a> <%}%>
<%} %>
<logic:equal name="isBadGuy" value="0">
<%if(friendBean==null){%><a href="confirmBadGuy.jsp?add=1&amp;badGuyId=<%=user.getId()%>">进黑名单</a><%}%>
</logic:equal>
<logic:equal name="isBadGuy" value="1">
<a href="OperBadGuy.do?delete=1&amp;badGuyId=<%=user.getId()%>">出黑名单</a>
</logic:equal><br/><%}%>
<%if(userStatus.getTong()==0&&UserInfoUtil.getUserStatus(loginUser.getId()).getTong()>0&&UserInfoUtil.getUserStatus(loginUser.getId()).getTong()<20000){%>
<a href="inviteTong.jsp?userId=<%=user.getId()%>">邀请<%=user.getGenderText()%>加入本帮</a><br/>
<%}}
if(friendBean!=null && friend2Bean!=null){
UserStatusBean loginUserStatus=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
	if(userStatus.getMark()!=2 && loginUserStatus.getMark()==0 && loginUser.getGender()!=user.getGender()&& friendBean.getLevelValue()>=500&& friend2Bean.getLevelValue()>=500){
%>
<a href="/friend/proposal.jsp?toId=<%=user.getId()%>">向<%=user.getGenderText()%>求婚</a><br/>
<%   }
	if(friendBean.getMark()==0 && friend2Bean.getMark()==0 && friendBean.getLevelValue()>=Constants.MIN_FRIEND_LEVEL_FOR_JY && friend2Bean.getLevelValue()>=Constants.MIN_FRIEND_LEVEL_FOR_JY){
%>
<a href="/friend/jyProposal.jsp?toId=<%=user.getId()%>">与<%=user.getGenderText()%>义结金兰</a><br/> 
<% }else if(friendBean.getMark()==1){
%>
<a href="/friend/deleteJy.jsp?toId=<%=user.getId()%>">与<%=user.getGenderText()%>割袍断义</a><br/> 
<%		
	}
	else if(friendBean.getMark()==2){
%>
<a href="/friend/divorce.jsp?userId=<%=user.getId()%>">与<%=user.getGenderText()%>离婚</a><br/>
<%		
}} 
//liuyi 2006-10-30 交友系统 end
%>
<%if(user.getHome()==1){%>
<a href="/home/viewHomeUser.jsp?userId=<%=user.getId()%>">查<%=user.getGenderText()%>档案</a>  
<a href="/home/home.jsp?userId=<%=user.getId()%>"><%=user.getGenderText()%>的家园</a><br/>
<%}%>
<a href="userBagPost.jsp?roomId=<%=roomId%>&amp;userId=<%=user.getId()%>">使用道具</a> 
<a href="/pet/viewuserpet.jsp?id=<%=user.getId()%>">宠物信息</a><br/>
<a href="/admin/report/user.jsp?id=<%=user.getId()%>">x举报该用户x</a><br/>
<%}// 如果是自己查看不显示部分链接end%>
<%}%>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%
String chatRoomId = (String)session.getAttribute("chatroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/> 
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/> 
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>