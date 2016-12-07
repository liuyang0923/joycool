<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.UserBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%! static int COUNT_PRE_PAGE = 5;
	static String rankStr[] = {"","一","二","三","四-六","七-十"};
%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
//if (loginUser == null){
//	response.sendRedirect("/user/login.jsp");
//	return;
//}
MatchInfo matchInfo = MatchAction.getCurrentMatch();
MatchUser matchUser = null;
MatchRank matchRank = null;
if (loginUser != null){
	matchUser = MatchAction.getMatchUser(loginUser.getId());
	matchRank = MatchAction.service.getMatchRank(" user_id=" + loginUser.getId());
}
String tip = "";
MatchGood matchGood = null;
PagingBean paging = null;
List prizeList = null;	//奖品列表
List pointList = null;	//靓点兑换列表
int prizeCount = 0;		//奖品总数
int pointCount = 0;		//靓点兑换总数
int tmp = 0;
int type = action.getParameterInt("t");
if (type < 0 || type > 2){
	type = 0;
}
String title = "商品兑换商城";
//兑换区首页
if (type == 0){
	prizeList = MatchAction.service.getGoodList(" hide=0 and flag in (1,2,3,4,5) order by flag limit 4");
	pointList = MatchAction.service.getGoodList(" hide=0 and flag=0 order by prio desc,id desc limit 3");
	prizeCount = (prizeList.size() == 4 ? prizeCount = 3 : prizeList.size());
	pointCount = (pointList.size() == 3 ? pointCount = 2 : pointList.size());
} else if (type == 1){
//更多奖品
	tmp = SqlUtil.getIntResult("select count(id) from match_good where hide=0 and flag in (1,2,3,4,5)",5);
	paging = new PagingBean(action,tmp,COUNT_PRE_PAGE,"p");
	prizeList = MatchAction.service.getGoodList(" hide=0 and flag in (1,2,3,4,5) order by flag");
	title = "所有大赛奖品";
} else if (type == 2){
//更多靓点奖品
	tmp = SqlUtil.getIntResult("select count(id) from match_good where hide=0 and flag=0",5);
	paging = new PagingBean(action,tmp,COUNT_PRE_PAGE,"p");
	pointList = MatchAction.service.getGoodList(" hide=0 and flag=0 order by prio desc,id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	title = "所有靓点商品";
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){
%><%=action.getTopTrendsByRandom()%><a href="index.jsp">乐后选拔赛首页</a>|<a href="rank.jsp">乐后排行榜</a><br/>
<a href="help.jsp">兑换规则</a>|<a href="elog.jsp">兑换记录</a><br/><%
if (type == 0){
// 兑换首页
%>您本届总靓点是<%=matchUser != null?matchUser.getVoteCount():0%>,可兑换靓点是<%=matchUser != null?matchUser.getCurrentVote2():0%><br/>
==本期大赛奖品展示==<br/>
<%if (prizeList != null && prizeList.size() > 0){
	for(int i = 0 ; i < prizeCount ; i++){
		matchGood = (MatchGood)prizeList.get(i);
		if (matchGood != null){
			%><a href="exch.jsp?gid=<%=matchGood.getId()%>">第<%=rankStr[matchGood.getFlag()]%>名奖品-<%=matchGood.getGoodNameWml()%></a><br/><img src="<%=MatchAction.ATTACH_GOOD_URL_ROOT + matchGood.getPhoto()%>" alt="o" /><br/><%
		}
	}
	if (prizeList.size() > prizeCount){
		%><a href="goods.jsp?t=1">更多奖品</a><br/><%
	}
}%>
==靓点兑换区==<br/>
<%if (pointList != null && pointList.size() > 0){
	for (int i = 0 ; i < pointCount ; i++){
		matchGood = (MatchGood)pointList.get(i);
		if (matchGood != null){
			%><a href="exch.jsp?gid=<%=matchGood.getId()%>"><%=matchGood.getGoodNameWml()%></a><br/><img src="<%=MatchAction.ATTACH_GOOD_URL_ROOT + matchGood.getPhoto()%>" alt="o" /><br/><%=matchGood.getPrice2()%>靓点.<a href="exch.jsp?gid=<%=matchGood.getId()%>">兑换</a><br/><%
		}
	}
	if (pointList.size() > pointCount){
		%><a href="goods.jsp?t=2">更多靓点兑换品</a><br/><%
	}
}
} else if (type == 1){
// 更多奖品
	if (prizeList != null && prizeList.size() > 0){
		for(int i = 0 ; i < prizeList.size() ; i++){
			matchGood = (MatchGood)prizeList.get(i);
			if (matchGood != null){
				%><a href="exch.jsp?gid=<%=matchGood.getId()%>">第<%=rankStr[matchGood.getFlag()]%>名奖品-<%=matchGood.getGoodNameWml()%></a><br/><img src="<%=MatchAction.ATTACH_GOOD_URL_ROOT + matchGood.getPhoto()%>" alt="o" /><br/><%
			}
		}
	}%><%=paging!=null?paging.shuzifenye("goods.jsp?t=1",true,"|",response):""%><%
} else if (type == 2){
// 更多靓点奖品
	if (pointList != null && pointList.size() > 0){
		for (int i = 0 ; i < pointList.size() ; i++){
			matchGood = (MatchGood)pointList.get(i);
			if (matchGood != null){
				%><a href="exch.jsp?gid=<%=matchGood.getId()%>"><%=matchGood.getGoodNameWml()%></a><br/><img src="<%=MatchAction.ATTACH_GOOD_URL_ROOT + matchGood.getPhoto()%>" alt="o" /><br/><%=matchGood.getPrice2()%>靓点.<a href="exch.jsp?gid=<%=matchGood.getId()%>">兑换</a><br/><%
			}
		}
	}%><%=paging!=null?paging.shuzifenye("goods.jsp?t=2",true,"|",response):""%><%
}
%><a href="help.jsp">兑换规则</a><br/>
<a href="index.jsp">乐后选拔赛首页</a>|<a href="rank.jsp">乐后排行榜</a><br/><%
} else {
%><%=tip%><br/><%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>