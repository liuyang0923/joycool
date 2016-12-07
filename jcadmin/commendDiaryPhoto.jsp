<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.SqlUtil" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action=new HomeAction(request); 
int diaryId=action.getParameterInt("diaryId");
int photoId=action.getParameterInt("photoId");
int delDiaryId=action.getParameterInt("delDiaryId");
int delPhotoId=action.getParameterInt("delPhotoId");
UserBean user=null;
String tip=null;
//根据用户ID设置推荐日记
if(diaryId > 0)
{
//	action.getHomeService().updateHomeDiary("mark=1,recommend_time=now()","id="+diaryId);
	SqlUtil.executeUpdate("insert into jc_home_diary_top set create_time=now(),diary_id=" + diaryId);
	OsCacheUtil.flushGroup(OsCacheUtil.COMMEND_DIARYANDPHOTO_GROUP,"commendDiary");
	tip="ID为"+diaryId+"的日记已经被推荐成功!";
}
//根据用户ID取消设置推荐日记
if(delDiaryId > 0)
{
//	action.getHomeService().updateHomeDiary("mark=0","id="+delDiaryId);
	SqlUtil.executeUpdate("delete from jc_home_diary_top where diary_id=" + delDiaryId + " limit 1");
	OsCacheUtil.flushGroup(OsCacheUtil.COMMEND_DIARYANDPHOTO_GROUP,"commendDiary");
	tip="ID为"+delDiaryId+"的日记已经被取消推荐!";
}

//根据用户Id设置推荐照片
if(photoId > 0)
{
	SqlUtil.executeUpdate("insert into jc_home_photo_top set create_time=now(),photo_id=" + photoId);
//	action.getHomeService().updateHomePhoto("mark=1,recommend_time=now()","id="+photoId);
	OsCacheUtil.flushGroup(OsCacheUtil.COMMEND_DIARYANDPHOTO_GROUP,"commendPhoto");
	tip="ID为"+photoId+"的照片已经被推荐成功!";
}
//根据用户id设置取消推荐照片
if(delPhotoId > 0)
{
//	action.getHomeService().updateHomePhoto("mark=0","id="+delPhotoId);
	SqlUtil.executeUpdate("delete from jc_home_photo_top where photo_id=" + delPhotoId + " limit 1");
	OsCacheUtil.flushGroup(OsCacheUtil.COMMEND_DIARYANDPHOTO_GROUP,"commendPhoto");
	tip="ID为"+delPhotoId+"的照片已经被取消推荐!";
}
%>
<html >
<head>
</head>

<div align="center">
<font color="red"><%=tip!=null?tip:""%></font><br/>
推荐日记<br/>
<form name="form1" method="post" action="commendDiaryPhoto.jsp">
日记ID：<input type="text" name="diaryId" onKeyPress="if(event.keyCode<48||event.keyCode>57)  event.returnValue=false;" /><input type="submit" name="submit" value="确定"/>
</form>
</div>
<div align="center">
取消推荐的日记<br/>
<form name="form1" method="post" action="commendDiaryPhoto.jsp">
日记ID：<input type="text" name="delDiaryId" onKeyPress="if(event.keyCode<48||event.keyCode>57) event.returnValue=false;" /><input type="submit" name="submit" value="确定"/>
</form>
</br>
</div>



<br/>
<div align="center">
推荐相片<br/>
<form name="form1" method="post" action="commendDiaryPhoto.jsp">
相片ID：<input type="text" name="photoId" onKeyPress="if(event.keyCode<48||event.keyCode>57) event.returnValue=false;" /><input type="submit" name="submit" value="确定"/>
</form>
</br>
</div>

<div align="center">
取消推荐的相片<br/>
<form name="form1" method="post" action="commendDiaryPhoto.jsp">
相片ID：<input type="text" name="delPhotoId" onKeyPress="if(event.keyCode<48||event.keyCode>57) event.returnValue=false;" /><input type="submit" name="submit" value="确定"/>
</form>
</br>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</div>

</html>