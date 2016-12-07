<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.LogUtil"%><%@include file="../filter.jsp"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.action.stock2.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.bean.stock2.StockNoticeBean" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.stock2.StockCCBean" %><%
response.setHeader("Cache-Control", "no-cache");			
StockService service = new StockService();
int userId=StringUtil.toInt(request.getParameter("userId"));
int count = StringUtil.toInt(request.getParameter("count"));
int stockId = StringUtil.toInt(request.getParameter("stockId"));
//分配用户股票
if(userId>0 && count >0  && stockId>0){
	if(userId>10000000){%>
	<script>
	alert("用户ID不存在!");
	window.navigate("stock.jsp");
	</script>
	<%return;
	}
	UserBean user = UserInfoUtil.getUser(userId);
	if(user==null){%>
	<script>
	alert("用户ID不存在!"); 
	window.navigate("stock.jsp");
	</script>
	<%return;
	}
	StockBean stock = service.getStock("id="+stockId);
	if(stock==null){%>
	<script>
	alert("待分配股票不存在!");
	window.navigate("stock.jsp");
	</script>
	<%return;
	}
	StockCCBean stockCC  = service.getStockCC("user_id="+userId+" and stock_id="+stock.getId());
	LogUtil.logAdmin(adminUser.getName() + "+股票" + stockId + "用户"+userId);
	if(stockCC==null){
	stockCC = new StockCCBean();
	stockCC.setStockId(stock.getId());
	stockCC.setCount(count);
	stockCC.setCountF(0);
	stockCC.setCost(0);
	stockCC.setUserId(user.getId());
	service.addStockCC(stockCC);
	}else{
	service.updateStockCC("count=count+"+count,"id="+stockCC.getId());
	}%>
	<script>
	alert("赠与<%=user.getNickName()%>-----(<%=count%>)股-----<%=stock.getName()%>股票");
	window.navigate("stock.jsp"); 
	</script>
<%return;
}

Vector vec = service.getStockList(" 1=1 order by id desc");
%>
<html>
<head>
	 <script language="javascript" >
function checkform(){
	if (confirm('你确定要提交信息吗？')) {
       return true;
       } else {
        return false;
       }
}
</script>
</head>
	<body>
	<H1 align="center">分配个人股票后台</H1><hr>
		<table border="1" align="center" >
			<tr>
				<td>
					id
				</td>
				<td>
					股票ID
				</td>
				<td>
					股票名称
				</td>
				<td>
					用户ID
				</td>
				<td>
					分配数量
				</td>
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				StockBean stock = (StockBean) vec.get(i);%>	
	<form action="stock.jsp" name="userStock"   method="post" onsubmit="return checkform()">
			<tr>
				<td align="center">
					<%=i + 1%>
				</td>
				<td align="center">
					<%=stock.getId()%>
					<input type="hidden" name="stockId" value="<%=stock.getId() %>">
				</td>
				<td >
					<%=stock.getName()%>
				</td>
				<td>
					<input type=text name="userId" maxlength="8" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;">
				</td>
				<td>
					<input type=text name="count" maxlength="9"  onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;">
				</td>
				<td>
					<input type="submit" name="submit" value="确定"/>
				</td>
			</tr>
	</form>
			<%}%>
		</table>
		<br />
		<p align="center">
		<a href="/jcadmin/manage.jsp">返回管理首页</a><br />
		</p>
		
	</body>
</html>
