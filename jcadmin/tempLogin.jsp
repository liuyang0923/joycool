<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*, net.joycool.wap.util.*,net.joycool.wap.cache.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");

String mobile = request.getParameter("mobile");
long number = 1;
try{
	if(mobile!=null){
	    number = Long.parseLong(mobile);
	}    
}catch(Exception e){
	number = 0;
}
if(mobile==null || mobile.length()!=11 || number==0){
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head><meta http-equiv="Cache-Control" content="max-age=0" forua="true"/></head>
<card title="登录乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String backTo = (String)request.getAttribute("backTo");
if(backTo == null){
	backTo=(String)session.getAttribute("loginReturnURL");
	if(backTo==null){
	backTo = BaseAction.URL_PREFIX;
	}
}
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
%>
<% 
if(mobile!=null){ 
%>请输入正确的手机号。<br/>
<% } %>
为了安全，以后需要ID和密码登陆乐酷。用如果您不知道自己的ID和密码，请输入手机号，即可查看，请记牢哦：<br/>
输入手机号(必须是移动用户):<input type="text" name="mobile"  maxlength="11" value="13"/><br/>
<anchor title="确定">确定
    <go href="/jcadmin/tempLogin.jsp?backTo=<%=backTo%>" method="post">
    <postfield name="mobile" value="$mobile"/>
    </go>
</anchor><br/>
<a href="/user/login.jsp">直接登陆</a><br/>
<a href="/user/register.jsp?backTo=<%=backTo%>">我要注册</a><br/>
若您的用户名和密码遗忘或丢失，请拨打010－62790306－831或839或842，010-62790282让管理员帮助找回。<br/>
<a href="<%=(oldBackTo.replace("&", "&amp;"))%>" title="进入">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%
}
else{
    String key = mobile;
    List userIdList = (List) OsCacheUtil.get(key,
		OsCacheUtil.USER_BY_MOBILE_GROUP,
		OsCacheUtil.USER_BY_MOBILE_FLUSH_PERIOD);
    if(userIdList==null){
	    userIdList = SqlUtil.getIntList("select id from user_info where mobile='" + mobile + "'", Constants.DBShortName);
	    if(userIdList==null){
			userIdList = new ArrayList();
		}
	    OsCacheUtil.put(key, userIdList,
			OsCacheUtil.USER_BY_MOBILE_GROUP);
    }
    if(userIdList==null){
    	userIdList = new ArrayList();
    }
    
    UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
	if(loginUser!=null && loginUser.getId()>0){
		Integer userId = new Integer(loginUser.getId());
		if(!userIdList.contains(userId)){
			userIdList.add(userId);
		}
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head><meta http-equiv="Cache-Control" content="max-age=0" forua="true"/></head>
<card title="登录乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String backTo = (String)request.getAttribute("backTo");
if(backTo == null){
	backTo=(String)session.getAttribute("loginReturnURL");
	if(backTo==null){
	backTo = BaseAction.URL_PREFIX;
	}
}
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
%><%
if(userIdList.size()==0){
%>
对不起，我们无法根据这个手机号，取得您的ID和密码。<br/>
重新输入手机号(必须是移动用户):<input type="text" name="mobile"  maxlength="11" value="13"/><br/>
<anchor title="确定">确定
    <go href="/jcadmin/tempLogin.jsp?backTo=<%=backTo%>" method="post">
    <postfield name="mobile" value="$mobile"/>
    </go>
</anchor><br/>
您也可以请拨打010－62790306－809或811或812让管理员帮助找回。<br/>
<a href="<%=(oldBackTo.replace("&", "&amp;"))%>" title="进入">返回</a><br/>
<%
}
else{
	String content = "您在乐酷有" + userIdList.size() + "个帐号.";
	for (int i = 0; i < userIdList.size(); i++) {
		Integer userId = (Integer) userIdList.get(i);
		if (userId == null)
			continue;

		UserBean user = UserInfoUtil.getUser(userId
				.intValue());
		String passWord = user.getPassword();
		if(mobile.startsWith("134") || mobile.startsWith("135")||
				mobile.startsWith("136")||mobile.startsWith("137")||
				mobile.startsWith("138")||mobile.startsWith("139")||
				mobile.startsWith("159")){
		    passWord = Encoder.decrypt(passWord);
		    String send = "ID" + (i + 1) + ":" + userId + "密码"
				    + passWord + ";";
		    if ((content.length() + send.length()) >= 60) {
				// 发送短信息
				SmsUtil.send(SmsUtil.CODE, content, mobile,
						SmsUtil.TYPE_SMS);
				content = send;
				if (i == (userIdList.size() - 1)) {
					SmsUtil.send(SmsUtil.CODE, content,
							mobile, SmsUtil.TYPE_SMS);
				}
			} else {
				content = content + send;
				if (i == (userIdList.size() - 1)) {
					SmsUtil.send(SmsUtil.CODE, content,
							mobile, SmsUtil.TYPE_SMS);
				}
			}
		}
	}
	%>
	您的用户ID和密码已经发送到您的手机上，请注意接收和保存.<br/>
	用户ID(数字):<br/><input type="text" name="uid"  maxlength="20" value="v"/>
<br/>
密码：<br/><input type="text" name="password"  maxlength="20" value="v"/>
<br/>
    <anchor title="确定">登录
    <go href="/user/Login.do?backTo=<%=backTo%>" method="post">
    <postfield name="uid" value="$uid"/>
    <postfield name="password" value="$password"/>
    </go>
    </anchor><br/>   
为了安全，以后需要ID和密码登陆乐酷。用如果您不知道自己的ID和密码，请输入手机号，即可查看，请记牢哦：<br/>
	<%
}	
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%
}
%>