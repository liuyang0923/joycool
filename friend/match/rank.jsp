<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%!static int COUNT_PRE_PAGE = 5;%>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
// 更新排行榜
MatchAction.statCompose();
String tip = "";
String title = "";
String photoAddress = "";
int type = action.getParameterInt("t"); //0:按靓点多少排名 1:最新佳人 2:前十名 3:乐后推荐
List list = null;
MatchRank rank = null;
MatchUser matchUser = null;
MatchInfo matchInfo = MatchAction.getCurrentMatch();
int playerCount = matchInfo.getUserCount();
UserBean loginUser = action.getLoginUser();
if (loginUser != null){
	rank = MatchAction.service.getMatchRank(" user_id=" + loginUser.getId());
	matchUser = MatchAction.getMatchUser(loginUser.getId());
}
if (type < 0 || type > 3){
	type = 0;
}
int totalCount = SqlUtil.getIntResult("select count(user_id) from match_user where checked<3",5);
if (totalCount == -1){
	totalCount = 0;
}
PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
switch (type){
case 0:{
// 按靓点多少排名
title = "全部排名";
list = MatchAction.service.getMatchUserList(" checked<3 order by vote_count desc,user_id desc limit "  + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
break;
}
case 1:{
// 最新乐后
title = "最新乐后";
list = MatchAction.service.getMatchUserList(" checked=1 order by create_time desc limit "  + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
break;
}
case 2:{
// 乐后十强
title = "乐后十强";
paging = new PagingBean(action,MatchAction.getTopTenList().size(),COUNT_PRE_PAGE,"p");
break;
}
case 3:{
// 官方推荐
title = "官方推荐";
paging = new PagingBean(action,MatchAction.getRecoList().size(),COUNT_PRE_PAGE,"p");
break;
}
}
String myMatch = "<a href=\"join.jsp\">我要参赛</a>";
if (matchUser != null && !"".equals(matchUser.getPhoto())){
	// 只要照片没上传，就不算正式参赛
	myMatch = "<a href=\"vote.jsp?uid=" + matchUser.getUserId() + "\">我的赛况</a>";
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title %>"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=action.getTopTrendsByRandom()%><%
if (loginUser != null && rank != null){
%>您现在的靓点是<%=rank.getVoteCount()%>排名第<%=rank.getId()%>位,要再接再厉哦.<br/><%	
} else {
%>目前已有<%=totalCount%>位乐后参与比赛!<br/><%
}%>
<%=myMatch%>|<a href="join.jsp">参赛规则</a>|<a href="/jcforum/forum.jsp?forumId=11090">靓女答疑</a><br/>
<a href="rank.jsp?t=1">最新靓女</a>|<a href="rank.jsp?t=3">官方推荐</a>|<a href="fans.jsp">粉丝排行</a><br/>
--------------------<br/>
<%
switch (type){
	case 0:{
		// 全部排名
		for (int i = 0 ; i < list.size() ; i++){
			matchUser = (MatchUser)list.get(i);
			if (matchUser != null){
				%>第<%=i+paging.getStartIndex()+1 %>名:<%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%>(<%=matchUser.getVoteCount()%>靓点)<br/>
				<img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/>
				<%if (matchInfo != null && matchInfo.getFalg()==1){%><a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><br/><%}%>
				<%
			}
		}%><%=paging!=null?paging.shuzifenye("rank.jsp",false,"|",response):""%><%
		break;
	}
	case 1:{
		// 最新乐后
		for (int i = 0 ; i < list.size() ; i++){
			matchUser = (MatchUser)list.get(i);
			if (matchUser != null){
				%><%=i+paging.getStartIndex()+1 %>.<%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%>(<%=matchUser.getVoteCount()%>靓点)<br/>
				<img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/>
				<%if (matchInfo != null && matchInfo.getFalg()==1){%><a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><br/><%}%>
				<%
			}
		}%><%=paging!=null?paging.shuzifenye("rank.jsp?t=1",true,"|",response):""%><%
		break;
	}
	case 2:{
		// 乐后十强
		for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
			rank = (MatchRank)MatchAction.getTopTenList().get(i);
			if (rank != null){
				matchUser = MatchAction.getMatchUser(rank.getUserId());
				if (matchUser != null){
					%><%=i + 1%>.<%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%>(<%=matchUser.getVoteCount()%>靓点)<br/>
					<img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/>
					<%if (matchInfo != null && matchInfo.getFalg()==1){%><a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><br/><%}%>
					<%
				}
			}
		}%><%=paging!=null?paging.shuzifenye("rank.jsp?t=2",true,"|",response):""%><%
		break;
	}
	case 3:{
		// 官方推荐
		for (int i = paging.getStartIndexR() ; i > paging.getEndIndexR() ; i--){
			matchUser = MatchAction.getMatchUser(((Integer)MatchAction.getRecoList().get(i)).intValue());
			if (matchUser != null){
				%><%=paging.getCurrentPageIndex()*paging.getCountPerPage()+paging.getStartIndexR()-i+1%>.<%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%>(<%=matchUser.getVoteCount()%>靓点)<br/>
				<img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/>
				<%if (matchInfo != null && matchInfo.getFalg()==1){%><a href="vote.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><br/><%}%>
				<%
			}
		}%><%=paging!=null?paging.shuzifenye("rank.jsp?t=3",true,"|",response):""%><%
		break;
	}
}
%>ID搜索<br/>
<input name="uid" format="*N"/><br/>
<anchor>
	搜索
	<go href="vote2.jsp" method="post">
		<postfield name="uid" value="$uid" />
	</go>
</anchor><br/><%
} else {
%><%=tip%><br/><br/><%
}%><a href="index.jsp">返回乐后首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>