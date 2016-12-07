<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static int[] fieldPrice1 = {0,20000,40000,50000,800000}; //(第一块地)依次为雪山，山崖，平原，河畔的价格
	static int[] fieldExp1 = {0,0,500,1200,2500};			 //(第一块地)依次为雪山，山崖，平原，河畔的成就值
	static int[] fieldPrice = {0,40000,60000,80000,100000};  //依次为雪山，山崖，平原，河畔的价格
	static int[] fieldExp = {0,3000,2000,4000,8000};		 	 //依次为雪山，山崖，平原，河畔的成就值
	static String[] intro = {"","描述:常年积雪覆盖,脆弱的植物都被这厚厚的积雪所覆盖,只有顽强的植物才能屹立在白雪皑皑的雪山中.",
							"描述:陡峭的悬崖壁上很难生长出植物,只有顽强的生物才能在这里生长发芽.<br/>特点:相比气候恶劣的雪山,种植时间缩短10%.",
							"描述:一望无际的平原上生长着各种珍奇的物种,肥沃的土地为各种植物提供者所需的营养.<br/>特点:相比气候恶劣的雪山,种植时间缩短15%.",
							"描述:轻轻的河水,孕育着河水两岸生长的植物,非常适合各种花草生长.<br/>特点:相比气候恶劣的雪山,种植时间缩短20%."};
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List fieldTypeList = FlowerUtil.getFieldTypeList();
	List al = FlowerUtil.getFieldTypeList();
	FieldBean fb = null;
	int id = action.getParameterInt("id");		//待升级土地的id
	fb = service.getFieldById(id);				//待升级土地的类型
	if (fb == null){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	int fid = fb.getType();
	if (fid <= 0){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	if (fid >= fieldTypeList.size() -1){
		action.sendRedirect("mess.jsp?e=13",response);
		return;
	}
	if (id <= 0){
		action.sendRedirect("mess.jsp?e=3",response);
		return;	
	}
	// 取得用户第一块土地的id
	FlowerUserBean fub = FlowerUtil.getUserBean(action.getLoginUserId());
	List fieldList = service.getField(action.getLoginUserId());
	fb = (FieldBean)fieldList.get(0);
	int firstFieldId = fb.getId();
	int u = action.getParameterInt("u");
	boolean result = false;
	if ( u==1 ){
		if (firstFieldId == id){
			result = action.updateField(id,fid+1,fieldPrice1[fid+1],fieldExp1[fid+1],fieldList);
		}else{
			result = action.updateField(id,fid+1,fieldPrice[fid+1],fieldExp[fid+1],fieldList);
		}
		if (!result){
			int errorNum = action.getAttributeInt("err");
			action.sendRedirect("update2.jsp?err=" + errorNum,response);
			return;
		}
	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<% if (result){
	//升级成功
		%>你成功的把你的养殖地升级为<%=al.get(fid + 1)%>!<br/>
		<% if ( fid + 2 < fieldTypeList.size() ){
				%>*下一级*<br/>
				<%if (firstFieldId == id){
					%>【<%=al.get(fid+2)%>】需要<%=fieldPrice1[fid+2]%>金币,扣除<%=fieldExp1[fid+2]%>点成就值<br/><%
				  } else {
					%>【<%=al.get(fid+2)%>】需要<%=fieldPrice[fid+2]%>金币,扣除<%=fieldExp[fid+2]%>点成就值<br/><%		  
				  }%>
				 <%=intro[fid+2]%><br/>	
		<%}%>
 <%}else{
	//没有升级
		%>你现在的土地是<%=al.get(fid)%>,可以升级为<%=al.get(fid + 1)%>!<br/>
		你拥有<%=fub.getExp() - fub.getUsedExp()%>点成就值<br/>
		<%if (firstFieldId == id){
			%>【<%=al.get(fid+1)%>】需要<%=fieldPrice1[fid+1]%>金币,扣除<%=fieldExp1[fid+1]%>点成就值<br/><%
	    } else {
			%>【<%=al.get(fid+1)%>】需要<%=fieldPrice[fid+1]%>金币,扣除<%=fieldExp[fid+1]%>点成就值<br/><%		  
	    }%>
		<%=intro[fid+1]%><br/>
		<a href="update.jsp?u=1&amp;fid=<%=fid%>&amp;id=<%=id%>">确定升级</a><br/>
 <%}%>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>