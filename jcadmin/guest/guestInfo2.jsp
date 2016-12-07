<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.regex.Matcher,java.util.regex.Pattern,java.util.*,jc.guest.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30; %>
<%GuestHallAction action = new GuestHallAction(request,response);
String tip = "";
GuestUserInfo tmpUser = null;
GuestUserInfo guestUser = null;
int id = action.getParameterInt("id");
if (id <= 0){
	tip = "传入的ID错误.<a href=\"guestInfo.jsp\">返回列表</a>";
} else {
	guestUser = action.getGuestUser(id);
	if (guestUser == null){
		tip = "传入的ID错误.<a href=\"guestInfo.jsp\">返回列表</a>";
	} else {
		int modify = action.getParameterInt("m");
		if (modify == 1){
			// 修改...
			String userName = action.getParameterNoEnter("un");
//			String password = action.getParameterNoEnter("pw");
//			String mobile = action.getParameterNoEnter("um");
			// 昵称
			if (userName == null || "".equals(userName)){
				tip = "没有输入用户名.<a href=\"guestInfo2.jsp?id=" + guestUser.getId() + "\">返回修改</a>";
			} else if(userName.length() < 2 || userName.length() > 10){
				tip = "用户名长度错误.<a href=\"guestInfo2.jsp?id=" + guestUser.getId() + "\">返回修改</a>";
			} else {
				tmpUser = GuestHallAction.service.getUserInfo(" user_name='" + userName + "'");
				if (tmpUser != null && tmpUser.getId()  != guestUser.getId()){
					tip ="此用户名已被别人占用.<a href=\"guestInfo2.jsp?id=" + guestUser.getId() + "\">返回修改</a>";
				} else {
					guestUser.setUserName(userName);
					// 同时修改游客聊天室map里的用户bean。如果有的话。
					GuestUserInfo guestUser2 = (GuestUserInfo)GuestAction.getGuestMap().get(new Integer(guestUser.getId()));
					if (guestUser2 != null){
						GuestAction.getGuestMap().put(new Integer(guestUser.getId()),guestUser);
					}
				}
			}
//			// 密码
//			if (password == null || "".equals(password)){
//				tip = "没有输入密码.<a href=\"guestInfo2.jsp?id=" + guestUser.getId() + "\">返回修改</a>";
//			} else if (password.length() < 4 || password.length() > 10){
//				tip = "密码位数错误.<a href=\"guestInfo2.jsp?id=" + guestUser.getId() + "\">返回修改</a>";
//			} else {
//			    Pattern pattern = Pattern.compile("[0-9a-zA-Z]*");	// 只匹配字符和数字
//			    Matcher match = pattern.matcher(password);
//			    if (!match.matches()){
//			    	tip = "密码格式错误.<a href=\"guestInfo2.jsp?id=" + guestUser.getId() + "\">返回修改</a>";
//			    } else {
//			    	guestUser.setPassword(password);
//			    }
//			}
//			// 手机号
//			if (mobile != null && !"".equals(mobile)){
//				Pattern pattern = Pattern.compile("(1\\d{10})");
//				Matcher isPhone = pattern.matcher(mobile);
//				if (!isPhone.matches()){
//					tip = "手机号输入错误.<a href=\"guestInfo2.jsp?id=" + guestUser.getId() + "\">返回修改</a>";
//				} else {
//					guestUser.setMobile(mobile);
//				}
//			}
			if ("".equals(tip)){
				SqlUtil.executeUpdate("update guest_user_info set user_name='" + StringUtil.toSql(guestUser.getUserName()) + "' where id=" + guestUser.getId(),6);
				response.sendRedirect("guestInfo.jsp");
				return;
			}
		}
	}
}
%>
<html>
	<head>
		<title>游客大厅用户</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%if ("".equals(tip)){
%>UID:<%=guestUser.getId()%><br/>
<form action="guestInfo2.jsp?m=1&id=<%=guestUser.getId()%>" method="post">
昵称:<input type="text" name="un" value="<%=guestUser.getUserName()%>" /><br/>
<%--密码:<input type="text" name="pw" value="<%=guestUser.getPassword()%>" /><br/>
绑定手机:<input type="text" name="um" value="<%=guestUser.getMobile()%>" /><br/> --%>
<input type="submit" value="确认修改">
<input id="cmd" type="button" value="返回" onClick="javascript:window.location.href='guestInfo.jsp'">
</form>
<%
} else {
%><%=tip%><br/><%	
}%>
	</body>
</html>