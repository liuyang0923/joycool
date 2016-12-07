<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="java.util.Vector"%><%

IUserService userService = ServiceFactory.createUserService();
			//Vector cityNameList = userService.getCityFromUserInfor();
			Vector cityNameList = new Vector();
			cityNameList.add("北京");
			%>

<html>
<head>
<title>添加通知</title>
		
<script language="javascript">
	function check(){
		if (document.noticForm.title.value.length<1){
			alert("请填写通知标题！");
			document.noticForm.title.focus();
		  	return false;
		}
		if (document.noticForm.content.value.length<1 && document.noticForm.link.value.length<1){
			alert("通知内容和链接页面不能同时为空！");
		  	return false;
		}
	}
</script>
	
<head>
<body>
<center>
<table width="80%">
<tr>
<td height="30">
<b>添加通知：</b>
<hr>
</td>
</tr>
</table>

<table width="80%">
<form name="noticForm" action="addNoticeDeal.jsp" method="post" onsubmit="return check();">
<tr>
<td>请先择要发送的用户类型：</td>
</tr>
<tr>
<td height="10"></td>
</tr>
<tr>
<td>
<input type="radio" name="userType" value="01" checked>
<select name="area">
		<%
			String cityName = "";
			for (int i = 0; i < cityNameList.size(); i++) {
				cityName = (String) cityNameList.get(i);
				out.print("<option value=" + cityName + ">");
				out.print(cityName);
				out.print("</option>");

			}
		%>
</select>
地区的用户
</td>
</tr>

<tr>
<td>
<input type="radio" name="userType" value="02">
所有
<select name="gender">
<option value="1">男</option>
<option value="0">女</option>
</select>
同志
</td>
</tr>

<tr>
<td>
<input type="radio" name="userType" value="03">
所有在线用户
</td>
</tr>

<tr>
<td>
<input type="radio" name="userType" value="04">
属于
<select name="roomId">
<option value="01">聊天大厅</option>
<option value="02">小黑屋</option>
<option value="03">同城聊</option>
</select>
聊天室的用户
</td>
</tr>

<tr>
<td>
<input type="radio" name="userType" value="05">
所有聊天室的创建者
</td>
</tr>

<tr>
<td>
<input type="radio" name="userType" value="06">
目前在小黑屋里的用户
</td>
</tr>
<tr>
<td>
<input type="radio" name="userType" value="07">
<input style="width:35" type="text" name="days" value="10" maxlength="4" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
>
<%--
<select name="days">
<option value="1">一</option>
<option value="10">十</option>
<option value="20">二十</option>
<option value="30">三十</option>
<option value="180">半年</option>
<option value="360">一年</option>
</select>
--%>
天没来过，目前在线的用户&nbsp;&nbsp;&nbsp;
</td>
</tr>

<tr>
<td>
<input type="radio" name="userType" value="08">
根据等级
<select name="rank1">
<%for(int i=0;i<51;i++){%>
<option value="<%=i%>"><%=i%></option>
<%}%>
</select>

到
<select name="rank2">
<%for(int i=0;i<51;i++){%>
<option value="<%=i%>"><%=i%></option>
<%}%>
</select>

之间
</td>
</tr>

<tr>
<td height="30"></td>
</tr>
<tr>
<td>
请填写通知信息：
</td>
</tr>
<tr>
<td height="10"></td>
</tr>

<tr>
<td>
通知类型
<select name="type">
<option value="3">
普通通知
</option>
</select>
</td>
</tr>
<tr>
<td>
标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题
<input type="text" name="title">
&nbsp;&nbsp;<font color="red">*</font>
</td>
</tr>
<tr>
<td>
内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;容
<input type="text" name="content">
</td>
</tr>
<tr>
<td>
链接页面
<input type="text" name="link">
</td>
</tr>
<tr>
<td>
隐藏链接页面
<input type="text" name="hideLink">
</td>
</tr>
<tr>
<td height="30"></td>
</tr>
<tr>
<td>
<input type="submit" name="submit" value="发送">
</td>
</tr>
</form>
</table>
<center>
</body>
</html>
