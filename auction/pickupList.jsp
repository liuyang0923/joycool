<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.LuckyAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="java.util.Vector"%><%
response.setHeader("Cache-Control","no-cache");
int[] list = LuckyAction.pickupPlace;
String[] name = {
"论坛须知",
"拍卖帮助",
"股市入市须知",
"街霸帮助",
"大航海帮助",
"宠物排行榜",
"贺卡热门列表",
"猜数字帮助",
"三公帮助",
"大富豪帮助",
"桃花源帮助",
"大富翁购物超市",
};
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="pickup">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%for(int i = 0;i < list.length;i++){%>
<%=name[i]%><%if(list[i]>0){
DummyProductBean dummyProduct =LuckyAction.getDummyService().getDummyProducts(list[i]);
%>(<%=dummyProduct.getName()%>)
<%}%><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>