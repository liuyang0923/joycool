<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="com.jspsmart.upload.*,net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%SmartUpload smUpload = new SmartUpload();
MatchAction action = new MatchAction(request);
String tip = "";
String hideStr = "";
int goodId = action.getParameterInt("gid");
MatchGood matchGood = null;
if (goodId > 0){
	matchGood = MatchAction.getGood(goodId);
	if (matchGood == null){
		response.sendRedirect("good.jsp");
		return;
	}
}
//修改商品
int modify = action.getParameterInt("m");
if (modify == 1){
	try{
		smUpload.initialize(pageContext);
		smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
		smUpload.upload();
		boolean result = action.modifyGood(smUpload,matchGood);
		if (result){
			response.sendRedirect("good.jsp");
			return;
		} else {
			tip = (String)request.getAttribute("tip");
		}
	}catch (Exception e){
		tip = "格式不正确.只能上传gif,jpg,png格式.";
	}
}
%>
<html>
	<head>
		<title>修改商品休息</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a>|<a href="good.jsp">回上页</a><br/>
		<%=tip%><br/>
		<form action="mgood.jsp?m=1&gid=<%=matchGood.getId()%>" method="post" enctype="multipart/form-data">
			商品名(20字内):<input type="text" name="gname" value="<%=matchGood.getGoodName()%>"><br/>
			商品价格:<input type="text" name="price" value="<%=matchGood.getPrice()%>"><br/>
			商品靓点:<input type="text" name="price2" value="<%=matchGood.getPrice2()%>"><br/>
			商品库存:<input type="text" name="count" value="<%=matchGood.getCount()%>"><br/>
			优先级:<select name="prio" >
				<%for (int i = 1 ; i <= 10 ; i++ ){
					%><option value="<%=i%>"><%=i%></option><%	
				}%>
			</select><br/>
			<script language="javascript">document.forms[0].prio.value="<%=matchGood.getPrio()%>";</script>
			所属分类:<select name="flag">
				<option value="1">奖品1</option>
				<option value="2">奖品2</option>
				<option value="3">奖品3</option>
				<option value="4">奖品4(4-6名)</option>
				<option value="5">奖品7(7-10名)</option>
				<option value="0">靓点商品</option>
			</select><br/>
			<script language="javascript">document.forms[0].flag.value="<%=matchGood.getFlag()%>";</script>
			描述:(200字内)<br/><textarea name="describe" cols="80" rows="10"><%=matchGood.getDescribe()%></textarea><br/>
			<img src="<%=MatchAction.ATTACH_GOOD_URL_ROOT + matchGood.getPhoto()%>" alt="无法显示" /><br/>
			图片:<input type="file" name="image"><br/>
			<input type="submit" value="修改">
		</form>
	</body>
</html>