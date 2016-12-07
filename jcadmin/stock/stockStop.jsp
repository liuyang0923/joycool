<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.job.*,net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean,net.joycool.wap.bean.stock.*"%><%
//股票跌停，则以10元补给用户
int price = 10;
int stockId = StringUtil.toInt(request.getParameter("id"));
if(stockId<1)return;

List stockHolderList = ServiceFactory.createStockService().getStockHolderList("stock_id=" + stockId);
if(stockHolderList!=null){
	for(int i=0;i<stockHolderList.size();i++){
		StockHolderBean bean = (StockHolderBean)stockHolderList.get(i);
		if(bean==null)continue;
		
		int userId = bean.getUserId();
		long count = bean.getTotalCount();
		try{
		    UserInfoUtil.updateUserStatus("game_point=game_point+" + count*price, "user_id=" + userId, userId, userId, "股票跌停补偿" + count*price + "乐币");	
		}catch(Exception e){
		}
	}
}

String sql = "delete from jc_stock_holder where stock_id=" + stockId;
SqlUtil.executeUpdate(sql, Constants.DBShortName);
%>
done.