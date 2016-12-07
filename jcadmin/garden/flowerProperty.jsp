<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static FlowerService service =new FlowerService(); %>
<html>
	<head>
		<title>花之境属性管理</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%	FlowerAction action = new FlowerAction(request);
	int type = action.getParameterInt("t");
	int flowerId = action.getParameterInt("fid");
	FlowerBean fb = null;  
	if (type == 1){			//隐藏
		SqlUtil.executeUpdate("update flower_property set state = 0 where id =" + flowerId,5);
		FlowerUtil.resetFlowerMap();
	} else if (type == 2) {	//显示
		SqlUtil.executeUpdate("update flower_property set state = 1 where id =" + flowerId,5);
		FlowerUtil.resetFlowerMap();
	}
	List flowerList = service.getFlowerList("1");
%>	<a href="flowerIndex.jsp"><--返回首页</a><br/>
	增加数据:<br/>
	<form method="post" action="flowerCreate.jsp">
		花&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名&nbsp;:<input type="text" name="name"/><font color="red">*</font>
		价&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;格&nbsp;:<input type="text" name="price"/><br/>
		时&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间&nbsp;:<input type="text" name="time"/>
		&nbsp;公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;式&nbsp;:<input type="text" name="comp"/><br/>
		类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别&nbsp;:<input type="text" name="type"/><font color="red">*</font> 1=普通;2=精品;3=珍贵;4=稀有;5=特殊<br/>
		合&nbsp;成&nbsp;时&nbsp;间&nbsp;:<input type="text" name="compTime"/><br/>
		成就(合成后):<input type="text" name="successExp"/><br/>
		成就(种植后):<input type="text" name="growExp"/><br/>
		<input type="submit" value="创  建" onClick="return confirm('确定创建?')"/>
	</form>
	<table border=1 width=100% align=center>
		<tr bgcolor=#C6EAF5>
			<td align=center><font color=#1A4578>ID</font></td>
			<td align=center><font color=#1A4578>花名</font></td>
			<td align=center><font color=#1A4578>价格</font></td>
			<td align=center><font color=#1A4578>种植时间(单位：秒)</font></td>
			<td align=center><font color=#1A4578>状态</font></td>
			<td align=center><font color=#1A4578>合成公式</font></td>
			<td align=center><font color=#1A4578>类别</font></td>
			<td align=center><font color=#1A4578>合成时间(秒)</font></td>
			<td align=center><font color=#1A4578>成就(合成后)</font></td>
			<td align=center><font color=#1A4578>成就(种植后)</font></td>
			<td align=center><font color=#1A4578>操作</font></td>
		</tr>
		<% for (int i=0;i<flowerList.size();i++){
				fb = (FlowerBean)flowerList.get(i);
				%><tr><td><%=fb.getId()%></td><%
				%><td><img src="../../garden/f/img/<%=fb.getId()%>.gif"/><%=fb.getName()%></td><%
				%><td><%=fb.getPrice()%></td><%
				%><td><%=fb.getTime()%></td><%
				if (fb.getState() == 1){
					%><td align=center><font color=blue>显示</font></td><%
				} else {
					%><td align=center><font color=gray>隐藏</font></td><%
				}
				%><td><%=action.getCompoundExp(fb.getCompose())%></td><%
				%><td><%=fb.getType()%></td><%
				%><td><%=fb.getCompTime()%></td><%
				%><td><%=fb.getSuccessExp()%></td><%
				%><td><%=fb.getGrowExp()%></td><%
				%><td><a href="flowerProModify.jsp?m=1&fid=<%=fb.getId()%>">改</a>&nbsp;
				<% if (fb.getState() == 1){
						%><a href="flowerProperty.jsp?fid=<%=fb.getId()%>&t=1">隐</a><%
				   } else { 
						%><a href="flowerProperty.jsp?fid=<%=fb.getId()%>&t=2">显</a><%
				   }%>
				</td></tr><%
		   }%>	
	</table>
	</body>
</html>