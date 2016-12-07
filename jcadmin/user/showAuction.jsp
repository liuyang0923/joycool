<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.auction.*,net.joycool.wap.service.impl.AuctionServiceImpl"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.bean.auction.*,net.joycool.wap.framework.*"%>
<%!static int COUNT_PRE_PAGE = 10;
   static AuctionServiceImpl service = new AuctionServiceImpl();
%>
<%AuctionAction action = new AuctionAction(request);
PagingBean paging = null;
AuctionBean bean = null;
DummyProductBean dummyProduct = null;
List list = null;
int pageNow = 0;
int userId = action.getParameterInt("uid");
if (userId > 0){
	paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
	pageNow = paging.getCurrentPageIndex();
	list = service.getAuctionList(" left_user_id=" + userId + " order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE );
}%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<form action="showAuction.jsp" method="get">
用户ID:<input type="text" name="uid">
<input type="submit" value="查询">
</form>
<table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>
			<font color=#1A4578>id</font>
		</td>
		<td align=center>
			<font color=#1A4578>拍卖人</font>
		</td>
		<td align=center>
			<font color=#1A4578>购买人</font>
		</td>
		<td align=center>
			<font color=#1A4578>物品</font>
		</td>
		<td align=center>
			<font color=#1A4578>是否已售</font>
		</td>
		<td align=center>
			<font color=#1A4578>起拍价</font>
		</td>
		<td align=center>
			<font color=#1A4578>一口价</font>
		</td>
		<td align=center>
			<font color=#1A4578>当前价</font>
		</td>
		<td align=center>
			<font color=#1A4578>次数</font>
		</td>
		<td align=center>
			<font color=#1A4578>时间</font>
		</td>
		<td align=center>
			<font color=#1A4578>行囊</font>
		</td>
	</tr>
	<% if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				bean = (AuctionBean)list.get(i);
				dummyProduct = action.getDummyProduct(bean.getProductId());
				if (bean != null){
					%><tr>
						<td><%=bean.getId()%></td>
						<td><%=bean.getLeftUserId()%></td>
						<td><%=bean.getRightUserId()%></td>
						<td><%=dummyProduct != null?dummyProduct.getName():"未知"%></td>
						<td><%=bean.getMark()==1?"已售":"未售"%></td>
						<td><%=StringUtil.bigNumberFormat(bean.getStartPrice())%></td>
						<td><%=StringUtil.bigNumberFormat(bean.getBitePrice())%></td>
						<td><%=StringUtil.bigNumberFormat(bean.getCurrentPrice())%></td>
						<td><%=bean.getTime()%></td>
						<td><%=bean.getCreateDatetime()%></td>
						<td><%=bean.getUserBagId()%></td>
					  </tr><%
				}
			}
	   }%>
</table>
<%=paging != null?paging.shuzifenye("showAuction.jsp?uid=" + userId, true, "|", response):""%>
<body>
</html>