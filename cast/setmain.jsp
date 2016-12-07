<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CacheService cacheService = CacheService.getInstance();%><%
	
	CastleAction action = new CastleAction(request);
	CastleBean castle = action.getCastle();
	if(action.hasParam("c")){
		String pwd = request.getParameter("pwd");
		
		// 获取用户设置信息
		UserSettingBean userSetting = action.getLoginUser().getUserSetting();
		boolean pwdok=true;
		if(userSetting != null && userSetting.getBankPw() != null ) {
			if(pwd == null || !pwd.equals(userSetting.getBankPw())) {
				action.tip("tip", "密码不正确");
				pwdok=false;
			}
		}
		if(pwdok)
			action.setMain();
	}
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="设定主城"><p><%@include file="top.jsp"%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
<%=castle.getCastleNameWml()%>(<%=castle.getX()%>|<%=castle.getY()%>)<br/>
如果你要设定主城，为了安全起见请输入你的银行密码。如果你想把这个城堡设定为主城的话，原来的主城内高于等级10的资源田会降到10级，而石匠铺都会失去，新主城的大兵营和大靶场也会失去。<br/>
<input name="pwd" value=""/><br/>
<anchor>确认设定为主城<go href="setmain.jsp?c=1" method="post">
<postfield name="pwd" value="$pwd"/>
</go></anchor><br/>
<a href="fun.jsp?t=32">返回皇宫</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>