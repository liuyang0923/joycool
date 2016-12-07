<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.stage.*,net.wxsj.bean.stage.*,java.util.*"%><%
StageAdminAction action = new StageAdminAction();
action.editQuestion(request, response);

String result = (String) request.getAttribute("result");

if("failure".equals(result)){
	String tip = (String) request.getAttribute("tip"); 
%>
<script>
alert("<%=tip%>");
history.back(-1);
</script>
<%
	return;
}
else if("success".equals(result)){
    response.sendRedirect("questionList.jsp");
	return;
}

QuestionBean question = (QuestionBean) request.getAttribute("question");
ArrayList answerList = (ArrayList) request.getAttribute("answerList");
AnswerBean answer = null;
int count, i;
%>
<form action="editQuestion.jsp" method="post">
<table width="100%" border="0">
  <tr>
    <td width="100">标题：</td>
	<td><input type="text" name="title" size="50" value="<%=question.getTitle()%>"/></td>
  </tr>
  <tr>
    <td>说明：</td>
	<td><textarea name="content" cols="50" rows="5"><%=question.getContent()%></textarea></td>
  </tr>
  <tr>
    <td>类型：</td>
	<td><select name="type"><option value="0" <%if(question.getType() == 0){%>selected="selected"<%}%>>选择题</option><option value="1" <%if(question.getType() == 1){%>selected="selected"<%}%>>填空题</option></select></td>
  </tr>
  <tr>
    <td>状态：</td>
	<td><select name="ended"><option value="0" <%if(question.getEnded() == 0){%>selected="selected"<%}%>>未结束</option><option value="1" <%if(question.getEnded() == 1){%>selected="selected"<%}%>>已结束</option></select></td>
  </tr>
  <tr>
    <td>提示网址：</td>
	<td><input type="text" name="tipUrl" size="50" value="<%=question.getTipUrl()%>"/></td>
  </tr>
  <tr>
    <td>答案：</td>
	<td><input type="text" name="correctAnswer" size="50" value="<%=question.getCorrectAnswer()%>"/>（如果是填空题请填写）</td>
  </tr>
  <tr>
    <td>设置：</td>
	<td><input type="text" name="maxCount" size="5" value="<%=question.getMaxCount()%>"/>人回答后停止。</td>
  </tr>
  <tr>
    <td>奖励类型：</td>
	<td><select name="bonusType"><option value="0" <%if(question.getBonusType() == 0){%>selected="selected"<%}%>>乐币</option><option value="1" <%if(question.getBonusType() == 1){%>selected="selected"<%}%>>道具</option><option value="2" <%if(question.getBonusType() == 2){%>selected="selected"<%}%>>经验值</option></select></td>
  </tr>
  <tr>
    <td>奖励值：</td>
	<td><input type="text" name="bonus" size="10" value="<%=question.getBonus()%>"/>（道具则填道具ID）</td>
  </tr>
<%
count = answerList.size();
if(count > 0){
	answer = (AnswerBean) answerList.get(0);
}
else {
	answer = null;
}
%>
  <tr>
    <td>候选答案1：</td>
	<td><input type="text" name="answer1" size="50" value="<%if(answer != null){%><%=answer.getContent()%><%}%>"/><input type="radio" name="correctAnswerIndex" value="1" <%if(answer != null && answer.getIsCorrect() == 1){%>checked<%}%>></td>
  </tr>
<%
if(count > 1){
	answer = (AnswerBean) answerList.get(1);
}
else {
	answer = null;
}
%>
  <tr>
    <td>候选答案2：</td>
	<td><input type="text" name="answer2" size="50" value="<%if(answer != null){%><%=answer.getContent()%><%}%>"/><input type="radio" name="correctAnswerIndex" value="2" <%if(answer != null && answer.getIsCorrect() == 1){%>checked<%}%>></td>
  </tr>
<%
if(count > 2){
	answer = (AnswerBean) answerList.get(2);
}
else {
	answer = null;
}
%>
  <tr>
    <td>候选答案3：</td>
	<td><input type="text" name="answer3" size="50" value="<%if(answer != null){%><%=answer.getContent()%><%}%>"/><input type="radio" name="correctAnswerIndex" value="3" <%if(answer != null && answer.getIsCorrect() == 1){%>checked<%}%>></td>
  </tr>
<%
if(count > 3){
	answer = (AnswerBean) answerList.get(3);
}
else {
	answer = null;
}
%>
  <tr>
    <td>候选答案4：</td>
	<td><input type="text" name="answer4" size="50" value="<%if(answer != null){%><%=answer.getContent()%><%}%>"/><input type="radio" name="correctAnswerIndex" value="4" <%if(answer != null && answer.getIsCorrect() == 1){%>checked<%}%>></td>
  </tr>
<%
if(count > 4){
	answer = (AnswerBean) answerList.get(4);
}
else {
	answer = null;
}
%>
  <tr>
    <td>候选答案5：</td>
	<td><input type="text" name="answer5" size="50" value="<%if(answer != null){%><%=answer.getContent()%><%}%>"/><input type="radio" name="correctAnswerIndex" value="5" <%if(answer != null && answer.getIsCorrect() == 1){%>checked<%}%>></td>
  </tr>
<%
if(count > 5){
	answer = (AnswerBean) answerList.get(5);
}
else {
	answer = null;
}
%>
  <tr>
    <td>候选答案6：</td>
	<td><input type="text" name="answer6" size="50" value="<%if(answer != null){%><%=answer.getContent()%><%}%>"/><input type="radio" name="correctAnswerIndex" value="6" <%if(answer != null && answer.getIsCorrect() == 1){%>checked<%}%>></td>
  </tr>
  <tr>
    <td><input type="checkbox" value="1" name="restart"></td>
	<td>清空历史答题记录（需重新开始时请选）</td>
  </tr>
  <tr>
    <td></td>
	<td><input type="submit" name="B" value="修改问题"/></td>
  </tr>
</table>
<input type="hidden" name="id" value="<%=question.getId()%>"/>
</form>