<%@ page language="java" import="net.joycool.wap.bean.PagingBean,jc.family.*,net.joycool.wap.util.*,jc.family.game.boat.*,jc.family.game.*,java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
FamilyAction action = new FamilyAction(request);
FamilyMedalBean bean = null;
String tip = "";
int did = action.getParameterInt("did");
if (did > 0){
	List list2 = FamilyAction.service.getMedalList(" id=" + did);
	if (list2 == null || list2.size() == 0)
		tip = "这枚勋章尚未授予任何家族.";
	else {
		bean = (FamilyMedalBean)list2.get(0);
		if (bean != null){
			FamilyHomeBean fmBean = FamilyAction.getFmByID(bean.getFmId());
			if (fmBean != null){
				// 删除缓存
				fmBean.setMedalList(null);
				// 删除文件
				java.io.File f = new java.io.File(net.joycool.wap.util.Constants.RESOURCE_ROOT_PATH + "family/medal/" + bean.getImg());
				if (f.exists()) {
					f.delete();
				}
				// 删除数据库
				SqlUtil.executeUpdate("delete from fm_medal where id=" + bean.getId() , 5);
			}
		}
	}
}
int count = SqlUtil.getIntResult("select count(id) from fm_medal",5);
PagingBean paging = new PagingBean(action, count, 30, "p");
List list = FamilyAction.service.getMedalList("1 order by id desc limit "+paging.getStartIndex()+",30" );
 %>
<html>
  <head>
    <title>勋章管理</title>
 <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
 <%="".equals(tip)?"":tip%><br>
<input id="cmd" type="button" value="增加新勋章" onClick="javascript:window.location.href='addmedal.jsp'"><br/>
   <table border="1" width="100%">
    <tr>
     <td align="center">ID</td>
     <td align="center">家族ID</td>
     <td align="center">图片</td>
     <td align="center">排序</td>
     <td align="center">勋章名称</td>
     <td align="center">详细介绍</td>
     <td align="center">获取时间</td>
     <td align="center">结止时间</td>
     <td align="center">操作</td>
    </tr>
    <%
     if(list != null && list.size() > 0){
      for(int i=0;i<list.size();i++){
       bean = (FamilyMedalBean) list.get(i);
       if(bean != null){
       %>
       <tr>
        <td align="center"><%=bean.getId()%></td>
        <td align="center"><%=bean.getFmId()%></td>
        <td align="center"><img src="/rep/family/medal/<%=bean.getImg()%>" alt="杯具了"></td>
        <td align="center"><%=bean.getSeq()%></td>
        <td align="center"><%=StringUtil.toWml(bean.getName())%></td>
        <td align="center"><%=StringUtil.toWml(bean.getInfo())%></td>
        <td align="center"><%=DateUtil.formatTime(bean.getCreateTime())%></td>
        <td align="center"><%=DateUtil.formatTime(bean.getEndTime())%></td>
        <td align="center"><a href="medal.jsp?did=<%=bean.getId()%>" onClick="return confirm('真的要删除这枚勋章吗?')">删除</a>|<a href="modifyMedal.jsp?id=<%=bean.getId()%>">修改</a></td>
       </tr>
       <%
       }
      }
     }
     %>
   </table>
   <%=paging.shuzifenye("medal.jsp", false, "|", response)%>
 <a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>
