<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@include file="top.jsp"%><%!
static int ROW = 16;%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int level = action.getParameterInt("i");
List[][] dropCollection = FarmWorld.dropCollection;
List tipList = new ArrayList();
Integer[] randomItem = null;
%>
<html>
<head>
</head>
<script src='js/tooltip.js' type='text/javascript'></script>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
	掉落查询
	<br />
	<%if(level>0){
		randomItem = new Integer[10];
		for(int i = 0;i<randomItem.length;i++){
		
			if(RandomUtil.percentRandom(12)){
				List dropList = FarmWorld.dropCollection2[level];
				if(dropList != null) {
					Integer iid = (Integer)RandomUtil.randomObject(dropList);
					randomItem[i] = iid;
				}
			
			} else {		
		
				int grade = FarmWorld.getRandomGrade() + 1;
				List dropList = FarmWorld.dropCollection[level][grade];
				while(dropList == null && grade > 0) {	// 没有这品质物品则降级
					grade--;
					dropList = FarmWorld.dropCollection[level][grade];
				}
				if(dropList != null){
					Integer iid = (Integer)RandomUtil.randomObject(dropList);
					randomItem[i] = iid;
				}
			}
		}
	
	List[] dc = dropCollection[level];
	for(int i=1;i<dc.length;i++){
	List dcl = dc[i];
	%>
<%=DummyProductBean.gradeString[i]%>：
<%
	if(dcl!=null)
	for(int j=0;j<dcl.size();j++){
	Integer iid = (Integer)dcl.get(j);
	DummyProductBean item = world.getItem(iid.intValue());
	tipList.add(item);
	%>
	<a onmouseover='showdscp(event,"item<%=item.getId()%>")' onmousemove='movedscp(event,"item<%=item.getId()%>")' onmouseout='hinddscp(event,"item<%=item.getId()%>")'
			href="editItem.jsp?id=<%=item.getId()%>"><%=item.getName()%></a>
	<%}%>
<br/>
	<%}%>
	<%if(randomItem!=null){%>
随机掉落：
<%
for(int j=0;j<randomItem.length;j++){
Integer iid = randomItem[j];
if(iid==null) continue;
DummyProductBean item = world.getItem(iid.intValue());
tipList.add(item);
%>
<a onmouseover='showdscp(event,"item<%=item.getId()%>")' onmousemove='movedscp(event,"item<%=item.getId()%>")' onmouseout='hinddscp(event,"item<%=item.getId()%>")'
		href="editItem.jsp?id=<%=item.getId()%>"><%=item.getName()%></a>
<%}%>
	<%}%>
	<%}%>
	<table width="100%">
		<%for (int i8 = 1; i8 < dropCollection.length; i8+=ROW) {%>
		<tr>
<%for (int i = i8; i < i8+ROW && i<dropCollection.length; i++) {
			List[] dc = dropCollection[i];
%>
		<td>
			<%if(dc!=null){%><a href="farmDrop.jsp?i=<%=i%>">等级<%=i%></a><%}else{%>等级<%=i%><%}%>
		</td>
<%}%>
	</tr>
		
<%}%>
	</table>

	<a href="index.jsp">返回新手管理首页</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>

	<br />
<%@include file="tipList.jsp"%>
</body>
</html>
