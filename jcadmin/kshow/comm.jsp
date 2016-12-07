<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.util.*,com.jspsmart.upload.*,net.joycool.wap.bean.*,jc.show.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><%
	CoolShowAction action = new CoolShowAction(request);
	String[] gender = {"女","男","通用"};
	String[] hidden = {"隐藏","显示"};
	if(action.getParameterString("add")!=null){
		request.setAttribute("tip","添加成功!");
	}
	int hid = action.getParameterInt("chhid");
	if(hid > 0){
		Commodity delcom = CoolShowAction.getCommodity(hid);
		if(delcom != null){
			if(delcom.getDel() == 0){
				delcom.setDel(1);
			}else if(delcom.getDel() == 1){
				delcom.setDel(0);
			}
			CoolShowAction.service.upd("update show_commodity set del="+delcom.getDel()+" where id="+hid);
			request.setAttribute("tip","成功"+hidden[delcom.getDel()]+"商品"+StringUtil.toWml(delcom.getName())+"!");
		}else{
			request.setAttribute("tip","没有预计"+hidden[delcom.getDel()]+"的商品!");
		}
	}
	String a = action.getParameterString("a");
	if("a".equals(a)){
		SmartUpload smUpload = new SmartUpload();
		smUpload.initialize(pageContext);
		smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
		smUpload.setMaxFileSize(50*1024);
		smUpload.upload();
		File upfile = smUpload.getFiles().getFile(0);
		String name = smUpload.getRequest().getParameter("name");
		if(name != null && !"".equals(name)){
			Commodity com = new Commodity();
			String strdue = smUpload.getRequest().getParameter("due").trim();
			String strtype = smUpload.getRequest().getParameter("type").trim();
			String strgend = smUpload.getRequest().getParameter("gend").trim();
			String bak = smUpload.getRequest().getParameter("bak").trim();
			String id = smUpload.getRequest().getParameter("id");
			String strprice = smUpload.getRequest().getParameter("price").trim();
			String iid = smUpload.getRequest().getParameter("iid");
			int next = StringUtil.toInt(smUpload.getRequest().getParameter("next"));
			String partOther = StringUtil.noEnter(smUpload.getRequest().getParameter("partOther"));
			int due = 0;
			int type = 1;
			int gend = 0;
			float price = 0;
			if(strdue.length()>0 && strdue.length() < 11){
				due = Integer.parseInt(strdue);
			}
			if(strdue.length()>0 && strdue.length() < 11){
				type = Integer.parseInt(strtype);
			}
			if(strgend.length()>0 && strgend.length() < 11){
				gend = Integer.parseInt(strgend);
			}
			if(strprice.length()>0 && strprice.length() < 11){
				price = Float.parseFloat(strprice);
			}
			if(id!=null && id.length() > 0 && id.length() < 11){
				com.setId(Integer.parseInt(id.trim()));
			}
			if(iid!=null && iid.length() > 0 && iid.length() < 11){
				com.setIid(Integer.parseInt(iid.trim()));
			}
			com.setPartOther(partOther);
			com.setNext(next);
			com.setName(name.trim());
			com.setType(type);
			com.setDue(due);
			com.setGender(gend);
			com.setPrice(price);
			com.setBak(bak);
			Commodity com2 = null;
			if(com.getId() > 0){
				com2 = CoolShowAction.getCommodity(com.getId());
			}
			if(upfile != null && upfile.getFileExt() != null && upfile.getFileExt().length() > 0){
				String bigimg = System.currentTimeMillis()+"."+upfile.getFileExt();
				com.setBigImg(bigimg);
				upfile.saveAs(CoolShowAction.ALL_URL+CoolShowAction.IMAGE_URL[2]+ bigimg,SmartUpload.SAVE_PHYSICAL);
				if(com2 != null)
					CoolShowAction.delFile(com2.getBigImg(),2);
			}else{
				com.setBigImg("");
			}
			if(com2 != null){
				CoolShowAction.service.alterComm(com,com2);
				CoolShowAction.flushCommodityCache(com.getId());
				request.setAttribute("tip","修改成功!");
				response.sendRedirect("goods.jsp");
				return;
			}else{
				com.setId(com.getIid());
				CoolShowAction.service.insertComm(com);
				response.sendRedirect("goods.jsp?add=true");
				return;
			}
		}else{
			request.setAttribute("tip","数据不足,添加失败!");
		}
	}
	int p = action.getParameterInt("p");
	int count = SqlUtil.getIntResult("select count(id) from show_commodity",5);
	PagingBean paging = new PagingBean(action,count,30,"p");
	List list = CoolShowAction.service.getCommodityList("1 order by id desc limit "+p*30+",30");
 %>
  <head>
    <title>商品管理</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
  	商品管理: - <a href="altercomm.jsp">添加商品</a><br/>
  	<table border="1" width="100%">
  	<tr><td>商品ID/物品ID</td><td>商品名字</td><td>类别</td><td>性别</td><td>价格</td><td>图片</td><td>购买次数</td><td>有效天数</td><td>描述</td><td>操作</td><td>推荐</td></tr>
  	<%
  		if(list != null && list.size() > 0){
  			for(int i=0;i<list.size();i++){
	  			Commodity com = (Commodity)list.get(i);
	  			%>
	  			<tr>
	  			<td><%=com.getId()%>/<%=com.getIid()%></td>
	  			<td><%=com.getName()%></td>
	  			<td><%if(com.getType()>0&&com.getType() <= CoolShowAction.part.length){%><%=CoolShowAction.part[com.getType()-1]%><%}%></td>
	  			<td><%if(com.getGender()>=0&&com.getGender() < gender.length){%><%=gender[com.getGender()]%><%}%></td>
	  			<td><%=com.getPrice()%>g</td>
	  			<td><img alt="x" src="/rep/show/comm/<%=com.getBigImg()%>"/></td>
	  			<td><%=com.getCount()%></td>
	  			<td><%=com.getDue()%></td>
	  			<td><%=com.getBak()%></td>
	  			<td><a href="altercomm.jsp?id=<%=com.getId()%>">修改</a>|<a href="comm.jsp?chhid=<%=com.getId()%>"><%if(com.getDel() == 0||com.getDel()==1){%><%=hidden[com.getDel()]%><%}%></a></td>
	  			<td>
		  			<form action="advs.jsp" method="post">
		  				<input type="hidden" name="commid" value="<%=com.getId()%>"/>
		  				<select name="plc">
		  					<%
		  						for(int j=0;j<CoolShowAction.place.length;j++){
		  							%><option value="<%=j%>"><%=CoolShowAction.place[j]%></option><%
		  						}
		  					 %>
		  				</select>
		  				<input type="submit" value="确定"/>
		  			</form>
	  			</td>
	  			</tr>
	  			<%
  			}
  			%><%=paging.shuzifenye("comm.jsp?jcfr=1",true,"|",response)%><%
  		}
  	 %>
  	</table>
  	<a href="altercomm.jsp">添加商品</a><br/>
  	<a href="goods.jsp">物品管理</a><br/>
  	<a href="advs.jsp">商品推荐</a><br/>
 	<a href="index.jsp">返回酷秀首页</a>
  </body>
</html>
