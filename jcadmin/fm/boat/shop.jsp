<%@ page language="java" import="java.text.*,net.joycool.wap.util.*,jc.family.game.boat.*,java.util.*" pageEncoding="utf-8"%>
<%
BoatAction action = new BoatAction(request,response);
String[] boatType = {"普通","爆烈龙舟","尖锋龙舟","凤凰龙舟"};
int del = action.getParameterInt("del");
if(del > 0){
	BoatAction.service.upd("delete from fm_game_boat where id="+del);
	request.setAttribute("tip","删除成功!");
}
if(1==action.getParameterInt("a")){
	String name = action.getParameterString("name");// 名字不能为空
	String bak = action.getParameterString("bak");
	int id = action.getParameterInt("id");
	int type = action.getParameterInt("type");
	int speed = action.getParameterInt("speed");
	int maxspeed = action.getParameterInt("maxspeed");
	int anglereset = action.getParameterInt("anglereset");
	float rent = action.getParameterFloat("rent");
	int renttype = action.getParameterInt("renttype");
	int point = action.getParameterInt("point");
	int useTime = action.getParameterInt("useTime");
	if(name != null && name.length() > 0){
		BoatBean bb = new BoatBean();
		bb.setName(name);
		bb.setBoatType(type);
		bb.setSpeed(speed);
		bb.setMaxSpeed(maxspeed);
		bb.setSpeAngleReset(anglereset);
		bb.setRent(rent);
		bb.setRentType(renttype);
		bb.setPoint(point);
		bb.setUseTime(useTime);
		bb.setBak(bak);
		bb.setId(id);
		if(id > 0){
			if(id == 1){
				bb.setName("传统龙舟");
				bb.setBoatType(0);
				bb.setSpeAngleReset(0);
				bb.setRent(0);
				bb.setRentType(0);
				bb.setPoint(0);
				bb.setUseTime(1);
				bb.setBak("");
			}
			BoatAction.service.alterBoat(bb);
			request.setAttribute("tip","修改成功!");
		} else {
			BoatAction.service.insertBoat(bb);
			response.sendRedirect("shop.jsp?a=2");
			return;
		}
	}else{
		response.sendRedirect("addboat.jsp?a=1");
		return;
	}
}
if(1==action.getParameterInt("a")){
	request.setAttribute("tip","添加成功!");
}
List list = BoatAction.service.getBoatList("1 order by id desc");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>龙舟商店</title>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
	<a href="addboat.jsp">添加龙舟</a><br>
	<table border="1">
		<tr>
			<td align="center">龙舟名</td>
			<td align="center">龙舟类型</td>
			<td align="center">加速效果(米/分钟)</td>
			<td align="center">最高速度(米)</td>
			<td align="center">复位功能</td>
			<td align="center">价格</td>
			<td align="center">可使用次数</td>
			<td align="center">积分限制</td>
			<td align="center">操作</td>
		</tr>
		<%
			if(list != null && list.size() > 0){
				DecimalFormat numFormat = new DecimalFormat("#.#");
				for(int i=0;i<list.size();i++){
					BoatBean bean = (BoatBean)list.get(i);
					if(bean != null){
						%>
						<tr>
							<td align="center"><%=StringUtil.toWml(bean.getName())%></td>
							<td align="center"><%=boatType[bean.getBoatType()]%></td>
							<td align="center"><%=bean.getSpeed()%></td>
							<td align="center"><%=bean.getMaxSpeed()%></td>
							<td align="center"><%=bean.getSpeAngleReset()==0?"无":"有"%></td>
							<td align="center"><%if(bean.getRentType()==0){%><%=numFormat.format(bean.getRent())%>亿乐币<%}else{%><%=numFormat.format(bean.getRent())%>酷币<%}%></td>
							<td align="center"><%=bean.getUseTime()%></td>
							<td align="center"><%=bean.getPoint()==0?"无限制":bean.getPoint()%></td>
							<td align="center"><%if(bean.getId()!=1){%><a href="shop.jsp?del=<%=bean.getId()%>">删除</a>|<%}%><a href="addboat.jsp?id=<%=bean.getId()%>">修改</a></td>
						</tr>
						<%
					}
				}
			}
		 %>
	</table>
	<a href="boat.jsp">返回龙舟管理</a><br>
	<a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>