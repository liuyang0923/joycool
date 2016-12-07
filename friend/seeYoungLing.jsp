<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.HashMap" %><%@ page import="java.util.*,net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
//action.seeYoungLing(request);
List list = action.getOnlineUserList();
// 获取在线用户个数
int count = list.size();
HashMap hm = new HashMap();
for (int i = 0; i < count; i++) {
	// 获取一个随机在线用户
	int pos = (int) RandomUtil.nextInt(list.size());
	// 判断用户是否为登录用户并且ID不是自己(不判断了)
	list.get(pos);
	int userId = StringUtil.toInt((String) list.get(pos));
	if (userId <= 0 ) {		// ||  userId == action.getLoginUser().getId()
		continue;
	}
	// 获取用户信息
	UserBean user = (UserBean) UserInfoUtil.getUser(userId);
	if (user == null) {
		continue;
	} else if (action.checkDatetime(user.getCreateDatetime())) {
		continue;
	}
	if (hm.get(new Integer(user.getId())) == null) {
		hm.put(new Integer(user.getId()), user);
	}
	// 查看是否已经有5个人
	if (hm.size() >= 10) {
		break;
	}
}
request.setAttribute("hm", hm);
String result=(String) request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//HashMap hm = (HashMap) request.getAttribute("hm");
%>
<card title="乐酷新人欢迎区">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
Iterator it = hm.values().iterator();
int i = 1 ;
while(it.hasNext()){
	UserBean user=(UserBean)it.next();
	if(user==null){continue;}%>
	<%=i%>.
	<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%i++;}%>
<a href="/friend/seeYoungLing.jsp">换下一批新人</a><br/>

<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>