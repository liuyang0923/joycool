<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,com.jspsmart.upload.*,net.joycool.wap.bean.PagingBean,jc.show.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><%
	CoolShowAction action = new CoolShowAction(request);
	String[] gender = {"女","男","通用"};
	String[] hidden = {"隐藏","显示"};
	if(action.getParameterString("add")!=null){
		request.setAttribute("tip","添加成功!");
	}
	String a = action.getParameterString("a");
	int hid = action.getParameterInt("chhid");	// 隐藏和显示
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
	if("a".equals(a)){
		SmartUpload smUpload = new SmartUpload();
		smUpload.initialize(pageContext);
		smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
		smUpload.setMaxFileSize(50*1024);
		smUpload.upload();
		File upfile = smUpload.getFiles().getFile(0);
		File upfile2 = smUpload.getFiles().getFile(1);
		
		String name = smUpload.getRequest().getParameter("name");
		if(name != null && !"".equals(name)){
			Commodity good = new Commodity();
			String strdue = smUpload.getRequest().getParameter("due").trim();
			String strtype = smUpload.getRequest().getParameter("type").trim();
			String strgend = smUpload.getRequest().getParameter("gend").trim();
			String bak = smUpload.getRequest().getParameter("bak").trim();
			String id = smUpload.getRequest().getParameter("id");
			int catalog = StringUtil.toInt(smUpload.getRequest().getParameter("catalog"));
			float price = StringUtil.toFloat(smUpload.getRequest().getParameter("price"));
			int next = StringUtil.toInt(smUpload.getRequest().getParameter("next"));
			String partOther = StringUtil.noEnter(smUpload.getRequest().getParameter("partOther"));
			int due = 0;
			int type = 1;
			int gend = 0;
			if(strdue.length()>0 && strdue.length() < 11){
				due = Integer.parseInt(strdue);
			}
			if(strdue.length()>0 && strdue.length() < 11){
				type = Integer.parseInt(strtype);
			}
			if(strgend.length()>0 && strgend.length() < 11){
				gend = Integer.parseInt(strgend);
			}
			if(id!=null && id.length() > 0 && id.length() < 11){
				good.setId(Integer.parseInt(id.trim()));
			}	
			good.setPartOther(partOther);
			good.setNext(next);
			good.setName(name.trim());
			good.setType(type);
			good.setDue(due);
			good.setCatalog(catalog);
			good.setGender(gend);
			good.setBak(bak);
			good.setPrice(price);
			Commodity good2 = null;
			if(good.getId() > 0){
				good2 = CoolShowAction.getCommodity(good.getId());
			}
			if(upfile != null &&!upfile.isMissing()){
				String bigimg = System.currentTimeMillis()+"."+upfile.getFileExt();
				good.setBigImg(bigimg);
				upfile.saveAs(CoolShowAction.IMAGE_URL_COMMODITY+ bigimg,SmartUpload.SAVE_PHYSICAL);
				if(good2 != null)
					CoolShowAction.delFile(good2.getBigImg(),1);
			}

			if(upfile2 != null &&!upfile2.isMissing()){
				String bigimg = System.currentTimeMillis()+"."+upfile2.getFileExt();
				good.setGoodsImg(bigimg);
				upfile2.saveAs(CoolShowAction.IMAGE_URL_GOODS+ bigimg,SmartUpload.SAVE_PHYSICAL);
				if(good2 != null)
					CoolShowAction.delFile(good2.getGoodsImg(),2);
			}

			if(good2 != null){
				CoolShowAction.service.alterComm(good,good2);
				CoolShowAction.flushCommodityCache(good.getId());
				request.setAttribute("tip","修改成功!");
			}else{
				CoolShowAction.service.insertComm(good);
				response.sendRedirect("goods.jsp?add=true");
				return;
			}
		}else{
			request.setAttribute("tip","数据不足,添加失败!");
		}
	}
	int filterGender = action.getParameterIntS("fgender");
	if(filterGender!=-1) {
		session.setAttribute("kshow-fg", new Integer(filterGender));
	}else {
		Integer fg = (Integer)session.getAttribute("kshow-fg");
		if(fg!=null)
			filterGender=fg.intValue();
	}
	
	int filterType = action.getParameterIntS("ftype");
	if(filterType!=-1) {
		session.setAttribute("kshow-ft", new Integer(filterType));
	}else {
		Integer ft = (Integer)session.getAttribute("kshow-ft");
		if(ft!=null)
			filterType=ft.intValue();
	}
	
	int filterCatalog = action.getParameterIntS("fcatalog");
	if(filterCatalog!=-1) {
		session.setAttribute("kshow-fc", new Integer(filterCatalog));
	}else {
		Integer ft = (Integer)session.getAttribute("kshow-fc");
		if(ft!=null)
			filterCatalog=ft.intValue();
	}
	String filter = "";

	if(filterGender>=0)	// 全部
		filter = " and gender=" + filterGender;
	if(filterType>=0)	// 全部
		filter += " and type=" + filterType;
	if(filterCatalog>=0)	// 全部
		filter += " and catalog=" + filterCatalog;

	int p = action.getParameterInt("p");
	int count = SqlUtil.getIntResult("select count(id) from show_commodity where del=0" + filter,5);
	PagingBean paging = new PagingBean(action,count,30,"p");
	List list = CoolShowAction.service.getCommodityList("1 " + filter + " order by id desc limit "+p*30+",30");
 %>
  <head>
    <title>物品管理</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
  	物品管理: 性别<select name="fgender" onchange="window.location='goods.jsp?fgender='+this.value"><option value="-2"<%if(filterGender==-2){%> selected<%}%>>全部</option><option value="0"<%if(filterGender==0){%> selected<%}%>>女</option><option value="1"<%if(filterGender==1){%> selected<%}%>>男</option><option value="2"<%if(filterGender==2){%> selected<%}%>>通用</option></select>
  	层次<select name="ftype" onchange="window.location='goods.jsp?ftype='+this.value">
  <option value="-2"<%if(filterType==-2){%> selected<%}%>>全部</option><%
  	List partList = CoolShowAction.getPartList();
	for(int i=0;i<partList.size();i++){
	PartBean part = (PartBean)partList.get(i);
%><option value="<%=part.getId()%>"<%if(filterType==part.getId()){%> selected<%}%>><%=part.getName()%></option><%
	}
%></select>
分类<select name="fcatalog" onchange="window.location='goods.jsp?fcatalog='+this.value">
  <option value="-2"<%if(filterCatalog==-2){%> selected<%}%>>全部</option><%
  	List catalogList = CoolShowAction.getCatalogList();
	for(int i=0;i<catalogList.size();i++){
	CatalogBean part = (CatalogBean)catalogList.get(i);
%><option value="<%=part.getId()%>"<%if(filterCatalog==part.getId()){%> selected<%}%>><%=part.getName()%></option><%
	}
%></select> - <a href="altergood.jsp">添加物品</a><br/>
<table border="1" width="100%">
  	<tr><td>物品id</td><td>名称</td><td>层次</td><td>分类</td><td>性别</td><td>价格</td><td>有效期</td><td>商品图片</td><td>物品图片</td><td>操作</td><td>推荐</td><td>覆盖</td><td>购买次数</td></tr>
  	<%
  		if(list != null && list.size() > 0){
  			for(int i=0;i<list.size();i++){
	  			Commodity good = (Commodity)list.get(i);
	  			Commodity comm = good;
	  			%>
	  			<tr><td width=60><%=good.getId()%></td>
	  			<td width=200><%=StringUtil.toWml(good.getName())%></td>
	  			<td width="60"><%if(good.getType()>0&&good.getType() <= CoolShowAction.part.length){%><%=CoolShowAction.part[good.getType()-1]%><%}%></td>
	  			<td width="60"><%if(good.getCatalog()>0){%><%=CoolShowAction.getCatalog(good.getCatalog()).getName()%><%}%></td>
	  			<td width="40"><%if(good.getGender()>=0&&good.getGender() < gender.length){%><%=gender[good.getGender()]%><%}%></td>
	  			<td width="40"><%if(comm!=null){%><%=comm.getPrice()%><%}%></td>
	  			<td width="40"><%=good.getDue()%></td>
	  			<td width="80"><img alt="x" src="/rep/show/comm/<%=comm.getBigImg()%>"/></td>
	  			<td width="100"><img alt="x" src="/rep/show/goods/<%=good.getGoodsImg()%>" width=80 height=100/></td>
	  			<td><a href="altergood.jsp?id=<%=good.getId()%>">修改</a>|<a href="goods.jsp?chhid=<%=comm.getId()%>"><%if(comm.getDel() == 0||comm.getDel()==1){%><%=hidden[comm.getDel()]%><%}%></a></td>
	  			<td>
		  			<form action="advs.jsp" method="post">
		  				<input type="hidden" name="commid" value="<%=comm.getId()%>"/>
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
	  			<td><%if(comm.getPartOtherCount()==0){%>无<%}else{%><font color=red><%=comm.getPartOtherCount()%>个</font><%}%></td>
	  			<td><%=comm.getCount()%></td>
	  			</tr>
	  			<%
  			}
  			%><%=paging.shuzifenye("goods.jsp?jcfr=1",true,"|",response)%><%
  		}
  	 %>
  	</table>
  	<a href="altergood.jsp">添加物品</a><br/>
 	<a href="index.jsp">返回酷秀首页</a>
  </body>
</html>
