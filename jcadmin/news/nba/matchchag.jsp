<%@ page language="java" import="jc.news.nba.*,net.joycool.wap.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	BeanMatch bm  = action.changeMatch();
	String kk = action.getParameterNoCtrl("kk");
%><html>
  <head>
    <title>赛事更改</title>
    <script type="text/javascript">
     	function d(){
    		var tm1 = document.getElementsByName("tm1");
    		var tm2 = document.getElementsByName("tm2");
    		var code = document.getElementsByName("code");
    		var sttm = document.getElementsByName("sttm");
   			if(tm1[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
   				alert("主队不能为空!");
   				tm1[0].focus();
   				return false;
   			}else if(tm2[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
   				alert("客队不能为空!");
   				tm2[0].focus();
   				return false;
   			}else if(code[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
   				alert("分数不能为空!");
   				code[0].focus();
   				return false;
   			}else if(sttm[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
   				alert("开赛时间不能为空!");
   				sttm[0].focus();
   				return false;
   			}else 
   				return true;
     	}
    </script>
  </head>
  <body><% 
  	if(bm != null){ 
  		%>
  		<form action="match.jsp" method="post" onsubmit="return d()"/>	
		  <table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>双方队伍</font> 
				</td>
				<td align=center>
					<font color=#1A4578>得分</font>
				</td>
				<td align=center>
					<font color=#1A4578>赛事时间</font>
				</td>
			</tr>
			<tr>
				<td align=center>
	  				<input type="text" name="tm1" value="<%=bm.getTeam1()%>"/>VS
	  				<input type="text" name="tm2" value="<%=bm.getTeam2()%>"/>
				</td>
				<td align=center>
	  				<input type="text" name="code" value="<%=bm.getCode()%>"/>
				</td>
				<td align=center>
	  				<input type="text" name="sttm" value="<%=DateUtil.formatDate2(bm.getStartTime())%>"/>
				</td>
				<td align=center>
	  				<input type="hidden" name="mid" value="<%=bm.getId()%>"/>
				</td>
			</tr>
			</table>
			状态:
	  		<select name="sta"><%
	  			if(bm.getStaticValue() == 1){
	  		 %><option value="0">未赛</option>
	  			<option value="1" selected="selected">已完赛</option><%
	  			}else{
	  		 %><option value="0" selected="selected">未赛</option>
	  			<option value="1">已完赛</option><%
	  			} 
	  		%></select><br/><%
	  			if(kk != null){
	  			%><input type="hidden" name="ld" value="<%=action.format(bm.getStartTime(),"yyyy-MM-dd")%>"/><%
	  			}
	  		 %><input type="submit" value="提交"/><%
	  			if(kk != null){
	  			%><a href="match.jsp?ld=<%=action.format(bm.getStartTime(),"yyyy-MM-dd")%>">取消</a><%
	  			}else{
	  			%><a href="match.jsp">取消</a><%
	  			}
	  		 %>
  		</form>
  		<%
  	}
   %><a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
