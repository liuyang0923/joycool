<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.*, net.joycool.wap.framework.*,net.joycool.wap.util.db.*"%><%@ page import="java.sql.*"%>        
<html>       
<%
String  id =(String)request.getParameter("id");
System.out.println(id);
   DbOperation dbOp = new DbOperation();
    dbOp.init();
    try{
    	boolean flag= dbOp.executeUpdate("delete from  padvertise  where id ="+id);
    	if(flag){
		 //response.sendRedirect("adebook.jsp");
		 BaseAction.sendRedirect("/jcadmin/adebook/adebook.jsp", response);
		 return;
		}else{%>
			<p>删除失败</p>
			<p><a href="adebook.jsp">返回</a></p>
    	<%	}
    }catch(Exception e)
    {e.printStackTrace();}
    
    dbOp.release();
%>


</form>
</html>