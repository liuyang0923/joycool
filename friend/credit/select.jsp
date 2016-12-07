<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   UserBase userBase = CreditAction.getUserBaseBean(action.getLoginUser().getId());
// UserBase userBase = action.service.getUserBase(" user_id=" + action.getLoginUser().getId());
   int type = action.getParameterInt("t") ;
   int modify = action.getParameterInt("m");
   int submit = action.getParameterInt("s");
   int sId = action.getParameterInt("sid");
   CreditProvince province = null;
   CreditCity city = null;
   List list = null;
   String tip = "";
   if (userBase == null){
		tip = "用户记录不存在.";
   } else if(sId < 0){
	   	tip = "参数选择错误.";
   } else {
	    if (submit == 1){
	       if (sId > 0) {
			   if (type == 1){
				   // 提交省
				   province = CreditAction.service.getProvince(" id=" + sId);
				   if (province != null){
					   city = CreditAction.service.getCity(" hypo=" + province.getId() + " limit 1");
					   if (city != null){
						   SqlUtil.executeUpdate("update credit_user_base set province=" + province.getId() + ",city=" + city.getId() + " where user_id=" + action.getLoginUser().getId(),5);
					  	   userBase.setProvince(province.getId());
					  	   userBase.setCity(city.getId());
					  	   CreditAction.userBaseCache.put(new Integer(userBase.getUserId()),userBase);
					  	   type = 2;
					   } else {
						   tip ="参数错误,城市不存在.";
					   }
				   } else {
					   tip ="参数错误,省份不存在.";
				   }
			   } else if (type == 2){
				   // 提交市
				   city = CreditAction.service.getCity(" id=" + sId);
				   if (city != null){
					   if (city.getHypo() == userBase.getProvince()){
						   SqlUtil.executeUpdate("update credit_user_base set city=" + city.getId() + " where user_id=" + action.getLoginUser().getId(),5);
					   	   userBase.setCity(city.getId());
					   	   CreditAction.userBaseCache.put(new Integer(userBase.getUserId()),userBase);
					   	   //userBase.setProvince(0);
					   } else {
						   tip ="参数错误,城市不存在.";
					   }
				   } else {
					   tip ="参数错误,城市不存在.";
				   }
				   // 跳到下一页
				   if (modify==1){
					   out.clearBuffer();
					   response.sendRedirect("navi.jsp");					   
				   } else {
					   out.clearBuffer();
					   response.sendRedirect("select2.jsp?t=3");   
				   }
				   return;
			   }
	       } else {
	    	   tip = "提交的参数错误.";
	       }
	    }
	    if (type == 1){
	    	list = CreditAction.service.getProvinceList(" 1");
	    } else if (type == 2){
	    	list = CreditAction.service.getCityList(" hypo = (select province from credit_user_base where user_id=" + action.getLoginUser().getId() + ")");
	    	if (list.size() == 0){
	    		tip = "您没有选择省份,或您的用户数据不存在.";
	    	}
	    } else {
	    	tip = "参数错误.";
	    }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (list.size() > 0){
if (type == 1){
	for (int i = 0 ; i < list.size() ; i++){
		province = (CreditProvince)list.get(i);
		%><a href="select.jsp?t=<%=type%>&amp;s=1&amp;sid=<%=province.getId()%>&amp;m=<%=modify%>"><%=province.getProvince()%></a>.<%
	}
}else if (type == 2){
	for (int i = 0 ; i < list.size() ; i++){
		city = (CreditCity)list.get(i);
		%><a href="select.jsp?t=<%=type%>&amp;s=1&amp;sid=<%=city.getId()%>&amp;m=<%=modify%>"><%=city.getCity()%></a>.<%
	}	
}
}
%><br/>
<%	
} else {
	%><%=tip%><br/><a href="navi.jsp">返回</a><br/><%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>