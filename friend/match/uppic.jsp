<%@ page contentType="text/html;charset=utf-8"%><%@ page import="jc.match.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*,com.jspsmart.upload.SmartUpload;"%><% response.setHeader("Cache-Control","no-cache");
SmartUpload smUpload = new SmartUpload();
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
// 没登陆,返回登陆页面
response.sendRedirect("/user/login.jsp");
return;
}
String tip = "";
String error = "";
MatchInfo matchInfo = MatchAction.getCurrentMatch();
if (matchInfo != null){
	if (matchInfo.getFalg() == 2){
		error = "本次比赛已结束,请等待下次比赛.";
	}
} else {
	error = "比赛尚未开始,敬请期待.";
}
boolean result = false;
MatchUser matchUser = MatchAction.getMatchUser(loginUser.getId());
if (matchUser != null && matchUser.getChecked() == 3){
	error = "您已经被取消参赛资格.";
} else {
	int upload = action.getParameterInt("u");
	if (upload == 1){
		try{
			smUpload.initialize(pageContext);
			smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
			smUpload.setMaxFileSize(50*1024);
			smUpload.upload();	
			result = action.upPic(smUpload);
			if (!result){
				tip = (String)request.getAttribute("tip");
			}
		} catch (Exception e){
			tip = "文件类型不正确,或体积太大.";
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>上传参赛照片</title>
</head>
<body><p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(error)){
if (!result){
%>图片:200*300以内<br/>
<form action="<%=response.encodeURL("uppic.jsp?u=1")%>" method="post" enctype="multipart/form-data" >
<%if(!"".equals(tip)){%><font color="red"><%=tip%></font><br/><%}%>
<input type="file" name="image"><br/>
<input type="submit" name="submit" value="上传"/><br/>
注:支持WAP2.0的手机才可以上传记图片.<br/>
</form>
<%	
} else {
%>上传成功.<br/><%	
}%>
<a href="join.jsp?t=1">返回申请参赛页</a><br/>
<%	
} else {
	%><%=error %><br/><%
}
%>
<a href="index.jsp">返回选美首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</body>
</html>