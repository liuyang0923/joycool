<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	int listCount1 = 0;
	int listCount2 = 0;
	int listCount3 = 0;
	int listCount4 = 0;
	int loginUid = action.getLoginUserId();
	int cultureDish = action.getParameterInt("c");			//培养皿编号
	int flowerId = action.getParameterInt("fid");			//传过来的鲜花编号
	DishBean dish = FlowerUtil.getUserDish(loginUid);		//获取用户培养皿
	int d = action.getParameterInt("d");					//要删除的培养皿编号
															//用户所持有的特殊物品
	List specList = service.getSeeds(" user_id = " + action.getLoginUserId() + " and type = 3");
	
	//检测非法参数
	if (flowerId != 0){										//如果花的ID不为0				
		if ( flowerId > flowerTypeList.size() - 1 ||        //花编号是否过大
			!action.isContainFlower(flowerId) ||			//用户是不是真有这朵花
			FlowerUtil.getFlower(flowerId).getType()==5 ){	//是不是准许种的花
			action.sendRedirect("mess.jsp?e=3",response);
			return;
		}
	} else if (cultureDish != 0){							//如果花ID等于0，但培养皿编号却不等于0
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}			
	if ((cultureDish < 1 || cultureDish > 4) && flowerId != 0){
		action.sendRedirect("mess.jsp?e=5",response);
		return;
	}
							
	if ( d > 0 && d <= 4 ){
		if (dish.getTime() != -28800000 && action.dishToList(dish).size() != 0){
			// 用户正在合成，无法取消。
			action.sendRedirect("mess.jsp?e=15",response);
			return;
		}
		action.deleteFromDish(d,dish);						//删除培养皿中的花
		if ( d == 1 ){										//刷新培养皿Bean
			dish.setCultureDish1(0);
		} else if ( d == 2 ){
			dish.setCultureDish2(0);
		} else if ( d == 3 ){
			dish.setCultureDish3(0);
		} else if ( d == 4 ){
			dish.setCultureDish4(0);
		}
//		// 培养皿为空时重置时间
//		if (action.dishToList(dish).size() == 0){
//			dish.setTime(-28800000);
//		}						
	}// else {
//		action.sendRedirect("mess.jsp?e=3",response);
//		return;		
//	}
	
	//==========一定要在上面先删除需要删除的花，再做统计，才准确==========
	List statList = action.flowerStat(loginUid);			//统计已有的鲜花数量与品种
	if (statList.size() == 0 && action.dishToList(dish).size()== 0){	//如果花房里没花...
		action.sendRedirect("mess.jsp?e=4",response);		//没种过花的不许进入实验室
		return;
	}
	
	if ( statList.size() != 0 ){
		listCount1 = action.getFlowerCount((ArrayList)statList.get(0));	//普通鲜花的数量
		listCount2 = action.getFlowerCount((ArrayList)statList.get(1));	//精品.. .. ..
		listCount3 = action.getFlowerCount((ArrayList)statList.get(2));	//珍贵.. .. ..
		listCount4 = action.getFlowerCount((ArrayList)statList.get(3));	//稀有.. .. ..	
	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
实验室<br/>
在这里你可以配制出新的花种<br/>
【我的试验台】<br/>
<%  if (dish.getTime() != -28800000 && action.dishToList(dish).size() != 0){ 	//"-28800000"="1970-01-01 00:00:00"
		long compTime = 0;
		if (dish.getState() == 0){
			compTime = FlowerUtil.COMP_FAIL_TIME - (System.currentTimeMillis() - dish.getTime());
		} else {
			compTime = FlowerUtil.getFlower(dish.getState()).getCompTime() - (System
					.currentTimeMillis() - dish.getTime());
		}
		if (compTime <= 0){
			%><a href="comp.jsp?c=1">合成实验结束</a><%
		} else {
			%>正在合成实验中,<%=DateUtil.formatTimeInterval3(compTime)%>后完成.<%
			if (specList.size() != 0 && !dish.isSpecGoodsUsed()){
				%><a href="bag.jsp?f=2">使用特殊物品</a><%
			}%><%
		}
   }else{
%>培养皿1:<%
	if (cultureDish == 1){
		if (dish.getCultureDish1() != flowerId){				//不可以放入相同的花
			if (dish.getCultureDish1() != 0){
				action.deleteFromDish(1,dish);					//将培养皿中原来的花加回
			}
			dish.setCultureDish1(flowerId);
			service.descFlower(loginUid, flowerId,1);			//仓库中花的数量 -1
			service.updateDish(cultureDish,flowerId,loginUid);
			if (flowerId <= 7){									//从相应的列表中的数量-1
				listCount1 --;
			} else if (flowerId >= 8 && flowerId <= 11){
				listCount2 --;
			} else if (flowerId >= 12 && flowerId <= 15){
				listCount3 --;
			} else if (flowerId >= 16 && flowerId <= 20){
				listCount4 --;
			} 										
		}%><a href="pro.jsp?b=3&amp;p=<%=flowerId%>"><%=flowerTypeList.get(flowerId)%></a>&nbsp; <a href="lab.jsp?d=1">取消</a><%
	} else {													//显示培养皿原来的花
		if ( dish.getCultureDish1() == 0){
			%><a href="ghouse.jsp?t=1&amp;c=1">放入鲜花</a><%
		} else {
			%><a href="pro.jsp?b=3&amp;p=<%=dish.getCultureDish1()%>"><%=flowerTypeList.get(dish.getCultureDish1())%></a>&nbsp; <a href="lab.jsp?d=1">取消</a><%
		}
	}%><br/>
	培养皿2:<%
	if (cultureDish == 2){
		if (dish.getCultureDish2() != flowerId){		
			if (dish.getCultureDish2() != 0){
				action.deleteFromDish(2,dish);					//将培养皿中原来的花加回
			}
			dish.setCultureDish2(flowerId);
			service.descFlower(loginUid, flowerId,1);
			service.updateDish(cultureDish,flowerId,loginUid);
			if (flowerId <= 7){									//从相应的列表中的数量-1
				listCount1 --;
			} else if (flowerId >= 8 && flowerId <= 11){
				listCount2 --;
			} else if (flowerId >= 12 && flowerId <= 15){
				listCount3 --;
			} else if (flowerId >= 16 && flowerId <= 20){
				listCount4 --;
			} 
		}%><a href="pro.jsp?b=3&amp;p=<%=flowerId%>"><%=flowerTypeList.get(flowerId)%></a>&nbsp; <a href="lab.jsp?d=2">取消</a><%
	} else {
		if ( dish.getCultureDish2() == 0){
			%><a href="ghouse.jsp?t=1&amp;c=2">放入鲜花</a><%
		} else {
			%><a href="pro.jsp?b=3&amp;p=<%=dish.getCultureDish2()%>"><%=flowerTypeList.get(dish.getCultureDish2())%></a>&nbsp; <a href="lab.jsp?d=2">取消</a><%
		}
	}%><br/>
	培养皿3:<%
	if (cultureDish == 3){
		if (dish.getCultureDish3() != flowerId){		
			if (dish.getCultureDish3() != 0){
				action.deleteFromDish(3,dish);					//将培养皿中原来的花加回
			}
			dish.setCultureDish3(flowerId);
			service.descFlower(loginUid, flowerId,1);
			service.updateDish(cultureDish,flowerId,loginUid);
			if (flowerId <= 7){									//从相应的列表中的数量-1
				listCount1 --;
			} else if (flowerId >= 8 && flowerId <= 11){
				listCount2 --;
			} else if (flowerId >= 12 && flowerId <= 15){
				listCount3 --;
			} else if (flowerId >= 16 && flowerId <= 20){
				listCount4 --;
			} 
		}%><a href="pro.jsp?b=3&amp;p=<%=flowerId%>"><%=flowerTypeList.get(flowerId)%></a>&nbsp; <a href="lab.jsp?d=3">取消</a><%
	} else {
		if ( dish.getCultureDish3() == 0){
			%><a href="ghouse.jsp?t=1&amp;c=3">放入鲜花</a><%
		} else {
			%><a href="pro.jsp?b=3&amp;p=<%=dish.getCultureDish3()%>"><%=flowerTypeList.get(dish.getCultureDish3())%></a>&nbsp; <a href="lab.jsp?d=3">取消</a><%
		}
	}%><br/>
	培养皿4:<%
	if (cultureDish == 4){
		if (dish.getCultureDish4() != flowerId){	
			if (dish.getCultureDish4() != 0){
				action.deleteFromDish(4,dish);					//将培养皿中原来的花加回
			}
			dish.setCultureDish4(flowerId);
			service.descFlower(loginUid, flowerId,1);
			service.updateDish(cultureDish,flowerId,loginUid);
			if (flowerId <= 7){									//从相应的列表中的数量-1
				listCount1 --;
			} else if (flowerId >= 8 && flowerId <= 11){
				listCount2 --;
			} else if (flowerId >= 12 && flowerId <= 15){
				listCount3 --;
			} else if (flowerId >= 16 && flowerId <= 20){
				listCount4 --;
			} 
		}%><a href="pro.jsp?b=3&amp;p=<%=flowerId%>"><%=flowerTypeList.get(flowerId)%></a>&nbsp; <a href="lab.jsp?d=4">取消</a><%
	} else {
		if ( dish.getCultureDish4() == 0){
			%><a href="ghouse.jsp?t=1&amp;c=4">放入鲜花</a><%
		} else {
			%><a href="pro.jsp?b=3&amp;p=<%=dish.getCultureDish4()%>"><%=flowerTypeList.get(dish.getCultureDish4())%></a>&nbsp; <a href="lab.jsp?d=4">取消</a><%
		}
	}
	%><br/><a href="validate.jsp">>>开始合成</a><%
}
%><br/>
<a href="ghouse.jsp">我的花房</a>展示我所拥有的所有鲜花<br/>
普通(<%=listCount1 %>)|精品(<%=listCount2 %>)<br/>
珍贵(<%=listCount3 %>)|稀有(<%=listCount4 %>)<br/>
<a href="result.jsp?t=1">黄金成果</a>:记录下所有我的实验成果<br/>
<a href="lablog.jsp">合成日志</a>:记录下所有我实验结果<br/>
购买鲜花:直接购买所需的鲜花<br/>
花市|<a href="ghouse.jsp">花房</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a>|<a href="store.jsp">仓库</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<a href="index.jsp">返回花之境首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>