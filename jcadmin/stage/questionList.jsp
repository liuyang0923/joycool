<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.bean.stage.*,net.wxsj.action.stage.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%
StageAdminAction action = new StageAdminAction();
action.questionList(request, response);

ArrayList list = (ArrayList) request.getAttribute("list");
PagingBean paging = (PagingBean) request.getAttribute("paging");

int i, count;
QuestionBean bean = null;
%>
问题列表<br/>
<table width="100%" border="1">
    <tr><td><strong>序号</strong></td><td><strong>标题</strong></td><td><strong>类型</strong></td><td><strong>正确答案</strong></td><td><strong>提示网址</strong></td><td><strong>最大回答数</strong></td><td><strong>状态</strong></td><td><strong>奖励方式</strong></td><td><strong>奖励</strong></td><td><strong>回答数</strong></td><td><strong>正确数</strong></td><td><strong>操作</strong></td></tr>
<%
count = list.size();
for(i = 0; i < count; i ++){
	bean = (QuestionBean) list.get(i);
	
%>
    <tr>
	<td><%=(i + 1)%></td>
	<td><%=bean.getTitle()%></td>
	<td><%=bean.getTypeStr()%></td>
	<td><%=bean.getCorrectAnswer()%></td>
	<td><%=bean.getTipUrl()%></td>
	<td><%=bean.getMaxCount()%></td>
	<td><%=bean.getEndedStr()%></td>
	<td><%=bean.getBonusTypeStr()%></td>
	<td><%=bean.getBonus()%></td>
	<td><%=bean.getCurrentCount()%></td>
	<td><%=bean.getCorrectCount()%></td>
	<td><a href="editQuestion.jsp?id=<%=bean.getId()%>">编辑</a> <a href="deleteQuestion.jsp?id=<%=bean.getId()%>" onclick="return confirm('确认删除？')">删除</a></td>
	</tr>
<%
}
%>
</table>

<%
String fenye = PageUtil.shuzifenye(paging, paging.getPrefixUrl(), false, " ", response);
if(fenye != null && !"".equals(fenye)){
%>
<%=fenye%>
<%
}
%>

<form action="addQuestion.jsp" method="post">
<table width="100%" border="0">
  <tr>
    <td width="100">标题：</td>
	<td><input type="text" name="title" size="50" value=""/></td>
  </tr>
  <tr>
    <td>说明：</td>
	<td><textarea name="content" cols="50" rows="5"></textarea></td>
  </tr>
  <tr>
    <td>类型：</td>
	<td><select name="type"><option value="0">选择题</option><option value="1">填空题</option></select></td>
  </tr>
  <tr>
    <td>提示网址：</td>
	<td><input type="text" name="tipUrl" size="50" value="http://"/></td>
  </tr>
  <tr>
    <td>答案：</td>
	<td><input type="text" name="correctAnswer" size="50" value=""/>（如果是填空题请填写）</td>
  </tr>
  <tr>
    <td>设置：</td>
	<td><input type="text" name="maxCount" size="5" value=""/>人回答后停止。</td>
  </tr>
  <tr>
    <td>奖励类型：</td>
	<td><select name="bonusType"><option value="0">乐币</option><option value="1">道具</option><option value="2">经验值</option></select></td>
  </tr>
  <tr>
    <td>奖励值：</td>
	<td><input type="text" name="bonus" size="10" value=""/>（道具则填道具ID）</td>
  </tr>
  <tr>
    <td>候选答案1：</td>
	<td><input type="text" name="answer1" size="50" value=""/><input type="radio" name="correctAnswerIndex" value="1"></td>
  </tr>
  <tr>
    <td>候选答案2：</td>
	<td><input type="text" name="answer2" size="50" value=""/><input type="radio" name="correctAnswerIndex" value="2"></td>
  </tr>
  <tr>
    <td>候选答案3：</td>
	<td><input type="text" name="answer3" size="50" value=""/><input type="radio" name="correctAnswerIndex" value="3"></td>
  </tr>
  <tr>
    <td>候选答案4：</td>
	<td><input type="text" name="answer4" size="50" value=""/><input type="radio" name="correctAnswerIndex" value="4"></td>
  </tr>
  <tr>
    <td>候选答案5：</td>
	<td><input type="text" name="answer5" size="50" value=""/><input type="radio" name="correctAnswerIndex" value="5"></td>
  </tr>
  <tr>
    <td>候选答案6：</td>
	<td><input type="text" name="answer6" size="50" value=""/><input type="radio" name="correctAnswerIndex" value="6"></td>
  </tr>
  <tr>
    <td></td>
	<td><input type="submit" name="B" value="添加问题"/></td>
  </tr>
</table>
</form>