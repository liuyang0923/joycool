<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.util.*,jc.show.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><%
	CoolShowAction action = new CoolShowAction(request);
	String[] genders = {"女","男","通用"};
	int id = action.getParameterInt("id");
	Commodity good1 = null;
	if(id > 0){
		good1 = CoolShowAction.getCommodity(id);
	}
 %>
  <head>
    <title>物品<%if(good1 != null){%>修改<%}else{%>添加<%}%></title>
    <script type="text/javascript">
		function showPictures(obj) {
			document.getElementById("preview").innerHTML = "<img src='" + obj.value + "' alt='商品图片'>";
		}
		function showPictures2(obj) {
			document.getElementById("preview2").innerHTML = "<img src='" + obj.value + "' alt='物品图片(用于合成)'>";
		}
  	</script>  
    <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<form action="goods.jsp?a=a" method="post" enctype="multipart/form-data">
 		<%
  		if(good1 != null){
	%>
		<input type="hidden" name="id" value="<%=good1.getId()%>"/>
 		物品修改:<br/>
 		名称:<input type="text" name="name" value="<%=StringUtil.toWml(good1.getName())%>"/><br/>
 		价格:<input type="text" name="price" value="<%=good1.getPrice()%>"/>g<br/>
 		层次:<select name="type">
 		<%
 		List partList = CoolShowAction.getPartList();
 		for(int i=0;i<partList.size();i++){
 			PartBean part = (PartBean)partList.get(i);
 			if(part.getId() == good1.getType()){
 		%><option value="<%=part.getId()%>" selected><%=part.getName()%></option><%
 			}else{
 		%><option value="<%=part.getId()%>"><%=part.getName()%></option><%
 			}
 		}
 		%>
 		</select><br/>
		商品分类:<select name="catalog">
 		<%
 		List catalogList = CoolShowAction.getCatalogList();
 		for(int i=0;i<catalogList.size();i++){
 			CatalogBean catalog = (CatalogBean)catalogList.get(i);
 			if(catalog.getId() == good1.getCatalog()){
 		%><option value="<%=catalog.getId()%>" selected><%=catalog.getName()%></option><%
 			}else{
 		%><option value="<%=catalog.getId()%>"><%=catalog.getName()%></option><%
 			}
 		}
 		%>
 		</select><br/>
 		有效期:<input type="text" name="due" value="<%=good1.getDue()%>"/>天(0代表无限期)<br/>
  		性别:<select name="gend">
  		<%
 		for(int i=0;i<genders.length;i++){
 			if((i) == good1.getGender()){
 		%><option value="<%=i%>" selected="selected"><%=genders[i]%></option><%
 			}else{
 		%><option value="<%=i%>"><%=genders[i]%></option><%
 			}
 		}
 		%>
 		</select><br/>
 		
  		商品图片:<input type="file" name="bigimg" onchange="showPictures(this)"/><div id="preview"><img src="/rep/show/comm/<%=good1.getBigImg()%>" alt="商品图片"/></div><br/>
  		
  		物品图片:<input type="file" name="goodsimg" onchange="showPictures2(this)"/>(合成图片用)<div id="preview2"><img src="/rep/show/goods/<%=good1.getGoodsImg()%>" alt="物品图片(用于合成)"/></div><br/>
 		描述:<textarea rows="5" name="bak" cols="15"><%=good1.getBak()%></textarea><br/>
  	 	覆盖:<input type="text" name="partOther" value="<%=good1.getPartOther()%>"/><br/>
  	 	连接:<input type="text" name="next" value="<%=good1.getNext()%>"/><br/>
	<%
  		}else{
	%>
  		物品增加:<br/>
  		名称:<input type="text" name="name"/><br/>
  		价格:<input type="text" name="price" value="1"/>g<br/>
  		层次:<select name="type">
  		<%
 		List partList = CoolShowAction.getPartList();
 		for(int i=0;i<partList.size();i++){
 			PartBean part = (PartBean)partList.get(i);
  		%><option value="<%=part.getId()%>"><%=part.getName()%></option><%
  		}
  		%></select><br/>
  		商品分类:<select name="catalog">
  		<%
 		List catalogList = CoolShowAction.getCatalogList();
 		for(int i=0;i<catalogList.size();i++){
 			CatalogBean catalog = (CatalogBean)catalogList.get(i);
  		%><option value="<%=catalog.getId()%>"><%=catalog.getName()%></option><%
  		}
  		%></select><br/>
  		有效期:<input type="text" name="due" value="0"/>天(0代表无限期)<br/>
  		性别:<select name="gend">
  		<%
 		for(int i=0;i<genders.length;i++){
 		%><option value="<%=i%>"><%=genders[i]%></option><%
 		}
 		%>
 		</select><br/>
 		
   		商品图片:<input type="file" name="bigimg" onchange="showPictures(this)"/><div id="preview"></div><br/>
   		
  		物品图片:<input type="file" name="goodsimg" onchange="showPictures2(this)"/>(合成图片用)<div id="preview2"></div><br/>
  		描述:<textarea rows="5" name="bak" cols="15"></textarea><br/>
  	 	覆盖:<input type="text" name="partOther" value=""/><br/>
  	 	连接:<input type="text" name="next"/><br/>
	<%
  		}
  	 %>
  		<input type="submit" value="提交"/><input type="reset" value="重置"/>
  	</form>
 	<a href="goods.jsp">返回物品管理</a><br/>
 	<a href="comm.jsp">进入商品管理</a><br/>
 	<a href="index.jsp">返回酷秀首页</a>
  </body>
</html>
