<%@ page contentType="text/html;charset=utf-8"%><div id="ltop1"><div id="ltop5"><a href="page1.jsp" id="n1"><img src="img/blank.gif" title="资源"></a><a href="page2.jsp" id="n2"><img src="img/blank.gif" title="建筑"></a><a href="page3.jsp" id="n3"><img src="img/blank.gif" title="地图"></a><a href="stat.jsp" id="n4"><img src="img/blank.gif" title="排行榜"></a></div></div>
<%if(castleRes==null)castleRes=new UserResBean();
long now2 = System.currentTimeMillis();%><div id="lres0"><table align="center" cellspacing="0" cellpadding="0"><tr valign="top">
<td><img class="res" src="img/1.gif" title="木材"></td>
<td><%=castleRes.getWood(now2)%>/<%=castleRes.getMaxRes()%></td>
<td class="s7"><img class="res" src="img/2.gif" title="石头"></td>
<td><%=castleRes.getStone(now2)%>/<%=castleRes.getMaxRes()%></td>
<td class="s7"><img class="res" src="img/3.gif" title="铁块"></td>
<td><%=castleRes.getFe(now2)%>/<%=castleRes.getMaxRes()%></td>
<%if(castleRes.getMaxRes()>=10000){%>
<td>
<table class="f9" cellspacing="0" cellpadding="0">
<tr>
<td class="s7"> <img class="res" src="img/4.gif" title="粮食"></td>
<td id=l1><%=castleRes.getGrain(now2)%>/<%=castleRes.getMaxGrain()%></td>
</tr>
<tr>
<td class="s7" style="padding-top:4px;"> <img class="res" src="img/5.gif" title="粮食消耗"></td>
<td style="padding-top:4px;"><%=castleRes.getPeople()+castleRes.getPeople2()%>/<%=castleRes.getGrainSpeed2()%></td>
</tr>
</table>
</td>
<%}else{%>
<td class="s7"> <img class="res" src="img/4.gif" title="粮食"></td>
<td><%=castleRes.getGrain(now2)%>/<%=castleRes.getMaxGrain()%></td>
<td class="s7"> &nbsp;<img class="res" src="img/5.gif" title="粮食消耗">&nbsp;<%=castleRes.getPeople()+castleRes.getPeople2()%>/<%=castleRes.getGrainSpeed2()%></td>
<%}%>
</tr></table></div>