<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.spec.castle.*" %><%@ page import="net.joycool.wap.spec.farm.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.bank.BankAction" %><%@ page import="net.joycool.wap.bean.bank.MoneyLogBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%!
%><%
CustomAction action = new CustomAction(request);
int userId = action.getParameterInt("id");
UserBean user=UserInfoUtil.getUser(userId);
CastleUserBean cuser = CastleUtil.getCastleUser(userId);
FarmUserBean fuser = FarmWorld.service.getFarmUser("user_id=" + userId);
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
<div align="center">
<H2 align="center">用户游戏后台</H2>
<p><%=user.getNickNameWml()%>(<a href="queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getId()%></a>)</p>
<table width="500">
<tr><td width="100">桃花源</td><td>

<%if(fuser==null){%>(无)<%}else{%>
	<table>
	<tr><td width="100">名称</td><td width="300"><%=fuser.getNameWml()%>(<a href="../farm/viewUser.jsp?userId=<%=userId%>">查看</a>)</td></tr>
	<tr><td>等级</td><td><%=fuser.getRank()%></td></tr>
	</table>
<%}%>

</td></tr>

<tr><td>城堡战争</td><td>
<%if(cuser==null){%>(无)<%}else{%>
	<table>
	<tr><td width="100">名称</td><td width="300"><%=cuser.getNameWml()%>(<a href="../castle/user.jsp?uid=<%=userId%>">查看</a>)</td></tr>
	<tr><td>总人口</td><td><%=cuser.getPeople()%></td></tr>
	</table>
<%}%>

</td></tr>
</table>
<br/>
</body>
</html>