<%@ page contentType="image/jpeg;charset=UTF-8" import="jc.util.*,java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*" %>
<%response.setHeader("Cache-Control","no-cache");
session.setAttribute("currentModuleUrl", "/user/login.jsp");
java.awt.image.BufferedImage image = VerifyUtil3.getVerifyJpgDef(request);
javax.imageio.ImageIO.write(image, "JPEG", response.getOutputStream());
%>
