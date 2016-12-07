<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.UserBase,jc.credit.CreditAction,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
String tip = "";
UserBean user = null;
MatchUser matchUser = null;
MatchArea matchArea = null;
MatchInfo matchInfo = MatchAction.getCurrentMatch();
List areaList = MatchAction.getAreaList();
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="地区排行"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%>各地区乐后排名:<br/>
<% if (areaList != null && areaList.size() > 0){
	for (int i = 0 ; i < areaList.size() ; i++){
		matchArea = MatchAction.getArea(StringUtil.toInt(areaList.get(i) + ""));
		if (matchArea != null){
			matchUser = MatchAction.service.getMatchUser(" area_id=" + matchArea.getId() + " order by vote_count desc,user_id desc limit 1");
			%><%=i+1%>.<a href="area2.jsp?id=<%=matchArea.getId()%>"><%=matchArea.getAreaNameWml()%>[<%=matchArea.getCount()%>名靓女]</a><br/><%
			if (matchUser != null){
				user = UserInfoUtil.getUser(matchUser.getUserId());
				if (user != null){
					%><%=user.getNickNameWml()%><br/><img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/><%if (matchInfo != null && matchInfo.getFalg()==1){%><a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">投票</a>|<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><%}%><br/><%
				}
			}
		}
	}
}
%>
<a href="index.jsp">返回乐后首页</a><br/>
<%
} else {
%><%=tip%><br/><a href="/lswjs/index.jsp">返回导航</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>