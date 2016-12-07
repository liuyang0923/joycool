<%@ page pageEncoding="utf-8"%>
<%   
	if(action.getGuestUser() != null) {
	   		response.sendRedirect("index.jsp");
 	   		return;
	}
//**********************如果是登陆用户的话,就用他绑定的游客信息登陆.如果没有,就给他新建一个**********************
 UserBean user = action.getLoginUser();
   if (user != null){
	   guestUser = GuestHallAction.service.getUserInfo(" buid=" + user.getId() + " order by id limit 1");
	   if (guestUser != null){
		   // 写入最后登陆时间
		   SqlUtil.executeUpdate("update guest_user_info set last_time='" + DateUtil.formatSqlDatetime(System.currentTimeMillis()) + "' where id=" + guestUser.getId() , 6);
		   guestUser.setLastTime(System.currentTimeMillis());
		   // 写入cookie
		   action.saveToCookie(guestUser);
		   // 写入session
		   session.setAttribute(GuestHallAction.GUEST_KEY,guestUser);
		   // 绑定的游客用户登陆成功
		   // 给今日奖励
//	   	   action.getAwardToday(guestUser);
		   response.sendRedirect("index.jsp");
		   return;
	   }
   }
GuestUserInfo autoUser = action.getGuestUserCk();	// 尝试从cookie中取
   if (autoUser != null){
	    // 查一下这个遊客账户是不是真的存在.
   		autoUser = GuestHallAction.getGuestUser(autoUser.getId());
	    if (autoUser != null){
	    	// 存在,直接登陆.
	       session.setAttribute(GuestHallAction.GUEST_KEY,autoUser);
	 	   SqlUtil.executeUpdate("update guest_user_info set last_time='" + DateUtil.formatSqlDatetime(System.currentTimeMillis()) + "' where id=" + autoUser.getId() , 6);
	 	   autoUser.setLastTime(System.currentTimeMillis());
	 	  net.joycool.wap.bean.UserBean userBean = action.getLoginUser();
	 	   if(userBean != null) {
	 		    if (autoUser.getBuid() == 0 && GuestHallAction.service.getUserInfo(" buid=" + userBean.getId() + " order by id limit 1") == null)
	 		    autoUser.setBuid(userBean.getId());
				SqlUtil.executeUpdate("update guest_user_info set buid=" + userBean.getId() + " where id=" + autoUser.getId(), 6);
	 		    response.sendRedirect("index.jsp");
	 	   		return;
	 	   }
	    }
   } else if(user!=null) {
		   // 没有，就给它建立一个
		   String userName = user.getNickName();
		   guestUser = GuestHallAction.service.getUserInfo(" user_name='" +StringUtil.toSql(userName) + "'");
		   if (guestUser != null){
			   // 该用户的昵称和某位游客的昵称重名了
			   userName = userName + user.getId();
		   }
		   int result = action.addUser(userName);	// 此方法在添加用户时已经将用户的bean放入session中，并且写了cookie了。不要再写一次。
		   // 这里的guestUser得从session里重新取一遍才是正确的
		   if (result == 0){
			   guestUser = action.getGuestUser();
			   // 给今日奖励
//		   	   action.getAwardToday(guestUser);
			   // 写入最后登陆时间
			   SqlUtil.executeUpdate("update guest_user_info set last_time='" + DateUtil.formatSqlDatetime(System.currentTimeMillis()) + "' where id=" + guestUser.getId() , 6);
			   guestUser.setLastTime(System.currentTimeMillis());
			   response.sendRedirect("index.jsp");
			   return;
		   } else {
			   response.sendRedirect("back.jsp?i=" + result);
			   return;
		   }
   }

if (autoUser != null){
	%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游乐园"><p>
<%=BaseAction.getTop(request, response)%>欢迎回来,<%=autoUser.getUserNameWml()%><br/>
<a href="index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml><%
return;}%>