<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.File"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static IUserService userService = ServiceFactory.createUserService();
Integer zero = new Integer(0);
static String[] typeName = {"","帖子图片","回复图片","聊天图片","用户头像","家园相册","图库","可信度","家族","家族相册"};
static String[] paths = {"","jcforum/","jcforum/","chat/","friend/attach/","home/myalbum/","","credit/","family/","family/photo/"};

static void processCheck(int type, String finalFile, int id2, int res, int del, String realfile){
switch(type){
		case 1: {
			net.joycool.wap.cache.util.ForumCacheUtil.updateForumContent("attach='"+finalFile+"'","id="+id2,id2);
		} break;
		case 2: {
			net.joycool.wap.cache.util.ForumCacheUtil.updateForumReply("attach='"+finalFile+"'","id="+id2,id2);
		} break;
		case 3: {
		} break;
		case 4: {
			net.joycool.wap.action.friend.FriendAction.getFriendService().updateFriend(
					"attach='" + finalFile+"'", "user_id=" + id2);
			net.joycool.wap.action.friend.FriendAction.getFriendService().flushFriend(id2);
		} break;
		case 5: {
			SqlUtil.executeUpdate("update jc_home_photo set attach='"+finalFile+"' where id="+id2);
		} break;
		case 6: {
			SqlUtil.executeUpdate("update img_lib set img='"+finalFile+"',flag=" + (res==1?2:1) + " where id="+id2,5);
		} break;
		case 7: {
			SqlUtil.executeUpdate("update credit_user_base set photo='"+finalFile+"' where user_id="+id2,5);
		   	jc.credit.UserBase userBase = jc.credit.CreditAction.getUserBaseBean(id2);
		   	if (userBase != null){
			   	userBase.setPhoto(finalFile);
			   	jc.credit.CreditAction.userBaseCache.put(new Integer(userBase.getUserId()), userBase);
		   	}
		} break;
		case 8:{
			jc.family.FamilyAction.checkLogoImg(id2,finalFile);
		} break;
		case 9:{
			jc.family.photo.FmPhotoAction.checkOver(id2,finalFile);
		} break;
		}
		if(res==1){
			String fullPath=Constants.RESOURCE_ROOT_PATH+paths[type]+realfile;
			if(realfile.startsWith("/"))
				fullPath=Constants.RESOURCE_ROOT_PATH+realfile.substring(1,realfile.length());
			File f1 = new File(fullPath);
			if(f1.exists()){
		        f1.delete();
		    }
	    }
	    SqlUtil.executeUpdate("insert ignore into img_check_his select * from img_check where id="+del);
	    SqlUtil.executeUpdate("delete from img_check where id="+del);
}

%><%

CustomAction action = new CustomAction(request);
int del = action.getParameterInt("del");
if(del>0) {
	List logList = SqlUtil.getObjectsList("select id,id2,type,file,create_time,bak,pool_id from img_check where id="+del);
	if(logList.size()!=0){
		Object[] objs = (Object[])logList.get(0);
		int res = action.getParameterInt("res");
		int type=((Long)objs[2]).intValue();
		int id2=((Long)objs[1]).intValue();
		String finalFile=null;
		int poolId = ((Long)objs[6]).intValue();
		if(res==0){	// 审核通过
			finalFile=objs[3].toString();
			
			if(poolId != 0) {
				SqlUtil.executeUpdate("update img_pool set flag=1 where id="+poolId, 5);
			}
		}else if(res==1){	// 审核失败
			finalFile="x.gif";
	
			if(poolId != 0) {
				SqlUtil.executeUpdate("delete from img_pool where id="+poolId, 5);
			}
		}
		if(finalFile!=null) {
			if(poolId!=0){
				logList = SqlUtil.getObjectsList("select id,id2,type,file,create_time,bak,pool_id from img_check where pool_id="+poolId);
				for(int i = 0;i < logList.size();i++){
					Object[] objs2 = (Object[])logList.get(i);
					type=((Long)objs2[2]).intValue();
					int del2=((Long)objs2[0]).intValue();
					id2=((Long)objs2[1]).intValue();
					processCheck(type, finalFile, id2, res, del2, objs2[3].toString());
				}
			} else {
				processCheck(type, finalFile, id2, res , del,objs[3].toString());
			}
		}
	}
}

PagingBean paging = new PagingBean(action,10000,30,"p");
%>
<html>
<head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<font color=red>先审核再发</font>-<a href="img.jsp?p=<%=paging.getCurrentPageIndex()%>">刷新</a>
<table width="800" border="2">
<tr>
<td>id</td><td>id2</td>
<td>用户</td>
<td>时间</td>
<td align=center>类型</td>
<td>结果</td>
</tr>
<%
//显示封禁列表
List logList = SqlUtil.getObjectsList("select id,id2,type,file,create_time,bak from img_check order by id limit " + paging.getStartIndex()+",30");
if(logList != null){
	Iterator itr = logList.iterator();
	int i = 1;
	while(itr.hasNext()){
	Object[] objs = (Object[])itr.next();
	int type=((Long)objs[2]).intValue();
	String imgfile = objs[3].toString();
%>
<tr>
<td width="60"><%=objs[0]%></td><td width="60"><%=objs[1]%></td>
<td width="50"></td>
<td width="120"><%=objs[4]%></td>
<td align=center width=100><%=typeName[type]%></td>
<td><img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%><%if(!imgfile.startsWith("/")){%>/<%=paths[type]%><%}%><%=imgfile%>" alt="x"/></td>
<td><a href="img.jsp?res=0&del=<%=objs[0]%>&p=<%=paging.getCurrentPageIndex()%>" onclick="return confirm('确认通过审核？')"><font color=green>审核通过</font></a>-
<a href="img.jsp?res=1&del=<%=objs[0]%>&p=<%=paging.getCurrentPageIndex()%>"><font color=red>不通过</font></a></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<%=paging.shuzifenye("img.jsp",false,"|",response)%><br/>
<a href="imghis.jsp">查看审核过的图片列表</a><br/>
<p align="center"><a href="../chatmanage.jsp">返回</a></p>
<body>
</html>