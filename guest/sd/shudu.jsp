<%@ page contentType="text/html;charset=utf-8" %><%@ page import="jc.guest.*,jc.guest.sd.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.Base2Action"%>
<% 
	ShuDuAction action = new ShuDuAction(request,response);
	GuestUserInfo guestUser = action.getGuestUser();
	int uid = 0;
	if(guestUser != null) {
		uid = guestUser.getId();
	}
	int lvl = action.getParameterInt("lvl");
	if(lvl <= 0 || lvl > 4) {
		lvl = 1;
	}
	ShuDuBean shuDuBean = (ShuDuBean) session.getAttribute("shuDuBean");
	if (shuDuBean == null || "yes".equals(request.getParameter("ano"))) {
		shuDuBean = action.getShuDu(uid,lvl);
		if (shuDuBean != null) {
			response.sendRedirect("shudu.jsp?lvl="+shuDuBean.getLvl());
			return;
		}
	}
	// 游币不足
	if (shuDuBean == null) {
		response.sendRedirect("notice.jsp?type=4&lvl="+lvl);
		return;
	}
	// 保存
	String strMiddle = request.getParameter("strMiddle");
	if(strMiddle != null && strMiddle.length() > 100) {
		if (uid > 0) {
			shuDuBean.setMiddle(strMiddle);
			action.saveShuDu(shuDuBean);
			response.sendRedirect("notice.jsp?type=2&lvl="+lvl);
		} else {
			response.sendRedirect("notice.jsp?type=2&res=1&lvl="+lvl);
		}
		return;
	}
	// 完成
	String strOver = request.getParameter("strOver");
	if(strOver != null && strOver.length() > 100){
		shuDuBean.setMiddle(strOver);
		shuDuBean.setOver(strOver);
		boolean isWin = action.smtShuDu(shuDuBean);
		if (isWin) {
			response.sendRedirect("notice.jsp?type=3&lvl="+lvl);
		} else {
			response.sendRedirect("notice.jsp?type=3&res=1&lvl="+lvl);
		}
	}
	
	String stringStart = shuDuBean.getStart();
	if(shuDuBean.getMiddle() != null && shuDuBean.getMiddle().length() > 100){
		stringStart = shuDuBean.getMiddle();
	}
	String[] conStart = stringStart.split("_");
 %><%Base2Action.getPageTop(out, "数独");%>
<style>
.cho{width:20px;height:20px;text-align:center;}
.bod1{color:black;position:absolute;width:24px;height:24px;text-align:center;background-color:#FFC0CB;border:1px solid #666;cursor:pointer;}
.bod2{color:black;position:absolute;width:24px;height:24px;text-align:center;background-color:#FFFFFF;border:1px solid #00EE00;cursor:pointer;color:blue;}
.bod3{color:black;position:absolute;width:24px;height:24px;text-align:center;background-color:#FFFFFF;border:1px solid #666;cursor:pointer;}
.bod4{color:black;position:absolute;width:24px;height:24px;text-align:center;background-color:#FFC0CB;border:1px solid #00EE00;cursor:pointer;color:blue;}
</style>
<div id="choose" style="display:none;position:absolute;z-index:100;background-color:yellow;cursor:pointer;">
<table style="color:black;" cellpadding=0 cellspacing=0>
<tr><td onclick="chooseit(1)" class="cho">1</td><td onclick="chooseit(2)" class="cho">2</td><td onclick="chooseit(3)" class="cho">3</td></tr>
<tr><td onclick="chooseit(4)" class="cho">4</td><td onclick="chooseit(5)" class="cho">5</td><td onclick="chooseit(6)" class="cho">6</td></tr>
<tr><td onclick="chooseit(7)" class="cho">7</td><td onclick="chooseit(8)" class="cho">8</td><td onclick="chooseit(9)" class="cho">9</td></tr>
</table>
</div>
 <div id="board" style="height:235px;width:235px;" align="center">
 <%for(int i=0;i<conStart.length;i++){
 		int left = 24;
		int top = 24;
		int width = 24;
		int height = 24;
 		int x;
		int y;
		if(i < 9){
			x = i*left;
			y = 0+30;
		} else {
			int temp = i/9;
			x = (i - temp*9)*left;
			y = temp*top+30;
		}	
				
		int dispStyle = 0;	
		String numTemp = conStart[i];
		String[] numi = numTemp.split(",");
		if(i >= 3 && i <= 5 || i >= 12 && i <= 14 || i >= 21 && i <= 23 || i >= 27 && i <= 29 || i >= 36 && i <= 38 || i >= 45 && i <= 47 || i >= 33 && i <= 35 || i >= 42 && i <= 44 || i >= 51 && i <= 53 || i >= 57 && i <= 59 || i >= 66 && i <= 68 || i >= 75 && i <= 77) {
			if(numi[1].equals("0")){
				dispStyle = 2;
			} else{
				dispStyle = 3;
			}
		} else {
			if(numi[1].equals("0")){
				dispStyle = 4;
			} else{
				dispStyle = 1;
			}
		}
		if (dispStyle == 1) {
 %><div class="bod1" style="left:<%=x%>px;top:<%=y%>px;" id="disp_<%=i%>"><%=numi[0]%></div><%
		} else if (dispStyle == 2) {
 %><div class="bod2" style="left:<%=x%>px;top:<%=y%>px;" id="disp_<%=i%>" onclick="addChildDiv(this)"><%if(!"0".equals(numi[0])){%><%=numi[0]%><%}%></div><%
		} else if (dispStyle == 3) {
 %><div class="bod3" style="left:<%=x%>px;top:<%=y%>px;" id="disp_<%=i%>"><%=numi[0]%></div><%
		} else if (dispStyle == 4) {
 %><div class="bod4" style="left:<%=x%>px;top:<%=y%>px;" id="disp_<%=i%>" onclick="addChildDiv(this)"><%if(!"0".equals(numi[0])){%><%=numi[0]%><%}%></div><%
		}
 }%>
 </div>
	<script>
		var dest = '<%=response.encodeURL("shudu.jsp?lvl=" + lvl)%>';
		var showShu = new Array();
		var board = document.getElementById('board');
 		var left = 24;
		var top = 24;
		var width = 24;
		var height = 24;
		var choose = document.getElementById('choose');
		var curr;
		var currIndex;
		function addChildDiv(disp){
			choose.style.display="";
			curr=disp;
			var dispId = disp.getAttribute("id");
			currIndex=dispId.split('_')[1];
			
			var row = Math.floor(currIndex/9);
			var col = currIndex % 9;

			choose.style.left=col*18+6;
			choose.style.top=row*18+36;
		}
		
		function chooseit(v) {
			if(curr)
				curr.innerHTML=v;
			var numi1 = showShu[currIndex];
			numi1 = v + ",0";
			showShu[currIndex] = numi1;
		   	choose.style.display="none";
		}
		
		function getStrShuDu() {
			var stringShowShu="";
			for (var i = 0; i < showShu.length; i++) {
				stringShowShu = stringShowShu + showShu[i] + "_";
			}
			stringShowShu = stringShowShu.substring(0,stringShowShu.length-1);
			return stringShowShu;
		}
		
		function checkGame() {
			add('strOver', getStrShuDu());
			go(dest);
			return false;
		}
			
		function saveGame() {
			add('strMiddle', getStrShuDu());
			go(dest);
			return false;
		}
		
		function load(){
			<%
			for(int i=0;i<conStart.length;i++){
			%>showShu[<%=i%>] = "<%=conStart[i]%>";<%
			}
			%>
		}
		window.onload = function() {
			load();
		}
	</script>
<a href="<%=response.encodeURL("notice.jsp?type=1&amp;lvl="+lvl)%>">重玩</a>.
<a href="#" onclick="return saveGame();">保存</a>.
<a href="#" onclick="return checkGame();">完成</a><br/>
<a href="<%=response.encodeURL("/guest/index.jsp")%>">返回游乐园</a><br/>
<%=BaseAction.getBottomShort(request, response)%><%Base2Action.getPageBottom(out);%>