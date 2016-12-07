<%@ page import="java.lang.reflect.*"%><%
Object obj = request.getAttribute("obj");
if(obj!=null){
Class c = obj.getClass();
Field[] fs = c.getDeclaredFields();
%>
<p><%=c.getName()%></p>
<%
for(int i = 0;i < fs.length;i++) {
	Field f = fs[i];
	int m = f.getModifiers();
	if(Modifier.isStatic(m)) continue;
	f.setAccessible(true);
	%>
<%=f.getName()%>&nbsp;=&nbsp;<%=f.get(obj)%>&nbsp;[<%=f.getType().getSimpleName()%>]<br/>
<%if(obj instanceof Collection){
Collection cl = (Collection)obj;
int limit = 20;
Iterator iter = cl.iterator();
while(iter.hasNext() && --limit>0) {
request.setAttribute("obj", iter.next());
%><%@include file="viewObject.jsp"%>

<%}}%>
<%}%>
<%}%>