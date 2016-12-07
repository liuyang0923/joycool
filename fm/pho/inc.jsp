<%@ page pageEncoding="utf-8"%><%
if (action.hasParam("fid")) {session.setAttribute("fid",new Integer(action.getParameterInt("fid")));}
Integer sifid = (Integer)session.getAttribute("fid");
if (sifid == null) {response.sendRedirect("/fm/index.jsp");return;}
int fid = sifid.intValue();
int uid = 0;
UserBean loginUser = action.getLoginUser();
if (loginUser != null) {uid = loginUser.getId();}
%>