<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,jc.credit.*,net.joycool.wap.bean.UserBean"%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
//if (loginUser == null){
//	response.sendRedirect("/user/login.jsp");
//	return;
//}
String tip = "";
MatchInfo matchInfo = MatchAction.getCurrentMatch();;
UserBase userBase = null;
MatchUser matchUser = null;
if (loginUser != null){
	userBase = CreditAction.getUserBaseBean(loginUser.getId());
	matchUser = MatchAction.getMatchUser(loginUser.getId());
}
int type = action.getParameterInt("t");
if (type!=0 && loginUser != null && loginUser.getGender() == 1){
	tip = "抱歉,您是男性用户,现不能参与乐后活动!!";
}
if (type == 1 && matchUser != null && !"".equals(matchUser.getPhoto())){
	type = 0;
}
// 该用户已参赛并正在审核
//if (matchUser != null && matchUser.getChecked() == 0 && !"".equals(matchUser.getPhoto())){
//	type = 2;
//}
if (type == 1 && loginUser != null){
	UserInfo userInfo = CreditAction.service.getUserInfo(" user_id=" + loginUser.getId());
	if (userInfo == null || userInfo.getTotalPoint() < 60){
		tip = "您没有填写可信度或可信度没有达到60%,无法参赛.现在马上去填写<a href=\"/friend/credit/credit.jsp\">交友可信度</a>.";
	}
}
String title = "";
if (type == 0){
	title = "参赛规则";
} else if (type == 1){
	title = "确认参赛信息";
} else {
	title = "参赛审核";
}
if (type != 0){
	if (matchInfo != null){
		if (matchInfo.getFalg() == 2){
			tip = "本次比赛已结束,请等待下次比赛.";
		}
	} else {
		tip = "比赛尚未开始,敬请期待.";
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
switch (type){
case 0:{
//参赛规则
%>超级乐后(简称"乐后")选拔赛说明<br/>
<%if (matchInfo != null){
%><%=matchInfo.getTitleWml()%>,于<%=DateUtil.formatDate1(new Date(matchInfo.getStartTime()))%>开始,<%=MatchAction.dayDiffStr(matchInfo.getStartTime(),matchInfo.getEndTime())%>与<%=DateUtil.formatDate1(new Date(matchInfo.getEndTime()))%>结束!<%
}%>
获胜的女用户,未必是最漂亮的,但一定是最有人气、最有魅力、最多人捧场的,不是吗?欢迎所有女性用户热情参与,也欢迎大家带新的女用户来乐酷参赛！展现你们的靓丽风采吧！<br/>
活动奖品丰厚,前10名免费获得乐酷大奖！所有参赛选手,均可在活动后用获得的选票(靓点),免费兑换实物礼品.选票越多,能兑换的奖品越多哦!<br/>
乐后选拔赛的排名以投票获得靓点的多少进行排名.参赛规则如下:<br/>
1.必须是在社区注册过的女性.<br/>
2.交友可信度达到60%以上(<a href="/friend/credit/credit.jsp">完善交友可信度</a>)<br/>
3.上传一张近期的生活照为参赛照片(可在参赛后随时更换,以吸引更多粉丝)<br/>
4.比赛中若发现用户不是女性,或恶意捣乱,将直接取消参赛资格和所有选票靓点.<br/>
5.更多参赛规则请查看<a href="more.jsp">详细规则说明</a>.<br/>
<%if (loginUser != null && loginUser.getGender()==0 && (matchUser == null || "".equals(matchUser.getPhoto()))){
	%><a href="join.jsp?t=1">我要参赛</a><br/><%
}%>
<a href="index.jsp">返回乐后首页</a><br/>
<%
break;
}
case 1:{
//参赛信息页
%><a href="join.jsp">参赛规则</a>|<a href="index.jsp">参赛区首页</a><br/>
您的参赛资料如下<br/>
所在城市:<%=action.getPlaceString(userBase!=null?userBase.getCity():-1)%><br/>
<%if(userBase == null){
%>年龄:未填写<br/>生日:未填写<br/>星座:未填写<br/>身高:未填写<br/><%	
} else {
%>年龄:<%=userBase.getAge()!=0?userBase.getAge() + "":"未填写"%><br/>
生日:<%if(userBase.getBirYear()==0 || userBase.getBirMonth()==0 || userBase.getBirDay()==0){%>未填写<%}else{%><%=userBase.getBirYear()%>/<%=userBase.getBirMonth()%>/<%=userBase.getBirDay()%><%}%><br/>
星座:<%=!"error".equals(CreditAction.getAstroString(userBase.getAstro()))?CreditAction.getAstroString(userBase.getAstro()):"未填写"%><br/>
身高:<%=userBase.getStature()!=0?userBase.getStature() + "":"未填写"%><br/>
<%}%>
参赛宣言:<%=matchUser==null?"":matchUser.getEncounceWml()%><a href="enounce.jsp">修改</a><br/>
以上信息为您的交友可信度信息,若与现实不符,请及时修改您的交友可信度.<br/>
<a href="/friend/credit/credit.jsp">完善交友可信度</a><br/>
<!--<a href="/home/homePhotoCat.jsp">从家园选择参赛照片</a><br/> -->
<a href="uppic.jsp">从手机选择照片上传</a><br/>
<%
break;
}
case 2:{
//已参赛并正在审核
%>您的参赛信息已经提交,等待审核当中,请耐心等待,图片需要在10分钟内审核,注2-4点GM不审核.<br/>
<a href="index.jsp">返回乐后首页</a><br/>
<%
}
}
} else {
%><%=tip%><br/><a href="index.jsp">返回乐后首页</a><br/><%		
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>