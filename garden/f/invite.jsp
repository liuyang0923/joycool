<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static int COUNT_PER_PAGE = 5;%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List list = UserInfoUtil.getUserFriends(action.getLoginUserId());
	ArrayList noneList = new ArrayList();		//没有花园的朋友
	FlowerUserBean fub = null;
	UserBean user = null;
	int friendId = 0;
	for (int i=0;i<list.size();i++){
		friendId = StringUtil.toInt(list.get(i).toString());
		user = UserInfoUtil.getUser(friendId);
		//取出的ID有可能不存在，所以要再判断一下。
		if ( user != null ){
			fub = FlowerUtil.getUserBean(friendId);
			if ( fub == null ){
				noneList.add(new Integer(friendId));
			}
   		}
   	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
邀请好友<br/>
好东西要与好朋友一起分享哦，马上邀请你的好友来加入花园吧！<br/>
<% if (noneList.size() != 0){
   		PagingBean paging = new PagingBean(action, noneList.size(), COUNT_PER_PAGE, "p");
   		int endIndex = paging.getEndIndex();
		for(int i = paging.getStartIndex();i < endIndex;i++){
			friendId = StringUtil.toInt(String.valueOf(noneList.get(i)));
			%><%=i + 1%>.<a href="doInvite.jsp?uid=<%=friendId%>">邀请开通</a>.<a href="/user/ViewUserInfo.do?userId=<%=friendId%>"><%=UserInfoUtil.getUser(friendId).getNickNameWml()%></a><br/><%
		}%><%=paging.shuzifenye("invite.jsp",false,"|",response)%><%
   } else {
	   %>您没有未开通花园的好友.<br/>
	   <a href="../../user/ViewFriends.do">添加其他乐酷好友</a><br/>
		添加其他花园玩家为好友<br/><%
			List list2 = SqlUtil.getIntList("select user_id from flower_user where user_id <> " + action.getLoginUserId() + " order by rand() limit 5",5);
			Integer otherUserId;
			for (int i=0;i<list2.size();i++){
					otherUserId = (Integer)list2.get(i);
					%><a href="/user/ViewUserInfo.do?userId=<%=otherUserId.intValue()%>"><%=UserInfoUtil.getUser(otherUserId.intValue()).getNickNameWml()%></a><br/><%
			}%><a href="more.jsp">更多花园玩家</a><br/>
	   <%
   }%><a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>