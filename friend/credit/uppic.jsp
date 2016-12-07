<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*,com.jspsmart.upload.SmartUpload;"%><% response.setHeader("Cache-Control","no-cache");
   SmartUpload smUpload = new SmartUpload();
   CreditAction action = new CreditAction(request);
   if (action.getLoginUser() == null){
	   // 没登陆,返回登陆页面
	   out.clearBuffer();
	   response.sendRedirect("../user/login.jsp");
	   return;
   }
   int upload = action.getParameterInt("u");
   boolean result = false;
   String tip = "";
   // 上传
   if (upload == 1){
		try{
			smUpload.initialize(pageContext);
			smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
			smUpload.setMaxFileSize(50*1024);
			smUpload.upload();	
			result = action.updatePic(smUpload);
			if (!result){
				tip = (String)request.getAttribute("tip");
			}
		} catch (Exception e){
			tip = "文件类型不正确,或体积太大.";
		}
   }
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>交友可信度</title>
</head>
<body><p align="left">
<%=BaseAction.getTop(request, response)%>
上传本人生活照<br/>
---------------------<br/>
<%if (upload == 1 && result == true){
%>上传成功，等待审核……<br/><a href="navi.jsp">返回基本资料</a><br/><a href="navi.jsp">返回可信度首页</a><br/><%	
} else {
%><form action="<%=response.encodeURL("uppic.jsp?u=1")%>" method="post" enctype="multipart/form-data" >
<%if(!"".equals(tip)){%><font color="red"><%=tip%></font><br/><%}%>
<input type="file" name="image"/><br/>
<input type="submit" name="submit" value="上传"/><br/>
注：支持WAP2.0的手机才可以上传图片.<br/>
<a href="navi.jsp">返回</a><br/>
<%}%>
---------------------<br/>
</form><%=BaseAction.getBottomShort(request, response)%></p>
</body>
</html>