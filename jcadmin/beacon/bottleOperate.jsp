<%@ page language="java" pageEncoding="utf-8"%>
<%@ page
	import="java.util.*,net.joycool.wap.spec.bottle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%>
<%!public static BottleService service = new BottleService();%>
<html>
	<%
		BottleAction action = new BottleAction(request);
		String f = action.getParameterString("f");
		int id = action.getParameterInt("id");
		//删除瓶子
		if ("bottle".equals(f)) {
			//先从list中删除。加锁。
			synchronized(BottleAction.lock){
				action.bottleCountList.remove(new Integer(id));
			}
			boolean result = service.deleteBottle(id);
			result = service.deleteAllReplyByBottleId(id);
			if (result) {
				action.innerRedirect("bottle.jsp",response);
			} else {
				%><font color=red>删除失败</font>
				<br />
				<a href="bottle.jsp">返回</a>
				<%
			}
		}
		//删除留言
		if ("reply".equals(f) || "allReply".equals(f)) {
			int bid = service.getReplyById(id).getBottleId();
			boolean result = service.deleteReply(id);
			result = service.decreaseReplyCount(bid);
			if (result) {
				if("allReply".equals(f)){
					response.sendRedirect("bottleAllReply.jsp");
				}else{
					response.sendRedirect("bottleReply.jsp?id=" + bid);
				}
				
			} else {
				%><font color=red>删除失败</font><br />
				<%if("allReply".equals(f)){
					%><a href="bottleAllReply.jsp">返回</a><%
				}else{
					%><a href="bottle.jsp">返回</a><%
				}
			}
		}
	%>
</html>