<%@ page language="java" import="java.util.*,jc.news.nba.*,net.joycool.wap.bean.*,net.joycool.wap.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	BeanNews bn = action.getNewById();
%><html>
  <head>
    <title>新闻修改 </title>
    <script type="text/javascript">
    	function d(){
    		var name = document.getElementsByName("name");
    		var kind = document.getElementsByName("kind");
    		var cont = document.getElementsByName("cont");
    		var view = document.getElementsByName("view");
   			if(name[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
   				alert("标语不能为空!");
   				name[0].focus();
   				return false;
   			}
   			if(cont[0].value.replace(/(^\s*)|(\s*$)/g, "") == ""){
   				alert("内容不能为空!");
   				cont[0].focus();
   				return false;
   			}
   			if(view[0].value.length > 2){
   				alert("醒目字段不要超过2个字!");
   				return false;
   			}
    		for(var i = 0;i < kind.length;i++){
    			if(kind[i].checked){
    				return true;
    			}
    		}
    		alert("至少选择一个分类哦!");
    		return false;
    	}
    </script>
  </head>
  <body><%
  if(bn != null ){
  %><form action="news.jsp" method="post">
  	按照类型查询:<br/>
  	<select name="kk">
  	<option value="1">姚明—火箭</option>
  	<option value="2">易建联—篮网</option>
  	<option value="3">科比—湖人</option>
  	<option value="4">诸强动态</option>
  	<option value="5">火热战报</option>
  	</select><br/>
  	<input type="submit" value="查询"/>
  	</form>
  	<form action=news.jsp method="post" onsubmit="return d()"><%
  		if(bn.getFlag() == 5){
  			%>内容分类:(至少选一项)<br/>
		  	<input type="checkbox" name="kind" value="5" checked="checked" />
		  	火热战报<br/>
		  	<input type="checkbox" name="kind" value="1"/>
		  	姚明—火箭<br/>
		  	<input type="checkbox" name="kind" value="2"/>
		  	易建联—篮网<br/>
		  	<input type="checkbox" name="kind" value="3"/>
		  	科比—湖人<br/>
		  	<input type="checkbox" name="kind" value="4"/>
  			诸强动态<br/><%
  		}else if(bn.getFlag() == 1){
  			%>内容分类:(至少选一项)<br/>
		  	<input type="checkbox" name="kind" value="5"/>
		  	火热战报<br/>
		  	<input type="checkbox" name="kind" value="1" checked="checked" />
		  	姚明—火箭<br/>
		  	<input type="checkbox" name="kind" value="2"/>
		  	易建联—篮网<br/>
		  	<input type="checkbox" name="kind" value="3"/>
		  	科比—湖人<br/>
		  	<input type="checkbox" name="kind" value="4"/>
  			诸强动态<br/><%
  		}else if(bn.getFlag() == 2){
  			%>内容分类:(至少选一项)<br/>
		  	<input type="checkbox" name="kind" value="5"/>
		  	火热战报<br/>
		  	<input type="checkbox" name="kind" value="1"/>
		  	姚明—火箭<br/>
		  	<input type="checkbox" name="kind" value="2" checked="checked" />
		  	易建联—篮网<br/>
		  	<input type="checkbox" name="kind" value="3"/>
		  	科比—湖人<br/>
		  	<input type="checkbox" name="kind" value="4"/>
  			诸强动态<br/><%
  		}else if(bn.getFlag() == 3){
  			%>内容分类:(至少选一项)<br/>
		  	<input type="checkbox" name="kind" value="5"/>
		  	火热战报<br/>
		  	<input type="checkbox" name="kind" value="1"/>
		  	姚明—火箭<br/>
		  	<input type="checkbox" name="kind" value="2"/>
		  	易建联—篮网<br/>
		  	<input type="checkbox" name="kind" value="3" checked="checked" />
		  	科比—湖人<br/>
		  	<input type="checkbox" name="kind" value="4"/>
  			诸强动态<br/><%
  		}else{
  			%>内容分类:(至少选一项)<br/>
		  	<input type="checkbox" name="kind" value="5"/>
		  	火热战报<br/>
		  	<input type="checkbox" name="kind" value="1"/>
		  	姚明—火箭<br/>
		  	<input type="checkbox" name="kind" value="2"/>
		  	易建联—篮网<br/>
		  	<input type="checkbox" name="kind" value="3"/>
		  	科比—湖人<br/>
		  	<input type="checkbox" name="kind" value="4" checked="checked" />
  			诸强动态<br/><%
		}
		if(!bn.getView().equals("")){
		%>醒目字段:
  		<input type="text" name="view" size="9" maxlength="2" value="<%=bn.getView()%>"/>(允许为空)<br/><%
		}else{
		%>醒目字段:
  		<input type="text" name="view" size="9" maxlength="2"/>(允许为空)<br/><%
		}
  	 %>
  	新闻标语:
  	<input type="text" name="name" size="9" value="<%=bn.getName()%>"/>(不能为空)<br/>
  	内容:(不能为空)<br/>
  	<textarea rows="20" cols="70" name="cont"><%=bn.getCont()%></textarea><br/>
  	<input type="hidden" name="chgId" value="<%=bn.getId()%>"/>
  	<input type="reset" value="重置"/>
  	<input type="submit" value="修改"/>
  	</form><%
  }else{
  %>没有这条新闻!<%
  }
   %><a href="news.jsp">返回新闻列表</a><br/><a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
