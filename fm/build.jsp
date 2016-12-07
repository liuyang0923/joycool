<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*,net.joycool.wap.bean.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmUser=action.getFmUser();
UserBean userbean=action.getLoginUser();
int cmd=action.isBuildFm();
FmApplyUser fmappUser=action.service.selectFamilyApplyUser(userbean.getId(),1,true);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="申请建立家族"><p align="left"><%
if(fmUser!=null&&fmUser.getFm_id()!=0){%>
您必须退出现在的家族才能建立新的家族!<br/><%
}else if (fmUser!=null&&fmUser.isLeaveDate()!=Constants.MIN_LEAVE_DATE){%>
您解散家族时间不足<%=Constants.MIN_LEAVE_DATE%>天,还差<%=Constants.MIN_LEAVE_DATE - fmUser.isLeaveDate()%>天<br/><%
}else if (action.getFmId()!=0){%>
您已加入帮会,不能再申请建立家族<br/><%
}else if (fmappUser!=null&&fmappUser.getIs_apply()==1){%>
您已经创建了一个家族,不能创建第二个!<br/><%
}else{
%>家族是乐酷社区的基础,是用户沟通交流的好去处.在家族可以一起玩团队游戏,增进感情哦!!!<br/>
家族建立需求:<br/>
1:1亿资金<br/>
2:等级大于15级<br/>
3:社交值达到500<br/>
4:需要邀请至少4个人一起参与申请<br/>
————————<br/><%
if(cmd==2){%>
请输入家族名称(不得超过6个字)<br/>
<input name="fname" maxlength="6" /><br/>
<anchor>确定缴纳1亿乐币并下一步
  <go href="tobuild.jsp?cmd=1" method="post">
    <postfield name="fname" value="$(fname)" />
  </go>
</anchor><br/><%
}else{
String game_point=(String)request.getAttribute("game_point");
String rank=(String)request.getAttribute("rank");
String social=(String)request.getAttribute("social");
%>很遗憾,(<%=game_point==null?"":game_point+","%><%=rank==null?"":rank+","%><%=social==null?"":social+","%>)所以您还不能建立家族!<br/><%
}
}%>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>