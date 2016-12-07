<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,net.joycool.wap.bean.*,java.util.*,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
String friendIdList = ub.getFriendString();
List friendList = action.getFriShowList(friendIdList); 
PagingBean paging = null;
if (friendList != null && friendList.size() > 0){
 paging = new PagingBean(action, friendList.size(), 5, "p");
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="好友酷秀">
<p><%=BaseAction.getTop(request, response)%>==好友酷秀==<br/><%
if (friendList != null && friendList.size() > 0){
		for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
			CoolUser cu = (CoolUser)friendList.get(i);
			UserBean fub = UserInfoUtil.getUser(cu.getUid());
			if(fub != null){
			%><%=i+1%>.<a href="/user/ViewUserInfo.do?userId=<%=fub.getId()%>"><%=fub.getNickNameWml()%></a><br/>
<a href="/kshow/frishow.jsp?uid=<%=fub.getId()%>"><img src="/rep/show/t/<%=CoolShowAction.getCoolShowThumb(fub)%>" alt="秀"/></a><br/><%
			}
		}%><%=paging.shuzifenye("frdshowlist.jsp",false,"|",response)%><%
}else{
%>你还没有好友!<br/><%
}		
%><a href="index.jsp">&gt;我的酷秀</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>