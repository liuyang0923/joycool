<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.mentalpic.*,com.jspsmart.upload.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%  SmartUpload smUpload = new SmartUpload();
    MentalPicAction action = new MentalPicAction(request);
	MentalPicQuestion question = null;
	MentalPicOption option = null;
	List optionList = null;
    String tip = "";
	int id = action.getParameterInt("id");
	int del = action.getParameterInt("del");
	int up = action.getParameterInt("up");
	
	if (id > 0){
		question = MentalPicAction.service.getQuestion(" id=" + id + " and flag=1 and del=0");
		if (question == null){
			tip = "您所查找的问题不存在，或已被删除，或类型错误。";
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
					result = action.modifyPicWord(smUpload,question,optionList);
					if (!result){
						tip = (String)request.getAttribute("tip");
					} else {
						action.sendRedirect("pwModify.jsp",response);
						return;
					}
				} catch (Exception e){
					tip = "上传失败.<br/>类型错误或文件单文件超过10KB";
				}
			}
		}
	} else {
		tip = "参数错误.没有ID.";
	}
%>
<html>
	<head>
		<title>看图选字题库</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
==看图选字题库==<br/>	
<%if ("".equals(tip)){
%><form action="pwModify2.jsp?up=1&id=<%=question.getId()%>" method="post" enctype="multipart/form-data" >
标题:<input type="text" name="title" value="<%=StringUtil.toWml(question.getTitle())%>"/><br/>
内容:<br/><textarea name="content" cols="60" rows="6"><%=question.getContent()%></textarea><br/>
<img src="<%=action.getPicUrlPath() + question.getContentPic()%>" alt="Loading..." ><br/>
图片上传:<input type="file" name="image"><br/>
<%if(optionList != null && optionList.size() > 0){
for (int i = 0;i < optionList.size();i++){
option = (MentalPicOption)optionList.get(i);
%>选项<%=i+1%>:<input type="text" name="opt<%=i+1%>" value="<%=option.getOption()%>"><br/>
  解析<%=i+1%>:<br/><textarea name="analyse<%=i+1%>" cols="60" rows="6"><%=option.getAnalyse()%></textarea><br/><%
}
}%><input type="submit" value="修改" /><input type="button" value="取消并返回" onClick="document.location='pwModify.jsp'"/><br/>
</form>
<%
} else {
	%><font color="red"><%=tip%></font><br/><a href="pwModify.jsp">返回</a><br/><%
}%>
	</body>
</html>