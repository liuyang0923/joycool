<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
//if (loginUser == null){
//	response.sendRedirect("/user/login.jsp");
//	return;
//}
int uid = 0;
int cityRank = 0;
String tip = "";
String call = "她";
int fansCount = 0;
boolean isMySelf = false;
String photoAddress = "";
UserBean user = null;
MatchUser matchUser = null;
UserBase userBase = null;
MatchVoted voted = null;
MatchRank rank = null;
MatchTodayVote todayVote = null;
MatchFans fans = null;
MatchRes res = null;
List resList = MatchAction.getResList();
List haveList = new ArrayList();
if (loginUser != null){
	fans = MatchAction.getMatchFans(loginUser.getId());
}
int good[] = null;
if (fans != null){
	good = fans.getGood();
} else {
	good = new int[8];
}
MatchInfo matchInfo = MatchAction.getCurrentMatch();
if (matchInfo != null && matchInfo.getFalg() == 1){
	uid = action.getParameterInt("uid");
	if (uid <= 0){
		tip = "该用户没有参赛.";
	} else {
		matchUser = MatchAction.getMatchUser(uid);
		 if (matchUser == null){
			tip = "该用户没有参赛.";
		 } else {
			 if (loginUser != null && uid == loginUser.getId()){
			 	isMySelf = true;
			 	call = "我";
			 }
			 userBase = CreditAction.getUserBaseBean(matchUser.getUserId());
			 rank = MatchAction.service.getMatchRank(" user_id=" + matchUser.getUserId());
			 if (userBase == null){
				 tip = "该用户没有填写交友可信度信息.";
			 }
			 fansCount = SqlUtil.getIntResult("select count(id) from match_fans_ab where right_uid=" + uid,5);
		 }
	}
	cityRank = action.cityRank2(matchUser);
} else {
	tip  = "比赛已经结束,不可以投票.";
}

%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="为她投票"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><a href="/wapIndex.jsp">乐酷首页</a>><a href="index.jsp">乐后活动</a><br/>
<%user = UserInfoUtil.getUser(matchUser.getUserId());
if (user != null){
%>为<%=user.getNickNameWml()%>投票:<br/><%	
}%>
<%if (matchInfo != null && matchInfo.getFalg() == 1){
%><%if(isMySelf){%>赠送自己<%}else{%>你要赠送她<%}%>:<br/>
<%for (int i = 0 ; i < good.length - 1 ; i++){
	res = MatchAction.getRes(i);
	if (res != null){
		if (good[i] > 0){
			haveList.add(i + "");
			%><%=isMySelf?"我":"您"%>有<%=MatchAction.getRes(i).getResName() + good[i]%>个,投<%=call%><input name="g<%=i%>" format="*N"/>个<br/><%
		} else {
			%><%=isMySelf?"我":"您"%>有<%=MatchAction.getRes(i).getResName()%>0个,投<%=call%>0个<br/><%
		}
	}
}%>
<anchor>
	为<%=call%>投票
	<go href="doVote.jsp?uid=<%=uid%>" method="post">
		<%if (haveList != null && haveList.size() > 0){
			String tmp = "";
			for (int i = 0 ; i < haveList.size() ; i++){
				tmp = (String)haveList.get(i);
				if (tmp != null && !"".equals(tmp)){
					%><postfield name="g<%=tmp%>" value="$g<%=tmp%>" /><%
				}
			}
		}%>
	</go>
</anchor>|<a href="shop.jsp">购买投票靓点</a><br/>
<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">查看<%=call%>的参赛资料</a><br/><%
}
} else {
%><%=tip%><br/><%
}
%><a href="index.jsp">返回乐后首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>