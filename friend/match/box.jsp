<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,java.text.DecimalFormat"%>
<%!static int COUNT_PRE_PAGE = 5;
   static DecimalFormat numFormat = new DecimalFormat("0.0");%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
PagingBean paging = null;
List voteList = null;
List trendsList = null;
//MatchVote vote = null;
MatchTrends trend = null;
String tip = "";
String tmp = "";
int limit = 5;
int p = action.getParameterInt("p");
int start = p*limit;
int end = 0;
int type = action.getParameterInt("t");
if (type > 2 || type < 0){
	type = 0;
}
int resId = action.getParameterInt("rid");
MatchRes res = MatchAction.getRes(resId);
if (type == 1 && res == null){
	tip ="没有找到此物品.";
}
//paging = new PagingBean(action,100,COUNT_PRE_PAGE,"p");
trendsList = MatchAction.service.getTrendsList("left_uid=" + loginUser.getId() + " and flag=0 order by id desc limit " + start + ",6");
end = trendsList.size();
//voteList = MatchAction.service.getVoteList(" left_uid=" + loginUser.getId() + " order by id desc limit "  + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="我的奢侈品"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){
if (type == 1){
//物品说明
%><%=StringUtil.toWml(res.getResName())%><br/><img src="img/<%=res.getPhoto()%>" alt="o" /><br/><%=StringUtil.toWml(res.getResName())%>,销售价格<%if (res.getCur() == 0){%><%=StringUtil.bigNumberFormat2(res.getPrices())%>乐币<%}else{%><%=numFormat.format(res.getPrices() / 100f)%>酷币<%}%>/个,可兑换靓点<%=res.getPoint()%>分.<br/>
<a href="box.jsp">返回上一级</a><br/>
<%
} else if (type == 2){
//我的赠送记录
if(trendsList.size() > limit){
	end = trendsList.size() - 1;
}
if (trendsList != null && trendsList.size() > 0){
	for (int i = 0 ; i < end ; i++){
		trend = (MatchTrends)trendsList.get(i);
		if (trend != null && trend.getContent() != null){
			tmp = trend.getContent().replace("%l","");
			tmp = tmp.replace("%r","<a href=\"/user/ViewUserInfo.do?userId=" + trend.getRightUid() + "\">" + UserInfoUtil.getUser(trend.getRightUid()).getNickNameWml() + "</a>");
		%><%=DateUtil.sformatTime(trend.getCreateTime())%><%=tmp%><br/><%
		}
	}
	if (trendsList.size() > 5){
		%><a href="box.jsp?t=<%=type%>&amp;p=<%=p+1%>">下一页</a><%
	}
	if (p > 0){
		%>|<a href="box.jsp?t=<%=type%>&amp;p=<%=p-1%>">上一页</a><%
	}
	%><br/><%
}%><a href="box.jsp">返回上一级</a><br/><%
} else {
//礼品盒
%>我目前可以赠送的奢侈品:<br/>
<%=action.statMyGoods("box.jsp?t=1")%><br/>
<a href="shop.jsp">购买更多奢侈品</a><br/>
<a href="box.jsp?t=2">查看我的赠送记录</a><br/>
<%
}
} else {
%><%=tip%><br/><%
}
%><a href="index.jsp">返回乐后首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>