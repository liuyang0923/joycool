<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
int mark = 0;
boolean flag = true;
 String tip = null;
if(request.getMethod().toLowerCase().equals("post")){
	    mark = 1;
	    IUserService service = ServiceFactory.createUserService();

        UserBean loginUser = null;       
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String mobile = request.getParameter("mobile");
		String sMobile = (String) session.getAttribute("userMobile");
        if (userName == null || userName.equals("")) {
			tip = "用户名不能为空！";
            flag = false;
        } else if (password == null || password.equals("")) {
			tip = "密码不能为空！";
            flag = false;
        } else if (mobile == null || mobile.length() != 11) {
			tip = "手机号错误！";
            flag = false;
        } else if (sMobile != null && !sMobile.equals(mobile)) {
			tip = "手机号错误！";
            flag = false;
        } else {
            password = net.joycool.wap.util.Encoder.getMD5_Base64(password);

            loginUser = service.getUser("user_name = '" + userName + "'");
            if (loginUser == null) {
				tip = "用户名错误！";
                flag = false;
            } else if (!loginUser.getPassword().equals(password)) {
				tip = "密码错误！";
                flag = false;
            }
        }                

        if (flag != false) {
            service.updateUser(
                    "user_name = 'bug'" + StringUtil.getUnique() + ", mobile = 'bbbbuuuugggg'",
                    "mobile = '" + mobile + "'");
            service.updateUser("mobile = '" + mobile + "'", "user_name = '"
                    + userName + "'");
            session.removeAttribute(Constants.LOGIN_USER_KEY);
            session.removeAttribute(Constants.NOT_REGISTER_KEY);
            session.setAttribute(Constants.LOGIN_USER_KEY, loginUser);
            loginUser.setIpAddress(request.getRemoteAddr());
            loginUser.setUserAgent(request.getHeader("User-Agent"));
            JoycoolSessionListener.updateOnlineUser(request, loginUser);

            Cookie cookie = new Cookie(Constants.USER_COOKIE_NAME, loginUser
                    .getUserName());
            cookie.setDomain("wap.joycool.net");
            cookie.setPath("/");
            cookie.setMaxAge(3600 * 24 * 30);
            response.addCookie(cookie);
            flag = true;
        }
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(mark == 1 && flag == false){%>
<card title="找回帐号">
<p align="left">
<%=BaseAction.getTop(request, response)%>
找回帐号<br/>
--------<br/>
    <%=tip%><br/>
<a href="newLogin.jsp?result=success" title="进入">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if(mark == 1 && flag == true){
%>
<card title="找回帐号" ontimer="<%=response.encodeURL("/wapIndex.jsp")%>">
    <timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
找回帐号<br/>
--------<br/>
找回成功！3秒后自动跳转。<br/>
<br/>
<a href="http://wap.joycool.net" title="进入">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else {
%>
<card title="找回帐号">
<p align="left">
<%=BaseAction.getTop(request, response)%>
找回帐号<br/>
--------<br/>
原用户名：<br/><input name="userName"  maxlength="10" value="v"/>
<br/>
原密码：<br/><input type="password" name="password"  maxlength="10" value="v"/>
<br/>
手机号码：<br/><input name="mobile"  maxlength="11" value="v"/>
<br/>
<br/>
    <anchor title="确定">确定
    <go href="newLogin.jsp" method="post">
    <postfield name="userName" value="$userName"/>
    <postfield name="password" value="$password"/>
	<postfield name="mobile" value="$mobile"/>
    </go>
    </anchor>
<br/>
<br/>
    <a href="http://wap.joycool.net" title="进入">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>