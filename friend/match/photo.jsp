<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.UserBase,jc.credit.CreditAction,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,net.joycool.wap.bean.home.*"%>
<%! static net.joycool.wap.service.impl.HomeServiceImpl homeService = new net.joycool.wap.service.impl.HomeServiceImpl();
    static int COUNT_PRE_PAGE = 5;%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
String tip = "";
String call = "她";
UserBean user = null;
List photoList = null;
PagingBean paging = null;
boolean isMySelf = false;
MatchUser matchUser = null;
HomePhotoBean photo = null;
int uid = action.getParameterInt("uid");
if (uid <= 0){
	tip = "该用户没有参赛.";
} else {
	matchUser = MatchAction.getMatchUser(uid);
	if (matchUser == null){
		tip = "该用户没有参赛.";
	} else {
		if (loginUser != null && uid == loginUser.getId()){
			isMySelf = true;
			call = "我";
		}
		photoList = homeService.getHomePhotoList(" cat_id=0 and user_id=" + matchUser.getUserId() + " order by id desc");
		paging = new PagingBean(action, photoList.size(), COUNT_PRE_PAGE, "p");
		if (photoList == null || photoList.size() == 0){
			tip = call + "还没有上传过照片.";
		}
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="查看照片"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
user = UserInfoUtil.getUser(matchUser.getUserId());
if (user != null){
%><a href="vote.jsp?uid=<%=matchUser.getUserId()%>"><%=user.getNickNameWml()%></a>的参赛资料><%=call%>的照片<br/><%}%>
<%for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	photo = (HomePhotoBean)photoList.get(i);
	if (photo != null){
		%><%=i+1%>.<%=StringUtil.toWml(photo.getTitle())%><br/>
		<img src="<%=Constants.MYALBUM_RESOURCE_ROOT_URL%><%=photo.getAttach()%>" alt="o"/><br/><%
	}
}%><%=paging.shuzifenye("photo.jsp?uid=" + matchUser.getUserId(),true,"|",response)%>
<a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">为<%=call%>投票</a><br/>
<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">返回<%=call%>的参赛资料</a><br/>
<a href="index.jsp">返回乐后首页</a><br/>
<%
} else {
%><%=tip%><br/><a href="index.jsp">返回乐后首页</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>