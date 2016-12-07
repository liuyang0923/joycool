<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,
                 java.io.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.bean.stock.StockHolderBean" %><%@ page import="net.joycool.wap.bean.bank.StoreBean" %><% 
IStockService stockService = ServiceFactory.createStockService();
IBankService bankService = ServiceFactory.createBankService();
Vector stockHolderList = stockService.getStockHolderList("1=1");
StockHolderBean stockHolder = null;
int totalCount=0;
for(int i = 0;i<stockHolderList.size();i++){
	stockHolder = (StockHolderBean)stockHolderList.get(i);
	long money = stockHolder.getTotalCount()*10;
	int userId= stockHolder.getUserId();
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
stockService.updateStockHolder("total_count=0","1=1");
BankCacheUtil.flushBankStore();
%>
一共更新了<%=totalCount%>条记录！
