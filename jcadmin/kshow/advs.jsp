<%@ page language="java" pageEncoding="utf-8" import="jc.show.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html>
<%
	CoolShowAction action = new CoolShowAction(request);
	int delall = action.getParameterInt("delall");
	int del = action.getParameterInt("del");
	int commid = action.getParameterInt("commid");
	int delback = 0;
	if(delall == 1){
		CoolShowAction.service.upd("delete from show_adv");
		request.setAttribute("tip","数据已清空!");
	}
	if(del > 0){
		delback = action.delAdv(del);
		if(1 == delback){
			request.setAttribute("tip","删除成功!");
		}else if(2 == delback){
			request.setAttribute("tip","所删除信息不存在!");
		}
	}
	if(commid > 0){
		action.addAdv(response,commid,action.getParameterInt("plc"));
	}
	if("true".equals(action.getParameterString("add"))){
		request.setAttribute("tip","推荐成功!");
	}else if("false".equals(action.getParameterString("add"))){
		request.setAttribute("tip","推荐物品不存在!");
	}
	List list = CoolShowAction.service.getAdv(" 1");
 %>
  <head>
    <title>广告推荐管理</title>
    <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
  <table width="100%">
  	<tr>
  	<td>推荐id</td>
  	<td>商品id</td>
  	<td>推荐地点</td>
  	<td>操作</td>
  	</tr>
  <%
  if(list != null && list.size() > 0){
  	for(int i = 0;i< list.size();i++){
  		BeanAdv bean = (BeanAdv)list.get(i);
  		%>
  	<tr>
  	<td><%=i+1%></td>
  	<td><%=bean.getCommid()%></td>
  	<td><%=CoolShowAction.place[bean.getPlace()]%></td>
  	<td><a href="adv.jsp?commid=<%=bean.getCommid()%>">查看</a>|<a href="advs.jsp?del=<%=bean.getId()%>" onclick="return confirm('确认删除?')">删除</a></td>
  	</tr>
  	<%
  	}
  }else{
  %>
  	<tr>
  	<td colspan="4">暂无信息</td>
  	</tr>
  	<%
  }
   %>
   </table>
   <br/>
   添加推荐:
   <form action="advs.jsp" method="post">
   	<table>
   		<tr>
   			<td>商品id</td><td><input type="text" name="commid" /></td>
   		</tr>
   		<tr>
   			<td>推荐地点</td>
   			<td>
   				<select name="plc">
   					<%
   						for(int i=0;i<CoolShowAction.place.length;i++){
   							%><option value="<%=i%>"><%=CoolShowAction.place[i]%></option><%
   						}
   					 %>
   				</select>
   			</td>
   		</tr>
   		<tr>
   			<td><input type="submit" value="添加"/></td>
   		</tr>
   	</table>
   	
   </form>
  	<a href="advs.jsp?delall=1" onclick="return confirm('确认清空?')">数据清空</a><br/>
  	<a href="goods.jsp">商品管理</a><br/>
 	<a href="index.jsp">返回酷秀首页</a>
  </body>
</html>
