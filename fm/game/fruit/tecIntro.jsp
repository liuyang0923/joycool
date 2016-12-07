<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,jc.family.game.pvz.*,java.util.*,jc.util.SimpleChatLog2,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.*"%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%int type=action.getParameterInt("t");
if(type == 0 || type >5){%>
该水果科技尚未发明!<br/>
<%}else if(type == 1){%>
作用范围:家族全体水果<br/>
尖刺果皮:聪博士的心血研究成果,在亲手触摸数万种植物后,终于发现有尖刺的水果......扎人很痛啊.改造后会提升水果的伤害噢!<br/>
<%}else if(type == 2){%>
作用范围:家族全体水果<br/>
加厚果皮:聪博士发现果皮的厚度影响到水果的结实程度,结实程度呢也就是防御力啦!在运用转基因技术时意外发现了果皮增厚的秘方.此研究的结论是,果皮越厚,果肉越少!<br/>
<%}else if(type == 3){%>
作用范围:家族全体果园生产水果<br/>
果影分身:在水果基因信息上加入乐酷标签,会提升单位阳光下水果产出量.更多乐酷,更多水果!<br/>
<%}else if(type == 4){%>
作用范围:家族全体水果<br/>
喷气水果:工程学与水果生物学融合的产物,第二次水果科技革命的里程碑,让水果迸发无尽动力与耀眼火焰,还不会烤熟你的水果,可以放心提升水果速度!<br/>
<%}else if(type == 5){%>
作用范围:当前果园<br/>
阳光透镜:选择阳光透镜,得到更多阳光!<br/>
<%}%>
<%if(action.hasParam("s")){
int side = 0;
if(request.getParameter("s").equals("1")){side =1;}
%>
<a href="fmO.jsp?s=<%=side %>">返回</a><br/>
<%}else if(action.hasParam("x")&&action.hasParam("y")){ %>
<a href="tecupd.jsp?x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">返回</a><br/>
<%} %>
<a href="game.jsp">返回水果战</a><br/>
<br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>