<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.regex.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%><%!
	static Pattern wordsFilter = Pattern.compile("[操逼鸡舔插射插穴淫乳骚搞曰网]|夜情|奶|做爱|干|小洞|叉|草|你[娘妈姐]|色|杂种|贱人|妓|嫖|奸|交|裸|生殖|阴|[./BＢPＰJＪ]|激情|肉棒|阳具|少妇");
	%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   GuestUserInfo guestUser=null;
%><%@include file="in.jsp"%>
<%
	String nick = action.getParameterNoEnter("n");
   int from = action.getParameterInt("f");
   guestUser = action.getGuestUser();	//此方法会先从session中
   String topStr = "第一次来到游乐园吗？给自己起个响亮的昵称吧~(昵称不可修改，要牢记哦)";
   if (from  == 1){
	   topStr = "您还没有设置昵称,这样跟别人打招呼不太礼貌哦.先给自己起个响亮的名字吧~";
   }
   if (guestUser != null){
	   response.sendRedirect("back.jsp?i=20");
	   return;
   }
   if (nick != null && nick.length()!=0){
	   	int result = 0;
	   	if(wordsFilter.matcher(nick).find())
			result = 24;
	   	else
		   	result = action.addUser(nick);
	   	if (result == 0){
		   	response.sendRedirect("nickok.jsp");
		   	return;
	   	} else {
		   // tip = request.getAttribute("tip").toString();
		   	action.innerRedirect("back.jsp?i=" + result);
		   	return;
	   	}
   }
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="设置昵称">
<p><%=BaseAction.getTop(request, response)%> <%if ("".equals(tip)){
%><%=topStr%><br />
请输入2~10个字作为昵称:<br />
<input name="nick" maxlength="10" /><br />
<anchor>确定<go href="nick.jsp" method="post">
<postfield name="n" value="$nick" />
</go></anchor>|<%if(from==1){%><a href="chat.jsp">返回</a>
<%}else{%><a href="index.jsp">返回</a>
<%}%><br />
<%if (user == null){
%><a href="login.jsp">已注册游乐园的用户请点此登陆</a><br />
<%	
}%> <%
} else {
	%><%=tip%><br />
<a href="nick.jsp">重新输入</a><br />
<a href="index.jsp">返回首页</a><br />
<%
}%> <%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>