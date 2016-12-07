<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%><%@ page import="java.util.ArrayList"%><%@ page import="java.util.List,
                 java.sql.*"%><%
    DbOperation dbOp = new DbOperation();
    dbOp.init();
    
    List dateList = new ArrayList();
    try{
    	ResultSet rs = dbOp.executeQuery("select distinct create_date from jc_money_flux order by create_date desc");
    	if(rs!=null){
    		while(rs.next()){
    			dateList.add(rs.getDate(1));
    		}
    	}
    }catch(Exception e)
    {}
    
    dbOp.release();
%>
<html>
每天金融统计报表<br><br>
<%
    for(int i=0;i<(dateList.size()-9);i++){
    	Date date = (Date)dateList.get(i);
    	if(date==null)continue;
    	
    	%><a href="viewFlow.jsp?date=<%= date %>" ><%= date %></a><br><% 
    }
%>
<br><br>
<a href="totalMoney.jsp" >系统当前乐币数据</a><br>
</html>