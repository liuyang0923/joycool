<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static FlowerService service =new FlowerService();%>
<%  FlowerAction action = new FlowerAction(request);
	int backId = action.getParameterInt("b");
	int uid = action.getParameterInt("uid");
	UserBean user = UserInfoUtil.getUser(uid);
	List showList = new ArrayList();
	int mode = 0;
	if ( backId == 2 ){
		showList = service.getUserStore("user_id = " + uid + " and type = 1");
	} else if ( backId == 3 ){
		showList = service.getUserStore("user_id = " + uid + " and type = 2 ");
	}
%>
<html>
	<head>
		<title>花之境数据列表</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<table width="100%" style="border: none;">
			<tr>
				<td style="border: none;" width="11%">
					<% if ( backId == 2 ){ 
							%>以下是<a href="../user/queryUserInfo.jsp?id=<%=uid%>"><%=user.getNickNameWml()%></a>的所有种子信息&nbsp;<%
							mode = 1;
					   } else if ( backId == 3 ){
					        %>以下是<a href="../user/queryUserInfo.jsp?id=<%=uid %>"><%=user.getNickNameWml()%></a>的所有花朵信息&nbsp;<%
					        mode = 2;
					   }%>					
				</td>
				<td style="border: none;" width="30%"><form method = "post" action="flowerIndex.jsp?uid=<%=uid %>">
				  	<input type = "submit" value ="<--返回首页"/>
				</form></td>
			  </tr>
		</table>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center><font color=#1A4578>ID</font></td>
				<td align=center><font color=#1A4578>花朵编号</font></td>
				<td align=center><font color=#1A4578>花/种子名</font></td>
				<td align=center><font color=#1A4578>数量</font></td>
				<td align=center><font color=#1A4578>操作</font></td>
			</tr>
			<% FlowerStoreBean fsb = null;
			   for (int i =0;i < showList.size();i++){
			   		fsb = (FlowerStoreBean)showList.get(i);
					%><tr><td><%=fsb.getId()%></td><%
					%><td><%=fsb.getFlowerId()%></td><%
					%><td><img src="../../garden/f/img/<%=fsb.getFlowerId()%>.gif"/><%=FlowerUtil.getFlower(fsb.getFlowerId()).getName()%></td><%
					%><td><%=fsb.getCount()%></td><%
					%><td><a href="flowerOperation.jsp?mode=<%=mode%>&ope=1&uid=<%=uid%>&b=<%=backId%>&fid=<%=fsb.getFlowerId()%>&c=<%=fsb.getCount()%>">加↑</a>&nbsp;<a href="flowerOperation.jsp?mode=<%=mode%>&ope=2&uid=<%=uid%>&b=<%=backId%>&fid=<%=fsb.getFlowerId()%>&c=<%=fsb.getCount()%>">减↓</a></td></tr><%
			   }%>
		</table>
	</body>	
</html>