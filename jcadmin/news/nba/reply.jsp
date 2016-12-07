<%@ page language="java" import="java.util.*,jc.news.nba.*,net.joycool.wap.bean.*,net.joycool.wap.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	int mid = action.getParameterInt("mid");
	List list = null;
	BeanMatch bm = null;
	if(mid > 0){
		action.updReply(mid,response);
		list = action.service.getReplyList(" del=0 and match_id="+ mid + " order by create_time desc");
		bm = action.getMatchById(mid);
	}	
	PagingBean paging = null;
%><html>
  <head>
    <title>言论管理</title>
    	<script type="text/javascript">
    		function ddd(){
	    		var cont = document.getElementsByName("cont");
	   			if(cont[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
	   				alert("内容不能为空!");	   				
	   				cont[0].focus();
	   				return false;
	    		}else if(cont[0].value.length > 255){
	    			alert("内容不能超过255个字");
	   				cont[0].focus();
	   				return false;
	    		}else {
	    			return true;
	    		}
    		}
    		
    	</script>
  </head>
  <body>
  言论管理:<%
  if(bm != null){
  	%><%=bm.getTeam1()%>VS<%=bm.getTeam2()%>共<%=bm.getSumReply()%>条评论<%
  } 
  %><br/>		
  <table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align="center" width="5%">
			<font color=#1A4578>UserId</font>
		</td>
		<td align="center" width="10%">
			<font color=#1A4578>UserName</font>
		</td>
		<td align=center>
			<font color=#1A4578>内容</font>
		</td>
		<td align="center" width="15%">
			<font color=#1A4578>发表时间</font>
		</td>
		<td align="center" width="10%">
			<font color=#1A4578>操作</font>
		</td>
	</tr><%
  	if(list != null && list.size() > 0){
		paging = new PagingBean(action, list.size(), 10, "p");
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			BeanReply br = (BeanReply)list.get(i);
	  		%><tr>
	  		<td align=center><%=br.getUid()%></td><%
	  		if(br.getUid() == 0){
	  		%><td align=center>直播间管理员:</td><%
	  		}else{
	  		%><td align=center><%=UserInfoUtil.getUser(br.getUid()).getNickNameWml()%></td><%
	  		}													
  			%><td align=left><%=StringUtil.toWml(br.getCont())%></td>
  			<td align=center><%=DateUtil.formatDate2(br.getCreateTime())%></td>
  			<td align=center><a href="reply.jsp?del=<%=br.getId()%>&mid=<%=mid%>" onclick="return confirm('确定删除？')">删除</a></td>
			</tr><%
  		}
  	}
  	%>	
  	</table><%
  	if(paging != null){
  	%><%=paging.shuzifenye("reply.jsp?jcfr=1&mid="+mid, true, "|", response)%><%
  	}
  	%>
  	<form action="reply.jsp" method="post" onsubmit="return ddd()">
  	<input type="hidden" name="uid" value="0"/>
  	<input type="hidden" name="mid" value="<%=mid%>"/>
  	<textarea rows="20" cols="70" name="cont"></textarea><br/>
  	<input type="submit" value="发言"/>
  	</form>
  	<a href="live.jsp">返回直播列表</a><br/>
  	<a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
