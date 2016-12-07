<%@ page language="java" import="java.util.*,jc.news.nba.*,net.joycool.wap.util.*,net.joycool.wap.bean.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	action.updateMatch();
	int show = action.getShow();
	int y = action.getParameterInt("y");
	int m = action.getParameterInt("m");
	int d = action.getParameterInt("d");
	String[] sta = {"未赛","已完赛","直播中"};
	String[] ss = {"","年","月","日",""};
	List list = null;
	PagingBean paging = null;
	if(show < 0)
		list = action.getMLByDate();
	else
		list = action.getAllMch();	
	if(list != null)
		paging = new PagingBean(action, list.size(), 20, "p");
%><html>
  <head>
    <title>赛事管理</title>
    	<script type="text/javascript">
    		function dd(){
	    		var y = document.getElementsByName("y");
	    		var m = document.getElementsByName("m");
	    		var d = document.getElementsByName("d");
	   			if(y[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
	   				alert("年份不能为空!");
	   				y[0].focus();
	   				return false;
	    		} else if(m[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
	   				alert("月份不能为空!");
	   				m[0].focus();
	   				return false;
	    		} else if(d[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
	   				alert("日期不能为空!");
	   				d[0].focus();
	   				return false;
	    		} else
	    			return true;  
    		}
    	</script>
  </head>
  <body>		
  <table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>
			<font color=#1A4578>id号</font>
		</td>
		<td align=center>
			<font color=#1A4578>双方队伍</font>
		</td>
		<td align=center>
			<font color=#1A4578>比赛时间</font>
		</td>
		<td align=center>
			<font color=#1A4578>状态</font>
		</td>
		<td align=center>
			<font color=#1A4578>比分</font>
		</td>
		<td align=center>
			<font color=#1A4578>添加时间</font>
		</td>
		<td align=center>
			<font color=#1A4578>操作</font>
		</td>
	</tr><%  	
	  	if(list != null && list.size() > 0){
			for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
				BeanMatch bm = (BeanMatch)list.get(i);
		  		%><tr>
		  		<td align=center><%=bm.getId()%></td>
		  		<td align=center><%=bm.getTeam1()%>VS<%=bm.getTeam2()%></td>
		  		<td align=center><%=DateUtil.formatDate2(bm.getStartTime())%></td>
		  		<td align=center><%=sta[bm.getStaticValue()]%></td>
		  		<td align=center><%=bm.getCode()%></td>
		  		<td align=center><%=DateUtil.formatDate2(bm.getCreateTime())%></td><%
		  		if(bm.getStaticValue() == 2){
		  		%><td align=center><a href="alive.jsp?mid=<%=bm.getId()%>">进入直播</a></td><%
		  		}else{
		  		%><td align=center><%
				if(show < 0){
				%><a href="matchchag.jsp?chgemid=<%=bm.getId()%>&amp;kk=y">修改</a><%
				}else{
				%><a href="matchchag.jsp?chgemid=<%=bm.getId()%>">修改</a><%
				}
		  		 %>|<a href="match.jsp?del=<%=bm.getId()%>" onclick="return confirm('确定删除？')">删除</a><%
		  			if(bm.getStaticValue() != 1){
		  				if(y>0 && m>0 && d>0){
		  		 %>|<a href="match.jsp?updId=<%=bm.getId()%>&y=<%=y%>&m=<%=m%>&d=<%=d%>" onclick="return confirm('确定直播？')">开始直播</a><%
		  				}else{
		  		 %>|<a href="match.jsp?updId=<%=bm.getId()%>" onclick="return confirm('确定直播？')">开始直播</a><%
		  				}
		  		 	}
		  		%></td><%
		  		}
		  		%></tr><%
			}
	  	}else{
		  	%><tr>
		  	<td>暂无赛事</td>
		  	</tr><%
	  	}
	%></table><%=paging.shuzifenye("match.jsp?jcfr=1", true, "|", response)%><br/><%
if(show > 0){
%>请正确输入<%=ss[show]%>!<br/><%
}%><form action="match.jsp" method="post" onsubmit="return dd()">
		年:<input type="text" name="y" size="5"/>
		月:<input type="text" name="m" size="5"/>
		日:<input type="text" name="d" size="5"/>
		<input type="submit" value="搜索"/>
	</form>
	<a href="updmatch.jsp">添加赛事</a><br/>
  	<a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
