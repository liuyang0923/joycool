<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.UserBean"%><%@ page import="java.net.URLEncoder"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%!
static net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();%><%
response.setHeader("Cache-Control","no-cache");
String password=(String)session.getAttribute("oripassword");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>

<%
String requestResult = (String)request.getAttribute("result");
if(requestResult!=null){%>
    <%if(requestResult.equals("failure")){%>
<card title="乐酷免费注册">
<p align="left">
<%//=BaseAction.getTop(request, response)%>
[提示]<%=request.getAttribute("tip")%><br/>
<%if(password==null){%>【乐酷免费注册】<%}else{%>【填写资料】<%}%><br/>
昵称(3到10位中文):<input name="nickname" maxlength="15" value=""/><br/>
<%if(password==null){%>初始密码过于简单,请输入新密码(4到15位):<input name="password2"  maxlength="15" value=""/><br/><%}%>
性别:<select name="gender" value="0">
<option value="1">男</option>
<option value="0">女</option>
</select><br/>
年龄:<input name="age" format="*N" maxlength="3" value="20"/><br/>
自我介绍:<input name="selfIntroduction"  maxlength="100" value=""/><br/>
<anchor title="确定">确认提交
<go href="/user/Register.do" method="post">
	<postfield name="password" value="<%if(password==null){%>$password2<%}else{%><%=password%><%}%>"/>
	<postfield name="nickname" value="$nickname"/>
	<postfield name="gender" value="$gender"/>
	<postfield name="age" value="$age"/>
	<postfield name="selfIntroduction" value="$selfIntroduction"/>
</go>
</anchor><br/>
<anchor title="back"><prev/>后退</anchor><br/>
<%String backTo = (String) request.getAttribute("backTo");
backTo = (backTo.replace("&", "&amp;"));%>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    <%}%>

    <%if(requestResult.equals("success")){%>
<%
String backTo = (String) request.getAttribute("backTo");
backTo = (backTo.replace("&", "&amp;"));
int userId = StringUtil.toInt(request.getParameter("userID"));
UserBean user = UserInfoUtil.getUser(userId);
if(user==null) {
out.clearBuffer();
response.sendRedirect(("/lswjs/index.jsp"));
return;
}

%>
<card title="乐酷免费注册" ontimer="<%=response.encodeURL(backTo)%>">
    <timer value="50"/>
<p align="left">
<%//=BaseAction.getTop(request, response)%>
【乐酷免费注册】<br/>
注册成功！5秒后自动跳转。<br/>
记住您的ID：<%= user.getId() %><br/>
密码：<%= StringUtil.toWml(Encoder.decrypt(user.getPassword())) %><br/>

	<a href="<%=backTo%>" title="进入">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    <%}%>
<%}	//request.getAttribute("result")!=null
	else {
	
%>
<% String userMobile = (String)session.getAttribute("userMobile");
//userMobile = "";
if(userMobile==null&&SecurityUtil.isInnerIp(request.getRemoteAddr())){
	userMobile=request.getParameter("adminreg");
	session.setAttribute("userMobile",userMobile);
}/*
if(userMobile == null) {	// 暂停校验手机号，接口不安全
	request.getRequestDispatcher("/verify.jsp").forward(request,response);
	return;
}else */if(userMobile != null &&userMobile.length() > 5){
List userIdList = SqlUtil.getIntList("select a.id from user_info a,user_status b where a.mobile='" + userMobile + "' and b.user_id=a.id order by b.last_login_time desc limit 4", Constants.DBShortName);
if(userIdList == null || userIdList.size() == 0){
%>
<card title="乐酷免费注册">
<p align="left">
<%//=BaseAction.getTop(request, response)%>
<%
String backTo = request.getParameter("backTo");
if(backTo == null){
	backTo = "/wapIndex.jsp";
}
if(session.getAttribute(Constants.LOGIN_USER_KEY) != null){
	out.clearBuffer();
	response.sendRedirect((backTo));
	return;
}
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
//String registerId=(String)session.getAttribute("registerId");
//int userID=StringUtil.toInt(registerId);
//if(registerId==null){
//userID=SequenceUtil.getSeq("userID");
//session.setAttribute("registerId",userID+"");
//}
//int userID = SequenceUtil.getSeq("userID");
%>
<%if(password==null){%>【乐酷免费注册】<%}else{%>【填写资料】<%}%><br/>
昵称(3到10位中文):<input name="nickname" maxlength="15" value=""/><br/>
<%if(password==null){%>初始密码过于简单,请输入新密码(4到15位):<input name="password2"  maxlength="15" value=""/><br/><%}%>
性别:<select name="gender" value="0">
<option value="1">男</option>
<option value="0">女</option>
</select><br/>
年龄:<input name="age" format="*N" maxlength="3" value="20"/><br/>
自我介绍:<input name="selfIntroduction"  maxlength="100" value=""/><br/>
<a href="/Column.do?columnId=12623">乐酷用户协议和游戏规则</a><br/>
<anchor title="确定">同意并确认提交
<go href="/user/Register.do?backTo=<%=backTo%>" method="post">
	<postfield name="password" value="<%if(password==null){%>$password2<%}else{%><%=password%><%}%>"/>
	<postfield name="nickname" value="$nickname"/>
	<postfield name="gender" value="$gender"/>
	<postfield name="age" value="$age"/>
	<postfield name="selfIntroduction" value="$selfIntroduction"/>
</go>
</anchor><br/>
【温馨提示】<br/>
注册即赠送新人卡,使用后可获得神秘礼物,让你赌场、交友、游戏都happy!<br/>
【注意事项】<br/>
禁止现金交易虚拟货币,谨防欺诈!<br/>
乐酷不收取任何信息费和流量费.GPRS上网所需的流量费请咨询当地运营商(移动联通等)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="乐酷免费注册">
<p align="left">
您的手机号已注册了以下ID(<%=userIdList.size()%>个):<br/>
<%Iterator iter = userIdList.iterator();
int i = 0;
while(iter.hasNext()){
Integer userId = (Integer)iter.next();
UserBean user = UserInfoUtil.getUser(userId.intValue());
%>

ID<%=++i%>:<%=userId%>(<%=user.getNickNameWml()%><a href="/user/autologin.jsp?id=<%=userId%>">登陆</a>)<br/>
密码:<%=Encoder.decrypt(user.getPassword())%><br/>
<%
net.joycool.wap.bean.UserSettingBean userSetting = service.getUserSetting("user_id=" + user.getId());
if(userSetting != null && userSetting.getBankPw() != null && userSetting.getBankPw().length() > 0){
%>银密:<%=userSetting.getBankPw()%><br/><%
}%>
<%}%>
同样的手机号不能再注册其他ID了：）请注意保管现有的ID和密码。<br/>
<a href="/user/login.jsp">登陆</a><br/>
<a href="/wapIndex.jsp">返回乐酷首页</a><br/>
</p>
</card>
<%}}else{

if(session.getAttribute(Constants.LOGIN_USER_KEY) != null){
	out.clearBuffer();
	response.sendRedirect("/wapIndex.jsp");
	return;
}
	if(request.getParameter("find")!=null){	// 找回密码
%><card title="找回密码">
<p align="left">【短信修改密码】<br/>
如果忘记了密码,想要直接修改,只需要用注册时的手机号发送修改短信到10690329013076即可,指定的密码必须都是数字!!.例如,如果要修改密码为999888,那么发送的短信内容为"修改密码999888".(点击<a href ="sms:10690329013076?body=修改密码">这里</a>直接开始编辑短信)<br/>
如果要修改银行密码为234234,那么发送的短信内容为"修改银行密码234234"(点击<a href ="sms:10690329013076?body=修改银行密码">这里</a>直接开始编辑短信).成功发送后系统会在1分钟内完成修改(不论是否有回复短信,修改都会完成).<br/>
!!注意:短信内容是系统自动识别,请严格按规范输入,甚至是符号也不能增加<br/><a href="user/login.jsp">&gt;&gt;重新登陆</a><br/>
<%
	}else{
%><card title="乐酷免费注册">
<p align="left">
如何注册乐酷账号<br/>
————————<br/>
【注册方法1】<br/>
请发短信ZC+密码(必须是数字)到10690329013076(点击<a href ="sms:10690329013076?body=ZC">这里</a>直接开始编辑短信)，稍候就能用手机号和该密码<a href="/user/login.jsp?dir=1">登陆</a>，系统不会回复短信。<br/>
【注册方法2】<br/>
请发短信ZC到10690329013076(点击<a href ="sms:10690329013076?body=ZC">这里</a>直接开始编辑短信)，系统会稍候将密码发送到您的手机上。<br/>
收到密码后请点击<a href="/user/login.jsp?dir=1">登陆</a>并输入手机号和密码<br/>
部分联通和电信手机号无法注册<br/>
【<a href="find.jsp">短信修改密码</a>】<br/><br/>
<anchor title="back"><prev/>返回上一页.我想再转转</anchor><br/>
您还可以<a href="/guest/chat.jsp">和在线游客聊天</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
<%}	//request.getAttribute("result")!=null
%>
</wml>