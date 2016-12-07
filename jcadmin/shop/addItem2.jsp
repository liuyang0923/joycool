<%@ page language="java" import="java.io.*,org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.disk.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.cache.util.*,java.util.*,org.apache.commons.fileupload.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*"%><%@ page contentType="text/html;charset=utf-8"%>
<%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	ShopAction shopAction = new ShopAction(request);
	String a = request.getParameter("a");
	
	int t = shopAction.getParameterInt("type");
	if(a != null) {
		if(a.equals("hasAdd")) {
			int id = shopAction.getParameterInt("id");
			ItemBean itemBean = ShopAction.shopService.getShopItemByItemId(id);
			if(itemBean != null) {
				
				response.getWriter().write("<div style=\"margin-top:10px;color:red\">该物品已经添加</div>");
				response.getWriter().write("<div style=\"color:red\">剩余/库存：");
				response.getWriter().write(itemBean.getOdd()+"/"+itemBean.getMax()+"</div>");
				response.getWriter().write("<div style=\"color:red\">当前价格："+itemBean.getPrice()+"</div>");
			} else {
				DummyProductBean productBean = UserBagCacheUtil.getItem(id);
				response.getWriter().write("<div>该商品未添加</div>");
				response.getWriter().write("<div>商品详细信息:");
				response.getWriter().write(productBean.getDescription());
				response.getWriter().write("</div>");
			}
			return;
		}
	}
	

	///////////////////////////////////////////
	if(a == null) {
		System.out.println("aaabbbb");
	} else if(a.equals("a")) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
				
			List items = upload.parseRequest(request);
				
			Iterator it = items.iterator();
				
			HashMap params = new HashMap();
				
			while(it.hasNext()) {
				FileItem item = (FileItem) it.next();
					
				if(item.isFormField()) {
					params.put(item.getFieldName(), item.getString());
				} else {
					
					String name = item.getFieldName();
					String name2 = item.getName();
					String fileExt = StringUtil.convertNull(
								FileUtil.getFileExt(name2)).toLowerCase();
						
					String filePath = ShopAction.SHOP_PHOTO_PATH;// "E:\\eclipse\\workspace\\joycool-portal\\img/home";
					String fileName = FileUtil.getUniqueFileName() + "." + fileExt;
					
					if(name2 != null && name2.length() > 0) {
						int id = StringUtil.toInt((String)params.get("id"));
						
						if(id > 0) {
							//删除图片 start
							ItemBean itemBean = ShopAction.shopService.getShopItemById(id);
							if(itemBean.getPhotoUrl() != null) {
								String path = new String(ShopAction.SHOP_PHOTO_PATH + itemBean.getPhotoUrl());
								File file = new File(path);
								file.delete();
							}
							//删除图片 end
						}
						
						
						File fullFile=new File(fileName);
				        File savedFile=new File(filePath,fullFile.getName());
				        item.write(savedFile);
				        
				        params.put(name, fileName);
					}
			        
				}	
			}
			
			int id = StringUtil.toInt((String)params.get("id"));
			
			if(id > 0) {	
				int itemId = StringUtil.toInt((String)params.get("item_id"));//this.getParameterInt("item_id");
				String photoUrl = (String)params.get("photo");
				float price = StringUtil.toFloat((String)params.get("price"));//this.getParameterFloat("price");
				if(price < 1) {
					request.setAttribute("msg", "价格不能小于1,修改失败");
				}
				int max = StringUtil.toInt((String)params.get("max"));//this.getParameterInt("max");
				int odd = StringUtil.toInt((String)params.get("odd"));//this.getParameterInt("odd");
				
				if(max >= 0) {
					if(odd > max) {
						request.setAttribute("msg", "剩余不能大于库存,修改失败");
					}
				} else {
					odd = max;
				}
				
				
				int type = StringUtil.toInt((String)params.get("type"));//this.getParameterInt("type");
				String desc = new String(((String)params.get("desc")).getBytes("iso-8859-1"), "utf-8");//this.getParameterString("desc");
				int seq = StringUtil.toInt((String)params.get("seq"));
				String name = new String(((String)params.get("name")).getBytes("iso-8859-1"), "utf-8");
				int times = StringUtil.toInt((String)params.get("times"));
				int due = StringUtil.toInt((String)params.get("due"));
				
				ItemBean itemBean = new ItemBean();
				itemBean.setId(id);
				itemBean.setName(name);
				itemBean.setItemId(itemId);
				itemBean.setPrice(price);
				itemBean.setMax(max);
				itemBean.setOdd(odd);
				itemBean.setType(type);
				itemBean.setDesc(desc);
				itemBean.setSeq(seq);
				itemBean.setPhotoUrl(photoUrl==null?"":photoUrl);
				itemBean.setTimes(times);
				itemBean.setDue(due);
				
				ShopAction.shopService.updateItem(itemBean);
				
				request.setAttribute("msg", "修改成功");
			} else {
				int itemId = StringUtil.toInt((String)params.get("item_id"));//this.getParameterInt("item_id");
				System.out.println("aaa");
//				ItemBean item = ShopAction.shopService.getShopItemByItemId(itemId);
//				
//				if(item != null) {
//					request.setAttribute("msg", "添加失败，该物品已经添加");
//					return;
//				}
				float price = StringUtil.toFloat((String)params.get("price"));//this.getParameterFloat("price");
				if(price < 1) {
					request.setAttribute("msg", "价格不能小于1,增加失败");					
				}
				int max = StringUtil.toInt((String)params.get("max"));//this.getParameterInt("max");
				int odd = StringUtil.toInt((String)params.get("odd"));//this.getParameterInt("odd");
				
				if(max >= 0) {
					if(odd > max) {
						request.setAttribute("msg", "剩余不能大于库存,增加失败");
						return;
					}
				} else {
					odd = max;
				}
				
				int type = StringUtil.toInt((String)params.get("type"));//this.getParameterInt("type");
				String desc = new String(((String)params.get("desc")).getBytes("iso-8859-1"), "utf-8");//this.getParameterString("desc");
				String name = new String(((String)params.get("name")).getBytes("iso-8859-1"), "utf-8");
				int times = StringUtil.toInt((String)params.get("times"));
				int due = StringUtil.toInt((String)params.get("due"));
				System.out.println("aaa2");
				String photoUrl = (String)params.get("photo");
				ItemBean itemBean = new ItemBean();
				itemBean.setItemId(itemId);
				itemBean.setPrice(price);
				itemBean.setName(name);
				itemBean.setMax(max);
				itemBean.setOdd(odd);
				itemBean.setType(type);
				itemBean.setDesc(desc);
				itemBean.setPhotoUrl(photoUrl==null?"":photoUrl);
				itemBean.setTimes(times);
				itemBean.setDue(due);
				
				ShopAction.shopService.addItem(itemBean);
				
				request.setAttribute("msg", "增加成功");
				System.out.println("aaa3");
			}
		}/////////////////
	
	
	List list = ShopAction.shopService.getItemListByTypeAdmin(t);
	DummyServiceImpl dummyService = new DummyServiceImpl();

	Vector vector = dummyService.getDummyProductList("(dummy_id < 10 or dummy_id = 15)");
	
	
	
	PagingBean paging = new PagingBean(shopAction, list.size(), 10, "p");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加道具</title>
    <script type="text/javascript" src="ajax.js"></script>
    <script type="text/javascript" src="/jcadmin/js/JS_functions.js"></script>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<style>
	
	.td80{
	width:80px;text-align:center;
	}
	.td100{
	width:100px;text-align:center;
	}
	.td150{
	width:150px;text-align:center;
	}
	
	</style>
  </head>
  
  <body>
  <%
	String[] types = ShopUtil.getTypes();
	int count = types.length;
	for(int i = 0; i < count; i++) {
		if(types[i] != null && types[i].length() > 0) {
			if(t == i) {
%><%=i==0?"":"|"%><%=types[i] %><%} else {%><%=i==0?"":"|"%><a href="shop.jsp?type=<%=i%>"><%=types[i]%></a><%}}} %><br/>
  	<p style="color:red;"><%=request.getAttribute("msg") != null ? request.getAttribute("msg") :"" %></p>
  	<table>
  		<tr><td>商品id/物品id</td><td class="td100">商品名字</td><td class="td100">物品名字</td><td class="td80">价格</td><td class="td150">剩余/库存</td><td class="td100">被购买次数</td><td class="td80">是否推荐</td><td class="td80">序列</td><td class="td80">有效期</td><td class="td80">使用次数</td><td class="td80">图片</td><td class="td80">类别</td><td class="td150">操作</td></tr>
  		<%for(int i = paging.getStartIndex(); i < paging.getEndIndex(); i++) { 
  			int ii = ((Integer)list.get(i)).intValue();
  			ItemBean itemBean = ShopAction.shopService.getShopItemById(ii);
  			DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());
  		%>
  		<tr>
  		<td><%=itemBean.getId() %>/<%=itemBean.getItemId() %></td>
  		<td class="td100"><%=itemBean.getName() %></td>
  		<td class="td100"><%=productBean.getName()%></td>
  		<td class="td80"><%=itemBean.getPrice()+ShopUtil.GOLD_NAME_PATH %></td>
  		<td class="td150"><%=((itemBean.getOdd()==-1)?"无穷多":""+itemBean.getOdd())+"/"+((itemBean.getMax()==-1)?"无穷多":""+itemBean.getMax()) %></td>
  		<td class="td100"><%=itemBean.getCount() %></td>
  		<td class="td80"><%=itemBean.getSugguest() == 0 ?"否":"是" %>|<a href="shop.jsp?a=sug&id=<%=ii %>"><%=itemBean.getSugguest() == 0?"推荐":"不推荐" %></a></td>
  		<td class="td80"><%=itemBean.getSeq() %></td>
  		<td class="td80"><%=itemBean.getDue()/1440 %>/<%=productBean.getDue()/1440 %></td>
  		<td class="td80"><%=itemBean.getTimes() %>/<%=productBean.getTime() %></td>
  		<td class="td80"><img src="/joycool-rep/shop/<%=itemBean.getPhotoUrl() %>" alt="aa"/></td>
  		<td class="td150"><%=types[itemBean.getType()] %></td>
  		<td class="td150"><a href="shop.jsp?a=delete&id=<%=ii %>" onclick="if(!confirm('确定删除?')) {return false;}">删除</a>|<a href="shop.jsp?a=u&id=<%=ii %>#add">修改</a>|<a href="shop.jsp?a=d&id=<%=ii %>" onclick="if(!confirm('确定<%=itemBean.getHidden() == 0 ?"不隐藏":"隐藏" %>吗?')) {return false;}"><%=itemBean.getHidden() == 0 ?"不隐藏":"隐藏" %></a></td>
  		</tr>
  		<%} %>
  	</table>
  	<%=paging.shuzifenye("shop.jsp?type="+t, true, "|", response)%>
  	<a href="shop.jsp#add">增加新商品</a>|<a href="shop.jsp">刷新</a>|<a href="add.jsp">新增物品</a>|<a href="pay.jsp">支付网关</a>|<a href="types.jsp">类别管理</a>|<a href="shopads.jsp">广告语</a>|<a href="discount.jsp">打折管理</a>
  	<form action="record.jsp" method="get">
  		<input type="text" name="user_id"/>
  		<input type="submit" value="查询"/>
  	</form>
  	
  	<a name="add"></a>
  	<div id="add" style="display:block;">
  	<form action="addItem2.jsp?a=a" method="post" enctype="multipart/form-data">
  	<input type="hidden" name="id" value="<%=request.getParameter("id") %>"/>
  	选择物品:<select id="item_id" name="item_id" onchange="hasAdd(this.value)">
  		<option value="0" selected="selected">选择商品</option>
  		<%for(int i = 0;i < vector.size(); i++) {
  			DummyProductBean bean = (DummyProductBean)vector.get(i);%>
  		<option value="<%=bean.getId() %>"><%=bean.getName()%></option><%} %> 		
  	</select>
  	<span id="isadd"></span><br/>
  	名称:<input type="text" id="name" name="name" value="<%= (request.getAttribute("itemBean") == null)?"": ((ItemBean)request.getAttribute("itemBean")).getName()%>"/><br/>
  	物品价格:<input type="text" id="price"  name="price" value="<%= (request.getAttribute("itemBean") == null)?"": ((ItemBean)request.getAttribute("itemBean")).getPrice()%>"/><br/>
  	物品库存:<input type="text" id="max" name="max" value="<%= (request.getAttribute("itemBean") == null)?"-1": ((ItemBean)request.getAttribute("itemBean")).getMax()%>"/>(-1表示无穷多,0表示没有)<br/>
  	物品剩余:<input type="text" id="odd" name="odd" value="<%= (request.getAttribute("itemBean") == null)?"-1": ((ItemBean)request.getAttribute("itemBean")).getOdd()%>"/>(-1表示无穷多,0表示没有)<br/>
  	序列号:<input id="seq" name="seq" value="<%= (request.getAttribute("itemBean") == null)?"0": ((ItemBean)request.getAttribute("itemBean")).getSeq()%>"/><br/>
  	使用次数:<input type="text" id="odd" name="times" value="<%= (request.getAttribute("itemBean") == null)?"1": ((ItemBean)request.getAttribute("itemBean")).getTimes() %>"/><br/>
  	有效期:<input id="seq" name="due" value="<%= (request.getAttribute("itemBean") == null)?"0": ((ItemBean)request.getAttribute("itemBean")).getDue()%>"/>(分钟为单位[一天:<%=60*24 %>分钟,一月:<%=60*24*30 %>分钟])<br/>
  	所属类别:<select id="type" name="type">
  	<option value="0">请选择</option>
  	<%for(int i = 1; i < count; i ++) {%>
  		<%if(types[i] != null && types[i].length() > 0) {%>
  		<option value="<%=i%>"><%=types[i] %></option>
  		<%} %>
  	<%} %>
  	</select><br/>
  	描述:<textarea name="desc" style="width:200px;height:100px;"><%=(request.getAttribute("itemBean") == null)?"": ((ItemBean)request.getAttribute("itemBean")).getDesc() %></textarea><br/>
  	<%if(request.getAttribute("itemBean") != null) {
  		ItemBean itemBean = (ItemBean)request.getAttribute("itemBean");
  		if(itemBean.getPhotoUrl() != null && itemBean.getPhotoUrl().length() > 0) {
  	%>
  	<img src="<%="/joycool-rep/shop/" + itemBean.getPhotoUrl() %>" alt="aa"/><br/>
  	<%}} %>
  	图片:<input type="file" name="photo"/><br/>
  	
  	<input type="submit" value="<%=(request.getAttribute("itemBean") == null)?"增加": "修改"%>" onclick="if(!check()){ return false;}"/>
    </form>
    <%if(request.getAttribute("itemBean")!= null) {
  		ItemBean bean = (ItemBean)request.getAttribute("itemBean");
  	%>
  	<script>
  		selectOption(document.getElementById("item_id"), "<%=bean.getItemId() %>");
  		selectOption(document.getElementById("type"), "<%=bean.getType() %>");
  	</script>
  	<%} else{ %>
  	<script>
  		selectOption(document.getElementById("type"), "<%=shopAction.getParameterInt("type") %>");
  	</script>
  	<%} %>  	
    </div>
  </body>
  <script type="text/javascript">
    function check(){
    	var price = document.getElementById("price").value;
    	var item = document.getElementById("item_id").value;
    	var type = document.getElementById("type").value;
    	var max = document.getElementById("max").value;
    	var odd = document.getElementById("odd").value;
    	
    	if(price.length == 0) {
    		alert("请输入价格");
    		return false;
    	}
    	
    	if(max.length == 0){
    		alert("请输入库存");
    		return false;
    	}
    	
    	if(odd.length == 0) {
    		alert("请输入剩余数");
    		return false;
    	}
    	
    	if(max > -1) {
	    	if(odd > max){
	    		alert("剩余数不能大于库存");
	    		return false;
	    	}
    	}
    	
    	if(item == 0) {
    		alert("请选择物品");
    		return false;
    	}
    	
    	if(type == 0) {
    		alert("请选择类型");
    		return false;
    	}
    	
    	var max = document.getElementById("max").value;
    	var odd = document.getElementById("odd").value;
    	
    	return true;
    }
  
  
  	function showAdd(){
  		var item = document.getElementById("add");
  		item.style.display = (item.style.display == "block" ? "none" : "block");
  	}
  	
  	
  	 var handlesuccess = function(instance) {
  	 	document.getElementById("isadd").innerHTML = instance.responseText;
	 }
	 
	 var handleloading = function() {
	 }
	 
	 var handlefailure = function() {
	 }
	 
	  var callback = {
		success: handlesuccess,
	 	loading: handleloading,
	 	failure: handlefailure,
	 	param:{}
	 }
	 
	 function hasAdd(id) {
  		Ajax.request('GET','shop.jsp?a=hasAdd&id='+id, null, callback);
  	}
  </script>
</html>
