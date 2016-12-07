<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.action.user.*,net.joycool.wap.action.money.*,net.joycool.wap.framework.JoycoolSessionListener,net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
if (1==1) {
	BaseAction.sendRedirect("/user/login.jsp", response);
	return;
}
%>
<%
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
//liuyi 2007-01-24 登录跳转页面bug修改 start
if(returnUrl==null || returnUrl.indexOf("log")!=-1 || returnUrl.indexOf("auto")!=-1){
	returnUrl = BaseAction.URL_PREFIX;
}	
//liuyi 2007-01-24 登录跳转页面bug修改 end	
returnUrl = returnUrl.replace("&", "&amp;");
String backTo = returnUrl;

String mobile = (String) session.getAttribute("userMobile");
if (mobile == null||mobile.length()<10) {  //取不到手机号
	BaseAction.sendRedirect("/user/register.jsp", response);
	return;
}
else{  //判断老号码
	int id = 0;
	Integer userId = (Integer) OsCacheUtil.get(mobile,
			OsCacheUtil.USER_ID_GROUP,
			OsCacheUtil.USER_ID_FLUSH_PERIOD);
	if (userId == null) {
		String sql = "select id from user_info where mobile='"
				+ mobile + "'";
		id = SqlUtil.getIntResult(sql, Constants.DBShortName);
		OsCacheUtil.put(mobile, new Integer(id),
					OsCacheUtil.USER_ID_GROUP);
	}
	else{
		id = userId.intValue();
	}
	if(id>0){ //老手机号
		//response.sendRedirect(("/user/login.jsp"));
		BaseAction.sendRedirect("/user/login.jsp", response);
		return;
	}
}

//判断是否已经登录
if(session.getAttribute(Constants.LOGIN_USER_KEY) != null){
	response.sendRedirect((backTo));
	return;
}

String action = request.getParameter("action");
//自动注册
if("do".equals(action)){
	RegisterAction registerAction = new RegisterAction();
	boolean flag = registerAction.autoRegister(request);
	//String registerId=(String)session.getAttribute("registerId");
	//int userId=StringUtil.toInt(registerId);
	//int userId = SequenceUtil.getSeq("userID");
	int userId = StringUtil.toInt(request.getParameter("userID"));
	UserBean user = UserInfoUtil.getUser(userId);
	if(flag && user!=null){  		
			session.setAttribute("userMobile", "fttodeath");
			session.setAttribute("allowVisit", "true");
			JoycoolSessionListener.updateOnlineUser(request, user);
			%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head><meta http-equiv="Cache-Control" content="max-age=0" forua="true"/></head>
<%//lbj_12_16_start
//原来的链接地址是：backTo
%>
<card title="乐酷免费注册" ontimer="<%=response.encodeURL(backTo)%>">
    <timer value="60"/>
<p align="left">
<%//=BaseAction.getTop(request, response)%>
注册成功！（3秒钟跳转）<br/>
记住您的ID：<%= user.getId() %><br/>
密码：<%= StringUtil.toWml(Encoder.decrypt(user.getPassword())) %><br/>
<%//lbj_12_16_start
//原来的链接地址是：(backTo)
%>
<a href="<%= (backTo) %>" title="进入">直接跳转</a><br/>
<%//lbj_12_16_end%>
<a href="<%= ("/enter/index.jsp") %>" title="进入">存书签，以后直接登陆乐酷</a><br/>
<a href="<%= ("/user/sendUserInfo.jsp") %>" title="进入">将ID和密码短信发送手机上</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
			<%
	}
	else{
		String tip = (String)request.getAttribute("tip");
		if(tip==null)tip="注册失败!";
		%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head><meta http-equiv="Cache-Control" content="max-age=0" forua="true"/></head>
<card title="乐酷免费注册">
<p align="left">
<%= tip %><br/>
<anchor title="back"><prev/>返回</anchor><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>		
		<%
	}
}
//填写注册信息
else{
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
//String registerId=(String)session.getAttribute("registerId");
//int userID=0;
//if(registerId==null){
//	registerId="" + SequenceUtil.getSeq("userID");
//    session.setAttribute("registerId",registerId);
//}
//userID=StringUtil.toInt(registerId);
int userID = SequenceUtil.getSeq("userID");

String[] nickNames = Util.getMaleNicks();
int gender = 1;
if(System.currentTimeMillis()%8<2){
	nickNames = Util.getFemaleNicks();
	gender = 0;
}
String nickName = nickNames[RandomUtil.nextInt(nickNames.length)] + (System.currentTimeMillis()%1002607);
String password = (System.currentTimeMillis() + "").substring(8);  
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head><meta http-equiv="Cache-Control" content="max-age=0" forua="true"/></head>
<card title="乐酷免费注册">
<p align="left">
<%//=BaseAction.getTop(request, response)%>
乐酷快速注册为您分配的资料如下：<anchor title="确定">直接确认开始游戏
    <go href="/jcadmin/autoRegister.jsp?backTo=<%=backTo %>" method="post">
    <postfield name="isAuto" value="true"/>
    <postfield name="action" value="do"/>
	<postfield name="userID" value="<%= userID %>"/>
    <postfield name="password" value="$password"/>
	<postfield name="nickname" value="$nickname"/>
	<postfield name="gender" value="$gender"/>
	<postfield name="age" value="$age"/>
	<postfield name="selfIntroduction" value="$selfIntroduction"/>
    </go>
    </anchor><br/>
您的登陆ID：<%= userID %>(请记住!)<br/>
密码：<input name="password"  maxlength="10" value="<%= password %>"/><br/>
昵称：<input name="nickname"  maxlength="10" value="<%= nickName %>"/><br/>
性别：<select name="gender" value="<%= gender %>">
    <option value="1">男</option>
    <option value="0">女</option>
	</select><br/>
年龄：<input name="age" maxlength="3" value="20"/><br/>
自我介绍：<input name="selfIntroduction"  maxlength="100" value="v"/><br/>
    <anchor title="确定">直接确认开始游戏
    <go href="/jcadmin/autoRegister.jsp?backTo=<%=backTo %>" method="post">
    <postfield name="isAuto" value="true"/>
    <postfield name="action" value="do"/>
	<postfield name="userID" value="<%= userID %>"/>
    <postfield name="password" value="$password"/>
	<postfield name="nickname" value="$nickname"/>
	<postfield name="gender" value="$gender"/>
	<postfield name="age" value="$age"/>
	<postfield name="selfIntroduction" value="$selfIntroduction"/>
    </go>
    </anchor><br/>
    <br/>
<a href="<%= ("/user/login.jsp") %>" title="进入">用其它ID登录</a><br/>    
注册就赠送10000乐币，让你赌场、交友、游戏都happy!<br/>
别忘了记住ID和密码哦！<br/>
若注册的登陆ID和密码遗忘或丢失，请拨打4007790940让管理员帮助找回。<br/>
<a href="<%= (oldBackTo) %>" title="进入">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%}%>