<%@ page language="java" import="net.joycool.wap.util.*,jc.family.game.boat.*,jc.family.game.*,java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
BoatAction action = new BoatAction(request,response);
if(1 == action.getParameterInt("b")){
	request.setAttribute("tip","赛事添加成功!");
} else if(2 == action.getParameterInt("b")){
	request.setAttribute("tip","无法强制关闭赛事!");
} else if(3 == action.getParameterInt("b")){
	request.setAttribute("tip","无须重新打开赛事!");
} else if(4 == action.getParameterInt("b")){
	request.setAttribute("tip","赛事已强行关闭!");
} else if(5 == action.getParameterInt("b")){
	request.setAttribute("tip","赛事已重新打开!");
}
String[] weekGame = {"","龙舟","雪仗","问答"};
String[] state = {"未开玩","已开玩","已完赛"};
String[] state2 = {"已开赛","已强制关闭"};
int wid = action.getParameterInt("wid");
if(wid > 0){
 action.addMatch(wid);
 response.sendRedirect("todaygame.jsp?b=1");
}
List list = GameAction.getCurrentMatchList();
int cMid = action.getParameterInt("cMid");
int oMid = action.getParameterInt("oMid");
if(cMid > 0){
 MatchBean cMatch = (MatchBean) GameAction.matchCache.get(new Integer(cMid));;
 if(cMatch != null){
 	if(cMatch.getState() == 0){
	  BoatAction.service.upd("update fm_game_match set state2=1 where id="+cMid);
	  cMatch.setState2(1);
	 response.sendRedirect("todaygame.jsp?b=4");
 	} else {
	 response.sendRedirect("todaygame.jsp?b=2");
 	} 
 }
} else if (oMid > 0) {
 MatchBean oMatch = (MatchBean) GameAction.matchCache.get(new Integer(oMid));
 if(oMatch != null){
 	if(oMatch.getState2() == 1){
	  BoatAction.service.upd("update fm_game_match set state2=0 where id="+oMid);
	  oMatch.setState2(0);
	 response.sendRedirect("todaygame.jsp?b=5");
 	} else {
	 response.sendRedirect("todaygame.jsp?b=3");
 	}
 }
}
 %>
<html>
  <head>
    <title>今日赛事</title>
 <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
   <table border="1">
    <tr>
     <td align="center">序号</td>
     <td align="center">比赛</td>
     <td align="center">开始时间</td>
     <td align="center">结束时间</td>
     <td align="center">举办状态</td>
     <td align="center">操作</td>
     <td align="center">玩家游戏状态</td>
    </tr>
    <%
     if(list != null && list.size() > 0){
      for(int i=0;i<list.size();i++){
       Integer key = (Integer) list.get(i);
       MatchBean bean = (MatchBean) BoatAction.matchCache.get(key);
       if(bean != null){
       %>
       <tr>
        <td align="center"><%=i+1%></td>
        <td align="center"><%=weekGame[bean.getType()]%></td>
        <td align="center"><%=DateUtil.formatTime(bean.getStarttime())%></td>
        <td align="center"><%=DateUtil.formatTime(bean.getEndtime())%></td>
        <td align="center"><%=state2[bean.getState2()]%></td>
        <td align="center"><%if(bean.getState() > 0){%>无操作<%}else{if(bean.getState2() == 0){%><a href="todaygame.jsp?cMid=<%=bean.getId()%>">强制关闭</a><%}else{%><a href="todaygame.jsp?oMid=<%=bean.getId()%>">重新打开</a><%}}%></td>
        <td align="center"><%=state[bean.getState()]%></td>
       </tr>
       <%
       }
      }
     }
     %>
   </table>
 <a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>
