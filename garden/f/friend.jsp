<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static int COUNT_PER_PAGE = 10; %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List list = UserInfoUtil.getUserFriends(action.getLoginUserId());
	ArrayList myFriendList = new ArrayList();
	FlowerUserBean fub = null;
	UserBean user = null;
	int friendId = 0;
	for (int i=0;i<list.size();i++){
		friendId = StringUtil.toInt(list.get(i).toString());
		user = UserInfoUtil.getUser(friendId);
		//取出的ID有可能不存在，所以要再判断一下。
		if ( user != null ){
			fub = FlowerUtil.getUserBean(friendId);
			if ( fub != null ){
				myFriendList.add(new Integer(friendId));
			}
   		}
   	}%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
好友的花园<br/>
<% if (myFriendList.size() > 0){
	   PagingBean paging = new PagingBean(action, myFriendList.size(), COUNT_PER_PAGE, "p");
	   int endIndex = paging.getEndIndex();
	   for (int i = paging.getStartIndex();i < endIndex;i++){
			friendId = StringUtil.toInt(myFriendList.get(i).toString());
			user = UserInfoUtil.getUser(friendId);
			//取出的ID有可能不存在，所以要再判断一下。
			if ( user!=null ){
				%><%=i + 1%>.<a href="fgarden2.jsp?fuid=<%=friendId %>"><%=user.getNickNameWml()%><br/></a><%
	   		}
	   }%><%=paging.shuzifenye("friend.jsp",false,"|",response)%><%
   } else {
		%>您还没有开通花园的好友<br/>
		  <a href="invite.jsp">邀请你的乐酷好友加入花园</a><br/>
		  添加其他花园玩家为好友<br/><%
		  List list2 = SqlUtil.getIntList("select user_id from flower_user where user_id <> " + action.getLoginUserId() + " order by rand() limit 5",5);
		  Integer otherUserId;
		  for (int i=0;i<list2.size();i++){
		  		otherUserId = (Integer)list2.get(i);
		  		%><a href="/user/ViewUserInfo.do?userId=<%=otherUserId.intValue()%>"><%=UserInfoUtil.getUser(otherUserId.intValue()).getNickNameWml() %></a><br/><%
		  }
}%>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>