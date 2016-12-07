<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.util.db.*,net.joycool.wap.bean.UserBean"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String ip = request.getRemoteAddr();
int linkId = StringUtil.toId((String) session.getAttribute("linkId"));
if(linkId == 0 || !SecurityUtil.isCmwap(ip)){
response.sendRedirect("/user/login.jsp");
return;
}
String mobile = (String)session.getAttribute("userMobile");
if(mobile != null) {	// 取到手机号则立刻去注册页面
	response.sendRedirect("/register.jsp");
	return;
}
if(request.getParameter("sid") == null){
		int sid = StringUtil.toInt((String)session.getAttribute("sid"));
		if(sid==-1){	// 新插入记录
			DbOperation db = new DbOperation(5);
			db.executeUpdate("insert into call_log3 set session='"+session.getId()+"',create_time=now(),ip='"+ip+"'");
			sid=db.getLastInsertId();
			db.release();
			session.setAttribute("sid",String.valueOf(sid));
		}
		String eurl = "http://"+request.getServerName()+response.encodeURL("/verify.jsp?sid=1");
//		System.out.println(eurl);
//		eurl = "http://211.157.107.130/abc/jc.jsp?fr=" + net.joycool.wap.util.Encoder.encrypt(eurl);
	//	System.out.println(eurl);
	//	response.sendRedirect("http://bomb.ebinf.com/jc.jsp?fr=" + net.joycool.wap.util.Encoder.encrypt(eurl));
		
		response.sendRedirect("http://221.179.205.25/bjebinf/gg.jsp?fr=" + URLEncoder.encode(eurl, "utf-8"));
	//	response.sendRedirect("http://211.136.107.36/mnf/m-11534.fet?url=" + java.net.URLEncoder.encode(eurl, "utf-8"));
		return;

} else {
		
		int status=1;
		mobile = request.getParameter("_mn_");
		if(mobile != null) {
			mobile = Encoder.decrypt(mobile);
			if (mobile.startsWith("86")) {
				mobile = mobile.substring(2);
			}

		} else {
			mobile = "";
		}
		if(mobile.startsWith("1")&&mobile.length()==11) {
			session.setAttribute("userMobile", mobile);
		}
			
		int sid = StringUtil.toInt((String)session.getAttribute("sid"));
		if(sid!=-1){
			SqlUtil.executeUpdate("update call_log3 set phone='"+StringUtil.toSql(mobile)+"',status="+status+" where id="+sid,5);
		}

}
if(true){
request.getRequestDispatcher("/register.jsp").forward(request,response);
return;
}
/*
if(request.getParameter("nopic")!=null){
	session.setAttribute("userMobile", "");
	response.sendRedirect(("user/register.jsp?" + unique));
	return;
}*/

String tip = "乐酷免费注册/登陆，享受更多超值服务<br/>请输入下面图片中的验证码:<br/>";
boolean result = false;

String verify = request.getParameter("verify");
String answer = (String)session.getAttribute("verify");

if(verify != null) {
	if(answer != null && verify.equals(answer) && session.getAttribute("userMobile") != null) {
		result = true;
	} else {
		tip = "对不起,您的验证码输入错误，请重新输入:<br/>";
	}
}

String self = ("verify.jsp?");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(!result){%>
<card title="乐酷免费注册">
<p align="left">
<%=tip%>
<img src="verifypic.jsp" alt=""/><br/>
<input type="text" name="verify" format="*N" maxlength="6" value=""/><br/>
<anchor title="确定">确定
<go href="<%=self%>" method="post">
<postfield name="verify" value="$verify"/>
</go>
</anchor>
|<a href="<%=self%>">刷新试试</a><br/>
如果您始终无法看到图片，请检查手机是否禁止图片显示。<br/>
您还可以发短信ZC到13718998855，系统即为您免费注册，并将ID和密码发送到您的手机上。<br/>
若您的用户名和密码遗忘或丢失，请拨打010-51285713让管理员帮助找回。<br/>
<a href="/wapIndex.jsp">返回乐酷首页</a><br/>
</p>
</card>
<%}else{
	response.sendRedirect(("register.jsp?"));
	return;
}%>
</wml>