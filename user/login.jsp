<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="javax.servlet.http.Cookie"%><%@ page import="java.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.*,net.joycool.wap.util.encoder.Base64x"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%!
	
%><%
response.setHeader("Cache-Control","no-cache");
int uid = StringUtil.toInt(request.getParameter("uid"));
String autoBookmark = null;	// 自动提示书签地址用于自动登录
UserBean autoUser = null;
if(uid<=0){
	CookieUtil ck = new CookieUtil(request, response); // 根据cookie登录
	String jcal = ck.getCookieValue("jcal");
	if(jcal!=null&&jcal.length()==16) {
		String duid = jcal.substring(5,10);
		uid = Base64x.decodeInt(duid);
		autoUser = UserInfoUtil.getUser(uid);
		if(autoUser!=null){
			String pwdcheck = jcal.substring(0,5) + jcal.substring(10,16);
			String pwdcheck2 = Base64x.encodeMd5(Encoder.decrypt(autoUser.getPassword()));
			if(pwdcheck2.substring(0,11).equals(pwdcheck)){	// 密码校验成功
				autoBookmark="/enter/enter2.jsp?uid="+autoUser.getId()+"&amp;p="+autoUser.getPassword();
			}
		}
	}
}
if("five".equals(request.getAttribute("result"))){
	request.setAttribute("result","failure");
	request.setAttribute("tip","用户ID或手机号输入不正确");
}
int linkId = StringUtil.toId((String) session.getAttribute("linkId"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:present name="result" scope="request">

    <logic:equal name="result" value="failure">
<card title="登录乐酷">
<p align="left">
<%//=BaseAction.getTop(request, response)%>
    <bean:write name="tip" filter="false"/><br/>
<%
String mobile=(String)session.getAttribute("userMobile");
UserBean user=null;
Vector userList=null;

String backTo = (String)request.getAttribute("backTo");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL start
if(backTo == null){
	backTo=(String)session.getAttribute("loginReturnURL");
	if(backTo==null){
	backTo = "/wapIndex.jsp";
	}
}
if(backTo.toLowerCase().indexOf("logout")!=-1){
	backTo = "/wapIndex.jsp";
}
backTo = backTo.replace("&", "&amp;");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL end
if(session.getAttribute(Constants.LOGIN_USER_KEY) != null){
	out.clearBuffer();
	response.sendRedirect((backTo));
	return;
}
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
%>
<%if(user!=null){%>
您的昵称<a href="/jcadmin/autoLogin.jsp?id=<%=user.getId() %>&amp;mobile=<%=user.getMobile() %>"><%= StringUtil.toWml(user.getNickName()) %></a> 
ID:<%=user.getId()%> 密码:<%=Encoder.decrypt(user.getPassword())%><br/>
<%}%>登录方法一:<br/>
<a href="http://qq.joycool.net/enter/qq.jsp?s=<%=linkId%>">
<img src="http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/img/Connect_logo_3.png" alt="QQ登陆" />
</a><br/>
登录方法二:<br/>
用户ID或手机号:<br/><input type="text" name="uid" format="*N" maxlength="11" value="<%if(uid>0){%><%=uid%><%}%>"/>
<br/>
密码：<br/><input type="text" name="password"  maxlength="20" value=""/>
<br/>
<br/>
    <anchor title="确定">登录
    <go href="Login.do?backTo=<%=backTo%>" method="post"><%String red = (String)session.getAttribute("red");if(red!=null){%><postfield name="red" value="<%=red%>"/><%}%>
    <postfield name="uid" value="$uid"/>
    <postfield name="password" value="$password"/>
    </go>
    </anchor>
如果你忘记ID和密码,请点击<a href="/find.jsp">找回密码</a><br/>
<a href="/register.jsp?backTo=<%=backTo%>">注册</a><br/>
若您的用户名和密码遗忘或丢失，请拨打13347907223让管理员帮助找回。<br/>
<a href="<%=(oldBackTo)%>" title="进入">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
<%--//mcq_2006_8_31_一个手机对应N个ID的页面_start--%>    
     <logic:equal name="result" value="userList">
<card title="登录乐酷">
<p align="left">
<%
String backTo = (String)request.getAttribute("backTo");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL start
if(backTo == null){
	backTo=(String)session.getAttribute("loginReturnURL");
	if(backTo==null){
	backTo = "/wapIndex.jsp";
	}
}
if(backTo.toLowerCase().indexOf("logout")!=-1){
	backTo = "/wapIndex.jsp";
}
backTo = backTo.replace("&", "&amp;");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL end
//Vector userMobileList = (Vector)request.getAttribute("userList");  // zhouj:重大漏洞，不能用明码手机号直接取所有id
String mobile=(String)session.getAttribute("userMobile");
if(mobile!=null&&mobile.length()>5){%>
(ID) (密码)<br/><%
List userMobileList = SqlUtil.getIntList("select a.id from user_info a,user_status b where a.mobile='" + mobile + "' and b.user_id=a.id order by b.last_login_time desc limit 4", Constants.DBShortName);
UserBean user=null;
for(int i=0;i<userMobileList.size();i++){
Integer userId = (Integer)userMobileList.get(i);
user = UserInfoUtil.getUser(userId.intValue());%>
<%=i+1%>.<%=user.getId()+" "%><%=Encoder.decrypt(user.getPassword())%><br/>
<%}}%> 
欢迎游玩乐酷游戏社区，更多精彩更多好友，等你登录后拥有哦！
登录方法一:<br/>
<a href="http://qq.joycool.net/enter/qq.jsp?s=<%=linkId%>">
<img src="http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/img/Connect_logo_3.png" alt="QQ登陆" />
</a><br/>
登录方法二:<br/>
用户ID或手机号:<br/><input type="text" name="uid" format="*N" maxlength="11" value="<%if(uid>0){%><%=uid%><%}%>"/>
<br/>
密码：<br/><input type="text" name="password"  maxlength="20" value=""/>
<br/>
<br/>
    <anchor title="确定">登录
    <go href="Login.do?backTo=<%=backTo%>" method="post">
    <postfield name="uid" value="$uid"/>
    <postfield name="password" value="$password"/>
    </go>
    </anchor>
(请记住您的ID和密码,以后登录需要用到哦~)<br/>
<a href="/register.jsp?backTo=<%=backTo%>">注册</a>(需填写材料)<br/>
若您的用户名和密码遗忘或丢失，请拨打13347907223让管理员帮助找回。<br/>
<a href="<%=backTo%>" title="进入">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
<%--//mcq_2006_8_31_一个手机对应N个ID的页面_end--%>

<%--//mcq_2006_8_31_登录用户五步走的开关(第五步)()_start--%>    
<logic:equal name="result" value="five">
<card title="登录乐酷">
<p align="left">
<%//=BaseAction.getTop(request, response)%>
登录乐酷<br/>
--------<br/>
<%
String backTo = (String)request.getAttribute("backTo");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL start
if(backTo == null){
	backTo=(String)session.getAttribute("loginReturnURL");
	if(backTo==null){
	backTo = "/wapIndex.jsp";
	}
}
if(backTo.toLowerCase().indexOf("logout")!=-1 || backTo.toLowerCase().indexOf("auto")!=-1 || backTo.toLowerCase().indexOf("register")!=-1){
	backTo = "/wapIndex.jsp";
}
backTo = backTo.replace("&", "&amp;");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL end
%>
    <bean:write name="tip" filter="false"/><br/>
    输入手机号(必须是移动用户):<br/><input type="text" name="message" format="*N" maxlength="11" value="13"/><br/>
    若您的用户名和密码遗忘或丢失，请拨打13347907223让管理员帮助找回。<br/>
    <anchor title="确定">确定
    <go href="Login.do?backTo=<%=backTo%>" method="post">
    <postfield name="message" value="$message"/>
    </go>
    </anchor><br/>
	<anchor title="back"><prev/>返回登录页面</anchor><br/>
    <a href="<%=backTo%>" title="进入">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
<%--//mcq_2006_8_31_登录用户五步走的开关(第五步)()_end--%>
<%--//mcq_2006_8_31_登录用户五步走的开关(第五步)()_start--%>   
    <logic:equal name="result" value="sendMessage">
<%
String backTo = (String) request.getAttribute("backTo");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL start
if(backTo == null){
	backTo=(String)session.getAttribute("loginReturnURL");
	if(backTo==null){
	backTo = "/wapIndex.jsp";
	}
}
if(backTo.toLowerCase().indexOf("logout")!=-1){
	backTo = "/wapIndex.jsp";
}
if(backTo.indexOf("/entry/")!=-1 || backTo.toLowerCase().indexOf("auto")!=-1 || backTo.toLowerCase().indexOf("register")!=-1){
	backTo = request.getParameter("backTo");
}
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL end
backTo = backTo.replace("&", "&amp;");
%>
<card title="登录乐酷" ontimer="<%=response.encodeURL(backTo)%>">
<timer value="30"/>
<p align="left">
<%//=BaseAction.getTop(request, response)%>
发送成功<br/>
--------<br/>
    <bean:write name="tip" filter="false"/>
    <a href="<%=backTo%>" title="进入">立即返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
<%--//mcq_2006_8_31_登录用户五步走的开关(第五步)()_end--%>
    <logic:equal name="result" value="success">
<%
String backTo = (String) request.getAttribute("backTo");
if(backTo==null){
	backTo = "/wapIndex.jsp";
}
if(backTo.indexOf("/entry/")!=-1 || backTo.toLowerCase().indexOf("auto")!=-1 || backTo.toLowerCase().indexOf("register")!=-1){
	backTo = request.getParameter("backTo");
	if(backTo==null || backTo.indexOf("login.jsp")!=-1){
		backTo = "/wapIndex.jsp";
	}
}
if(backTo.toLowerCase().indexOf("logout")!=-1 || backTo.indexOf("fpic")!=-1){
	backTo = "/wapIndex.jsp";
}
backTo = (backTo.replace("&", "&amp;"));
// 登录成功，把id写入cookie
String red = request.getParameter("red");
if(red!=null){
	response.sendRedirect(red);
	return;
}
UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
LogUtil.logLogin("login:" + loginUser.getId() + ":" + request.getRemoteAddr() + ":" + request.getHeader("User-Agent") + ":" + new CookieUtil(request).getCookieValue("jsid"));
session.setAttribute("cd-fourmSTime", Long.valueOf(System.currentTimeMillis() + 20000));	// 20秒内不能发帖或者回帖
if(loginUser!=null && request.isRequestedSessionIdFromCookie()){
	out.clearBuffer();
	response.sendRedirect("loginok.jsp");
	return;
}
%>
<card title="登录乐酷" ontimer="<%=response.encodeURL(backTo)%>">
    <timer value="60"/>
<p align="left">
登录成功！3秒后自动跳转。<br/>
下次学会用书签登录哦，不用输入ID和密码，更快捷哦：）<br/>
<a href="<%=backTo%>">直接跳转</a><br/>
<a href="/enter/index.jsp">免输入ID/密码直接登录的秘籍</a><br/>
<a href="/user/userInfo.jsp">修改密码</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
</logic:present>
<logic:notPresent name="result" scope="request">
<card title="登录乐酷">
<p align="left">
<%//=BaseAction.getTop(request, response)%>
<%
String mobile=(String)session.getAttribute("userMobile");
UserBean user=null;
Vector userList=null;

//lbj_2006_12_16_start
String backTo = request.getParameter("backTo");
if(backTo == null){
	backTo = (String)request.getAttribute("backTo");
}
//lbj_2006_12_16_end
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL start
if(backTo == null){
	backTo=(String)session.getAttribute("loginReturnURL");
	if(backTo==null || backTo.indexOf("login.jsp")!=-1){
	backTo = "/wapIndex.jsp";
	}
}
if(backTo.toLowerCase().indexOf("logout")!=-1 || backTo.toLowerCase().indexOf("auto")!=-1 || backTo.toLowerCase().indexOf("register")!=-1){
	backTo = "/wapIndex.jsp";
}
backTo = backTo.replace("&", "&amp;");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL end
if(session.getAttribute(Constants.LOGIN_USER_KEY) != null){
	out.clearBuffer();
	response.sendRedirect((backTo));
	return;
}
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");

//liuyi 2006-12-21 注册登录修改 start
int flag = 0;  //默认取不到手机号
int id = 0;
String key = mobile;
List userIdList = (List) OsCacheUtil.get(key,
		OsCacheUtil.USER_BY_MOBILE_GROUP,
		OsCacheUtil.USER_BY_MOBILE_FLUSH_PERIOD);
if (mobile != null && (mobile.startsWith("13") || mobile.startsWith("15"))) {//判断老号码
	String sql = "select a.id from user_info a, user_status b where a.id=b.user_id and a.mobile='"
		+ mobile + "' order by b.last_login_time desc limit 0,1";
	id = SqlUtil.getIntResult(sql, Constants.DBShortName);
	
	if (userIdList == null) {
		sql = "select id from user_info where mobile='"
				+ mobile + "'";
		userIdList = SqlUtil.getIntList(sql, Constants.DBShortName);
		if (userIdList == null) {
			userIdList = new ArrayList();
		}
		OsCacheUtil.put(key, userIdList,
				OsCacheUtil.USER_BY_MOBILE_GROUP);
	}
	
	if(id>0){ //老手机号
		flag = 1;
	}
	else{ //新手机号
		flag = 2;
	}
}
//liuyi 2006-12-21 注册登录修改 end
if(flag==0){  //取不到手机号
%>
<%if(user!=null){%>
您的昵称<a href="/jcadmin/autoLogin.jsp?id=<%=user.getId() %>&amp;mobile=<%=user.getMobile() %>"><%= StringUtil.toWml(user.getNickName()) %></a> 
ID:<%=user.getId()%> 密码:<%=Encoder.decrypt(user.getPassword())%><br/>
<%
}
%>
<%if(autoUser!=null){%>欢迎回来,<%=autoUser.getNickNameWml()%><%}else{%>【欢迎登录】<%}%><br/>
<%if(autoBookmark!=null){%><a href="<%=autoBookmark%>">&gt;&gt;我要自动登录</a><br/><a href="/enter/logindel.jsp">!!删除自动登录信息</a><br/><%}%>

<%if(request.getParameter("guest")!=null){
%>不好意思,对方设置了交流权限,她只愿意和登录用户交流,想打动她,就请登录或<a href="/register.jsp">免费注册</a>吧<br/><%	
}else{
%><%if(request.getParameter("dir")==null){%>登录后才能进行此操作,想展示你的魅力,就请登录或<a href="/register.jsp">免费注册</a>吧<br/><%}%>
<%if(request.getParameter("act")!=null){%>对方设置了交友权限,您还没有登录,不能与Ta交流哦。想获得Ta的青睐,就请登录或<a href="/register.jsp">免费注册</a>吧<br/><%}%>
<%}%>
登录方法一:<br/>
<a href="http://qq.joycool.net/enter/qq.jsp?s=<%=linkId%>">
<img src="http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/img/Connect_logo_3.png" alt="QQ登陆" />
</a><br/>
登录方法二:<br/>
ID或手机号:<br/><input type="text" name="uid" format="*N" maxlength="11" value="<%if(uid>0){%><%=uid%><%}%>"/><br/>
密码:<br/><input type="text" name="password"  maxlength="20" value=""/><br/>
<anchor title="确定">登录
    <go href="Login.do?backTo=<%=backTo%>" method="post"><%String red = (String)session.getAttribute("red");if(red!=null){%><postfield name="red" value="<%=red%>"/><%}%>
    <postfield name="uid" value="$uid"/>
    <postfield name="password" value="$password"/>
    </go>
</anchor>|<a href="/register.jsp?backTo=<%=backTo%>">注册送大礼</a><br/>
不注册也可以<a href="/guest/index.jsp">去游乐园逛逛</a>.<br/>
如果你忘记ID和密码,请点击<a href="/find.jsp">找回密码</a><br/>
若还是无法找回密码，请拨打13347907223让管理员帮助找回。<br/>
您还可以<a href="/guest/chat.jsp">和在线游客聊天</a><br/>
<anchor title="back"><prev/>返回上一页.我想再转转</anchor><br/>
<%
}else if(flag==1){  //老手机号
UserBean lastUser = UserInfoUtil.getUser(id);
%>
欢迎回家，老朋友！大家都等着你登录呢！<br/>
<%if(autoBookmark!=null){%><a href="<%=autoBookmark%>">&gt;&gt;我要自动登录</a><br/><%}%>
登录方法一:<br/>
<a href="http://qq.joycool.net/enter/qq.jsp?s=<%=linkId%>">
<img src="http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/img/Connect_logo_3.png" alt="QQ登陆" />
</a><br/>
登录方法二:<br/>
用户ID或手机号:<br/><input type="text" name="uid" format="*N" maxlength="11" value="<%if(uid>0){%><%=uid%><%}%>"/><br/>
密码：<br/><input type="text" name="password"  maxlength="20" value=""/><br/>
<anchor title="确定">登录
    <go href="Login.do?backTo=<%=backTo%>" method="post">
    <postfield name="uid" value="$uid"/>
    <postfield name="password" value="$password"/>
    </go>
</anchor><br/>
您上次登录用的：<br/>
ID: <%= lastUser.getId() %><br/>
密码；<%= Encoder.decrypt(lastUser.getPassword())%><br/>
<%
if((userIdList!=null && userIdList.size()>1)){
%>
<anchor title="确定">查看其他ID
    <go href="Login.do?backTo=<%=backTo%>" method="post">
    <postfield name="userMob" value="<%=mobile%>"/>
    </go>
</anchor><br/>
<%}%>
若您有其他问题，请拨打13347907223让管理员帮助找回。<br/>
<anchor title="back"><prev/>返回</anchor><br/>
<%	
}else if(flag==2){  //新手机号%>
欢迎来乐酷，登录后，享受游戏聊天乐趣啦！<br/>
登录方法一:<br/>
<a href="http://qq.joycool.net/enter/qq.jsp?s=<%=linkId%>">
<img src="http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/img/Connect_logo_3.png" alt="QQ登陆" />
</a><br/>
登录方法二:<br/>
用户ID(数字):<br/><input type="text" name="uid" format="*N" maxlength="20" value="<%if(uid>0){%><%=uid%><%}%>"/><br/>
密码：<br/><input type="text" name="password"  maxlength="20" value=""/><br/>
<anchor title="确定">登录
    <go href="Login.do?backTo=<%=backTo%>" method="post">
    <postfield name="uid" value="$uid"/>
    <postfield name="password" value="$password"/>
    </go>
</anchor>|
<a href="/register.jsp?backTo=<%=backTo%>">注册</a><br/>
若您忘记了ID和密码，请拨打13347907223让管理员帮助找回。<br/>
<%	
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>
</wml>