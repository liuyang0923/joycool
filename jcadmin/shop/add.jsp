<%@ page language="java" import="net.joycool.wap.cache.*,net.joycool.wap.cache.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.service.impl.*,net.joycool.wap.spec.shop.*,net.joycool.wap.bean.dummy.*" pageEncoding="UTF-8"%><%@include file="../filter.jsp"%><%
if(!group.isFlag(0)) return;
String[] modeStr = {"","dummyId!=2特殊家具","dummyId!=2为宝箱","3","4","荣誉卡","6","7","增加论坛经验值","9","10","11","12","13","14","城堡战争卡","人物经验卡","酷币卷","更改 UserHonorBean.rank值","论坛VIP卡 更改ForumUserBean.vip的值","鹦鹉卡","21","22","23","24","25","26","27","28","29","30"};

ShopAction action = new ShopAction(request);
List tipList = new ArrayList();
int id = action.getParameterInt("id");
DummyServiceImpl dummyServiceImpl = new DummyServiceImpl();
if (null != request.getParameter("add")) {
	String name = action.getParameterString("name");
	
	if (!name.equals("")) {
			DummyProductBean item = new DummyProductBean();
			item.setId(action.getParameterInt("id"));
            item.setName(name);
            item.setIntroduction(action.getParameterString("introduction"));
            item.setDescription(action.getParameterString("description"));
			item.setMode(action.getParameterInt("mode"));
			item.setValue(action.getParameterInt("value"));
			item.setTime(action.getParameterInt("time"));
			item.setDummyId(action.getParameterInt("dummyId"));
			item.setPrice(action.getParameterInt("price"));
			item.setMark(action.getParameterInt("mark"));
			item.setBrush(action.getParameterInt("brush"));
			item.setBuyPrice(action.getParameterInt("buyPrice"));
			item.setBind(action.getParameterInt("bind"));
			
			int due = action.getParameterInt("due");
			String due2 = action.getParameterString("due2");
			if(due2.equals("hour")) {
				due = due * 60;
			} else if(due2.equals("day")) {
				due = due * 24 * 60;
			} else if(due2.equals("week")) {
				due = due * 24 * 60 * 7;
			} else if(due2.equals("month")) {
				due = due * 24 * 60 * 30;
			}
			item.setDue(due);
			item.setRank(action.getParameterInt("rank"));
			item.setSeq(action.getParameterInt("seq"));
			item.setStack(action.getParameterInt("stack"));
			item.setUnique(action.getParameterInt("unique"));
			item.setCooldown(action.getParameterInt("cooldown"));
			item.setAttribute("");
			item.setClass1(10);
			item.setUsage("");
			
		dummyServiceImpl.addDummyProduct(item);
		
		if (OsCacheUtil.USE_CACHE) {
			String key = "SELECT * from item WHERE (dummy_id < 10 or dummy_id = 15)";
			OsCacheUtil.flushGroup(OsCacheUtil.DUMMY_CACHE_GROUP,key);
		}
		
		
        response.sendRedirect("add.jsp");
	} else {%>
	<script>
	alert("请填写正确各项参数！");
	</script>
<%}
} else if(null != request.getParameter("u")){
	String name = action.getParameterString("name");
	if (!name.equals("")) {
		int ids = action.getParameterInt("id");
		String time = request.getParameter("time");
		String value = request.getParameter("value");
		
		int due = action.getParameterInt("due");
		
		String due2 = action.getParameterString("due2");
		
		String bind = action.getParameterString("bind");
		
		String stack = action.getParameterString("stack");
		
		int dummyId = action.getParameterInt("dummyId");
		
		if(due2.equals("hour")) {
			due = due * 60;
		} else if(due2.equals("day")) {
			due = due * 24 * 60;
		} else if(due2.equals("week")) {
			due = due * 24 * 60 * 7;
		} else if(due2.equals("month")) {
			due = due * 24 * 60 * 30;
		}
		
		int mode = action.getParameterInt("mode");
		
		dummyServiceImpl.updateDummyProduct("name='"+StringUtil.toSql(name)+"',time = " + time +", value=" + value + ",due="+due+",mode="+mode+",bind="+bind+",stack="+stack+",dummy_id="+dummyId," id = " + ids);
		
		if (OsCacheUtil.USE_CACHE) {
			String key = "SELECT * from item WHERE (dummy_id < 10 or dummy_id = 15)";
			OsCacheUtil.flushGroup(OsCacheUtil.DUMMY_CACHE_GROUP,key);
			
			ICacheMap dummyProductCache = CacheManage.itemProto;
			
			dummyProductCache.srm(ids);
			
		}
	}
	response.sendRedirect("add.jsp?msg=1");
	return;
}
List itemList = SqlUtil.getIntList("select id from item WHERE (dummy_id < 10 or dummy_id=15) ", 4);
PagingBean paging = new PagingBean(action, itemList.size(),30,"p");
itemList = itemList.subList(paging.getStartIndex(), paging.getEndIndex());

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>新添加物品</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<style>
	
	.td80{
	width:80px;text-align:center;
	}
	.td100{
	width:100px;text-align:center;
	}
	.td150{
	width:150px;text-align:center;
	}
	
	</style>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		<%
		String msg = request.getParameter("msg");
		if(msg != null) {%>
		alert("修改成功");
		<%}%>
	</script>
  </head>
  
  <body>
  物品后台
	<br />
<a href="add.jsp">刷新</a><br/>
<%if(id==0) {%>
	<table width="100%">
	<tr>
		<td>
			id
		</td>
		<td class="td80">
			名称
		</td>
		<td class="td100">
			描述
		</td>
		<td>
			mode
		</td>
		<td>
			dummyId
		</td>
		<td>
			due
		</td>
		<td>
			time
		</td>
		<td>
			堆叠
		</td>
		<td>
			唯一
		</td>
		<td>
			绑定
		</td>
		<td>value值</td>
		<td>
			操作
		</td>
	</tr>
		<%for (int i = 0; i < itemList.size(); i++) {
			Integer iid = (Integer)itemList.get(i);
			DummyProductBean item = UserBagCacheUtil.getItem(iid.intValue());
			tipList.add(item);
%>
	<tr>
		<td>
			<%=item.getId()%>
		</td>
		<td class="td80">
			<a href="../farm/editItem.jsp?id=<%=item.getId()%>"><%=item.getName()%></a>
		</td>
		<td class="td100">
			<%=item.getDescription()%>
		</td>
		<td>
			<%=item.getMode() <= modeStr.length ? modeStr[item.getMode()]+ "|mode=" +item.getMode() : item.getMode()%>
		</td>
		<td>
			<%=item.getDummyId() %>
		</td>
		<td>
			<%=item.getDue() %>
		</td>
		<td>
			<%=item.getTime() %>
		</td>
		<td>
			<%=item.getStack()%>
		</td>
		<td>
			<%=item.getUnique()%>
		</td>
		<td>
			<%=item.getBind()%>
		</td>
		<td><%=item.getValue() %></td>
		<td>
			<a href="add.jsp?id=<%=item.getId() %>">修改</a>
		</td>
	</tr>
		
		<%}%>
	</table>
	<%=paging.shuzifenye("add.jsp", false, "|", response,20)%>
<%} %>
	<a href="shop.jsp">返回商城管理首页</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
  <%if(id==0) {%>
  
    <form method="post" action="add.jsp?add=1">
    
名称：<input type="text" name="name"/><br/>
描述：<textarea rows="10"  cols="17" name="description"/></textarea><br/>
使用次数:<input type=text name="time" value="1"/><br/>
物品类型：<select name="mode">
			<option value="0">请选择</option>
			<%for(int i = 1;i < 100;i++) {%>
				<option value="<%=i %>"><%=i<30?modeStr[i]:i %></option>
			<%} %>
		</select><br/>
物品功能value值：<input type="text" name="value"/><br/>
堆叠:<input type=text name="stack" value="1"><br/>
绑定:<input type=text name="bind" value="1">(1绑定)<br/>
<input type=text name="introduction" value="">
<br/>dummyId:
<select name="dummyId">
	<%for(int i = 1;i < 20;i++) {%>
	<option value="<%=i %>"><%=i %></option>
	<%} %>
</select><br/>
有效期:<input type=text name=due value="1"><select name="due2"><option value="min">分钟</option><option value="hour">小时</option><option value="day" selected="selected">天</option><option value="week">周</option>
<option value="month">月</option>
</select><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>
<%}else { 
	DummyProductBean item = UserBagCacheUtil.getItem(id);
%>
    <form method="post" action="add.jsp?u=1">
    <input type=hidden name="id" value="<%=id %>"/><br/>
    id:<%=id %><br/>
名称：<input type="text" name="name" value="<%=item.getName() %>"/><br/>
描述：<textarea rows="10"  cols="17" name="description" value="<%=item.getDescription() %>"/></textarea><br/>
使用次数:<input type=text name="time"  value="<%=item.getTime() %>"/><br/>
物品类型：<select name="mode">
			<option value="0">请选择</option>
			<%for(int i = 1;i < 100;i++) {
				if(i == item.getMode()) {
			%>
				<option value="<%=i %>"  selected="selected"><%=i<30?modeStr[i]:i %></option>
			<%} else {
				%>
				<option value="<%=i %>"><%=i %></option>
				<%}
			} %>
		</select><br/>
物品功能value值：<input type="text" name="value" value="<%=item.getValue() %>"/>
堆叠:<input type=text name="stack" value="<%=item.getStack() %>"><br/>
绑定:<select name="bind">
<%if(item.getBind()==1) {%>
<option value="1">绑定</option><option value="0">不绑定</option>
<%}else{ %>
<option value="0">不绑定</option><option value="1">绑定</option>
<%} %>
</select>
<br/>dummyId:
<select name="dummyId">
	<%for(int i = 1;i < 20;i++) {
				if(i == item.getDummyId()) {
			%>
				<option value="<%=i %>"  selected="selected"><%=i %></option>
			<%} else {
				%>
				<option value="<%=i %>"><%=i %></option>
				<%}
			} %>
</select><br/>
有效期:<input type=text name=due value="<%=item.getDue() %>"><select name="due2"><option value="min">分钟</option><option value="hour">小时</option><option value="day">天</option><option value="week">周</option>
<option value="month">月</option>
</select>
	<input type="submit" value="修改">
	<br />
</form>
<%} %>
  </body>
</html>
