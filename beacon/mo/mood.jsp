<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static MoodService service=new MoodService();
	static int COUNT_PRE_PAGE=6; %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	MoodAction action=new MoodAction(request);
	int uid;
	//此页面即可以接收参数"id"，也可以接收参数"uid",最终目的是要得到此心情主人的UID。
	int id=action.getParameterInt("id");
	//根据心情id来取得心情
	MoodBean mb=service.getMoodById(id);
	//再根据此心情来取得uid。如果心情为空，则uid等于登陆者的id
	if (mb!=null){
		uid=mb.getUserId();
	}else{
		uid=action.getParameterInt("uid");
		if(uid==0){
			uid=action.getLoginUser().getId();
		}
	}
	
	UserBean user = UserInfoUtil.getUser(uid);
	int	logoUid=action.getLoginUser().getId();
	
    int totalCount = SqlUtil.getIntResult("select count(*) from mood_list where user_id=" + uid, 5);
    PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
	ArrayList list=service.getAllMoodById(uid,pageNow,COUNT_PRE_PAGE);
%><wml>
<card title="心情">
<p><%=BaseAction.getTop(request, response)%><%if(pageNow == 0&&list.size()>0){
mb=(MoodBean)list.get(0);
%><% if ( mb != null ){
		if (logoUid==uid){
			%><a href="../../user/ViewUserInfo.do?userId=<%=logoUid%>"><%=user.getNickNameWml()%></a><%
		} else {
			%><a href="../../user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a><%
		}
	  
}%>的心情:<img src="img/<%=mb.getType()%>.gif" alt="<%=mb.getTypeName()%>" /><a href="view.jsp?id=<%=mb.getId()%>"><%=StringUtil.toWml(StringUtil.limitString(mb.getMood(),20))%></a>(回<%=mb.getReplyCount()%>)<br/><%
}
	mb=null;
	if (logoUid==uid && pageNow==0){
		%>=写心情(最多50字)=<br/>
<select name="xq" value="<%=RandomUtil.nextInt(30) + 1%>"><%
		List moodType=action.getMoodType();
		   for(int i=1;i<moodType.size();i++){
				%><option value="<%=i%>"><%=moodType.get(i) %></option><%
		   }
	  %></select>
		<input name="mood" value="" maxlength="100" /><br/>
		<anchor>
			添加此刻的心情
			<go href="operate.jsp?id=<%=uid%>&amp;f=create" method="post">
				<postfield name="mood" value="$mood" />
				<postfield name="xq" value="$xq"/>
			</go>
		</anchor><br/><%
	}
%>=<%=user.getNickNameWml()%>的心情历程=<br/><%
if(list.size()==0){
%>(暂无)<br/><%
}
	for(int i=0;i<list.size();i++){
		mb=(MoodBean)list.get(i);
		%><a href="view.jsp?id=<%=mb.getId()%>"><%=StringUtil.toWml(StringUtil.limitString(mb.getMood(),20))%></a><br/>(回<%=mb.getReplyCount()%>)<%=DateUtil.sformatTime(mb.getCreateTime())%><br/>
		<%--<%
			//登陆者可以删除自己的心情
			if(logoUid==uid){
				%><a href="operate.jsp?id=<%=mb.getId()%>&amp;f=mdel">删</a><%
			}
		%>
		--%><%
	}
%><%=paging.shuzifenye("mood.jsp?uid=" + uid, true, "|", response)%>
<%	if (mb != null){
		if (logoUid==uid){
			%><a href="sameMood.jsp">与我同心情的朋友</a><br/>
			  <a href="friend.jsp">我的好友的心情</a><br/>
			  <a href="../../user/ViewUserInfo.do?userId=<%=logoUid%>">返回我的资料</a><br/><%
		} else {
			%><a href="../../user/ViewUserInfo.do?userId=<%=user.getId()%>">返回<%=user.getNickNameWml()%>的资料</a><br/>
			<a href="mood.jsp">返回我的心情</a><br/><%
		}
	}
%><%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>