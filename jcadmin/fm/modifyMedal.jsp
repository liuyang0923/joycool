<%@ page language="java" import="com.jspsmart.upload.*,net.joycool.wap.bean.PagingBean,jc.family.*,net.joycool.wap.util.*,jc.family.game.boat.*,jc.family.game.*,java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
SmartUpload smUpload = new SmartUpload();
FamilyAction action = new FamilyAction(request);
FamilyMedalBean bean = null;
String tip = "";
int id = action.getParameterInt("id");
if (id > 0){
	List list = FamilyAction.service.getMedalList(" id=" + id);
	if (list.size() > 0){
		bean = (FamilyMedalBean)list.get(0);
		if (bean == null)
			tip = "记录不存在.";
	}
}
int up = action.getParameterInt("up");
if (up == 1 && bean != null){
	try{
		smUpload.initialize(pageContext);
		smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
		smUpload.upload();	
		com.jspsmart.upload.File upFile = smUpload.getFiles().getFile(0);
		String fileName = "";
		if ("".equals(upFile.getFileName())){
			fileName = bean.getImg();
		}  else {
			fileName = System.currentTimeMillis() + "." + upFile.getFileExt();
			upFile.saveAs(net.joycool.wap.util.Constants.RESOURCE_ROOT_PATH + "family/medal/" + fileName,SmartUpload.SAVE_PHYSICAL);
		}
		int fmId = StringUtil.toInt(smUpload.getRequest().getParameter("fmid"));
		FamilyHomeBean fmBean = FamilyAction.getFmByID(fmId);
		if (fmBean == null){
			tip = "家族不存在.";
		}
		int seq = StringUtil.toInt(smUpload.getRequest().getParameter("seq"));
		String name = smUpload.getRequest().getParameter("name");
		if (name.length() > 45 ){
			tip = "名子太长了.";
		}
		String info = smUpload.getRequest().getParameter("info");
		if (info.length() > 145){
			tip = "介绍太长了.";
		}
		String endTime = smUpload.getRequest().getParameter("endTime");
		if (endTime == null || "".equals(endTime))
			endTime = "2020-12-31 00:00:00";
		if ("".equals(tip)){
			SqlUtil.executeUpdate("update fm_medal set name='" + StringUtil.toSql(name) + "',fm_id=" + fmId + ",seq=" + seq + ",img='" + fileName + "',info='" + StringUtil.toSql(info) + "',end_time='" + endTime + "' where id=" + bean.getId(),5);
			fmBean.setMedalList(null);
			tip = "执行成功.";
		}
	} catch (Exception e){
		tip = "上传失败.可能是类型错误.";
	}
}
%>
<html>
  <head>
    <title>勋章管理</title>
 	<link href="../farm/common.css" rel="stylesheet" type="text/css">
 	<script language="JavaScript" src="/jcadmin/js/WebCalendar.js" ></script>
  </head>
  <body>
 <%="".equals(tip)?"":tip%><br>
<input id="cmd" type="button" value="回上一页" onClick="javascript:window.location.href='medal.jsp'"><br/>
<%if (bean != null){
%><form action="modifyMedal.jsp?up=1&id=<%=bean.getId()%>" method="post" enctype="multipart/form-data" >
原图片:<br/>
<img src="/rep/family/medal/<%=bean.getImg()%>" alt="<%=StringUtil.toWml(bean.getName())%>"/><br/>
选择图片:<input type="file" name="medal"><br/>
家族ID:<input type="text" name="fmid" value="<%=bean.getFmId()%>"/><br/>
排序:<input type="text" name="seq" value="<%=bean.getSeq()%>"/><br/>
勋章名:<input type="text" name="name" value="<%=bean.getName()%>"/><br/>
详细介绍:<input type="text" name="info" value="<%=bean.getInfo()%>"/><br/>
到期时间:<input type="text" size=14 name="endTime" value="<%=DateUtil.formatSqlDatetime(bean.getEndTime())%>" onclick="SelectDate(this,'yyyy-MM-dd');"/><br/>
<input type="submit" value="提交"><br/><%
}%>
</form>
 <a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>
