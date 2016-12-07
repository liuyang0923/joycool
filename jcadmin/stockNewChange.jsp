<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,
                 java.io.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.bean.stock.StockHolderBean" %><%@ page import="net.joycool.wap.bean.bank.StoreBean" %><%@ page import="net.joycool.wap.action.stock2.StockService" %><%@ page import="net.joycool.wap.bean.stock2.StockCCBean" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><% 
StockService stockService = new StockService();
IBankService bankService = ServiceFactory.createBankService();

int stockId = StringUtil.toInt(request.getParameter("stockId"));
int price2=   StringUtil.toInt(request.getParameter("price2"));
if(stockId >0 && price2 !=-1 ){
float price1 =  StringUtil.toFloat(request.getParameter("price1"));
if(price1==-1){
price1=0;
}
//狩猎公园新股分红更新所有拥护该股票用户账户金额
if(price1>0){
String sql="update stock_account a, stock_cc b set a.money=a.money+(b.count+b.count_f)*"+price1+" where  a.user_id=b.user_id and b.stock_id="+stockId;
SqlUtil.executeUpdate(sql,Constants.DBShortName);
}
//狩猎公园新股分红更新所有拥护该股票用户银行账户金额
if(price2>0){
Vector stockCCList = stockService.getStockCCList(" stock_id ="+stockId);
int totalCount=0;
StockCCBean stockCC = null;
	for(int i = 0;i<stockCCList.size();i++){
		stockCC = (StockCCBean)stockCCList.get(i);
		if(stockCC.getUserId()==431){
		continue;
		}
		long count = stockCC.getCount()+stockCC.getCountF();
		long money = count*price2;
		int userId= stockCC.getUserId();
		StoreBean store = bankService.getStore("user_id="+userId);
		if(store==null){
			store = new StoreBean();
			store.setUserId(userId);
			store.setMoney(money);
			bankService.addStore(store);
		}else{
			bankService.updateStore("money=money+"+money,"user_id="+userId);
		}
		totalCount++;
	}
if(stockCCList.size()>0){
	BankCacheUtil.flushBankStore();
}%>
<script>
alert("共更新"+<%=totalCount%>+"条记录！");
window.navigate("stockNewChange.jsp");
</script>
<%}
}

Vector stockList = stockService.getStockList("1=1");
%>
<html>
	<head>
		<title>股市分红管理后台</title>
<script language="javascript">
  function operate()
     {
     if (confirm('你确定要提交信息吗？')) {
       return true;
       } else {
        return false;
       }
      }
</script>
	</head>
<body>
<H1 align="center">股市分红管理后台</H1>
<hr>

	<table align="center" width="800" border="1">
		<th>股票编号</th><th>股票代码</th><th>股票名称</th><th>每股账户分红价格</th><th>每股银行分红价格</th><th>操作</th>
			<%for (int i = 0; i < stockList.size(); i++) {
				StockBean stock= (StockBean) stockList.get(i);%>
			<tr>
				<form method="post" action="stockNewChange.jsp">
					<td align="center">
						<%=stock.getId()%>
						<input type="hidden" name="stockId" value="<%=stock.getId()%>" >
				    </td>
				    <td align="center">
						<%=stock.getCode()%>
				    </td>
					<td align="center">
						<%=stock.getName()%>
					</td>
					<td align="center">
						<input type="text" name="price1" value="0.5" >
					</td>
					<td align="center">
						<input type="text" name="price2" value="0">
					</td>
					<td align="center">
						<input type="submit" id="add" name="add" value="确定" onClick="return operate()" >
					</td>
				</form>
			</tr>
			<%}%>
	</table>

</body>
</html>
