<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>
<%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*,java.util.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	int mid = action.getParameterInt("mid");
	int uid = 0;
	if(action.getLoginUser() != null)
		uid = action.getLoginUser().getId();
	List ll = null;
	List rl = null;
	if(mid > 0){
		ll = action.service.getLiveList(" del=0 and match_id="+ mid + " order by create_time desc");
		rl = action.service.getReplyList("del=0 and match_id="+ mid + " order by create_time desc");
	}
	BeanMatch bm = action.getMatchById(mid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><% 
if(bm != null){
	if(bm.getStaticValue() == 2){%>
<card title="比赛直播间" ontimer="<%=response.encodeURL("alive.jsp?mid="+mid+"&amp;p="+action.getParameterInt("p")+"&amp;pg="+action.getParameterInt("pg"))%>"><timer value="300"/><p>
<%=BaseAction.getTop(request, response)%><%
		%><%=bm.getTeam1()%>VS<%=bm.getTeam2()%>&#160;&#160;<%=bm.getCode()%>&#160;&#160;<%
		if(bm.getPart() > 4){
		%>加时赛<%=bm.getPart()-4%><br/><%
		}else{
		%>第<%=bm.getPart()%>节<br/><%
		}
		%><a href="alive.jsp?mid=<%=mid%>">刷新</a>|<a href="reply.jsp?mid=<%=mid%>">发言</a>共有<%=bm.getSumLive()%>条直播信息<br/><%
		if(ll != null && ll.size() > 0){
			PagingBean paging = new PagingBean(action, ll.size(), 5, "pg");
			for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
				BeanLive bl = (BeanLive)ll.get(i);
				%>•<%=StringUtil.toWml(bl.getCont())%><br/><%
		  	}
		%><%=paging.shuzifenye("alive.jsp?jcfr=1&amp;mid="+mid, true, "|", response)%><%
		}
		%>支持<%
		if(mid > 0 && action.getSupportById(uid,mid) != null){
		%>&#160;&#160;<%=bm.getTeam1()%>&#160;<%=bm.getSupport1()%>VS<%=bm.getTeam2()%>&#160;<%=bm.getSupport2()%>(感谢您的支持!)<br/><%
		}else{
		%>&#160;&#160;<a href="sptbk.jsp?spt=1&amp;mid=<%=mid%>"><%=bm.getTeam1()%></a>&#160;<%=bm.getSupport1()%>VS
		<a href="sptbk.jsp?spt=2&amp;mid=<%=mid%>"><%=bm.getTeam2()%></a>&#160;<%=bm.getSupport2()%><br/><%
		}
		%><a href="reply.jsp?mid=<%=mid%>">发言</a>共有<%=bm.getSumReply()%>条评论<br/><%
		if(rl != null && rl.size() > 0){
			PagingBean paging = new PagingBean(action, rl.size(), 5, "p");
			for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
				BeanReply br = (BeanReply)rl.get(i);
		 		if(br.getUid() == 0){
		 			%>直播间管理员:<%
				}else{
					%><a href="../../user/ViewUserInfo.do?userId=<%=br.getUid()%>"><%=UserInfoUtil.getUser(br.getUid()).getNickNameWml()%></a>:<%
				}													
				%><%=StringUtil.toWml(br.getCont())%>&#160;<%=action.format(br.getCreateTime(),"HH:mm")%><br/><%
			}
		%><%=paging.shuzifenye("alive.jsp?jcfr=1&amp;mid="+mid, true, "|", response)%><%
		}
%><a href="match.jsp?maid=<%=mid%>">返回赛事查询</a><br/><a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%><%
	}else if(bm.getStaticValue() == 0){
%><card title="=比赛直播间="><p><%=BaseAction.getTop(request, response)%><%
%><%=bm.getTeam1()%>VS<%=bm.getTeam2()%>&#160;开赛时间:<%=DateUtil.formatDate2(bm.getStartTime())%>&#160;<br/><%
%><a href="match.jsp?maid=<%=mid%>">返回赛事查询</a><br/><a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%><%
	}else{
%><card title="=比赛直播间="><p><%=BaseAction.getTop(request, response)%><%
		if(ll != null && ll.size() > 0){
			%><%=bm.getTeam1()%>VS<%=bm.getTeam2()%>&#160;&#160;<%=bm.getCode()%>&#160;&#160;已完赛<br/><%
		%><a href="alive.jsp?mid=<%=mid%>">刷新</a>|<a href="reply.jsp?mid=<%=mid%>">发言</a>共有<%=bm.getSumLive()%>条直播信息<br/><%
			if(ll != null && ll.size() > 0){
				PagingBean paging = new PagingBean(action, ll.size(), 5, "pg");
				for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
					BeanLive bl = (BeanLive)ll.get(i);
					%>•<%=StringUtil.toWml(bl.getCont())%><br/><%
			  	}
			%><%=paging.shuzifenye("alive.jsp?jcfr=1&amp;mid="+mid, true, "|", response)%><%
			}
			%>支持:&#160;&#160;<%=bm.getTeam1()%>&#160;<%=bm.getSupport1()%>VS<%=bm.getTeam2()%>&#160;<%=bm.getSupport2()%><br/><%
		%><a href="reply.jsp?mid=<%=mid%>">发言</a>共有<%=bm.getSumReply()%>条评论<br/><%
			if(rl != null && rl.size() > 0){
				PagingBean paging = new PagingBean(action, rl.size(), 5, "p");
				for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
					BeanReply br = (BeanReply)rl.get(i);
			 		if(br.getUid() == 0){
			 			%>直播间管理员:<%
					}else{
						%><a href="../../user/ViewUserInfo.do?userId=<%=br.getUid()%>"><%=UserInfoUtil.getUser(br.getUid()).getNickNameWml()%></a>:<%
					}													
					%><%=StringUtil.toWml(br.getCont())%>&#160;<%=action.format(br.getCreateTime(),"HH:mm")%><br/><%
				}
			%><%=paging.shuzifenye("alive.jsp?jcfr=1&amp;mid="+mid, true, "|", response)%><%
			}
		}else{
			%>对不起，该比赛未进行直播<br/><%
		}
%><a href="match.jsp?maid=<%=mid%>">返回赛事查询</a><br/><a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%><%
	}
}else{
%><card title="=比赛直播间="><p><%=BaseAction.getTop(request, response)%><%
%>不存在该赛事直播!<br/><a href="match.jsp">返回赛事查询</a><br/><a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%><%
}%></p>
</card>
</wml>