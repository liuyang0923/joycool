<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.bank.StoreBean"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.IBankService"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.job.HuntAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
static IBankService bankService=ServiceFactory.createBankService();%><%
response.setHeader("Cache-Control", "no-cache");

String quarryIdS=(String)session.getAttribute("quarryId");
String quarry = request.getParameter("quarryId");
HuntAction action = new HuntAction(request);
UserBean loginUser = action.getLoginUser();

Tiny2Action action2 = new Tiny2Action(request, response);
if(action2.checkGame()) return;
int[] count = HuntAction.countMap.getCount(loginUser.getId());
if(action2.getGame() == null){
	if(loginUser.getId() >= net.joycool.wap.action.job.fish.FishAction.oldUserId){
		if((count[0] - 4) % 5 == 0){
			action2.startGame(games[0], 1);
			count[0]++;
			return;
		}
	}else{
		if((count[0] - 4) % 5 == 0){
			action2.startGame(games[0], 1);
			count[0]++;
			return;
		}
	}
}

if(ForbidUtil.isForbid("game",loginUser.getId())){
	response.sendRedirect("/enter/not.jsp");
	return;
}
if(quarryIdS!=null && quarryIdS.equals(quarry) && action.isCooldown("tong",1000))
{
	session.removeAttribute("quarryId");
}else
{
int rand = net.joycool.wap.util.RandomUtil.seqInt(100);
session.setAttribute("huntcheck", new Integer(rand));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="狩猎公园">
<p align="left">
这个猎物已经死亡.<br/>
<a href="huntArea.jsp?s=<%=rand%>">继续前行</a><br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
</wml>
	<%
	return;
}
// 武器状态信息通用验证start
HuntUserWeaponBean userWeapon = action.getUsableHuntUserWeapon();
if (userWeapon == null){
	// 没有武器或武器过期了(不从狩猎页进入时校验)
	//response.sendRedirect(("hunt.jsp?msg=2"));
	BaseAction.sendRedirect("hunt.jsp?msg=2", response);
	return;
}
//武器状态信息通用验证end 
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int weaponId = userWeapon.getWeaponId();

int quarryId = StringUtil.toInt(quarry);
HuntQuarryBean quarryBean=action.getQuarry(quarryId);
HashMap weaponMap = LoadResource.getWeaponMap();
HuntWeaponBean weapon = (HuntWeaponBean) weaponMap.get(new Integer(
		weaponId));

if(action.haveEnoughMoney(weapon.getShotPrice())&&(quarryBean.getHarmPrice()/2)>us.getGamePoint()){
	StoreBean store=bankService.getStore("user_id="+us.getUserId());
	int STORE_MONEY=1000000;
	if(!((store==null)||store.getMoney()<STORE_MONEY)){
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="没钱买医药费" ontimer="<%=response.encodeURL("huntArea.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
你只好<a href="huntArea.jsp">继续前行</a><br/>
狩猎管理员突然拉住了你的手：兄台，这个猎物对你来说太危险了，如果被咬伤，连医药费都不够啊……还是先去别的地方，攒点医药费吧……（3秒钟跳转）<br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
	<%
	return;}}
//买子弹
boolean ok = action.buyBall(weaponId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
//钱不够
if(ok==false){
%><card title="没钱买弹药">
<p align="left">
<%=BaseAction.getTop(request, response)%>
啊！您的钱不够买火药。需要尽快去赚点乐币哦。<br/>
<a href="quarryMarket.jsp">猎物收购站</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
<%
//钱足够
}else{
	

//取得射击结果
int result = action.getHuntResult(weaponId);
int point=action.addUserFrankForHunt(quarryId, result);
us=UserInfoUtil.getUserStatus(loginUser.getId());
count[0]++;

int rand = net.joycool.wap.util.RandomUtil.seqInt(100);
session.setAttribute("huntcheck", new Integer(rand));

// 攻击成功
if (Constants.HIT==result){
if(quarryBean.getPrice()>=0) {	// 价值为负数的不给买入
	action.doAddHuntUserQuarry(quarryId);
	
	if(count[0] < 2000){	// 如果一天内打了2000次则无法再捡到物品
		// 一定概率获得卡片，得到随机以下其中一张
		int[] card = {36, 51, 52, 53};
		int rnd;
		if(loginUser.getId() < net.joycool.wap.action.job.fish.FishAction.oldUserId)
			rnd = RandomUtil.nextInt(500);
		else
			rnd = RandomUtil.nextInt(500);
		if(rnd < 4) {
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(hour>7){
				UserBagCacheUtil.addUserBagCacheNotice(loginUser.getId(), card[rnd], null);
			}
		}
	}
	if(count[0]>400&&count[0] % 100 == 0){
		response.sendRedirect("quarryMarket.jsp");
		return;
	}
} else
	UserInfoUtil.updateUserCash(loginUser.getId(), quarryBean.getPrice(), 6, "打错猎物被扣钱");
	
	
%><card title="攻击成功" ontimer="<%=response.encodeURL("huntArea.jsp?s="+rand)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="huntArea.jsp?s=<%=rand%>">继续前行</a><br/>
<%if(quarryBean.getPrice()>=0){%>
耶！你击中了<%=quarryBean.getName()%>！它惨叫一声，慢慢倒下了！ 你获得了价值<%=quarryBean.getPrice()%>的<%=quarryBean.getName()%>！<%=point>=0?"并获得":"被扣"%><%=Math.abs(point)%>点经验值！现有乐币<%=us.getGamePoint()%>,经验值<%=us.getPoint()%>点.<br/>
<%}else{%>
啊！你击中了<%=quarryBean.getName()%>！由于狩猎不良行为，你被罚款<%=-quarryBean.getPrice()%>，以后别再犯啦！<br/>
<%}%>
<a href="viewQuarryList.jsp">我的猎物</a><br/>
（5秒钟自动跳转、在狩猎区前行）<br/><br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>

</p>
</card>
<%
// 攻击失败
}else if (Constants.NOHIT==result){
%>
<card title="攻击失败" ontimer="<%=response.encodeURL("huntArea.jsp?s="+rand)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="huntArea.jsp?s=<%=rand%>">继续前行</a><br/>
啊！<%=quarryBean.getName()%>晃动了一下，逃走了。唉，你运气不好，<%=point>=0?"但积累了":"被扣"%>经验值<%=Math.abs(point)%>点。继续练习吧，菜鸟.<br/>
<a href="viewQuarryList.jsp">我的猎物</a><br/>
（5秒钟自动跳转、在狩猎区前行）<br/><br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
<%
}else if (Constants.HARM==result){
action.deductUserMoneyForHarm(quarryId);
%>
<card title="被咬伤" ontimer="<%=response.encodeURL("huntArea.jsp?s="+rand)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="huntArea.jsp?s=<%=rand%>">继续前行</a><br />
<%=quarryBean.getName()%>被惊了一下，发现了你！它冲你扑来！啊，你受伤了！损失了<%=quarryBean.getHarmPrice()%>乐币治伤，唉，没死就走运了，失败是成功之母，你<%=point>=0?"获得":"被扣"%>经验值<%=Math.abs(point)%>点。
<br />
<a href="viewQuarryList.jsp">我的猎物</a>
<br />
（5秒钟自动跳转、在狩猎区前行）<br/><br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>

</p>
</card><%
} 
 }
%></wml>
