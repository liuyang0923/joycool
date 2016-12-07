package net.joycool.wap.action.huangye;

import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.friendlink.LinkRecordBean;
import net.joycool.wap.bean.huangye.JCLinkHuangYeBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendLinkService;
import net.joycool.wap.service.infc.IHuangYeService;
import net.joycool.wap.util.StringUtil;

public class HuangYeAction {

	public int PUBLIC_NUMBER_PER_PAGE = 25;

	IHuangYeService huangyeService = ServiceFactory.createHuangYeService();

	IFriendLinkService friendlinkService = ServiceFactory
			.createFriendLinkService();

	public void search(HttpServletRequest request) {
		JCLinkHuangYeBean top = null;
		JCLinkHuangYeBean middle = null;
		LinkRecordBean topRecord = null;
		LinkRecordBean middleRecord = null;
		int toplinkId = 0;
		// 获取4个随机得置顶link信息
		Vector topList = huangyeService
				.getJCLinkHuangYeList("mark=1 order by rand() limit 0,4 ");

		// 取得公聊参数
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		String number = request.getParameter("number");
		if (number != null) {
			int number1 = Integer.parseInt(number);
			pageIndex = number1 - 1;
		}
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		String prefixUrl = "index.jsp?backTo=" + URLEncoder.encode(backTo);
		int totalCount = huangyeService.getHuangYeRecords(0);
		// 判断公聊信息数量
		int totalPageCount = totalCount / PUBLIC_NUMBER_PER_PAGE;
		if (totalCount % PUBLIC_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得要显示的消息列表
		int start = pageIndex * PUBLIC_NUMBER_PER_PAGE;
		int end = PUBLIC_NUMBER_PER_PAGE;
		// 按当前页码得到中部link信息
		Vector middleList = huangyeService
				.getJCLinkHuangYeList("mark=0 order by number asc limit "
						+ start + ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("backTo", backTo);
		request.setAttribute("prefixUrl", prefixUrl);

		// 传值到页面
		request.setAttribute("topList", topList);
		request.setAttribute("middleList", middleList);
	}

	// 添加友链
	public void addFriend(HttpServletRequest request) {
		String friendlinkName = request.getParameter("friendlinkName");
		String friendlinkaddress = request.getParameter("friendlinkaddress");
		String tip = null;
		boolean flag = true;
		int b = friendlinkaddress.indexOf("http://", 0);
		if (friendlinkName == null || friendlinkName.equals("")) {
			tip = "网站名称不能为空！";
			flag = false;
		} else if (friendlinkaddress == null || friendlinkaddress.equals("")) {
			tip = "网站地址不能为空！";
			flag = false;
		} else if (b == -1) {
			tip = "网站地址请按照下列格式书写：http://wap.joycool.net";
			flag = false;
		} else if (friendlinkService.chenkLinkRecordName("name='"
				+ StringUtil.toSql(friendlinkName) + "'")) {
			tip = "您申请的网站名称已经存在，请输入其他名称！";
			flag = false;
		}

		if (flag == false) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", tip);
			return;
		} else {
			int linktypeid = 13;
			int linkid = friendlinkService.getLinkIdHuangYe();
			if (linkid == 0) {
				linkid = 40000;
			}
			friendlinkService.InsertLinkRecord(linkid, friendlinkName,
					friendlinkaddress, linktypeid);
			LinkRecordBean linkrecord = friendlinkService
					.getLinkRecord("name='" + StringUtil.toSql(friendlinkName) + "'");
			request.setAttribute("linkrecord", linkrecord);
			request.setAttribute("result", "success");
		}
	}

	public void hyViews(HttpServletRequest request, int mark) {

		int perPageRecords = 25;
		request.setAttribute("PERPAGERECORDS", perPageRecords + "");

		int records = huangyeService.getHuangYeRecords(mark);
		request.setAttribute("RECORDS", records + "");

		int pages = (records + (perPageRecords - 1)) / perPageRecords;
		request.setAttribute("PAGES", pages + "");

		String pageNo = request.getParameter("pageNo");
		if (pageNo == null || pageNo.trim().length() < 1) {
			pageNo = "1";
		} else if (Integer.parseInt(pageNo) > pages) {
			pageNo = pages + "";
		} else if (Integer.parseInt(pageNo) < 1) {
			pageNo = "1";
		}
		request.setAttribute("PAGENO", pageNo);

		Vector huangyeList = huangyeService.getHuangYeList(Integer
				.parseInt(pageNo), perPageRecords, mark);
		request.setAttribute("HUANGYELIST", huangyeList);

		int maxNum = huangyeService.getHYMaxNum(mark);
		request.setAttribute("MAXNUM", maxNum + "");

		return;

	}

	public static String wenZiFenYeInWeb(int totalPages, int currentPageNo,
			String prefixURL) {

		StringBuffer sb = new StringBuffer();

		if (totalPages == 1)
			return "";
		if (currentPageNo == 1) {
			sb.append("首页&nbsp;&nbsp;");
			sb.append("上一页&nbsp;&nbsp;");
		} else {
			sb.append("<a href=\"" + prefixURL + "pageNo=" + 1
					+ "\">首页</a>&nbsp;&nbsp;");
			sb.append("<a href=\"" + prefixURL + "pageNo="
					+ (currentPageNo - 1) + "\">上一页</a>&nbsp;&nbsp;");
		}

		if (currentPageNo == totalPages) {
			sb.append("下一页&nbsp;&nbsp;");
			sb.append("尾页");
		} else {
			sb.append("<a href=\"" + prefixURL + "pageNo="
					+ (currentPageNo + 1) + "\">下一页</a>&nbsp;&nbsp;");
			sb.append("<a href=\"" + prefixURL + "pageNo=" + totalPages
					+ "\">尾页</a>");
		}

		sb
				.append("<form method=\"get\" action=\""
						+ prefixURL.substring(0, prefixURL.length() - 1)
						+ "\">第<input name=\"pageNo\" type=\"text\"  size=\"1\" onKeyPress=\"if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;\"/>页<input type=\"submit\" name=\"submit\" value=\"GO\" /></form>");

		return sb.toString();
	}

	public void addHuangYe(HttpServletRequest request) {
		JCLinkHuangYeBean hy = new JCLinkHuangYeBean();

		int number = StringUtil.toInt(request.getParameter("number"));
		int linkId = StringUtil.toInt(request.getParameter("linkId"));
		int mark = StringUtil.toInt(request.getParameter("mark"));
		hy.setNumber(number);
		hy.setLinkId(linkId);
		hy.setMark(mark);

		if(number==-1||linkId==-1||mark==-1)
		{
			request.setAttribute("validate","您输入的信息有误，请重新输入！");
			return;
		}
		
		if (huangyeService.numExist(hy.getNumber(), hy.getMark()) != null) {
			request.setAttribute("NUMEXIST", "您输入的序号已经存在！");
			return;
		}
		if (!huangyeService.linkIdExist("link_record", "WHERE link_id="+hy.getLinkId())) {
			request.setAttribute("LINKIDEXIST", "您输入的link_id不存在！");
			return;
		}
		if (huangyeService.linkIdExist("jc_link_huangye","WHERE link_id="+hy.getLinkId()+" and mark=" + mark)) {
			request.setAttribute("LINKIDEXIST", "您输入的link_id在表中重复！");
			return;
		}

		huangyeService.addHY(hy);
		request.setAttribute("SUCCESS", "您的数据已经成功增加！");
		return;
	}

	public void alterHuangYeView(HttpServletRequest request) {
		String id = request.getParameter("id");
		Vector hyList = huangyeService.getJCLinkHuangYeList("id=" + id);
		JCLinkHuangYeBean hy = (JCLinkHuangYeBean) hyList.get(0);
		request.setAttribute("HUANGYE", hy);
		return;
	}
	//对huangye信息进行修改
	public void alterHuangYe(HttpServletRequest request) {

		JCLinkHuangYeBean hy = new JCLinkHuangYeBean();

		int id =StringUtil.toInt(request.getParameter("id"));
		int number = StringUtil.toInt(request.getParameter("number"));
		int oldNum = StringUtil.toInt(request.getParameter("oldNum"));
		int linkId = StringUtil.toInt(request.getParameter("linkId"));
		int oldLinkId = StringUtil.toInt(request.getParameter("oldLinkId"));
		int mark = StringUtil.toInt(request.getParameter("mark"));
		int oldMark = StringUtil.toInt(request.getParameter("oldMark"));
		hy.setId(id);
		hy.setNumber(number);
		hy.setLinkId(linkId);
		hy.setMark(mark);
		//对输入信息正确性进行验证
		if(number==-1||linkId==-1||mark==-1)
		{
			request.setAttribute("validate","您输入的信息有误，请重新输入！");
			return;
		}
		// 对number是否重复进行验证
//		if (hy.getNumber() != oldNum || hy.getMark() != oldMark) {
//			JCLinkHuangYeBean chy = huangyeService.numExist(hy.getNumber(), hy
//					.getMark());
//			if (chy != null) {
//				chy.setNumber(oldNum);
//				chy.setMark(oldMark);
//				if (huangyeService.changNumber(hy, chy)) {
//					request.setAttribute("SUCCESS", "您的数据已经交换成功！");
//					return;
//				} else {
//					request.setAttribute("FAILURE", "您的数据交换过程出现问题，请重试！");
//					return;
//				}
//			} else {
//				huangyeService.alterHY(hy);
//				request.setAttribute("SUCCESS", "您的数据已经修改成功！");
//				return;
//			}
//		}
		//linkId唯一性验证
		if (!huangyeService.linkIdExist("link_record", "WHERE link_id="+hy.getLinkId())) {
			request.setAttribute("LINKIDEXIST", "您输入的link_id不存在！");
			return;
		}
		
		if (linkId!=oldLinkId) {
			if (huangyeService.linkIdExist("jc_link_huangye","WHERE link_id="+hy.getLinkId()+" and mark=" + mark)) {
				request.setAttribute("LINKIDEXIST", "您输入的link_id在表中重复！");
				return;
			}
		}
		
		if (huangyeService.alterHY(hy)) {
			request.setAttribute("SUCCESS", "您的数据已经修改成功！");
			return;
		} else {
			request.setAttribute("FAILURE", "您的数据修改过程出现问题，请重试！");
			return;
		}

	}

	public void deleteHY(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (huangyeService.deleteHY(Integer.parseInt(id))) {
			request.setAttribute("SUCCESS", "记录已经成功删除！");
		} else {
			request.setAttribute("FAILURE", "记录删除过程出现问题，请重试！");
		}
		return;
	}
	
}