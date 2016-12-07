<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.util.encoder.Base64x,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   GuestUserInfo guestUser = action.getGuestUser();
   String code = StringUtil.removeCtrlAsc(action.getParameterString("c"));
   if (code == null || code.length()!=16){
	   // 没有书签
	   if (guestUser != null){
		   String password = Base64x.encodeMd5(guestUser.getPassword());
		   String id = Base64x.encodeInt(guestUser.getId());
		   code = password.substring(0, 5) + id + password.substring(5, 11);
		   
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="card1" title="乐酷游戏社区" ontimer="<%=response.encodeURL("bm.jsp?c=" + java.net.URLEncoder.encode(code))%>">
<timer value="1"/>
<p align="left">
页面跳转中...
</p>
</card>
</wml><%		   
		   return;
	   } else {
		   if (guestUser == null){
			   response.sendRedirect("login.jsp");
			   return;
		   }
	   }
   } else {
	   // 书签登陆
/*	    int oldUid = 0;
	    if (guestUser != null){
			oldUid = guestUser.getId();
	    }*/
	    if(guestUser==null){
		   	String duid = code.substring(5,10);
			int uid = Base64x.decodeInt(duid);
			guestUser = GuestHallAction.getGuestUser(uid);
				if (guestUser != null){
					String pwdcheck = code.substring(0,5) + code.substring(10,16);
					String pwdcheck2 = Base64x.encodeMd5(guestUser.getPassword());
	//				if (oldUid != guestUser.getId()){
						if(pwdcheck2.substring(0,11).equals(pwdcheck)){	// 密码校验成功
							// 把用户bean放入session中
							session.setAttribute(GuestHallAction.GUEST_KEY, guestUser);
							// 再放入cookie中
							action.saveToCookie(guestUser);
							response.sendRedirect("back.jsp?i=18&uid=" + guestUser.getId());
							return;
						} else {
							response.sendRedirect("login.jsp");
							return;
						}
	//			   }
			  } else {
				   response.sendRedirect("login.jsp");
				   return;
			  }
		}
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐酷游乐园"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>尊敬的用户,您把"本页"加为手机书签后,以后从这个书签就可以自动登录进入游乐园,不需要输入用户昵称和密码啦!(注意,每次修改密码之后必须重新保存本页面!)<br/>
这个页面书签代表的用户是昵称:<%=guestUser.getUserNameWml()%>的帐号哦.<br/>
如果您还不知道怎么把本页加为书签,请看<a href="bm2.jsp">帮助</a>!<br/>
<a href="index.jsp">返回游乐园</a><br/>
<%
} else {
%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>