<%@ page contentType="image/jpeg;charset=UTF-8" import="jc.util.*,java.util.*" %><%@include file="header.jsp"%>
<%response.setHeader("Cache-Control","no-cache");
Tiny2Game2 tg = (Tiny2Game2)game;
response.getOutputStream().write(tg.question.image);
//javax.imageio.ImageIO.write(tg.image, "JPEG", response.getOutputStream());
%>
