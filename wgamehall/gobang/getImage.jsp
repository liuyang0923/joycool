<%@ page contentType="image/gif" import="java.awt.*, java.awt.image.*,javax.imageio.*, net.joycool.wap.action.wgamehall.*" %><%
response.setHeader("Cache-Control","no-cache");
session.setAttribute("currentModuleUrl", "/wgamehall/gobang/playing.jsp?gameId="+request.getParameter("gameId"));
GoBangAction action = new GoBangAction(request);
BufferedImage image = action.getImage(request);
ImageIO.write(image, "GIF", response.getOutputStream()); 
%>