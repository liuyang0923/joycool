<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.user.*"%>
<%! static String[] tips= {"","如果忘记了密码,必须发短信来找回您的密码.",
		                    "您需要进行短信验证,成为正式会员后才能用刚才的功能.",
		                    "申请高级用户方式:"}; %>
<% response.setHeader("Cache-Control","no-cache");
UserAction2 action = new UserAction2(request);
UserBean2 user = action.getLoginUser2();
int type = action.getParameterInt("t");
if (user != null && user.getChecked() == 1){
//	response.sendRedirect("/user/login.jsp");
//	return;
//} else if (user.getChecked() == 1){
	response.sendRedirect("modifyA.jsp");
	return;
}
String tip = "";
String title = "找回登陆密码";
String backTo = action.getParameterNoCtrl("backTo");
if (backTo == null || "".equals(backTo)){backTo = BaseAction.INDEX_URL;}
if (type >0 && type < tips.length){
	tip = tips[type];
}
if (type == 3){
	title = "申请高级用户";
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(tip != null && !"".equals(tip)){%><%=tip%><br/><%}%>
发送短信A,到号码<a href ="sms:13718998855?body=a">13718998855</a>,即可完成.<br/>
我们承诺注册过程安全,且绝对免费!<br/>
(智能手机点击<a href ="sms:13718998855?body=a">这里</a>直接开始编辑短信)<br/>
非智能机窍门:点击拨打此<a href ="wtai://wp/mc;13718998855">13718998855</a>号码,立刻挂断,然后在通话记录中找到此号码输入短信.<br/>
<a href="<%=backTo%>">返回上一页</a><br/>
您还可以<a href="/guest/chat.jsp">和在线游客聊天</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>