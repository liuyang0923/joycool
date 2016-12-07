<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@include file="filter.jsp"%><%

%><html>
<link href="farm/common.css" rel="stylesheet" type="text/css">
<body>
<table cellpadding=5 border=3 cellspacing=5 style="line-height: 150%">
<tr><td width="200">
<%if(group.isFlag(4)){%>
<%if(group.isFlag(1)){%>
<a href="stock2/stock.jsp">给用户分配股票</a><br/>
<a href="/joycool-admin/">网站后台</a><br/>
<%}%>

<%--<a href="tong/chat/chatInfo.jsp">帮会聊天室管理后台</a><br/>--%>
<a href="forbid.jsp">门户封禁</a><br/>
<a href="online.jsp">门户在线</a><br/>
<%--<a href="systemNotice.jsp">系统公告</a><br/>
<a href="tongNotice.jsp">帮会公告</a><br/>
<a href="rumorList.jsp">游戏战况</a><br/>
<a href="/WapNews/admin/">新闻管理</a><br/>
a href="adebook/adebook.jsp">ebook广告添加</a><br/--%>
<%--<a href="http://blog.joycool.netforbid.jsp">博客封禁</a><br/>
<a href="http://blog.joycool.netonline.jsp">博客在线</a><br/>
<a href="yichaLog.jsp">易查统计</a><br/>
<a href="forum/forumIndex.jsp?id=14">贴图中心内容管理</a><br/>
<a href="forum/forumIndex.jsp?id=15">客服留言管理</a><br/>
<a href="notice/addNotice.jsp">通知管理</a><br/>--%>
贴图中心内容管理<br/>
客服留言管理<br/>
通知管理<br/>
<a href="friendadver/friendAdverIndex.jsp">交友中心内容管理</a><br/>
<%--<a href="testStat.jsp">成人用品问卷调查结果</a><br/>--%>
<a href="clearCardMap.jsp">清空机会卡数据</a><br/>
<a href="clearHuntMap.jsp">清空打猎数据</a><br/>
<a href="hunt/viewQuarry.jsp">猎物管理</a><br/>
<a href="hunt/viewHuntTask.jsp">显示管理打猎活动</a><br/>
<a href="chat/inviteIndex.jsp">邀请好友来乐酷</a><br/>
<a href="job/psychology/index.jsp">心理测试题目管理</a><br/>
<%--<a href="randomRoomId.jsp">随机聊天室管理</a><br/>--%>
</td><td>
<%--<a href="userLoginChange.jsp">五步走管理后台</a><br/>--%>
<a href="stat/newreg.jsp"><font color=red>新用户留存率统计</font></a><br/>
<a href="job/happycard/stat/index.jsp">乐酷贺卡管理后台</a><br/>
<a href="todayStar.jsp">指定今日之星</a><br/>
<a href="commendDiaryPhoto.jsp">推荐日记相片</a><br/>

<a href="advice/adviceManager.jsp">页面广告后台</a><br/>

</td><td width=200 valign=top>
	<br>
<%if(group.isFlag(0)){%>
<a href="view/session.jsp">session查询</a><br/><br/>
<a href="money/quota.jsp"><font color=red>乐币/道具发放配额管理</font></a><br/>
<br/>
<%}%>
<a href="user/applys.jsp">申诉箱</a>
<%}%>
</td></tr>
</table>
</body>
</html>