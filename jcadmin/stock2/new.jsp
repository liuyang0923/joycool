<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.stock2.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.stock2.*" %><%@ page import="net.joycool.wap.bean.*" %><%
response.setHeader("Cache-Control", "no-cache");			
StockService service = new StockService();
CustomAction action = new CustomAction(request);
int stockId=action.getParameterInt("stockId");
List list = service.getStockList("status=1");

%>
<html>
<head>
</head>
<body>
<%if(!action.isMethodGet()){
	StockBean stock2 = service.getStock("id=" + stockId);
	long count = action.getParameterLong("count");
	float price=action.getParameterInt("price");
	if(stock2==null||count<10000l||count>100000000000l||price<10||price>1000){
		response.sendRedirect("new.jsp");
		return;
	}
	int maxCount=2000;	// 每人最大2000签
	List wtList = service.getStockWTList("stock_id=" + stockId + " and mark=1 and count-cj_count>=10000");
	List units = new ArrayList(wtList.size()*10);	// 这个列表里保存所有的中签列表，每个委托可能有多个数据在里面
	HashMap unitsCount2 = new HashMap(500);	// 确保每个用户最多2000万股委托
	for(int i = 0;i < wtList.size();i++){
		StockWTBean wt = (StockWTBean)wtList.get(i);
		long unitCount = (wt.getCount()-wt.getCjCount())/10000;	// 此人申购多少签，每签100手，余数不要
		if(unitCount>maxCount)
			unitCount = maxCount;
		
		Integer key = new Integer(wt.getUserId());
		int left = 0;
		Long ic = (Long)unitsCount2.get(key);	// 确保每人最多maxCount签
		if(ic==null){
			unitsCount2.put(key, new Long(unitCount));
		}else {
			if(unitCount+ic.longValue()>maxCount) {
				unitsCount2.put(key, new Long(maxCount));
				unitCount = maxCount - ic.longValue();
			} else 
				unitsCount2.put(key, new Long(unitCount+ic.longValue()));
		}
		
		for(int j=0;j<unitCount;j++)
			units.add(wt);
			
	}
	if(units.size()==0){	//没有人来申购
		response.sendRedirect("new.jsp");
		return;
	}
	int all = units.size();
	int totalUnit = (int)(count / 10000);
	List units2 = new ArrayList(totalUnit/200);	// 中签的
	HashMap unitsCount = new HashMap(totalUnit/200);
	for(int i = 0;i < totalUnit;i++){
		if(units.size()==0)break;
		int got = RandomUtil.nextInt(units.size());
		
		StockWTBean wt = (StockWTBean)units.remove(got);
		Integer key = new Integer(wt.getId());
		
		Integer ic = (Integer)unitsCount.get(key);
		if(ic==null){
			unitsCount.put(key, new Integer(1));
			units2.add(wt);
		}else
			unitsCount.put(key, new Integer(ic.intValue()+1));
	}
	// 计算完毕，开始发售股票
	for(int i=0;i<units2.size();i++){
		StockWTBean stockWT = (StockWTBean)units2.get(i);
		long count2 = ((Integer)unitsCount.get(new Integer(stockWT.getId()))).intValue() * 10000l;	// 计算股数
		
		{
			float buyPrice = price;
			long cost = (long) (buyPrice * count2);
			long buyCount = stockWT.getCount() - stockWT.getCjCount();// 委托购买数量

			StockCCBean cc = StockWorld.getStockCC("user_id= "
					+ stockWT.getUserId() + " and stock_id = " + stockId);// 获取委托购买股票用户持仓中是否拥有准备购买的股票
			long initCount = (cc == null ? 0 : cc.getCount() + cc.getCountF()); 
			
			// 写入两条成交记录到成交表
			StockWorld.addStockCJ(stockId, stockWT.getUserId(),
					buyPrice, count2, 1, 0, stockWT.getId(), initCount);// 插入委托购买购票用户的成交记录
			// ---------------------------------------------------
			// 如果委托2的成交数量>=委托数量
			if (buyCount <= count2) {
				// 删除数据库记录
				StockWorld.deleteStockWT("id=" + stockWT.getId());// 删除委托购买用户记录
			} else {
				// 更新数据库记录
				StockWorld.updateStockWT("cj_count=cj_count+" + count2,
						"id=" + stockWT.getId());
			}
			// ---------------------------------------------------

			StockAccountBean saleStockAccount = StockWorld
					.getStockAccount(stockWT.getUserId());// 获取委托出售股票用户账户信息
			if (saleStockAccount != null) {
				StockWorld.UpdateStockAccount(// 更新委托出售股票用户账户金额
						"money_f=money_f-" + (cost),
						"user_id=" + stockWT.getUserId());
			}

			if(cc == null)
				StockWorld.addStockCC(stockWT.getUserId(), stockId, count2, 0, cost);
			else
				StockWorld.addStockCC(cc, count2, cost);


		}
	}

	service.updateStock("status=0,`count`=`count`+"+count, "id="+stockId);
%><script>
alert('新股发行完毕,中签率百分之<%=(float)totalUnit*100/all%>');
window.location="new.jsp";
</script>
<%}%>


<H1 align="center">新股发行</H1><hr>

<%if(stockId==0){%>
	<%if(list.size()==0){%>
		(暂无新股可以发行)<br/>
	<%}else{%>
发行新股：<select name="stockId" onchange="javascript:if(this.value!=0)(window.location='new.jsp?stockId='+this.value)">
<option value="0">未选择</option>
<%for(int i=0;i<list.size();i++){
StockBean stock = (StockBean)list.get(i);

%><option value="<%=stock.getId()%>"><%=stock.getName()%></option>

<%}%>
</select>
	<%}%>
<%}else{
	StockBean stock2 = service.getStock("id=" + stockId);
%><%=stock2.getName()%><br/>
<a href="new.jsp">重新选择新股发行</a></br>
<br/>
<form action="new.jsp?stockId=<%=stockId%>" method="post">
发行数量(股)<input type=text name="count" value="1000000000"><br/>
发行价格<input type=text name="price" value="30"><br/>
<input type=submit  value="提交发行新股" onclick="return confirm('确认发行该新股？')">
</form>

<%}%>

<br />
<br>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br />
</body>
</html>
