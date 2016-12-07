<%@ page contentType="text/html;charset=utf-8"%><%!
static String colors[]={"FFFFFF","888888","FFFFFF","00FF00","0070DD","8800FF","FFFF00","FF8000","D1CC80"};
%>
<link rel="stylesheet" type="text/css" href="css/wow.css">
<TABLE cellspacing="0" cellpadding="0" class="defb"><TBODY><TR><TD class="c11 defb"></TD><TD class="c12 defb"></TD><TD class="c13 defb"></TD></TR><TR><TD class="c21 defb"></TD><TD id="toolTipBox" class="newToolBoxStyle defb">
<TABLE border="0" cellpadding="0" cellspacing="0" class="defb"><TBODY><TR><TD class="defb">
<DIV class="myTable line12">
	<span style='color:#<%=colors[item.getGrade()]%>' class="myBold myItemName"><%=item.getName()%></span><br/>
	<div class='s11'>
	<%if(item.isFlagQuest()){%><span style='color:#fefefe'>任务物品</span><br/><%}
	else if(item.isBind()){%><span style='color:#fefefe'>拾取后绑定</span><br/><%}%>
	<span><span class='s51'><span style='color:#fefefe'><%=item.getClass2Name()%></span></span><span class='s52'><span style='color:#fefefe'></span></span></span><br class='s53'/>
<%
	List attrList = item.getUsageList();
	if(attrList.size() > 0) {
		for(int ia = 0;ia < attrList.size();ia++) {
			int[] attr = (int[])attrList.get(ia);%>
			<span class="setItemGray">使用：<%=FarmWorld.usageString(attr, null)%></span><br/>
<%
		}
	}
%>
	<%if(item.getRank()>1){%><span style='color:#fefefe'>需要等级 <%=item.getRank()%></span><br/><%}%>
	出售 <%if(item.getPrice()>=10000){%><%=item.getPrice()/10000%><img alt='金币' style='margin:0px 2px -2px 2px' src='css/gcoin.gif'><%}%>	<%if(item.getPrice()>=100){%><%=item.getPrice()/100%100%><img alt='银币' style='margin:0px 2px -2px 2px' src='css/scoin.gif'><%}%><%=item.getPrice()%100%><img alt='铜币' style='margin:0px 2px -2px 2px' src='css/ccoin.gif'></div>
	<div class='ttipsp'></div>
</div>
</TD></TR></TBODY></TABLE>
</TD><TD class="c23 defb"></TD></TR><TR><TD class="c31 defb"></TD><TD class="c32 defb"></TD><TD class="c33 defb"></TD></TR></TBODY></TABLE>
