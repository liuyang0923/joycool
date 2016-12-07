<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.cache.util.HomeCacheUtil" %><%
IHomeService homeService =ServiceFactory.createHomeService();
//用户表中的相片日记 总数和更新时间
Vector list=homeService.getHomeUserList("1=1"); 
for(int i=0;i<list.size();i++)
{//
	HomeUserBean homeuser=(HomeUserBean)list.get(i);
	int diary=homeService.getHomeDiaryCount("user_id="+homeuser.getUserId());
	int photo=homeService.getHomePhotoCount("user_id="+homeuser.getUserId());
	// macq_2006-12-20_增加家园的缓存_start
	HomeCacheUtil.updateHomeCacheById(" photo_count ="+photo+" , diary_count="+diary,"user_id="+homeuser.getUserId(),homeuser.getUserId());
	//homeService.updateHomeUser(" photo_count ="+photo+" , diary_count="+diary,"user_id="+homeuser.getUserId());
	// macq_2006-12-20_增加家园的缓存_end
}


//zhul 2006-10-11 更新用户日记相册中的review_count字段 start
Vector diaryList=homeService.getHomeDiaryList("0=0");
for(int i=0;i<diaryList.size();i++)
{
	HomeDiaryBean diary=(HomeDiaryBean)diaryList.get(i);
	int reviewCount=homeService.getHomeDiaryReviewCount("diary_id="+diary.getId());
	homeService.updateHomeDiary("review_count="+reviewCount,"id="+diary.getId());
}

Vector photoList=homeService.getPhotoList("0=0");
for(int i=0;i<photoList.size();i++)
{
	HomePhotoBean photo=(HomePhotoBean)photoList.get(i);
	int reviewCount=homeService.getHomePhotoReviewCount("photo_id="+photo.getId());
	homeService.updateHomePhoto("review_count="+reviewCount,"id="+photo.getId());
}
//zhul 2006-10-11 更新用户日记相册中的review_count字段 end
%>
