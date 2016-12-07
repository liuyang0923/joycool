<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="---俄罗斯轮盘---" ontimer="<%=response.encodeURL("/job/wheel/StartWheel.jsp")%>">
<%=BaseAction.getTop(request, response)%>
<%
String result = (String)session.getAttribute("result");
String num = (String)session.getAttribute("num");
String flag= (String)session.getAttribute("flag");
String userMoney=(String)session.getAttribute("userMoney");
int  randnum=0;
if(flag.equals("true")){
%>
<img src="/img/job/wheel/<%=num%>.gif" alt="图片"/><br/>
<%}else{
while(true){
randnum=new java.util.Random().nextInt(10);
if(randnum!=Integer.parseInt(num))
break;
}
%>
<img src="/img/job/wheel/<%=randnum%>.gif" alt="图片"/><br/>
<%}out.println(result);%>
<br/>再来一盘:<br/>
<timer value="100"/>
（3秒后跳转到俄罗斯轮盘页面）<br/>
<a href="/job/wheel/StartWheel.jsp">俄罗斯轮盘首页</a><br/>
您现在有<%=userMoney%>乐币<br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp" title="返回上一级">返回导航中心</a><br/>

<%=BaseAction.getBottom(request, response)%>
</card>
</wml>