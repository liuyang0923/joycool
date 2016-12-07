<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.Random"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/zippo/game/index.jsp"));
	return;
}

Random rand = new Random();
int star1Id = rand.nextInt(11) + 1;
int star2Id = rand.nextInt(11) + 1;
while(star2Id == star1Id){
	star2Id = rand.nextInt(11) + 1;
}
int zippo1Id = rand.nextInt(21) + 1;
int zippo2Id = rand.nextInt(21) + 1;
while(zippo2Id == zippo1Id){
	zippo2Id = rand.nextInt(21) + 1;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猜名人专用火机">
<p align="left">
猜名人专用火机<br/>
-------------------<br/>
<a href="/wxsj/zippo/game/pairList.jsp">您已经确认的猜测</a><br/>
=明星专区=<br/>
先了解名人再猜他们的专用zippo(芝宝)：<br/>
<img src="/wxsj/zippo/images/star/thumbnail/<%=star1Id %>.gif" alt="图片"/>&nbsp;<img src="/wxsj/zippo/images/star/thumbnail/<%=star2Id %>.gif" alt="图片"/><br/>
<a href="/wxsj/zippo/game/star.jsp?id=1">1.刘德华</a>&nbsp;<a href="/wxsj/zippo/game/star.jsp?id=3">2.周杰伦</a><br/>
<a href="/wxsj/zippo/game/star.jsp?id=5">3.张铁林</a>&nbsp;<a href="/wxsj/zippo/game/star.jsp?id=8">4.庞龙</a><br/>
<a href="/wxsj/zippo/game/star.jsp?id=4">5.周星驰</a>&nbsp;<a href="/wxsj/zippo/game/star.jsp?id=2">6.张曼玉</a><br/>
<a href="/wxsj/zippo/game/star.jsp?id=7">7.李咏</a>&nbsp;<a href="/wxsj/zippo/game/star.jsp?id=6">8.贝克汉姆</a><br/>
=zippo（芝宝）专区=<br/>
先熟悉zippo（芝宝）再猜名人：<br/>
<img src="/wxsj/zippo/images/zippo/thumbnail/<%=zippo1Id %>.gif" alt="图片"/>&nbsp;<img src="/wxsj/zippo/images/zippo/thumbnail/<%=zippo2Id %>.gif" alt="图片"/><br/>
<a href="/wxsj/zippo/game/zippoList.jsp?typeId=4">军旅风格</a>&nbsp;<a href="/wxsj/zippo/game/zippoList.jsp?typeId=1">都市风格</a>&nbsp;<a href="/wxsj/zippo/game/zippoList.jsp?typeId=6">车迷一族</a><br/>
<a href="/wxsj/zippo/game/zippoList.jsp?typeId=2">成功人士</a>&nbsp;<a href="/wxsj/zippo/game/zippoList.jsp?typeId=5">青春魅力</a>&nbsp;<a href="/wxsj/zippo/game/zippoList.jsp?typeId=3">个性人类</a><br/>
=zippo（芝宝）常识=<br/>
了解这些可以增加你的中奖机率哦~<br/>
<a href="/wxsj/zippo/intro.jsp">*zippo（芝宝）简介</a><br/>
zippo是世界知名的经典打火机，其历史悠久，设计独特...<br/>
<a href="/wxsj/zippo/history.jsp">*zippo（芝宝）历史</a><br/>
zippo 是美国人乔治·布雷斯代在1932年发明的，迄今有60多年的历史了...<br/>
<a href="/wxsj/zippo/story.jsp">*zippo（芝宝）故事</a><br/>
60多年来，Zippo时刻都在发生着让人难以忘怀的动人故事...<br/>
<a href="/wxsj/zippo/rule.jsp">具体奖励和答题规则</a><br/>
 <%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>