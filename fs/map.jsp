<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>------城市地图------<br/>
<a href="/fs/index.jsp">城市</a>|<a href="blackMarket.jsp">黑市</a>|<a href="bank.jsp">银行</a>|<a href="hospital.jsp">医院</a><br/>
<a href="postOffice.jsp">邮局</a>|<a href="userBag.jsp">房屋中介</a>|
<%if (action.getFsUser().isEndDate()) {%>
	<a href="sceneList.jsp?sceneId=1">结束游戏</a>
<%} else {%>
	<a href="sceneList.jsp">火车站</a>
<%}%><br/>