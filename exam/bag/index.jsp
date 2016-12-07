<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static int COUNT_PRE_PAGE = 10; %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<% response.setHeader("Cache-Control","no-cache");
%><wml><card title="备战考试"><p><%=BaseAction.getTop(request, response)%>
==备战考试==<br/>
好好学习 天天向上<br/>
我的书包(开发中)<br/>
好友的书包(开发中)<br/>
->复习资料库<br/>
<a href="lib.jsp?s=1&amp;f=1">初中数学</a>|<a href="lib.jsp?s=1&amp;f=2">高中数学</a><br/>
<a href="lib.jsp?s=2&amp;f=1">初中语文</a>|<a href="lib.jsp?s=2&amp;f=2">高中语文</a><br/>
<a href="lib.jsp?s=3&amp;f=1">初中英语</a>|<a href="lib.jsp?s=3&amp;f=2">高中英语</a><br/>
<a href="lib.jsp?s=4&amp;f=1">初中物理</a>|<a href="lib.jsp?s=4&amp;f=2">高中物理</a><br/>
<a href="lib.jsp?s=5&amp;f=1">初中化学</a>|<a href="lib.jsp?s=5&amp;f=2">高中化学</a><br/>
<a href="lib.jsp?s=6&amp;f=1">初中生物</a>|<a href="lib.jsp?s=6&amp;f=2">高中生物</a><br/>
<a href="lib.jsp?s=7&amp;f=1">初中历史</a>|<a href="lib.jsp?s=7&amp;f=2">高中历史</a><br/>
<a href="lib.jsp?s=8&amp;f=1">初中政治</a>|<a href="lib.jsp?s=8&amp;f=2">高中政治</a><br/>
<a href="lib.jsp?s=9&amp;f=1">初中地理</a>|<a href="lib.jsp?s=9&amp;f=2">高中地理</a><br/>
<%=BaseAction.getBottomShort(request, response)%><br/>
</p></card></wml>