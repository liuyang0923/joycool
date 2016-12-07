<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.UserBase,jc.credit.CreditAction,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
String tip = "";
List userList = null;
UserBean user = null;
PagingBean paging = null;
MatchArea matchArea = null;
MatchUser matchUser = null;
MatchInfo matchInfo = MatchAction.getCurrentMatch();
int id = action.getParameterInt("id");
int more = action.getParameterInt("m");
if (id <= 0){
	tip = "该地区不存在.";
} else {
	matchArea = MatchAction.service.getArea(" id=" + id);
	if (matchArea == null){
		tip  = "该地区不存在.";
	} else {
		paging = new PagingBean(action, matchArea.getCount(), COUNT_PRE_PAGE, "p");
		userList = MatchAction.service.getMatchUserList(" area_id=" + matchArea.getId() + " order by vote_count desc,user_id desc limit "+paging.getStartIndex()+"," + COUNT_PRE_PAGE);
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="地区排行"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if (more == 0){
%><%=matchArea.getAreaNameWml()%>排行榜<br/>
<a href="area.jsp">查看其它排行区</a><br/>
==<%=matchArea.getAreaNameWml()%>靓女排行==<br/>
<%if (userList != null && userList.size() > 0){
	for (int i = 0 ; i < userList.size() ; i++){
		matchUser = (MatchUser)userList.get(i);
		if (matchUser != null){
			user = UserInfoUtil.getUser(matchUser.getUserId());
			if (user != null){
				%><%=i+paging.getStartIndex()+1%>.<%=user.getNickNameWml()%><br/><img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/><%if (matchInfo != null && matchInfo.getFalg()==1){%><a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><%}%><br/><%
			}
		}
	}%><%=paging.shuzifenye("area2.jsp?id=" + matchArea.getId(), true, "|", response)%><%
}
%>
<a href="area2.jsp?m=1&amp;id=<%=matchArea.getId()%>">地区说明</a><br/>
<a href="area.jsp">查看其它排行区</a><br/>
<a href="index.jsp">返回乐后首页</a><br/><%
} else {
// 地区说明
%><%=matchArea.getAreaNameWml()%>说明:<br/><%=matchArea.getDescribeWml()%><br/><a href="area2.jsp?id=<%=matchArea.getId()%>">返回排名页</a><br/><%
}
} else {
%><%=tip%><br/><a href="/lswjs/index.jsp">返回导航</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>