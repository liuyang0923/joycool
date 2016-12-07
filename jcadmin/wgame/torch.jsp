<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.action.wgame.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%
CustomAction action = new CustomAction(request, response);
int act = action.getParameterInt("a");
if(act==1){
	SqlUtil.executeUpdate("update torch set user_id=0", 4);
	CacheManage.stuff.srm("torchMap");
	CacheManage.stuff.srm("torchList");
	SqlUtil.executeUpdate("update torch_user set torch_count=0,torches=0");
	TorchAction.userCache.clear();
} else if(act==2) {
	TorchAction.calcAll();
	CacheManage.stuff.srm("torchUser");
} else if(act==3) {
	SqlUtil.executeUpdate("update torch set user_count=0", 4);
	CacheManage.stuff.srm("torchMap");
	CacheManage.stuff.srm("torchList");
} else if(act==4) {
	SqlUtil.executeUpdate("update torch_user set point=0");
	TorchAction.userCache.clear();
}

%>
<html>
	<head>
	</head>
	<body>
<a href="torch.jsp?a=4">清除所有人火炬指数</a><br/>
<a href="torch.jsp?a=2">计算所有人的积分</a><br/>
<a href="torch.jsp?a=1">把火炬释放</a><br/>
<a href="torch.jsp?a=3">清除所有火炬指数</a><br/>
		<br />
	</body>
</html>
