<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.CountUtil"%><%@ page import="java.net.URL"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLConnection"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.cache.OsCacheUtil"%><%@ page import=" net.joycool.wap.bean.friendlink.LinkRecordBean"%><%
int linkId = StringUtil.toInt(request.getParameter("linkId"));
//liuyi 2006-09-21 pv统计 start
String from = request.getParameter("from");
//liuyi 2006-09-21 pv统计 end
if(linkId == -1){
    //response.sendRedirect("http://wap.joycool.net");
    BaseAction.sendRedirect(null, response);
}else{
	    String mobile = (String) session.getAttribute("userMobile");
	    String sql = "SELECT * FROM link_record where link_id="+linkId;
	      //wucx 2006-10-12 pv缓存 start
	    String linkinfo=null;
	    linkinfo=(String) OsCacheUtil.get(linkId+"",
			OsCacheUtil.FRIEND_LINK_GROUP,
				OsCacheUtil.FRIEND_LINK_FLUSH_PERIOD);
	    
		String url=null;
		if (linkinfo == null) {
		     DbOperation dbOp = new DbOperation();
		    dbOp.init();
		    ResultSet rs=null;		
		    rs = dbOp.executeQuery(sql);  
		    if(rs!=null && rs.next()){
			    url=rs.getString("url");	
			    dbOp.release();
			    if (url != null){
			        OsCacheUtil.put(linkId+"", url,
					    OsCacheUtil.FRIEND_LINK_GROUP);
			    }
		    }
		    else{
		        dbOp.release();
			    //response.sendRedirect("http://wap.joycool.net");
			    BaseAction.sendRedirect(null, response);
			    return;
		    }	
		}  
		else
		{
		    url=linkinfo;
		}
		//wucx 2006-10-12 pv缓存 end
		//liuyi 2006-09-21 pv统计 start
			if(url!=null){
			    if(from!=null && !from.equals("")){
				    if(url.indexOf("?")!=-1){
				        url = url + "&from=jc";
				    }
				    else{
				        url = url + "?from=jc";
				    }
				}
			LogUtil.logJumpout(mobile + ":" + linkId);
			response.sendRedirect(url);
			}
			//liuyi 2006-09-21 pv统计 end
			else 
			//response.sendRedirect("http://wap.joycool.net");
			BaseAction.sendRedirect(null, response);

}
%>