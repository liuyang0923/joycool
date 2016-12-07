<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	List list = SqlUtil.getObjectsList("select time from castle_stat_week group by time order by time desc", 5);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p>
<%=BaseAction.getTop(request, response)%>
【城堡大排名!】<br/>
本周排名-<a href="statw3.jsp">本周攻击</a>.<a href="statw4.jsp">本周防御</a>.<a href="statw5.jsp">本周抢夺</a><br/>
总排名-<a href="stat.jsp">城主</a>.<a href="stat6.jsp">指挥官</a>.<a href="stat2.jsp">联盟</a>.<a href="stat3.jsp">攻击总排名</a>.<a href="stat4.jsp">防御总排名</a>.<a href="stat5.jsp">抢夺总排名</a><br/>
历史周排名-<%
for(int i=0;i<list.size();i++){
Object[] os = (Object[])list.get(i);
%><%if(i!=0){%>.<%}%><a href="statw.jsp?t=<%=os[0]%>"><%=os[0]%></a><%
}%><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottom(request, response)%></p></card></wml>