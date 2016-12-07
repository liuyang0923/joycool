<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.stage.*,net.wxsj.action.stage.*,java.util.*,net.wxsj.util.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

StageAction action = new StageAction();
action.answer(request, response);

String result = (String) request.getAttribute("result");
//正确
if("right".equals(result)){
	String tip = (String) request.getAttribute("tip");
%>
<wml>
<card title="回答结果">
<p align="left">
恭喜你，答案正确，<%=tip%>！<br/>
<a href="index.jsp">接受其他任务</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//第二步
else if("wrong".equals(result)){
%>
<wml>
<card title="回答结果">
<p align="left">
哎呀~答错啦，这位老兄只能给你100乐币作为鼓励，记得下次看答案提示哦~~<br/>
<a href="index.jsp">接受其他任务</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//失败
else if("hasTry".equals(result)){
	String tip = (String) request.getAttribute("tip");
%>
<wml>
<card title="回答结果">
<timer value="30"/>
<p align="left">
你已经做过这个任务啦。<br/>
<a href="index.jsp">接受其他任务</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//失败
else if("failure".equals(result)){
	String tip = (String) request.getAttribute("tip");
%>
<wml>
<card title="回答结果">
<timer value="30"/>
<p align="left">
回答结果<br/>
--------------<br/>
<%=StringUtil.toWml(tip)%><br/>
<anchor>马上返回
<prev/>
</anchor><br/>
<a href="index.jsp">接受其他任务</a><br/>
</p>
</card>
</wml>
<%
	return;
}
%>