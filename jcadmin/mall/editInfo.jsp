<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.mall.*,net.wxsj.bean.mall.*,java.util.*,net.wxsj.util.*"%><%
MallAdminAction action = new MallAdminAction();
action.editInfo(request, response);

InfoBean info = (InfoBean) request.getAttribute("info");

String result = (String) request.getAttribute("result");
//失败
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
//成功
else if("success".equals(result)){
%>
<script>
alert("修改成功！");
document.location = "info.jsp?id=<%=info.getId()%>";
</script>
<%
	return;
}

ArrayList tagList = (ArrayList) request.getAttribute("tagList");
ArrayList areaTagList = (ArrayList) request.getAttribute("areaTagList");
int i, count;
TagBean tag = null;
AreaTagBean areaTag = null;

ArrayList tagIntList = info.getTagIntList();
ArrayList areaTagIntList = info.getAreaTagIntList();
%>
<form action="editInfo.jsp" method="post">
名称：<input type="text" name="name" size="20" value="<%=info.getName()%>"/><br/>
类型：<select name="infoType"><option value="0" <%if(info.getInfoType() == 0){%>selected="selected"<%}%>>求购</option><option value="1" <%if(info.getInfoType() == 1){%>selected="selected"<%}%>>出售</option></select><br/>
用户昵称：<input type="text" name="userNick" size="20" value="<%=info.getUserNick()%>"/><br/>
介绍：<textarea name="intro" cols="40" rows="5"><%=info.getIntro()%></textarea> <br/>
分类标签：<SELECT NAME="tags" SIZE="<%=tagList.size()%>" MULTIPLE> 
<%
count = tagList.size();
for(i = 0; i < count; i ++){
	tag = (TagBean) tagList.get(i);	
%>
<option value="<%=tag.getId()%>" <%if(IntUtil.intInList(tagIntList, tag.getId())){%>selected="selected"<%}%>><%=tag.getName()%></option>
<%
}
%>
</SELECT><br/>
地区标签：<SELECT NAME="areaTags" SIZE="<%=areaTagList.size()%>" MULTIPLE> 
<%
count = areaTagList.size();
for(i = 0; i < count; i ++){
	areaTag = (AreaTagBean) areaTagList.get(i);	
%>
<option value="<%=areaTag.getId()%>" <%if(IntUtil.intInList(areaTagIntList, areaTag.getId())){%>selected="selected"<%}%>><%=areaTag.getName()%></option>
<%
}
%>
</SELECT><br/>
期望价格：<input type="text" name="price" size="20" value="<%=info.getPrice()%>"/><br/>
交易方式：<input type="text" name="buyMode" size="20" value="<%=info.getBuyMode()%>"/><br/>
联系电话：<input type="text" name="telephone" size="20" value="<%=info.getTelephone()%>"/><br/>
所在地区：<input type="text" name="address" size="20" value="<%=info.getAddress()%>"/><br/>
验证信息：<select name="validated">
<option value="0" <%if(info.getValidated() == 0){%>selected="selected"<%}%>>未验证</option><option value="1" <%if(info.getValidated() == 1){%>selected="selected"<%}%>>已验证</option>
</select><br/>
置顶：<select name="isTop">
<option value="0" <%if(info.getIsTop() == 0){%>selected="selected"<%}%>>否</option><option value="1" <%if(info.getIsTop() == 1){%>selected="selected"<%}%>>是</option>
</select><br/>
精华：<select name="isJinghua">
<option value="0" <%if(info.getIsJinghua() == 0){%>selected="selected"<%}%>>否</option><option value="1" <%if(info.getIsJinghua() == 1){%>selected="selected"<%}%>>是</option>
</select><br/>
<input type="hidden" name="id" value="<%=info.getId()%>"/>
<input type="submit" name="B" size="20" value="修改"/>
</form>