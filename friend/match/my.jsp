<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.*,net.joycool.wap.bean.UserBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
String tip  = "";
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
int cityRank = 0;
String myRank = "仍处于发力阶段!";
String tmp = "";
MatchRank rank = null;
MatchTrends trend = null;
List trendsList = null;
MatchPhotoHistory history = null;
MatchUser matchUser = MatchAction.getMatchUser(loginUser.getId());
UserBase userBase = null;
if (matchUser == null){
	tip = "您不是参赛用户.";
	if (loginUser.getGender() == 0){
		tip += "赶快来报名参赛吧O(∩_∩)O~.";	
	}
} else {
	rank = MatchAction.service.getMatchRank(" user_id=" + matchUser.getUserId());
	if (rank != null){
		if (rank.getId() <= 30){
			myRank = rank.getId() + "";
		}
	}
	userBase = CreditAction.getUserBaseBean(loginUser.getId());
	if (userBase == null){
		tip = "您还没有填写可信度.";
	}
	trendsList = MatchAction.service.getTrendsList(" right_uid=" + loginUser.getId() + " and flag=0 order by id desc limit 5");
}
MatchInfo matchInfo = MatchAction.getCurrentMatch();
int playerCount = (matchInfo != null ? matchInfo.getUserCount() : 0);
cityRank = action.cityRank2(matchUser);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="我的赛况"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){%>
<%if (matchUser.getChecked() == 3){
%>您已被取消资格,详情请咨询管理员.<br/><%
}
%>
<%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%><br/>
目前靓点:<%=matchUser.getVoteCount()%><br/>
目前排名:<%=myRank%><br/>
所在城市:<%=action.getPlaceString(matchUser.getPlaceId())%>,<%if (cityRank <= 10){%>目前高居该地区第<%=cityRank%>名.<%}else{%>目前地区排名第<%=cityRank%>名.<%}%><br/>
<%if(userBase == null){
%>年龄:未填写<br/>生日:未填写<br/>星座:未填写<br/>身高:未填写<br/><%	
} else {
%>年龄:<%=userBase.getAge()!=0?userBase.getAge() + "":"未填写"%><br/>
生日:<%if(userBase.getBirYear()==0 || userBase.getBirMonth()==0 || userBase.getBirDay()==0){%>未填写<%}else{%><%=userBase.getBirYear()%>/<%=userBase.getBirMonth()%>/<%=userBase.getBirDay()%><%}%><br/>
星座:<%=!"error".equals(CreditAction.getAstroString(userBase.getAstro()))?CreditAction.getAstroString(userBase.getAstro()):"未填写"%><br/>
身高:<%=userBase.getStature()!=0?userBase.getStature() + "":"未填写"%><br/>
<%}%>
参赛宣言:<%=matchUser==null?"":matchUser.getEncounceWml()%><br/>
<a href="enounce.jsp?b=1">修改个人宣言</a><br/>
<img src="<%=action.getCurrentPhoto(matchUser,false)%>" alt="o" /><br/>
<a href="uppic.jsp">修改参赛照片</a>|<a href="/friend/credit/credit.jsp">修改交友可信度</a><br/>
==我的粉丝团==<br/>
<%if (trendsList != null && trendsList.size() > 0){
	for (int i = 0 ; i < trendsList.size() ; i++){
		trend = (MatchTrends)trendsList.get(i);
		if (trend != null && trend.getContent() != null){
			tmp = trend.getContent().replace("%l","<a href=\"/user/ViewUserInfo.do?userId=" + trend.getLeftUid() + "\">" + UserInfoUtil.getUser(trend.getLeftUid()).getNickNameWml() + "</a>");
			tmp = tmp.replace("%r","我");
		}
			%><%=DateUtil.sformatTime(trend.getCreateTime())%><%=tmp%><br/><%
		}
  }
%><a href="allFans.jsp?uid=<%=loginUser.getId()%>&amp;b=1">查看我的全部粉丝</a><br/>
<%
} else {
%><%=tip%><br/><%
}
%><a href="index.jsp">返回选美首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>