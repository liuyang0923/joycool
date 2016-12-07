<%@ page language="java" import="net.joycool.wap.util.*,jc.credit.*,java.util.*,jc.search.*" pageEncoding="utf-8"%><%
	SearchAction action = new SearchAction(request);
	int pro = action.getParameterInt("province");
	int astro = action.getParameterInt("astro");
	int gender = action.getParameterInt("gender");
	int aim = action.getParameterInt("aim");
	String todo=action.getParameterString("todo");
	List proList = action.service.getProvinceList(" 1");
	List cityList = null;
	if(todo!=null && "sel".equals(todo))
		cityList = action.getProCity(pro);
	else
		action.updHot(response);
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>搜友管理</title>
    <script>
    function fuc_selcity(){
	    document.formSel.todo.value="sel";
	    document.formSel.submit();
    }
    </script>
  </head>
  <body>
  	<table width=100%>
  	<tr>
  	<td>
	  <table border=1 width=30% align=left>
		<tr bgcolor=#C6EAF5>
			<td align="center" width="10%">序号</td>
			<td align="center" >搜索条件</td>
			<td align="center" width="10%">操作</td>
	 		</tr><%
	 		if(SearchAction.hot.size() > 0){
	 			for(int i=0;i<SearchAction.hot.size();i++){
	 				BeanSearch bean = (BeanSearch)SearchAction.hot.get(i);
					String show = "";
					int city = bean.getCity();
					if(city > 0){
						CreditCity ccc = action.service.getCity(" id="+city);
						show = show+"/"+ccc.getCity();
					}
					show = action.getShow(bean,show);
	  			%><tr>
	  			<td align="center"><%=i+1%></td>
	  			<td align="left"><%=show%></td>
	  			<td align="center"><a href="search.jsp?del=<%=i+1%>">删除</a></td>
	  			</tr><%
	 			}
	 		}
	 		 %>
	  	</table>
	  </td>
	  </tr>
	  <tr>
	  <td>
	  	<table>
			添加搜索条件:
		  	<form name="formSel" action="search.jsp" method="post">
		  	<input type="hidden" name="todo" value="add">
			  	省份：<select id="province" name="province" onchange="fuc_selcity()"><option value="0">无</option><%
			  		if(proList != null && proList.size()>0){
				  		for(int i=0;i<proList.size();i++){
				  			CreditProvince beanPro = (CreditProvince)proList.get(i);
				  			if(beanPro.getId() == pro){
				  		%><option value="<%=beanPro.getId()%>" selected="selected"><%=beanPro.getProvince()%></option><%
				  			}else{
				  		%><option value="<%=beanPro.getId()%>"><%=beanPro.getProvince()%></option><%
				  			}
				  		}
			  		}
			  	 %>
			  	</select><br/>
				城市：<select name="city"><%
					if(cityList != null && cityList.size()>0){
						for(int i=0;i<cityList.size();i++){
							CreditCity beancity = (CreditCity)cityList.get(i);
							if(beancity != null){
							%><option value="<%=beancity.getId()%>"><%=StringUtil.toWml(beancity.getCity())%></option><%
							}
						}
					}else{
					%><option value="0">无</option><%
					}
				 %></select><br/> 
				星座：<select name="astro"><%
					for(int i=0;i<SearchAction.astro.length;i++){
						if(i == astro){
					%><option value="<%=i%>" selected="selected"><%=SearchAction.astro[i]%></option><%
						}else{
					%><option value="<%=i%>"><%=SearchAction.astro[i]%></option><%
						}
					}%></select><br/>
				性别：<select name="gender"><%
					for(int i=0;i<SearchAction.gender.length;i++){
						if(i == gender){
					%><option value="<%=i%>" selected="selected"><%=SearchAction.gender[i]%></option><%
						}else{
					%><option value="<%=i%>"><%=SearchAction.gender[i]%></option><%
						}
					}%></select><br/>
				交友目的：<select name="aim"><%
					for(int i=0;i<SearchAction.aim.length;i++){
						if(i == aim){
					%><option value="<%=i%>" selected="selected"><%=SearchAction.aim[i]%></option><%
						}else{
					%><option value="<%=i%>"><%=SearchAction.aim[i]%></option><%
						}
					}%></select><br/>
				<input type="submit" value="添加" onclick="return dd()"/>
		  	</form>
  		</table>
  	</td>
  	</tr>
  	</table>
  </body>
</html>
