<%
net.joycool.wap.bean.UserBean checkLoginUser = (net.joycool.wap.bean.UserBean) session.getAttribute(net.joycool.wap.util.Constants.LOGIN_USER_KEY);
if(net.joycool.wap.spec.AntiAction.checkUser(checkLoginUser.getId(),request)) {
request.getRequestDispatcher("/farm/aAU.jsp").forward(request, response);
return;
}
%>