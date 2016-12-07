<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CastleService service = CastleService.getInstance();
static java.text.DecimalFormat numFormat = new java.text.DecimalFormat("0.000");
%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	long now=System.currentTimeMillis();
	CastleUserBean user = CastleUtil.getCastleUser(id);
	if(!action.isMethodGet()){
		long lastNow = action.getParameterLong("now");
		int flag=action.getParameterFlag("flag");
		user.setFlag(flag);
		String name = action.getParameterNoEnter("name");
		user.setName(name);
		user.setCastleCount(action.getParameterInt("cc"));
		user.setTong(action.getParameterInt("tong"));
		user.setGold(action.getParameterInt("gold"));
		user.setPeople(action.getParameterInt("people"));
		user.setCur(action.getParameterInt("cur"));
		user.setMain(action.getParameterInt("main"));
		user.setRace(action.getParameterInt("race"));
		
		user.reCalc(now);
		user.setCivil(action.getParameterInt("civil"));
		user.setCivilSpeed(action.getParameterInt("civilSpeed"));
		
		float protect=action.getParameterFloat("protect");
		float lock=action.getParameterFloat("lock");
		float sp=action.getParameterFloat("sp");
		user.setSpTime((long)(sp*DateUtil.MS_IN_DAY)+lastNow);
		user.setLockTime((long)(lock*DateUtil.MS_IN_DAY)+lastNow);
		user.setProtectTime((long)(protect*DateUtil.MS_IN_DAY)+lastNow);
		
		user.setQuest(action.getParameterInt("quest"));
		user.setQuestStatus(action.getParameterInt("questStatus"));
		
		SqlUtil.executeUpdate("update castle_user set flag="+flag+",castle_count="+user.getCastleCount()+  ",tong="+user.getTong()+ ",people="+user.getPeople()+
				",quest="+user.getQuest()+  ",quest_status="+user.getQuestStatus()+  ",gold="+user.getGold()+  
				",civil="+user.getCivil()+  ",civil_speed="+user.getCivilSpeed()+  ",civil_time="+now+
				",sp_time='"+DateUtil.formatSqlDatetime(user.getSpTime())+ "',lock_time='"+DateUtil.formatSqlDatetime(user.getLockTime())+ "',protect_time='"+DateUtil.formatSqlDatetime(user.getProtectTime())+
				"',name='"+StringUtil.toSql(user.getName())+"',main="+user.getMain()+",race="+user.getRace()+",cur="+user.getCur()+" where uid="+user.getUid(), 5);
		response.sendRedirect("user.jsp?id="+id);
		return;
	}
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	修改城堡<br/>
【<a href="user.jsp?id=<%=id%>"><%=user.getNameWml()%></a>】<br/>

<form action="muser.jsp?id=<%=id%>" method=post>
名称:<input type=text name="name" value="<%=user.getNameWml()%>">(uid:<%=user.getUid()%>)<br/>
资料:<input type=text name="info" value="<%=user.getInfo()%>" readonly><br/>
种族:<input type=text name="race" value="<%=user.getRace()%>"><br/>
拥有城堡:<input type=text name="cc" value="<%=user.getCastleCount()%>"><br/>
总人口:<input type=text name="people" value="<%=user.getPeople()%>"><br/>
文明度速度:<input type=text name="civilSpeed" value="<%=user.getCivilSpeed()%>">/天<br/>
主城:<input type=text name="main" value="<%=user.getMain()%>"><br/>
当前城堡:<input type=text name="cur" value="<%=user.getCur()%>"><br/>
<br/>
联盟:<input type=text name="tong" value="<%=user.getTong()%>"><br/>
文明度:<input type=text name="civil" value="<%=user.getCivil(now)%>"><br/>
金币:<input type=text name="gold" value="<%=user.getGold()%>"><br/>
<input type=checkbox name="flag" value="0"<%if(user.isFlagPalace()){%> checked<%}%>>有皇宫<br/>
<br/>
任务:<input type=text name="quest" value="<%=user.getQuest()%>"><br/>
任务状态:<select name="questStatus">
<option value="0"<%if(user.getQuestStatus()==0){%> selected<%}%>>未完成</option>
<option value="1"<%if(user.getQuestStatus()==1){%> selected<%}%>>已完成</option>
</select><br/>
<br/>
新手保护:<input type=text name="protect" value="<%=numFormat.format((float)(user.getProtectTime()-now)/DateUtil.MS_IN_DAY)%>">天<br/>
冻结:<input type=text name="lock" value="<%=numFormat.format((float)(user.getLockTime()-now)/DateUtil.MS_IN_DAY)%>">天<br/>
SP帐号:<input type=text name="sp" value="<%=numFormat.format((float)(user.getSpTime()-now)/DateUtil.MS_IN_DAY)%>">天<br/>
<br/>
<input type=hidden name="now" value="<%=now%>"><br/>
<input type=submit value="确认修改">
</form><br/>

	</body>
</html>