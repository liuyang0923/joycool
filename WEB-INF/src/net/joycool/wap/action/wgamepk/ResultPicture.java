package net.joycool.wap.action.wgamepk;

import java.util.Random;

public class ResultPicture {
	// 共有多少组图片
	public static int picture_num = 3;

	public static String getPicture(int result) {
		
		StringBuffer str = new StringBuffer();
		
		str.append("<img src=\"" + getStr(result) + "\" alt=\"loading\"/><br/>");

		return str.toString();
	}
	
	public static String getStr(int result) {
		String str = "";
		Random rand = new Random();
		int num = Math.abs(rand.nextInt() % picture_num) + 1;
		if (result == 0) {
		     str = "../../img/gameresult/"+num + "_0.gif";
		} else if (result == 1) {
			str = "../../img/gameresult/"+num + "_1.gif";
		} else if (result == 2) {
			str = "../../img/gameresult/"+num + "_2.gif";
		};
		return str;
	}
}
//<img src="../img/questions/gamequestion.gif" alt="问答接龙"/><br/>
//attachCode.append("<img src=\"" + attach.getFileURL() + "\" alt=\"loading\"/><br/>");