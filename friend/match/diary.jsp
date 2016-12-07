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
List diaryList = null;
PagingBean paging = null;
boolean isMySelf = false;
MatchUser matchUser = null;
HomeDiaryBean diary = null;
int diaryId = action.getParameterInt("did");
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
		if (diaryId > 0){
			// 查看某一篇日记
			diary = homeService.getHomeDiary(" id=" + diaryId);
			if (diary == null || diary.getDel() == 1){
				tip = "没有找到这篇日记.";
			} else if (diary.getUserId() != matchUser.getUserId()){
				tip = "不是她的日记.";
			}
		} else {
			// 显示日记列表
			diaryList = homeService.getHomeDiaryList(" cat_id=0 and user_id=" + matchUser.getUserId() + " and del=0 order by id desc");
			paging = new PagingBean(action, diaryList.size(), COUNT_PRE_PAGE, "p");
			if (diaryList == null || diaryList.size() == 0){
				tip = call + "还没有写过日记.";
			}
		}
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="查看日记"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
user = UserInfoUtil.getUser(matchUser.getUserId());
if(diaryId > 0){
if (user != null){
%><a href="vote.jsp?uid=<%=matchUser.getUserId()%>"><%=user.getNickNameWml()%></a>的日记<br/><%
}%>
<%=StringUtil.toWml(diary.getTitel())%>(阅<%=diary.getHits()%>|评<%=diary.getReviewCount()%>)<br/>
<%=DateUtil.sformatTime(diary.getCreateTime())%><br/>
<%=StringUtil.toWml(diary.getContent())%><br/>
<a href="/home/homeDiary.jsp?diaryId=<%=diary.getId()%>&amp;userId=<%=matchUser.getUserId()%>">查看完整版</a><br/>
<a href="diary.jsp?uid=<%=matchUser.getUserId()%>">返回日记列表</a><br/>
<%
} else {
if (user != null){
%><a href="vote.jsp?uid=<%=matchUser.getUserId()%>"><%=user.getNickNameWml()%></a>的参赛资料><%=call%>的日记<br/><%}%>
<%for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	diary = (HomeDiaryBean)diaryList.get(i);
	if (diary != null){
		%><%=i+1%>.<a href="diary.jsp?uid=<%=matchUser.getUserId()%>&amp;did=<%=diary.getId()%>"><%=StringUtil.toWml(diary.getTitel())%></a>(<%=diary.getReviewCount() %>/<%=diary.getHits() %>)<br/><%
	}
}%><%=paging.shuzifenye("diary.jsp?uid=" + matchUser.getUserId(),true,"|",response)%>
<%}%>
<a href="vote2.jsp?uid=<%=matchUser.getUserId()%>">为<%=call%>投票</a><%if(!isMySelf){%>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注她</a><%}%><br/>
<a href="vote.jsp?uid=<%=matchUser.getUserId()%>">返回<%=call%>的参赛资料</a><br/>
<a href="index.jsp">返回乐后首页</a><br/>
<%
} else {
%><%=tip%><br/><a href="index.jsp">返回乐后首页</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>