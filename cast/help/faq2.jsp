<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	
	CustomAction customAction = new CustomAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="FAQ2"><p>
【城堡进阶说明】<br/>
0.地图上的图案说明请参考<a href="map.jsp">地图说明</a><br/>
1.在开采所可以选择当前的士兵来训练指挥官(冲撞车、投石车、拓荒者、侦察兵和统治者等无法训练),一个玩家最多可以拥有一个指挥官。<br/>
2.指挥官的粮食消耗统一为6每小时，运载量为0，移动速度与训练前的士兵相同。<br/>
3.指挥官最高为99级，每个单项技能最多只能加100点。兵种的攻防升级对指挥官无效。<br/>
4.如果想占领一个绿洲，首先建造开采并训练一个指挥官，当开采所到10级的时候，就可以让指挥官带兵占领绿洲了.<br/>
5.为何无法占领绿洲?<br/>
请确认绿洲在城堡周围的7x7范围内,也就是说距离城堡纵横都不能超过3格,此外开采所等级足够(点击开采所可以查看).最后别忘了让指挥官同行<br/>
6.支援绿洲的士兵消耗谁的粮食?攻打绿洲抢夺的是哪里的资源?<br/>
消耗的是占领方城堡的粮食,抢夺的是占领方城堡资源的一小部分<br/>
7.宝库什么用,如何抢宝物<br/>
一个十级的宝库可以存放一个普通宝物.如果想抢宝物,需要从十级宝库所在城堡,攻击拥有宝物的城堡,摧毁其宝库,并在指挥官的带领下发动一次成功的攻击.注意:一个玩家最多只能拥有一个宝物.<br/>
具体的宝物资料参考<a href="art.jsp">宝物详细资料</a><br/>
8.用拓荒者建立新的城堡有哪些条件限制?<br/>
目标必须是无人占领的荒漠,此外文明度和行宫/皇宫等级要足够(点击行宫/皇宫可以看到具体提示).在游戏初期可能还会限制总城堡数<br/>
9.用诗人/主教/祭司说服对方城堡有什么限制条件?<br/>
出兵方必须满足开分城的一切条件，包括文明度、扩张度等等。被攻击方至少要有4个城堡。<br/>
10.哪里可以训练诗人/主教/祭司?<br/>
在行宫/皇宫可以训练(需要先在研究所研发),但是训练有数量限制.而且这个限制是和拓荒累计的.举例:一个可以开一个分城的城堡,最多可以训练1个主教或者3个拓荒.<br/>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>