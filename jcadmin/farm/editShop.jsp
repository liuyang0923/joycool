<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmNpcWorld.getWorld();		
int id = action.getParameterInt("id");

			FarmShopBean shop =null;
			shop = world.getShop(id);
			if (null != request.getParameter("add")) {
				int npcId = action.getParameterInt("npcId");
				int itemId = action.getParameterInt("itemId");
				int brush = action.getParameterInt("brush");
				int price = action.getParameterInt("price");
				int stack = action.getParameterInt("stack");
				int sellPrice = action.getParameterInt("sellPrice");
				int maxStack = action.getParameterInt("maxStack");
				int flag = action.getParameterFlag("flag");
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
						world.updateShop(shop);
                        response.sendRedirect("farmShop.jsp");
	
                }%>
			<html>
	<head>
	</head>
	<link href="common.css" rel="stylesheet" type="text/css">
	<body>
	<table width="100%">
	<form method="post" action="editShop.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					npcid
				</td>
				<td>
			<input type=text name="npcId" size="20" value="<%=shop.getNpcId()%>">
			</td>
				<tr>
				<td>
					物品id
				</td>
				<td>
			<input type=text name="itemId" size="20" value="<%=shop.getItemId()%>">
			    </td>
				<tr>
				<td>
					brush
				</td>
				<td>
			<input type=text name="brush" size="20" value="<%=shop.getBrush()%>">
			</td>
				<tr>
				<td>
					npc出售价格
				</td>
				<td>
			<input type=text name="price" size="20" value="<%=shop.getBuyPrice()%>">默认
			<input type=text name="defBuyPrice" size="20" value="<%=shop.getDefBuyPrice()%>">
			</td>
			<tr>
				<td>
					npc回收价格
				</td>
				<td>
			<input type=text name="sellPrice" size="20" value="<%=shop.getSellPrice()%>">默认
			<input type=text name="defSellPrice" size="20" value="<%=shop.getDefSellPrice()%>">
			</td>
			<tr>
				<td>
					初始库存
				</td>
				<td>
			<input type=text name="stack" size="20" value="<%=shop.getStack()%>">默认
			<input type=text name="defStack" size="20" value="<%=shop.getDefStack()%>">
			</td>
			<tr>
				<td>
					最大库存
				</td>
				<td>
			<input type=text name="maxStack" size="20" value="<%=shop.getMaxStack()%>">
			</td>
			<tr>
				<td>
					标志
				</td>
				<td>
			<%for(int flag=0;flag<FarmShopBean.FLAG_COUNT;flag++){%>
			 <input type=checkbox width="100" name="flag" value="<%=flag%>" <%if(shop.isFlag(flag)){%>checked<%}%>><%=FarmShopBean.flagString[flag]%>
			 <%}%><br/>
			</td>
				
				
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>