<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="新股申购">
<p align="left">
<%=BaseAction.getTop(request, response)%>
【新股申购流程】<br/>
1、在新股发行日开市后，点击新股进行申购。申购时间为6小时。申购后如果撤销委托将被视为申购无效<br/>
2、申购时间结束后，该股票将会暂停交易，申购的所有订单将在当时被记录下来<br/>
3、所有申购委托将按照100手为单位进行电脑自动抽签（100手以下的视为无效），中签的数量将直接完成交易，未中签的在当天晚退回申购资金。注意：电脑抽签会在几秒内完成。<br/>
【新股申购常见问题】<br/>
1、为何申购了一万手但是只买到了一百手？<br/>
答：申购是以一百手为最小单位进行抽签的。每签对应100手，每中一签就获得一签的数量交易。<br/>
2、申购后如果撤销了委托，之后还能再次申购吗？<br/>
答：可以继续申购，只要在申购的6小时内，以最后结束时间为准。<br/>
3、申购乐酷股票和申购上交所的股票有什么区别？<br/>
答：最小单位（每签数量）不同，时间限制也不同。更重要的是，上交所的股票用乐币买不了。<br/>
4、如果申购的数量不到一个签(100手)怎么办<br/>
答：所有小于单签数量(100手)的申购都直接视为无效.<br/>
5、每个股票帐户可以申购多少股票<br/>
答：不同的股票数量不同，一般来说帮会股最多可以申购两千万股，超过的部分将被视为无效。注意！一个帐户申购多次也将被视为无效，如果不小心发现申购了多次，请撤销多余的委托<br/>
<a href="index.jsp">返回股市大厅</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>