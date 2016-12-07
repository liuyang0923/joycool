<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="java.util.List,net.joycool.wap.util.StringUtil,net.joycool.wap.framework.BaseAction,jc.family.FamilyHomeBean,jc.family.FamilyAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = null;
HttpSession session = request.getSession(false);
if(session!=null)
	loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
int fmid = StringUtil.toInt(request.getParameter("fm"));
FamilyHomeBean fmhome=FamilyAction.getFmByID(fmid);
if(fmhome==null){
response.sendRedirect("/fm/index.jsp");return;
}
	
if(loginUser == null){
	String server = request.getServerName();
	String code = request.getParameter("code");
	if(code == null){
        //liuyi 2007-01-25 登录问题修改 start
		//response.sendRedirect((BaseAction.INDEX_URL));
        BaseAction.sendRedirect("/user/login.jsp", response);
	    //liuyi 2007-01-25 登录问题修改 end
		return;
	}
	int qm = code.indexOf('?');
	if(qm >= 0)
		code = code.substring(0, qm);
	String [] ss = code.split("AAAA");
	if(ss == null || ss.length != 2){
        //liuyi 2007-01-25 登录问题修改 start
		//response.sendRedirect((BaseAction.INDEX_URL));
		BaseAction.sendRedirect("/user/login.jsp", response);
	    //liuyi 2007-01-25 登录问题修改 end
		return;
	}
	String idStr = ss[0];
	String password = ss[1];
	int id = StringUtil.toInt(Encoder.decrypt(idStr));
	if(id == -1){
        //liuyi 2007-01-25 登录问题修改 start
		//response.sendRedirect((BaseAction.INDEX_URL));
		BaseAction.sendRedirect("/user/login.jsp", response);
	    //liuyi 2007-01-25 登录问题修改 end
		return;
	}else if(SecurityUtil.checkForbidUserId(id)){
		BaseAction.sendRedirect("/user/login.jsp", response);
		return;
	}
	loginUser = UserInfoUtil.getUser(id);
	if(loginUser == null || !loginUser.getPassword().equals(password)){
        //liuyi 2007-01-25 登录问题修改 start
		//response.sendRedirect((BaseAction.INDEX_URL));
		BaseAction.sendRedirect("/user/login.jsp", response);
	    //liuyi 2007-01-25 登录问题修改 end
		return;
	}
	session = SecurityUtil.newSession(request);
	loginUser.setIpAddress(request.getRemoteAddr());
	loginUser.setUserAgent(request.getHeader("User-Agent"));
	JoycoolSessionListener.updateOnlineUser(request, loginUser);
session.setAttribute("cd-fourmSTime", Long.valueOf(System.currentTimeMillis() + 20000));	// 20秒内不能发帖或者回帖
	//liuyi 2006-12-28 登录统计修改 start
	LogUtil.logLogin("bookmarkf:" + loginUser.getId() + ":" + request.getRemoteAddr() + ":" + request.getHeader("User-Agent") + ":" + new CookieUtil(request).getCookieValue("jsid"));
    //liuyi 2006-12-28 登录统计修改 end

 	//liuyi 2007-01-25 登录问题修改 start
	//response.sendRedirect((BaseAction.INDEX_URL));
	if("2".equals(request.getParameter("f"))){
		response.sendRedirect("enter.jsp?f=2&code="+code);
	}else{
		request.getRequestDispatcher("/fm/enter.jsp?id="+fmid).forward(request,response);
	}
    //liuyi 2007-01-25 登录问题修改 end
	return;
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="card1" title="<%=fmhome.getFm_nameWml()%>家族">
<p align="left">
<%if("2".equals(request.getParameter("f"))){%>您的书签已过期,很快将无法使用,请把本页保存为新的书签<br/><%}else{
%>这是【<%=fmhome.getFm_nameWml()%>】家族的书签地址,请把此页加为书签,以后用这个书签就能自动登录并直接访问家族.<br/><%}%>
---------<br/>
注意：每次修改密码后，都需要重新保存本书签。<br/>
&lt;<a href="/fm/myfamily.jsp?id=<%=fmhome.getId()%>">返回家族</a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>