<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
MapNodeBean node = action.getUserNode();
MapBean map = action.world.getMap(node.getMapId());
int id = action.getParameterInt("id");
FarmUserBean user = null;
if(id > 0)
	user = action.world.getFarmUserCache(id);
else
	user = farmUser;
TongBean tong = null;
if(user.getTongUser()!=null)
	tong = FarmTongWorld.getTong(user.getTongUser().getTongId());
BattleStatus bs = user.getCurStat();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(user != null){%>
玩家:<%=user.getNameWml()%>[人类]<%if(user==farmUser&&user.getName().length()==0){%><a href="set.jsp">修改</a><%}%><br/>
等级:<%=user.getRank()%>/<%=user.getMaxRank()%><br/>
职业:<%=user.getClass1Name()%>|门派:<%if(tong==null){%>(无)<%}else{%><a href="../tong/tong.jsp?id=<%=tong.getId()%>"><%=tong.getNameWml()%></a><%}%><br/>
<%UserHonorBean userHonor = FarmWorld.getUserHonor(user.getUserId(),map);
if(userHonor!=null){%>
荣誉:<%=userHonor.getHonorWeek()%>(<%=map.getName()%>)<br/><%}%>
<%if(farmUser==user){%>
经验:<%=user.getExp()%>/<%=user.getUpgradeExp()%><br/>
财产:<%=FarmWorld.formatMoney(user.getMoney())%><br/>
==人物基本属性==<br/>
战斗等级:<%=user.getProRank(FarmProBean.PRO_BATTLE)%><br/>
<%if(farmUser.getPro(FarmProBean.PRO_BATTLE)!=null){%>
血:<%=user.hp%>/<%=bs.hp%><br/>
气力:<%=user.mp%>/<%=bs.mp%><br/>
体力:<%=user.sp%>/<%=bs.sp%><br/>
攻击:<%=bs.attack1%>-<%=bs.attack1+bs.attack1Float%><br/>
防御:<%=bs.defense1%><br/>
<a href="infos.jsp">详细属性</a><br/>
<%}%>
<%}else{%>
<%if(farmUser.getTongUser()!=null){%>
<%if(tong==null){
if(farmUser.getTongUser().getDuty()>=8&&user.getInviteTongUser()==0){%>
<a href="../tong/invite.jsp?id=<%=id%>">邀请加入本门派</a><br/>
<%}%>
<%}else{
if(farmUser.getTongUser().getDuty()>=8&&user.getTongUser().getDuty()<8){%>
<a href="../tong/expel.jsp?id=<%=id%>">开除出本门派</a><br/>
<%}%>
<%}%>
<%}%>

<%if(user.isDead()){%>(已死亡)<br/>
<%}else if(user.getPos()==farmUser.getPos()){%>
<a href="info2.jsp?id=<%=id%>">仔细查看</a>
<%if(map.isFlagArena()){%>|<a href="../cb/cb.jsp?a=4&amp;id=<%=id%>">攻击</a><%}
else if(!FarmWorld.isGroup(farmUser,user)&&!map.isFlagPeace()){%>|<a href="../cb/cb.jsp?a=4&amp;id=<%=id%>">强行攻击</a><%}%>
<br/>
<%}%>
<a href="/chat/post.jsp?toUserId=<%=id%>">聊天</a>
<% if(FarmWorld.getGroupUser(user.getUserId())==null){%>
|<a href="../group/invite.jsp?id=<%=id%>">邀请组队</a><%}%><br/>
<%}%>

<%}else{%>
该人物不存在或者不在线<br/>
<%}%>

<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>