<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*,
                 net.joycool.wap.bean.AdvertiseBean"%><%@ page import="java.util.ArrayList"%><%@ page import="java.util.List,
                 java.sql.*"%> <html>
<br>修改广告内容<br><br>               
<form method="post" action="adadd.jsp">               
<%
 String id=(String)request.getParameter("id");

   DbOperation dbOp = new DbOperation();
    dbOp.init();
    
    List adList = new ArrayList();
    try{
    	ResultSet rs = dbOp.executeQuery("select * from  padvertise where id = "+id);
    	if(rs.next()){
    		%>
    		<input type="hidden" name="flag" value="notify">
    		<input type="hidden" name="id" value=<%=id%>>
    		广告描述：<input  name ="describe"   type="text"    size="40" value=<%=rs.getString("ad_name")%>><br><br>
			广告链接：<input  name ="wapurl"     type="text"   size="40" value=<%=rs.getString("ad_wap")%>><br><br>
			<%if(rs.getInt("status")==1){%>
			是否有效：<input   type="radio"  name ="ad"  value="1"  checked/>有效 
  				 			<input   type="radio"    name ="ad"  value="0"  />无效  
  			<%}else if(rs.getInt("status")==0){%>	 	
  			是否有效：<input   type="radio"  name ="ad"  value="1"  />有效 
  				 			<input   type="radio"    name ="ad"  value="0"  checked/>无效  
  			<%}%>
<br><br><input type="submit" value="提交">	
    <%	}
    }catch(Exception e)
    {e.printStackTrace();}
    dbOp.release();
%>
</form>
</html>