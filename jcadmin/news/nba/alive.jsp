<%@ page language="java" import="java.util.*,jc.news.nba.*,net.joycool.wap.bean.*,net.joycool.wap.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	int mid = action.getParameterInt("mid");
	List list = null;
	if(mid > 0){
		action.updAlive(mid,response);
		list = action.service.getLiveList(" del=0 and match_id="+ mid + " order by create_time desc");
	}
	BeanMatch bm = action.getMatchById(mid);
	PagingBean paging = null;
	if(list != null)
		paging = new PagingBean(action, list.size(), 8, "p");
%><html>
  <head>
    <title>赛事直播间</title>
    	<script type="text/javascript">
    		function d(){
	    		var cont = document.getElementsByName("cont");
	    		if(cont[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
	   				alert("内容不能为空!");
	   				cont[0].focus();
	   				return false;
	    		}
	    		if(cont[0].value.length > 100){
	    			alert("内容不能超过100个字");
	   				cont[0].focus();
	   				return false;
	    		} 
	   			
    		}
    		function c(){
	    		var code = document.getElementsByName("code");
	    		var lt = document.getElementsByName("lt");
	   			if(code[0].value.replace(/(^\s*)|(\s*$)/g, "") == "" && lt[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
	   				alert("比分时间不能同时为空!");
	   				code[0].focus();
	   				return false;
	    		}else
	    			return true;    
    		}
    	</script>
  </head>
  <link href="../farm/common.css" rel="stylesheet" type="text/css">
  <body>赛事直播:<%
  if(bm != null){
  %>共<%=bm.getSumLive()%>条直播<%
  }%><br/>		
  <table border=1 width=100% align=center><%
  	if(bm != null){
	  	%><tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>队伍双方</font>
				</td>
				<td align="center" width="10%">
					<font color=#1A4578>比分</font>
				</td>
				<td align="center" width="10%">
					<font color=#1A4578>第几节</font>
				</td>
			</tr>
	  	<tr>
		<td align=center><%=bm.getTeam1()%>VS<%=bm.getTeam2()%></td>
	  	<td align=center><%=bm.getCode()%></td>
	  	<td align=center><%=bm.getPart()%></td>
	  	</tr>
	  	<tr>
	  	<td><hr/></td>
	  	<td><hr/></td>
	  	<td><hr/></td>
	  	</tr>
	  	<tr bgcolor=#C6EAF5>
			<td align=center>
				<font color=#1A4578>内容</font>
			</td>
				<td align="center" width="10%">
				<font color=#1A4578>添加时间</font>
			</td>
				<td align="center" width="10%">
				<font color=#1A4578>操作</font>
			</td>
		</tr><%
	  	if(list != null && list.size() > 0){
			for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			BeanLive bl = (BeanLive)list.get(i);
	  		%><tr>
	  		<td><%=StringUtil.toWml(bl.getCont())%></td>
	  		<td align=center><%=DateUtil.formatDate2(bl.getCreateTime())%></td>
	  		<td align=center><a href="alive.jsp?del=<%=bl.getId()%>&mid=<%=mid%>" onclick="return confirm('确定删除？')">删除</a></td>
			</tr><%
	  		}
	  	}
	  	%>	
	  	</table><%if(paging != null){
	  	%><%=paging.shuzifenye("alive.jsp?jcfr=1&mid="+mid, true, "|", response)%><%
	  	}
	  	%><form action="alive.jsp" method="post">
		  	小节:<br/>
		  	<select name="part"><%
				for(int i = 1; i < 5; i++){
					if(bm.getPart() == i){
					%><option value="<%=i%>" selected="selected">第<%=i%>节</option><%
					}else{
					%><option value="<%=i%>">第<%=i%>节</option><%
					}	
				}
				for(int i = 1; i < 7; i++){
					if(bm.getPart() == 4+i){
					%><option value="<%=4+i%>" selected="selected">加时赛<%=i%></option><%
					}else{
					%><option value="<%=4+i%>">加时赛<%=i%></option><%
					}	
				}  		
		  	 %></select>
		  	<input type="hidden" name="mid" value="<%=bm.getId()%>"/>
		  	<input type="submit" value="提交"/>
	  	</form>
	  	<hr>
	  	<form action="alive.jsp" method="post" onsubmit="return c()">
	  		<table>
	  			<tr>
	  				<td>比分:(XX-XXX)</td>
	  				<td>时间:(mm:ss)</td>
	  				<td><input type="hidden" name="mid" value="<%=bm.getId()%>"/></td>
	  			</tr>
	  			<tr>
	  				<td><input type="text" name="code" size="9"/></td>
	  				<td><input type="text" name="lt" size="9"/></td>
	  				<td><input type="submit" value="提交"/></td>
	  			</tr>
	  		</table>
	  	</form>
	  	<hr>
	  	<form action="alive.jsp" method="post" onsubmit="return d()">
		  	内容:<br/>
		  	<textarea rows="10" cols="70" name="cont"></textarea><br/>
		  	<input type="hidden" name="mid" value="<%=bm.getId()%>"/>
		  	<table>
	  			<tr>
	  				<td>比分:(XX-XXX)</td>
	  				<td>时间:(mm:ss)</td>
	  			</tr>
	  			<tr>
	  				<td><input type="text" name="code2" size="9"/></td>
	  				<td><input type="text" name="lt2" size="9"/></td>
	  			</tr>
		  	</table>
		  	<input type="submit" value="提交"/>
		  	<input type="reset" value="重置"/>
	  	</form><%
  	}else{
  		%>赛事id为空了!<%
  	}
  	%><a href="live.jsp">返回直播表</a><br/>
  	<a href="match.jsp">返回赛事列表</a><br/>
  	<a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
