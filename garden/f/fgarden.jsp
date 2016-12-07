<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.spec.garden.*,net.joycool.wap.cache.*,
	net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	int seedId = action.getParameterInt("s");
	int fieldOrder = action.getParameterInt("o");
	int loginUid = action.getLoginUserId();
	int taskCount = FlowerUtil.getTaskMap().size();
	FieldBean fb = null;
	FlowerUserBean fub = FlowerUtil.getUserBean(loginUid);
	//看地是不是自己的
	FieldBean fb2 = service.getFieldById(fieldOrder);
	if (fb2 != null){
		if (fieldOrder != 0 && fb2.getUserId() != loginUid){
			action.sendRedirect("mess.jsp?e=12",response);
			return;
		}
	}
	
	//用户钱<20000并且没土地的，不让玩
	if (action.getGold(20000) < 0 && fub == null){		//fub和fb是连带关系的，有fub则一定有fb
		action.sendRedirect("mess.jsp?e=5",response);
		return;
	}
	if (seedId != 0){
		if ( seedId < 0 || seedId > flowerTypeList.size() || FlowerUtil.getFlower(seedId).getType() == 5 ){
			action.sendRedirect("mess.jsp?e=3",response);
			return;
		} 
	}

	//取得花地的初始化信息
	action.gardenInit(seedId,fieldOrder);
	List fieldList = service.getField(loginUid);
	List al = FlowerUtil.getFieldTypeList();
	int fieldType = 0;	//养殖地类型
	//如果找不到该用户的花园信息，则跳至买地页面
	if ( fieldList.size() == 0 ){
		action.sendRedirect("mess.jsp?e=1",response);
		return;
	}
	List logList = service.getMessageList("user_id = " + loginUid + " and type = 0 order by id desc limit 1");
	FlowerMessageBean fm = null;
	String log = "无";
	if (logList.size() !=0 ){
		fm = (FlowerMessageBean)logList.get(0);
		UserBean user = UserInfoUtil.getUser(fm.getFromId());
		// 只显示三分钟内最新的消息
		if (!DateUtil.timepast(fm.getCreateTime(),180)){
			log = "<a href=\"/user/ViewUserInfo.do?userId=" + fm.getFromId() + "\">" + user.getNickNameWml() + "</a>来你" + fm.getContent();
		}
	}%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
-<a href="log.jsp">消息</a>:<%=log%><br/>
<%if (fub.getTaskNum() <= taskCount){
	%><a href="task.jsp">**新手任务**</a><br/><%
  }%>
您有现金:<%=action.getGold()%>|成就值:<%=fub.getExp() - fub.getUsedExp() %><br/>
<% 	int growTime = 0;
	for (int i=0;i<fieldList.size();i++){
		fb = (FieldBean)fieldList.get(i);
		if (fb.getField() == 0){
			%>-<%if (fb.getType() < 4){
				%><a href="update.jsp?id=<%=fb.getId()%>"><%=al.get(fb.getType())%></a>:<%
			}else{
				%><%=al.get(fb.getType())%>:<%
			}%>空<a href="bag.jsp?f=1&amp;o=<%=fb.getId()%>">种植</a><br/><%
		} else {
			growTime = action.abloomTime(fb.getCreateTime() / 1000,fb.getField(),fb.getType());
			if (growTime <= 0){		//已开花
				%>-<%if (fb.getType() < 4){
						%><a href="update.jsp?id=<%=fb.getId()%>"><%=al.get(fb.getType())%></a>:<%
					}else{
						%><%=al.get(fb.getType())%>:<%
					}%><%=FlowerUtil.getFlowerName(fb.getField())%><a href="pick.jsp?o=<%=fb.getId()%>">采摘</a><br/><%
			} else {
				%>-<%if (fb.getType() < 4){
						%><a href="update.jsp?id=<%=fb.getId()%>"><%=al.get(fb.getType())%></a>:<%
					}else{
						%><%=al.get(fb.getType())%>:<%
					}%><%=FlowerUtil.getFlowerName(fb.getField())%>剩<%=DateUtil.formatTimeInterval3(growTime) %>开花<br/><%
			}
		}
   }
   if (fieldList.size() == 1){
   		if (fb.getField() == 0){
   			%>不要让你的土地空闲着,鲜花的养殖是很宝贵的!<br/><%
   		} else {
   			%>记得花种好后要及时来采摘,否则就会枯萎哦!<br/><%
   		}
   }%>
   <% if (fieldList.size() < FlowerUtil.MAX_FIELD_COUNT){
   			%><a href="addField.jsp">+扩充土地+</a><br/><%
   	  }%>
【<a href="lab.jsp">实验室</a>】可以合成出高级花种<br/>
【购买鲜花】直接购买所需的鲜花<br/>
【<a href="result.jsp?t=1">黄金成果</a>】记录下所有我的实验成果<br/>
【<a href="fieldLog.jsp">土地日志</a>】土地的更新记录<br/>
【<a href="friend.jsp">拜访好友</a>】去给你的好友捣捣乱~<br/>
【<a href="invite.jsp">邀请好友</a>】邀请好友开通花园<br/>
花市|<a href="ghouse.jsp">花房</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a>|<a href="store.jsp">仓库</a><br/>
<a href="index.jsp">返回花之境首页</a><br/>
<a href="../island.jsp">返回采集岛</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>