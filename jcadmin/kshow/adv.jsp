<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.util.*,jc.show.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	CoolShowAction action = new CoolShowAction(request);
	int commid = action.getParameterInt("commid");
  	Commodity com = null;
	if(commid > 0){
		com = CoolShowAction.getCommodity(commid);
	}
	String[] gender = {"女","男","通用"};
 %>
<html>
  <head>
    <title>商品信息</title>
    <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<%
  		if(com != null){
			%>
			<img src="/rep/show/comm/<%=com.getBigImg()%>" alt="dd"/><br/>
			商品ID:<%=com.getId()%><br/>
			商品名称:<%=StringUtil.toWml(com.getName())%><br/>
			性别要求:<%=gender[com.getGender()]%><br/>
			商品类型:<%=CoolShowAction.part[com.getType()-1]%><br/>
			商品价格:<img src="/shop/img/gold.gif" alt="酷币"/><%=com.getPrice()%>g/<%=com.getDue()%>天<br/>
			商品说明:<%=StringUtil.toWml(com.getBak())%><br/>
			<%			
  		}else{
			%>
			无此商品或者此商品已经下架!<br/>
			<%
  		}
  	 %>
  	 <a href="advs.jsp">返回推荐列表</a><br/>
  	 <a href="index.jsp">返回酷秀首页</a>
  </body>
</html>