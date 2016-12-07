<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.mentalpic.*,com.jspsmart.upload.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%  SmartUpload smUpload = new SmartUpload();
    MentalPicAction action = new MentalPicAction(request);
    MentalPicQuestion question = null;
    MentalPicOption option = null;
	String tip = "";
	int id = action.getParameterInt("id");
	int del = action.getParameterInt("del");
	int up = action.getParameterInt("up");
	List optionList = null;
	if (id > 0){
		question = MentalPicAction.service.getQuestion(" id=" + id + " and del=0");
		if (question == null){
			tip = "要修改的题目不存在，或已被。";
		} else {
			optionList = MentalPicAction.service.getOptionList(" que_id=" + question.getId() + " and del=0");
			if (up == 1){
				// 上传文件
				boolean result = false;
				try{
					smUpload.initialize(pageContext);
					smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
					smUpload.setMaxFileSize(50*1024);
					smUpload.upload();	
					result = action.modifyWordPic(smUpload,question,optionList);
					if (!result){
						tip = (String)request.getAttribute("tip");
					} else {
						action.sendRedirect("wpModify.jsp",response);
						return;
					}	
				} catch (Exception e){
					tip = "上传失败.<br/>类型错误或文件单文件超过10KB";
				}
			}
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
%><form action="wpModify2.jsp?up=1&id=<%=question.getId() %>" method="post" enctype="multipart/form-data" >
标题:<input type="text" name="title" value="<%=StringUtil.toWml(question.getTitle())%>"/><br/>
内容:<br/><textarea name="content" cols="60" rows="6"><%=question.getContent()%></textarea><br/>
<%for (int i = 0;i<optionList.size();i++){
	option = (MentalPicOption)optionList.get(i);%>
<img src="<%=action.getPicUrlPath() + StringUtil.toWml(option.getOption())%>" alt="Loading..." ><br/>
选项<%=i+1%>:<input type="file" name="image<%=i+1%>"><br/>
答案<%=i+1%>:<br/><textarea name="analyse<%=i+1%>" cols="60" rows="6"><%=option.getAnalyse()%></textarea><br/><%
}%><input type="submit" value="修改" /><input type="button" value="取消并返回" onClick="document.location='wpModify.jsp'"/><br/>
</form>
<%
} else {
	%><font color="red"><%=tip%></font><br/><a href="wpModify.jsp">返回</a><br/><%
}%>
	</body>
</html>