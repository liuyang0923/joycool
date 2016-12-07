<%@ page language="java" import="jc.news.nba.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>添加赛事</title>
    <script type="text/javascript">
    	function ddd(){
    		var tms1 = document.getElementsByName("tm1");
    		var tms2 = document.getElementsByName("tm2");
    		var st = document.getElementsByName("sttm");
   			if(tms1[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""||tms2[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
   				alert("双方队伍不能为空!");
   				tms1[0].focus();
   				return false;
    		}
   			if(st[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
   				alert("比赛时间不能为空!");
   				st[0].focus();
   				return false;
    		}
 	  		return true;
    	}
    </script>
  </head>
  <body>
  <form action="match.jsp" method="post" onsubmit="return ddd()">
		双方队伍:<br/>
		<input type="text" name="tm1"/>VS<input type="text" name="tm2"/><br/>
		比赛时间:(YY-MM-DD/HH:MM:SS)<br/>
		<input type="text" name="sttm"/><br/>
		<input type="submit" value="提交" />
  		<a href="match.jsp">取消</a>
  	</form> 
  	<a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
