<%@ page contentType="text/html;charset=utf-8"%>
<div id="lright1"><a href="#"><span class="f10 c0 s7 b">城堡:</span></a><table class="f10"><%
if(user!=null){
List castleList = service.getCastleList(user.getUid());
for(int i=0;i<castleList.size();i++){
	CastleBean c2 = (CastleBean)castleList.get(i);
%><tr><td class="nbr"><span<%if(c2.getId()==id){%> class="c2"<%}%>>&#8226;</span>&nbsp; <a href="<%=baseURL%>?id=<%=c2.getId()%>"><%=c2.getCastleNameWml()%></a></td><td class="right"><table class="dtbl" cellspacing="0" cellpadding="0"><tr>
<td class="right dlist1">(<%=c2.getX()%></td>
<td class="center dlist2">|</td>
<td class="left dlist3"><%=c2.getY()%>)</td>
</tr></table></td>
</tr><%}%>
</table></div></div><%}%>