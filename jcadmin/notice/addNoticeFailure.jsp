<%@ page contentType="text/html;charset=utf-8"%>

<%String hint = request.getParameter("hint");
			if (hint == null || hint.equals("")) {
			} else {
				if ("01".equals(hint)) {
					out.print("<b>参数错误！</b><br><br>");
					out.print("用户类型没有选择");
				}
				if ("02".equals(hint)) {
					out.print("<b>参数错误！</b><br><br>");
					out.print("用户地区没有选择");
				}
				if ("03".equals(hint)) {
					out.print("<b>参数错误！</b><br><br>");
					out.print("用户性别没有选择");
				}
				if ("04".equals(hint)) {
					out.print("<b>参数错误！</b><br><br>");
					out.print("用户所在聊天室没有选择");
				}
				if ("05".equals(hint)) {
					out.print("<b>参数错误！</b><br><br>");
					out.print("用户访问天数没有填写");
				}
				if ("06".equals(hint)) {
					out.print("<b>参数错误！</b><br><br>");
					out.print("通知类型没有选择");
				}
				if ("07".equals(hint)) {
					out.print("<b>参数错误！</b><br><br>");
					out.print("通知标题不能为空");
				}
				if ("08".equals(hint)) {
					out.print("<b>参数错误！</b><br><br>");
					out.print("通知内容和链接页面不能同时为空");
				}
				if ("09".equals(hint)) {
					out.print("<b>发送失败！</b><br><br>");
					out.print("数据库异常，稍后再试");
				}
				if ("10".equals(hint)) {
					out.print("<b>无法发送！</b><br><br>");
					out.print("没有满足条件的用户");
				}
			}

		%>
