<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.action.job.HuntAction"%><%@ page import="java.util.*"%><%!
	static class UserComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			int[] c1 = (int[]) o1;
			int[] c2 = (int[]) o2;
			if (c1[1] < c2[1]) {
				return 1;
			} else {
				if (c1[1] == c2[1]) {
					return 0;
				} else {
					return -1;
				}
			}
		}
	}
	static UserComparator comp = new UserComparator();
	static String[] names = {"打猎", "当铺", "开仓", "钓鱼","机会卡","信件","聊天","浮生记"};
	static int[] max = {50,100,10,50,50,100,100,50};
%><%
	CustomAction action = new CustomAction(request);
	int type = action.getParameterInt("type");
	if(type < 0 || type >= names.length)
		type = 0;
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3><%=names[type]%>数据</h3>
<table cellpadding=2><tr><td>用户名</td><td>次数</td><td></td><td></td><td></td><td>ip</td><td>ua</td></tr>
<%
	HashMap map=null;
	if(type == 1)
		map = TongAction.countMap;
	else if(type == 2)
		map = TongAction.countMap2;
	else if(type == 3)
		map = net.joycool.wap.action.job.fish.FishAction.countMap;
	else if(type == 4)
		map = net.joycool.wap.action.job.CardAction.countMap;
	else if(type == 5)
		map = CountMaps.countMap1;
	else if(type == 6)
		map = CountMaps.countMap2;
	else if(type == 7)
		map = CountMaps.countMap3;
	else
		map = HuntAction.countMap;
	List list = new ArrayList(map.size());
	Iterator iter2 = map.entrySet().iterator();
	while(iter2.hasNext()) {
		Map.Entry e = (Map.Entry)iter2.next();
		int[] tmp = {((Integer)e.getKey()).intValue(), ((int[])e.getValue())[0]};
		list.add(tmp);
	}
	Collections.sort(list, comp);
	Iterator iter = list.iterator();
	while(iter.hasNext()) {
		int[] iid = (int[])iter.next();;
		if(iid[1]<max[type]) continue;
		UserBean user=UserInfoUtil.getUser(iid[0]);
%>
<tr><td><%if(user!=null){%>(<%=iid[0]%>)<a href="../user/queryUserInfo.jsp?id=<%=iid[0]%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId2(iid[0])%>)<%}else{%>(未知)<%}%></td>
<td align=right><%=iid[1]%></td>
<td align=right></td>
<td align=right></td>
<td align=right></td>
<td><%=user.getIpAddress()%></td><td width="300"><%=user.getUserAgent()%></td>
</tr>
<%}%>
</table>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>