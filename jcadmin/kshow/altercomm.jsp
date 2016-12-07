<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.util.*,jc.show.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><%
	CoolShowAction action = new CoolShowAction(request);
	String[] genders = {"女","男","通用"};
	int id = action.getParameterInt("id");
	int type = action.getParameterInt("type");
	Commodity com1 = null;
	List glist = null;
	if(id > 0){
		com1 = CoolShowAction.getCommodity(id);
		if(com1 != null){
			type = com1.getType();
			glist = action.getGoodsList(type);
		}else{
			request.setAttribute("tip","没有这个商品!");
		}
	}
	String todo = action.getParameterString("todo");
	if(todo!=null && "sel".equals(todo))
		glist = action.getGoodsList(type);
	if(type <= 0){
		type = 1;
		glist = action.getGoodsList(type);
	}
	
	Goods selGood = null;
	int iid = action.getParameterInt("iid");
	if(iid>0){
		selGood = CoolShowAction.getGoods(iid);
		type = selGood.getType();
	}
 %>
  <head>
    <title>商品<%if(com1 != null){%>修改<%}else{%>添加<%}%></title>
    <script type="text/javascript">
		function showPictures(obj) {
			if(obj.name=='bigimg'){
			//alert('file2');
			  document.getElementById("preview").innerHTML = "<img src='" + obj.value + "'>";
			}
		}
	    function selgoods(){
		    document.formSel.todo.value="sel";
		    document.formSel.submit();
	    }
  	</script> 
    <link href="../farm/common.css" rel="stylesheet" type="text/css"> 
  </head>
  <body>
 	商品添加:<br/>
  	<form name="formSel" action="altercomm.jsp" method="get">
		<input type="hidden" name="todo" value="add">
 		类型:<select name="type" onchange="selgoods()">
 		<%
 		List partList = CoolShowAction.getPartList();
 		for(int i=0;i<partList.size();i++){
 			PartBean part = (PartBean)partList.get(i);
 			if(part.getId() == type){
 		%><option value="<%=part.getId()%>" selected="selected"><%=part.getName()%></option><%
 			}else{
 		%><option value="<%=part.getId()%>"><%=part.getName()%></option><%
 			}
 		}
 		%>
 		</select><br/>
  	</form>
  	<form action="comm.jsp?a=a" method="post" enctype="multipart/form-data">
 		<%
  		if(com1 != null){
	%>
		<input type="hidden" name="id" value="<%=com1.getId()%>"/>
		<input type="hidden" name="type" value="<%=type>0?type:0%>"/>
 		名称:<input type="text" name="name" value="<%=StringUtil.toWml(com1.getName())%>"/><br/>
 		有效期:<input type="text" name="due" value="<%=com1.getDue()%>"/>天(0代表无限期)<br/>
  		价格:<input type="text" name="price" value="<%=com1.getPrice()%>"/>g<br/>
  		选择物品:<select name="iid">
  		<%
  		if(glist != null && glist.size() > 0){
  		for(int i=0;i<glist.size();i++){
  			Goods good = (Goods)glist.get(i);
  			if(good.getId() == com1.getIid()){
  		%><option value="<%=good.getId()%>" selected="selected"><%=good.getName()%></option><%
  			}else{
  		%><option value="<%=good.getId()%>"><%=good.getName()%></option><%
  			}
  		}
  		}
  		%></select><br/>
  		性别:<select name="gend">
  		<%
 		for(int i=0;i<genders.length;i++){
 			if((i) == com1.getGender()){
 		%><option value="<%=i%>" selected="selected"><%=genders[i]%></option><%
 			}else{
 		%><option value="<%=i%>"><%=genders[i]%></option><%
 			}
 		}
 		%>
 		</select><br/>
 		<div id="preview"><img src="/rep/show/comm/<%=com1.getBigImg()%>" alt="dd"/></div>
  		图片:<input type="file" name="bigimg" onchange="showPictures(this)"/>(商城给用户显示用)<br/>
 		描述:<textarea rows="5" name="bak" cols="15"><%=com1.getBak()%></textarea><br/>
  	 	覆盖:<input type="text" name="partOther" value="<%=com1.getPartOther()%>"/><br/>
  	 	连接:<input type="text" name="next" value="<%=com1.getNext()%>"/><br/>
	<%
  		}else{
	%>
		<input type="hidden" name="type" value="<%=type>0?type:0%>"/>
	<%if(selGood==null){	// 如果选择了物品来添加商品，不用再选择
		%>选择物品:<select name="iid">
  		<%
  		if(glist != null && glist.size() > 0){
  		for(int i=0;i<glist.size();i++){
  			Goods good = (Goods)glist.get(i);
  		%><option value="<%=good.getId()%>"><%=good.getName()%></option><%
  		}
  		}
  		%></select><br/>
  	<%}else{%>
  		<input type=hidden name="iid" value="<%=selGood.getId()%>">
  	<%}%>
  		性别:<select name="gend">
  		<%
 		for(int i=0;i<genders.length;i++){
 		%><option value="<%=i%>" <%if(selGood!=null&&selGood.getFlag()==i){%>selected<%}%>><%=genders[i]%></option><%
 		}
 		%>
 		</select><br/>
  		名称:<input type="text" name="name"<%if(selGood!=null){%> value="<%=selGood.getName()%>"<%}%>/><br/>
  		价格:<input type="text" name="price" value="0"/>g<br/>
  		有效期:<input type="text" name="due" value="0"/>天(0代表无限期)<br/>
 		<div id="preview"></div>
  		图片:<input type="file" name="bigimg" onchange="showPictures(this)"/>(商城给用户显示用)<br/>
  		描述:<textarea rows="5" name="bak" cols="15"></textarea><br/>
	<%
  		}
  	 %>
  	 	覆盖:<input type="text" name="partOther" value=""/><br/>
  	 	连接:<input type="text" name="next"/><br/>
  		<input type="submit" value="提交"/>
  	</form>
 	<a href="comm.jsp">返回商品管理</a><br/>
 	<a href="goods.jsp">进入物品管理</a><br/>
 	<a href="index.jsp">返回酷秀首页</a>
  </body>
</html>
