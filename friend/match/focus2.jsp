<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,net.joycool.wap.bean.home.HomeDiaryBean,net.joycool.wap.bean.home.HomeUserBean,net.joycool.wap.cache.util.HomeCacheUtil"%>
<%! static int COUNT_PRE_PAGE = 5; %>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
boolean isInMatch = false;
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
} else {
	if (MatchAction.getMatchUser(loginUser.getId()) != null){
		isInMatch = true;
	}
}
String tip = "";
String title = "我目前关注的靓女";
List list = null;
UserBean user = null;
MatchArea matchArea = null;
MatchUser matchUser = null;
MatchFocus matchFocus = null;
HomeUserBean homeUser = null;
UserBean onlineUser = null;
boolean hasNextPage = false;
int p = action.getParameterInt("p");
if (p < 0) p = 0;
int serial = 0;	// 序号
int end = 0;
int start = p * COUNT_PRE_PAGE;
int uid = action.getParameterInt("uid");
int type = action.getParameterInt("t");
if (!isInMatch && type != 0) type = 0;	// 除错.未参赛用户不能查看"关注我的人".
if (type == 0){
// 我关注的靓女排名
	list = MatchAction.service.getFocusList(" uid=" + loginUser.getId() + " order by id desc limit " + start + "," + (COUNT_PRE_PAGE + 1));
} else {
// 关注我的人
	title = "目前关注我的靓女";
	list = MatchAction.service.getFocusList(" uid2=" + loginUser.getId()  + " order by id desc limit " + start + "," + (COUNT_PRE_PAGE + 1));
}
if (list.size() > COUNT_PRE_PAGE){
	end = list.size() - 1;
	hasNextPage = true;
} else { 
	end = list.size();
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (type == 0){
// 我关注的靓女排名
	if (list != null && list.size() > 0){
		serial = start;
		for (int i = 0 ; i < end ; i++){
			matchFocus = (MatchFocus)list.get(i);
			if (matchFocus != null){
				matchUser = MatchAction.getMatchUser(matchFocus.getUid2());
				if (matchUser != null){
					user = UserInfoUtil.getUser(matchUser.getUserId());
					if (user != null){
						onlineUser = (UserBean)OnlineUtil.getOnlineBean(user.getId() + "");
						matchArea = MatchAction.getArea(matchUser.getAreaId());
						serial++;
						%><%=serial%>.<a href="vote.jsp?uid=<%=matchUser.getUserId()%>"><%=user.getNickNameWml()%></a>[<%if(onlineUser == null){%>离线<%}else{%><%=PositionUtil.getPositionName(user.getId() + "", onlineUser.getPositionId())%><%}%>]<%=matchUser.getVoteCount()%>靓点<br/>目前<%=matchArea!=null?matchArea.getAreaNameWml():"其他"%>地区排名<%=action.cityRank2(matchUser)%>名<br/><a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>&amp;c=1">取消关注</a><br/><%
					}
				}
			}
		}
		if (start > 0){
			%><a href="focus2.jsp?t=<%=type%>&amp;p=<%=p-1%>">上一页</a><%if(hasNextPage){%>|<%}else{%><br/><%}%><%
		}
		if (hasNextPage){
			%><a href="focus2.jsp?t=<%=type%>&amp;p=<%=p+1%>">下一页</a><br/><%
		}
	} else {
		%>您还没有关注过任何靓女,赶快找个心仪的参赛靓女,并加关注吧:),查看参赛资料就可找到添加功能.<br/><%
	}
} else {
// 关注我的人
	if (list != null && list.size() > 0){
		serial = start;
		for (int i = 0 ; i < end ; i++){
			matchFocus = (MatchFocus)list.get(i);
			if (matchFocus != null){
				user = UserInfoUtil.getUser(matchFocus.getUid());
				if (user != null){
					onlineUser = (UserBean)OnlineUtil.getOnlineBean(user.getId() + "");
					serial++;
					%><%=serial%>.<a href="vote.jsp?uid=<%=user.getId()%>"><%=user.getNickNameWml()%></a>[<%if(onlineUser == null){%>离线<%}else{%><%=PositionUtil.getPositionName(user.getId() + "", onlineUser.getPositionId())%><%}%>]<br/><%
				}
			}
		}
		if (start > 0){
			%><a href="focus2.jsp?t=<%=type%>&amp;p=<%=p-1%>">上一页</a><%if(hasNextPage){%>|<%}else{%><br/><%}%><%
		}
		if (list.size() > COUNT_PRE_PAGE){
			%><a href="focus2.jsp?t=<%=type%>&amp;p=<%=p+1%>">下一页</a><br/><%
		}
	} else {
		%>目前还没有人关注你.<br/><%
	}
}
} else {
%><%=tip%><br/><%
}
%><a href="focus.jsp">返回我的关注</a><br/><a href="index.jsp">返回选美首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>