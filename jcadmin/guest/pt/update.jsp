<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="net.joycool.wap.util.RandomUtil,jc.guest.*"%><%@ page import="java.util.*,jc.guest.pt.*" %><%response.setHeader("Cache-Control","no-cache"); %>
<%JigsawAction action=new JigsawAction(request,response);
JigsawBean bean = null;
int id = action.getParameterInt("id");
if (id <= 0){
	response.sendRedirect("index.jsp");
	return;
} else {
	bean = JigsawAction.service.selectOneJigsaw2(id);
	if (bean == null){
		response.sendRedirect("index.jsp");
		return;
	}
}
String tip = "";
int update = action.getParameterInt("u");
if (update == 1){
	boolean result = action.updateJigsawBean();
	if (!result){
		tip = (String)request.getAttribute("tip");
	} else {%>
	<script type="text/javascript">
			alert('修改成功~');
	</script>
		<%response.sendRedirect("index.jsp");
		return;
	}
}
%>
<html>
	<head>
		<title>修改图片信息</title>
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
	<font color="red"><%=tip %></font><br/>
	<form action="update.jsp?u=1&id=<%=bean.getId()%>" method="post">
		<table width="717" height="175" border="0" cellpadding="0" cellspacing="0">
		  <tr bgcolor=#C6EAF5>
		    <th colspan="2">修改拼图</th>
		  </tr>
		  <tr>
		    <td  align="center">图片等级：</td>
		    <td>
		      <label>
		        <select name="ps" id="ps" onchange="getLevelChange()">
		        	<option value="1" <%if(bean.getPicGameLevel() == 1){%>selected<%}%>>普通</option>
		        	<option value="2" <%if(bean.getPicGameLevel() == 2){%>selected<%}%>>中等</option>
		        	<option value="3" <%if(bean.getPicGameLevel() == 3){%>selected<%}%>>困难</option>
		        </select>
		      </label>
		    </td>
		  </tr>
		  <tr>
		    <td  align="center">图片行数：</td>
		    <td>
		      <label>
		        <input type="text" name="pr" id="pr" maxsize="6" format="*N" size="6" value="<%=bean.getPicRows() %>" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
		        </label>
		    </td>
		  </tr>
		  <tr>
		    <td  align="center">图片列数：</td>
		    <td>
		      <label>
		        <input type="text" name="pc" id="pc" maxsize="6" format="*N" size="6"  value="<%=bean.getPicCols() %>" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
		        </label>
		    </td>
		  </tr>
		  <tr>
		    <td  align="center">图片名称：</td>
		    <td>
		      <label>
		        <input type="text" name="pn" maxsize="10" size="10" value="<%=bean.getPicName()%>"/>
		        </label>
		    </td>
		  </tr>
		  <tr>
		      <td  align="center">图片编号：</td>
		      <td>
		        <label>
		          <input type="text" name="picn" maxsize="10" size="10" value="<%=bean.getPicNum()%>" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
		          </label>
		      </td>
		  </tr>
		  <tr>
		    <td  align="center">图片状态：</td>
		    <td>
		      <label>
		        <select name="pics"">
		        	<option value="0" <%if(bean.getPicState() == 0){%>selected<%}%>>隐藏</option>
		        	<option value="1" <%if(bean.getPicState() == 1){%>selected<%}%>>显示</option>
		        </select>
		      </label>
		    </td>
		  </tr>
		  <tr>
		    <td colspan="2" align="center">
		      <label>
		        <input type="submit" name="Submit" value="修改"/>
		        </label>
		    </td>
		  </tr>
	</table>
	</form>
	<a href="index.jsp">返回</a><br/>
	</body>
</html>