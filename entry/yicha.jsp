<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷站外搜索">
<%=BaseAction.getTop(request, response)%>
==乐酷站外搜索==<br/>
输入感兴趣的词语：<br/>
<input name="k" maxlength="50" value="美女"/><br/>
搜<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=i&amp;keyword=$k">美图</a>
.<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=p&amp;keyword=$k">娱乐</a>
.<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=n&amp;keyword=$k">小说</a>
.<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=r&amp;keyword=$k">铃声</a><br/>
<input name="k2" maxlength="50" value="免费"/><br/>
搜<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=g&amp;keyword=$k2">游戏</a>
.<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=s&amp;keyword=$k2">软件</a>
.<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=v&amp;keyword=$k2">视频</a><br/>
==网友搜索热门==<br/>
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=i&amp;keyword=美女">美女</a>.
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=i&amp;keyword=自拍">自拍</a>.
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=i&amp;keyword=明星">明星</a><br/>
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=n&amp;keyword=剧照">剧照</a>.
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=n&amp;keyword=动漫">动漫</a>.
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=n&amp;keyword=写真">写真</a><br/>
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=n&amp;keyword=美女">侦探</a>.
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=n&amp;keyword=自拍">冒险</a>.
<a href="http://u.yicha.cn/union/x.jsp?site=2145904013&amp;p=n&amp;keyword=美女">爱情</a><br/>
<%=BaseAction.getBottom(request, response)%>
</card>
</wml>