<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.money.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
String[] levels={
"混沌初开,乾坤乃定,日月合璧,凤凰和鸣",
"苍蝇之飞,不过数步,附于骥尾,同胜千里",
"莫言多,莫行过,虽是千伶百俐,不如一礼二磨",
"绝妙,绝妙,云无心以出岫,鸟倦飞而归巢,花艳艳,鱼跃跃",
"绿水因风皱面,青山为雪白头,诸般皆出天造就世上水",
"不教盘算,偏要盘算,直算得三尺肠闲着",
"船到江心补漏,马临坑坎收疆,鸟入笼,水跃跃,鱼在网里",
"不是赏欣胜景,何宜踏雪寻梅,孜孜乘兴而后快快府首",
"喜细即含富此时糊涂少便买卖已得自今福径纪休夸",
"混沌初开,乾坤乃定,日月合璧,凤凰和鸣",
"落花流水突然去，大块文章尽属虚",
"打草惊蛇，敲山震虎，已行蛇窜，虎而难恐勿上错手足",
"物各有主，须但消停，云里埋尸，久是自明",
"狐假虎威，狗仗人势，弄到其间，尽是无益",
"以蠡测海，坐井观天，虽有见识，亦仍枉然",
"燕巢幕上，鱼游釜中，眼前得地，脑后生风",
"得陇望蜀，得鱼望水，天长日久，人憎枝嫌",
"奇奇海市，妙妙蜃楼，一派佳景，却在阁头",
"鹍鼠黔驴，有技有能，考其实迹，能了浪净",
"雪水烹茶，桂花煮酒，一股清味，恐难到口",
"虚而复实，实而却虚，木头产耳，窦里生鱼",
"可怜可怜，物却有限，听之费问，视而不见",
"栉风沐雨，载月披星，何时可歇，枝到三更",
"蛙鼓惊梦，虹日东升，蜻蜓飞舞，蝴蝶穿花",
"红日遮天，绿波翻滚，渔舟稳坐，长杆自捞",
"伐柯伐柯须少惊鸟，纵有神艺亦未如何",
"灯油耗尽，漏声滴响，一听鸡鸣，逍遥自歇",
"难矣哉",
"莫言多,莫行过,虽是千伶百俐,不如一礼二磨",
"万朵彩云连兰府，一轮明月落前川",
"白玉楼中吹玉笛，红梅阁上落梅花",
"春萱并茂，兰玉连临",
"霜来树静月落楼",
"一木不能支大厦",
"玉燕投怀",
"莫轻狂，细端祥，好多枝头皆朋友，落花水面尽文章",
"棘刺难折",
"须谨言慎行，恐孤掌难鸣",
"绝妙,绝妙,云无心以出岫,鸟倦飞而归巢,花艳艳,鱼跃跃",
"两手劈开名利路，一肩挑尽落阳春",
"莫气赌，莫气赌，虽有长鞭，不及马股",
"盲人骑瞎马，夜里访深池",
"真好",
"老天不容",
"天复地载，万物仰赖，鹤鸣九霄，声闻雷音",
"左右莲转，前后拥从，天人不言，言必有神",
"水中之月，镜里之花，几般妙景，落在谁家",
"海不扬波风不鸣，冬雪飞六出半空，飘飘",
"绿水因风皱面,青山为雪白头,诸般皆出天造就世上水",
"梅老偏能耐雪冷，菊残却有傲霜枝",
"能",
"一心白当阳春趣，两袖青风日月秋",
"难",
"两个黄鹂鸣翠柳，一行白鹭上青天",
"春雨发生千野绿，秋风割去一天云",
"昨夜花映犹未落，今朝露湿又重开",
"好",
"一朵红云惊鸠雀，半天长月映残风",
"不教盘算,偏要盘算,直算得三尺肠闲着",
"方离发福生财地，又入堆金积玉城",
"须放开肚皮吃饭，务站定脚根作人",
"进一步门庭，添十分春色",
"春风拂弱柳，细雨润新苗",
"心中无险事，不怕鬼叫门",
"可也",
"不能",
"割鸡之事，何用牛刀",
"雀鹊有巢，斑鸠居之",
"船到江心补漏,马临坑坎收疆,鸟入笼,水跃跃,鱼在网里",
"星移斗转去，兰里欣欣回",
"不入虎穴，焉得虎子",
"鹬蚌相持，渔翁得利",
"凤毛济美，麟趾呈祥",
"芝兰竟秀，玉树生香",
"不危不险，去而复返",
"大何测特，子谁有益",
"春暖冰化，树高鹿鸣",
"傍虎吃食，有损无益",
"不是赏欣胜景,何宜踏雪寻梅,孜孜乘兴而后快快府首",
"青虽气无东池塘",
"伐倒大树有柴烧",
"眼看明月落人家",
"有",
"一条明路，直达青天，半途而废，可叹可怜",
"执柯伐柯，则达不多，不贵手脚，更无风波",
"闲特赏月，忙里贵风，弄到手毕，内净外空",
"仰懒步地，何必自利，只须勤俭，胜是贸易",
"喜细即含富此时糊涂少便买卖已得自今福径纪休夸",
"江水洗心，江月昭肝，争南访北，不难不难",
"好好好，一了百了，不谛雷鹭，何残风扫",
"离而复合，成而必破，再费唇舌，亦未如何",
"前门拒虎，后户进狼，慎之慎之， 切勿要强",
"不作风波于面上，只无火炭在脑中",
"莫惆怅，莫惆怅，命里八尺，难求一丈",
"闲里只夸金屋好，梦中不觉玉山颓",
"猛虎斗，飞龙争，水落石出，万木皆醒",
"落花流水突然去，大块文章尽属虚",
"混沌初开,乾坤乃定,日月合璧,凤凰和鸣",
"风兼景莫乱,究竟费工夫，慎重考虑后，祸行不单孤",
"打草惊蛇，敲山震虎，已行蛇窜，虎而难恐勿上错手足",
"物各有主，须但消停，云里埋尸，久是自明",
"狐假虎威，狗仗人势，弄到其间，尽是无益",
"以蠡测海，坐井观天，虽有见识，亦仍枉然",
"燕巢幕上，鱼游釜中，眼前得地，脑后生风",
"得陇望蜀，得鱼望水，天长日久，人憎枝嫌",
"鹍鼠黔驴，有技有能，考其实迹，能了浪净",
"奇奇海市，妙妙蜃楼，一派佳景，却在阁头",
"风兼景莫乱,究竟费工夫，慎重考虑后，祸行不单孤",
"先如山倒,后若抽丝",
"天之东偶,枝枝桑榆",
"刻鹄娄鹜,书虎成狗",
"红梅结子,绿竹生笋",
"前车之覆,后车之鉴",
"获罪于天无所祷也",
"半途而废令人落泪",
"朝琢久磨其如命何",
"打草惊蛇，敲山震虎，已行蛇窜，虎而难恐勿上错手足",
"于心难忍于心难安",
"事不干己何必着意",
"求则得之舍则失之",
"管中窥豹井底观天",
"即知如此何要如此",
"知道莫影却来问谁",
"糊作在东莫人敢指",
"拨开黑雾见青天",
"文泥可以封幽笑",
"物各有主，须但消停，云里埋尸，久是自明",
"同心合意步云梯",
"一杆明月约秋风",
"掌上明珠要土埋",
"池上淤泥落凤毛",
"麟趾春生步玉堂",
"夜鸣鸟能伏鸦",
"莺鸣竟敢笑大鹏",
"青草池塘长住蛙",
"鸟兽不可兴同郡",
"柳暗花明别有天地",
"双斧伐孤树",
"牵心刺肠",
"百酸搅腹",
"寸步难行",
"痴心妄想",
"船翻洋沟",
"青云直上",
"以蠡测海，坐井观天，虽有见识，亦仍枉然",
"人为万物灵,鬼为万物精,灵而弄精,精而弄灵",
"堪愁堪忧,大被蒙头,睡而不醒,醒而云游",
"穷通有命,富贵在天,南颠北寻,尽是枉然",
"螟蚕蠡贼,布生四野,恶之不尽,走之不得",
"参居于西,商居于东,虽有方位,永不相逢",
"竹本无心多生枝叶,藕虽有孔不染垢半",
"囊内钱空,床头金尽,身居闹市,虽遇相过",
"鸟急奔树,狗急跳墙,今逢此事,切勿转向",
"燕巢幕上，鱼游釜中，眼前得地，脑后生风",
"风里烧烛，旱地拿鱼，血心虽有，名利却无",
"大之生物，因财而笃，痴心妄想，大落难顾",
"莫喜莫喜，始而无未，差之毫厘，谬胜千里",
"不揣其本，而齐其末，虽济燃眉，恐起后祸",
"套又逐月，杞人忧天，心小气大，名分茫然",
"为人谋何所图，成可赚，坏骨败",
"冰生于水而寒于水，青出于兰则胜于兰",
"可奈何，可奈何，中流见砥柱，平地起风波",
"得陇望蜀，得鱼望水，天长日久，人憎枝嫌",
"莫强求一晃悠，十年尚还有臭",
"如效成孤杜鼠宁为",
"开寒舍而搏狡兔",
"蜉蝣今夜落钱花",
"鸡胁不足安遵拳",
"狗尾续貂",
"砍竹遮笋",
"罢罢罢",
"有想功成",
"鹍鼠黔驴，有技有能，考其实迹，能了浪净",
"即知重轻何不可办，可止则止，可行则行",
"以卵撞石",
"海底捞月",
"景星入户",
"群蚁附槐",
"李生道旁",
"花发上市",
"不敢说好",
"何请海宴",
"奇奇海市，妙妙蜃楼，一派佳景，却在阁头",
"绝无所好",
"发物蒙尘",
"依枝临叶",
"一手帐天",
"破茬剖梨",
"势若催枯",
"催枯拉功",
"大器晚成",
"器满必倾",
"苍蝇之飞,不过数步,附于骥尾,同胜千里",
"雪水烹茶，桂花煮酒，一股清味，恐难到口",
"虚而复实，实而却虚，木头产耳，窦里生鱼",
"栉风沐雨，载月披星，何时可歇，枝到三更",
"蛙鼓惊梦，虹日东升，蜻蜓飞舞，蝴蝶穿花",
"红日遮天，绿波翻滚，渔舟稳坐，长杆自捞",
"伐柯伐柯须少惊鸟，纵有神艺亦未如何",
"灯油耗尽，漏声滴响，一听鸡鸣，逍遥自歇",
"绿水因风皱面,青山为雪白头,诸般皆出天造就世上水",
"不教盘算,偏要盘算,直算得三尺肠闲着",
"喜细即含富此时糊涂少便买卖已得自今福径纪休夸",
"不是赏欣胜景,何宜踏雪寻梅,孜孜乘兴而后快快府首",
"虽言富贵在天，穷通有命，亦须行之人事，以待天命",
"船到江心补漏,马临坑坎收疆,鸟入笼,水跃跃,鱼在网里",
"蛙鼓惊梦，虹日东升，蜻蜓飞舞，蝴蝶穿花",
"玉燕投怀",
"莫轻狂，细端祥，好多枝头皆朋友，落花水面尽文章",
"须谨言慎行，恐孤掌难鸣",
"鲫鱼只得西江水，鸣雷一声到九天",
"两手劈开名利路，一肩挑尽落阳春",
"莫气赌，莫气赌，虽有长鞭，不及马股",
"盲人骑瞎马，夜里访深池红日遮天，绿波翻滚，渔舟稳坐，长杆自捞",
"天复地载，万物仰赖，鹤鸣九霄，声闻雷音",
"左右莲转，前后拥从，天人不言，言必有神",
"水中之月，镜里之花，几般妙景，落在谁家",
"海不扬波风不鸣，冬雪飞六出半空，飘飘",
"秋风有意残杨柳，冷雪无声老桂花",
"梅老偏能耐雪冷，菊残却有傲霜枝",
"一心白当阳春趣，两袖青风日月秋",
"伐柯伐柯须少惊鸟，纵有神艺亦未如何",
"春雨发生千野绿，秋风割去一天云",
"昨夜花映犹未落，今朝露湿又重开",
"九天日月开品运，万里风云起壮观",
"方离发福生财地，又入堆金积玉城",
"须放开肚皮吃饭，务站定脚根作人",
"进一步门庭，添十分春色",
"春风拂弱柳，细雨润新苗",
"灯油耗尽，漏声滴响，一听鸡鸣，逍遥自歇",
"不能",
"割鸡之事，何用牛刀",
"雀鹊有巢，斑鸠居之",
"珠浆润喉，甘露滋心",
"星移斗转去，兰里欣欣回",
"不入虎穴，焉得虎子",
"鹬蚌相持，渔翁得利",
"凤毛济美，麟趾呈祥",
"不危不险，去而复返",
"难矣哉",
"春暖冰化，树高鹿鸣",
"傍虎吃食，有损无益",
"树欲静而风不息",
"青虽气无东池塘",
"伐倒大树有柴烧",
"眼看明月落人家",
"正过双星踱鹊桥",
"万朵彩云连兰府，一轮明月落前川",
"一木不能支大厦",
"若雨催残桃李色,凄风吹折扬柳枝",
"虽言富贵在天，穷通有命，亦须行之人事，以待天命",
"须谨言慎行，恐孤掌难鸣",
"九天日月开品运，万里风云起壮观",
"为伤口角,功亏一篑",
"有即难约有善难为",
"莫乐,莫乐,成而复破,总让而尽心竭利守胶锁足里黄"
};
String username=null;
String msg=null;
CustomAction action=new CustomAction(request);

boolean validate=false;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int gamePoint=us.getGamePoint();
if(request.getSession().getAttribute("characterName")!=null){
	username=(String)request.getSession().getAttribute("characterName");
}
if(request.getParameter("username")!=null){
	if(gamePoint>=1000){
		username=request.getParameter("username");
		if(username.trim().equals("")){
			msg="请输入内容";
			validate=false;
			username=null;
		}else{
			request.getSession().setAttribute("characterName",username);
			validate=true;
		}
		if(validate==true){
			int userId=0;
			
			MoneyAction moenyAction=new MoneyAction();
			userId=action.getLoginUser().getId();
			//System.out.println("before:gamePoint="+action.getLoginUser().getUs().getGamePoint());
			gamePoint=gamePoint-1000;
			if(gamePoint<=0){
				//moenyAction.addMoneyFlowRecord(Constants.OTHER,1000+gamePoint,Constants.SUBTRATION,userId);
				gamePoint=0;
			}else
			{
				//moenyAction.addMoneyFlowRecord(Constants.OTHER,1000,Constants.SUBTRATION,userId);
			}
			//action.getLoginUser().getUs().setGamePoint(gamePoint);
			//IUserService userService=ServiceFactory.createUserService(); 
			UserInfoUtil.updateUserStatus("game_point="+gamePoint,"user_id="+userId,userId,UserCashAction.GAME,"人品测试扣钱1000");
			//System.out.println("after:gamePoint="+action.getLoginUser().getUs().getGamePoint());
		}		
	}else{
		msg="您的乐币不足!";
		validate=false;
		username=null;
	}
	
}
if(validate==true){%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="main" title="载入中...">
<onevent type="ontimer">
<go href="temp.jsp"/> 
</onevent>
<timer value="1"/>
<p mode="nowrap" align="left">载入中...</p>
</card>
</wml>
<%return ;}%>
<%
String result=null;
int number=0;
if(username!=null){
	if(!username.equals("")){
		char[] arrName=null;
		//	arrName=username.trim().toCharArray();
		//	for(int i=0;i<arrName.length;i++){
		//		number+=((int)arrName[i]);
		//		number=number%100;
		//		if(number==0)
		//			number=11;
		//		number=number*3*5*7*9*13;
		//		number=number%100;
		//		number=number*17*19;
		//		number=number%100;
		//		number=number*31;
		//		number=number%100;
		//	}
		//	number=number%100;
		//	result=levels[number];
		result=levels[Encoder.getMD5(username)];
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="诸葛神推">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(msg!=null){%><%=msg%><br/><%}else{%>
您要推算事情:<br/>
<%if(username!=null&&(!username.equals(""))){%><%=StringUtil.toWml(username)%><%}%><br/>
<%--人品得分:<%if(username!=null&&(!username.equals(""))){%><%=number%><%}%><br/>--%>
推算结果为:<br/>
<%if(username!=null&&(!username.equals(""))){%><%=result%><%}%><br/>
<%}%>
继续测算:<br/>
<input name="username" maxlength="15"/><br/>
<anchor>确认提交
<go method="post" href="<%=("result.jsp") %>">
<postfield name="username" value="$username" />
</go>
</anchor><br/>
<a href="index.jsp">返回诸葛神推首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>