<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.service.impl.DummyServiceImpl"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%!
static String[] colors={
"black","black","black","black","black","black","black","black","#805020","#805020",
"black","black","black","black","black","red","red","gold","black","black",
"black","black","black","black","black","red","red","black","black","black",
"black","#44DD00","#44DD00","black","black","black","black","black","black","black",
"black","black","black","black","black","black","black","black","blue","blue",
"blue","blue","blue","blue","blue","blue","black","black","black","black",
"black","black","black","black","black","black","black","black","#FF88FF","FF88FF",
"black","black","black","FF88FF","black","gray","gray","black","black","black",
"black","black","black","black","black","black","black","black","black","black",
"black","black","#805020","#805020","black","black","#44DD00","black","#805020","black",
"gold","black","black","black","#805020","black","black","black","black","black",
"#805020","black","black","#805020","black","black","black","black","black","black",
};
%>
<%			
CustomAction action = new CustomAction(request, response);
DummyProductBean dummyProduct =null;
DummyServiceImpl service = new DummyServiceImpl();
int userId=action.getParameterInt("userId");
int time = action.getParameterInt("time");
int due = action.getParameterInt("due");
List dummyList = action.getParameterIntList("dummyId");
if(userId>0   && dummyList.size()>0&&time>0){
	if(userId<=0){%>
	<script>
	alert("用户ID不存在!");
	window.navigate("userProp.jsp?userId=<%=userId%>");
	</script>
	<%return;
	}
	UserBean user = UserInfoUtil.getUser(userId);
	if(user==null){%>
	<script>
	alert("用户ID不存在!"); 
	window.navigate("userProp.jsp?userId=<%=userId%>");
	</script>
	<%return;
	}
	else{
	String give="赠与 [ "+user.getNickNameWml()+" ](" + user.getId() + ") ";
	DbOperation dbquota = new DbOperation(true);
	for(int i=0;i<dummyList.size();i++){
		int dummyId = ((Integer)dummyList.get(i)).intValue();
		dummyProduct =service.getDummyProducts(dummyId);
		int quota = dbquota.getIntResult("select item_count from admin_quota_item where id="+adminUser.getId()+" and item_id="+dummyId);
		if(quota < time){
			give += dummyProduct.getName()+ "x"+time + "(限额不足),";
		}else{
			dbquota.executeUpdate("update admin_quota_item set item_count=item_count-"+time+" where id="+adminUser.getId()+" and item_id="+dummyId);
			UserBagCacheUtil.addUserBagCache(userId,dummyId,time,due);
			give += dummyProduct.getName() + "x"+time + ",";
		}
	}
	dbquota.release();
	LogUtil.logAdmin(adminUser.getName() + give);
	%>
	<script>
	alert("<%=give%>");
	window.navigate("userProp.jsp?userId=<%=userId%>"); 
	</script>
	<%}%>

<%return;
}
//所有道具
Vector vec = service.getDummyProductList("1");
List[] lists = {new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
for (int i = 0; i < vec.size(); i++) {
	DummyProductBean bean = (DummyProductBean)vec.get(i);
	if(bean.getDummyId()<4||bean.getMode()>0)
		lists[0].add(bean);
	else if(bean.getDummyId()<5) {
		if(bean.isBind())
			lists[1].add(bean);
		else if(bean.getId()>40)
			lists[2].add(bean);
		else
			lists[3].add(bean);
} else if(bean.getDummyId()<11)
		lists[4].add(bean);
}
%>
<html>
<head>
	 <script language="javascript" >
function checkform(){
	if (confirm('你确定要赠送吗？')) {
       return true;
       } else {
        return false;
       }
}
</script>
</head>
	<body>
	<H2 align="center">添加个人道具后台</H1>
	<form action="userProp.jsp" name="userProp" method="post" onsubmit="return checkform()">
	
	<table align="center"  cellpadding=2 cellspacing=1 bgcolor=#D0D0D0>
	<tr><td bgcolor="#FFFFFF">
	用户id<input type=text name="userId" maxlength="8" <%if(userId>0){%>value="<%=userId%>"<%}%>onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"></td>
	<td bgcolor="#FFFFFF"><input type="submit" name="submit" value="赠送道具"/></td>
	<td bgcolor="#FFFFFF">数量<input type=text name="time" value=1 maxlength="3" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;" size=2></td>
	<td bgcolor="#FFFFFF"><select name="due">
	<option value="0">默认</option>
	<option value="1440">1天</option>
	<option value="10080">1星期</option>
	<option value="43200">1个月</option>
	</select></td>
	<td bgcolor="#FFFFFF"><a href="../user/userBag.jsp?userId=<%=userId%>">查看行囊</a></td>
	</tr>
</table>
<table valign=top><tr>
<%for(int c = 0;c < lists.length;c++){
%>
<td valign=top>
<table align="center" cellpadding=2 cellspacing=1 bgcolor=#D0D0D0>
<%for (int i = 0; i < lists[c].size(); i++) {
		DummyProductBean bean = (DummyProductBean)lists[c].get(i);
		String color = "black";
		if(bean.getId() < colors.length)
			color = colors[bean.getId()];
%><tr><td align="center" bgcolor="#FFFFFF">
	<%=bean.getId()%>
</td>
<td align="left" bgcolor="#FFFFFF">
<input type=checkbox name="dummyId" value="<%=bean.getId() %>"><font color=<%=color%>><%=bean.getName()%></font><%if(UserBagCacheUtil.isItemShow(bean.getId())){%><img src="/rep/lx/e<%=bean.getId()%>.gif" alt=""/><%}%></td>
	</tr>

	<%}%>
</table>
		
		
</td><%}%></tr></table></form>
		<br />
		<p align="center">
		<a href="/jcadmin/manage.jsp">返回管理首页</a><br />
		</p>
		
	</body>
</html>
					