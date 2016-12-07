<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page	import="net.joycool.wap.spec.bottle.*"%><%!
	static BottleService service = new BottleService();
%><%
 		BottleAction action = new BottleAction(request);

		int id = action.getParameterInt("id");
		int uid = action.getLoginUser().getId();
		String tip = null;
		String reply = action.getParameterString("reply");
		String f = action.getParameterString("f");

		if (reply == null){
			response.sendRedirect("read.jsp?id=" + id);
			return;
		}
		//过滤特殊字符串
		reply = reply.replace("\\", "\\\\").replace("'", "\\'");
		reply = reply.trim();
		
		if ("".equals(reply)) {
			tip = "回复不能为空";
		} else if (reply.length() > 100) {
			tip = "回复不得大于100字";
		}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="漂流瓶" ontimer="<%=response.encodeURL("back.jsp?f=" + f)%>"><timer value="10"/>
<p><%
		if ("回复不能为空".equals(tip) || "回复不得大于100字".equals(tip)) {
	%><%=tip%><br/><a href="read.jsp?id=<%=id%>&amp;f=<%=f%>">返回上级</a>
	<%
		} else {
			if (action.isCooldown("chat",15000)){
				boolean result = service.saveReply(id, uid, reply);
				if (result) {
					%>留言成功<br/><%
						} else {
					%>留言失败，请与管理员联系。<br/><%
				}
			}else{
				%>你回复的太快了<br/><%
			}
			if ("reve".equals(f)) {
	%><a href="reversion.jsp?id=<%=id%>&amp;f=<%=f%>">返回上级</a>
	<%
		} else if ("list".equals(f)) {
	%><a href="list.jsp?id=<%=id%>&amp;f=<%=f%>">返回上级</a>
	<%
		} else {
	%><a href="bottles.jsp?id=<%=id%>&amp;f=<%=f%>">返回上级</a>
	<%
		}
		}
	%>
</p>
</card>
</wml>

