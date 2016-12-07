<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.UserBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,jc.credit.*"%>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
MatchInfo matchInfo = MatchAction.getCurrentMatch();
UserBean loginUser = action.getLoginUser();
UserBase userBase = null;
int back = 0;
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
String tip = "";
String backString = "";
if (matchInfo != null && matchInfo.getFalg()==1){
	if (loginUser.getGender() == 1){
		tip = "男性用户无法参赛.";
	} else {
		userBase = CreditAction.getUserBaseBean(loginUser.getId());
	}
	back = action.getParameterInt("b");
	if (back == 0){
		// 返回参赛信息
		backString = "join.jsp?t=1";
	} else if (back == 1){
		// 返回我的比赛信息
		backString = "vote.jsp?uid=" + loginUser.getId();
	}
	int submit = action.getParameterInt("s");
	if (submit == 1 && loginUser.getGender() == 0){
		MatchUser matchUser = MatchAction.getMatchUser(loginUser.getId());
		if (matchUser == null){
			if (userBase == null && userBase.getCity() <= 0){
				tip = "您没有在可信度中填写所在省市区域.";
			} else {
				// 新建参赛用户
				matchUser = new MatchUser();
				matchUser.setUserId(loginUser.getId());
				matchUser.setPhoto("o.gif");		//photo can not be null.
				matchUser.setPhoto2("o.gif");	//photo2 can not be null.
				matchUser.setEncounce("");	//enounce can not be null.
				matchUser.setTotalVote(0);
				matchUser.setTotalConsume(0);
				matchUser.setPlaceId(userBase.getCity());	//我的所在地区
				// 如果大区ID=0的话就尝试写入大区信息。
				if (matchUser.getAreaId() == 0 && matchUser.getPlaceId() > 0){
					matchUser.setAreaId(SqlUtil.getIntResult("select flag from credit_city where id=" + matchUser.getPlaceId(),5));
				}
				action.addMatchUser(matchUser);	
				MatchAction.matchUserCache.put(new Integer(matchUser.getUserId()), matchUser);
			}
		} 
		if(!action.addEnounce(action.getParameterNoEnter("eno"))){
			tip = (String)request.getAttribute("tip");
		} else {
			response.sendRedirect(backString);
			return;
		}
	}	
} else {
	tip = "本期比赛已经结束,不可修改宣言.";
	backString = "my.jsp";
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="参赛宣言"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>填写您的参赛宣言<br/>
号召朋友为你投票,写出自己的靓点,参赛宣言书写自己的个性宣言!(参赛宣言限20字以内)<br/>
<input name="eno" maxlength="20"/><br/>
<anchor>
	确认
	<go href="enounce.jsp?s=1&amp;b=<%=back%>" method="post">
		<postfield name="eno" value="$eno" />
	</go>
</anchor><br/>
<%
} else {
%><%=tip%><br/><%
}
%>
<a href="<%=backString%>">返回</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>