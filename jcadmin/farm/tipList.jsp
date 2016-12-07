<%@ page contentType="text/html;charset=utf-8"%><%!
static String colors[]={"FFFFFF","888888","FFFFFF","00FF00","0070DD","8800FF","FFFF00","FF8000","D1CC80"};
static String attrColors[]={
"fefefe","fefefe","fefefe","fefefe","fefefe","fefefe","fefefe","fefefe","fefefe","00FF00","fefefe",
"fefefe","fefefe","fefefe","fefefe","fefefe","fefefe","fefefe","fefefe","fefefe","fefefe",
"00FF00","00FF00","00FF00","00FF00","00FF00","00FF00","00FF00","00FF00","00FF00","00FF00",
"0070DD","0070DD","0070DD","0070DD","0070DD","0070DD","0070DD","00FF00","00FF00","00FF00",
};
%>
<link rel="stylesheet" type="text/css" href="css/wow.css">
<%
for(int i=0;i<tipList.size();i++){
DummyProductBean item = (DummyProductBean)tipList.get(i);
ItemSetBean set = FarmWorld.one.getItemToSet(item.getId());
%>
<div id='item<%=item.getId()%>' class="toolTipContainerStyle" STYLE='left: 1087px; top: 464px; display: none'>
<TABLE cellspacing="0" cellpadding="0" class="defb"><TBODY><TR><TD class="c11 defb"></TD><TD class="c12 defb"></TD><TD class="c13 defb"></TD></TR><TR><TD class="c21 defb"></TD><TD id="toolTipBox" class="newToolBoxStyle defb">
<TABLE border="0" cellpadding="0" cellspacing="0" class="defb"><TBODY><TR><TD class="defb">
<DIV class="myTable line12">
	<span style='color:#<%=colors[item.getGrade()]%>' class="myBold myItemName"><%=item.getName()%></span><br/>
	<div class='s11'>
	<%if(item.isBind()){%><span style='color:#fefefe'>拾取后绑定</span><br/><%}%>
	<span><span class='s51'><span style='color:#fefefe'><%=item.getClass2Name()%></span></span><span class='s52'><span style='color:#fefefe'></span></span></span><br class='s53'/>
<%
	List attrList = item.getAttributeList();
	if(attrList.size() > 0) {
		for(int ia = 0;ia < attrList.size();ia++) {
			int[] attr = (int[])attrList.get(ia);%>
			<span style='color:#<%=attrColors[attr[0]]%>'><%=FarmWorld.itemString(attr, null)%></span><br/>
<%
		}
	}
%>
<span style='color:#fefefe'>耐久度：<%=item.getTime()%>/<%=item.getTime()%></span><br/>
	<%if(item.getRank()>1){%><span style='color:#fefefe'>装备等级 <%=item.getRank()%></span><br/><%}%>
<%if(item.getIntroduction().length()>0){%><span class="myYellow">“<%=item.getIntroduction()%>”</span><br/><%}%>
	<%if(set!=null){%>
	<span class="setNameYellow"><%=set.getName()%>（0/<%=set.getItemCount()%>）</span>
	<DIV class="setItemIndent">
<% List itemList2 = set.getItemList();
for(int si = 0;si < itemList2.size();si++){
Integer iid = (Integer)itemList2.get(si);
DummyProductBean item2=FarmWorld.getItem(iid.intValue());%>
<br/><SPAN class="setItemGray">
<%if(item2 == null){
%>(未知)<%}else{%>
<%=item2.getName()%>
<%}%>
</SPAN>
<%}%>
</DIV><BR/>
	<%
	attrList = set.getAttributeList();
	List countList = set.getCountList();
	for(int ia = 0;ia < attrList.size();ia++) {
		int[] attr = (int[])attrList.get(ia);
		Integer c = (Integer)countList.get(ia);%>
		<span class="setItemGray">(<%=c%>) 套装： <%=FarmWorld.itemString(attr, null)%></span><br/>
	<%}%>
	<%}%>
	出售 <%if(item.getPrice()>=10000){%><%=item.getPrice()/10000%><img alt='金币' style='margin:0px 2px -2px 2px' src='css/gcoin.gif'><%}%>	<%if(item.getPrice()>=100){%><%=item.getPrice()/100%100%><img alt='银币' style='margin:0px 2px -2px 2px' src='css/scoin.gif'><%}%><%=item.getPrice()%100%><img alt='铜币' style='margin:0px 2px -2px 2px' src='css/ccoin.gif'></div>
	<div class='ttipsp'></div>
</div>
</TD></TR></TBODY></TABLE>
</TD><TD class="c23 defb"></TD></TR><TR><TD class="c31 defb"></TD><TD class="c32 defb"></TD><TD class="c33 defb"></TD></TR></TBODY></TABLE>
</div>
<%}%>