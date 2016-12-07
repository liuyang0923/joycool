<%@ page language="java" import="java.util.*,jc.news.nba.*,net.joycool.wap.bean.*,net.joycool.wap.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	action.updNews(response);
	List list = null;
	int kk = action.getParameterInt("kk");
	if(kk > 0)
		list = action.service.getNews(" flag="+ kk + " order by create_time desc");
	else
		list = action.service.getNews(" 1 order by create_time desc");	
	PagingBean paging = null;
	String[] kind = {"暂无分类","姚明—火箭","易建联—篮网","科比—湖人","诸强动态","火热战报"};
%><html>
  <head>
    <title>新闻管理</title>
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
  <body>		
  <table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align="center" width="6%">
			<font color=#1A4578>醒目字段</font>
		</td>
		<td align="center" width="16%">
			<font color=#1A4578>新闻标语</font>
		</td>
		<td align="center">
			<font color=#1A4578>新闻内容</font>
		</td>
		<td align="center" width="10%">
			<font color=#1A4578>内容分类</font>
		</td>
		<td align="center" width="10%">
			<font color=#1A4578>添加时间</font>
		</td>
		<td align="center" width="9%">
			<font color=#1A4578>操作</font>
		</td>
	</tr>
  	<%
  	if(list != null && list.size() > 0){
		paging = new PagingBean(action, list.size(), 20, "p");
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
		BeanNews bn = (BeanNews)list.get(i);
  		%><tr><%if(bn.getView().equals("")){
  		%><td align=center>无</td><%
  		}else{
  		%><td align=center>[<%=bn.getView()%>]</td><%
  		}
  		%>
  		<td align=center><a href="newsdetail.jsp?nid=<%=bn.getId()%>&p=<%=action.getParameterInt("p") %>"><%=bn.getName()%></a></td>
  		<td><%=StringUtil.limitString(StringUtil.toWmlIgnoreAnd(bn.getCont()),222)%></td>
  		<td align=center><%=kind[bn.getFlag()]%></td>
		<td align=center><%=DateUtil.formatDate2(bn.getCreateTime())%></td>
		<td align=center><a href="newschag.jsp?nid=<%=bn.getId()%>">修改</a>&#160;<a href="news.jsp?del=<%=bn.getId()%>" onclick="return confirm('确定删除？')">删除</a></td>
		</tr><%
  		}
  	}
  	%>	
  	</table><%if(paging != null){
  	%><%=paging.shuzifenye("news.jsp?jcfr=1", true, "|", response)%><%
  	}
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
  	<form action=news.jsp method="post" onsubmit="return d()">
  	内容分类:(至少选一项)<br/>
  	<input type="checkbox" name="kind" value="5"/>
  	火热战报<br/>
  	<input type="checkbox" name="kind" value="1"/>
  	姚明—火箭<br/>
  	<input type="checkbox" name="kind" value="2"/>
  	易建联—篮网<br/>
  	<input type="checkbox" name="kind" value="3"/>
  	科比—湖人<br/>
  	<input type="checkbox" name="kind" value="4"/>
  	诸强动态<br/>
  	醒目字段:
  	<input type="text" name="view" size="21" maxlength="2"/>(允许为空)<br/>
  	新闻标语:
  	<input type="text" name="name" size="21"/>(不能为空)<br/>
  	内容:(不能为空)<br/>
  	<textarea rows="20" cols="70" name="cont"></textarea><br/>
  	<input type="reset" value="重置"/>
  	<input type="submit" value="添加"/>
  	</form>
  	<a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
