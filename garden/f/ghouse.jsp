<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static final int COUNT_PRE_PAGE=5; %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	ArrayList list1 = new ArrayList();
	ArrayList list2 = new ArrayList();
	ArrayList list3 = new ArrayList();
	ArrayList list4 = new ArrayList();
	ArrayList list5 = new ArrayList();
	int type = action.getParameterInt("t");				//t=1 从实验室转来；t=2 从底部链接转来 t=3 从花市传来的；
	int cultureDish = action.getParameterInt("c");		//培养皿编号
														//统计已有的鲜花数量与品种
	List statList = action.flowerStat(action.getLoginUserId());	
	if (statList.size() > 0){
		list1 = (ArrayList)statList.get(0);				//普通鲜花
		list2 = (ArrayList)statList.get(1);				//精品鲜花
		list3 = (ArrayList)statList.get(2);				//珍贵鲜花
		list4 = (ArrayList)statList.get(3);				//稀有鲜花
		list5 = (ArrayList)statList.get(4);				//特殊物品
	}
	if ( cultureDish > 4 ){								//检查非法参数
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}	
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
我的花房<br/>
<%if(statList.size() == 0){    //仓库为空
	%>花房里没有任何鲜花.<br/><a href="shop.jsp">>>购买花种</a><br/><a href="lab.jsp">>>返回实验室</a><br/><%
}else{
	if ( type == 1 || type == 3 ){
		%>选择你想要的鲜花:<br/><%
	}else{
		%>查看已拥有的鲜花:<br/><%
	}
	FlowerStoreBean fsb=null;
	if (type != 3){		//花市中不用显示普通的花
		if(list1.size() !=0 ){
			%>【普通】:<%
			for(int i=0;i<list1.size();i++){
				fsb=(FlowerStoreBean)list1.get(i);
				if( type == 1){							 	//如果是从实验室内转来的话...
					%><a href="lab.jsp?c=<%=cultureDish%>&amp;fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
				} else if ( type == 3){						//从花市转来
					%><a href="market1.jsp?fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
				} else {
					%><%=flowerTypeList.get(fsb.getFlowerId())%>(<%=fsb.getCount()%>)<%
				}
			}%><br/><%
		}
	}
	if(list2.size() != 0 ){
		%>【精品】:<%
		for(int i=0;i<list2.size();i++){
			fsb=(FlowerStoreBean)list2.get(i);
				if( type == 1){							 	//如果是从实验室内转来的话...
					%><a href="lab.jsp?c=<%=cultureDish%>&amp;fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
				} else if ( type == 3){						//从花市转来
					%><a href="market2.jsp?fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
				} else {
					%><%=flowerTypeList.get(fsb.getFlowerId())%>(<%=fsb.getCount()%>)<%
				}
		}%><br/><%
	}
	if(list3.size() != 0 ){
		%>【珍贵】:<%
		for(int i=0;i<list3.size();i++){
			fsb=(FlowerStoreBean)list3.get(i);
				if( type == 1){							 	//如果是从实验室内转来的话...
					%><a href="lab.jsp?c=<%=cultureDish%>&amp;fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
				} else if ( type == 3){						//从花市转来
					%><a href="market2.jsp?fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
				} else {
					%><%=flowerTypeList.get(fsb.getFlowerId())%>(<%=fsb.getCount()%>)<%
				}
		}%><br/><%
	}
	if(list4.size() != 0 ){
		%>【稀有】:<%
		for(int i=0;i<list4.size();i++){
			fsb=(FlowerStoreBean)list4.get(i);
			if( type == 1){							 	//如果是从实验室内转来的话...
				%><a href="lab.jsp?c=<%=cultureDish%>&amp;fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
			} else if ( type == 3){						//从花市转来
				%><a href="market2.jsp?fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
			} else {
				%><%=flowerTypeList.get(fsb.getFlowerId())%>(<%=fsb.getCount()%>)<%
			}
		}%><br/><%
	}
	if(type == 3){			// 只有从花市中访问时才显示特殊物品
		if(list5.size() != 0 ){
			%>【特殊】:<%
			for(int i=0;i<list5.size();i++){
				fsb=(FlowerStoreBean)list5.get(i);
				if( type == 1){							 	//如果是从实验室内转来的话...
					%><a href="lab.jsp?c=<%=cultureDish%>&amp;fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
				} else if ( type == 3){						//从花市转来
					%><a href="market2.jsp?fid=<%=fsb.getFlowerId()%>"><%=flowerTypeList.get(fsb.getFlowerId())%></a>(<%=fsb.getCount()%>)<%
				}
			}
		}%><br/><%		
	}
	if ( type == 1 ){
		%><a href="lab.jsp">返回实验室</a><br/><%
	} else if ( type == 3 ){
		%>返回花市<br/><%
	} else {
		%><a href="lab.jsp">进入实验室合成</a><br/><%
	}
}%>
花市|花房|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a>|<a href="store.jsp">仓库</a><br/>
<a href="result.jsp?t=1">黄金成果</a>|购买鲜花<br/><a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>