/*
 * Created on 2005-12-6
 *
 */
package net.joycool.wap.action.ebook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.ebook.EBookBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IEBookService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class EBookReadAction extends Action {

	public static final int PAGE_NUM = Constants.NEWS_WORD_PER_PAGE;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String address = request.getParameter("address");
		int ebookId = StringUtil.toInt(request.getParameter("ebookId"));
		if (ebookId < 0) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		// 取得当前电子书及其所属类别
		IEBookService ebookService = ServiceFactory.createEBookService();
		String condition = "id = " + ebookId;
		EBookBean ebook = ebookService.getEBook(condition);
		if (ebook == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}

		String realAddress = Constants.EBOOK_FILE_URL + ebook.getFileUrl()
				+ "/txt/" + address;

		String rootId = request.getParameter("rootId");

		int pageIndex = 0;
		if (request.getParameter("pageIndex") != null) {
			pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		} else if (request.getParameter("pageIndex1") != null) {
			pageIndex = StringUtil.toInt(request.getParameter("pageIndex1")) - 1;
			if (pageIndex < 0) {
				pageIndex = 0;
			}
		}
		String contents = null;
		try {	// 读写文件可能出错
			contents = this.getContent(realAddress);
		} catch(Exception e) {}
		if (contents == null)
			return mapping.findForward("systemFailure");
		int totalLength = contents.length();
		int totalPageCount = totalLength / PAGE_NUM;
		if (totalLength % PAGE_NUM != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}

		// 分页相关
		String prefixUrl = "EBookRead.do?address=" + address + "&amp;ebookId="
				+ ebookId + "&amp;rootId=" + rootId;
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("rootId", rootId);
		request.setAttribute("ebook", ebook);
		int count = pageIndex * PAGE_NUM;
		if(count < 0)
			count = 0;
		if(count >= totalLength)
			request.setAttribute("contents", "");
		else
			request.setAttribute("contents", contents.substring(count,
				((count + PAGE_NUM) < totalLength) ? count + PAGE_NUM : totalLength));

		return mapping.findForward("success");
	}

	private String getContent(String filePath) throws Exception {
		StringBuffer contents = new StringBuffer();

		InputStreamReader ebook = new InputStreamReader(new FileInputStream(
				filePath), "UTF-8");
		BufferedReader br = new BufferedReader(ebook);
		String chars = br.readLine();
		while (chars != null) {
			contents.append(chars);
			chars = br.readLine();
		}
		br.close();
		ebook.close();
		return contents.toString();
	}

}
