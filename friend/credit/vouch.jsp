<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<%! static int COUNT_PRE_PAGE = 5; %>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   String tip = "";
   CreditProvince province = null;
   UserInfo userInfo = action.getUserInfo(action.getLoginUser().getId());
   UserBase userBase = CreditAction.getUserBaseBean(action.getLoginUser().getId());
   PagingBean paging = null;
   List list = null;
   if (userInfo == null){
	   tip = "用户不存在";
   } else {
	   list = action.searchFriend(userBase,25,RandomUtil.nextInt(10));
	   paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%>欢迎进入乐酷社区,您的交友可信度为<%=userInfo.getTotalPoint()%>%,系统为您推荐了一些交友用户,快去看看吧!<br/><%
if (list.size() > 0){
	for (int i = paging.getStartIndex();i<paging.getEndIndex();i++){
		userBase = CreditAction.getUserBaseBean(list.get(i)!=null?StringUtil.toInt(list.get(i).toString()):0);
		if (userBase != null){
			province = CreditAction.service.getProvince(" id=" + userBase.getProvince());
			%><%=i+1%>.<a href="/chat/post.jsp?toUserId=<%=userBase.getUserId() %>"><%=UserInfoUtil.getUser(userBase.getUserId()).getNickNameWml()%></a><br/><%if (userBase.getPhoto() != null && !"".equals(userBase.getPhoto())){%><img src="<%=CreditAction.ATTACH_URL_ROOT + "/" + userBase.getPhoto()%>" alt="o"/><br/><%}%><%=userBase.getGenderTest()%>|<%=userBase.getAge()%>|<%=province != null?province.getProvince():"未填写" %><br/><%
		}
	}%><%=paging.shuzifenye("vouch.jsp",false,"|",response)%><%
}
} else {
%><%=tip%><br/><a href="navi.jsp">返回可信度首页</a><br/><%	
}%>
如果没有符合您要求的,或者您想让更多的朋友关注您,我们建议您<a href="navi.jsp">完善个人资料</a>.<br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>