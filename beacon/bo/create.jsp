<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.bottle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%!static BottleService service = new BottleService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="漂流瓶">
<p><%
		BottleAction action = new BottleAction(request);
		if (!action.isMethodGet()) {
			String tip = null;
			String title = action.getParameterNoEnter("title");
			String content = action.getParameterString("content");
			String mood = action.getParameterNoEnter("mood");

			//过滤特殊字符
			title = title.replace("\\", "\\\\").replace("'", "\\'");
			content = content.replace("\\", "\\\\").replace("'", "\\'");
			mood = mood.replace("\\", "\\\\").replace("'", "\\'");
			title = title.trim();
			content = content.trim();
			mood = mood.trim();
			
			int error = 0;	// 0 无错误 1 输入错误 2 无法创建
			//对记录做过滤。
			if ("".equals(title) || "".equals(content) || "".equals(mood)) {
				tip = "输入的信息不完整，请重新输入。";
				error = 1;
			} else if (title.length() == 0 || title.length() > 12) {
				tip = "标签至少1个字,最多20字";
				error = 1;
			} else if (content.length() < 2 || content.length() > 100) {
				tip = "表白至少2个字,最多100字";
				error = 1;
			} else if (mood.length() > 2) {
				tip = "心情最多2字";
				error = 1;
			}

			//取得id
			int uid = action.getLoginUser().getId();

			if(error==0){
				boolean can = SqlUtil.getIntResult("select count(id) from bottle_info where user_id=" +
						uid + " and create_time>curdate()", 5) < 2;
				if (can) {
					//先向List列表中写入数据，再向数据库中写入。
					boolean result = action.createBottle(uid, content, title,
							mood);
					if (result) {
						tip = "你已成功投放一个漂流瓶。";
					} else {
						tip = "放置失败,有问题请与在线管理员联系。";
						error = 2;
					}
				} else {
					tip = "放置失败:每天你只能放两个瓶子哦~！";
					error = 2;
				}
			}
	%><%=tip%>
	<%
		if (error != 1) {
	%><br />
	<a href="bottles.jsp">返回</a>
	<%
		} else {
	%><br />
	<a href="create.jsp">返回</a>
	<%
		}
	%>
	<%
		} else {
	%>
漂流标签(最多12字)<br/>
<input name="submitTitle" value="" maxlength="12"/><br/>
独白(2至100字)<br/>
<input name="submitContent" value="" maxlength="100"/><br/>
心情(最多2字)<br/>
<input name="submitMood" value="" maxlength="2" /><br/>
每天你只能放两个漂流瓶<br/>
<anchor>确定
<go href="create.jsp" method="post">
<postfield name="title" value="$submitTitle" />
<postfield name="content" value="$submitContent" />
<postfield name="mood" value="$submitMood" />
</go>
</anchor><br/>
<a href="bottles.jsp">返回</a><br/>
<%}%></p></card>
</wml>

