<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session= "false"%><%@ page import="java.util.*,jc.user.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.framework.CustomAction,net.joycool.wap.cache.util.ForumCacheUtil,net.joycool.wap.bean.jcforum.ForumContentBean"%><%@ page import="net.joycool.wap.action.wgame.WGameAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%//@ page errorPage="../failure.jsp"%><%@ page import="net.joycool.wap.framework.JoycoolSpecialUtil" %><%@ page import="java.util.Vector" %><%@ page import="java.util.HashMap" %>
<%! public static String ATTACH_URL_ROOT = "/rep/soft/"; %>
<%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
ForumContentBean con = null;
List list = SqlUtil.getIntList("select id from jc_forum_prime where forum_id=3506 order by id desc limit 5",2);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="手机软件专区">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/phone.gif" alt="o"/><br/>
进入:<a href="/jcforum/forum.jsp?forumId=3506">论坛</a>|<a href="/Column.do?columnId=4381">美图</a>|<a href="/Column.do?columnId=8774">书城</a><br/>
【UC浏览器】<br/>优秀的手机网页浏览工具<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "ucs60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "ucs60v3.sisx"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "ucs60v5.sisx"%>">V5版</a><br/>
其它:<a href="<%=ATTACH_URL_ROOT + "ucjava.jad"%>">JAVA版</a>|<a href="<%=ATTACH_URL_ROOT + "ucmtk.mrp"%>">MTK版</a><br/>
【腾讯QQ】<br/>手机版本的腾讯QQ<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "qq2008s602.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "qq2010s603.sisx"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "qq2008s605b1.sis"%>">V5版</a><br/>
其它:<a href="<%=ATTACH_URL_ROOT + "qqjava.zip"%>">JAVA版</a>|<a href="<%=ATTACH_URL_ROOT + "qqmtk.zip"%>">MTK版</a><br/>
【UC播放器】<br/>支持在线视频的视频播放器<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "ucps60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "ucps60v3.sisx"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "ucps60v5.sisx"%>">V5版</a><br/>
其它:<a href="<%=ATTACH_URL_ROOT + "ucpppc.CAB"%>">PPC版</a>|<a href="<%=ATTACH_URL_ROOT + "ucm8.CAB"%>">M8版</a><br/>
【来电通】<br/>NOKIA好用的来电管理软件<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "ldts60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "ldts60v3.sis"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "ldts60v5.sis"%>">V5版</a><br/>
【墨迹天气】<br/>NOKIA经典天气预报软件<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "MJWeathers60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "MJWeathers60v3.sisx"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "MJWeathers60v5.sisx"%>">V5版</a><br/>
【360手机安全】<br/>国产手机安全软件<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "360s60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "360s60v3.sisx"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "360s60v5.sisx"%>">V5版</a><br/>
【手机迅雷】<br/>迅雷手机版,下载利器<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "xunleis60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "xunleis60v3.sisx"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "xunleis60v5.sisx"%>">V5版</a><br/>
【搜狗输入】<br/>搜狗输入法手机版<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "sogous60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "sogous60v3.sisx"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "sogous60v5.sisx"%>">V5版</a><br/>
其它:<a href="<%=ATTACH_URL_ROOT + "sogouppc.CAB"%>">PPC版</a>|<a href="<%=ATTACH_URL_ROOT + "sogoan.apk"%>">Android</a><br/>
【天天动听】<br/>功能强大的手机音乐软件<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "TTPods60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "TTPods60v3.sisx"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "TTPods60v5.sisx"%>">V5版</a><br/>
其它:<a href="<%=ATTACH_URL_ROOT + "TTPodjava.jad"%>">JAVA版</a><br/>
【熊猫看书】<br/>好用的手机阅读软件<br/>
S60:<a href="<%=ATTACH_URL_ROOT + "91ks60v2.sis"%>">V2版</a>|<a href="<%=ATTACH_URL_ROOT + "91ks60v3.zip"%>">V3版</a>|<a href="<%=ATTACH_URL_ROOT + "91ks60v5.zip"%>">V5版</a><br/>
其它:<a href="<%=ATTACH_URL_ROOT + "91kjava.jar"%>">JAVA版</a>|<a href="<%=ATTACH_URL_ROOT + "91ksppc.cab"%>">PPC版</a><br/>
【<a href="/jcforum/forum.jsp?forumId=3506">高手指南</a>-菜鸟必读】<br/>
<%if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		con =ForumCacheUtil.getForumContent(StringUtil.toInt(list.get(i).toString()));
		if (con != null){
			%><a href="/jcforum/viewContent.jsp?contentId=<%=con.getId() %>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%
		}
	}
}%>
<a href="/jcforum/forum.jsp?forumId=3506">>>进入论坛</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>