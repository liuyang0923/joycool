<%@ page language="java" import="java.text.*,net.joycool.wap.util.*,jc.family.game.boat.*" pageEncoding="utf-8"%>
<%
	BoatAction action = new BoatAction(request,response);
	String[] type = {"爆烈龙舟","尖锋龙舟","凤凰龙舟"};
	String[] reset = {"无","有"};
	String[] rent = {"亿乐币","酷币"};
	int id = action.getParameterInt("id");
	BoatBean bean = null;
	if(id > 0){
		bean = BoatAction.service.getBoatBean(" id="+id);
	}
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>添加龙舟</title>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <p style="color:red;"><%=1==action.getParameterInt("a")?"名称不能为空!":""%></p>
  	<form action="shop.jsp?a=1" method="post">
  	<%
  	if(bean != null){
		DecimalFormat numFormat = new DecimalFormat("0.0");
  	%>
  	<input type="hidden" name="id" value="<%=bean.getId()%>"/>
  	<table border="1">
	  	<tr>
	  		<td>龙舟名称:</td>
	  		<td>
	  			<%if(bean.getId() == 1){
	  				%>传统龙舟<input type="hidden" name="name" value="传统龙舟"/><%
	  			}else{
	  				%>
	  				<input type="text" name="name" value="<%=StringUtil.toWml(bean.getName())%>"/>
	  				<%
	  			}%>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>龙舟类型:</td>
	  		<td>
	  			<%if(bean.getId() == 1){
	  				%>默认<%
	  			}else{
	  				%>
		  			<select name="type">
		  				<%
		  					for(int i=0;i<type.length;i++){
		  						%><option value="<%=i+1%>" <%if((i+1)==bean.getBoatType()){%>selected="selected" <%}%>><%=type[i]%></option><%
		  					}
		  				 %>
		  			</select>
	  				<%
	  			}%>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>加速效果:</td>
	  		<td><input type="text" name="speed" value="<%=bean.getSpeed()%>"/>(米/分钟)</td>
	  	</tr>
	  	<tr>
	  		<td>最高速度:</td>
	  		<td><input type="text" name="maxspeed" value="<%=bean.getMaxSpeed()%>"/>(米)</td>
	  	</tr>
	  	<tr>
	  		<td>复位功能:</td>
	  		<td>
	  			<%if(bean.getId() == 1){
	  				%>无<%
	  			}else{
	  				%>
			  			<select name="anglereset">
			  				<%
			  					for(int i=0;i<reset.length;i++){
			  						%><option value="<%=i%>" <%if(i==bean.getSpeAngleReset()){%>selected="selected" <%}%>><%=reset[i]%></option><%
			  					}
			  				 %>
			  			</select>
	  				<%
	  			}%>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>价格:</td>
	  		<td>
	  			<%if(bean.getId() == 1){
	  				%>无<%
	  			}else{
	  				%>
		  			<input type="text" name="rent" value="<%=numFormat.format(bean.getRent())%>" size="11"/>
		  			<select name="renttype">
		  				<%
		  					for(int i=0;i<rent.length;i++){
		  						%><option value="<%=i%>" <%if(i==bean.getRentType()){%>selected="selected" <%}%>><%=rent[i]%></option><%
		  					}
		  				 %>
		  			</select>
	  				<%
	  			}%>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>使用次数:</td>
	  		<td>
	  			<%if(bean.getId() == 1){
	  				%>不限次<%
	  			}else{
	  				%>
	  				<input type="text" name="useTime" value="<%=bean.getUseTime()%>"/>(次)
	  				<%
	  			}%>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>积分限制:</td>
	  		<td>
	  			<%if(bean.getId() == 1){
	  				%>无限制<%
	  			}else{
	  				%>
	  				<input type="text" name="point" value="<%=bean.getPoint()%>"/>
	  				<%
	  			}%>
	  			</td>
	  	</tr>
	  	<tr>
	  		<td>描述:</td>
	  		<td>
	  			<%if(bean.getId() == 1){
	  				%>无<%
	  			}else{
	  				%>
	  				<textarea name="bak"><%=bean.getBak()%></textarea>
	  				<%
	  			}%>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td><input type="submit" value="修改龙舟"/></td>
	  		<td><a href="shop.jsp">取消</a></td>
	  	</tr>
  	</table>
  	<%
  	} else {
  	%>
  	<table border="1">
	  	<tr>
	  		<td>龙舟名称:</td>
	  		<td><input type="text" name="name"/></td>
	  	</tr>
	  	<tr>
	  		<td>龙舟类型:</td>
	  		<td>
	  			<select name="type">
	  				<%
	  					for(int i=0;i<type.length;i++){
	  						%><option value="<%=i+1%>"><%=type[i]%></option><%
	  					}
	  				 %>
	  			</select>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>加速效果:</td>
	  		<td><input type="text" name="speed"/>(米/分钟)</td>
	  	</tr>
	  	<tr>
	  		<td>最高速度:</td>
	  		<td><input type="text" name="maxspeed"/>(米)</td>
	  	</tr>
	  	<tr>
	  		<td>复位功能:</td>
	  		<td>
	  			<select name="anglereset">
	  				<%
	  					for(int i=0;i<reset.length;i++){
	  						%><option value="<%=i%>"><%=reset[i]%></option><%
	  					}
	  				 %>
	  			</select>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>价格:</td>
	  		<td>
	  			<input type="text" name="rent" size="11"/>
	  			<select name="renttype">
	  				<%
	  					for(int i=0;i<rent.length;i++){
	  						%><option value="<%=i%>"><%=rent[i]%></option><%
	  					}
	  				 %>
	  			</select>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>使用次数:</td>
	  		<td><input type="text" name="useTime"/></td>
	  	</tr>
	  	<tr>
	  		<td>积分限制:</td>
	  		<td><input type="text" name="point"/></td>
	  	</tr>
	  	<tr>
	  		<td>描述:</td>
	  		<td><textarea name="bak"></textarea></td>
	  	</tr>
	  	<tr>
	  		<td><input type="submit" value="添加龙舟"/></td>
	  		<td><a href="shop.jsp">取消</a></td>
	  	</tr>
  	</table>
  	<%
  	}
  	%>
  	</form>
	<a href="boat.jsp">返回龙舟管理</a><br>
	<a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>