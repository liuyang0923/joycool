<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%ExamAction action = new ExamAction(request);
  int bagId = action.getParameterInt("bid");
  int back = action.getParameterInt("back");
  ExamBag bag = action.ExamAction.getBag(" id=" + bagId);
  String tip = "";
  boolean result = false;
  if (bag == null){
	  tip = "要查找的书包不存在.";
  } else {
	  int submit = action.getParameterInt("s");
	  int del = action.getParameterInt("d");
	  if (submit == 1){
		  // 修改
		  bag.setTitle(action.getParameterNoEnter("title").trim());
		  bag.setContent(action.getParameterString("content"));
		  bag.setQueType(action.getParameterInt("type"));
		  result = action.modifyBag(bag);
	  } else if (del == 1){
		  // 删除
		  bag.setDel(1);
		  result = action.modifyBag(bag);
	  } else if (del == 2){
		  // 恢复
		  bag.setDel(0);
		  result = action.modifyBag(bag);
	  }
	  if (result){
		  if(back == 1){
			  action.sendRedirect("allUser.jsp",response);
		  } else {
			  action.sendRedirect("userPublic.jsp?s=1&type=" + bag.getQueType() + "&uid=" + bag.getUserId(),response);
		  }
		  return;
	  } else {
		  tip = (String)request.getAttribute("tip");
	  }
  }
%>
<html>
	<head>
		<title>备战考试->用户修改</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
	<font color="red"><%=tip%></font><br/>
	<%if (back == 1){
		%><a href="allUser.jsp"><-返回上一页</a><%
	} else {
		%><a href="userPublic.jsp?s=1&type=<%=bag.getQueType()%>&uid=<%=bag.getUserId()%>"><-返回上一页</a><%	
	}%>
	
	<form action="userModify.jsp?s=1&bid=<%=bag.getId() %>" method = "post">
			标题:<input type="text" name="title" size=36  value="<%=StringUtil.toWml(bag.getTitle())%>"/><br/>
			内容:<br/><textarea name="content" cols="80" rows="10"><%=bag.getContent()%></textarea><br/>
			科目:<select name="type">
				<option value="10">数学->公式大全</option>
				<option value="11">数学->基本定理</option>
				<option value="12">数学->经典例题</option>
				<option value="13">语文->背诵默写</option>
				<option value="14">语文->文学常识</option>
				<option value="15">语文->范文例句</option>
				<option value="16">英语->词汇表</option>
				<option value="17">英语->语法大全</option>
				<option value="18">英语->范文例句</option>
				<option value="19">物理->常量</option>
				<option value="20">物理->公式大全</option>
				<option value="21">物理->基本定理</option>
				<option value="22">物理->经典例题</option>
				<option value="23">化学->公式大全</option>
				<option value="24">化学->基本定理</option>
				<option value="25">化学->常量</option>
				<option value="26">化学->经典实验</option>
				<option value="6">生物</option>
				<option value="7">历史</option>
				<option value="8">政治</option>
				<option value="9">地理</option>
			</select><br/>
			<script language="javascript">				
				document.forms[0].type.value="<%=bag.getQueType()%>";
			</script>
			<input type=submit value="修改"/>
		</form>
<%if (bag.getDel()==0){
	%><a href="userModify.jsp?d=1&bid=<%=bag.getId()%>&back=<%=back%>" onclick="return confirm('确定删除？')">删除</a><br/><%	
} else {
	%><a href="userModify.jsp?d=2&bid=<%=bag.getId()%>&back=<%=back%>" onclick="return confirm('确定恢复？')">恢复</a><br/><%		
}%>
	</body>
</html>