<%@ page contentType="text/html;charset=utf-8" %><%@include file="filter.jsp"%><%!
static String[] userAdminType = {"chata","foruma","homea","tonga","maila","teama","newsa","infoa","gamea","admin","op","foruma2","newsa2"};
static String[] userAdminName = {"聊天监察","论坛监察","家园监察","帮会监察","信件监察","圈子监察","新闻监察","个人资料监察","游戏监察","管理员","op允许","论坛管理","新闻管理"};
static String[] userForbidType = {"","2","3","4","5","6","7","8","9","10"};
static String[] userForbidName = {"发言","发信件","发贴","家园留言","帮会","圈子","个人资料","游戏"};
%><html><head>
<title>乐酷后台功能列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="js/xtree.js"></script>
<script type="text/javascript" src="js/xmlextras.js"></script>
<script type="text/javascript" src="js/xloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="js/xtree.css" />
<link href="farm/common.css" rel="stylesheet" type="text/css">
<style type="text/css">
 
body {
	background:	"#F0F0F0";
	border-right:1px solid gray;
}
td {
	border:0px;
}

</style>
</head>
<body style="font-size:14;margin-right:0px;margin-left:3px;" onload="document.qu.id.focus();">
<p>&nbsp;管理员:<a href="perm/set.jsp" target=mainFrame><%=adminUser.getName()%></a>-<a href="/enter/jcadmin/logout.jsp">注销</a><br/>&nbsp;
<a href="#" onclick="top.leftFrame.location.reload();return false;">刷新树</a>&nbsp;<a href="#" onclick="top.mainFrame.location.reload();return false;">刷新页面</a></p>

<script type="text/javascript">

/// XP Look
webFXTreeConfig.rootIcon				= "js/images/root.gif";
webFXTreeConfig.openRootIcon		= "js/images/xp/openfolder.png";
webFXTreeConfig.folderIcon			= "js/images/xp/folder.png";
webFXTreeConfig.openFolderIcon		= "js/images/xp/openfolder.png";
webFXTreeConfig.fileIcon				= "js/images/xp/file.png";
webFXTreeConfig.lMinusIcon			= "js/images/xp/Lminus.png";
webFXTreeConfig.lPlusIcon				= "js/images/xp/Lplus.png";
webFXTreeConfig.tMinusIcon			= "js/images/xp/Tminus.png";
webFXTreeConfig.tPlusIcon				= "js/images/xp/Tplus.png";
webFXTreeConfig.iIcon					= "js/images/xp/I.png";
webFXTreeConfig.lIcon					= "js/images/xp/L.png";
webFXTreeConfig.tIcon					= "js/images/xp/T.png";
webFXTreeConfig.blankIcon				= "js/images/blank.png";

//var tree = new WebFXLoadTree("WebFXLoadTree", "tree1.xml");
//tree.setBehavior("classic");

function insertNode(parentNode,childNode,url, target)
{
	if(target==null)
		childNode.target="mainFrame";
	else
		childNode.target=target;
	childNode.action=url;
	if(parentNode)
		parentNode.add(childNode);
}

var node_root = new WebFXTree("乐酷管理后台");
node_root.openIcon="js/images/root.gif";
node_root.action="manage.jsp";
node_root.target="mainFrame";
<%if(group.isFlag(4)){%>
	var user=new WebFXTreeItem("用户相关");
	insertNode(node_root,user,"user/queryUserInfo.jsp");
<%if(group.isFlag(1)){%>
		var user1=new WebFXTreeItem("道具管理");
		insertNode(user,user1,"auction/userProp.jsp");
		var user2=new WebFXTreeItem("桃花源道具管理");
		insertNode(user,user2,"auction/userProp2.jsp");
<%}%>
		var user3=new WebFXTreeItem("查询用户发言");
		insertNode(user,user3,"searchUserPost.jsp");
		var user3=new WebFXTreeItem("查询用户信件");
		insertNode(user,user3,"searchUserMessage.jsp");
		var user4=new WebFXTreeItem("行囊赠送记录");
		insertNode(user,user4,"user/userBagPresent.jsp");
		var user5=new WebFXTreeItem("银行日志");
		insertNode(user,user5,"user/moneyLog.jsp");
		var user6=new WebFXTreeItem("修改用户信息");
		insertNode(user,user6,"alterUserInfo.jsp");
<%}%>
	var admin=new WebFXTreeItem("监察和管理");
	insertNode(node_root,admin,"user/queryUserInfo.jsp");
		var admin1=new WebFXTreeItem("分类封禁管理");
		insertNode(admin,admin1,"chatmanage.jsp");
		<%for(int i=0;i<userForbidName.length;i++){
	%>		var admin1<%=i%>=new WebFXTreeItem("<%=userForbidName[i]%>");
			insertNode(admin1,admin1<%=i%>,"forbuser<%=userForbidType[i]%>.jsp");<%
		}%>
		var admin2=new WebFXTreeItem("封禁用户ID");
		insertNode(admin,admin2,"forbuserId.jsp");
		var admin3=new WebFXTreeItem("玩家监察管理");
		insertNode(admin,admin3,"adminuser.jsp?group=chata");
		<%for(int i=0;i<userAdminName.length;i++){
	%>		var admin3<%=i%>=new WebFXTreeItem("<%=userAdminName[i]%>");
			insertNode(admin3,admin3<%=i%>,"adminuser.jsp?group=<%=userAdminType[i]%>");<%
		}%>
		var admin4=new WebFXTreeItem("十大恶人管理");
		insertNode(admin,admin4,"top/blackList.jsp");
		var admin4=new WebFXTreeItem("荣誉市民管理");
		insertNode(admin,admin4,"top/credit.jsp");
		var admin4=new WebFXTreeItem("网站热心人管理");
		insertNode(admin,admin4,"beginner/beginnerManage.jsp");
		var admin4=new WebFXTreeItem("家园照片管理");
		insertNode(admin,admin4,"home/viewHomePhone.jsp");
		var admin5=new WebFXTreeItem("最近论坛审查");
		insertNode(admin,admin5,"user/forums.jsp");
		var admin6=new WebFXTreeItem("最近聊天审查");
		insertNode(admin,admin6,"chat/latest.jsp");
		var admin7=new WebFXTreeItem("乐乐卖场管理");
		insertNode(admin,admin7,"mall/index.jsp");
<%if(group.isFlag(5)){%>
	var other=new WebFXTreeItem("其他后台");
	insertNode(node_root,other,"user/index.jsp");
		var other1=new WebFXTreeItem("论坛版主管理");
		insertNode(other,other1,"jcforum/index.jsp");
<%if(group.isFlag(1)){%>
		var other2=new WebFXTreeItem("股市分红管理");
		insertNode(other,other2,"stockNewChange.jsp");
		var other3=new WebFXTreeItem("股票公告管理");
		insertNode(other,other3,"stock2/stockNotice.jsp");
		var other4=new WebFXTreeItem("帮会发工资");
		insertNode(other,other4,"tong/weekSalary.jsp");
		var other5=new WebFXTreeItem("帮会基金管理");
		insertNode(other,other5,"tong/index.jsp");
<%}%>
		var other6=new WebFXTreeItem("漂流瓶管理");
		insertNode(other,other6,"beacon/bottle.jsp");
		var other7=new WebFXTreeItem("心情管理");
		insertNode(other,other7,"beacon/mood.jsp");
		var other8=new WebFXTreeItem("圈子查看");
		insertNode(other,other8,"user/teams.jsp");
		var other9=new WebFXTreeItem("缘分测试");
		insertNode(other,other9,"beacon/question.jsp");
<%if(group.isFlag(1)){%>
		var other11=new WebFXTreeItem("采集岛");
		insertNode(other,other11,"garden/index.jsp");
<%}%>
<%}%>
<%if(group.isFlag(2)){%>
	var adv=new WebFXTreeItem("高级管理");
	insertNode(node_root,adv,"manage.jsp");
<%if(group.isFlag(1)){%>
<%if(group.isFlag(0)){%>
		var perm=new WebFXTreeItem("权限管理");
		insertNode(adv,perm,"perm/index.jsp");
		var app=new WebFXTreeItem("插件管理");
		insertNode(adv,app,"app/index.jsp");
<%}%>
		var farm=new WebFXTreeItem("桃花源");
		insertNode(adv,farm,"farm");
		var castle=new WebFXTreeItem("城堡战争");
		insertNode(adv,castle,"castle");
		var stat=new WebFXTreeItem("金融统计");
		insertNode(adv,stat,"stat");
		var sms=new WebFXTreeItem("注册短信");
		insertNode(adv,sms,"stat/sms.jsp");
		var adv5=new WebFXTreeItem("商城");
		insertNode(adv,adv5,"shop/shop.jsp");
		var adv6=new WebFXTreeItem("酷秀");
		insertNode(adv,adv6,"kshow/index.jsp");
		var st2=new WebFXTreeItem("物品图片");
		insertNode(adv,st2,"user/itemShow.jsp");
		var st3=new WebFXTreeItem("快捷通道");
		insertNode(adv,st3,"user/shortcut.jsp");
<%}%>
<%if(group.isFlag(0)){%>
		var st=new WebFXTreeItem("静态变量");
		insertNode(adv,st,"view/static.jsp");
		var cache=new WebFXTreeItem("旧缓存");
		insertNode(adv,cache,"cache/cacheAdmin.jsp");
		var cache2=new WebFXTreeItem("新缓存");
		insertNode(adv,cache2,"cache/cacheAdmin2.jsp?detail=1");
		var log2=new WebFXTreeItem("服务器");
		insertNode(adv,log2,"stat/linux.jsp");
		var texas=new WebFXTreeItem("德州扑克");
		insertNode(adv,texas,"wgame/texas/index.jsp");
<%}%>
		var log3=new WebFXTreeItem("实时人数统计");
		insertNode(adv,log3,"stat/online.jsp");
		var log1=new WebFXTreeItem("网站实时统计");
		insertNode(adv,log1,"log1.jsp");
		var sim=new WebFXTreeItem("短信猫");
		insertNode(adv,sim,"http://219.238.200.42/wappush/sim/start.jsp?fromTree=yes");
<%}%>
<%if(group.isFlag(6)){%>
	var edit=new WebFXTreeItem("编辑后台");
	insertNode(node_root,edit,"manage.jsp");
		var edit1=new WebFXTreeItem("NBA直播");
		insertNode(edit,edit1,"news/nba/admin.jsp");
		var edit2=new WebFXTreeItem("星座");
		insertNode(edit,edit2,"friend/astroIndex.jsp");
		var edit3=new WebFXTreeItem("搜友");
		insertNode(edit,edit3,"friend/search.jsp");
		var edit4=new WebFXTreeItem("求助");
		insertNode(edit,edit4,"friend/help/index.jsp");
		var edit5=new WebFXTreeItem("选美");
		insertNode(edit,edit5,"friend/match/index.jsp");
		var edit6=new WebFXTreeItem("世界杯竞猜");
		insertNode(edit,edit6,"wgame/worldcup/index.jsp");
		var edit7=new WebFXTreeItem("新闻后台");
		insertNode(edit,edit7,"jcforum/news.jsp");
		var edit8=new WebFXTreeItem("用户墙");
		insertNode(edit,edit8,"guest/wall/index.jsp");
		var edit9=new WebFXTreeItem("游铃");
		insertNode(edit,edit9,"game/index.jsp");
		var edit10=new WebFXTreeItem("家族后台");
		insertNode(edit,edit10,"fm/index.jsp");
		var edit10=new WebFXTreeItem("游乐园后台");
		insertNode(edit,edit10,"guest/guestInfo.jsp");
<%}%>
<%if(group.isFlag(3)){%>
	var not=new WebFXTreeItem("少用的后台");
	insertNode(node_root,not,"manage.jsp");
		var not1=new WebFXTreeItem("机会卡管理");
		insertNode(not,not1,"card/viewQuarry.jsp");
		var not2=new WebFXTreeItem("页面缓存");
		insertNode(not,not2,"stat/colCache.jsp");
<%if(group.isFlag(1)){%>
		var not3=new WebFXTreeItem("filter跳转");
		insertNode(not,not3,"view/redirect.jsp");
		var not4=new WebFXTreeItem("filter跳转wap2.0");
		insertNode(not,not4,"view/redirect2.jsp");
		var not5=new WebFXTreeItem("非游客模块");
		insertNode(not,not5,"view/loginmap.jsp");
		var not6=new WebFXTreeItem("游客页面");
		insertNode(not,not6,"view/nologin.jsp");
<%}%>
<%}%>
	//Write the hole Tree
	document.write(node_root);
</script>
<%if(group.isFlag(7)){%>
<table width="100%" align="center" style="border:0px">
    <form method="get" action="user/queryUserInfo.jsp" target="mainFrame">
	<tr>
		<td>
			<input type=text name="mobile" size="14">
			<input type=submit value="手机号">
		</td>
	</tr>
	</form>
	<form method="get" action="user/queryUserInfo.jsp" target="mainFrame" name="qu">
	<tr>
		<td>
			<input type=text name="id" size="12">
			<input type=submit value="搜索ID">
		</td>
	</tr>
	</form>
	<form method="get" action="user/queryUserInfo.jsp" target="mainFrame">
	<tr>
		<td>
			<input type=text name="nickname" size="16">
			<input type=submit value="昵称">
		</td>
	</tr>
	</form>
</table>
<%}%>
</body>
</html>