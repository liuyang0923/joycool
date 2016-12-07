<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="net.joycool.wap.util.RandomUtil,jc.guest.*"%><%@ page import="java.util.*,jc.guest.pt.*" %><%response.setHeader("Cache-Control","no-cache"); %>
<%JigsawAction action=new JigsawAction(request,response);
int add = action.getParameterInt("a");
String tip = "";
if (add == 1){
	boolean result = action.addJigsawBean();
	if (!result){
		tip = (String)request.getAttribute("tip");
	} else {%>
			<script type="text/javascript">
			alert('添加成功~');
			</script>
		<%response.sendRedirect("index.jsp");
		return;
	}
}
%>
<html>
	<head>
		<title>增加图片信息</title>
		<script type="text/javascript">
			function getLevelChange(){
				var level = document.getElementById('ps').value;
				if(level==1){
					document.getElementById('pr').value='3';
					document.getElementById('pc').value='3';
				}else if(level==2){
					document.getElementById('pr').value='4';
					document.getElementById('pc').value='4';
				}else if(level==3){
					document.getElementById('pr').value='5';
					document.getElementById('pc').value='5';
				}
			}
		</script>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
	<font color="red"><%=tip%></font><br/>
	<form action="add.jsp?a=1" method="post">
		<table width="717" height="175" border="0" cellpadding="0" cellspacing="0">
		  <tr bgcolor=#C6EAF5>
		    <th colspan="2">添加拼图</th>
		  </tr>
		  <tr>
		    <td  align="center">图片等级：</td>
		    <td>
		      <label>
		        <select name="ps" id="ps" onchange="getLevelChange()">
		        	<option value="1">普通</option>
		        	<option value="2">中等</option>
		        	<option value="3">困难</option>
		        </select>
		      </label>
		    </td>
		  </tr>
		  <tr>
		    <td  align="center">图片行数：</td>
		    <td>
		      <label>
		        <input type="text" name="pr" id="pr" maxsize="6" format="*N" size="6" value="3" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
		        </label>
		    </td>
		  </tr>
		  <tr>
		    <td  align="center">图片列数：</td>
		    <td>
		      <label>
		        <input type="text" name="pc" id="pc" maxsize="6" format="*N" size="6"  value="3" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
		        </label>
		    </td>
		  </tr>
		  <tr>
		    <td  align="center">图片名称：</td>
		    <td>
		      <label>
		        <input type="text" name="pn" maxsize="10" size="10"/>
		        </label>
		    </td>
		  </tr>
		  <tr>
		      <td  align="center">图片编号：</td>
		      <td>
		        <label>
		          <input type="text" name="picn" maxsize="10" size="10" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
		          </label>
		      </td>
		  </tr>
		  <tr>
		    <td colspan="2" align="center">
		      <label>
		        <input type="submit" name="Submit" value="添加"/>
		        </label>
		    </td>
		  </tr>
	</table>
	<a href="index.jsp">返回</a><br/>
	</form>
	</body>
</html>