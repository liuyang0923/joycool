<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Calendar"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.job.HuntAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.job.HuntQuarryBean"%><%@ page import="net.joycool.wap.bean.job.HuntUserWeaponBean"%><%@ page import="net.joycool.wap.bean.job.HuntWeaponBean"%><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
String buy = request.getParameter("buy");
int weaponIdInt = -1;
HuntAction action = new HuntAction(request);

if (buy == null){
//不是购买武器或更换武器（从其它入口进）
	HuntUserWeaponBean userWeapon = action.getUsableHuntUserWeapon();
	if (userWeapon == null){
		// 没有武器或武器过期了(不从狩猎页进入时校验)
		out.clearBuffer();
		response.sendRedirect(("hunt.jsp?msg=2"));
		return;
	}
	weaponIdInt = userWeapon.getWeaponId();
}
if (buy != null && "1".equals(buy)){
//购买武器或更换武器
	Calendar cal = Calendar.getInstance();
	int currentHour = cal.get(Calendar.HOUR_OF_DAY);
	if (currentHour < 8 && currentHour > 0){	// 半夜不让打
		out.clearBuffer();
		BaseAction.sendRedirect("hunt.jsp?msg=4", response);
		return;
	}
	String weaponId = request.getParameter("weaponId");
	if (weaponId == null){
		out.clearBuffer();
		response.sendRedirect(("hunt.jsp?msg=2"));
		return;
	}
	weaponIdInt = Integer.parseInt(weaponId);
	//判断武器是否为狙击步枪
	if(weaponIdInt==5){
		String userBagId = request.getParameter("userBagId");
		if(userBagId==null){
			out.clearBuffer();
			BaseAction.sendRedirect("hunt.jsp?msg=3", response);
			return;
		}else{
			int checkUserBagId = StringUtil.toInt(userBagId);
			String checkUserBagResult = action.checkUserBag(checkUserBagId);
				if (!"".equals(checkUserBagResult)){
					out.clearBuffer();
					BaseAction.sendRedirect("hunt.jsp?msg=3", response);
					return;
				}
		}
	}
	// 检测用户是否有足够的乐币买该武器
	String checkResult = action.checkUserPoint(weaponIdInt);
	if (!"".equals(checkResult)){
		out.clearBuffer();
		response.sendRedirect("hunt.jsp?msg=1");
		return;
	}
	// 处理用户武器状态表
	action.dealHuntUserWeapon(weaponIdInt,request);
}

if(weaponIdInt == -1){
	//防止从地址拦输入参数进入
	out.clearBuffer();
	response.sendRedirect("hunt.jsp");
	return;
}

// 得到武器信息（根据武器id）
HuntWeaponBean huntWeapon = action.getWeapon(weaponIdInt);
// 得到一个猎物
HuntQuarryBean huntQuarry = action.getQuarryByWeapon(weaponIdInt);
session.setAttribute("quarryId",huntQuarry.getId()+"");

int rand = huntQuarry.getId();
Integer sid = (Integer)session.getAttribute("huntcheck");
if (sid != null && sid.intValue() != action.getParameterIntS("s")) // 防止刷
	rand=0;
%>

<card title="狩猎区">
<p align="left">
<%=BaseAction.getTop(request, response)%>
你在丛林中前行，突然，你发现了<%=huntQuarry.getName()%>！<br/>
<%=loginUser.showImg(Constants.JOB_HUNT_IMG_PATH+huntQuarry.getImage())%>
你决定:<br/>
<a href="huntResult.jsp?quarryId=<%=rand%>">用<%=huntWeapon.getName()%>攻击！</a><br/>
<a href="huntArea.jsp?s=<%=sid%>">不理它，继续前行</a><br/>
<br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
</wml>