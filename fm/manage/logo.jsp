<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List,com.jspsmart.upload.SmartUpload,jc.family.*"%><%
response.setHeader("Cache-Control","no-cache");
SmartUpload smUpload = new SmartUpload();
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0||!fmLoginUser.isflagPublic()){
response.sendRedirect("/fm/myfamily.jsp");return;
}
FamilyHomeBean fm=FamilyAction.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>上传家族徽记</title>
</head>
<body><%
if(!fm.isUploadLogo()){
	%>您的家族游戏经验值不够,还差<%=Constants.MIN_GAME_POINT_UPLOAD-fm.getGame_num()%>点您才能上传家族徽记!请继续努力吧!<br/>
	<a href="management.jsp">返回家族管理</a><br/><%
}else{
	String tip = "";
	boolean result=false;
	int upload=action.getParameterInt("u");
	if (upload == 1){
		try{
			smUpload.initialize(pageContext);
			smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
			smUpload.setMaxFileSize(50*1024);
			smUpload.upload();	
			result = action.upPic(smUpload,fm);
			if (!result){
				tip = (String)request.getAttribute("tip");
			}
		} catch (Exception e){
			tip = "文件类型不正确,或体积太大.";
		}
	}
	if(!result){
		%>上传图片大小必须为50K以下.请尽量上传长*宽为200*60以下的图片,否则我们压缩处理后和原图片会有差别!<br/><%
		if(!fm.getLogoUrl().equals("")){
			%>新的图片会覆盖之前的图片,请谨慎操作!<br/><%
		}
		if(!"".equals(tip)){
			%><font color="red"><%=tip%></font><br/><%
		}
		%><form action="<%=response.encodeURL("logo.jsp?u=1")%>" method="post" enctype="multipart/form-data" >
		<input type="file" name="image" /><br/>
		<input type="submit" name="submit" value="上传"/><br/>
		注:支持WAP2.0的手机才可以上传记图片.<br/>
		</form><%
	}else{
		%>上传成功,等待审核......<br/><%
	}
}
%><a href="/fm/myfamily.jsp">返回我的家族</a><br/>
</body>
</html>
