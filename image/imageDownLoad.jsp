<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.image.ImageFileBean"%><%@ page import="net.joycool.wap.bean.image.ImageBean"%><%
    ImageBean image = (ImageBean)request.getAttribute("image");
    ImageFileBean image128128 = image.getFile128128();
    String address128 = image128128.getRealFileUrl();
	response.sendRedirect((address128));
%>