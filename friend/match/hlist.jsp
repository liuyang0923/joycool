<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.UserBase,jc.credit.CreditAction,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%! static int COUNT_PRE_PAGE = 5; %>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
UserBean user = null;
MatchUser matchUser = null;
MatchHistory matchHistory = null;
MatchAreaHistory matchAreaHistory = null;
MatchInfo matchInfo = MatchAction.getCurrentMatch();
String tip = "";
if (matchInfo != null){
	if (matchInfo.getFalg() != 2){
		response.sendRedirect("index.jsp");
		return;
	}
}
List list = null;
List areaList = null;
PagingBean paging = null;
int type = action.getParameterInt("t");
if (type < 0 || type > 3){
	type = 0;
}
if (type == 0){
// 历史赛事列表
	int count = SqlUtil.getIntResult("select count(id) from match_info where flag=2",5);
	paging = new PagingBean(action,count,COUNT_PRE_PAGE,"p");
	list = MatchAction.service.getInfoList(" flag=2 order by id desc limit "  + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
} else if (type == 1){
// 详细列表
	int matchId = action.getParameterInt("iid");
	if (matchId <= 0){
		tip = "您要查看的赛事不存在.";
	} else {
		matchInfo = MatchAction.service.getInfo(" id=" + matchId);
		if (matchInfo == null){
			tip = "您要查看的赛事不存在.";
		} else {
			int areaId = action.getParameterInt("aid");
			// 如果areaId>0，说明要读取某一场比赛某一地区的列表如果不，则返回要查询的比赛地区列表
			if (areaId > 0){
				matchAreaHistory = MatchAction.service.getAreaHistory(" match_id=" + matchId + " and area_id=" + areaId);
			} else {
				areaList = MatchAction.service.getAreaHistoryList(" match_id=" + matchId);
			}
			if (matchAreaHistory == null){
				// 有比赛，但是还没有分地区
				paging = new PagingBean(action,matchInfo.getUserCount(),COUNT_PRE_PAGE,"p");
				list = MatchAction.service.getHistoryList(" match_id=" + matchInfo.getId() + " order by rank asc limit "  + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
			} else {
				// 显示某一地区的排名
				type = 3;
				paging = new PagingBean(action,matchAreaHistory.getCount(),COUNT_PRE_PAGE,"p");
				list = MatchAction.service.getHistoryList(" area_id=" + matchAreaHistory.getAreaId() + " order by vote_count desc,user_id desc limit "+paging.getStartIndex()+"," + COUNT_PRE_PAGE);
			}
			if (areaList != null && areaList.size() > 0){
				// 显示列表
				type = 2;
			}
		}
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="历史赛事"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if (type == 0){
// 	历史赛事列表
if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		matchInfo = (MatchInfo)list.get(i);
		if (matchInfo != null){
			%><%=i + paging.getCurrentPageIndex() + 1 %>.<a href="hlist.jsp?t=1&amp;iid=<%=matchInfo.getId()%>"><%=matchInfo.getTitleWml()%></a><br/><%
		}
	}
}%><a href="history.jsp">返回乐后选拔赛首页</a><br/><%
} else if (type == 1){
// 详细列表	
%>开始日期:<%=DateUtil.formatDate2(matchInfo.getStartTime())%><br/>
结束日期:<%=DateUtil.formatDate2(matchInfo.getEndTime())%><br/>
共有<%=matchInfo.getUserCount()%>名靓女参赛,以下是赛事回顾.<br/>
<%if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		matchHistory = (MatchHistory)list.get(i);
		if (matchHistory != null){
			%>第<%=matchHistory.getRank()%>名:<%=UserInfoUtil.getUser(matchHistory.getUserId()).getNickNameWml()%>(<%=matchHistory.getVoteCount()%>靓点)<br/>
			<img src="<%=action.getCurrentAddress(matchHistory.getPhotoFrom()) + matchHistory.getPhoto2()%>"  alt="o" /><br/>
			<a href="/chat/post.jsp?toUserId=<%=matchHistory.getUserId()%>">与她聊天</a>|<a href="/home/home.jsp?userId=<%=matchHistory.getUserId()%>">她的家园</a>|<a href="/user/OperFriend.do?add=1&amp;friendId=<%=matchHistory.getUserId()%>">与她交友</a><br/><%
		}
	}%><%=paging!=null?paging.shuzifenye("hlist.jsp?t=1&amp;iid=" + matchInfo.getId(),true,"|",response):""%><%
}%><a href="hlist.jsp">返回上一页</a><br/><%
} else if (type == 2){
// 显示列表
%>开始日期:<%=DateUtil.formatDate2(matchInfo.getStartTime())%><br/>
结束日期:<%=DateUtil.formatDate2(matchInfo.getEndTime())%><br/>
共<%=MatchAction.CHS_NUM[areaList.size()]%>大地区<%=matchInfo.getUserCount()%>名靓女参赛,以下是赛事回顾.<br/>
<%
if (areaList != null && areaList.size() > 0){
	for (int i = 0 ; i < areaList.size() ; i++){
		matchAreaHistory = (MatchAreaHistory)areaList.get(i);
		if (matchAreaHistory != null){
			%><a href="hlist.jsp?t=1&amp;iid=<%=matchAreaHistory.getMatchId()%>&amp;aid=<%=matchAreaHistory.getAreaId()%>"><%=matchAreaHistory.getAreaNameWml()%>[<%=matchAreaHistory.getCount()%>名靓女]</a><br/><%
		}
	}
}
%><a href="hlist.jsp">返回上一页</a><br/><%
} else if (type == 3){
// 显示某场比赛某一地区的排名
%>开始日期:<%=DateUtil.formatDate2(matchInfo.getStartTime())%><br/>
结束日期:<%=DateUtil.formatDate2(matchInfo.getEndTime())%><br/>
<%if (matchAreaHistory != null){
%><%=matchAreaHistory.getAreaNameWml()%>[<%=matchAreaHistory.getCount()%>名靓女]<br/><%
}%>
<%if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		matchHistory = (MatchHistory)list.get(i);
		if (matchHistory != null){
			user = UserInfoUtil.getUser(matchHistory.getUserId());
			matchUser = MatchAction.getMatchUser(user.getId());
			if (user != null && matchUser != null){
				%><%=i+paging.getStartIndex()+1%>.<%=user.getNickNameWml()%><br/><img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/><a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><br/><%
			}
		}
	}%><%=paging.shuzifenye("hlist.jsp?t=1&amp;iid="  + matchAreaHistory.getMatchId() + "&amp;aid=" + matchAreaHistory.getAreaId(), true, "|", response)%><%
}%>
<a href="hlist.jsp?t=1&amp;iid=<%=matchAreaHistory.getMatchId()%>">返回上一页</a><br/><%
}
} else {
%><%=tip%><br/><a href="/lswjs/index.jsp">返回导航</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>