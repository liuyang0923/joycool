package net.joycool.wap.bean.pk;

import net.joycool.wap.action.pk.PKAction;

public class PKMObjBean extends PKProtoBean{
	/**
	 *  
	 * @author macq
	 * @explain： 物品属性信息
	 * @datetime:2007-3-8 13:51:47
	 * @return
	 * @return String
	 */
	public String toDetail() {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src=\"" + PKAction.PK_IMAGE_PATH + "/mobj/"
				+ id + ".gif\" alt=\"\"/>");
		sb.append("<br/>");
		sb.append(name);
		sb.append("<br/>");
		return sb.toString();
	}
}
