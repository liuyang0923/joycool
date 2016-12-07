<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.view6();
TinyGame6 tg = (TinyGame6)action.getGame();
int from = action.getParameterIntS("f");
int to = action.getParameterIntS("t");
if(to!=-1)
	from=-1;
int fromMark = 0;
if(from != -1)
	fromMark = tg.getLastMark(from);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
已移动了<%=tg.getMoveCount()%>次<br/>
<%
int[][] tower = tg.getTower();
int pos = 0;
for(int i=0;i<tg.getCount();i++){%><%=(char)('A'+i)%>-
<%for(int j=0;j<tg.getCount2();j++){
	if(tower[i][j]==0){
		break;
}else{%>
	<%if(j >= tg.getCount2() - 1 || tower[i][j + 1] == 0){%>
		<a href="6v.jsp?f=<%=i%>"><%=TinyGame6.mark[tower[i][j]]%></a>
	<%}else{%>
		<%=TinyGame6.mark[tower[i][j]]%>
	<%}%>
	
<%}

%><%}%><%if(from != -1&&from != i&&tg.getLastMark(i)<fromMark){%><a href="6v.jsp?f=<%=from%>&amp;t=<%=i%>">+</a><%}%><br/>
<%}%>
<br/>
<%if(action.isGameOver()){%>
<a href="<%=(action.getReturnURLWml())%>">游戏完成</a><br/>
<%}else{%>
移动，直到所有的数字都移动到A<br/>
移动过程中，大数字必须在小数字右侧<br/>
<a href="giveup.jsp">放弃</a><br/>
<%}%>

</p>
</card>
</wml>