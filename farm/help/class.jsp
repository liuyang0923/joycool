<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
桃花源中的战斗职业(非辅助职业)暂时分为六种.<br/>
剑客:拥有高超的技艺,轻盈之身法惑敌,于不意中杀敌于瞬间.<br/>
武夫:有着魁梧的体魄,惊人的爆发力,强大之威势震慑敌胆,不战而屈人之兵.<br/>
道士:拥有常人所不能拥有的智慧,吸纳大地之气,杀敌与千里之外.<br/>
刺客:拥有诡异的身法,华丽的招式,杀敌与无形之中.<br/>
艺人:拥有曼妙的舞姿,美妙的歌喉,魅惑敌人,增加自己队友的士气,是团队当中最受欢迎的.<br/>
行医:以救死扶伤为主.救人与危难当中.是一个队伍生存的保证.<br/>
*要修行某个职业,需要首先达到一定的等级,然后到潮州城找捕头完成一系列任务,最后在丁二的推荐下才能拜师学艺.
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>