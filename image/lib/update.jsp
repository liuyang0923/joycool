<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.imglib.*,com.jspsmart.upload.SmartUpload"%><% response.setHeader("Cache-Control","no-cache");
SmartUpload smUpload = new SmartUpload();   
ImgLibAction action = new ImgLibAction(request,response);
   if (action.getLoginUser() == null){
	   // 没登陆,返回登陆页面
	   response.sendRedirect("/user/login.jsp");
	   return;
   }
   LibUser libUser = ImgLibAction.service.getLibUser(" user_id=" + action.getLoginUid());
   if (libUser==null){
	   response.sendRedirect("lib.jsp");
	   return;
   }
   // 上传
   if (action.getParameterInt("u") == 1){
		try{
			smUpload.initialize(pageContext);
			smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
			smUpload.setMaxFileSize(50*1024);
			smUpload.upload();	
//			count = StringUtil.toInt(smUpload.getRequest().getParameter("count"));
			action.upPic(smUpload);
			return;
		} catch (Exception e){
			request.setAttribute("tip", "文件上传失败!");
			action.sendError(3);
			return;
		}
   }
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>上传图片</title>
</head>
<body><form enctype="multipart/form-data" method="post" action="<%=response.encodeURL("update.jsp?u=1") %>">
<p align="left">
上传图片<br/>
您还可以上传<%=ImgLibAction.MAX_COUNT - libUser.getCount()%>张图片<br/>
标题:(限12字以内)<br/><input type="text" name="title" value="" maxlength="12" /><br/>
图片:(50K以内)<br/><input type="file" name="file"/><br/>
<input type="submit" name="submit" value="上传"/><br/>
*上传后该图片将处于待审查状态,通过后将自动放入您的图库.<br/>
<a href="lib.jsp">返回图库</a><br/>
</p>
</form>
</body>
</html>