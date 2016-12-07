<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.service.impl.CatalogServiceImpl,java.io.File,com.jspsmart.upload.*,jc.download.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static CatalogServiceImpl service = new CatalogServiceImpl();%>
<%SmartUpload smUpload = new SmartUpload();
RingAction action = new RingAction(request);
String tip = "";
GameBean bean = null;
List list2 = service.getCatalogList(" type='ring' and visible=1");
CatalogBean cataBean = null;
//添加商品
int add = action.getParameterInt("a");
if (add == 1){
	try{
		smUpload.initialize(pageContext);
		smUpload.upload();
		boolean result = action.updateRing(smUpload);
		if (result){
			tip = "添加成功";
			net.joycool.wap.cache.OsCacheUtil.flushGroup("ring");
		} else {
			tip = (String)request.getAttribute("tip");
		}
	}catch (Exception e){
		tip = "上传失败.";
		e.printStackTrace();
	}
}
%>
<html>
	<head>
		<title>上传铃声</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		上传铃声:<br/><%=tip%><br/>
		<form action="ringUp.jsp?a=1" method="post" enctype="multipart/form-data">
			歌名(50字内):<input type="text" name="sname"><br/>
			演唱(50字内):<input type="text" name="singer" value="佚名"><br/>
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
			铃声:<input type="file" name="ring"><br/>
			<input type="submit" value="添加">
			<input id="cmd" type="button" value="首页" onClick="javascript:window.location.href='index.jsp'">
		</form>
	</body>
</html>