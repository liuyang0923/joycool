<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<img src="img/logo_no.gif" alt="aa"/><br/>
1.问：我购买的种子、化肥放在什么地方？<br/>
答：点击页面下方工具栏的包裹，会看见你购买的种子与化肥。<br/>
2.问：如何添加《黄金农场》里的好友？<br/>
答：《黄金农场》里的好友是和乐酷好友相通的，只要您的乐酷好友玩《黄金农场》就会在好友列表里显示；同样，要添加新的好友只要在乐酷中加其为好友并邀请他来玩《黄金农场》即可。<br/>
3.问：怎么才能增加经验值？<br/>
答：种植、收获等动作都会增加经验值。<br/>
4.问：怎么进入好友的农场并搞破坏啊？<br/>
答：通过点击好友—好友名称即可进入好友的农场，点击“放虫”可以放害虫，点击“放草”可以种杂草，或是发现好友的种子成熟后点击“摘取”，去摘取TA的果实。<br/>
5.问：我的土地长草长虫了怎么办？<br/>
答：当我们的好友来我们的土地搞破坏——种杂草和放害虫时，此时我们可以手动清除，否则会严重影响我们作物的生长。<br/>
6.问：怎么收获已成熟的果实？<br/>
答：等我们的果实成熟了我们点击采摘即可收取果实，会自动存放到我们的仓库。<br/>
7.问：可多次收获的作物在作物首次成熟之后我需要做什么动作吗？<br/>
在作物首次成熟之后，我们只要收获了即可，什么都不必做，当然最基本的除草除虫的动作还是要的，随后和其首次成熟一样。<br/>
8.问：为什么好友的作物已经成熟了可是我不能摘取？<br/>
答：好友的作物成熟后，不仅每个作物每人只能摘取一次，而且他人摘取的总量也是有限制的。<br/>
9.问：关于多季作物的含义？<br/>
答：是指该作物种植一次后可以多次收获 。<br/>
10.问：关于“果实xx/xx”是什么意思？<br/>
答：“果实xx/xx”表示该作物成熟了，/后边的xx是指该作物产出的总数量，/前边的xx是指该作物因虫、草的影响，或被好友摘过后，剩下的数量。作物成熟后，请及时收获到仓库中，以免被摘走；收获果实后，到仓库里将其卖出可获得金币。<br/> 
11.问：关于摘取好友的果实<br/>
答：摘取好友的果实不加经验值。<br/>
一块地每次被好友摘走的果实个数是随机的。每块地每次成熟时最多能被摘掉的产量是有限的，当一定数量的果实被好友摘走时，其他好友就不能再摘了。<br/>
12.问：关于经验与级别<br/>
答：经验每增加到一定数值，等级就会提升一级。播种每次动作可增加一定的经验。<br/>
13.问：如何种植作物<br/>
答：到商店买种子，然后种在已经开垦好的土地里，作物会自动生长；需及时除草、杀虫，否则作物的健康值下降会影响作物的产量；<br/>
<a href="s.jsp">返回农场首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>