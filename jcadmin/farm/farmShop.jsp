<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld.getWorld();
FarmNpcWorld world = FarmNpcWorld.getWorld();
List shopList = FarmNpcWorld.shopList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,shopList.size(),20);

			String prefixUrl = "farmShop.jsp";

			if (null != request.getParameter("delete")) {/*
				int id = StringUtil.toInt(request.getParameter("delete"));
				dbOp = new DbOperation();
				dbOp.init();
				dbOp
						.executeUpdate("delete from farm_map_node where id="
								+ id);
				dbOp.release();*/
			}

			%>
<%if (null != request.getParameter("add")) {
                int id = action.getParameterInt("id");
				int npcId = action.getParameterInt("npcId");
				int itemId = action.getParameterInt("itemId");
				int brush = action.getParameterInt("brush");
				int price = action.getParameterInt("price");
				int stack = action.getParameterInt("stack");
				int sellPrice = action.getParameterInt("sellPrice");
				int maxStack = action.getParameterInt("maxStack");
				int flag = action.getParameterFlag("flag");
			FarmShopBean shop = new FarmShopBean();
			shop.setNpcId(npcId);
			shop.setItemId(itemId);
			shop.setBrush(brush);
			shop.setBuyPrice(price);
			shop.setStack(stack);
			shop.setSellPrice(sellPrice);
			shop.setDefBuyPrice(action.getParameterInt("defBuyPrice"));
			shop.setDefSellPrice(action.getParameterInt("defSellPrice"));
			shop.setDefStack(action.getParameterInt("defStack"));
			shop.setMaxStack(maxStack);
			shop.setFlag(flag);
			world.addShop(shop);
                     response.sendRedirect("farmShop.jsp");

			}
			List vec = shopList.subList(paging.getStartIndex(),paging.getEndIndex());
			FarmShopBean shop = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		商店后台
		<br />
		<br />
		<form method="post" action="farmShop.jsp?add=1">
		    id：
			<input id="id" name="id"><br/>
			npcid：
			<input id="npcId" name="npcId"><br/>
			物品id：
			 <input id="itemId" name="itemId"><br/>
			 brush：
			 <input id="brush" name="brush"><br/>
			 npc出售价格：
			 <input id="price" name="price">默认<input id="price" name="defBuyPrice"><br/>
			 npc回收价格：
			 <input id="sellPrice" name="sellPrice">默认<input id="price" name="defSellPrice"><br/>
			 初始库存：
			 <input id="stack" name="stack">默认<input id="price" name="defStack"><br/>
			 最大库存：
			 <input id="maxStack" name="maxStack"><br/>
			 标志：
			 <%for(int flag=0;flag<FarmShopBean.FLAG_COUNT;flag++){%>
			 <input type=checkbox name="flag" value="<%=flag%>"><%=FarmShopBean.flagString[flag]%>
			 <%}%><br/>
			  
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>


		<table width="100%">
			<tr>
				<td>
					id
				</td>
				<td>
					npcid
				</td>
				<td>
					物品id
				</td>
				<td>
					brush
				</td>
				<td>
					npc出售价格/默认
				</td>
				<td>
					npc回收价格/默认
				</td>
				<td>
					初始库存/默认
				</td>
				<td>
					最大库存
				</td>
				<td>
					标志
				</td>
				
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				shop = (FarmShopBean) vec.get(i);
%>
			<tr>
				<td>
					<%=shop.getId()%>
				</td>
				<td>
					<%=shop.getNpcId()%>
				</td>
				<td>
					<%=shop.getItemId()%>
				</td>
				<td>
					<%=shop.getBrush()%>
				</td>
				<td>
					<%=shop.getBuyPrice()%>/<%=shop.getDefBuyPrice()%>
				</td>
				<td>
					<%=shop.getSellPrice()%>/<%=shop.getDefSellPrice()%>
				</td>
				<td>
					<%=shop.getStack()%>/<%=shop.getDefStack()%>
				</td>
				<td>
					<%=shop.getMaxStack()%>
				</td>
				<td width="80">
			<%for(int flag=0;flag<FarmShopBean.FLAG_COUNT;flag++){%>
			 <%if(shop.isFlag(flag)){%><%=FarmShopBean.flagString[flag]%>&nbsp;<%}%>
			 <%}%><br/>
				</td>
				
				<td>
					<a href="editShop.jsp?id=<%=shop.getId()%>">编辑</a>
				</td>
			</tr>
			<%}%>
		</table>
		<%=paging.shuzifenye(prefixUrl, false, "|", response)%>

		<a href="index.jsp">返回新手管理首页</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
