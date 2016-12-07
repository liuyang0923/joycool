<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%!
static String[] usageString = {
"无", "攻击力", "","防御+", "血+", "气力+", "体力+", "","","攻击速度+", "攻击速度",
"力量+", "敏捷+", "耐力+", "智力+", "精神+","","","","","",
"致命+","爆发+","幸运+","命中+","闪避%+","","","","","",
"金元素+","木元素+","水元素+","火元素+","土元素+","风元素+","雷元素+","","","",
};
%>
<script>
function addAttr(){
with(attrForm){
	if(attrSelect.selectedIndex==0) {
		alert("请选择属性");
		return;
	}
	attrSelected = attrSelect.options[attrSelect.selectedIndex];
	newOpt = document.createElement("OPTION");
	newOpt.value = attrSelected.value + '-' + attrValue1.value;
	newOpt.text = attrSelected.text + ' ' + attrValue1.value;
	if(attrValue2.value.length>0){
		newOpt.value += '-' + attrValue2.value;
		newOpt.text += ' ' + attrValue2.value;
	}
	attrList.options.add(newOpt, attrList.length);
}
}
function submitAttr(){
with(attrForm){
	with(attrList){
		if(options.length==0){
			alert("属性未设置");
			return;
		}
		var attr = "";
		for(var i=0;i<options.length;i++) {
			if(i>0)
				attr+=',';
			attr += options[i].value;
		}
	}
	window.opener.setAttr(attr);
}
}
</script>

<html>
<head>
</head>
<script language="JavaScript" src="../js/JS_functions.js"></script>
<link href="../common.css" rel="stylesheet" type="text/css">
<body>
	
<form name="attrForm">
<table><tr><td>
<select name="attrList" size="5" style="width:160px" multiple>
</select>
</td><td>
<input type="button"  name="" value= "上 移" onclick="moveUpSelect(document.all.attrList);"> <br>
<input type="button"  name="" value= "下 移" onclick="moveDownSelect(document.all.attrList);"> <br>
<input type="button"  name="" value= "删 除" onclick="deleteSelect(document.all.attrList);"> <br>
</td></tr></table>
<select name="attrSelect" style="width:100px" onChange="attrForm.attrValue2.value='';">
<%for(int i=0;i<usageString.length;i++)  if(usageString[i].length()>0) {%>
<option value="<%=i%>"><%=usageString[i]%></option>
<%}%>
</select>
<input type=text name="attrValue1" value="0"><input type=text name="attrValue2">
<input type="button"  name="" value= "+ 添 加" onclick="addAttr()"> <br>
	
	<input type="button" id="add" name="add" value="确认" onClick="submitAttr();window.close();">
</form>	
</body>
</html>
<script>
{
var attr = window.opener.getAttr();
}
</script>