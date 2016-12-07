<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 10;%>
<%YardAction action=new YardAction(request);
int tmp = 0;
int did = 0;
int cancel = 0;
String tip = "";
boolean isThisFamily = false;
int id = action.getFmId();
int fmId = action.getFmId();
if(fmId > 0){
	if (id == fmId)
		isThisFamily = true;
}else{
	response.sendRedirect("/fm/index.jsp");
	return;
}
CookBean2 cook = null;
List cookList = null;	// 做法步骤
List deployList = null;	// 配菜列表
YardDeployBean deploy = null;
YardDeployingBean deploying = null;
UserBean user = action.getLoginUser();
YardBean yard = action.getYardByID(id);
YardRecipeProtoBean recipe = null;
if (!yard.canEnterKitchen()){
	tip = "餐厅等级不够,无法进入.";
} else {
	if (!action.getFmUser().isflagYard()){
		tip = "只有大厨才能进入中级厨房哦.";
	} else {
		int recipId = action.getParameterInt("rid");
		if (recipId <= 0){
			tip = "您选择的食谱有误.";
		} else {
			recipe = YardAction.getRecipeProto(recipId);
			if (recipe == null){
				tip = "您没有这本食谱或食谱不存在.";
			} else {
				// 看这个家族有没有配置这道菜的材料
				did = action.getParameterInt("did");
				deploy = YardAction.yardService.getYardDeployBean(" fmid=" + fmId + " and id=" + did);
				if (deploy == null)
					deploy = YardAction.yardService.getYardDeployBean(" fmid=" + fmId + " and proto_id=" + recipe.getId() + " and inuse=0");
				if (deploy == null){
					tip =  "不好意思,配菜师傅还没有准备这道菜的配料.";
				} else {
					// 开始时间<0说明时间还是默认的1970年。就从第一次进入这个页面的时间点开始记时。
					if (deploy.getStartTime() < 0){
						deploy.setStartTime(System.currentTimeMillis());
						SqlUtil.executeUpdate("update fm_yard_deploy set start_time=now() where id=" + deploy.getId(),5);
					}
					// boolean isMyDeploy = false;
					HashSet hs = YardAction.yardService.getFmDeploying(fmId);
					deploying = YardAction.yardService.getYardDeployingBean(" deploy_id=" + deploy.getId());
					if (deploying == null){
						// isMyDeploy = true;
						if (hs.size() >= yard.getKitchenCounts3(yard.getKitchenRank3())){
							tip = "同时做的菜已太上限.";
						}
					}
					// else if (deploying.getUid() == user.getId()){
					//	    isMyDeploy = true;
					// }
					// if (!isMyDeploy){
					//  	tip = "这道菜正在被别人做.";
					// } else {
						cancel = action.getParameterInt("c");
						if (cancel == 1){
							tip = "放弃后,此份菜肴将被倒进垃圾桶.真的要取消吗?";
						} else if (cancel == 2){
							yard.cancel(deploy,deploying);
							tip = "菜肴被倒掉了!";
						} else {
							// 配好的菜的列表
							deployList = deploy.getMaterialList();
							// 做法步骤
							cookList = YardAction.yardService.getCookBeanList2(" recpie_id=" + recipe.getId() + " order by step");
							int in = action.getParameterInt("in");
							if (in > 0){
								int result = yard.useMaterial(request,cookList,in,deploy,deploying,user);
								if (result != 1){
									tip = (String)request.getAttribute("tip");
								}
							}
						}
					// }
			 	}
			 }
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="中级厨房"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
cook = (CookBean2)cookList.get(deploy.getStep() - 1);
if (cook != null){
%><%=recipe.getName()%><br/>已用时:<%=action.getCookingTimeStr(System.currentTimeMillis() - deploy.getStartTime())%><a href="kitchen5.jsp?id=<%=fmId%>&amp;rid=<%=recipe.getId()%>&amp;did=<%=deploy.getId()%>">刷新</a><br/>(<%=deploy.getStep()%>/<%=cookList.size()%>)<%=cook.getContent()%><br/>
<a href="kitchen6.jsp?id=<%=fmId%>&amp;did=<%=deploy.getId()%>&amp;did=<%=deploy.getId()%>">+放材料+</a><br/>
<a href="kitchen5.jsp?id=<%=fmId%>&amp;did=<%=deploy.getId()%>&amp;rid=<%=recipe.getId()%>&amp;c=1&amp;did=<%=deploy.getId()%>">放弃烹饪</a><br/>
<%
}
} else {
%><%=tip%><br/><%
if (cancel == 1){
%><a href="kitchen5.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>&amp;c=2&amp;did=<%=deploy.getId()%>">确定取消</a><br/>
<a href="kitchen5.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>">再想想</a><br/>
<%
} else if (recipe != null){
%><a href="kitchen5.jsp?id=<%=fmId%>&amp;rid=<%=recipe.getId()%>&amp;did=<%=did%>">返回<%=recipe.getName()%></a><br/><%
}
}%>
<a href="kitchen4.jsp?id=<%=id%>">返回中级厨房</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>