<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.util.db.*,net.joycool.wap.bean.AdvertiseBean"%><%@ page import="java.util.ArrayList"%><%@ page import="java.util.List,
                 java.sql.*"%><%
		String pagecount=request.getParameter("page");
		int total=0;
		if(pagecount==null||Integer.parseInt(pagecount)<0){
	   		 pagecount="0";
			}

    DbOperation dbOp = new DbOperation();
    dbOp.init();
    List adList = new ArrayList();
    try{
             ResultSet rs1 = dbOp.executeQuery("SELECT count(*)  as total FROM padvertise");
             rs1.next();
			total=rs1.getInt("total")/15+1; 
    		String sql="SELECT * FROM padvertise order by id desc limit "+Integer.parseInt(pagecount)*15+","+15+"";
    		//System.out.println(sql);
	    	ResultSet rs = dbOp.executeQuery(sql);
	    	if(rs==null){	}else{
	    	adList.clear();
	    	while(rs.next()){
	    		AdvertiseBean advertisebean=new AdvertiseBean();
	    		advertisebean.setID(rs.getInt("id"));
	    		advertisebean.setAd_name(rs.getString("ad_name"));
	    		advertisebean.setAd_wap(rs.getString("ad_wap"));
	    		advertisebean.setStatus(rs.getInt("status"));
	    		adList.add(advertisebean);
	    		}
	    	}
    }catch(Exception e)
    {e.printStackTrace();}
    
    dbOp.release();
%>
<html>
<br><br><center>广    告   列    表</center><br><br>

        <table border="1" cellspacing="1" width="95%" >
		<tr>
		<td width="5%" >编号</td>
		<td width="57%" >广告描述</td>
    	<td width="20%" >广告链接</td>
    	<td width="8%" >是否有效</td>	
    	<td width="5%" >修改</td>	
    	<td width="5%" >删除</td>
    	</tr>
<%
    for(int i=0;i<(adList.size());i++){
    	AdvertiseBean advertisebean = (AdvertiseBean)adList.get(i);
    	if(advertisebean==null)continue;
    	%>
    	<tr>
    	<td width="5%" ><%= (i+1) %></td>
    	<td width="57%" ><%= advertisebean.getAd_name() %></td>
    	<td width="20%" ><%= advertisebean.getAd_wap() %></td>
    	<%
    	int status = advertisebean.getStatus();
    	if(status==1){%>
    	<td width="8%" >有效</td>
    	<%}else if(status==0){%>
    	<td width="8%" >无效</td>
    	<%}%>
    	<td width="5%" ><a href="adnotify.jsp?id=<%=advertisebean.getId()%>">修改</a></td>
    	<td width="5%" ><a href="addelete.jsp?id=<%=advertisebean.getId()%>">删除</a></td>
		</tr>
		
<% }%></table>
			 <br>
	  		 <A>共【<%=total%>】页</A>&nbsp;&nbsp;&nbsp;
	    	 <A>当前第【<%=Integer.parseInt(pagecount)+1%>】页</A>&nbsp;&nbsp;&nbsp;
	    	  <A>每页【15】条</A>&nbsp;&nbsp;&nbsp;
      【<A href="adebook.jsp?page=0">首页</A>】&nbsp;&nbsp;&nbsp;
      【<A href="adebook.jsp?page=<%=Integer.parseInt(pagecount)-1%>">上页</A>】&nbsp;&nbsp;&nbsp;
      【<A href="adebook.jsp?page=<%=Integer.parseInt(pagecount)+1%>">下页</A>】&nbsp;&nbsp;&nbsp;
      【<A href="adebook.jsp?page=<%=total-1%>">尾页</A>】&nbsp;&nbsp;&nbsp;
<p>增加广告</p>

<form method="post" action="adadd.jsp">

				<input type="hidden" name="flag" value="add"/>
广告描述：<input  name ="describe"   type="text"    size="40" value=""><br><br>
广告链接：<input  name ="wapurl"     type="text"    size="40" value=""><br><br>
是否有效：<input   type="radio"  name ="ad"  value="1"  checked/>有效 
  				<input   type="radio"    name ="ad"  value="0"  />无效  
  				 

<br><br><input type="submit" value="增加">
</form>
</html>