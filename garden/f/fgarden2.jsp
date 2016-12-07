<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.spec.garden.*,net.joycool.wap.cache.*,net.joycool.wap.service.factory.*,
	net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	net.joycool.wap.service.infc.IUserService userService = ServiceFactory.createUserService();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	List list1 = new ArrayList();
	List list2 = new ArrayList();
	List list3 = new ArrayList();
	List list4 = new ArrayList();
	FlowerAction action = new FlowerAction(request);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	int seedId = action.getParameterInt("s");
	int friendUid = action.getParameterInt("fuid");
	UserBean user = UserInfoUtil.getUser(friendUid);
	FieldBean fb = null;
	FlowerUserBean fub = FlowerUtil.getUserBean(friendUid);
	if (fub == null){
		action.sendRedirect("mess.jsp?e=11",response);
		return;
	}
	//用户钱<20000并且没土地的，不让玩
	if (action.getGold(20000) < 0 && fub == null){		//fub和fb是连带关系的，有fub则一定有fb
		action.sendRedirect("mess.jsp?e=5",response);
		return;
	}
	boolean isABadGuys = userService.isUserBadGuy(friendUid,action.getLoginUserId());
	if (isABadGuys){
		//已被对方加至黑名单，无法访问他的花园。
		action.sendRedirect("mess.jsp?e=9",response);
		return;
	}
	//取得花地的初始化信息
	List fieldList = service.getField(friendUid);
	List al = FlowerUtil.getFieldTypeList();
	int fieldType = 0;	//养殖地类型
	//如果找不到该用户的花园信息，则跳至报错页面
	if ( fieldList.size() == 0 ){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	int composeCount = SqlUtil.getIntResult("select count(id) from flower_compose where user_id = " + friendUid,5);
	list1 = action.flowerCompose(1,friendUid);
	list2 = action.flowerCompose(2,friendUid);
	list3 = action.flowerCompose(3,friendUid);
	list4 = action.flowerCompose(4,friendUid);
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<a href="/user/ViewUserInfo.do?userId=<%=friendUid%>"><%=user.getNickNameWml()%></a>的花园<br/>
他所获得的成就值<%=fub.getExp() - fub.getUsedExp()%>点<br/>
<%int growTime = 0;
	for (int i=0;i<fieldList.size();i++){
		fb = (FieldBean)fieldList.get(i);
		// 取得地的初使化信息
		fb = action.friendGardenInit(fb);
		if (fb.getField() == 0){
			%>-<%=al.get(fb.getType())%>:空<br/><%
		} else {
			growTime = action.abloomTime(fb.getCreateTime() / 1000,fb.getField(),fb.getType());
			if (growTime <= 0){		//已开花
				%>-<%=al.get(fb.getType())%>:<%=FlowerUtil.getFlowerName(fb.getField())%><br/><%
			} else {
				%>-<%=al.get(fb.getType())%>:<%=FlowerUtil.getFlowerName(fb.getField())%>剩<%=DateUtil.formatTimeInterval3(growTime) %>开花<a href="steal.jsp?fuid=<%=friendUid%>&amp;id=<%=fb.getId()%>">我踩</a><br/><%
			}
		}
	}%>
<%	FlowerComposeBean fcb=null;
	int flowerId = 0;
	if (composeCount == 0){
		%>他的黄金成果:无<br/><%
	} else {
		%>他的黄金成果:<br/><%
		if(list1.size() != 0 ){
			%>【精品】:<%
			for(int i=0;i<list1.size();i++){
				flowerId=StringUtil.toInt(String.valueOf(list1.get(i)));
				%><%=FlowerUtil.getFlowerName(flowerId)%>&nbsp;<%
			}%><br/><%
		}
		if(list2.size() != 0 ){
			%>【珍贵】:<%
			for(int i=0;i<list2.size();i++){
				flowerId=StringUtil.toInt(String.valueOf(list2.get(i)));
				%><%=FlowerUtil.getFlowerName(flowerId)%>&nbsp;<%
			}%><br/><%
		}
		if(list3.size() != 0 ){
			%>【稀有】:<%
			for(int i=0;i<list3.size();i++){
				flowerId=StringUtil.toInt(String.valueOf(list3.get(i)));
				%><%=FlowerUtil.getFlowerName(flowerId)%>&nbsp;<%
			}%><br/><%
		}
		if(list4.size() != 0 ){
			%>【稀有】:<%
			for(int i=0;i<list4.size();i++){
				flowerId=StringUtil.toInt(String.valueOf(list4.get(i)));
				%><%=FlowerUtil.getFlowerName(flowerId)%>&nbsp;<%
			}%><br/><%
		}
	}%>
<%--查看他的成就[10]<br/>--%>
<a href="friend.jsp">返回好友列表</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>