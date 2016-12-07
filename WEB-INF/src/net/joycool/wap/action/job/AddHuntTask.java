/*
 * 
 *
 */
package net.joycool.wap.action.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.job.HuntTaskBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author zhul 2006-06-21 增加新打猎猎物， 页面addHuntTask.jsp调用此类，功能：检查用户输入的信息并保存
 *         成功后提示用户提交成功，完成此流程
 */
public class AddHuntTask extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IJobService jobService = ServiceFactory.createJobService();
		String tip = null;
		/**
		 * 取得参数
		 */
		DynaActionForm quarryForm = (DynaActionForm) form;
		String startDay = quarryForm.getString("startDay");
		String startHour = quarryForm.getString("startHour");
		String endDay = quarryForm.getString("endDay");
		String endHour = quarryForm.getString("endHour");
		String name = quarryForm.getString("name");
		int price = StringUtil.toInt(quarryForm.getString("price"));
		int harmPrice = StringUtil.toInt(quarryForm.getString("harmPrice"));
		String hitPointS = quarryForm.getString("hitPoint");
		int arrow = StringUtil.toInt(quarryForm.getString("arrow"));
		int handGun = StringUtil.toInt(quarryForm.getString("handGun"));
		int huntGun = StringUtil.toInt(quarryForm.getString("huntGun"));
		int ak47 = StringUtil.toInt(quarryForm.getString("ak47"));
		int awp = StringUtil.toInt(quarryForm.getString("awp"));
		String notice = quarryForm.getString("notice");
		FormFile file = (FormFile) quarryForm.get("image");
		int hitPoint = StringUtil.toInt(hitPointS);
		// 活动开始、结束时间
		String startTime = startDay + " " + startHour + ":00:00";
		String endTime = endDay + " " + endHour + ":00:00";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date ds = sdf.parse(startTime);
		Date de = sdf.parse(endTime);

		/**
		 * 检查参数，如果信息格式有误直接跳回用户输入页面，并提示用户！
		 */
		if (ds.after(de)) {
			tip = "活动开始时间晚于活动结束时间!!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (name == null || name.equals("")) {
			tip = "猎物名称不能为空！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (price < 0) {
			tip = "您输入的猎物价值有误!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (harmPrice < 0) {
			tip = "您输入的咬伤损失值有误!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (hitPointS.indexOf('-') != -1
				&& hitPointS.lastIndexOf('-') != 0) {
			tip = "您输入的打中经验值有误!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (arrow < 0) {
			tip = "您输入的对于弓箭动物出现的机率有误!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (handGun < 0) {
			tip = "您输入的对于手枪动物出现的机率有误!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (huntGun < 0) {
			tip = "您输入的对于猎枪动物出现的机率有误!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (ak47 < 0) {
			tip = "您输入的对于ak47动物出现的机率有误!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}else if (awp < 0) {
			tip = "您输入的对于ak47动物出现的机率有误!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (notice == null || notice.equals("")) {
			tip = "活动通知不能为空!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (file == null || file.getFileSize() == 0) {
			tip = "请上传图片！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 检查文件大小
		int fileSize = file.getFileSize();
		if (fileSize == 0 || fileSize > Constants.MAX_ATTACH_SIZE) {
			tip = "文件不能为空，或者文件大小太大！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 检查扩展名
		String fileExt = StringUtil.convertNull(
				FileUtil.getFileExt(file.getFileName())).toLowerCase();
		if (fileExt.equals("") || Constants.ATTACTH_TYPES.indexOf(fileExt) == -1) {
			tip = "文件类型不正确！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 上传文件
		String filePath = Constants.HUNT_IMAGE_PATH;
		String fileURL = file.getFileName();
		if (!FileUtil.uploadFile(file, filePath, fileURL)) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 将新猎物加入数据库
		HuntTaskBean task = new HuntTaskBean();
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setQuarryName(name);
		task.setPrice(price);
		task.setHarmPrice(harmPrice);
		task.setHitPoint(hitPoint);
		task.setImage(fileURL);
		task.setArrow(arrow);
		task.setHandGun(handGun);
		task.setHuntGun(huntGun);
		task.setAk47(ak47);
		task.setAwp(awp);
		task.setNotice(notice);
		if (!jobService.addHuntTask(task)) {
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		request.setAttribute("result", "success");
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
