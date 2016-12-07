<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 1800)
};
%><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);
RichUserBean richUser = action.getRichUser();

if(richUser.getWorldId()>=0) {	// 游戏中，而且不选择重新开始
response.sendRedirect(("go.jsp"));
return; }
if(ForbidUtil.isForbid("game",action.getLoginUser().getId())){
	response.sendRedirect("/enter/not.jsp");
	return;
}
int w = action.getParameterInt("w");
if(request.getParameter("r")!=null){	// start game

	RichWorld world;
	if(w >= 0 && w < RichAction.worlds.length){
	
		Tiny2Action action2 = new Tiny2Action(request, response);
		if(action2.checkGame(7)) return;
		
		world = RichAction.worlds[w];
		int resetCount = action.getParameterInt("rc");
		if(!world.isFlag(0)){
		}else if(world.isFull()){
			action.tip("return", "人满了，请稍后再来");
		}else if(world.resetCount != resetCount){
			response.sendRedirect("start.jsp?w="+w);
			return;
		}else{
			int role = action.getParameterInt("r");
			if(world.roleUser[role] > 0){
				action.tip("return", "该角色已经有人选用，请换一个试试");
			}else{
				if(action2.getGame() == null){
					action2.startGame(games[0], 7);
					return;
				}
			}
		}
	}else{
		action.tip("return");
	}
}
action.start();

int[] roleUser = action.world.roleUser;
int step = action.getParameterInt("s");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%if(action.isResult("return")){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="world.jsp?w=<%=w%>">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else if(step==0){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择人物：<br/><% String[] names = RichUserBean.roleNames;
for(int i=0;i<names.length;i++){ %>
<%if(roleUser[i]==0){%><a href="start.jsp?s=1&amp;r=<%=i%>&amp;w=<%=w%>&amp;rc=<%=action.world.resetCount%>"><%=names[i]%></a><%}else{%><%=names[i]%><%}%><%if(i%2==1){%><br/><%}else{out.print(' ');}%>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您选择了人物：<%=richUser.getRoleName()%><br/>
<a href="go.jsp">进入游戏</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%></wml>