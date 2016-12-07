<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	FlowerUserBean fub = FlowerUtil.getUserBean(action.getLoginUserId());
	if (fub == null){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	int next = action.getParameterInt("n");
	if (next == 1){
		fub = action.toNextTask(fub);
	} else {
		fub = action.checkTask(fub);
	}
	// 如果用户已经完成了所有的任务(也就是非法进入)，则跳回到花园页面
	if (fub.getTaskNum() > FlowerUtil.getTaskMap().size()){
		action.sendRedirect("fgarden.jsp",response);
		return;
	} 
	FlowerTask ft = FlowerUtil.getTaskInfo(fub.getTaskNum());
%><wml><card title="新手任务"><p><%=BaseAction.getTop(request, response)%>
<% if (!fub.isTaskComplete()){
//	   if (fub.getTaskNum() == 6){
	   		// 第6个任务，给先给用户加70点成就值。
//	   		FlowerUtil.updateExp(action.getLoginUserId(),70);
//	   }
	%>【任务<%=fub.getTaskNum() %>】<%=ft.getTitle()%><br/>
	   <%=ft.getStartMsg()%><br/>
	   【完成条件】<br/>
	   <%=ft.getMission()%><br/><%
   }else{
    %>【任务<%=fub.getTaskNum() %>】<%=ft.getTitle()%>(完成)<br/>
       <%=ft.getSuccess() %><br/>
       【已完成】<%=ft.getMission()%><br/>
       <%if (fub.getTaskNum() < FlowerUtil.getTaskMap().size()){
       		%><a href="task.jsp?n=1">继续下一个任务</a><br/><%
         }else if (fub.getTaskNum() == FlowerUtil.getTaskMap().size()){
         	%><a href="task.jsp?n=1">任务完成</a><br/><%
         }%>
    <%
}%>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>