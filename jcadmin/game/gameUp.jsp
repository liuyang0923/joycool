<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.service.impl.CatalogServiceImpl,java.io.File,com.jspsmart.upload.*,jc.download.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static CatalogServiceImpl service = new CatalogServiceImpl();%>
<%SmartUpload smUpload = new SmartUpload();
GameAction action = new GameAction(request);
String tip = "";
GameBean bean = null;
List list2 = service.getCatalogList(" type='wapgame' and visible=1");
CatalogBean cataBean = null;
//添加商品
int add = action.getParameterInt("a");
if (add == 1){
	try{
		smUpload.initialize(pageContext);
		smUpload.upload();
		boolean result = action.updateGame(smUpload);
		if (result){
			tip = "添加成功";
			net.joycool.wap.cache.OsCacheUtil.flushGroup("game");
		} else {
			tip = (String)request.getAttribute("tip");
		}
	}catch (Exception e){
		tip = "格式不正确.只能上传gif,jpg,png格式.";
		e.printStackTrace();
	}
}
%>
<html>
	<head>
		<title>上传游戏</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		上传游戏:<br/><%=tip%><br/>
		<form action="gameUp.jsp?a=1" method="post" enctype="multipart/form-data">
			游戏名(100字内):<input type="text" name="gname"><br/>
			描述:<br/><textarea name="description" cols="80" rows="10"></textarea><br/>
			<%if (list2 != null && list2.size() > 0){
				%>类型:<select name="cata">
						<%for (int i = 0 ; i < list2.size() ; i++){
							cataBean = (CatalogBean)list2.get(i);
							if (cataBean != null){
								%><option value="<%=cataBean.getId()%>"><%=StringUtil.toWml(cataBean.getName())%></option><%
							}
						}
						%>
				  </select><br/><%	
			}%>
			图片:<input type="file" name="image"><br/>
			游戏:<input type="file" name="game"><br/>
			<input type="submit" value="添加">
			<input id="cmd" type="button" value="首页" onClick="javascript:window.location.href='index.jsp'">
		</form>
	</body>
</html>