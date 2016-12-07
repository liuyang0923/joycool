<%@ page language="java" import="net.joycool.wap.util.*,jc.family.game.boat.*" pageEncoding="utf-8"%>
<%
	BoatAction action = new BoatAction(request,response);
	String[] distance = {"无","前进","后退"}; 
	String[] speed = {"无","提高","降低"}; 
	String[] angle = {"无","有"}; 
	int id = action.getParameterInt("id");
	AccidentBean bean = null;
	if(id > 0){
		bean = BoatAction.service.getAccidentBean(" id="+id);
	}
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>添加事件</title>
    <script type="text/javascript">
    	function onDis(obj){
    		if(obj.value > 0){
				document.getElementById("dis").innerHTML = "<input type='text' name='distance1' size='4'/>-<input type='text' name='distance2' size='4'/>m";
	   		}else{
				document.getElementById("dis").innerHTML = "";
    		}
    	}
    	function onSpe(obj){
    		if(obj.value > 0){
				document.getElementById("spe").innerHTML = "<input type='text' name='speed1' size='4'/>-<input type='text' name='speed2' size='4'/>%";
	   		}else{
				document.getElementById("spe").innerHTML = "";
    		}
    	}
    	function onAng(obj){
    		if(obj.value > 0){
				document.getElementById("ang").innerHTML = "<input type='text' name='angle1' size='4'/>-<input type='text' name='angle2' size='4'/>°";
	   		}else{
				document.getElementById("ang").innerHTML = "";
    		}
    	}
		function showPictures(obj) {
			if(obj.name=='bigimg'){
			  document.getElementById("preview").innerHTML = "<img src='" + obj.value + "'>";
			}
		}
    </script>
    
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <p style="color:red;"><%=1==action.getParameterInt("a")?"名称不能为空!":""%></p>
  	<form action="accident.jsp?a=1" method="post" enctype="multipart/form-data">
  	<%
  	if(bean != null){
  	%>
  	<input type="hidden" name="id" value="<%=bean.getId()%>"/>
  	<table border="1">
	  	<tr>
			<td>名称</td>
	  		<td><input type="text" name="name" value="<%=StringUtil.toWml(bean.getName())%>"/></td>
	  	</tr>
	  	<tr>
			<td>位置变化</td>
	  		<td>
				<select name="distanceType" onchange="onDis(this)">
					<%
						for(int i=0;i<distance.length;i++){
							%>
								<option value="<%=i%>" <%if(bean.getDistanceType() == i){%>selected="selected"<%}%>><%=distance[i]%></option>
							<%
						}
					 %>
				</select>
				<span id="dis"><%if(bean.getDistanceType() > 0){%><input type="text" name="distance1" size="4" value="<%=bean.getDistance1()%>"/>-<input type="text" name="distance2" size="4" value="<%=bean.getDistance2()%>"/>m<%}%></span>
	  		</td>
	  	</tr>
	  	<tr>
			<td>速度变化</td>
	  		<td>
				<select name="speedType" onchange="onSpe(this)">
					<%
						for(int i=0;i<speed.length;i++){
							%>
								<option value="<%=i%>" <%if(bean.getSpeedType() == i){%>selected="selected"<%}%>><%=speed[i]%></option>
							<%
						}
					 %>
				</select>
				<span id="spe"><%if(bean.getSpeedType() > 0){%><input type="text" name="speed1" size="4" value="<%=bean.getSpeed1()%>"/>-<input type="text" name="speed2" size="4" value="<%=bean.getSpeed2()%>"/>%<%}%></span>
	  		</td>
	  	</tr>
	  	<tr>
			<td>角度变化</td>
	  		<td>
				<select name="angleType" onchange="onAng(this)">
					<%
						for(int i=0;i<angle.length;i++){
							%>
								<option value="<%=i%>" <%if(bean.getAngleType() == i){%>selected="selected"<%}%>><%=angle[i]%></option>
							<%
						}
					 %>
				</select>  	
				<span id="ang"><%if(bean.getAngleType() > 0){%><input type="text" name="angle1" size="4" value="<%=bean.getAngle1()%>"/>-<input type="text" name="angle2" size="4" value="<%=bean.getAngle2()%>"/>°<%}%></span>
	  		</td>
	  	</tr>
	  	<tr>
			<td>出现概率</td>
	  		<td>
	  			<input type="text" name="percent" value="<%=bean.getPercent()%>"/>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>描述:</td>
	  		<td><textarea name="bak"><%=bean.getBak()%></textarea></td>
	  	</tr>
	  	<tr>
	  		<td>上传图片:</td>
	  		<td><span id="preview"><img src="/rep/family/boat/<%=bean.getBigImg()%>" alt="dd"/></span><input type="file" name="bigimg" onchange="showPictures(this)"/></td>
	  	</tr>
	  	<tr>
	  		<td><input type="submit" value="修改事件"/></td>
	  		<td><a href="accident.jsp">取消</a></td>
	  	</tr>
  	</table>
  	<%
  	} else {
  	%>
  	<table border="1">
	  	<tr>
			<td>名称</td>
	  		<td><input type="text" name="name"/></td>
	  	</tr>
	  	<tr>
			<td>位置变化</td>
	  		<td>
				<select name="distanceType" onchange="onDis(this)">
					<%
						for(int i=0;i<distance.length;i++){
							%>
								<option value="<%=i%>"><%=distance[i]%></option>
							<%
						}
					 %>
				</select>
				<span id="dis"></span>
	  		</td>
	  	</tr>
	  	<tr>
			<td>速度变化</td>
	  		<td>
				<select name="speedType" onchange="onSpe(this)">
					<%
						for(int i=0;i<speed.length;i++){
							%>
								<option value="<%=i%>"><%=speed[i]%></option>
							<%
						}
					 %>
				</select>
				<span id="spe"></span>
	  		</td>
	  	</tr>
	  	<tr>
			<td>角度变化</td>
	  		<td>
				<select name="angleType" onchange="onAng(this)">
					<%
						for(int i=0;i<angle.length;i++){
							%>
								<option value="<%=i%>"><%=angle[i]%></option>
							<%
						}
					 %>
				</select>  	
				<span id="ang"></span>
	  		</td>
	  	</tr>
	  	<tr>
			<td>出现概率</td>
	  		<td>
	  			<input type="text" name="percent"/>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>描述:</td>
	  		<td><textarea name="bak"></textarea></td>
	  	</tr>
	  	<tr>
	  		<td>上传图片:</td>
	  		<td><span id="preview"></span><input type="file" name="bigimg" onchange="showPictures(this)"/></td>
	  	</tr>
	  	<tr>
	  		<td><input type="submit" value="添加事件"/></td>
	  		<td><a href="accident.jsp">取消</a></td>
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