<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.UserBase,jc.credit.CreditAction,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
String tip = "";
String tmp = "";
int rnd = 0;
int end = 0;
int start = 0;
int num = 1;
UserBean user = null;
List randomList = null;
MatchUser matchUser = null;
MatchRank matchRank = null;
MatchTrends trend = null;
UserBase userBase = null;
MatchInfo matchInfo = MatchAction.getCurrentMatch();
if (matchInfo != null){
	if (matchInfo.getFalg() == 2){
		response.sendRedirect("history.jsp");
		return;
	}
} else {
	tip = "比赛尚未开始,敬请期待.";
}
List lastList = MatchAction.service.getMatchUserList(" checked=1 order by create_time desc limit 2");
List trendsList = MatchAction.service.getTrendsList(" flag=0 order by id desc limit 5");
//List fansConsList = MatchAction.service.getFansConsList(" 1 order by id desc limit 5");
if (loginUser != null){
	matchUser = MatchAction.getMatchUser(loginUser.getId());
}
String photoAddress = "";
String myMatch = "<a href=\"join.jsp\">我要参赛</a>";
if (matchUser != null && !"".equals(matchUser.getPhoto())){
	// 只要照片没上传，就不算正式参赛
	myMatch = "<a href=\"vote.jsp?uid=" + matchUser.getUserId() + "\">我的赛况</a>";
}
int totalCount = SqlUtil.getIntResult("select count(user_id) from match_user where checked<3",5);
if (totalCount == -1){
	totalCount = 0;
}
// 随机从一个地区中取出前两名
int randomId = 0;
MatchArea tmpArea = null;
int areaCount = MatchAction.areaMap.size();
if (areaCount > 0){
	randomId = RandomUtil.nextInt(MatchAction.areaMap.size());
	tmpArea = MatchAction.getArea(StringUtil.toInt("" + MatchAction.getAreaList().get(randomId)));
	if (tmpArea != null){
		randomList = MatchAction.service.getMatchUserList(" area_id=" + tmpArea.getId() + " order by vote_count desc,user_id desc limit 2");
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="超级乐后选拔赛"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%><img src="img/logo.gif" alt="o" /><br/>
<%=myMatch%>|<a href="focus.jsp">我的关注</a>|<a href="/jcforum/forum.jsp?forumId=11091">靓女论坛</a><br/>
目前已有<%=totalCount%>位靓女参与选拔赛<br/>
<%=action.getTopTrendsByRandom()%>
==<a href="area.jsp"><%=MatchAction.CHS_NUM[MatchAction.areaMap.size()]%>大地区靓女排行榜</a>==<br/>
<%if (randomList != null && randomList.size() > 0){
	for (int i = 0 ; i < randomList.size() ; i++){
		matchUser = (MatchUser)randomList.get(i);
		if (matchUser != null){
			user = UserInfoUtil.getUser(matchUser.getUserId());
			if (user != null){
				%><%=i+1%>.<%=user.getNickNameWml()%><br/><img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/><%if (matchInfo != null && matchInfo.getFalg()==1){%><a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><%}%><br/><%
			}
		}
	}
}%>
==参赛靓女推荐.<a href="rank.jsp?t=3">更多</a>==<br/>
<%if(MatchAction.recoList.size() > 0){
	num = 1;
	start = MatchAction.getRandomInt(0,MatchAction.recoList.size(),5);
	if (MatchAction.recoList.size() <= 5){
		end = MatchAction.recoList.size() - 1;
	} else {
		end = start + 5;
	}
	for (int i = end ; i >= start ; i--){
		matchUser = MatchAction.getMatchUser(((Integer)MatchAction.recoList.get(i)).intValue());
		if (matchUser != null){
			userBase = CreditAction.getUserBaseBean(matchUser.getUserId());
			if (userBase != null){
				%><%=num%>.<a href="/user/ViewUserInfo.do?userId=<%=matchUser.getUserId()%>"><%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%></a>(<%=userBase.getAge()%>岁,<%=action.getPlaceString(userBase.getCity())%> )<a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><br/><%
			}
			num++;
		}
	}
}
%>
<a href="rank.jsp">>>查看所有参赛靓女</a><br/>
ID搜索<br/>
<input name="uid" format="*N"/><br/>
<anchor>
	搜索
	<go href="vote.jsp" method="post">
		<postfield name="uid" value="$uid" />
	</go>
</anchor><br/>
<a href="goods.jsp">礼品兑换</a>|<a href="/chat/hall.jsp">靓女聊天</a>|<a href="area.jsp">地区排行</a><br/>
==投票动态.<a href="fans.jsp">粉丝排行</a>==<br/>
<%if (trendsList != null && trendsList.size() > 0){
for (int i = 0 ; i < trendsList.size() ; i++){
	trend = (MatchTrends)trendsList.get(i);
	if (trend != null){
		tmp = StringUtil.toWml(trend.getContent());
		if (tmp != null && !"".equals(tmp)){
			tmp = tmp.replace("%l","<a href=\"/user/ViewUserInfo.do?userId=" + trend.getLeftUid() + "\">" + UserInfoUtil.getUser(trend.getLeftUid()).getNickNameWml() + "</a>");
			if (tmp.indexOf("%r") > 0){
				tmp = tmp.replace("%r","<a href=\"/user/ViewUserInfo.do?userId=" + trend.getRightUid() + "\">" + UserInfoUtil.getUser(trend.getRightUid()).getNickNameWml() + "</a>");
			}
			%><%=tmp%><br/><%
		}
	}
}
}%>
<a href="rank.jsp?t=3">官方推荐</a>|<a href="shop.jsp">购买选票</a>|<a href="rank.jsp?t=1">最新靓女</a><br/>
<%=myMatch%>|<a href="area.jsp">地区排行</a>|<a href="/jcforum/forum.jsp?forumId=11090">靓女答疑</a><br/>
<a href="join.jsp">参赛规则</a>|<a href="/jcforum/forum.jsp?forumId=11091">靓女论坛</a>|<a href="/chat/hall.jsp">靓女聊天</a><br/>
<%
} else {
%><%=tip%><br/><a href="/lswjs/index.jsp">返回导航</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>