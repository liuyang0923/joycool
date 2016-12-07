<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.Base2Action"%>
<%!
public static int width=40;
public static int hight=40;
public static String[] roles = {"","曹操","张<br/>飞","赵<br/>云","马<br/>超","关羽","黄<br/>忠","兵","兵","兵","兵"};
public static String[] style = {"","bod1","bod2","bod3","bod4","bod5","bod6","bod7","bod7","bod7","bod7"};
public static String[] paddings = {"","28","16","16","16","8","16","8","8","8","8"};
public static int [][] view = {
	{2,1,1,3},
	{2,1,1,3},
	{4,5,5,6},
	{4,7,8,6},
	{9,0,0,10}
}; 
public static int [][] choosePosition = {
	{},
	{12,5},
	{-10,5},
	{-10,5},
	{-10,5},
	{12,-10},
	{-10,5},
	{-10,-10},
	{-10,-10},
	{-10,-10},
	{-10,-10}
};
%><%Base2Action.getPageTop(out, "华容道");%>
<style>
.cho{width:10px;height:10px;text-align:center;}
.bod1{position:absolute;width:79px;height:79px;text-align:center;background-color:yellow;border:1px solid #666;cursor:pointer;color:blue;}
.bod2{position:absolute;width:39px;height:79px;text-align:center;background-color:#BBBBAA;border:1px solid #666;cursor:pointer;color:blue;}
.bod3{position:absolute;width:39px;height:79px;text-align:center;background-color:#FFFFFF;border:1px solid #666;cursor:pointer;color:blue;}
.bod4{position:absolute;width:39px;height:79px;text-align:center;background-color:#FFBBBB;border:1px solid #666;cursor:pointer;color:blue;}
.bod5{position:absolute;width:79px;height:39px;text-align:center;background-color:red;border:1px solid #666;cursor:pointer;color:blue;}
.bod6{position:absolute;width:39px;height:79px;text-align:center;background-color:orange;border:1px solid #666;cursor:pointer;color:blue;}
.bod7{position:absolute;width:39px;height:39px;text-align:center;background-color:#FFFFAA;border:1px solid #666;cursor:pointer;color:blue;}
</style>
<div id="choose" style="display:none;position:absolute;z-index:100;background-color:#ABCDEF;cursor:pointer;">
<table border="1" style="color:green;" cellpadding=0 cellspacing=0>
<tr>
<td class="cho"></td>
<td onclick="chooseit(-1,0,0)" class="cho">上</td>
<td class="cho"></td>
</tr>
<tr>
<td onclick="chooseit(0,-1,0)" class="cho">左</td>
<td onclick="chooseit(0,0,0)" class="cho">x</td>
<td onclick="chooseit(0,1,1)" class="cho">右</td>
</tr>
<tr>
<td class="cho"></td>
<td onclick="chooseit(1,0,1)" class="cho">下</td>
<td class="cho"></td>
</tr>
</table>
</div>
<% 
for (int i=1;i<roles.length;i++) {
%><div id="<%=i%>" onclick="addChildDiv(this)" class="<%=style[i]%>"><div style="margin-top:<%=paddings[i]%>px"><%=roles[i]%></div></div><%
}
%>
<div id="board" style="height:235px;width:235px;" align="center"></div>
	<script>
		var widthFb = <%=width%>;
		var hightFb = <%=hight%>;
		var viewFb = new Array(); // 地图
		var choosePositionFB = new Array(); // 点击不同人物方向键出现点参数
		var roleHasDrowed = new Array(); // 已经画完的角色
		var movePrepare = new Array(); // 将要移动的人物所在点
		var chooseBload = document.getElementById('choose'); // 方向键
		var currRole; // 玩家点击的角色
		var currI; // 玩家点击的角色所在点I
		var currJ; // 玩家点击的角色所在点J
		var isWin = false; // 是否胜利
		
		function checkWin() {
			if (viewFb[3][1]==1 && viewFb[3][2]==1 && viewFb[4][1]==1 && viewFb[4][2]==1) {
				isWin = true;
			}
		}
		
		function drowRoleDiv(){
			var roleNumId = 0;
			var count = 0;
			for(var i=0;i<viewFb.length;i++){
				for (var j=0;j<viewFb[i].length;j++){
					if (viewFb[i][j] == 0) continue;
					if (viewFb[i][j] != roleNumId) {
						var isDrow = true;
						for (var z=0;z<roleHasDrowed.length;z++){
							if (roleHasDrowed[z] == viewFb[i][j]){	
								isDrow = false;
								break;
							}
						}
						if (isDrow) {
							roleHasDrowed[count] = viewFb[i][j];
							roleNumId = viewFb[i][j];
							count++;
							var role = document.getElementById(roleNumId);
							role.style.top=30+i*<%=hight%>;
							role.style.left=j*<%=width%>;
						}
					}
				}
			}
			roleHasDrowed = new Array();
		}
		
		function canMove(checkI,checkJ) {
			if (checkI < 0 || checkJ < 0 || checkI >= viewFb.length || checkJ >= viewFb[0].length){
				return false;
			}
			if (viewFb[checkI][checkJ] == 0) {
				return true;
			}
			if (viewFb[checkI][checkJ] == currRole) {
				return true;
			}
			return false;
		}
		
		function insertMovePrepare(i,j,numI,numJ,count) {
			if (!canMove(i+numI,j+numJ)){
				movePrepare = new Array();
				return false;
			}
			movePrepare[count] = new Array();
			movePrepare[count][0] = i;
			movePrepare[count][1] = j;
			return true;
		}
		
		function checkFromLeft(numI,numJ) {
			var count = 0;
			for (var i=0;i<viewFb.length;i++){
				for (var j=0;j<viewFb[i].length;j++){
					if (viewFb[i][j] != currRole) {
						continue;
					}
					if (!insertMovePrepare(i,j,numI,numJ,count)) {
						return false;
					} else {
						count++;
					}
				}
			}
			return true;
		}
		
		function checkFromRight(numI,numJ) {
			var count = 0;
			for (var i=viewFb.length-1;i>=0;i--){
				for (var j=viewFb[i].length-1;j>=0;j--){
					if (viewFb[i][j] != currRole) {
						continue;
					}
					if (!insertMovePrepare(i,j,numI,numJ,count)) {
						return false;
					} else {
						count++;
					}
				}
			}
			return true;
		}
		
		function chooseit(numI,numJ,from) {
			chooseBload.style.display="none";
			if (numI==0 && numJ==0) return;
			var startMove = true;
			if (from == 0) {
				if (!checkFromLeft(numI,numJ)) {
					startMove = false;
				}
			} else {
				if (!checkFromRight(numI,numJ)) {
					startMove = false;
				}
			}
			if (startMove) {
				for (var i=0;i<movePrepare.length;i++){
					var moveI = movePrepare[i][0];
					var moveJ = movePrepare[i][1];
					viewFb[moveI][moveJ] = 0;
					viewFb[moveI + numI][moveJ + numJ] = currRole;
				}
				movePrepare = new Array();
				drowRoleDiv();
				checkWin();
				if (isWin) {
					alert("云长果然有勇有谋,忠义两全,今日孟德得以逃生,来日定当相报!");
				}
			}
		}
		
		
		function addChildDiv(disp) {
			if (isWin) {
				alert("已经胜利了哦!");
				return;
			}
			var roleId = disp.getAttribute("id");
			var search = false;
			for(var i=0;i<viewFb.length;i++){
				for (var j=0;j<viewFb[i].length;j++){
					if (viewFb[i][j] == roleId){
						currRole = roleId;
						currI = i;
						currJ = j;
						search = true;
						break;
					}
				}
				if (search) {break;}
			}
			chooseBload.style.top=30+currI*hightFb+Number(choosePositionFB[roleId][1]);
			chooseBload.style.left=currJ*widthFb+Number(choosePositionFB[roleId][0]);
			chooseBload.style.display="";
		}
		
		function load(){
		 	var m=0; 
			<%
			for(int i=0;i<view.length;i++){
				%>viewFb[m]=new Array();var n=0;<%
				for (int j=0;j<view[i].length;j++){
				%>viewFb[m][n] = "<%=view[i][j]%>";n++;<%
				}%>
				m++;<%
			}
			%>
			var z=0;
			<%
			for(int i=0;i<choosePosition.length;i++){
				%>choosePositionFB[z]=new Array();var n=0;<%
				for (int j=0;j<choosePosition[i].length;j++){
				%>choosePositionFB[z][n] = "<%=choosePosition[i][j]%>";n++;<%
				}%>
				z++;<%
			}
			%>
			drowRoleDiv();
		}
		window.onload = function() {
			load();
		}
	</script>
<a href="<%=response.encodeURL("/guest/hrd/hrd.jsp")%>">重新开始</a><br/>
<a href="<%=response.encodeURL("/guest/index.jsp")%>">返回游乐园</a><br/>
<%=BaseAction.getBottomShort(request, response)%><%Base2Action.getPageBottom(out);%>