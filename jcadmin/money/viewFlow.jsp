<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.money.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="java.util.ArrayList"%><%@ page import="java.util.List"%><html>
	<b>每天金融统计报表&nbsp;&nbsp;<a href="index.jsp">返回</a></b><br><hr><br>
	<table width="90%" align="center" border="1">
		<tr>
			<td>
				乐币变化途径
			</td>
			<td>
				本日流入
			</td>
			<td>
				本日流出
			</td>
			<td>
				本日增量
			</td>
			<td>
				10日累计增量
			</td>
		</tr>
		<%// 数据库操作出始化
		    String date = request.getParameter("date");  //DateUtil.formatDate(DateUtil.rollDate(-1));
		    if(date==null || date.equals(""))return;
		
		    IMoneyService moneyService = ServiceFactory.createMoneyService();
		    
		    String sql = "select id from jc_money_flow_type order by id";
			List idList = SqlUtil.getIntList(sql, Constants.DBShortName);
			if (idList == null)return ;		
			
			long inFluxSum = 0;
			long outFluxSum = 0;
			long netFluxSum = 0;
			long tenDayFluxSum = 0;
			for(int i=0;i<idList.size();i++){
				Integer typeID = (Integer)idList.get(i);
				if(typeID==null)continue;
				
				MoneyFlowTypeBean flowType = moneyService.getMoneyFlowType("id=" + typeID.intValue());
				if(flowType==null)continue; 
									
				long outFlux = 0;
				long inFlux = 0;
				long netFlux = 0;
				long tenDayFlux = 0;
				
				sql = "select total_in from jc_money_flux where create_date='" + date + "' and type_id=" + typeID.intValue();
				inFlux = SqlUtil.getLongResult(sql, Constants.DBShortName);
					
				sql = "select total_out from jc_money_flux where create_date='" + date + "' and type_id=" + typeID.intValue();
				outFlux = SqlUtil.getLongResult(sql, Constants.DBShortName);
				
				netFlux = inFlux - outFlux;
				
				sql = "select sum(total_in) from jc_money_flux where type_id=" + typeID.intValue() + " and to_days('" + date + "')-to_days(create_date)<10 ";
				long totalInFlux = SqlUtil.getLongResult(sql, Constants.DBShortName);
				sql = "select sum(total_out) from jc_money_flux where type_id=" + typeID.intValue() + " and to_days('" + date + "')-to_days(create_date)<10 ";
				long totalOutFlux = SqlUtil.getLongResult(sql, Constants.DBShortName);
				tenDayFlux = totalInFlux - totalOutFlux;
				
				inFluxSum = inFluxSum + inFlux;
				outFluxSum = outFluxSum + outFlux;
				netFluxSum = netFluxSum + netFlux;
				tenDayFluxSum = tenDayFluxSum + tenDayFlux;
				%>
		<tr>
			<td>
				<%= flowType.getDescription() %>
			</td>
			<td>
				<%= outFlux %>
			</td>
			<td>
				<%= inFlux %>
			</td>
			<td>
				<%= netFlux %>
			</td>
			<td>			
				<b><%= tenDayFlux %></b>
			</td>
		</tr>
<%
			}
		%>
		<tr>
			<td>
				总计
			</td>
			<td>
				<%= outFluxSum %>
			</td>
			<td>
				<%= inFluxSum %>
			</td>
			<td>
				<%= netFluxSum %>
			</td>
			<td>			
				<b><%= tenDayFluxSum %></b>
			</td>
		</tr>
		</table>
</html>