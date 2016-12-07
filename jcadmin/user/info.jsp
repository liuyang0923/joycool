<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="net.joycool.wap.service.infc.*" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.spec.friend.*"%><%@ page import="java.io.File"%><%!
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
	static int getTongRank(TongUserBean bean) {
		int donation = bean.getDonation();
		int rank = 0;
		if (donation < 500) {
			rank = 0;
		} else if (donation < 2000) {
			rank = 1;
		} else if (donation < 10000) {
			rank = 2;
		} else if (donation < 30000) {
			rank = 3;
		} else if (donation < 100000) {
			rank = 4;
		} else if (donation < 200000) {
			rank = 5;
		} else if (donation < 500000) {
			rank = 6;
		} else if (donation < 1000000) {
			rank = 7;
		} else {
			rank = 8;
		}
		return rank;
	}
%><%
CustomAction action = new CustomAction(request);
int userId = StringUtil.toInt(request.getParameter("id"));
UserBean user=UserInfoUtil.getUser(userId);
if(request.getParameter("delPhoto")!=null && user.isFlagFriend()){	// 删除头像
	FriendBean friend=service.getFriend(user.getId());
	service.updateFriend("attach='3_2.gif'", "user_id=" + userId);
	service.flushFriend(userId);
	if (friend.getAttach().indexOf("_") == -1) {
		File f = new File(Constants.FRIEND_FILE_PATH, friend.getAttach());
		if (f.exists()) {// 检查File.txt是否存在
			f.delete();// 删除File.txt文件
		}
	}
}
int type=action.getParameterInt("type");
if(user!=null&&type>0) {
	int add = action.getParameterInt("add");
	if(type==1){
		UserInfoUtil.updateUserCash(userId, add*10000, 0, null);
	} else if(type==2){
		UserInfoUtil.updateUserSocial(userId, add);
	} else if(type==3){
		UserInfoUtil.updateUserRank(userId, add, UserInfoUtil.getUserStatus(userId).getPoint(), null);
	} else if(type==4){
		UserInfoUtil.updateUserRank(userId, UserInfoUtil.getUserStatus(userId).getRank(), add, null);
	} else if(type==5){
		UserInfoUtil.getUserStatus(userId).setTotalOnlineTime(add*60);
	}
	response.sendRedirect("info.jsp?id="+userId);
	return;
}
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body><%
UserBean loginUser = new UserBean();;
UserSettingBean set = loginUser.getUserSetting();
UserStatusBean userStatus=(UserStatusBean)UserInfoUtil.getUserStatus(user.getId());
String strId = String.valueOf(user.getId());
UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(strId);
int online=1; if(onlineUser == null) online=0;
boolean notSelf = false;
%>
<a href="queryUserInfo.jsp?id=<%=user.getId()%>"><%=(set!=null&&set.isFlagHideHat())?user.getUserHatText():user.getUserHat()%><%=StringUtil.toWml(user.getNickName())%>(<%=user.getId()%>)</a>-<a href="../alterUserInfo.jsp?inputUserId=<%=userId%>">修改</a><br/>


<% // 显示用户信息图片，录入新人卡、旗帜卡等等
boolean hideStar = set!=null&&set.isFlagHideStar();
String extraInfo = hideStar ? user.getExtraInfoText(10) : user.getExtraInfoNoCache(10);
if(extraInfo!=null){%><%=extraInfo%><br/><%}%>
<%if(user.getGender()==1){%>男<%}else{%>女<%}%>
/<%if(user.getCityname() != null){%><%=user.getCityname()%><%}else{%>未知<%}%>
/<%=user.getAge()%>岁<br/>
<%
jc.show.CoolUser cu = jc.show.CoolShowAction.getCoolUser(user);
if(cu!=null&&cu.getImgurl().length()!=0){
String kshowLink = "index.jsp";
if(notSelf)
	kshowLink = "frishow.jsp?uid=" + user.getId();
%><img src="/rep/show/t/<%=cu.getImgurl()%>" alt="秀"/><br/><%
}else{
%><img src="/kshow/img/e<%=user.getGender()%>.gif" alt="秀"/><br/><%
}%>
<%if(user.isFlagFriend()){
FriendBean friend=service.getFriend(user.getId());
	if(friend!=null && !friend.getAttach().equals("")){%>
<img src="<%=Constants.FRIEND_FILE_PATH_RESOURCE_ROOT_URL%><%=friend.getAttach()%>" alt=""/><a href="info.jsp?id=<%=userId%>&delPhoto=1" onclick="return confirm('确认删除头像？')">删除头像</a>
<br/>
	<%}else{%>该用户没有上传个人形象照片<br/><%}
}else{%>该用户没有上传个人形象照片<br/><%}

%>======<br/>
心情:<%
MoodUserBean mub=mservice.getLastMoodBean(user.getId());
if (mub != null){
	%><%if(!hideStar){%><img src="/beacon/mo/img/<%=mub.getType()%>.gif" alt="<%=mub.getTypeName()%>"/><%}else{%>[<%=mub.getTypeName()%>]<%}%><a href="../beacon/mood.jsp?f=findByUserId&id=<%=mub.getUserId()%>"><%=StringUtil.toWml(StringUtil.limitString(mub.getMood(),14))%></a><br/><%
}else{
		%>无<br/><%
}
%>个性签名:<%=StringUtil.toWml(user.getSelfIntroduction())%>-<a href="../alterUserInfo.jsp?inputUserId=<%=userId%>">修改</a><br/>
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
%>论坛经验:<%=forumUser.getExp()%>[<%=forumUser.getRankName()%>]<br/>
<%if(forumUser.getMForumId()>0){	/*版主*/
ForumBean forum = ForumCacheUtil.getForumCache(forumUser.getMForumId());
if(forum != null){
%>
论坛:<a href="forums.jsp?fid=<%=forum.getId()%>"><%=forum.getTitle()%></a>,版主<br/>
<%}}%>
<%if(forumUser.getInfo().length()>0){%>
<%=forumUser.getInfo()%><br/>
<%}%>
<%}%>
<%if(true){		// 自己才能看到的信息
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
<%
if(userStatus.getMark()==2){
	FriendAction action2 =new FriendAction(request);
	FriendMarriageBean friendMarriage=action2.infoMarriage(user.getId());
	if(friendMarriage!=null){
		int coupleId = (friendMarriage.getFromId()==user.getId())?friendMarriage.getToId():friendMarriage.getFromId();
		boolean flag = ((friendMarriage.getFromId()==loginUser.getId()) || (friendMarriage.getToId()==loginUser.getId()));
		UserBean coupleUser = UserInfoUtil.getUser(coupleId);
		int ringId=friendMarriage.getFingerRingId();
	    FriendRingBean ring = action2.getFriendRing(ringId);
	    if(!flag || !notSelf){	
			if(coupleUser!=null){
			    boolean isMan = (user.getGender()==1);
			    %><%= (isMan?"他":"她")%>的<%= (isMan?"老婆":"老公") %>是<a href="queryUserInfo.jsp?id=<%=coupleUser.getId()%>" ><%= StringUtil.toWml(coupleUser.getNickName()) %></a>.<img src="<%=ring.getImageUrl()%>" alt=""/><br/><%
			}
	    }
	    else{%>
			<%=user.getGenderText()%>是你的<%=user.getGender() == 1? "老公" : "老婆"%>。
				<img src="<%=ring.getImageUrl()%>" alt=""/><br/>
		<%
		}
	}
}
if(userStatus.getTong()>0&&userStatus.getTong()<20000){
TongUserBean tongUser=TongCacheUtil.getTongUser(userStatus.getTong(),user.getId());
TongBean tong=TongCacheUtil.getTong(userStatus.getTong());
if(tong!=null){
%>所属帮会:<a href="../tong/search.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle())%></a>,<%
if(tongUser!=null){
%>称号<%if(tongUser.getMark()==2){%>帮主<%}else if(tongUser.getMark()==1){%>副帮主<%}else if(tongUser.getMark()==3){%><%=StringUtil.toWml(tongUser.getHonor())%><%}else{%><%=TongAction.rankNames[getTongRank(tongUser)]%><%}%><br/><%}else{%>无称号<br/>
<%}}
}%>

最近登录:<%=userStatus.getLastLoginTimeStr()%><br/>
<a href="showNotice.jsp?uid=<%=userId%>">信息</a>|<a href="showAuction.jsp?uid=<%=userId%>">拍卖</a>|<a href="imglib.jsp?uid=<%=userId%>">图库</a><br/>
<%if(group.isFlag(0)){%>
<form method="post" action="info.jsp?id=<%=userId%>&type=1">
<input type="text" name="add" size="6" maxlength="6">万
<input type="submit" value="增加乐币">
</form>
<form method="post" action="info.jsp?id=<%=userId%>&type=2">
<input type="text" name="add" size="5" maxlength="6">
<input type="submit" value="增加社交值">
</form>
<form method="post" action="info.jsp?id=<%=userId%>&type=3">
<input type="text" name="add" size="2" maxlength="2">
<input type="submit" value="设置等级">
</form>
<form method="post" action="info.jsp?id=<%=userId%>&type=4">
<input type="text" name="add" size="8" maxlength="10">
<input type="submit" value="设置经验值">
</form>
<form method="post" action="info.jsp?id=<%=userId%>&type=5">
<input type="text" name="add" size="5" maxlength="6">小时
<input type="submit" value="设置在线时间">
</form>
<%}%>
</body>
</html>