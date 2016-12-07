<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.framework.*,net.joycool.wap.util.db.*,net.joycool.wap.bean.AdvertiseBean"%><%@ page import="java.sql.*"%> <html>
<%
 String wapurl=(String)request.getParameter("wapurl");
 String describe= (String)request.getParameter("describe");
 String ad=(String)request.getParameter("ad");
 String flag= (String)request.getParameter("flag");
 String id = (String) request.getParameter("id");

  DbOperation dbOp = new DbOperation();
 dbOp.init();
 if(flag.equals("notify")){
		 String sql="update  padvertise  set  ad_name='"+describe+"',ad_wap='"+wapurl+"',status = "+ad+"  where id = "+id;
 			boolean flag1= dbOp.executeUpdate(sql);
    	if(flag1){
				 //response.sendRedirect("adebook.jsp");
				 BaseAction.sendRedirect("/jcadmin/adebook/adebook.jsp", response);
				 return;
		}else{%>
			<p>修改失败</p>
			<p><a href="adebook.jsp">返回</a></p>
    	<%	}
 }else if (flag.equals("add")) {
   try{
   		String  sql1="insert into   padvertise (ad_name,ad_wap,status)values('"+describe+"','"+wapurl+"',"+ad+")";
   		//System.out.println(sql1);
    		boolean  flag2 = dbOp.executeUpdate(sql1);
    	if(flag2){
    		 //response.sendRedirect("adebook.jsp");
    		 BaseAction.sendRedirect("/jcadmin/adebook/adebook.jsp", response);
    		 return;
    	}else{
    	%>
			<p>添加失败</p>
			<p><a href="adebook.jsp">返回</a></p>
    	<%	
    	}
    }catch(Exception e)
    {e.printStackTrace();}
 }
    dbOp.release();
%>
</html>