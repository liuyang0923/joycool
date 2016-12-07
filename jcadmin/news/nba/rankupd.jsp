<%@ page language="java" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>排行管理</title>
    <script type="text/javascript">
    	function ddd(){
    		var list = document.getElementsByName("loc");
    		for(var i = 0; i < list.length; i++){
    			if(list[i].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
    				alert("不能为空!");
    				list[i].focus();
    				return false;
    			}
    		}
    		return true;
    	}
    </script>
  </head>
  <body>
    <form action="rank.jsp" method="post" onsubmit="return ddd()"> 
  	大西洋赛区:<br/>
  	排名|球队|胜|负|胜率|胜场差<br/>
  	<textarea rows="5" cols="35" name="loc"></textarea><br/>
  	东南赛区:<br/>
  	排名|球队|胜|负|胜率|胜场差<br/>
  	<textarea rows="5" cols="35" name="loc"></textarea><br/>
  	中部赛区:<br/>
  	排名|球队|胜|负|胜率|胜场差<br/>
  	<textarea rows="5" cols="35" name="loc"></textarea><br/>
  	西南赛区:<br/>
  	排名|球队|胜|负|胜率|胜场差<br/>
  	<textarea rows="5" cols="35" name="loc"></textarea><br/>
  	西北赛区:<br/>
  	排名|球队|胜|负|胜率|胜场差<br/>
  	<textarea rows="5" cols="35" name="loc"></textarea><br/>
  	太平洋赛区:<br/>
  	排名|球队|胜|负|胜率|胜场差<br/>
  	<textarea rows="5" cols="35" name="loc"></textarea><br/>
  	东部赛区:<br/>
  	排名|球队|胜|负|胜率|胜场差<br/>
  	<textarea rows="15" cols="35" name="loc"></textarea><br/>
  	西部赛区:<br/>
  	排名|球队|胜|负|胜率|胜场差<br/>
  	<textarea rows="15" cols="35" name="loc"></textarea><br/>
  	<input type="reset" value="重置"/>
  	<input type="submit" value="提交"/>
  	</form>
  	<a href="rank.jsp">返回排名</a>
  	<a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
