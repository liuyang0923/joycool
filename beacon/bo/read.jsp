<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.bottle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%!static BottleService service = new BottleService(); 
static final int COUNT_PRE_PAGE=5; %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="漂流瓶">
<p>
	<%
     	BottleAction action=new BottleAction(request);
		
		int id=action.getParameterInt("id");
		BottleBean bb=service.getBottleById(id);
		
		String f=action.getParameterString("f");
		
		//注意这里要输入每页显示的数据量
     	int totalCount = SqlUtil.getIntResult("select count(id) from bottle_viewer where bottle_id=" + id, 5);
     	PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
     	   
     	//取得要显示的页数。如果第一次访问，则从第1页开始
     	int pageNow = paging.getCurrentPageIndex();
     	   
     	//取得分页的SQL语句，注意这里也要输入每页显示的数据量
		ArrayList al=service.getReplyById(id,pageNow,COUNT_PRE_PAGE);
		
		//更改最后查看的时间
		service.modifyLastPickupTime(id);
		
		//这段代码是临时的。去掉一个日期中的时间部分，只保留年月日。
		String tmp=DateUtil.formatSqlDatetime(bb.getCreateTime());
		
		//这里对上面BottleBean返回的实例进行检测。如果返回的是个空值，则BottleBean的实例=NULL。
		if(bb==null || bb.getId()==0){
			out.clearBuffer();
			response.sendRedirect("null.jsp");
			return;
		}
		UserBean user = UserInfoUtil.getUser(bb.getUserId());
%><%=StringUtil.toWml(bb.getTitle())%><br />
	(
	<a href="/user/ViewUserInfo.do?userId=<%=user.getId() %>"><%=user.getNickNameWml() %></a>/<%=StringUtil.toWml(bb.getMood())%>/<%=tmp.substring(0,10)%>)
	<br />
	<%=StringUtil.toWml(bb.getContent())%><br />
----------
	<br />
	[留言]
	<br />
	<%
	if(al != null)
	{
		for(int i=0;i<al.size();i++)
		{
			BottleReply br=(BottleReply)al.get(i);
			user = UserInfoUtil.getUser(br.getUserId());
			%><%=pageNow*COUNT_PRE_PAGE+i+1%>.<%=StringUtil.toWml(br.getReply())%>(
	<a href="/user/ViewUserInfo.do?userId=<%=user.getId() %>"><%=user.getNickNameWml() %></a>/<%=DateUtil.sformatTime(br.getViewTime())%>)
	<br />
	<%
		}
	}
%><%=paging.shuzifenye("read.jsp?id="+id+"&amp;f="+f, true, "|", response)%>
	[漂流历史]
	<br />
	<%=DateUtil.sformatTime(bb.getCreateTime())%>起，它经过<%=DateUtil.formatTimeInterval3(System.currentTimeMillis()-bb.getCreateTime())%>的漂流，得到<%=bb.getReplyCount()%>份祝福
	<br />
	[给他留言](1至100字)
	<br />
	<input name="submitReply" value="" maxlength="100" />
	<br />
	<anchor>
	确定
	<go href="replay.jsp?id=<%=id%>&amp;f=<%=f%>" method="post">
	<postfield name="reply" value="$submitReply" />
	</go>
	</anchor>
	<br />
	<%//如果f变量不为空，则说明是从list.jsp页转到该页上的。
			if("reve".equals(f))
			{
				%><a href="reversion.jsp?id=<%=id%>">返回上级</a>
	<%		
			}else if ("list".equals(f))
			{
				%><a href="list.jsp?id=<%=id%>">返回上级</a>
	<%
			}else{
				%><a href="bottles.jsp?id=<%=id%>">继续捞取漂流瓶</a>
	<br />
	<a href="index.jsp">返回漂流瓶首页</a><br/>
	<a href="../../friend/friendCenter.jsp">返回交友中心</a><br/>
	<%}%><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>

