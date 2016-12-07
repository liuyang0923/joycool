<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.user.*"%>
<% response.setHeader("Cache-Control","no-cache");
UserAction2 action = new UserAction2(request);
String tip = "";
boolean result = false;
int submit = action.getParameterInt("s");
String backTo = action.getParameterNoCtrl("backTo");
if (backTo == null || "".equals(backTo)){backTo = BaseAction.INDEX_URL;}
UserBean2 user = action.getLoginUser2();
if (user == null){
	response.sendRedirect("/");
	return;
}
if (submit == 1){
	result = action.modifyInfo(request);
	if (result){
		response.sendRedirect("tips.jsp?t=1");
		return;
	} else {
		tip = (String)request.getAttribute("tip");
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="初级用户注册"><p>
<%=BaseAction.getTop(request, response)%>
<% if (user.getChecked() == 0){
%><%if(tip != null && !"".equals(tip)){%><%=tip%><%}else{%>恭喜您成为乐酷初级会员!您以后用该号码和密码可登陆乐酷.要修改您的昵称,以便更好聊天吗?<%}%><br/>
昵称(最多10字):<br/><input name="nickname" maxlength="10" value="初级"/><br/>
年龄(10-60):<br/><input name="age" maxlength="2" value="18" format="*N"/><br/>
性别:<select name="gender" value="<%=user==null?0:user.getGender()%>">
<option value="1">男</option>
<option value="0">女</option>
</select><br/>
<anchor title="ok">确认修改
<go href="modifyA.jsp?s=1" method="post">
	<postfield name="nickname" value="$nickname"/>
	<postfield name="age" value="$age"/>
	<postfield name="gender" value="$gender"/>
</go>
</anchor><br/><%	
} else {
tip = (String)request.getAttribute("tip");
%><%if(tip != null && !"".equals(tip)){%><%=tip%><%}else{%>恭喜您的短信认证成功!为了更好的体验乐酷,您只需对下列资料进行确认,就能升级成正式会员.<%}%><br/>
昵称(最多10字):<br/><input name="nickname" maxlength="10" value=""/><br/>
性别:<select name="gender" value="<%=user.getGender()%>">
<option value="1">男</option>
<option value="0">女</option>
</select><br/>
您需要严肃确认您的性别,以后不可更改.<br/>
<anchor title="确认">确认
<go href="/user/Register.do" method="post">
	<postfield name="nickname" value="$nickname"/>
	<postfield name="gender" value="$gender"/>
</go>
</anchor><br/>
<%}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>