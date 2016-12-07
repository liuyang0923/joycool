<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Date,net.joycool.wap.util.DateUtil,net.joycool.wap.util.UserInfoUtil,net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.friend.FriendBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
String result = "";
String tip = "";
UserStatusBean user = null;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
net.joycool.wap.util.ForbidUtil.ForbidBean forbid = net.joycool.wap.util.ForbidUtil.getForbid("home",loginUser.getId());
if (forbid != null){
	result = "forbid";
	tip = "您目前处于封禁状态,暂时无法修改交友资料,请到\"警察局\"查看封禁原因.";
} else {
	action.editFriend(request);
	result=(String) request.getAttribute("result");
	user=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("failure".equals(result)){
%>
<card title="修改个人家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/friend/inputRegisterInfo.jsp">创建我的交友信息</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
FriendBean friend=(FriendBean)request.getAttribute("friend");
%>
<card title="修改个人交友信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
为了让朋友们更好的认识您，跟您交流，希望您能留下详细的交友信息，最好是上传一张漂亮的形象照片哦:)<br/>
真实姓名：<input name="name"  maxlength="10" value="<%=StringUtil.toWml(friend.getName())%>"/><br/>
<%if(user.getMark()==0){%>
性别：<%=loginUser.getGender()==1?'男':'女'%><br/>
		<%}%>
年龄：<input name="age"  maxlength="10" value="<%=loginUser.getAge()%>"/><br/>
<%--//liuyi 2007-01-22 生日信息 start --%>
<%  
String birthday = friend.getBirthday();
Date date = DateUtil.parseDate(birthday, DateUtil.normalDateFormat);
int year = (date!=null)?(1900 + date.getYear()):1986;
int month = (date!=null)?(date.getMonth()+1):1;
int day = (date!=null)?date.getDate():1;
%>
生日：<input name="year"  maxlength="4" value="<%= year %>"/>年 
<input name="month"  maxlength="2" value="<%= month %>"/>月
<input name="day"  maxlength="2" value="<%= day %>"/>日（必填，每月只能修改一次）<br/>
<%--select name="open" value="<%= loginUser.getBirthdayOpenMark() %>">
    <option value="0">仅对好友公开</option>
    <option value="1">对所有人公开</option>
    <option value="2">不公开</option>
</select><br/--%>
<%--//liuyi 2007-01-22 生日信息 end --%>
手机号码：<input name="mobile"  maxlength="11" value="<%=friend.getMobile()%>"/><br/>
所在城市：<input name="city"  maxlength="10" value="<%=StringUtil.toWml(friend.getCity())%>"/><br/>
星座：<select name="constellation" value="<%=friend.getConstellation()%>">
    <option value="0">白羊座</option>
    <option value="1">金牛座</option>
    <option value="2">双子座</option>
    <option value="3">巨蟹座</option>
    <option value="4">狮子座</option>
    <option value="5">室女座</option>
    <option value="6">天秤座</option>
    <option value="7">天蝎座</option>
    <option value="8">人马座</option>
    <option value="9">摩羯座</option>
    <option value="10">宝瓶座</option>
    <option value="11">双鱼座</option>
	</select><br/>
身高：<input name="height"  maxlength="3" value="<%=friend.getHeight()%>"/>厘米<br/>
体重：<input name="weight"  maxlength="3" value="<%=friend.getWeight()%>"/>KG<br/>
职业：<input name="work"  maxlength="100" value="<%=StringUtil.toWml(friend.getWork())%>"/><br/>
性格：<select name="personality" value="<%=friend.getPersonality()%>">
    <option value="0">温柔体贴</option>
    <option value="1">活泼开朗</option>
    <option value="2">古灵精怪</option>
    <option value="3">憨厚老实</option>
    <option value="4">豪情奔放</option>
    <option value="5">天真淳朴</option>
	</select><br/>
婚姻状况：<select name="marriage" value="<%=friend.getMarriage()%>">
    <option value="0">未婚</option>
    <option value="1">已婚</option>
    <option value="2">离异</option>
    <option value="3">丧偶</option>
	</select><br/>
交友目的：<select name="aim" value="<%=friend.getAim()%>">
    <option value="0">恋人</option>
    <option value="1">知己</option>
    <option value="2">玩伴</option>
    <option value="3">解闷</option>
    <option value="4">其他</option>
	</select><br/>
择友条件：<input name="friendCondition"  maxlength="100" value="<%=StringUtil.toWml(friend.getFriendCondition())%>"/><br/>
    <anchor title="确定">完成提交
    <go href="/friend/editCommit.jsp" method="post">
    <postfield name="homeName" value="$homeName"/>
	<postfield name="name" value="$name"/>
	<postfield name="gender" value="<%=loginUser.getGender()%>"/>
	<postfield name="age" value="$age"/>
	<postfield name="mobile" value="$mobile"/>
	<postfield name="city" value="$city"/>
	<%--//liuyi 2007-01-22 生日信息 start --%>
	<postfield name="year" value="$year"/>
	<postfield name="month" value="$month"/>
	<postfield name="day" value="$day"/>
	<postfield name="open" value="$open"/>
	<%--//liuyi 2007-01-22 生日信息 end --%>
	<postfield name="constellation" value="$constellation"/>
	<postfield name="height" value="$height"/>
	<postfield name="weight" value="$weight"/>
	<postfield name="work" value="$work"/>
	<postfield name="personality" value="$personality"/>
	<postfield name="marriage" value="$marriage"/>
	<postfield name="aim" value="$aim"/>
	<postfield name="friendCondition" value="$friendCondition"/>
    </go>
    </anchor><br/>
<a href="/friend/friendInfo.jsp">返回个人交友信息</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%} else if ("forbid".equals(result)) {
// 被封
%><card title="修改个人交友信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>