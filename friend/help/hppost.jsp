<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,java.util.*,net.joycool.wap.framework.BaseAction,jc.answer.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	HelpAction action = new HelpAction(request);
	UserBean ub = action.getLoginUser();
	if(ub == null){
		response.sendRedirect("/user/login.jsp");
		return;
	}
	List list = null;
	BeanProblem bp = null;
	int loguid = action.getLoginUser().getId();
	int pid = action.getParameterInt("pid");
	if(pid > 0){
		action.updHpPost(pid);
		list = action.getAnswerAll(pid);
		bp = action.getProb(pid);
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%if(bp == null){%><card title="有偿求助" ontimer="<%=response.encodeURL("hpindex.jsp")%>">
<timer value="30"/><p><%=BaseAction.getTop(request, response)%>
您好，该主题不存在!3秒钟自动<a href="hpindex.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%></p></card><%
}else{%><card title="<%=StringUtil.toWml(bp.getPTitle())%>"><p><%=BaseAction.getTop(request, response)%><%
	if(true){
 %><a href="/user/ViewUserInfo.do?userId=<%=bp.getUid()%>"><%=UserInfoUtil.getUser(bp.getUid()).getNickNameWml()%></a><%=DateUtil.formatDate2(bp.getCreateTime())%><%
	} else {
	%><a href="/chat/post.jsp?toUserId=<%=bp.getUid()%>"><%=UserInfoUtil.getUser(bp.getUid()).getNickNameWml()%></a><%=DateUtil.formatDate2(bp.getCreateTime())%><%
	}
  %><br/>奖励:<%=StringUtil.toWml(bp.getPrize())%><br/>内容:<br/><%=StringUtil.toWml(bp.getPCont())%><br/><%
if(bp.getUid()==loguid){
%>楼主信誉投票:鲜花(<%=bp.getNumFlower()%>)臭鸡蛋(<%=bp.getNumEgg()%>)<br/><%
	
	if(list != null && list.size() > 0){
		PagingBean paging = new PagingBean(action, list.size(), 10, "p");
		for(int i = paging.getStartIndex();i < paging.getEndIndex();i++){
			BeanAnswer ba = (BeanAnswer)list.get(i);
			if(ba != null){
					if(true){
				%><a href="/user/ViewUserInfo.do?userId=<%=ba.getUid()%>"><%=UserInfoUtil.getUser(ba.getUid()).getNickNameWml()%></a><%
					}else{
				%><a href="/chat/post.jsp?toUserId=<%=ba.getUid()%>"><%=UserInfoUtil.getUser(ba.getUid()).getNickNameWml()%></a><%
					}
				 %>:<%=StringUtil.toWml(ba.getACont())%>(<%=action.format(ba.getCreateTime())%>)<%
				if(bp.getIsOver() == 1){
					if(ba.getFlag() ==1){
					%>已采用<%
					}
					%><br/><%
				}else{
					if(ba.getFlag() ==1){
					%>已采用<br/><%
					}else{
					%><a href="hpback3.jsp?aid=<%=ba.getId()%>&amp;pid=<%=bp.getId()%>">采用</a><br/><%
					}
				}
			}
		}
		%><%=paging.shuzifenye("hppost.jsp?jcfr=1&amp;pid="+bp.getId(), true, "|", response)%><%
	}
	if(bp.getIsOver() == 1){
		if(bp.getIsSovle() == 1){
		%>此帖已解决<br/><%
		}else{
		%>楼主点评：无满意答案，此帖已结束。<br/><%
		}
	}else{
		if(bp.getIsSovle() == 1){
		%>[<a href="hpback3.jsp?pid=<%=bp.getId()%>&amp;o=1">结束此帖</a>]<br/><%
		}else{
		%>[<a href="hpback3.jsp?pid=<%=bp.getId()%>&amp;o=1">无满意答案,结束此帖</a>]<br/><%
		}
	}
	%><a href="hpindex.jsp">返回有偿求助</a><br/><%
}else{
%>楼主信誉投票:<%if(HelpAction.service.isVote(pid,loguid)!=null || bp.getIsOver() == 1){%>鲜花(<%=bp.getNumFlower()%>)臭鸡蛋(<%=bp.getNumEgg()%>)<br/><%}else{%><a href="hpback3.jsp?vt=1&amp;pid=<%=bp.getId()%>">鲜花</a>(<%=bp.getNumFlower()%>)<a href="hpback3.jsp?vt=2&amp;pid=<%=bp.getId()%>">臭鸡蛋</a>(<%=bp.getNumEgg()%>)<br/><%}
	if(bp.getIsOver() != 1){
	%><input type="text" name="asw"/>
	<anchor>回复
	<go href="hpback2.jsp?pid=<%=bp.getId()%>" method="post">
	<postfield name="asw" value="$asw"/>
	</go>
	</anchor><br/><%
	}
	if(list != null && list.size() > 0){
		PagingBean paging = new PagingBean(action, list.size(), 10, "p");
		for(int i = paging.getStartIndex();i < paging.getEndIndex();i++){
			BeanAnswer ba = (BeanAnswer)list.get(i);
			if(ba != null){
					if(true){
				%><a href="/user/ViewUserInfo.do?userId=<%=ba.getUid()%>"><%=UserInfoUtil.getUser(ba.getUid()).getNickNameWml()%></a><%
					}else{
				%><a href="/chat/post.jsp?toUserId=<%=ba.getUid()%>"><%=UserInfoUtil.getUser(ba.getUid()).getNickNameWml()%></a><%
					}
				 %>:<%=StringUtil.toWml(ba.getACont())%>(<%=action.format(ba.getCreateTime())%>)<%
				 	if(ba.getFlag() == 1){
					%>已采用<%
				 	}
				  %><br/><%
			}
		}
		%><%=paging.shuzifenye("hppost.jsp?jcfr=1&amp;pid="+bp.getId(), true, "|", response)%><%
	}
	if(bp.getIsOver() == 1){
		if(bp.getIsSovle() == 1){
		%>此帖已解决<br/><%
		}else{
		%>楼主点评：无满意答案，此帖已结束。<br/><%
		}
	}
	%><a href="hpindex.jsp">返回有偿求助</a><br/><%
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}
%></wml>