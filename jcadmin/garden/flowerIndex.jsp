<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static FlowerService service =new FlowerService(); %>
<html>
	<head>
		<title>花之境管理</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%	FlowerAction action =new FlowerAction(request);
	int uid = action.getParameterInt("uid");
	int type = action.getParameterInt("type");
	int exp = action.getParameterInt("exp");
	FlowerUserBean fub = FlowerUtil.getUserBean(uid);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	if (fub == null && uid != 0){
		%><font color="red">ID：<%=uid%>&nbsp;不是花之境用户.</font><br/><a href="flowerIndex.jsp">返回</a><br/><%
		return;
	}
	// 操作成就值
	if (exp != 0){
		FlowerUtil.updateExp(uid,exp);
	}
	if (type == 0){
		int userCount = SqlUtil.getIntResult("select count(user_id) from flower_user",5);
		if (uid == 0){
			%><table width="100%" style="border: none;">
				<tr>
					<td style="border: none;" width="11%"><form method = "post" action = "flowerOperation.jsp?b=1" >
							用户ID:<input type="text" name="uid"/>
							<input type = "submit" value ="提交"/>
					</form></td>
					<td style="border: none;" width="30%"><form method = "post" action="flowerProperty.jsp">
					  	<input type = "submit" value ="更改属性表"/>
					</form></td>
				  </tr>
			  </table>
			  目前总用户数：<font color="red"><%=userCount %></font><br/>			  
			<%
		} else {
			UserBean user = UserInfoUtil.getUser(uid);
			long compTime = 0;
			String tip = "";
			%><form action="flowerIndex.jsp?uid=<%=user.getId()%>" method="post">
					用户ID:<%=uid%><br/>
					用户名:<a href="../user/queryUserInfo.jsp?id=<%=uid %>"><%=user.getNickNameWml()%></a><br/>
					成就值:<font color=blue><%=fub.getExp() - fub.getUsedExp()%></font>&nbsp;<input type="text" name="exp"/>
					<input type = "submit" value ="添加成就值" onclick="return confirm('确认增加成就值？')"/><br/>
					<% DishBean dish = FlowerUtil.getUserDish(uid);
					   if (dish.getTime() != -28800000){
					   		// 正在合成
					   		if (dish.getState() == 0){
					   			compTime = FlowerUtil.COMP_FAIL_TIME - (System.currentTimeMillis() - dish.getTime());
					   			if (compTime > 0){
					   				tip = "该用户正在合成实验中，但是实验失败。合成实验还有" + DateUtil.formatTimeInterval3(compTime) + "(" + compTime + " 毫秒)结束。";
					   			} else {
					   				tip = "该用户合成试验已结束，实验失败。";
					   			}
					   		} else {
					   			compTime = FlowerUtil.getFlower(dish.getState()).getCompTime() - (System
											.currentTimeMillis() - dish.getTime());
								if (compTime > 0){
					   				tip = "该用户正在合成实验中，实验成功。将合成出" + FlowerUtil.getFlowerName(dish.getState()) + "。合成实验还有" + DateUtil.formatTimeInterval3(compTime) + "(" + compTime + " 毫秒)结束。";
					   			} else {
					   				tip = "该用户合成试验已结束，成功合成出" + FlowerUtil.getFlowerName(dish.getState());
					   			}
					   		}
					   } else {
					   		tip = "用户未进行合成试验。";
					   }
					%><%=tip%><br/>
					用户的养殖地：<br/>
					<%List fieldList = service.getField(uid);
					  List al = FlowerUtil.getFieldTypeList();
					  FieldBean fb = null;
					  tip = "";
					  if (fieldList.size() != 0){
					  		for (int i = 0;i< fieldList.size();i++){
					  			fb = (FieldBean)fieldList.get(i);
					  			tip += al.get(fb.getType());
					  			if (fb.getField() == 0){
					  				tip += "-空";
					  			} else {
					  				int growTime = action.abloomTime(fb.getCreateTime() / 1000,fb.getField(),fb.getType());
					  				if (growTime >= 0){
					  					tip += "-" + FlowerUtil.getFlowerName(fb.getField()) + " 剩" + DateUtil.formatTimeInterval3(growTime) + "(" + growTime + "秒)开花";
					  				} else {
					  					tip += "-" + FlowerUtil.getFlowerName(fb.getField()) + " 已开";
					  				}
					  			}
					  			%><%=tip%><br/><%
					  			tip = "";
					  		}
					  }%>
					<% List stealList = fub.getStealList();%>
					今天他的地已被踩过<%=stealList.size() %>次，踩了<%=fub.getStealCount()%>位好友的地。<br/>
					操&nbsp;&nbsp;作:<a href = "flowerDataList.jsp?b=2&uid=<%=uid%>">查看所有种子</a> 
					  <a href = "flowerDataList.jsp?b=3&uid=<%=uid%>">查看所有花朵</a>
					  <a href = "flowerIndex.jsp?uid=<%=uid%>&type=1">增加一种种子</a>
					  <a href = "flowerIndex.jsp?uid=<%=uid%>&type=2">增加一种花</a>
					  |<a href ="flowerIndex.jsp">重新查找</a> 
			  </form><%
		}
	} else if (type == 1){
		%><table width="100%" style="border: none;">
			<tr><td style="border: none;" width="30%"><form method = "post" action="flowerIndex.jsp?uid=<%=uid %>">
				  	  <input type = "submit" value ="<--返回首页"/>
				</form></td></tr>
			<tr><td style="border: none;" width="11%">
				  <form action="flowerMess.jsp?uid=<%=uid%>&mode=1&b=2&ope=1" method="post">
						种子类型:<select name="fid">
						<%for(int i=1;i<flowerTypeList.size()-4;i++){
							%><option value=<%=i%>><%=flowerTypeList.get(i)%></option><%
						  }%>
						</select><br/>
						种子数量:<input type="text" name="count"><br/>
						<input type="submit" value="增加">
				  </form>				
			</td></tr>
		   </table><%
	} else if (type == 2){
			%><table width="100%" style="border: none;">
				<tr><td style="border: none;" width="30%">
					<form method = "post" action="flowerIndex.jsp?uid=<%=uid %>">
					  	  <input type = "submit" value ="<--返回首页"/>
					</form></td></tr>
				<tr><td style="border: none;" width="11%">
						<form action="flowerMess.jsp?uid=<%=uid%>&mode=2&b=3&ope=1" method="post">
							鲜花类型:<select name="fid">
							<%for(int i=1;i<flowerTypeList.size();i++){
								%><option value=<%=i%>><%=flowerTypeList.get(i)%></option><%
							  }%>
							</select><br/>
							鲜花数量:<input type="text" name="count"><br/>
							<input type="submit" value="增加">
					    </form>			
				</td></tr>
		   </table><%
	}		  
%><table width="100%" style="border: none;">
				<tr>
					<td style="border: none;" width="11%"><form method = "post" action = "flowerOperation.jsp?b=1" >
							用户ID:<input type="text" name="uid"/>
							<input type = "submit" value ="查询"/>
					</form></td>
				  </tr>
			  </table></body>
</html>