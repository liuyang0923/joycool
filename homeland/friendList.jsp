<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.util.*, net.joycool.wap.bean.home.*" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.framework.*"%><%
	response.setHeader("Cache-Control","no-cache");
	net.joycool.wap.service.infc.IUserService userService = ServiceFactory.createUserService();
	//IHomeService homeService =ServiceFactory.createHomeService();
	// 取得参数
	CustomAction cusAction = new CustomAction(request, response);
	
		String option = request.getParameter("o");
		if (option == null) {
			return;
		}

		//判断用户id
		int uid = cusAction.getParameterInt("uid");
		UserBean loginUser = new UserBean();
		if(uid == 0) {
			loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
		} else if(uid > 0){
			loginUser = UserInfoUtil.getUser(uid);
		}
		
		//zhul 2006-10-13 优化好友 start
		//zhul 2006-10-13 获取用户好友
		ArrayList userFriends = UserInfoUtil.getUserFriends(uid);
		Vector userList=new Vector();
		if (option.equals("on")) {
			//在线好友
			for(int i=0;i<userFriends.size();i++)
			{
				String userIdKey=(String)userFriends.get(i);
				if(OnlineUtil.isOnline(userIdKey))
					userList.add(userIdKey);
			}
			
		} else if (option.equals("out")) {
			//离线好友
			for(int i=0;i<userFriends.size();i++)
			{
				String userIdKey=(String)userFriends.get(i);
				if(!OnlineUtil.isOnline(userIdKey))
					userList.add(userIdKey);
			}
			
		}
		PagingBean paging = new PagingBean(cusAction, userList.size(), 10, "p");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="我的好友">
	<p>
	<%=BaseAction.getTop(request, response)%>
	<!-- 
	<a href="<%=("/homeland/myPage.jsp") %>">家园</a>|<a href="<%=("/homeland/h.jsp") %>">管理</a>|<a href="<%=("/lswjs/index.jsp") %>">导航</a><br/>
	 -->
	<a href="<%=("/homeland/myPage.jsp") %>">家园</a><br/>
<%if(uid == ((UserBean) session.getAttribute(Constants.LOGIN_USER_KEY)).getId()) {%>
	<%=option.equals("on")? "我的在线好友":""%><%=option.equals("out")?"我的离线好友":""%><br/>
<%} else { %>
	<%=option.equals("on")? loginUser.getNickName() + "的在线好友":""%><%=option.equals("out")? loginUser.getNickName() + "的离线好友":""%><br/>
<%} %>

------------<br/>
<%
int count = userList.size();
for(int i = paging.getStartIndex(); i < paging.getEndIndex(); i ++){
	if(i>count-1) break;
	String userId = (String) userList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
    //liuyi 2006-11-01 结义显示 start 
	UserFriendBean friend = userService.getUserFriend(loginUser.getId(), user.getId());
	UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(""+user.getId());
%>
<%=(i + 1)%>.<a href="<%=("/homeland/home.jsp?uid=" + uid)%>"><%=user.getNickNameWml() %></a>
<% if(onlineUser!=null){%>(<%=LoadResource.getPostionById(onlineUser.getId() + "", onlineUser.getPositionId()).getPositionName()%>)<%}%><br/>
<%
    //liuyi 2006-11-01 结义显示 end 
}
%>
<%=paging.shuzifenye("/homeland/friendList.jsp?uid=" + uid + "&amp;o="+option, true, "|", response)%>
<br/>
	<a href="<%=("/homeland/myPage.jsp") %>">回家</a>
	<br/><%=BaseAction.getBottomShort(request, response)%>
	</p>
	</card>
</wml>