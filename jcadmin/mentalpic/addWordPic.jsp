<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.mentalpic.*,com.jspsmart.upload.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%  SmartUpload smUpload = new SmartUpload();
    MentalPicAction action = new MentalPicAction(request);
	String tip = "";
	int count = action.getParameterInt("count");
	int up = action.getParameterInt("up");
	if (up == 1){
		// 上传文件
		boolean result = false;
		try{
			smUpload.initialize(pageContext);
			smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
			smUpload.setMaxFileSize(50*1024);
			smUpload.upload();	
			count = StringUtil.toInt(smUpload.getRequest().getParameter("count"));
			result = action.updateWordPic(smUpload);
			if (!result){
				tip = (String)request.getAttribute("tip");
			}	
		} catch (Exception e){
			tip = "上传失败.<br/>类型错误或文件单文件超过10KB";
		}
	}
%>
<html>
	<head>
		<title>看字选图题库</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
==看字选图题库==<br/>
<%if ("".equals(tip)){
	if (count <= 0){
%><form action="" method="post">
		请输入选项的个数:<input type="text" name="count" /><input type="submit" value="确定"/><input type="button" value="返回" onClick="document.location='index.jsp'"/>
  </form><%
	} else {
%><form action="addWordPic.jsp?up=1" method="post" enctype="multipart/form-data" >
<input type="hidden" name="count" value="<%=count%>">
标题:<input type="text" name="title" /><br/>
内容:<br/><textarea name="content" cols="60" rows="6"></textarea><br/>
<%for (int i = 0 ; i < count ; i++){
%>选项<%=i+1%>:<input type="file" name="image<%=i+1%>"><br/>
  答案<%=i+1%>:<br/><textarea name="analyse<%=i+1%>" cols="60" rows="6"></textarea><br/><%
}%><input type="submit" value="添加" /><input type="button" value="取消并返回" onClick="document.location='addWordPic.jsp'"/><br/>
</form>
<%
	}
} else {
	%><font color="red"><%=tip%></font><br/><a href="addWordPic.jsp">返回</a><br/><%
}
%>
	</body>
</html>