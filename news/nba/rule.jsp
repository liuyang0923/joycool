<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="常规赛排名规则"><p><%=BaseAction.getTop(request, response)%>
=常规赛排名规则=<br/><% 
%>注1：胜差指该球队与所在分部领先者之间净胜场次之差除以2。得分小于100、得分大于等于100分别指本队在单场得分低于100或超过100时的战绩。<br/><% 
%>注2：东西部两个联盟的各前八名进入季后赛。同一分部的三个分区第一名和一个成绩最好的分区第二名按战绩决定本分部前四名，剩下的四支球队按各自战绩排定名次。<br/><% 
%>注3：在季后赛中决定两队之间主场优势的依据为双方的常规赛胜率高低，而不是排名。<br/><% 
%>注4：当两队胜场差相同时，依次按以下依据排出先后：<br/><% 
%>1、相互之间战绩好的居前；<br/><% 
%>2、在各自联盟中的胜率高的居前；<br/><% 
%>3、在赛区中的胜率高的居前(只在两队同属一赛区时适用)；<br/><% 
%>4、与所在联盟的其他前八名球队交手的胜率高的居前；<br/><% 
%>5、与另一个联盟的前八名球队交手的胜率高的居前；<br/><% 
%>6、得失分差高的居前。<br/><% 
%>注5：当三队以上胜场差相同时，依次按以下依据排出先后：<br/><% 
%>1、相互之间战绩好的居前；<br/><% 
%>2、在各自联盟中的胜率高的居前；<br/><% 
%>3、在赛区中的胜率高的居前(各队同属一赛区时适用)；<br/><% 
%>4、与所在联盟的其他前八名球队交手的胜率高的居前；<br/><% 
%>5、得失分差高的居前。<br/><% 
%><a href="rank.jsp">返回常规赛排名</a><br/>
<a href="index.jsp">返回NBA专题</a><br/>
<%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>