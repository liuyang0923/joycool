<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end

String man=(String)request.getParameter("man");
//判断重新设定男用户欢迎词
if(request.getParameter("clear") != null && man.equals("0")){
	Vector salutatoryManList = ContentList.getSalutatoryManList();
	if(salutatoryManList != null){
		salutatoryManList.removeAllElements();		
	}
	salutatoryManList.add("热心用户提示您：移动收的流量费还是跟以前一样，包月比较划算。");
	salutatoryManList.add("哇~~你选中了这个美女众多的聊天室，只能说：你太有才了！");
	salutatoryManList.add("阿土伯悄悄跟你说道：在聊天室里跟好友聊天的时候，还可以一边跟对方PK噢。");
	salutatoryManList.add("客官：你以为躲起来就找不到你了吗？没有用的！象你这样出色的男人，无论在什么地方，都像漆黑中的萤火虫一样，那样的鲜明，那样的出众。");
	salutatoryManList.add("乐酷社区美女沉鱼落雁的笑，免费游乐不变样，小样愣着干嘛，还不快找个靓妹私聊！");
	salutatoryManList.add("欢迎您，有缘的网友，无论您风尘仆仆而至、还是无意之中而入，免费的乐酷欢迎您！");
	salutatoryManList.add("管理员说：最近移动老是给出资费提醒糊弄人，他以前就一直这么收流量费的啊，跟乐酷没关系，乐酷不收费！");
	salutatoryManList.add("每天你都会逛地方,但不一定有什么事情发生,但当你来到乐酷,一段时间后,你将会彻底爱上它!");
	salutatoryManList.add("管理员对你说：“最近是不是被移动的资费提醒吓坏不敢来啦？其实那个流量费跟以前一样没有变化啦，放心吧。”");
	salutatoryManList.add("管理员对您抛来暧昧的问候：您是不是最近忙着帮会的事务把我们都给忘了。来我们这里歇歇脚吧，找个您喜欢的姑娘长谈！");
	//response.sendRedirect("forsalutatory.jsp");
	BaseAction.sendRedirect("/jcadmin/forsalutatory.jsp", response);
	return;
}
//判断重新设定女用户欢迎词
if(request.getParameter("clear") != null && man.equals("1")){
	Vector salutatoryWomanList = ContentList.getSalutatoryWomanList();
	if(salutatoryWomanList != null){
		salutatoryWomanList.removeAllElements();		
	}
	salutatoryWomanList.add("美女终于盼到你了，你是不是被移动的资费提醒吓坏不敢来啦？其实那个流量费跟以前一样没有变化啦，放心吧。");	  
	salutatoryWomanList.add("美女你一定要常来聊天室，移动的手机流量费又没变，乐酷完全免费，帅哥们也还是那么多。");	  
	salutatoryWomanList.add("一位戴墨镜的酷哥冷冷地对你说：“出来混，迟早是要还的；进来玩，迟早是要聊的。还好这里不收费，像我这样的帅哥还很多，选一个？”");	  
	salutatoryWomanList.add("管理员说：最近移动老是给出资费提醒糊弄人，他以前就一直这么收流量费的，跟乐酷没关系，乐酷不收费！");	  
	salutatoryWomanList.add("欢迎您，有缘的网友，无论您风尘仆仆而至、还是无意之中而入，免费的乐酷欢迎您！");	  
	salutatoryWomanList.add("热心用户提示您：移动收的流量费还是跟以前一样，包月比较划算。");	  
	salutatoryWomanList.add("想让自己每一时刻都充满着欢歌笑语吗?那就请停住你匆匆的脚步,这里会让你感受。");	  
	salutatoryWomanList.add("管理员匆匆的跑来对你说道：又不会多收费你怎么就不常常来！有个帅哥已经得了相思病了！非您得亲自去和他聊聊了。");	  
	salutatoryWomanList.add("欢迎您来到聊天室，我们这里有好多帮主都在，您可以尽情挑选个有财有势的，这样您就是乐酷第一大帮的帮主夫人了！");	  
	salutatoryWomanList.add("进了聊天室就别光在这里发呆啦，那么多帅哥都在等你呢，赶紧跟他们聊聊吧.");	  
	//response.sendRedirect("forsalutatory.jsp");
	BaseAction.sendRedirect("/jcadmin/forsalutatory.jsp", response);
	return;
}
%>
<html>
<head>
 <script language="javascript" >
function checkform(){
			if(document.bcontent.content.value == ''){
				alert("内容不能为空！");
				return false;
			}else{
				  return true;
				}
		}
</script>
</head>
<body>
<p><a href="forsalutatory.jsp?clear=1&man=0">重新设定男用户欢迎词</a></p>
<p>男用户欢迎词列表</p>
<table width="100%" border="2">
<%
//显示男用户欢迎词列表
Vector salutatoryManList = ContentList.getSalutatoryManList();
if(salutatoryManList != null){
	Iterator itr = salutatoryManList.iterator();
	int i = 1;
	while(itr.hasNext()){
%>
<tr>
<td width="10%"><%=i%></td>
<td width="80%"><%=((String)itr.next())%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<p><a href="forsalutatory.jsp?clear=1&man=1">重新设定女用户欢迎词</a></p>
<p>女用户欢迎词列表</p>
<table width="100%" border="2">
<%
//显示女用户欢迎词列表
Vector salutatoryWomanList = ContentList.getSalutatoryWomanList();
if(salutatoryWomanList != null){
	Iterator itr = salutatoryWomanList.iterator();
	int i = 1;
	while(itr.hasNext()){
%>
<tr>
<td width="10%"><%=i%></td>
<td width="80%"><%=((String)itr.next())%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<body>
</html>