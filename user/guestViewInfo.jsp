<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.friend.FriendBean,net.joycool.wap.action.friend.*,net.joycool.wap.framework.*,net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.bean.jcforum.*,net.joycool.wap.bean.tong.*,net.joycool.wap.bean.tong.TongBean,net.joycool.wap.action.tong.TongAction,net.joycool.wap.cache.util.*"%><%@ page import="jc.credit.*,net.joycool.wap.spec.friend.*,net.joycool.wap.action.friend.*,net.joycool.wap.bean.friend.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.cache.util.*,net.joycool.wap.cache.*"%>
<%! static MoodService mservice = new MoodService(); 
	static String aim[] = {"未填写","恋人","知已","玩伴","其它"};
	static String astro[] = {"白羊座","金牛座","双子座","巨蟹座","狮子座","室女座","天秤座","天蝎座","人马座","摩羯座","宝瓶座","双鱼座"};
	static String xingge[] = {"温柔体贴","活泼开朗","古灵精怪","憨厚老实","豪情奔放","天真淳朴"};
	static String hunyin[] = {"未婚","已婚","离异","丧偶"};
	static String mudi[] = {"恋人","知己","玩伴","解闷","其他"};
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
%>
<%
response.setHeader("Cache-Control","no-cache");
CreditAction action = new CreditAction(request);
UserBean user = null;
RankBean rank = null;
MoodUserBean mub = null;
String tip = "";
String topLink = "";
String rankName = "";
String marriage = "";
HashMap rankMap = null;
boolean hideStar = false;
boolean showForumRank = false;
String userNameStr = "";
String forumRank = "";
String tongStr = "";
int mark=0;
int otlevel = 0;
int online = 1;	//0:离线 1:在线
int uid = action.getParameterInt("userId");
if (uid <= 0){
	uid = action.getParameterInt("toUserId");
}
int type = action.getParameterInt("t");
if (type < 0 || type > 4){
	type = 0;
}
UserBean onlineUser = null;
UserStatusBean userStatus = null;
ForumUserBean forumUser = null;
switch (type){
case 0:{
	topLink = "资料首页|<a href=\"guestViewInfo.jsp?t=1&amp;userId=" + uid + "\">工作</a>|<a href=\"guestViewInfo.jsp?t=2&amp;userId=" + uid + "\">生活</a>|<a href=\"guestViewInfo.jsp?t=3&amp;userId=" + uid + "\">外貌</a>|<a href=\"guestViewInfo.jsp?t=4&amp;userId=" + uid + "\">社区信息</a>|<a href=\"/home/home2.jsp?userId=" + uid + "\">TA的家园</a><br/>";
	break;
}
case 1:{
	topLink = "<a href=\"guestViewInfo.jsp?userId=" + uid + "\">资料首页</a>|工作|<a href=\"guestViewInfo.jsp?t=2&amp;userId=" + uid + "\">生活</a>|<a href=\"guestViewInfo.jsp?t=3&amp;userId=" + uid + "\">外貌</a>|<a href=\"guestViewInfo.jsp?t=4&amp;userId=" + uid + "\">社区信息</a>|<a href=\"/home/home2.jsp?userId=" + uid + "\">TA的家园</a><br/>";
	break;
}
case 2:{
	topLink = "<a href=\"guestViewInfo.jsp?userId=" + uid + "\">资料首页</a>|<a href=\"guestViewInfo.jsp?t=1&amp;userId=" + uid + "\">工作</a>|生活|<a href=\"guestViewInfo.jsp?t=3&amp;userId=" + uid + "\">外貌</a>|<a href=\"guestViewInfo.jsp?t=4&amp;userId=" + uid + "\">社区信息</a>|<a href=\"/home/home2.jsp?userId=" + uid + "\">TA的家园</a><br/>";
	break;
}
case 3:{
	topLink = "<a href=\"guestViewInfo.jsp?userId=" + uid + "\">资料首页</a>|<a href=\"guestViewInfo.jsp?t=1&amp;userId=" + uid + "\">工作</a>|<a href=\"guestViewInfo.jsp?t=2&amp;userId=" + uid + "\">生活</a>|外貌|<a href=\"guestViewInfo.jsp?t=4&amp;userId=" + uid + "\">社区信息</a>|<a href=\"/home/home2.jsp?userId=" + uid + "\">TA的家园</a><br/>";
	break;
}
case 4:{
	topLink = "<a href=\"guestViewInfo.jsp?userId=" + uid + "\">资料首页</a>|<a href=\"guestViewInfo.jsp?t=1&amp;userId=" + uid + "\">工作</a>|<a href=\"guestViewInfo.jsp?t=2&amp;userId=" + uid + "\">生活</a>|<a href=\"guestViewInfo.jsp?t=3&amp;userId=" + uid + "\">外貌</a>|社区信息|<a href=\"/home/home2.jsp?userId=" + uid + "\">TA的家园</a><br/>";
	break;
}
}
if (uid <= 0){
	tip = "该用户不存在.";
} else {
	user = UserInfoUtil.getUser(uid);
	if (user == null){
		tip = "该用户不存在.";
	} else {
		mub=mservice.getLastMoodBean(user.getId());
		onlineUser = (UserBean)OnlineUtil.getOnlineBean(String.valueOf(uid));
		if (onlineUser == null){
			online=0;
		}
		userNameStr = user.getNickNameWml() + "[" + user.getId() + "](" + (online==0?"离线":"在线") + ")" ;
		userStatus = (UserStatusBean)UserInfoUtil.getUserStatus(user.getId());
		// 用户等级
		rankMap=LoadResource.getRankMap();
		if (userStatus != null){
			rank = (RankBean) rankMap.get(new Integer(userStatus.getRank()));
			rankName = userStatus.getRank() + "";
			if(user.getGender()==1){
				rankName += "["  + rank.getMaleName() + "]";
			} else { 
				rankName += "[" + rank.getFemaleName() + "]";
			}
		}
		// 论坛经验
		forumUser = ForumCacheUtil.getForumUser(user.getId());
		if(forumUser != null){
			// 经验值10以下的无视
			if(forumUser.getExp() >= 10){
				showForumRank = true;
				forumRank = "论坛经验:" + forumUser.getExp() + "[" + forumUser.getRankName() + "]";
				// 是否版主?
				if(forumUser.getMForumId()>0){
					ForumBean forum = ForumCacheUtil.getForumCache(forumUser.getMForumId());
					if(forum != null){
						forumRank += "<br/>论坛:<a href=\"/jcforum/forum.jsp?forumId=" + forum.getId() + "\">" + forum.getTitle() + "</a>,版主";
					}
				}
			}
		}
		// 活跃值
		if (userStatus != null){
			if(userStatus.getOnlineTime() >= 5)
				otlevel = (int)Math.pow(userStatus.getOnlineTime() / 5, otrate);
		}
		// 结婚没?
		if(userStatus.getMark()==2){
			FriendAction action2 =new FriendAction(request);
			FriendMarriageBean friendMarriage=action2.infoMarriage(user.getId());
			if(friendMarriage!=null){
				int coupleId = (friendMarriage.getFromId()==user.getId())?friendMarriage.getToId():friendMarriage.getFromId();
				UserBean coupleUser = UserInfoUtil.getUser(coupleId);
				int ringId=friendMarriage.getFingerRingId();
			    FriendRingBean ring = action2.getFriendRing(ringId);
				if(coupleUser!=null){
				    boolean isMan = (user.getGender()==1);
				    if (isMan){
				    	marriage += "他的老婆是:";
				    } else {
				    	marriage += "她的老公是:";
				    }
				    marriage += "<a href=\"/user/ViewUserInfo.do?roomId=0&amp;userId=" + coupleUser.getId() + "\" >" + StringUtil.toWml(coupleUser.getNickNameWml()) + "</a>";
					if (ring != null){
						marriage += ".<a href=\"/friend/review.jsp?marriageId=" + friendMarriage.getId() + "\"><img src=\"" + ring.getImageUrl() + "\" alt=\"o\"/></a>";
					}
				}
			}
	   }
	   // 帮会
	   if(userStatus.getTong()>0&&userStatus.getTong()<20000){
		   TongUserBean tongUser = TongCacheUtil.getTongUser(userStatus.getTong(),user.getId());
		   TongBean tong = TongCacheUtil.getTong(userStatus.getTong());
		   TongAction taction = new TongAction(request);
		   if(tong!=null){
			   tongStr = "所属帮会:<a href=\"/tong/tong.jsp?tongId=" + tong.getId() + "\">" + StringUtil.toWml(tong.getTitle()) + "</a>,";
		   }
		   if(tongUser!=null){
			   tongStr += "称号:";
			   if(tongUser.getMark()==2){
			   		tongStr += "帮主";
			   } else if (tongUser.getMark() == 1){
				   	tongStr += "副帮主";
			   } else {
					tongStr += StringUtil.toWml(TongAction.getTongTitle(tongUser));   
			   }
		   } else {
			   tongStr += "无称号";
		   }
	   }
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户信息">
<p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=userNameStr%><br/><%
if (type != 0){%><%=topLink%><%}
switch (type){
case 0:{
// 首页，基本信息
UserBase userBase = CreditAction.getUserBaseBean(user.getId());
if(user.getGender()==1){%>男<%}else{%>女<%}%>/<%if(user.getCityname() != null){%><%=user.getCityname()%><%}else{%>未知<%}%>/<%=user.getAge()%>岁<br/>
<%FriendBean friend = FriendAction.getFriendService().getFriend(user.getId());
if(friend != null && friend.getAttach()!=null && !friend.getAttach().equals("")){
%><img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%>/friend/attach/<%=friend.getAttach()%>" alt="o"/><br/><%
}%>
<%if (mub != null){%>心情:<img src="/beacon/mo/img/<%=mub.getType()%>.gif" alt="<%=mub.getTypeName()%>" /><%=StringUtil.toWml(StringUtil.limitString(mub.getMood(),14))%><br/><%}%>
<%
String cub = jc.show.CoolShowAction.getCoolShowThumb(user);
if(cub!=null){
String kshowLink = "index.jsp";
kshowLink = "frishow.jsp?uid=" + user.getId();
%><a href="/kshow/<%=kshowLink%>"><img src="/rep/show/t/<%=cub%>" alt="秀"/></a><br/><%
}else{
%><a href="/kshow/index.jsp"><img src="/kshow/img/e<%=user.getGender()%>.gif" alt="秀"/></a><br/><%
}%>
<%=topLink%>
<%if (jc.match.MatchAction.getMatchUser(uid) == null){
%><a href="/friend/match/index.jsp">乐后选秀</a><br/><%
} else {
%><a href="/friend/match/vote.jsp?uid=<%=uid%>">为她投票选秀!</a><br/><%	
}%>
==<%=user.getGenderText()%>的真实资料==<br/>
<% if (userBase != null){
%>生日:<%if(userBase.getBirYear() == 0 || userBase.getBirMonth() == 0 || userBase.getBirDay() == 0){%>未填写<%}else{%><%=userBase.getBirYear()%>年<%=userBase.getBirMonth()%>月<%=userBase.getBirDay()%>日;星座:<%=CreditAction.getAstroString(userBase.getAstro())%>;属<%=userBase.getAnimals()%><%}%><br/>
身高:<%if(userBase.getStature() == 0){%>未填写<%}else{%><%=userBase.getStature()%><%}%><br/>
交友目的:<%if(userBase.getAim() == 0){%>未填写<%}else{%><%=aim[userBase.getAim()]%><%}%><br/>
居住地:<%if(userBase.getProvince() == 0 || userBase.getCity() == 0){%>未填写<%}else{%><%=action.getPlace(userBase)%><%}%><br/>
学历:<%if(userBase.getEducation() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getEducation())%><%}%><br/>
个性类型:<%if(userBase.getPersonality() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getPersonality())%><%}%><br/>
血型:<%if(userBase.getBlood() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getBlood())%><%}%><br/><%
} else {
	if (friend != null){
		%>真实姓名:<%=StringUtil.toWml(friend.getName())%><br/>
		生日:<%=StringUtil.toWml(friend.getBirthday())%><br/>
		所在城市:<%=StringUtil.toWml(friend.getCity())%><br/>
		星座:<%=astro[friend.getConstellation()]%><br/>
		身高:<%=friend.getHeight()%>厘米<br/>
		体重:<%=friend.getWeight()%>KG<br/>
		职业:<%=StringUtil.toWml(friend.getWork())%><br/>
		性格:<%=xingge[friend.getPersonality()]%><br/>
		婚姻:<%=hunyin[friend.getMarriage()]%><br/>
		交友目的:<%=mudi[friend.getAim()]%><br/>
		择友条件:<%=StringUtil.toWml(friend.getFriendCondition())%><br/>
		<%
	} else {
		%>该用户暂没有填写任何资料.<br/><%
	}
}%>

<a href="/user/glogin.jsp?uid=<%=user.getId()%>">聊天</a>|<a href="/user/glogin.jsp?uid=<%=user.getId()%>">写信</a>|<a href="/user/glogin.jsp?uid=<%=user.getId()%>">动作</a>|<a href="/user/glogin.jsp?uid=<%=user.getId()%>">邀请游戏</a><br/>
<a href="/user/glogin.jsp?uid=<%=user.getId()%>">加为好友</a>|<a href="/user/glogin.jsp?uid=<%=user.getId()%>">对<%=user.getGenderText()%>使用道具</a><br/>
<a href="/user/glogin.jsp?uid=<%=user.getId()%>"><%=user.getGenderText()%>的日记</a>|<a href="/user/glogin.jsp?uid=<%=user.getId()%>"><%=user.getGenderText()%>的更多照片</a><br/>
<a href="/chat/hall2.jsp">返回聊天室</a><br/>
<a href="/guest/chat.jsp">返回游客聊天室</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%
break;
}
case 1:{
// 工作
UserWork userWork = CreditAction.service.getUserWork(" user_id=" + uid);
%>行业:<%if(userWork == null || userWork.getTrade() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getTrade())%><%}%><br/>
收入:<%if(userWork == null || userWork.getEarning() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getEarning())%><%}%><br/>
职业梦想:<%if(userWork == null || userWork.getDream() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getDream())%><%}%><br/>
<%
break;
}
case 2:{
// 生活
UserLive userLive = CreditAction.service.getUserLive(" user_id=" + uid);
%>最长恋爱时间:<%if(userLive == null || userLive.getLoveTime() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getLoveTime())%><%}%><br/>
恋爱谁买单:<%if(userLive == null || userLive.getBill() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getBill())%><%}%><br/>
是否抽烟:<%if(userLive == null || userLive.getSmoke() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getSmoke())%><%}%><br/>
喜欢的影星:<%if(userLive == null || "".equals(userLive.getFilmStart())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFilmStart()) %><%}%><br/>
喜欢的歌手:<%if(userLive == null || "".equals(userLive.getSinger())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getSinger())%><%}%><br/>
婚姻状况:<%if(userLive == null || userLive.getMarrage() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getMarrage())%><%}%><br/>
有无小孩:<%if(userLive == null || userLive.getChild()== 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getChild())%><%}%><br/>
喜欢的食物:<%if(userLive == null || "".equals(userLive.getFood())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFood())%><%}%><br/>
喜欢的运动:<%if(userLive == null || "".equals(userLive.getSport())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getSport())%><%}%><br/>
最近关注:<%if(userLive == null || "".equals(userLive.getFocusOn())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFocusOn())%><%}%><br/>
擅长:<%if(userLive == null || "".equals(userLive.getGoodAt())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getGoodAt())%><%}%><br/>
<%
break;
}
case 3:{
// 外貌
UserLooks userLooks = CreditAction.service.getUserLooks(" user_id=" + uid);
%>体型:<%if(userLooks == null || userLooks.getBodyType() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getBodyType())%><%}%><br/>
外貌自评:<%if(userLooks == null || userLooks.getLooks() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getLooks())%><%}%><br/>
魅力部位:<%if(userLooks == null || userLooks.getCharm() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getCharm())%><%}%><br/>
<%
break;
}
case 4:{
// 社区基本信息
%>当前状态:<%if(online==0){%>离线<%}else{%><%=(PositionUtil.getPositionName(String.valueOf(uid),onlineUser.getPositionId()))%><%}%><br/>

等级:<%=rankName%><br/>
<%if (showForumRank){
%><%=forumRank%><br/><%	
}%>
活跃:R<%=otlevel%>(<%=getOTString(otlevel)%>)<br/>
<%if (userStatus != null){
%>社交指数:<%=userStatus.getSocial()%>点<br/>
携带财富:<%=StringUtil.bigNumberFormat(userStatus.getGamePoint())%>乐币<br/><%
if (!"".equals(marriage)){
%><%=marriage%><br/><%	
}
}
if (!"".equals(tongStr)){
%><%=tongStr%><br/><%
}
%>最近登录:<%=userStatus.getLastLoginTimeStr()%><br/><%
break;
}
}
%><%
} else {
%><%=tip%><br/><%	
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>