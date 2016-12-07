<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.io.File,com.jspsmart.upload.*,net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=10; 
	String goods[] = {"靓点商品","奖品1","奖品2","奖品3","奖品4","奖品7"};
%>
<%SmartUpload smUpload = new SmartUpload();
MatchAction action = new MatchAction(request);
String tip = "";
String hideStr = "";
MatchGood matchGood = null;
//添加商品
int add = action.getParameterInt("a");
if (add == 1){
	try{
		smUpload.initialize(pageContext);
		smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
		smUpload.upload();
		boolean result = action.addGood(smUpload);
		if (result){
			tip = "添加成功";
		} else {
			tip = (String)request.getAttribute("tip");
		}
	}catch (Exception e){
		tip = "格式不正确.只能上传gif,jpg,png格式.";
		e.printStackTrace();
	}
}
// 设置隐藏
int hide = action.getParameterInt("h");
if (hide == 1){
	int goodId = action.getParameterInt("gid");
	matchGood = MatchAction.getGood(goodId);
	if (matchGood != null){
		SqlUtil.executeUpdate("update match_good set `hide`=" + (matchGood.getHide() == 0 ? 1 : 0) + " where id=" + matchGood.getId(),5);		
		matchGood.setHide(matchGood.getHide() == 0 ? 1 : 0);
		MatchAction.goodsMap.put(new Integer(matchGood.getId()),matchGood);
	} else {
		tip = "要删除的商品不存在";
	}
}
// 删除
int del = action.getParameterInt("d");
if (del > 0){
	matchGood = MatchAction.getGood(del);
	if (matchGood != null){
		SqlUtil.executeUpdate("delete from match_good where id=" + del,5);
		java.io.File file = new java.io.File(MatchAction.ATTACH_GOOD_ROOT + matchGood.getPhoto());
		if (file.exists()){
			file.delete();
		}
		MatchAction.goodsMap.remove(new Integer(matchGood.getId()));
	}
}
PagingBean paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
List list = MatchAction.service.getGoodList(" 1 order by prio desc,id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<html>
	<head>
		<title>商品管理</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a><br/>
		<%=tip%><br/>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>商品ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>商品名称</font>
				</td>
				<td align=center>
					<font color=#1A4578>所属类型</font>
				</td>
				<td align=center>
					<font color=#1A4578>产品价格</font>
				</td>
				<td align=center>
					<font color=#1A4578>积分价格</font>
				</td>
				<td align=center>
					<font color=#1A4578>现有/库存</font>
				</td>
				<td align=center>
					<font color=#1A4578>购买数</font>
				</td>
				<td align=center>
					<font color=#1A4578>优先级</font>
				</td>
				<td align=center>
					<font color=#1A4578>物品图片</font>
				</td>
				<td align=center>
					<font color=#1A4578>操作</font>
				</td>
			</tr>
				<% if (list != null && list.size() > 0){
						for(int i = 0 ; i < list.size() ; i++){
							matchGood = (MatchGood)list.get(i);
							if (matchGood != null){
								hideStr = "<a href=\"good.jsp?gid=" + matchGood.getId() + "&h=1\">";
								if (matchGood.getHide() == 0){
									hideStr += "隐藏</a>";
								} else {
									hideStr += "显示</a>";
								}
								%><tr>
									<td><%=matchGood.getId()%></td>
									<td><%=matchGood.getGoodNameWml()%></td>
									<td><%=goods[matchGood.getFlag()]%></td>
									<td><%=matchGood.getPrice()%></td>
									<td><%=matchGood.getPrice2()%></td>
									<td><%=matchGood.getCountNow()%>/<%=matchGood.getCount()%></td>
									<td><%=matchGood.getBuyCount()%></td>
									<td><%=matchGood.getPrio()%></td>
									<td><img src="<%=MatchAction.ATTACH_GOOD_URL_ROOT + matchGood.getPhoto()%>" alt="无法显示" /></td>
									<td><a href="mgood.jsp?gid=<%=matchGood.getId()%>">修改</a>/<a href="good.jsp?d=<%=matchGood.getId()%>" onclick="return confirm('真的要删除此商品吗?删除后无法找回!!')">删除</a>/<%=hideStr%></td>
								  </tr><%
							}
						}
				   }%>
		</table>
		<%=paging!=null?paging.shuzifenye("good.jsp",false,"|",response):""%>
		<form action="userGood.jsp" method="post">
			用户购买查询:<input type="text" name="uid" />
			<input type="submit" value="查询" />
		</form>
		<form action="good.jsp?a=1" method="post" enctype="multipart/form-data">
			商品名(20字内):<input type="text" name="gname"><br/>
			商品价格:<input type="text" name="price" /><br/>
			商品靓点:<input type="text" name="price2" /><br/>
			商品库存:<input type="text" name="count" /><br/>
			优先级:<select name="prio" >
				<%for (int i = 1 ; i <= 10 ; i++ ){
					%><option value="<%=i%>"><%=i%></option><%	
				}%>
			</select><br/>
			所属分类:<select name="flag">
				<option value="1">奖品1</option>
				<option value="2">奖品2</option>
				<option value="3">奖品3</option>
				<option value="4">奖品4(4-6名)</option>
				<option value="5">奖品7(7-10名)</option>
				<option value="0" selected="true">靓点商品</option>
			</select><br/>
			描述:(200字内)<br/><textarea name="describe" cols="80" rows="10"></textarea><br/>
			图片:<input type="file" name="image"><br/>
			<input type="submit" value="添加">
		</form>
	</body>
</html>