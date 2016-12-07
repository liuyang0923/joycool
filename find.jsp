<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.UserBean"%><%@ page import="java.net.URLEncoder"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%!
static net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="找回密码">
<p align="left">【特别提示】<br/>
如果您是QQ登录用户请直接登录,无需找回密码;若QQ密码丢失,请登录QQ官网找回。<br/>
【短信修改登录密码】<br/>
如果您是手机注册用户忘记了密码,只需用注册时的手机号发送短信到10690329013076即可修改密码(注意:密码必须都是数字!!).例如,如果要修改密码为999888,那么发送的短信内容为"修改密码999888".(点击<a href ="sms:10690329013076?body=修改密码">这里</a>直接开始编辑短信),示意图请见下文。<br/>
【短信修改银行密码】<br/>
如果要修改银行密码为234234,那么发送的短信内容为"修改银行密码234234"(点击<a href ="sms:10690329013076?body=修改银行密码">这里</a>直接开始编辑短信),示意图请见下文。<br/>短信成功发送后,系统会在1分钟内完成修改,不会回复短信.您只需用修改后的密码直接登录即可。<br/>
!!注意:短信内容是系统自动识别,请严格按规范输入,不要添加任何符号。<br/>&gt;&gt;短信发送成功,<a href="user/login.jsp">重新登录</a><br/>
++改登录密码--短信示意图++<br/>
<img src="img/pw1.gif"/><br/><br/>
++改银行密码--短信示意图++<br/>
<img src="img/pw2.gif"/><br/>
如有疑问,请拨打4006665721咨询。<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>