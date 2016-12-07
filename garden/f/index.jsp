<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	FlowerAction action = new FlowerAction(request);
UserBean loginUser = action.getLoginUser();
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<%if(loginUser==null||loginUser.getUserSetting()==null||!loginUser.getUserSetting().isFlagHideLogo()){%><img src ="img/logo.gif" alt="logo" /><br/><%}%>
欢迎来到【<a href="hb.jsp">花之境</a>】,在这里你可以养殖各种珍奇的花草,可以让你实现成为超级花匠的梦想!<br/>
<a href="fgarden.jsp">我的花园</a><br/>
<a href="result.jsp?t=1">黄金成果</a><br/>
+<a href="/jcforum/forum.jsp?forumId=9066">采集岛居民交流区</a>+<br/>
<a href="rank.jsp">超级花匠</a><br/>
<a href="hb.jsp">游戏手册</a><br/>
----------<br/>
<a href="../island.jsp">返回采集岛</a><br/>
<a href="../../home/home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>