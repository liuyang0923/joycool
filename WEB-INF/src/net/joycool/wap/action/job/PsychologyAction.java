package net.joycool.wap.action.job;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.job.PsychologyAnswerBean;
import net.joycool.wap.bean.job.PsychologyBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.PageUtil;
import net.joycool.wap.util.StringUtil;

public class PsychologyAction extends BaseAction {
	IJobService jobService = null;

	int PAGE_SIZE = 10;

	public PsychologyAction() {
		jobService = ServiceFactory.createJobService();
	}

	/**
	 * fanys 2006-08-09 /job/psychology/index.jsp
	 * 
	 * @param request
	 * @param response
	 */
	public void psychology(HttpServletRequest request,
			HttpServletResponse response) {
		int pageIndex = 0;
		int pageNo = 0;// 就是用户输入的页号
		int pageCount = 0;
		int itemCount = 0;
		Vector psychologyList = null;
		String strWhere = " 1=1 ";
		if (request.getParameter("pageIndex") != null) {
			pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		}
		if (null != request.getParameter("pageNo")) {
			pageNo = StringUtil.toInt(request.getParameter("pageNo"));
		}
		if (pageNo > 0) {
			pageIndex = pageNo - 1;
		}
		itemCount = jobService.getPsychologyCount(strWhere);
		pageCount = itemCount / PAGE_SIZE;
		if ((itemCount % PAGE_SIZE) > 0)
			pageCount++;
		pageIndex = Math.min(pageIndex, pageCount-1);
		pageIndex = Math.max(0, pageIndex);
		psychologyList = jobService.getPsychologyList(strWhere + " limit "
				+ pageIndex * PAGE_SIZE + "," + PAGE_SIZE);
		String pagination = PageUtil.shuzifenye(pageCount, pageIndex,
				"/job/psychology/index.jsp", false,
				" ", response);
		request.setAttribute("pagination", pagination);
		request.setAttribute("psychologyList", psychologyList);
		request.setAttribute("pageIndex", pageIndex + "");
	}

	/**
	 * /job/psychology/question.jsp
	 * 
	 * @param request
	 * @param response
	 */
	public void question(HttpServletRequest request,
			HttpServletResponse response) {
		int psychologyId = 1;
		PsychologyBean psychology = null;
		Vector answerList = null;
		if (null != request.getParameter("id"))
			psychologyId = StringUtil.toInt(request.getParameter("id"));
		answerList = jobService.getPsychologyAnswerList("psychology_id="
				+ psychologyId);
		psychology = jobService.getPsychology("id=" + psychologyId);
		request.setAttribute("psychology", psychology);
		request.setAttribute("answerList", answerList);
	}

	/**
	 * /job/psychology/result.jsp
	 * 
	 * @param request
	 * @param response
	 */
	public void answer(HttpServletRequest request, HttpServletResponse response) {
		int answerId = 1;
		int nextPsychologyId = 1;
		Vector vecNext3Questions = null;
		if (null != request.getParameter("answerId")) {
			answerId = StringUtil.toInt(request.getParameter("answerId"));
		}
		PsychologyAnswerBean answer = jobService.getPsychologyAnswer("id="
				+ answerId);
		PsychologyBean nextPsychology = jobService.getPsychology("id>"
				+ answer.getPsychologyId() + " order by id limit 0,1");
		if (null != nextPsychology)
			nextPsychologyId = nextPsychology.getId();
		else {
			nextPsychology = jobService
					.getPsychology(" 1=1 order by id limit 0,1");
			nextPsychologyId = nextPsychology.getId();
		}
		vecNext3Questions = jobService.getPsychologyList("id>"
				+ nextPsychologyId + " order by id limit 0,3");
		int size = vecNext3Questions.size();

		if (size >= 0 && size < 3) {
			Vector vecTemp = null;
			vecTemp = jobService.getPsychologyList("1=1 order by id limit 0,"
					+ (3 - size));
			if (vecTemp.size() > 0) {
				for (int i = 0; i < vecTemp.size(); i++) {
					vecNext3Questions.add(vecTemp.get(i));
				}
			}
		}
		request.setAttribute("nextPsychologyId", nextPsychologyId + "");
		request.setAttribute("answer", answer);
		request.setAttribute("next3Questions", vecNext3Questions);

	}

}
