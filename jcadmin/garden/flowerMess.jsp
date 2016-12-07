<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static FlowerService service =new FlowerService();%>
<html>
	<head>
		<title>花之境管理</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%	FlowerAction action =new FlowerAction(request);
	boolean result = false;
	int uid = action.getParameterInt("uid");
	int flowerId = action.getParameterInt("fid");
	int mode = action.getParameterInt("mode");
	int operation = action.getParameterInt("ope");
	int count = action.getParameterInt("count");
	int count2 = action.getParameterInt("count2");
	int backId = action.getParameterInt("b");
	int maxCount = action.getParameterInt("maxC");
	if (count2 != 0){
		count = count2;
	}
    if ( mode==1 ){ 
		if ( operation == 1 ){ 
			//增加花种的数量
			result = service.buyFlowerSeed(uid,flowerId,count);
		} else {
			//减少花种的数量
			if (count > maxCount){
				count = maxCount;
			}
			result = service.descSeed(uid,flowerId,count);
		}
    } else if ( mode == 2 ){
		if ( operation == 1 ){
			//增加花朵的数量
			result = service.addFlower(uid,flowerId,count);
		} else {
			//减少花朵的数量
			if (count > maxCount){
				count = maxCount;
			}
			result = service.descFlower(uid,flowerId,count);
		}		   
    }
    if (result){
   		%>操作成功。<br/><%
    }else{
   		%>操作失败。<br/><%
    }%>
	<a href="flowerDataList.jsp?b=<%=backId%>&uid=<%=uid%>"><--返回</a>
	</body>
</html>