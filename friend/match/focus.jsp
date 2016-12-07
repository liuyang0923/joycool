<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,net.joycool.wap.bean.home.HomeDiaryBean,net.joycool.wap.bean.home.HomeUserBean,net.joycool.wap.cache.util.HomeCacheUtil"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
boolean isInMatch = false;
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
} else {
	if (MatchAction.getMatchUser(loginUser.getId()) != null){
		isInMatch = true;
	}
}
String tip = "";
List list = null;
UserBean user = null;
MatchUser matchUser = null;
MatchFocus matchFocus = null;
HomeUserBean homeUser = null;
PagingBean paging = null;
int uid = action.getParameterInt("uid");
if (uid > 0){
	user = UserInfoUtil.getUser(uid);
	if (user == null){
		tip = "此用户不存在.";
	} else {
		matchUser = MatchAction.getMatchUser(user.getId());
		if (matchUser == null){
			tip = "此用户没有参加比赛.";
		} else {
			int confirm = action.getParameterInt("c");
			matchFocus = MatchAction.service.getFocus(" uid=" + loginUser.getId() + " and uid2=" + matchUser.getUserId() + " limit 1");
			if (matchFocus == null){
				if (confirm > 0){
					tip = "您没有关注过此用户.";
				} else {
					if (matchUser.getUserId() == loginUser.getId()){
						tip = "不可关注自己.";
					} else {
						matchFocus = new MatchFocus();
						matchFocus.setUid(loginUser.getId());
						matchFocus.setUid2(matchUser.getUserId());
						MatchAction.service.addFocus(matchFocus);
						tip = "添加关注成功.";
					}
				}
			} else {
				if (confirm == 1){
					tip = "您确定要取消对" + user.getNickNameWml() + "的关注吗?这样您将无法查看对方的参赛动态信息.<br/><a href=\"focus.jsp?uid=" + matchUser.getUserId() + "&amp;c=2\">确定</a>|<a href=\"focus2.jsp\">再考虑考虑</a>";
				} else if (confirm == 2){
					SqlUtil.executeUpdate("delete from match_focus where uid=" + loginUser.getId() + " and uid2=" + matchUser.getUserId(),5);
					tip = "已取消关注.<br/><a href=\"focus.jsp\">返回</a>";
				} else {
					tip = "您已经关注过此用户了.<br/><a href=\"focus.jsp\">返回</a>";
				}
			}
		}
	}
} else {
	list = MatchAction.service.getFocusList(" uid=" + loginUser.getId() + " order by id desc limit 50");
	if (list == null || list.size() == 0){
		tip = "您还没有关注过任何靓女,赶快找个心仪的参赛靓女,并加关注吧:),查看参赛资料就可找到添加功能.";
	} else {
		paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="我的关注"><p>
<%=BaseAction.getTop(request, response)%>
<a href="index.jsp">乐后首页</a>>我关注的靓女<br/>
<%if (isInMatch){
%><a href="focus2.jsp?t=1">关注我的用户</a><br/><%
}
if ("".equals(tip)){
%><a href="focus2.jsp">我关注的靓女排名</a><br/><%
for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
	matchFocus = (MatchFocus)list.get(i);
	if (matchFocus != null){
		user = UserInfoUtil.getUser(matchFocus.getUid2());
		homeUser = HomeCacheUtil.getHomeCache(matchFocus.getUid2());
		if (homeUser != null){
			if (user != null){
				%><%=i+1%>.<a href="/home/home.jsp?userId=<%=homeUser.getUserId()%>"><%=user.getNickNameWml()%></a>(日记<%=homeUser.getDiaryCount()%>,相册<%=homeUser.getPhotoCount()%>)<br/><%
			}
		} else {
			if (user != null){
				%><%=i+1%>.<%=user.getNickNameWml()%>(日记0,相册0)<br/><%
			}
		}
	}
}%><%=paging.shuzifenye("focus.jsp",false,"|",response)%><%
} else {
%><%=tip%><br/><%
}
%><a href="index.jsp">返回选美首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>