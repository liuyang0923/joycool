<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.bottle.*"%><?xml version="1.0" encoding="UTF-8"?><%@ page
	import="net.joycool.wap.framework.BaseAction"%>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="漂流瓶"><p><%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
把心愿写进瓶子,让它在银河中随波漂流……当瓶子到达彼岸,被人拾起时,投放与拾取的人都将得到牛郎织女的祝福,愿望就可以实现!<br/>
现有<%=BottleAction.bottleCountList.size()%>个漂流瓶<br/>
<a href="bottles.jsp">>去银河捞取漂流瓶</a><br/>
*帮助说明*<br/>
1.《漂流瓶》是应部分玩家要求，综合网络上流行的漂流瓶类游戏开发的，寻缘交友类游戏。游戏让你在茫茫人海，随着那份神秘的缘分，找到心目中的TA。<br/>
2.目前该游戏处于测试阶段，部分功能尚有待完善，如玩家在游戏过程中遇到问题时，可直接联系在线管理员，我们会尽快予以回复。<br/>
<%=BaseAction.getBottom(request, response)%></p></card>
</wml>