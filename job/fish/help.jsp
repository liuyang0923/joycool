<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.fish.*" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FishAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt=""/><br/>
欢乐渔场游戏说明<br/>
------------------<br/>
在欢乐渔场中，你不但可以在钓到鱼后当场拿到兑换的乐币。还能享受跟现实相仿的钓鱼乐趣。<br/>
1、随机事件<br/>
在渔场的三个区域内，会不时出现一些事件，如天气变换，水质污染等，这些事件影响着鱼儿上钩的概率，请注意渔场动态，随时改变你的垂钓地点，以便钓到更多的鱼~<br/>
2、鱼儿上钩<br/>
既然是钓鱼，当然会考验你的运气了，可能有鱼上钩可能什么动静也没有，有鱼上钩的时候系统会提示你赶紧拉竿，没有鱼上钩的话你只好再继续垂钓了。<br/>
2、拉竿方式<br/>
在钓鱼过程中，每当有鱼上钩时，系统会提示玩家鱼儿咬钩的消息，你可根据提示消息来判断采用何种拉竿方式。共有四项拉竿方式可以选择。对不同情况采用相应的拉竿方式，有助于提高垂钓的成功率。<br/>
5、特殊偶遇：<br/>
　在钓鱼过程中，你有时会遇到河妖，女鬼，还有出没不定的海盗，他们会可能会带给你一些特殊的收获，也可能会造成你很重大的损失哦。<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>