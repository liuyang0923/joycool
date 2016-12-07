<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*, net.joycool.wap.util.*,net.joycool.wap.cache.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");

String mobile = request.getParameter("mobile");
if(mobile!=null && (mobile.startsWith("86") && mobile.length()>2) ){
	mobile = mobile.substring(2);
}
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
<card title="登录乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
if(returnUrl==null){
	returnUrl = "ViewFriends.do";
}	
String backTo = returnUrl.replace("&", "&amp;");
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
%>
<% 
if(mobile!=null){ 
%>请输入正确的手机号。<br/>
<% } %>
如果您担心忘记的ID和密码，请输入手机号，即可收到短信保存哦。<br/>
输入手机号(必须是移动用户):<input type="text" name="mobile"  maxlength="11" value="13"/><br/>
<anchor title="确定">确定
    <go href="Login.do?backTo=<%=backTo%>" method="post">
    <postfield name="message" value="$mobile"/>
    </go>
</anchor><br/>
<a href="register.jsp?backTo=<%=backTo%>">我要注册</a><br/>
<a href="<%=(oldBackTo.replace("&", "&amp;"))%>">返回</a><br/>
若您的用户名和密码遗忘或丢失，请拨打13347907223让管理员帮助找回。<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%
}
else{

	String sql = "select a.id from user_info a,user_status b where a.mobile='"
							+ mobile + "' and b.user_id=a.id order by b.last_login_time desc limit 4";
    List userIdList = SqlUtil.getIntList(sql);
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
<card title="登录乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
if(returnUrl==null){
	returnUrl = "/wapIndex.jsp";
}	
String backTo = returnUrl.replace("&", "&amp;");
%><%
{
	boolean flag = false;
	String content = "您在乐酷有" + userIdList.size() + "个帐号.";
	for (int i = 0; i < userIdList.size(); i++) {
		Integer userId = (Integer) userIdList.get(i);
		if (userId == null)
			continue;

		UserBean user = UserInfoUtil.getUser(userId
				.intValue());
		String passWord = user.getPassword();
		if(true/*mobile.startsWith("134") || mobile.startsWith("135")||
				mobile.startsWith("136")||mobile.startsWith("137")||
				mobile.startsWith("138")||mobile.startsWith("139")||
				mobile.startsWith("159")*/){
			flag = true;
			if(passWord!=null){
		        passWord = Encoder.decrypt(passWord);
			}
			else{
				passWord = "";
			}
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
	<% if(flag){ %>
	您的用户ID和密码已经发送到您的手机上，请注意接收和保存.<br/>	
	<% }else{ %>
	该手机不是移动用户.<br/>
	<%}%>  
    <a href="<%=backTo%>" title="进入">返回</a><br/>
    若您的用户名和密码遗忘或丢失，请拨打13347907223让管理员帮助找回。<br/>
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