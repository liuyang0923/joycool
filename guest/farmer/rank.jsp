<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="net.joycool.wap.bean.*,java.util.*,jc.guest.*,jc.guest.farmer.*,net.joycool.wap.framework.*"%><%
FarmAction action=new FarmAction(request,response);
GuestUserInfo guestUser = action.getGuestUser();
int uid = 0;
if(guestUser != null) {
	uid = guestUser.getId();
}
List list = FarmAction.service.getFarmerBeanList("change_lv > 0 order by lv1_num desc,lv2_num desc,lv3_num desc");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="完美农夫排行榜"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (uid > 0) {
	%><a href="userrecord.jsp?uid=<%=uid%>">我获得的评分</a><br/><%
}
if (list != null && list.size() > 0) {
	PagingBean paging = new PagingBean(action,list.size(),10,"p");
	for (int i = paging.getStartIndex();i < paging.getEndIndex(); i++) {
		FarmerBean fmBean = (FarmerBean) list.get(i);
		GuestUserInfo tempGuest = action.getGuestUser(fmBean.getUid());
  		%><%=i+1%>.<a href="userrecord.jsp?uid=<%=fmBean.getUid()%>"><%=tempGuest.getUserNameWml()%></a>&#160;<%=fmBean.getLv1()%><br/><%
	}
  	%><%=paging.shuzifenye("rank.jsp?jcfr=1",true,"|",response)%><%
} else {
	%>暂无排名!<br/><%
}
%><a href="index.jsp">返回完美农夫首页</a><br/><%
FarmerBean notice = action.getNotice();
if (notice != null) {
	GuestUserInfo tempGuest = action.getGuestUser(notice.getUid());
	%>恭喜<%=tempGuest.getUserNameWml()%>获得了1次完美评分!<br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>