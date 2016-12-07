<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! MoodService service=new MoodService(); %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
	MoodAction action=new MoodAction(request);
	int type = action.getParameterInt("t");
	int id = action.getParameterInt("id");
%><wml>
<card title="心情"><p>
<% if (type == 1){
		%>你已经被对方加入黑名单,无法回复TA的心情.<br/><a href="view.jsp?id=<%=id%>">返回上级</a><%
   } else if (type == 2){
   		%>回复的字数过多.<br/><a href="view.jsp?id=<%=id%>">返回上级</a><%
   } else if (type == 3){
   		%>心情的字数过多.<br/><a href="view.jsp?id=<%=id%>">返回上级</a><%
   } else if (type ==4){
   		%>没有找到好友的心情.<br/><a href="mood.jsp">返回上级</a><%
   }%>
</p></card>
</wml>