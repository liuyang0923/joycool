<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.util.db.*,net.joycool.wap.framework.*"%><%
	
	CastleAction action = new CastleAction(request);
	CastleUserBean user = action.getCastleUser();
	String u = request.getParameter("a");
	if(u != null && u.equals("u")) {
		String name = action.getParameterNoEnter("name");
		if(name != null){
			name=name.replace("和平鸽","[#0]");
			if(name == null || name.length() == 0) {
				request.setAttribute("msg", "不能为空");
			} else if(StringUtil.getCLength(name) > 60 || name.length()>45) {
				request.setAttribute("msg","不能大于30个字");
			} else {
				DbOperation db = new DbOperation(5);
				if(db.executeUpdate("update castle_user set info = '" + StringUtil.toSql(name) + "' where uid = " + user.getUid())) {
					user.setInfo(name);
					request.setAttribute("msg","更改成功");
				} else {
					request.setAttribute("msg","更改失败");
				}
				db.release();
			}
		}
	}
	CastleBean myCastleBean = action.getCastle();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="修改帐户信息"><p>
<%=StringUtil.toWml(myCastleBean.getCastleName())%>(<%=myCastleBean.getX()%>|<%=myCastleBean.getY()%>)<br/>
城主:<%=StringUtil.toWml(user.getName())%><br/>
<%if(request.getAttribute("msg") != null) {%><%=request.getAttribute("msg") %><br/><%} %>
修改个人资料：<br/><input name="name" value=""/><br/>
<anchor>修改<go href="uInfo.jsp?a=u" method="post">
<postfield name="name" value="$name"/></go></anchor>
<br/>
<a href="account.jsp">返回帐号管理</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>