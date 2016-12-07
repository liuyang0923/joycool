<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.DateUtil"%><%
int select = action.getParameterInt("s");
%><%if(select==0){%>运送资源<%}else{%><a href="fun.jsp?pos=<%=pos%>">运送资源</a><%}%>|<%if(select==1){%>买进<%}else{%><a href="fun.jsp?pos=<%=pos%>&amp;s=1">买进</a><%}%>|<%if(select==2){%>卖出<%}else{%><a href="fun.jsp?pos=<%=pos%>&amp;s=2">卖出</a><%}%><br/>
商人数:<%=building.getGrade()-action.getUserResBean().getMerchant() %>/<%=building.getGrade()%><br/><%

	switch(select){
	case 0:{
	
	int carry = action.getMerchantCarry();
%>每个商人可以运载<%=carry%>资源.<br/>输入对方城堡的位置:<br/>
X:<input name="x" format="*N"/>Y:<input name="y" format="*N"/><br/>
<anchor>运送资源<go href="merchant.jsp"><postfield name="x" value="$x"/><postfield name="y" value="$y"/></go></anchor><br/>
+派出的商人+<br/>
<%
List fromList = cacheService.getCacheMerchantByFromCid(action.getCastle().getId());
for(int i = 0; i < fromList.size(); i++) {
	MerchantBean merchantBean = (MerchantBean)fromList.get(i);
%>
<%=(i+1)%>.<%if(merchantBean.getType() == 0) {%>
派往<%CastleBean castleBean = CastleUtil.getCastleById(merchantBean.getToCid());
if(castleBean!=null){%><a href="search.jsp?x=<%=castleBean.getX()%>&amp;y=<%=castleBean.getY()%>"><%=castleBean.getX()%>|<%=castleBean.getY()%></a><%}else{%>[?]<%}%>的<%=merchantBean.getCount() %>商人剩余<%=DateUtil.formatTimeInterval2(merchantBean.getEndTime())%><br/>
资源:木<%=merchantBean.getWood() %>|石<%=merchantBean.getStone() %>|铁<%=merchantBean.getFe() %>|粮<%=merchantBean.getGrain() %><br/>
<%} else { %>
返回途中的<%=merchantBean.getCount() %>商人剩余<%=DateUtil.formatTimeInterval2(merchantBean.getEndTime())%><br/>
<%}%><%}%>
+派来的商人+<br/>
<%
List toList = cacheService.getCacheMerchantByToCid(action.getCastle().getId());
for(int i = 0; i < toList.size(); i++) {
	MerchantBean merchantBean = (MerchantBean)toList.get(i);
	if(merchantBean.getType() == 0) {
%><%=(i+1)%>.
来自<%CastleBean castleBean = CastleUtil.getCastleById(merchantBean.getFromCid());
if(castleBean!=null){%><a href="search.jsp?x=<%=castleBean.getX()%>&amp;y=<%=castleBean.getY()%>"><%=castleBean.getX()%>|<%=castleBean.getY()%></a><%}else{%>[?]<%}%>的运输剩余<%=DateUtil.formatTimeInterval2(merchantBean.getEndTime())%><br/>
资源:木<%=merchantBean.getWood() %>|石<%=merchantBean.getStone() %>|铁<%=merchantBean.getFe() %>|粮<%=merchantBean.getGrain() %><br/>
<%}}

	}break;
	case 1:{
	
%>==现有交易==<br/><%
		CastleBean castle = action.getCastle();
		UserResBean urb = action.getUserResBean();
		int p = action.getParameterInt("p");
		int start = p*10;
		int merchantCount = building.getGrade() - action.getUserResBean().getMerchant();
		String diss = "(x-"+castle.getX()+")*(x-"+castle.getX()+")+(y-"+castle.getY()+")*(y-"+castle.getY()+")";
		List list = action.getCastleService().getTradeList("cid!="+action.getCastle().getId()+" and " + diss + "<distance order by "+diss + " limit "+start+",11");
		int carry = action.getMerchantCarry();
		for(int i=0;i<list.size()&&i<10;i++){
			TradeBean trade = (TradeBean)list.get(i);
			int needTime = (int)(castle.calcDistance(trade.getX(),trade.getY()) * 3600 / 16);
%><a href="search.jsp?x=<%=trade.getX()%>&amp;y=<%=trade.getY()%>"><%=trade.getX()%>|<%=trade.getY()%></a><%=trade.getSupplyName()%><%=trade.getSupplyRes()%>换<%=trade.getNeedName()%><%=trade.getNeedRes()%><br/>
需时<%=DateUtil.formatTimeInterval2(needTime)%>-<%if(merchantCount<trade.getNeedMerchant(carry)){%>商人不足<%}else if(!urb.hasEnoughRes(trade.getNeed(),trade.getNeedRes())){%>资源不足<%}else{%><a href="trade.jsp?id=<%=trade.getId()%>&amp;s=1">接受</a><%}%><br/><%
	}
%><%if(list.size()<=10){%>下一页<%}else{%><a href="fun.jsp?pos=<%=pos%>&amp;s=1&amp;p=<%=p+1%>">下一页</a><%}%>|<%if(p==0){%>上一页<%}else{%><a href="fun.jsp?pos=<%=pos%>&amp;s=1&amp;p=<%=p-1%>">上一页</a><%}%><br/><%

	}break;
	case 2:{
	
%>提供:<input name="sR" format="*N"/><select name="su" value="1"><option value="1">木材</option><option value="2">石头</option><option value="3">铁块</option><option value="4">粮食</option></select><br/>
需求:<input name="nR" format="*N"/><select name="ne" value="2"><option value="1">木材</option><option value="2">石头</option><option value="3">铁块</option><option value="4">粮食</option></select><br/>
最高运输时间(小时):<input name="maxHour" format="*N"/><br/>
<anchor>确定卖出<go href="trade.jsp?s=2" method="post">
<postfield name="supply" value="$su"/><postfield name="need" value="$ne"/>
<postfield name="supplyRes" value="$sR"/><postfield name="needRes" value="$nR"/>
<postfield name="maxHour" value="$maxHour"/>
</go></anchor><br/><%
		List list = action.getCastleService().getTradeList("cid="+action.getCastle().getId());
%>==现有卖出==<br/><%
		for(int i=0;i<list.size();i++){
			TradeBean trade = (TradeBean)list.get(i);
%><%=i+1%>.<%=trade.getSupplyName()%><%=trade.getSupplyRes()%>换取<%=trade.getNeedName()%><%=trade.getNeedRes()%>-<a href="trade.jsp?del=<%=trade.getId()%>&amp;s=2">删</a><br/><%
		}

	}break;
	}
%>