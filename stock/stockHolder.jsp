<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock.StockHolderBean" %><%@ page import="net.joycool.wap.bean.stock.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.stockHolder(request);
Vector holder=(Vector)request.getAttribute("holder");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="持有股票">
<p align="left">
<%=BaseAction.getTop(request, response)%>
持有股票：<br/>
<%for(int i=0;i<holder.size();i++){
  StockHolderBean stockHolder=(StockHolderBean)holder.get(i);
  if(stockHolder!=null){
      StockBean stock=action.getStock(stockHolder.getStockId());
      if(stock!=null){
         if(stock.getPrice()>10){%>
<a href="/stock/stockInfo.jsp?id=<%=stock.getId()%>"><%=stock.getName()%></a>：<br/>
<%       }else{%>
<%=stock.getName()%>：<br/>
<%         }%>
<%=stockHolder.getTotalCount()%>股<br/>
平均成本：<%=stockHolder.getAveragePrice()%><br/>
当前价格：<%=stock.getPrice()%><br/>
       <%if(stock.getPrice()>10){%>
<anchor title="确定">交易
<go href="/stock/stockBusiness.jsp?id=<%=stock.getId()%>" method="post">
</go>
</anchor>
<br/>
<%       }
     }
  }
}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%>
<%=fenye%><br/>
<%}%>
<br/>
<a href="/stock/stockAccount.jsp">返回我的帐户</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
