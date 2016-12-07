<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=10; %>
<%
MatchAction action = new MatchAction(request);
UserInfo userInfo = null;
MatchUser matchUser = null;
MatchArea matchArea = null;
MatchPhotoHistory history = null;
int pass = action.getParameterInt("pass");
int del = action.getParameterInt("del");
String photoAddress = "";
// 审核通过
if (pass > 0){
	matchUser = MatchAction.getMatchUser(pass);
	if (matchUser != null){
		SqlUtil.executeUpdate("update match_user set checked=1 where user_id=" + matchUser.getUserId(),5);
		matchUser.setChecked(1);
		// 改缓存
		MatchAction.matchUserCache.put(new Integer(matchUser.getUserId()),matchUser);
		// 写入历史记录
		history = new MatchPhotoHistory();
		history.setUserId(matchUser.getUserId());
		history.setPhoto(matchUser.getPhoto());
		history.setPhoto2(matchUser.getPhoto2());
		history.setPhotoFrom(matchUser.getPhotoFrom());
		MatchAction.service.addPhotoHistory(history);
		//SqlUtil.executeUpdate("insert into match_photo_history (user_id,photo,photo_from,create_time) values (" + user.getUserId() + ",(select photo,photo_from from match_user where user_id=" + user.getUserId() + "),now())",5);
		//NoticeAction.sendNotice(user.getUserId(), "" + "恭喜你,您的参赛信息已获批准,您目前的参赛票" + user.getVoteCount() +  "票,赶紧让你的粉丝为你投票吧!!", "", NoticeBean.GENERAL_NOTICE, "", "/friend/match/index.jsp");
	}
}
// 不通过
if (del > 0){
	matchUser = MatchAction.getMatchUser(del);
	if (matchUser != null){
		SqlUtil.executeUpdate("update match_user set checked=2,photo='o.gif',photo2='o.gif' where user_id=" + matchUser.getUserId(),5);
		matchUser.setChecked(2);
		matchUser.setPhoto("o.gif");
		matchUser.setPhoto2("o.gif");
		// 改缓存
		MatchAction.matchUserCache.put(new Integer(matchUser.getUserId()),matchUser);
		//NoticeAction.sendNotice(user.getUserId(), "" + "您的可信度或真实照片有误,现不能参赛,请修改您的交友可信度信息和上传真实相片.", "", NoticeBean.GENERAL_NOTICE, "", "/friend/match/index.jsp");
		// 删除图片[注意不能删除o.gif和x.gif]
		if (matchUser.getPhoto().length() > 5){
			File f = new File(action.getCurrentAddress2(matchUser.getPhotoFrom()) + matchUser.getPhoto());
			if (f.exists()){
				f.delete();
			}
			// 删除缩略图
			f = new File(MatchAction.ATTACH_ROOT + matchUser.getPhoto2());
			if (f.exists()){
				f.delete();
			}
		}
	}
}
// 取消资格
int cancel = action.getParameterInt("c");
if (cancel > 0){
	SqlUtil.executeUpdate("update match_user set checked=3 where user_id=" + cancel,5);
	matchUser = MatchAction.getMatchUser(cancel);
	if (matchUser != null){
		matchArea = MatchAction.getArea(matchUser.getAreaId());
		matchUser.setChecked(3);
		//改缓存
		MatchAction.matchUserCache.put(new Integer(matchUser.getUserId()),matchUser);
	}
	// 被取消的用户所在的分区人数-1
	SqlUtil.executeUpdate("update match_area set `count`=`count`-1 where id=(select area_id from match_user where user_id=" + cancel + ")",5);
	if (matchArea != null){
		matchArea.setCount(matchArea.getCount() - 1);
	}
}
int recover = action.getParameterInt("r");
if (recover > 0){
	SqlUtil.executeUpdate("update match_user set checked=0 where user_id=" + recover,5);
	matchUser = MatchAction.getMatchUser(recover);
	if (matchUser != null){
		matchUser.setChecked(0);
		//改缓存
		MatchAction.matchUserCache.put(new Integer(matchUser.getUserId()),matchUser);
	}
}
MatchInfo matchInfo = MatchAction.getCurrentMatch();
int playerCount = (matchInfo != null ? matchInfo.getUserCount() : 0);
PagingBean paging = new PagingBean(action,playerCount,COUNT_PRE_PAGE,"p");
List list = MatchAction.service.getMatchUserList(" checked=0 order by create_time desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>

<%@page import="java.io.File"%><html>
	<head>
		<title>审核参赛用户</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a><br/>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>用户</font>
				</td>
				<td align=center>
					<font color=#1A4578>上一张照片</font>
				</td>
				<td align=center>
					<font color=#1A4578>照片</font>
				</td>
				<td align=center>
					<font color=#1A4578>时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>审核</font>
				</td>
			</tr>
			<% for (int i = 0 ; i < list.size() ; i++){
				matchUser = (MatchUser)list.get(i);
				history = MatchAction.service.getMatchPhotoHistory(" user_id=" + matchUser.getUserId() + " order by id desc limit 1");
				if (matchUser != null){
					%><tr>
							<td><%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%>(<%=matchUser.getUserId()%>)</td>
							<td><%if (history != null){
									  %><img src="<%=action.getCurrentAddress(history.getPhotoFrom()) + history.getPhoto()%>" alt="无法显示" /><%
							      }%></td>
							<td><img src="<%=action.getCurrentAddress(matchUser.getPhotoFrom()) + matchUser.getPhoto()%>" alt="无法显示" /></td>
							<td><%=DateUtil.formatSqlDatetime(matchUser.getCreateTime())%></td>
							<td><a href="checkUser.jsp?pass=<%=matchUser.getUserId()%>&p=<%=paging.getCurrentPageIndex()%>" onclick="return confirm('确认通过审核？')"><font color=green>审核通过</font></a>|<a href="checkUser.jsp?del=<%=matchUser.getUserId()%>&p=<%=paging.getCurrentPageIndex()%>"><font color=red>不通过</a></font>|
							<%if (matchUser.getChecked() == 3){
								%><a href="checkUser.jsp?r=<%=matchUser.getUserId()%>" onclick="return confirm('真的要恢复她的资格吗？')">恢复资格</a><%
							} else {
								%><a href="checkUser.jsp?c=<%=matchUser.getUserId()%>" onclick="return confirm('真的要取消她的资格吗？')">取消资格</a><%
							}%></td>
					  </tr><%
				}
			}%>
		</table>
		<%=paging!=null?paging.shuzifenye("checkUser.jsp",false,"|",response):""%>
	</body>
</html>