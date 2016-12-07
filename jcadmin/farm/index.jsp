<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@include file="top.jsp"%>
			<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
	新手地图管理后台<br/><br/>
	<a href="farmMap.jsp">地图管理</a><br/>
	<a href="farmMapNode.jsp">地图结点管理</a><br/>
	<a href="farmNpc.jsp">人物npc管理</a><br/>
	<a href="farmTalk.jsp">人物对话管理</a><br/>
	<a href="farmQuest.jsp">任务管理</a><br/>
	<a href="farmSkill.jsp">专业技能管理</a><br/>
	<a href="farmPro.jsp">专业管理</a><br/>
	<a href="farmCrop.jsp">农牧作物</a><br/>
	<a href="farmFactory.jsp">加工厂</a><br/>
	<a href="farmFactoryCompose.jsp">加工厂订单</a><br/>
	<a href="farmLand.jsp">采集的地图</a><br/>
	<a href="farmLandItem.jsp">采集的物品</a><br/>
	<a href="farmBook.jsp">书籍</a><br/>
	<a href="farmPick.jsp">任务宝箱</a>|<a href="farmPick2.jsp">大地图采集</a><br/>
	<a href="farmItem.jsp">物品</a>|
	<a href="farmEquip.jsp">装备</a>|
	<a href="farmItemSet.jsp">套装</a>|
	<a href="farmUsage.jsp">消耗品</a>|
	<a href="farmDrop.jsp">掉落</a><br/>
	<a href="farmShop.jsp">商店</a><br/>
	<a href="farmCreature.jsp">怪物</a><br/>
	<a href="farmCreatureSpawn.jsp">怪物生长区域</a><br/>
	<a href="farmStone.jsp">七彩石</a><br/>
	<a href="farmCollect.jsp">收藏品</a><br/>
	<a href="farmCar.jsp">驿站路线</a>|<a href="farmMapSign.jsp">路标</a><br/>
	<a href="farmTrigger.jsp">触发器</a><br/>
	<a href="itemCompose.jsp">合成公式查询</a><br/>
	
	----------运行情况--------<br/>
	<a href="land.jsp?id=1">采集场1</a>|<a href="land.jsp?id=2">采集场2</a><br/>
	<form action="viewUser.jsp" method="get">
	id:<input type=text name="id">
	<input type=submit value="人物查询">
	</form>
	<form action="viewTong.jsp" method="get">
	id:<input type=text name="id">
	<input type=submit value="门派查询">
	</form>
	</body>
</html>