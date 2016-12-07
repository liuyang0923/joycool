<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
if(request.getSession().getAttribute("loginUser")==null){
response.sendRedirect("/user/login.jsp");
return;
}
FarmAction action = new FarmAction(request);
FarmUserBean user = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源帮助">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(10,request,response)%>
<%if(user!=null&&user.getRank()>=6){%>
1.<a href="stone.jsp">七彩石</a><br/>
2.<a href="car.jsp">驿站远行</a><br/>
3.<a href="element.jsp">五行元素</a><br/>
4.<a href="creature.jsp">特殊怪物</a><br/>
5.<a href="quest.jsp">特殊任务</a><br/>
6.<a href="class.jsp">职业</a><br/>
7.<a href="group.jsp">组队</a><br/>
8.<a href="tong.jsp">门派</a><br/>
9.<a href="collect.jsp">收藏盒</a><br/>
0.<a href="pvp.jsp">pvp对战</a><br/>
A.<a href="pro2.jsp">各专业介绍</a><br/>
---------<br/><%}%>
1.<a href="bank.jsp">桃花钱庄</a><br/>
2.<a href="user.jsp">人物属性</a><br/>
3.<a href="pro.jsp">专业</a><br/>
4.<a href="mypro.jsp">专业技能</a><br/>
L.<a href="land.jsp">采集场</a><br/>
F.<a href="factory.jsp">加工厂</a><br/>
7.<a href="pro9.jsp">战斗属性分配</a><br/>
8.<a href="equips.jsp">装备</a><br/>
9.<a href="battle.jsp">战斗/休息</a><br/>
0.<a href="bmap.jsp">桃源村地图</a><br/>
<a href="/jcforum/forum.jsp?forumId=2878">+玩家交流区+</a><br/>
<a href="../index.jsp">返回桃花源首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>