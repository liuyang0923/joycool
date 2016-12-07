<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.match.MatchAction,jc.match.MatchUser,jc.match.MatchRank,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   int uid = action.getParameterInt("uid");
   MatchUser matchUser = null;
   MatchRank rank = null;
   UserInfo userInfo = null;
   UserBean user = null;
   int userId = 0;
   String tip = "";
   UserBean loginUser = action.getLoginUser();
   if (loginUser == null){
	   response.sendRedirect("/user/login.jsp");
	   return;
   }
   if (uid == 0 || uid == loginUser.getId()){
   		userInfo = action.getUserInfo(loginUser.getId());
   		matchUser = MatchAction.getMatchUser(loginUser.getId());
   		rank = MatchAction.service.getMatchRank(" user_id=" + loginUser.getId());
   } else {
	    if (UserInfoUtil.getUser(uid) == null){
	    	tip = "用户ID:" + uid + "不存在.";
	    } else {
	    	//userInfo = action.service.getUserInfo(" user_id=" + uid);
	    	if (action.getLoginUser().getFriendList().contains(uid + "")){
	    		userInfo = action.getUserInfo(uid);	
	    	} else {
	    		tip = "对方还不是您的好友,不可查看可信度.";
	    	}
	    }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if(uid==0){
// 查看我自己的可信度
%>您目前的交友可信度为<%=userInfo.getTotalPoint()%>%<br/>温馨提示:您的可信度分值越高,其他人就会对您越信任,越愿意向您敞开心扉,提升交友成功率!以下信息越完整,可信度分值就越高:<br/>
<a href="navi.jsp">信息完整度(40)</a>:<%=userInfo.getIntactPoint()%><br/>
<a href="phone.jsp">手机号码认证(40)</a>:<%=userInfo.getPhonePoint()%><br/>
<a href="net.jsp">网站信用担保(10)</a>:<%=userInfo.getNetPoint()%><br/>
<a href="cons.jsp">消费累积(5)</a>:<%=userInfo.getConPoint()%><br/>
可靠玩家打分:<%=userInfo.getPlayerCount() != 0?userInfo.getPlayerPoint() / userInfo.getPlayerCount():0%><br/>
最近点评人:<%=action.getGradeUserString(action.getLoginUser().getId(),3)%><br/>
<a href="more.jsp">更多点评人</a><br/>
<a href="flist.jsp">给别人去打分</a><br/>
美女选拔赛火热进行当中,所有美女均可参赛,还等什么?快快参与吧!(交友可信度达到60%方可参赛.)<br/>
<%if (matchUser == null){
	%><a href="/friend/match/index.jsp">我要参赛</a><br/><%
} else if(rank != null){
	%>您目前排名是:<%=rank.getId()%><br/><%
}%>
<a href="/lswjs/index.jsp">[返回上一页]</a><br/>
<%	
} else {
// 查看别人的可信度
%><%=UserInfoUtil.getUser(userInfo.getUserId()).getGenderText()%>目前的交友可信度为<%=userInfo.getTotalPoint()%>%<br/>
<a href="navi2.jsp?uid=<%=uid%>">信息完整度(40)</a>:<%=userInfo.getIntactPoint()%><br/>
手机号码认证(40):<%=userInfo.getPhonePoint()%><br/>
网站信用担保(10):<%=userInfo.getNetPoint()%><br/>
消费累积(5):<%=userInfo.getConPoint()%><br/>
可靠玩家打分:<%=userInfo.getPlayerCount() != 0?userInfo.getPlayerPoint() / userInfo.getPlayerCount():0%><br/>
可信度高于80的用户的点评:<a href="grade.jsp?uid=<%=uid%>">给<%=UserInfoUtil.getUser(uid).getGenderText()%>打分</a><br/>
最近点评人:<%=action.getGradeUserString(uid,3)%><br/>
<a href="more.jsp?uid=<%=uid %>">更多点评人</a><br/>
<a href="credit.jsp">我的可信度</a><br/>
<a href="credit.jsp">[返回可信度首页]</a><br/>
<%	
}
} else {
%><%=tip%><br/>
<%if (uid == action.getLoginUser().getId()){
	%><a href="flist.jsp">返回</a><br/><%
} else {
	%><a href="/user/ViewUserInfo.do?userId=<%=uid%>">返回TA的资料</a><br/><a href="credit.jsp">返回我的可信度</a><br/><%
}
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>