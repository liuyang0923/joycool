<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="股市指南">
<p align="left">
<%=BaseAction.getTop(request, response)%>
股市指南<br/>
1、	每个上市公司都是乐酷栏目。他们的经营情况和股价密切相关，经营情况好坏的指标就是每天访问人数的多少！各位股东努力吧！<br/>
2、	股市开市时间是每天10点，此时系统根据前一天各上市公司经营情况自动调整当天股价走向。证券公告每日预告走向，请密切关注。<br/>
3、	为了保障股民利益，如果股价跌幅过大，该支股票会进入跌停状态，当日不能交易。股价会在回升后重新开放交易。<br/>
4、	经营情况决定股价大走向，但是因为炒股是一种风险极大的投资行为，股价波动难免，入市须谨慎！<br/>
5、	炒股三大忌：借钱炒股、用超过财产一半的钱炒股、贪心舍不得及时抛出。<br/>
6、	公测共有4家上市公司，将来个人家园、自建聊天室在满足条件之后同样可以上市！届时家园主人、聊天室管理员将摇身一变成为董事长，财源滚滚！<br/>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>