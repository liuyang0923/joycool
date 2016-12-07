<%@include file="../checkMobile.jsp"%><%if(request.getParameter("productId")!=null){%><%@include file="../bank/checkpw.jsp"%><%}%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.Tong4Action"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
%><%
response.setHeader("Cache-Control","no-cache");

Tiny2Action action2 = new Tiny2Action(request, response);
if(action2.checkGame(5)) return;
if(action2.getGame() == null){
	if(net.joycool.wap.util.RandomUtil.nextInt(2) == 0){
		action2.startGame(games[0], 5);
		return;
	}
}else{
	if(net.joycool.wap.util.RandomUtil.nextInt(2) == 0){
		request.setAttribute("extra-shop", new Integer(4));
	}else{
		request.setAttribute("extra-shop", new Integer(5));
	}
}

String mark = request.getParameter("mark");
if(mark==null){
	mark = "d";
}
/*
if("destroy".equals(mark)) {
BaseAction.sendRedirect("/tong/tongList.jsp", response);
return;
}*/
/*
if("destroy".equals(mark)&&request.getParameter("test")==null) {
BaseAction.sendRedirect("/tong/tongList.jsp", response);
return;
}*/

Tong4Action action = new Tong4Action(request);
if(mark.equals("r")) {
	Calendar cal = Calendar.getInstance();
	int currentHour = cal.get(Calendar.HOUR_OF_DAY);
	if(currentHour == 3 || currentHour == 4)
		action.doTip("failure", "夜间帮会城墙维护,暂停访问.");
	else {
/*		Tong4Action.Tong2User t2u = action.getUser2();
		if(t2u.choice!=action.getParameterInt("c")){
			net.joycool.wap.bean.UserBean user = action.getLoginUser();
			System.out.print(user.getId());
			System.out.print(';');
		}*/
		action.tongCityResult(request);
		String qs = request.getQueryString();
		if(qs!=null&&qs.length()>5&&qs.indexOf('&',qs.length()-4)==-1){
			net.joycool.wap.bean.UserBean user = action.getLoginUser();
			System.out.print(user.getId());
			System.out.print(',');
		}
	}
} else {
	action.tongCityResult(request);
	String qs = request.getQueryString();
	if(qs!=null&&qs.length()>5&&qs.indexOf('&',qs.length()-4)==-1){
		net.joycool.wap.bean.UserBean user = action.getLoginUser();
		System.out.print(user.getId());
		System.out.print(',');
	}
}
String result =(String)request.getAttribute("result");


%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
	int tongId = StringUtil.toInt(request.getParameter("tongId"));
	out.clearBuffer();
	response.sendRedirect("tongList.jsp");
return;
}else if(result.equals("timeError")){
TongBean tong=(TongBean)request.getAttribute("tong");
String url;
if(request.getParameter("productId")==null) url=("/tong/tongCity2.jsp?tongId="+tong.getId()+"&amp;mark="+mark);
else url=("/tong/tongCity.jsp?tongId="+tong.getId());
url+="&amp;"+net.joycool.wap.util.RandomUtil.seqInt(10);
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="<%=url%>">返回城墙</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
String url;
if(request.getParameter("productId")==null) url=("/tong/tongCity2.jsp?tongId="+tong.getId()+"&amp;mark="+mark);
else url=("/tong/tongCity.jsp?tongId="+tong.getId());
url+="&amp;"+net.joycool.wap.util.RandomUtil.seqInt(10);
%>
<card title="<%=StringUtil.toWml(tong.getTitle()) %>" ontimer="<%=response.encodeURL(url)%>">
<timer value="10"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒钟返回城墙页面)<br/>
<a href="<%=url%>">返回帮会城墙</a><br/>
</p>
</card>
<%}%></wml>
