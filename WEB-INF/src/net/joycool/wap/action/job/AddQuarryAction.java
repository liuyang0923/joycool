/*
 * 
 *
 */
package net.joycool.wap.action.job;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.job.HuntQuarryAppearRateBean;
import net.joycool.wap.bean.job.HuntQuarryBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author zhul 2006-06-21 增加新猎物， 页面addQuarry.jsp调用此类，功能：检查用户输入的信息并保存
 *         成功后提示用户提交成功，完成此流程
 */
public class AddQuarryAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IJobService jobService = ServiceFactory.createJobService();
		String tip = null;
		/**
		 * 取得参数
		 */
		DynaActionForm quarryForm = (DynaActionForm) form;
		String name = quarryForm.getString("name");
		int price = StringUtil.toInt(quarryForm.getString("price"));
		int harmPrice = StringUtil.toInt(quarryForm.getString("harmPrice"));
		String hitPointS = quarryForm.getString("hitPoint");
		int arrow = StringUtil.toInt(quarryForm.getString("arrow"));
		int handGun = StringUtil.toInt(quarryForm.getString("handGun"));
		int huntGun = StringUtil.toInt(quarryForm.getString("huntGun"));
		int ak47 = StringUtil.toInt(quarryForm.getString("ak47"));
		int awp = StringUtil.toInt(quarryForm.getString("awp"));
		FormFile file = (FormFile) quarryForm.get("image");
		int hitPoint = StringUtil.toInt(hitPointS);
		// 判断此猎物是否已经存在
		HuntQuarryBean quarry = null;
		quarry = jobService.getHuntQuarry("name='" + StringUtil.toSql(name) + "'");
		/**
		 * 检查参数，如果信息格式有误直接跳回用户输入页面，并提示用户！
		 */
		if (name == null || name.equals("")) {
			tip = "猎物名称不能为空！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (quarry != null) {
			tip = "您输入的猎物已经存在!";
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
		} else if (hitPointS.indexOf('-') != -1 && hitPointS.indexOf('-') != 0) {
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
			tip = "您输入的对于awp动物出现的机率有误!";
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
		if (!FileUtil.uploadFiles(file, filePath, fileURL)) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 将新猎物加入数据库
		quarry = new HuntQuarryBean();
		quarry.setName(name);
		quarry.setPrice(price);
		quarry.setHarmPrice(harmPrice);
		quarry.setHitPoint(hitPoint);
		quarry.setImage(fileURL);
		if (!jobService.addHuntQuarry(quarry)) {
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 获取猎物id
		quarry = jobService.getHuntQuarry("name='" + StringUtil.toSql(name) + "'");
		// 将猎物对于各种武器出现的机率加入jc_hunt_quarry_appear_rate
		HuntQuarryAppearRateBean appearRate = new HuntQuarryAppearRateBean();
		appearRate.setWeaponId(Constants.ARROW);
		appearRate.setQuarryId(quarry.getId());
		appearRate.setAppearRate(arrow);
		if (!jobService.addHuntQuarryAppearRate(appearRate)) {
			request.setAttribute("tip", "数据库操作有误!检查数据库!");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		appearRate.setWeaponId(Constants.HANDGUN);
		appearRate.setQuarryId(quarry.getId());
		appearRate.setAppearRate(handGun);
		if (!jobService.addHuntQuarryAppearRate(appearRate)) {
			request.setAttribute("tip", "数据库操作有误!检查数据库!");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		appearRate.setWeaponId(Constants.HUNTGUN);
		appearRate.setQuarryId(quarry.getId());
		appearRate.setAppearRate(huntGun);
		if (!jobService.addHuntQuarryAppearRate(appearRate)) {
			request.setAttribute("tip", "数据库操作有误!检查数据库!");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		appearRate.setWeaponId(Constants.AK47);
		appearRate.setQuarryId(quarry.getId());
		appearRate.setAppearRate(ak47);
		if (!jobService.addHuntQuarryAppearRate(appearRate)) {
			request.setAttribute("tip", "数据库操作有误!检查数据库!");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		appearRate.setWeaponId(Constants.AWP);
		appearRate.setQuarryId(quarry.getId());
		appearRate.setAppearRate(awp);
		if (!jobService.addHuntQuarryAppearRate(appearRate)) {
			request.setAttribute("tip", "数据库操作有误!检查数据库!");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 更新打猎内存
		LoadResource resource = new LoadResource();
		resource.clearArrowMap();
		resource.clearHandGunMap();
		resource.clearHuntGunMap();
		resource.clearAk47Map();
		resource.clearQuarryMap();
		resource.clearWeaponMap();
		resource.clearAWPMap();
		request.setAttribute("result", "success");
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
