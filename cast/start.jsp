<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	if(true){
		new CastleAction(request,response).innerRedirect("end.jsp");
		return;
	}
	CastleAction action = new CastleAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="建立城堡"><p>
【开始城堡游戏】<br/>
城主:<%=action.getLoginUser().getNickNameWml() %><br/>
选择种族:<br/>
<select name="race" value="5"><option value="5">随机</option><option value="1">拜索斯</option><option value="2">伊斯</option><option value="3">杰彭</option></select><br/>
选择地图区域:<br/>
<select name="field" value="5">
<option value="5">随机</option>
<option value="1">西北方(200|200)</option>
<option value="2">西南方(200|600)</option>
<option value="3">东北方(600|200)</option>
<option value="4">东南方(600|600)</option></select><br/>
<anchor>确认以上选择
<go href="i.jsp">
<postfield name="race" value="$race"/>
<postfield name="field" value="$field"/>
</go></anchor><br/>
注:城堡创建时，城主名称将与用户名称保持一致，城堡创建后名称将无法修改<br/>
<a href="/user/userInfo.jsp">修改昵称</a>
<br/><a href="s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>