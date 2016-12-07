<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int p = gardenAction.getParameterInt("p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(p==0) {%>
<img src="img/logo_no.gif" alt="a"/><br/>
欢迎来到黄金农场<br/>
在这里可以种植蔬莱，水果当个开心的农夫。<br/>
<a href="help.jsp?p=1">1.欢迎来当一个开心农夫</a><br/>
<a href="help.jsp?p=2">2.当一个好农夫还需要一点学问哦！</a><br/>
<a href="help.jsp?p=3">3.千万不要以为种地很简单！</a><br/>
<a href="help.jsp?p=4">4.还记得小学地理课上说得“多季作物”吗？</a><br/>
<a href="help.jsp?p=5">5.如何赚到钱</a><br/>
<a href="help.jsp?p=6">6.多去朋友那看看，会有更多的收获哦。</a><br/>
<a href="s.jsp">&lt;&lt;返回农场</a><br/>
<%} else if(p==1){%>
在这里可以种植蔬莱，水果当个开心的农夫。<br/>
1.选择商店链接，可以选购您需要的种子<br/>
2.点击土地旁的播种链接，将进入播种时的种子选择页面<br/>
3.点击所要种植作物后边的“选择”链接，即可完成播种<br/>
4.如果心情不好，也可以在其他人的园地做坏事哦。<br/>
<a href="help.jsp?p=2">当一个好农夫还需要一点学问哦！</a><br/>
<a href="help.jsp?p=0">更多帮助</a>|<a href="s.jsp">返回农场</a><br/>
<%} else if(p==2){%>
当一个好农夫还需要一点学问哦！<br/>
不同的作物有不同的成长周期，但大部分都分为五个阶段：发芽，小叶子，大叶子，开花，成熟。<br/>
<a href="help.jsp?p=3">千万不要以为种地很简单！</a><br/>
<a href="help.jsp?p=0">更多帮助</a>|<a href="s.jsp">返回农场</a><br/>
<%} else if(p==3){%>
千万不要以为种地很简单！<br/>
作物的每个阶段都需要好好照看，要小心病虫害和杂草，这些最后都会影响收成的。当土地上有害虫和杂草时，后边会出现除虫、除草的提示。<br/>
<a href="help.jsp?p=4">还记得小学地理课上说得“多季作物”吗？</a><br/>
<a href="help.jsp?p=0">更多帮助</a>|<a href="s.jsp">返回农场</a><br/>
<%} else if(p==4){%>
还记得小学地理课上说得“多季作物”吗？<br/>
有些作物只能收一季的，收获完就枯萎了。<br/>
有的作物可以收两季，还有能收三季的，他们是可以收获多次的。<br/>
购买种子时请注意说明。<br/>
<a href="help.jsp?p=5">如何赚到钱</a><br/>
<a href="help.jsp?p=0">更多帮助</a>|<a href="s.jsp">返回农场</a><br/>
<%} else if(p==5){%>
如何赚到更多的钱<br/>
作物收获后，您可以在仓库里面看到。将果实出售就变成金币了．<!-- 但是鲜花是不能够出售的，你可以将它做成礼物送给别人，要知道这可是你自己亲手种出来的鲜花啊！ --><br/>
<a href="help.jsp?p=6">多去朋友那看看，会有更多的收获哦。</a><br/>
<a href="help.jsp?p=0">更多帮助</a>|<a href="s.jsp">返回农场</a><br/>
<%} else if(p==6){%>
多去朋友那看看，会有更多的收获哦。<br/>
你可以查看你的好友列表，点击好友就可以看到他们的农场了。有到成熟的作物你也可以摘取一部分。看到还没有成熟的作物你可以放点草，扔点虫。嘿嘿，偶尔做一下坏事应该没什么的。同时，要是谁在你的好友那里放了虫草，你也可以帮忙除掉哦。园主会感谢你的哦。<br/>
<a href="help.jsp?p=0">更多帮助</a>|<a href="s.jsp">返回农场</a><br/>
<%} %>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>