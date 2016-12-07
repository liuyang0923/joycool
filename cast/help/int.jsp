<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	
	int type = Integer.parseInt(request.getParameter("t"));
	StringBuilder sb = new StringBuilder();
	switch(type) {
		case 1:
sb.append("地图介绍:<br/>在城堡战争中,你将和成千上万的玩家在虚拟世界里一起.尤其值得注意的是你城堡附近的玩家,可以通过点击“地图”观察到他们.");
		break;
		case 2:
sb.append("城堡介绍:<br/>每个玩家进入游戏都有一座属于自己的城堡，在城堡内可以建设自己的建筑和开发兵种。如果一个星期不进入城堡，帐号会被冻结。冻结期间，其他玩家无法攻击和支援你的城堡。进入城堡帐号点击解除冻结即可继续游戏。");
		break;
		case 3:
sb.append("资源介绍:<br/>每个城堡都有四种资源:木头,石头,铁块,粮食.在开始发展你的城堡之前，你需要兴建资源田来增加资源的供给。当你有一定经济基础，即资源达到一定数量的时候，你就可以着手开始兴建你的建筑。仓库和粮仓可以用来存放你的资源。山洞可以隐藏你的部分资源来防止敌方的抢夺。");
		break;
		case 4:
sb.append("兵种介绍:<br/>兵营建立后可以建造(有些兵种需要研发所研发之后才能训练)：<br/>长枪兵、弓箭手、剑兵和骑士。兵种的不同，消耗的资源也不同。<br/>长枪兵：<br/>长枪兵是城堡的最基础兵种，没有厚厚的护甲，也没有锋利的大家，最大的好处是训练费用非常便宜，大批的长枪兵一样具有威慑力！<br/>弓箭手：<br/>弓箭手装备弓作为远程武器，匕首作为近战武器。弓箭手过低的生命值和防御使他们在战斗中很容易成为炮灰型的角色。好在他们的造价比较便宜，所以积攒几十上百个的也可以给敌人带来不错的压力。<br/>剑士：<br/>物攻加强兵种。剑士要比长枪兵强很多，他们的铠甲和武器都很厚实。他们的攻击能力在中前期很强，会给对方造成很大伤害，因此，在战斗中应该尽量避免和剑士硬碰硬。<br/>骑士：<br/>物防加强兵种。骑士的攻防都相当不错，行军速度也较快的，可以利用其速度偷袭来给敌人施加压力，作为防御主力也游刃有余。唯一的缺点是造价太贵。");
		break;
		case 5:
sb.append("攻城介绍:<br/>当城堡拥有了自己的兵力之后，就可以攻打其他玩家的城堡。城堡之间的战争会消耗双方的兵力，胜利方可以掠夺对方的资源");
		break;
	}
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争游戏指南"><p><%=sb.toString() %><br/>
<a href="help.jsp">返回详细游戏指南</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>